<template>
  <div>
    <span>{{ this.$store.state.uuid }}의 출석/채팅참여</span>
    <v-row justify="center">
      <v-date-picker v-model="date" @click:date="classNameFetch" :landscape="landscape" locale="ko-kr" :allowed-dates="allowedDates" class="mt-4" min="1900-04-01" max="2100-10-30"></v-date-picker>
      <v-col class="d-flex" cols="12" sm="6">
        <v-select :items="items" :label="date" solo @change="showPartin"></v-select>
      </v-col>
    </v-row>

    <div style="margin-top: 100px;">
      <h2>show participation</h2>
      <h2>{{ date }}</h2>
    </div>

    <div>
      <p>
        출석왕
        <span>이름:{{ attendFirst }}</span>
        <v-img :src="'https://k4d106.p.ssafy.io/profile/' + attendFirstUid + '/256'" id="preview" alt="" style="width:100px; height:100px; left:45%;"></v-img>
      </p>
      <p>
        채팅참여왕
        <span>이름{{ partinFirst }}</span>
        <v-img :src="'https://k4d106.p.ssafy.io/profile/' + partinFirstUid + '/256'" id="preview" alt="" style="width:100px; height:100px; left:45%;"></v-img>
      </p>
    </div>
    <div>
      <div>{{ this.$store.state.name }}님</div>
      <div>{{ selectedRoomName }} 수업에서 수업참여도</div>
      <div>총 수강생 {{ fetchRoomlen }}명중에 {{ UserPartinRank }}위입니다</div>
      <div>{{ selectedRoomName }} 수업에서 출석을</div>
      <div>총 수강생 {{ fetchRoomlen }}명중에 {{ UserAttendRank }}위입니다</div>
    </div>
  </div>
</template>

<script>
import { userEvalList } from '@/api/auth';
import { fetchRoomname, evaluateList } from '@/api/class';

export default {
  data() {
    return {
      uuid: this.$store.state.uuid,
      name: '',
      eval: [],
      items: [],
      landscape: true,
      date: '',
      menu: false,

      attendFirst: '',
      attendFirstUid: '',
      attendData: [],

      partinFirst: '',
      partinFirstUid: '',
      partData: [],

      UserPartinRank: '',
      UserAttendRank: '',

      arrayDates: [],

      selectedRoomName: '',
      fetchRoomlen: 0,
      evalcheck: false,
    };
  },

  async created() {
    const { data } = await userEvalList(this.uuid);
    for (var i = 0; i < data.length; i++) {
      var eval_date = data[i].eval_date.slice(0, 10);
      this.arrayDates.push(eval_date);
    }
    this.eval = data;
    this.items = [];
    this.evalcheck = false;
  },
  methods: {
    classNameFetch() {
      for (var i = 0; i < this.eval.length; i++) {
        if (this.eval[i].eval_date.slice(0, 10) === this.date && !this.items.includes(this.eval[i].room_name)) {
          this.items.push(this.eval[i].room_name);
        }
      }
    },
    allowedDates(val) {
      for (var i = 0; i < this.arrayDates.length; i++) {
        if (this.arrayDates[i] == val) {
          return true;
        }
      }
    },
    async showPartin(selected) {
      this.evalcheck = true;
      this.partinRank = [];
      this.attendRank = [];
      this.fetchRoomlen = 0;
      for (var i = 0; i < this.eval.length; i++) {
        if (this.eval[i].room_name === selected) {
          this.selectedRoomName = selected;
          const { data } = await fetchRoomname(this.eval[i].room_name);
          const roomPartinUser = data[0].rid;
          const res = await evaluateList(roomPartinUser);
          var maxPartin = 0;
          this.fetchRoomlen = res.data.length;
          for (var j = 0; j < res.data.length; j++) {
            this.partData.push({ uid: res.data[j].uid, participation: res.data[j].participation });
            this.attendData.push({ uid: res.data[j].uid, attend_time: res.data[j].attend_time });
            //출석1등 청강생 구하기
            if (res.data[j].ranking === 1) {
              this.attendFirst = res.data[j].name;
              this.attendFirstUid = res.data[j].uid;
            }
            //채팅참여도 1위 청강생 구하기
            if (maxPartin < res.data[j].participation) {
              var maxPartin = res.data[j].participation;
              this.partinFirst = res.data[j].name;
              this.partinFirstUid = res.data[j].uid;
            }

            //로그인한 유저 출석 순위 구하기
            if (this.uuid == res.data[j].uid) {
              this.UserAttendRank = res.data[j].ranking;
            }
          }
          this.partinRank.sort(function(a, b) {
            if (a.participation < b.participation) {
              return 1;
            }
            if (a.participation > b.participation) {
              return -1;
            }

            return 0;
          });
          //로그인한 유저 채팅참여도 순위 구하기
          for (var k = 0; k < this.partData.length; k++) {
            if (this.partData[k].uid === this.$store.state.uuid) {
              this.UserPartinRank = this.partData.indexOf(this.partData[k]) + 1;
            }
          }
        }
      }
    },
  },
};
</script>
