<template>
  <div>
    <span>{{ this.$store.state.uuid }}의 평가목록</span>
    <v-row justify="center">
      <v-date-picker v-model="date" @click:date="classNameFetch" :allowed-dates="allowedDates" class="mt-4" min="1900-04-01" max="2100-10-30"></v-date-picker>
      <v-col class="d-flex" cols="12" sm="6">
        <v-select :items="items" :label="date" solo @change="showEvaluation"></v-select>
      </v-col>
    </v-row>
    <v-row v-if="evalcheck">
      <h2>show evaluation</h2>
      <h2>{{ date }}</h2>
      <EvalPieChart :learnData="learnData" :key="change" />
    </v-row>
  </div>
</template>

<script>
import { userEvalList } from '@/api/auth';
import EvalPieChart from '@/views/components/EvalPieChart';
export default {
  components: {
    EvalPieChart,
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
      evalcheck: false,
      learnData: [
        {
          data: '집중',
          per: 12,
        },
        {
          data: '딴짓',
          per: 12,
        },
        {
          data: '졸음',
          per: 12,
        },
        {
          data: '자리비움',
          per: 12,
        },
      ],
      change: 0,
    };
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
    allowedDates(val) {
      var arrayDates = this.arrayDates;
      arrayDates.indexOf(val) !== -1;
    },
    classNameFetch() {
      this.roomName = [];
      for (var i = 0; i < this.eval.length; i++) {
        console.log(this.eval[i]);
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
    showEvaluation(selected) {
      // console.log(selected);
      for (var i = 0; i < this.eval.length; i++) {
        if (this.eval[i].room_name === selected) {
          this.evalcheck = true;
          this.learnData[0].per = this.eval[i].attention;
          this.learnData[1].per = this.eval[i].distracted;
          this.learnData[2].per = this.eval[i].asleep;
          this.learnData[3].per = this.eval[i].afk;
          // console.log(this.eval[i].attention, this.eval[i].distracted, this.eval[i].asleep, this.eval[i].afk);
        }
      }
      this.change++;
    },
  },
};
</script>

<style></style>
