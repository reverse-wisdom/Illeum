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
      <!-- <p>
        출석왕
        <span>{{ firstUser }}</span>
      </p> -->
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
          // this.saveTime._i = this.$moment(ridData.data.start_time).format('YYYY-MM-DD, h:mm:ss');
          console.log('수업시작', ridData.data.start_time);
          this.saveTime = this.$moment(ridData.data.start_time)
            .add(10, 'm')
            .format('YYYY-MM-DD, HH:mm:ss');
          console.log('+10분', this.saveTime);
          // this.saveTime = this.$moment(this.saveTime).format('YYYY-MM-DD, h:mm:ss');

          for (var j = 0; j < res.data.length; j++) {
            this.attendRank.push({ uid: res.data[j].uid, attend_time: res.data[j].attend_time });
            console.log(this.attendRank);
            this.attendRank.sort(function(a, b) {
              return a.attend_time < b.attend_time ? -1 : a.attend_time > b.attend_time ? 1 : 0;
            });
          }

          console.log(this.attendRank);
          for (var m = 0; m < this.attendRank.length; m++) {
            // var test1 = this.$moment(this.attendRank[0].attend_time).format('YYYY-MM-DD, h:mm:ss a');

            if (this.attendRank[m].uid === this.$store.state.uuid) {
              this.attenduidTime = this.$moment(this.attendRank[m].attend_time).format('YYYY-MM-DD, HH:mm:ss');
              console.log(this.attenduidTime);
              console.log(this.saveTime);
              if (this.attenduidTime > this.saveTime) {
                this.lateCheck = true;
                this.attend = '지각';
              } else if (this.attenduidTime == '' || this.attenduidTime == undefined || this.attenduidTime == null) {
                this.attend = '결석';
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
