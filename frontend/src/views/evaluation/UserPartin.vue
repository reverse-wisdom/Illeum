<template>
  <div>
    <span>{{ this.$store.state.uuid }}의 출결목록</span>
    <v-row justify="center">
      <v-date-picker v-model="date" @click:date="classNameFetch" :allowed-dates="allowedDates" class="mt-4" min="1900-04-01" max="2100-10-30"></v-date-picker>
      <v-col class="d-flex" cols="12" sm="6">
        <v-select :items="items" :label="date" solo @change="showPartin"></v-select>
      </v-col>
    </v-row>
    <v-row></v-row>

    <div>
      <h2>show participation</h2>
      <h2>{{ date }}</h2>
    </div>

    <div>
      <p>
        출석왕
        <span>{{ firstUser }}</span>
      </p>
      <p>
        채팅참여왕
        <span>{{ maxUser }}</span>
      </p>
    </div>
    <div>
      <div>{{ this.$store.state.name }}님</div>
      <div>{{ selectedRoomName }} 수업에서 총 수강생 {{ fetchRoomlen }}명중에 {{ partuidRank }}위입니다</div>
    </div>
  </div>
</template>

<script>
import { userEvalList } from '@/api/auth';
import { fetchRoomname, evaluateList } from '@/api/class';
import EvalPieChart from '@/views/components/EvalPieChart';
import EvalRadarchart from '@/views/components/EvalRadarchart';
export default {
  components: {
    EvalPieChart,
    EvalRadarchart,
  },

  data() {
    return {
      uuid: this.$store.state.uuid,
      name: '',
      eval: [],
      items: [],
      date: '',
      menu: false,
      mindate: '2021-04-01',
      maxdate: '2021-04-30',

      partinRank: [],
      partuidRank: '',
      attendRank: [],
      attenduidRank: '',
      arrayDates: [],
      roomName: [],
      partData: [],
      attendData: [],
      selectedRoomName: '',
      maxUser: '',
      firstUser: '',
      fetchRoomlen: 0,
      evalcheck: false,
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
    allowedDates(val) {
      var arrayDates = this.arrayDates;
      arrayDates.indexOf(val) !== -1;
    },
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
      // console.log(selected);
      this.evalcheck = true;
      this.partinRank = [];
      this.fetchRoomlen = 0;
      for (var i = 0; i < this.eval.length; i++) {
        if (this.eval[i].room_name === selected) {
          this.selectedRoomName = selected;
          const { data } = await fetchRoomname(this.eval[i].room_name);
          const roomPartinUser = data[0].rid;
          const res = await evaluateList(roomPartinUser);

          var maxPartin = 0;
          var first = 100000;
          for (var j = 0; j < res.data.length; j++) {
            this.partinRank.push({ uid: res.data[j].uid, participation: res.data[j].participation });
            if (maxPartin < res.data[j].participation) {
              var maxPartin = res.data[j].participation;
              this.maxUser = res.data[j].name;
            }

            if (first > res.data[j].ranking) {
              var first = res.data[j].ranking;
              this.firstUser = res.data[j].name;
            }
            if (this.$store.state.uuid === res.data[j].uid) {
              this.name = this.$store.state.name;
              this.fetchRoomlen = res.data.length;
            }
          }

          console.log(this.partinRank);

          this.partinRank.sort(function(a, b) {
            if (a.participation < b.participation) {
              return 1;
            }
            if (a.participation > b.participation) {
              return -1;
            }

            return 0;
          });
          console.log(this.partinRank);
          //로그인한 청강자 순위 구하기
          for (var k = 0; k < this.partinRank.length; k++) {
            // console.log(this.partinRank.length);
            console.log(this.partinRank[k].uid);
            console.log(this.$store.state.uuid);
            if (this.partinRank[k].uid == this.$store.state.uuid) {
              console.log(this.partinRank[k].uid);
              console.log(this.$store.state.uuid);
              this.partuidRank = this.partinRank.indexOf(this.partinRank[k]) + 1;
            }
          }
          console.log(this.partuidRank);
        }
      }
      // console.log(maxPartin, this.maxUser);
      // console.log(first, this.firstUser);
      this.change++;
      this.renderKey++;
    },
  },
};
</script>
