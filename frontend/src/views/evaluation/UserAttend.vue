<template>
  <div>
    <span>{{ this.$store.state.uuid }}의출결현황</span>
    <v-row justify="center">
      <v-date-picker
        v-model="date"
        width="500"
        @click:date="classNameFetch"
        :landscape="landscape"
        locale="ko-kr"
        :allowed-dates="allowedDates"
        class="mt-4"
        min="1900-04-01"
        max="2100-10-30"
      ></v-date-picker>
      <v-col cols="12" sm="6">
        <v-select :items="items" :label="date" solo @change="showPartin"></v-select>
      </v-col>
    </v-row>
    <div style="margin-top: 100px;">
      <h2>show attendence</h2>
      <h2>eval_date:{{ date }}</h2>
      <h2>{{ dayLabel }}</h2>
    </div>
    <div>
      <p>
        출석왕
        <span>{{ firstUser }}</span>
      </p>
    </div>
    <div>
      <div>유저이름:{{ this.$store.state.name }}님</div>
      <div>출석시간:{{ attenduidTime }}</div>
      <div>지각여부:{{ attend }}</div>
    </div>
  </div>
</template>

<script>
import { userEvalList } from '@/api/auth';
import { fetchRoomname, evaluateList, findByRidClass } from '@/api/class';

export default {
  data() {
    return {
      uuid: this.$store.state.uuid,
      name: '',
      eval: [],
      items: [],
      week: new Array('일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'),
      landscape: true,
      date: '',
      menu: false,
      mindate: '2021-04-01',
      maxdate: '2021-04-30',
      lateCheck: false,
      attend: '',
      saveTime: '',
      partinRank: [],
      partuidRank: '',
      attendRank: [],
      attenduidTime: '',
      arrayDates: [],
      roomName: [],
      partData: [],
      attendData: [],
      selectedRoomName: '',
      maxUser: '',
      firstUser: '',
      fetchRoomlen: 0,
      evalcheck: false,
      selected: true,
    };
  },
  // props: {
  //   date: {
  //     type: String,
  //   },
  //   items: {
  //     type: [],
  //   },
  // },
  computed: {
    dayLabel: function(date) {
      return this.week[new Date(this.date).getDay()];
    },
  },
  async created() {
    // const uuid = this.$store.state.uuid;
    // console.log(this.uuid);
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
      this.roomName = [];
      for (var i = 0; i < this.eval.length; i++) {
        if (this.eval[i].eval_date.slice(0, 10) === this.date && !this.roomName.includes(this.eval[i].room_name)) {
          this.roomName.push(this.eval[i].room_name);
        }
      }

      this.items = this.roomName;
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
      this.saveTime = '';
      for (var i = 0; i < this.eval.length; i++) {
        if (this.eval[i].room_name === selected) {
          this.selectedRoomName = selected;
          const { data } = await fetchRoomname(this.eval[i].room_name);
          const roomPartinUser = data[0].rid;
          const res = await evaluateList(roomPartinUser);
          const ridData = await findByRidClass(roomPartinUser);
          this.saveTime = this.$moment(ridData.data.startTime).format('YYYY-MM-DD, h:mm:ss a');
          console.log(ridData.data.startTime);
          this.saveTime = this.$moment(this.saveTime).add('m', 10);
          this.saveTime = this.$moment(this.saveTime).format('YYYY-MM-DD, h:mm:ss a');
          console.log(this.saveTime);
          //채팅참여도1등, 출석1등 구하기
          console.log(res.data);
          for (var j = 0; j < res.data.length; j++) {
            this.partinRank.push({ uid: res.data[j].uid, participation: res.data[j].participation });
            // attendRank push 메소드 시간순으로 정렬됨
            this.attendRank.push({ uid: res.data[j].uid, attend_time: res.data[j].attend_time });
            console.log(this.attendRank);
            this.attendRank.sort(function(a, b) {
              return a.attend_time < b.attend_time ? -1 : a.attend_time > b.attend_time ? 1 : 0;
            });
            // if (maxPartin < res.data[j].participation) {
            //   var maxPartin = res.data[j].participation;
            //   this.maxUser = res.data[j].name;
            // }

            // if (first > res.data[j].ranking) {
            //   var first = res.data[j].ranking;
            //   this.firstUser = res.data[j].name;
            // }
            // if (this.$store.state.uuid === res.data[j].uid) {
            //   this.name = this.$store.state.name;
            //   this.fetchRoomlen = res.data.length;
            // }
          }
          //채팅참여도 배열 제일 많은순으로 정렬하기
          // this.partinRank.sort(function(a, b) {
          //   if (a.participation < b.participation) {
          //     return 1;
          //   }
          //   if (a.participation > b.participation) {
          //     return -1;
          //   }

          //   return 0;
          // });

          //로그인한 청강자 채팅참여도 순위 구하기
          // for (var k = 0; k < this.partinRank.length; k++) {
          //   if (this.partinRank[k].uid === this.$store.state.uuid) {
          //     this.partuidRank = this.partinRank.indexOf(this.partinRank[k]) + 1;
          //   }
          // }
          //로그인한 청강자 출석시간 순위 구하기
          console.log(this.attendRank);
          for (var m = 0; m < this.attendRank.length; m++) {
            // var test1 = this.$moment(this.attendRank[0].attend_time).format('YYYY-MM-DD, h:mm:ss a');

            if (this.attendRank[m].uid === this.$store.state.uuid) {
              this.attenduidTime = this.$moment(this.attendRank[m].attend_time).format('h:mm:ss A');
              if (this.attenduidTime < this.saveTime) {
                this.lateCheck = true;
                this.attend = '지각';
              } else {
                this.attend = '정상';
              }
            }
          }
        }
      }

      this.change++;
      this.renderKey++;
    },
  },
};
</script>

<style></style>
