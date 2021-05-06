<template>
  <div>
    <h2>화상수업 리스트 목록</h2>
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
          <v-btn color="info" @click="startWebRTC(item)">수업 생성</v-btn>
        </template>
        <template v-else>
          <v-btn color="success" @click="joinWebRTC(item)">수업 참여</v-btn>
        </template>
      </template>
    </v-data-table>
    <v-btn @click="main">
      홈으로
    </v-btn>
  </div>
</template>
<script>
import { classAll, updateClass } from '@/api/class';
import { findUidAndRid } from '@/api/entrant';
import { start, entrance } from '@/api/rabbit';
import { insertEvaluation } from '@/api/evaluation';
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
    };
  },
  async created() {
    const { data } = await classAll();
    for (let index = 0; index < data.length; index++) {
      if (data[index].room_state != '종료') {
        this.rooms.push(data[index]);
      }
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
              const { data } = await updateClass({ rid: value.rid, room_state: '진행' }); // PUT: /api/room/updateByRid
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
          const { data } = await updateClass({ rid: value.rid, room_state: '진행' }); // PUT: /api/room/updateByRid
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
    async openOrClose(value) {
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
            if (value.uid == this.$store.state.uuid) {
              try {
                const { data } = await updateClass({ rid: value.rid, room_state: '진행' });
                if (data == 'success') this.$router.push({ name: 'ClassMaster', query: { room_name: value.room_name, rid: value.rid } });
              } catch {
                this.$swal({
                  icon: 'error',
                  title: '화상수업 생성 오류.!!',
                });
              }
            } else {
              this.$router.push({ name: 'Class', query: { room_name: value.room_name, rid: value.rid } });
            }
          }
        }
      } else {
        if (value.uid == this.$store.state.uuid) {
          try {
            const { data } = await updateClass({ rid: value.rid, room_state: '진행' });
            if (data == 'success') this.$router.push({ name: 'ClassMaster', query: { room_name: value.room_name, rid: value.rid } });
          } catch {
            this.$swal({
              icon: 'error',
              title: '화상수업 생성 오류.!!',
            });
          }
        } else {
          this.$router.push({ name: 'Class', query: { room_name: value.room_name, rid: value.rid } });
        }
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
