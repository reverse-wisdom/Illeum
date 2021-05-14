<template>
  <div>
    <div v-if="lecturerReady == true">
      <div v-for="(item, idx) in classLi" :key="idx">
        <div v-if="item.room_state == '진행'">
          <div class="blog-card">
            <input type="radio" name="select" id="tap-1" checked />
            <input type="checkbox" id="imgTap" />

            <div class="inner-part">
              <label for="imgTap" class="img">
                <!-- founderUid -->
                <v-img :src="url" id="preview" alt="" style="width:240px; height:240px;"></v-img>
              </label>
              <div class="content content-1">
                <span>클래스명:{{ item.room_name }}</span>

                <div class="title">강의자:{{ $store.state.name }}</div>
                <div class="text">진행단계:{{ item.room_state }}</div>
                <div class="text">공개여부:{{ item.room_type }}</div>
                <div class="text">시작시간:{{ item.start_time }}</div>
                <div class="text">종료시간:{{ item.end_time }}</div>
                <v-btn class="button" x-large style=" padding: 20px 30px; font-size: 1.2rem; margin-left: 200px;" color="#6173FF" @click="startRTC(item)">강의시작</v-btn>
              </div>
            </div>
          </div>
          <!-- <h3>{{ item.rid }}</h3>
          <h3>{{ $store.state.name }}</h3>
          <h3>{{ item.room_name }}</h3>
          <h3>{{ item.room_state }}</h3>
          <h3>{{ item.room_type }}</h3>
          <h3>{{ item.start_time }}</h3>
          <v-img :src="url" id="preview" alt="" style="width:100px; height:100px; left:45%;"></v-img> -->
        </div>
      </div>
    </div>
    <div v-else>
      <p>강의진행중인 강좌가 없음</p>
    </div>
  </div>
</template>

<script>
import { userClasslist } from '@/api/auth.js';
export default {
  data() {
    return {
      lecturerReady: false,
      classLi: [],
      url: null,
    };
  },
  async created() {
    this.url = '/profile/' + this.$store.state.uuid + '/256';
    const { data } = await userClasslist(this.$store.state.uuid);
    console.log(data);
    if (data) {
      this.lecturerReady = true;
      this.classLi = data;
    } else {
    }
  },
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.blog-card {
  display: grid;
  /* position: absolute; */
  height: 270px;
  width: 75%;
  max-width: 650px;
  margin: 20px auto;
  border-radius: 25px;
  background: white;
  box-shadow: 0px 5px 10px rgba(0, 0, 0, 0.5);
}
.inner-part {
  position: absolute;
  display: flex;
  height: 280px;
  align-items: center;
  justify-content: center;
  padding: 0 25px;
}
#imgTap:checked ~ .inner-part {
  padding: 0;
  transition: 0.1s ease-in;
}
.inner-part .img {
  height: 220px;
  width: 220px;
  flex-shrink: 0;
  overflow: hidden;
  border-radius: 20px;
  box-shadow: 2px 3px 15px rgba(0, 0, 0, 0.1);
}
#imgTap:checked ~ .inner-part .img {
  height: 370px;
  width: 850px;
  z-index: 99;
  margin-top: 10px;
  transition: 0.3s 0.2s ease-in;
}
.img img {
  height: 100%;
  width: 100%;
  object-fit: cover;
  cursor: pointer;
  opacity: 0;
  transition: 0.6s;
}
#tap-1:checked ~ .inner-part .img-1 {
  opacity: 1;
  transition-delay: 0.2s;
}
.content {
  padding: 0 10px 0 10px;
  width: 380px;
  margin-left: 40px;

  transition: 0.6s;
}
#imgTap:checked ~ .inner-part .content {
  display: none;
}
#tap-1:checked ~ .inner-part .content-1 {
  opacity: 1;
  margin-left: 20px;
  z-index: 100;
  transition-delay: 0.3s;
}
.content span {
  display: block;
  color: #6173ff;
  margin-bottom: 10px;
  font-size: 22px;
  font-weight: 900;
}
.content .title {
  font-size: 30px;
  font-weight: 700;
  color: #0d0925;
  margin-bottom: 10px;
}
.content .text {
  color: #4e4a67;
  font-size: 15px;
  margin-bottom: 10px;
  line-height: 1.2em;
  text-align: justify;
  overflow: hidden;
}
.content .button {
  display: inline-flex;
  padding: 35px 40px;
  font-size: 16px;
  text-transform: uppercase;
  color: #fff0e6;
  font-weight: 600;
  letter-spacing: 1px;
  border-radius: 50px;
  cursor: pointer;
  outline: none;
  border: 1px solid #1976d2;
  background: #1976d2;
}
.content .button:hover {
  background: #1976d2;
}

input[type='radio'],
input[type='checkbox'] {
  display: none;
}
</style>
