<template>
  <div class="user-partin">
    <v-row>
      <p>
        출결확인을 위해
        <strong style="letter-spacing:1px">①날짜를 체크하고 ②클래스를 선택</strong>
        해주세요.
      </p>
    </v-row>
    <v-row justify="center">
      <div style="margin-right: 5%">
        <div style="border: 1px solid ##2E95FF; background: #FF625C ; border-radius: 50px; color:white; padding:0.5rem; width:70%; margin:1rem auto;  font-size:1rem; margin-bottom: 10%;">
          <v-icon color="white">mdi-alarm-check</v-icon>
          출석왕
        </div>

        <v-avatar class="mb-3" color="grey darken-1" size="200">
          <v-img :src="'/profile/' + attendFirstUid + '/256'" id="preview" alt=""></v-img>
        </v-avatar>
        <p style="font-size:1.7rem; font-weight:bold;">
          {{ attendFirst }}
          <span style="font-size:1.2rem;">교육생</span>
        </p>
      </div>
      <div>
        <div style="border: 1px solid ##2E95FF; background: #FF625C ; border-radius: 50px; color:white; padding:0.5rem; width:70%; margin:1rem auto;  font-size:1rem; margin-bottom: 10%;">
          <v-icon color="white">mdi-forum-outline</v-icon>
          채팅참여왕
        </div>
        <div>
          <v-avatar class="mb-3" color="grey darken-1" size="200">
            <v-img :src="'/profile/' + partinFirstUid + '/256'" id="preview" alt=""></v-img>
          </v-avatar>
          <p style="font-size:1.7rem; font-weight:bold;">
            {{ partinFirst }}
            <span style="font-size:1.2rem;">교육생</span>
          </p>
        </div>
      </div>

      <v-col cols="12" sm="6">
        <v-date-picker
          width="300"
          color="#FF625C"
          v-model="date"
          @click:date="classNameFetch"
          :landscape="landscape"
          :allowed-dates="allowedDates"
          :weekday-format="getDay"
          :month-format="getMonth"
          :header-date-format="headerDate"
          :title-date-format="titleDate"
          class="mt-4"
          min="1900-04-01"
          max="2100-10-30"
        ></v-date-picker>

        <v-col style="margin:auto;" cols="12" sm="10">
          <v-select :items="items" :label="date" solo @input="showPartin" style="max-width:100%; "></v-select>
        </v-col>
      </v-col>
    </v-row>

    <!-- <h2>{{ date }}</h2> -->

    <div v-if="uidcheck" style="display:flex; flex-direction:column;">
      <v-alert v-if="UserAttendRank != 1000" style="width:90%; margin:auto;" color="#756BFF" border="left" dark>
        해당 수업의 총 수강생 {{ fetchRoomlen }}명중에 출석순위는
        <span style="font-size:1.5rem;">{{ UserAttendRank }}위</span>
        입니다
      </v-alert>
      <v-alert v-else border="left" style="width:90%; margin:auto;" color="#756BFF" dark>수업에 참여하지 않았습니다.</v-alert>
      <v-alert v-if="zeroPartinchk == false" border="left" style="width:90%; margin: 2% auto; " color="#41EA93" dark>
        해당 수업의 총 수강생 {{ fetchRoomlen }}명중에 채팅참여도는
        <span style="font-size:1.5rem;">{{ UserPartinRank }}위</span>
        입니다
      </v-alert>
      <v-alert v-else border="left" style="width:90%; margin: 2% auto;" color="#41EA93" dark>채팅에 참여하지 않았습니다.</v-alert>
    </div>
  </div>
</template>

<script>
import { userEvalList } from '@/api/auth';
import { fetchRoomname, evaluateList } from '@/api/class';
import { findCount } from '@/api/entrant';

const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];
const monthsOfYear = ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'];

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
      zeroPartinchk: false,
      uidcheck: false,
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
    titleDate(date) {
      return monthsOfYear[new Date(date).getMonth(date)] + ' ' + new Date(date).getDate(date) + '일 <br/>' + daysOfWeek[new Date(date).getDay(date)] + '요일';
    },
    getDay(date) {
      return daysOfWeek[new Date(date).getDay(date)];
    },
    getMonth(date) {
      return monthsOfYear[new Date(date).getMonth(date)];
    },
    headerDate(date) {
      return new Date(date).getFullYear(date) + ' ' + monthsOfYear[new Date(date).getMonth(date)];
    },
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
      this.partData = [];
      this.attendData = [];
      this.fetchRoomlen = 0;
      this.zeroPartinchk = false;
      this.UserAttendRank = '';
      for (var i = 0; i < this.eval.length; i++) {
        if (this.eval[i].room_name === selected && this.date == this.eval[i].eval_date.slice(0, 10)) {
          this.selectedRoomName = selected;
          const { data } = await fetchRoomname(this.eval[i].room_name);
          const roomPartinUser = data[0].rid;
          const res = await evaluateList(roomPartinUser);
          const res2 = await findCount(roomPartinUser);

          var maxPartin = 0;
          // this.fetchRoomlen = res.data.length;
          for (var j = 0; j < res.data.length; j++) {
            // 해당 클래스 전체수강생 인원수 구하기 & 배열만들기
            if (res.data[j].eval_date.slice(0, 10) === this.date) {
              this.partData.push({ uid: res.data[j].uid, participation: res.data[j].participation });
              this.attendData.push({ uid: res.data[j].uid, attend_time: res.data[j].attend_time });
              this.fetchRoomlen = res2.data;
              //로그인한 유저 참여도 데이터 있는지 없는지 체크하고 조건부 렌더링 체크
              if (res.data[j].uid != this.$store.state.uuid) {
                this.uidcheck = true;
              }
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
          }
        }
      }
      console.log(this.partData);
      this.partData.sort(function(a, b) {
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
        if (this.partData[k].uid === this.$store.state.uuid && this.partData[k].participation != 0) {
          this.UserPartinRank = this.partData.indexOf(this.partData[k]) + 1;
          break;
        } else if (this.partData[k].uid === this.$store.state.uuid && this.partData[k].participation == 0) {
          this.zeroPartinchk = true;
        }
      }
      console.log(this.partData);
      console.log(this.fetchRoomlen);
      console.log(this.partinFirst);
      console.log(this.partinFirstUid);
    },
  },
};
</script>
<style scoped>
.user-partin {
  margin: 3% 2%;
  background: #f9f9f9;
}
</style>
