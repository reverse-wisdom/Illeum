package com.ssafy.pjt.Controller;

import java.net.URI;
import java.nio.file.Path;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.pjt.dto.UploadFile;
import com.ssafy.pjt.service.ImageService;
//import com.ssafy.pjt.provider.service.MemberService;
import com.ssafy.pjt.util.MediaUtils;
import com.ssafy.pjt.util.UploadFileUtils;
import com.ssafy.pjt.Repository.MemberRepository;
import com.ssafy.pjt.dto.Member;

/**
 * @author https://github.com/woobong/spring-boot-jpa-summernote-image-upload-example/
 */
@Controller
@RequestMapping("/api")
public class ImageController {

	@Autowired
	ImageService imageService;
    @Autowired
    private MemberRepository memberRepository;
	@Autowired
	ImageController self;

	/**
	 * 파일 업로드
	 * 
	 * @param file
	 * @return 고유URL주소
	 */
	@PostMapping({ "/data", "/image" })
	@ResponseBody
	public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
		try {
			UploadFile uploadedFile = imageService.store(file);
			return ResponseEntity.ok().body("/api/data/" + uploadedFile.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * 파일 다운로드
	 * 
	 * @param 파일고유번호
	 * 
	 * @return HTTP프로토콜처리된파일
	 */
	@GetMapping({ "/data/{fileId}", "/image/{fileId}" })
	@ResponseBody
	public ResponseEntity<?> serveFile(@PathVariable int fileId) {
		try {
			UploadFile uploadedFile = imageService.load(fileId);
			HttpHeaders headers = new HttpHeaders();

			String fileName = uploadedFile.getOriginFileName();
			headers.add(HttpHeaders.CONTENT_DISPOSITION,
					"attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");

			if (MediaUtils.containsImageMediaType(uploadedFile.getContentType())) {
				headers.setContentType(MediaType.valueOf(uploadedFile.getContentType()));
			} else {
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			}

			Resource resource = imageService.loadAsResource(uploadedFile.getSaveFileName());
			return ResponseEntity.ok().headers(headers).body(resource);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * 프로필 이미지 업로드
	 */
	@PostMapping({ "/member/image/update/{uuid}" })
	@ResponseBody
	public ResponseEntity<?> handleProfileImageUpload(@PathVariable int uid,
			@RequestParam("file") MultipartFile file) {
		try {
			UploadFile uploadedFile = imageService.store(file, "/member", String.valueOf(uid), true);
			Member member = memberRepository.findByUid(uid);
			member.setThumbnail(String.valueOf(uploadedFile.getId()));
			memberRepository.save(member);
			return ResponseEntity.ok().body("/api/image/" + uploadedFile.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e);
		}
	}

	@Autowired
	private boolean isWindows;

	/**
	 * 프로필 이미지 다운로드
	 * 
	 * @param 파일고유번호
	 * 
	 * @return HTTP프로토콜처리된파일
	 */
//	@Cacheable(value = "cacheProfileImage", key = "{ #uuid, #size.orElse(64) }")
	@GetMapping({ "/member/image/{uuid}", "/member/image/{uuid}/{size}" })
	@ResponseBody
	public ResponseEntity<?> serveProfileImage(@PathVariable int uid, @PathVariable Optional<Integer> size) {
		int imageSize = size.orElse(64);
		if (imageSize % 16 != 0 || imageSize / 256 > 1)
			imageSize = 64;
		try {
			Member member = memberRepository.findByUid(uid);
			if (member == null || member.getThumbnail() == null || member.getThumbnail().length() == 0) {
				return self.serveProfileDefaultImage(imageSize);
			}
			String ino = member.getThumbnail();
			if (ino.startsWith("http") || ino.chars().allMatch(Character::isDigit) == false) { // 숫자의 형태가 아니라면
				URI redirectUri = new URI(ino);
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setLocation(redirectUri);
				return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
			}
			UploadFile uploadedFile = imageService.load(Integer.parseInt(ino));

			HttpHeaders headers = new HttpHeaders();
			Resource resource = null;

			if ((isWindows && uploadedFile.getFilePath().toLowerCase().startsWith("c:"))
					|| (!isWindows && uploadedFile.getFilePath().startsWith("/"))) {
				String fileName = String.valueOf(uid); /* uploadedFile.getOriginFileName(); */
				headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
						+ new String(member.getName().getBytes("UTF-8"), "ISO-8859-1") + "\"");

				if (MediaUtils.containsImageMediaType(uploadedFile.getContentType())) {
					headers.setContentType(MediaType.valueOf(uploadedFile.getContentType()));
				} else {
					headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				}
				if (imageSize == 0)
					resource = imageService.loadAsResource(uploadedFile.getSaveFileName());
				else {
					String filename = UploadFileUtils.getThumbnailFileName(uploadedFile.getSaveFileName(), imageSize);
					resource = imageService.loadAsResource(filename);
				}
			} else {
				return self.serveProfileDefaultImage(imageSize);
			}
			return ResponseEntity.ok().headers(headers).body(resource);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e);
		}
	}

	@ResponseBody
	@Cacheable(value = "defaultImage", key = "#imageSize")
	public ResponseEntity<?> serveProfileDefaultImage(int imageSize) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"default\"");
			headers.setContentType(MediaType.IMAGE_PNG);
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//			Resource[] resources = resolver.getResources("classpath*:static/*.png");
			Resource resource = resolver.getResource("classpath:static/user-" + imageSize + ".png");
//			Path file = ResourceUtils.getFile("classpath:static/user-" + imageSize + ".png").toPath();
//			Resource resource = new UrlResource(file.toUri());
			return ResponseEntity.ok().headers(headers).body(resource);
		} catch (Exception e) {
			return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
