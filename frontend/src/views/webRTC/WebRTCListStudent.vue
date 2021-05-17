<template>
  <div class="webRTCList">
    <v-alert
      class="text-start font-weight-black"
      border="left"
      dark
      id="alert"
      color="#2E95FF"
      style="margin-top:6%; font-size: 1.5rem; letter-spacing: 2.3px; padding-left:20px; line-height: 45px;"
      elevation="3"
      height="70"
    >
      <span>
        <v-icon>mdi-account-check</v-icon>
        화상수업리스트
      </span>
    </v-alert>
    <v-data-table
      :headers="headers"
      :items="rooms"
      :items-per-page="10"
      item-key="rid"
      class="elevation-1"
      :footer-props="{
        showFirstLastPage: true,
        firstIcon: 'mdi-arrow-collapse-left',
        lastIcon: 'mdi-arrow-collapse-right',
        prevIcon: 'mdi-minus',
        nextIcon: 'mdi-plus',
        'items-per-page-text': '페이지당 화상회의수',
      }"
    >
      <template v-slot:[`item.action`]="{ item }">
        <template v-if="checkUser(item) == '강의자'">
          <v-btn color="info" @click="startWebRTC(item)" :disabled="!hasWebcam">수업 생성</v-btn>
        </template>
        <template v-else-if="item.room_state == '진행'">
          <v-btn color="success" @click="joinWebRTC(item)" :disabled="!hasWebcam">수업 참여</v-btn>
        </template>
        <template v-else>
          <v-btn color="warning" disabled>준비중</v-btn>
        </template>
      </template>
    </v-data-table>
  </div>
</template>
<script>
import { classAll, updateClass } from '@/api/class';
import { usePartinClass } from '@/api/auth';
import { start } from '@/api/rabbit';
export default {
  data() {
    return {
      headers: [
        {
          text: '방제목',
          align: 'start',
          value: 'room_name',
        },
        { text: '시작시간', value: 'start_time' },
        { text: '진행/준비', value: 'room_state' },
        { text: '공개/비공개', value: 'room_type' },
        { text: '', value: 'action' },
      ],
      rooms: [],
      isChecked: false, // for camera check interval vue watch value
      time: null, // for camera check interval
      hasMicrophone: false,
      hasSpeakers: false,
      hasWebcam: false,
      isMicrophoneAlreadyCaptured: false,
      isWebcamAlreadyCaptured: false,
    };
  },
  watch: {
    isChecked: function() {
      var ref = this;
      setTimeout(function() {
        ref.time = setInterval(
          ref.checkDeviceSupport(ref, () => {
            console.log('사용 가능한 상태인지 체크 중');
            if (!ref.hasWebcam /*|| ref.isWebcamAlreadyCaptured*/) {
              console.log('카메라를 사용할 수 없어요.');
              //alert('카메라가 없거나 사용중 또는 웹의 권한이 없어 화상회의를 사용할 수 없는 상태입니다.');
              // alert('카메라가 인식되지 않습니다. 연결 상태를 확인해주세요.');
              this.$swal({
                icon: 'error',
                title: '카메라가 인식되지 않습니다. 연결 상태를 확인해주세요.!!',
              });
            }
          }),
          1500
        );
      }, 100);
    },
  },
  async created() {
    const result = await classAll();
    for (let index = 0; index < result.data.length; index++) {
      await usePartinClass(this.$store.state.uuid).then(async ({ data }) => {
        for (let index2 = 0; index2 < data.length; index2++) {
          if (result.data[index].room_state != '완료' && data[index2].rid == result.data[index].rid) {
            this.rooms.push(result.data[index]);
          }
        }
      });
    }
    var ref = this;
    navigator.mediaDevices
      .getUserMedia({ video: true })
      .then(function(stream) {
        ref.hasWebcam = true;
        stream.getTracks()[0].stop();
      })
      .catch((err) => {
        ref.$swal({
          icon: 'error',
          title: '웹캠이 존재하지 않거나<br/> 다른 프로그램에서 사용중입니다.!!',
        });
        ref.hasWebcam = false;
      });
  },
  mounted() {
    this.isChecked = true;
  },
  methods: {
    main() {
      this.$router.push({ name: 'Home' });
    },
    checkUser(value) {
      if (value.uid == this.$store.state.uuid) return '강의자';
      else return '참여자';
    },
    async startWebRTC(value) {
      if (value.room_type == '비공개') {
        const { value: room_password } = await this.$swal({
          icon: 'question',
          title: '비밀번호를 입력해 주세요',
          input: 'text',
          showCancelButton: true,
        });

        if (room_password != undefined) {
          if (room_password != value.room_password) {
            this.$swal({
              icon: 'error',
              title: '클래스 비밀번호가 일치하지 않습니다.!!',
            });
          } else {
            try {
              var start_time = new Date(new Date().toString().split('GMT')[0] + ' UTC').toISOString().split('.')[0] + '.000Z';
              const { data } = await updateClass({ rid: value.rid, room_state: '진행', start_time }); // PUT: /api/room/updateByRid
              if (data == 'success') {
                const { data } = await start(value.rid); // GET: /api/rtc/start (rabbitMQ)
                console.log(data);
                if (data == 'success') this.$router.push({ name: 'TeacherWebRTC', query: { room_name: value.room_name, rid: value.rid } });
              }
            } catch {
              this.$swal({
                icon: 'error',
                title: '화상수업 생성 오류.!!',
              });
            }
          }
        }
      } else {
        try {
          var start_time = new Date(new Date().toString().split('GMT')[0] + ' UTC').toISOString().split('.')[0] + '.000Z';
          const { data } = await updateClass({ rid: value.rid, room_state: '진행', start_time }); // PUT: /api/room/updateByRid
          if (data == 'success') {
            const { data } = await start(value.rid); // GET: /api/rtc/start (rabbitMQ)
            if (data == 'success') this.$router.push({ name: 'TeacherWebRTC', query: { room_name: value.room_name, rid: value.rid } });
          }
        } catch {
          this.$swal({
            icon: 'error',
            title: '화상수업 생성 오류.!!',
          });
        }
      }
    },
    async joinWebRTC(value) {
      if (value.room_type == '비공개') {
        const { value: room_password } = await this.$swal({
          icon: 'question',
          title: '비밀번호를 입력해 주세요',
          input: 'text',
          showCancelButton: true,
        });

        if (room_password != undefined) {
          if (room_password != value.room_password) {
            this.$swal({
              icon: 'error',
              title: '클래스 비밀번호가 일치하지 않습니다.!!',
            });
          } else {
            this.$router.push({ name: 'StudentWebRTC', query: { room_name: value.room_name, rid: value.rid } });
          }
        }
      } else {
        this.$router.push({ name: 'StudentWebRTC', query: { room_name: value.room_name, rid: value.rid } });
      }
    },
    checkDeviceSupport(ref, callback) {
      if (navigator.mediaDevices && navigator.mediaDevices.enumerateDevices) {
        // Firefox 38+ seems having support of enumerateDevicesx
        navigator.enumerateDevices = function(callback) {
          navigator.mediaDevices.enumerateDevices().then(callback);
        };
      }

      var MediaDevices = [];
      // var isHTTPs = location.protocol === 'https:';
      var canEnumerate = false;

      if (typeof MediaStreamTrack !== 'undefined' && 'getSources' in MediaStreamTrack) {
        canEnumerate = true;
      } else if (navigator.mediaDevices && !!navigator.mediaDevices.enumerateDevices) {
        canEnumerate = true;
      }
      if (!canEnumerate) {
        return;
      }

      if (!navigator.enumerateDevices && window.MediaStreamTrack && window.MediaStreamTrack.getSources) {
        navigator.enumerateDevices = window.MediaStreamTrack.getSources.bind(window.MediaStreamTrack);
      }

      if (!navigator.enumerateDevices && navigator.enumerateDevices) {
        navigator.enumerateDevices = navigator.enumerateDevices.bind(navigator);
      }

      if (!navigator.enumerateDevices) {
        if (callback) {
          callback();
        }
        return;
      }

      MediaDevices = [];
      navigator.enumerateDevices(function(devices) {
        devices.forEach(function(_device) {
          var device = {};
          for (var d in _device) {
            device[d] = _device[d];
          }

          if (device.kind === 'audio') {
            device.kind = 'audioinput';
          }

          if (device.kind === 'video') {
            device.kind = 'videoinput';
          }

          var skip;
          MediaDevices.forEach(function(d) {
            if (d.id === device.id && d.kind === device.kind) {
              skip = true;
            }
          });

          if (skip) {
            return;
          }

          if (!device.deviceId) {
            device.deviceId = device.id;
          }

          if (!device.id) {
            device.id = device.deviceId;
          }

          if (!device.label) {
            device.label = 'Please invoke getUserMedia once.';
            // if (!isHTTPs) {
            //   device.label = 'HTTPs is required to get label of this ' + device.kind + ' device.';
            // }
          } else {
            if (device.kind === 'videoinput' && !ref.isWebcamAlreadyCaptured) {
              ref.isWebcamAlreadyCaptured = true;
            }

            if (device.kind === 'audioinput' && !ref.isMicrophoneAlreadyCaptured) {
              ref.isMicrophoneAlreadyCaptured = true;
            }
          }

          if (device.kind === 'audioinput') {
            ref.hasMicrophone = true;
          }

          if (device.kind === 'audiooutput') {
            ref.hasSpeakers = true;
          }

          if (device.kind === 'videoinput') {
            ref.hasWebcam = true;
          }

          // there is no 'videoouput' in the spec.

          MediaDevices.push(device);
        });

        if (callback) {
          callback();
        }
      });
    },
  },
  destroyed() {
    this.isChecked = false;
    clearInterval(this.time);
  },
};
</script>
<style scoped>
.webRTCList {
  margin: 3% 2%;
  font-family: 'GongGothicLight';
}
</style>
