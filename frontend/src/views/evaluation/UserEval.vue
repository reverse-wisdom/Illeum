<template>
  <div class="user-eval">
    <v-row>
      <p>출결확인을 위해 1.날짜를 체크하고 2.클레스를 선택해주세요</p>
    </v-row>
    <v-row justify="center">
      <v-date-picker
        v-model="date"
        width="400"
        @click:date="classNameFetch"
        :landscape="landscape"
        locale="ko-kr"
        color="#FF625C"
        :allowed-dates="allowedDates"
        class="mt-4"
        min="1900-04-01"
        max="2100-10-30"
      ></v-date-picker>

      <v-col cols="12" sm="6">
        <label for="" style=" border: 1px solid #756BFF; padding:10px 20px;  font-size:1.2rem; color:#fff; background:#756BFF;">클래스선택</label>
        <v-select style="margin-top: 2%;" :items="items" :label="date" solo @input="showEvaluation"></v-select>
      </v-col>
    </v-row>
    <v-row
      style="display: flex;
    justify-content: center;"
    >
      <div v-if="evalcheck" style="margin-top: 3%; display:flex; flex-direction: row;  flex-wrap: wrap; justify-content: flex-start; justify-content: space-around;">
        <div v-if="attendchk" style="margin-right:10vw;">
          <div class="label-eval">개인데이터</div>
          <EvalPieChart :learnData="learnData" :key="change" />
        </div>
        <div v-else>
          <h3 style="">결석한 수업입니다. 평균데이터만 확인할 수 있습니다.</h3>
        </div>
        <div>
          <div class="label-eval">해당클래스의 평균 및 개인 평가데이터</div>
          <EvalRadarChart :learnData="learnData" :averageData="averageData" :key="renderKey" />
        </div>
      </div>
    </v-row>
  </div>
</template>

<script>
import { userEvalList } from '@/api/auth';
import { fetchRoomname, evaluateList } from '@/api/class';
import EvalPieChart from '@/views/components/EvalPieChart';
import EvalRadarChart from '@/views/components/EvalRadarChart';
export default {
  components: {
    EvalPieChart,
    EvalRadarChart,
  },

  data() {
    return {
      uuid: this.$store.state.uuid,
      eval: [],
      items: [],
      date: '',
      landscape: true,
      menu: false,
      arrayDates: [],
      roomName: [],
      evalcheck: false,
      averageData: [
        {
          data: '집중',
          per: 0,
        },
        {
          data: '딴짓',
          per: 0,
        },
        {
          data: '졸음',
          per: 0,
        },
        {
          data: '자리비움',
          per: 0,
        },
        {
          data: '참여도',
          per: 0,
        },
      ],

      learnData: [
        {
          data: '집중',
          per: 0,
        },
        {
          data: '딴짓',
          per: 0,
        },
        {
          data: '졸음',
          per: 0,
        },
        {
          data: '자리비움',
          per: 0,
        },
        {
          data: '참여도',
          per: 0,
        },
      ],
      per1: 0,
      per2: 0,
      per3: 0,
      per4: 0,
      per5: 0,
      change: 0,
      renderKey: 1,
      attendchk: false,
      choiceClass: '',
    };
  },

  async created() {
    const { data } = await userEvalList(this.uuid);
    console.log(data);
    for (var i = 0; i < data.length; i++) {
      var eval_date = data[i].eval_date.slice(0, 10);
      this.arrayDates.push(eval_date);
    }
    this.eval = data;
    this.items = [];
    this.evalcheck = false;
    this.attendchk = false;
  },
  methods: {
    classNameFetch() {
      this.items = [];
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
    async showEvaluation(selected) {
      this.per1 = 0;
      this.per2 = 0;
      this.per3 = 0;
      this.per4 = 0;
      this.per5 = 0;
      this.attendchk = false;
      this.learnData = [
        {
          data: '집중',
          per: 0,
        },
        {
          data: '딴짓',
          per: 0,
        },
        {
          data: '졸음',
          per: 0,
        },
        {
          data: '자리비움',
          per: 0,
        },
        {
          data: '참여도',
          per: 0,
        },
      ];
      this.averageData = [
        {
          data: '집중',
          per: 0,
        },
        {
          data: '딴짓',
          per: 0,
        },
        {
          data: '졸음',
          per: 0,
        },
        {
          data: '자리비움',
          per: 0,
        },
        {
          data: '참여도',
          per: 0,
        },
      ];
      const { data } = await fetchRoomname(selected);
      const roomPartinUser = data[0].rid;
      const res = await evaluateList(roomPartinUser);

      for (var j = 0; j < res.data.length; j++) {
        this.per1 += res.data[j].attention;
        this.per2 += res.data[j].distracted;
        this.per3 += res.data[j].asleep;
        this.per4 += res.data[j].afk;
        this.per5 += res.data[j].participation;
      }
      // 소수둘째자리에서 반올림해서 소수첫째자리까지 보여줌
      this.averageData[0].per = (this.per1 / res.data.length).toFixed(1);
      this.averageData[1].per = (this.per2 / res.data.length).toFixed(1);
      this.averageData[2].per = (this.per3 / res.data.length).toFixed(1);
      this.averageData[3].per = (this.per4 / res.data.length).toFixed(1);
      this.averageData[4].per = (this.per5 / res.data.length).toFixed(1);
      for (var i = 0; i < this.eval.length; i++) {
        if (this.eval[i].attend != '결석' && this.date == this.eval[i].eval_date.slice(0, 10)) {
          console.log(this.eval[i].attend);
          console.log(this.eval[i].eval_date.slice(0, 10));
          console.log(this.date);
          this.evalcheck = true;
          this.attendchk = true;
          if (this.eval[i].room_name == selected && this.date == this.eval[i].eval_date.slice(0, 10)) {
            this.learnData[0].per = this.eval[i].attention;
            this.learnData[1].per = this.eval[i].distracted;
            this.learnData[2].per = this.eval[i].asleep;
            this.learnData[3].per = this.eval[i].afk;
            this.learnData[4].per = this.eval[i].participation;
            console.log(this.learnData);
          }
          break;
        } else {
          this.attendchk = false;
        }
        this.change++;
        this.renderKey++;
      }
    },
  },
};
</script>

<style scoped>
.user-eval {
  margin: 3% 2%;
  background: #f9f9f9;
}
.label-eval {
  border: 1px solid #41ea93;
  background: #41ea93;
  color: #fff;
  padding: 0.5rem 0rem;
}
</style>
