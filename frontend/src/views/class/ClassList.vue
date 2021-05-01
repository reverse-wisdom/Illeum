<template>
  <div>
    <h2>클래스 리스트 목록</h2>
    <v-data-table
      :headers="headers"
      :items="rooms"
      :items-per-page="5"
      item-key="room_name"
      class="elevation-1"
      @click:row="detailClass"
      :footer-props="{
        showFirstLastPage: true,
        firstIcon: 'mdi-arrow-collapse-left',
        lastIcon: 'mdi-arrow-collapse-right',
        prevIcon: 'mdi-minus',
        nextIcon: 'mdi-plus',
        'items-per-page-text': '페이지당 클래스수',
      }"
    ></v-data-table>
  </div>
</template>
<script>
import { classAll, insertRoom } from '@/api/class';
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
        { text: '방상태', value: 'room_state' },
        { text: '공개/비공개', value: 'room_type' },
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
    async detailClass(value) {
      var uid = this.$store.state.uuid; // 지금 uid는 임시고정 값!!!!!
      var rid = value.rid;
      var insertInfo = { uid: uid, rid: rid };

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
              title: '방 비밀번호가 일치하지 않습니다.!!',
            });
          } else {
            if (value.uid == this.$store.state.uuid) {
              this.$router.push({ name: 'ClassMaster', query: { room_name: value.room_name } });
            } else {
              try {
                const { data } = await insertRoom(insertInfo);
                if (data != null) {
                  this.$router.push({ name: 'Class', query: { room_name: value.room_name } });
                }
              } catch {
                this.$swal({
                  icon: 'error',
                  title: '참여할 수 없는 방입니다.!!',
                });
              }
            }
          }
        }
      } else {
        try {
          const { data } = await insertRoom(insertInfo);
          if (data != null) {
            this.$router.push({ name: 'Class', query: { room_name: value.room_name } });
          }
        } catch {
          this.$swal({
            icon: 'error',
            title: '참여할 수 없는 방입니다.!!',
          });
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
