<template>
  <div>
    <h2>수강자-진행</h2>
    <div v-if="learnerDoing == true">
      <p>수강진행중인 강좌가 있음</p>
      <div v-for="(item, idx) in classLi" :key="idx">
        <div v-if="item.room_state == '진행'">
          <h3>RID:{{ item.rid }}</h3>
          <h3>강의자:{{ item.founder }}</h3>
          <h3>클래스명:{{ item.room_name }}</h3>

          <h3>공개여부:{{ item.room_type }}</h3>
          <h3>시작시간:{{ item.start_time }}</h3>
          <h3>종료시간:{{ item.end_time }}</h3>
          <v-btn color="primary" @click="attendClass(item)">수업참여</v-btn>
        </div>
      </div>
    </div>
    <div v-else>
      <p>수강진행중인 강좌가 없음</p>
    </div>
  </div>
</template>

<script>
import { usePartinClass } from '@/api/auth.js';
export default {
  data() {
    return {
      learnerDoing: false,
      classLi: [],
    };
  },
  async created() {
    const { data } = await usePartinClass(this.$store.state.uuid);
    console.log(data);
    if (data) {
      this.learnerDoing = true;
      this.classLi = data;
    }
  },
  mounted() {
    let cdn1 = document.createElement('script');
    cdn1.setAttribute('src', 'https://cdn.jsdelivr.net/npm/rtcmulticonnection@latest/dist/RTCMultiConnection.min.js');
    cdn1.setAttribute('id', 'cdn1');
    document.body.appendChild(cdn1);

    let cdn2 = document.createElement('script');
    cdn2.setAttribute('src', 'https://rtcmulticonnection.herokuapp.com/socket.io/socket.io.js');
    cdn2.setAttribute('id', 'cdn2');
    document.body.appendChild(cdn2);
  },
  methods: {
    async attendClass(value) {
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
  },
  destroyed() {
    // cdn 제거
    var el1 = document.querySelector('#cdn1');
    el1.remove();
    var el2 = document.querySelector('#cdn2');
    el2.remove();
  },
};
</script>

<style></style>
