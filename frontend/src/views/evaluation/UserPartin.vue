<template>
  <div>
    <span>{{ this.$store.state.uuid }}의 평가목록</span>
    <v-row justify="center">
      <v-date-picker v-model="date" @click:date="classNameFetch" :allowed-dates="allowedDates" class="mt-4" min="1900-04-01" max="2100-10-30"></v-date-picker>
      <v-col class="d-flex" cols="12" sm="6">
        <v-select :items="items" :label="date" solo @change="showPartin"></v-select>
      </v-col>
    </v-row>

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
      eval: [],
      items: [],
      date: '',
      menu: false,
      // date: '2018-03-02',
      mindate: '2021-04-01',
      maxdate: '2021-04-30',
      // arrayDates: ['2021-04-27', '2021-04-23'],
      arrayDates: [],
      roomName: [],
      partData: [],
      attendData: [],
      maxUser: '',
      firstUser: '',
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

      for (var i = 0; i < this.eval.length; i++) {
        if (this.eval[i].room_name === selected) {
          const { data } = await fetchRoomname(this.eval[i].room_name);
          const roomPartinUser = data[0].rid;
          const res = await evaluateList(roomPartinUser);
          var maxPartin = 0;
          var first = 100000;
          for (var j = 0; j < res.data.length; j++) {
            if (maxPartin < res.data[j].participation) {
              var maxPartin = res.data[j].participation;
              this.maxUser = res.data[j].name;
            }

            if (first > res.data[j].ranking) {
              var first = res.data[j].ranking;
              this.firstUser = res.data[j].name;
            }
          }
        }
      }
      // console.log('채팅참여왕', this.partData);
      // console.log('출석왕', this.attendData);
      console.log(maxPartin, this.maxUser);
      console.log(first, this.firstUser);
      this.change++;
      this.renderKey++;
    },
  },
};
</script>
