<template>
  <div class="user-attend">
    <v-row>
      <p>출결확인을 위해 1.날짜를 체크하고 2.클레스를 선택해주세요</p>
    </v-row>
    <v-row>
      <v-col cols="12" sm="4">
        <v-date-picker
          v-model="date"
          width="350"
          header-color="#000"
          @click:date="classNameFetch"
          locale="ko-kr"
          :allowed-dates="allowedDates"
          class="mt-4"
          min="1900-04-01"
          max="2100-10-30"
        ></v-date-picker>
      </v-col>

      <v-col cols="12" sm="4">
        <div style="margin-top:1rem;">
          <label for="" style="border: 1px solid black; padding:10px 20px; font-size:1.2rem; color:#fff; background:#000;">클래스선택</label>
          <div style=" margin-top: 20px;">
            <v-select :items="items" :label="date" solo @input="showPartin" placeholder="클래스를 선택해주세요"></v-select>
          </div>
        </div>
        <div class="attend-info" style=" ">
          <div class="attend-time-detail" style="display:flex; flex-direction:row;">
            <div class="title-attend">
              <v-icon x-large>mdi-alarm</v-icon>
            </div>
            <div class="label-attend">출석시간</div>
          </div>
          <div>{{ attend_time }}</div>

          <div class="attend-check" style="display:flex; flex-direction:row;">
            <v-icon x-large>
              mdi-calendar-check
            </v-icon>
            <div class="label-attend">지각여부</div>
          </div>
          <div :class="{ normal: attend == '정상', late: attend == '지각', afk: attend == '결석' }">
            {{ attend }}
          </div>
        </div>
      </v-col>
      <v-col cols="12" sm="4">
        <div>
          <div class="clock-info">
            <div class="clock">
              <Clock></Clock>
            </div>

            <section class="time-contain">
              <p class="attend-time time-shadow" v-text="currentTime"></p>
            </section>
          </div>
          <div class="title-saying">오늘의 명언</div>
          <div class="saying">
            <p>{{ randomSaying }}</p>
          </div>
        </div>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import { userEvalList } from '@/api/auth';
import Clock from '@/views/components/Clock.vue';
export default {
  components: {
    Clock,
  },
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
      attend_time: '',
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
      // digital clock
      currentTime: null,
      //명언
      timeSaying: [
        '당신은 지체할 수도 있지만 시간은 그러하지 않을 것이다.',
        '계획을 세우는 데 소비한 시간이 길어질수록 실행에 옮기는 시간이 줄어든다.',
        '과거를 기억 못하는 이들은 과거를 반복하기 마련이다.',
        '짧은 인생은 시간낭비에 의해 더욱 짧아진다.',
        '미래를 신뢰하지 마라, 죽은 과거는 묻어버려라, 그리고 살아있는 현재에 행동하라.',
        '승자는 시간을 관리하며 살고, 패자는 시간에 끌려 산다.',
      ],
      randomSaying: '',
    };
  },

  computed: {
    dayLabel: function(date) {
      return this.week[new Date(this.date).getDay()];
    },
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
    //random saying
    this.randomSaying = this.timeSaying[Math.floor(Math.random() * this.timeSaying.length)];

    // digital clock
    this.currentTime = this.$moment().format('LTS');
    setInterval(() => this.updateCurrentTime(), 1 * 1000);
  },
  methods: {
    updateCurrentTime() {
      this.currentTime = this.$moment().format('LTS');
    },

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
    async showPartin(selected) {
      this.evalcheck = true;
      this.partinRank = [];
      this.attendRank = [];
      this.fetchRoomlen = 0;
      this.saveTime = '';
      this.attend_time = '';
      this.attend = '';
      for (var i = 0; i < this.eval.length; i++) {
        if (this.eval[i].room_name == selected && this.date == this.eval[i].eval_date.slice(0, 10)) {
          console.log(this.date);
          console.log(this.eval[i].eval_date.slice(0, 10));
          this.attend_time = this.eval[i].attend_time;
          this.attend = this.eval[i].attend;
          break;
        }
      }
    },
  },
};
</script>

<style scoped>
.user-attend {
  margin: 3% 1%;
  background: #f4f4f4;
}

section.time-contain {
  display: flex;
}

.attend-time {
  color: #000;
  font-size: 3em;
}

h3.is-3:not(:last-child) {
  margin: 0;
  padding: 0;
}

.time-shadow {
  text-shadow: 0 0 15px rgba(100, 100, 100, 0.35);
}
.user-attend .saying {
  border: 3px solid black;
  border-radius: 10px;
  width: 80%;
  padding: 3rem 2rem 1rem 2rem;
  font-weight: 900;

  /* margin-bottom: 1rem; */
}
.title-saying {
  border: 0px solid black;
  width: 39.3vh;
  margin: auto;
  color: #fff;
  padding: 0.2rem 2rem;
  background: #000;
  position: absolute;
  z-index: 2;
}
.user-attend .clock-info {
  display: flex;
  flex-direction: column;
  justify-items: center;
  align-items: center;
  margin-left: -5rem;
}
.attend-info {
  display: flex;
  flex-direction: column;
  border: 3px solid #000;

  font-weight: 900;
  padding: 1rem 1rem;
  margin: 2rem 1rem;
}
.attend-check {
  margin-top: 5%;
}
.late {
  background: #ff9c6c;
  border: 0px solid black;
  border-radius: 10px;
  width: 30%;
  height: 60%;
  padding: 10px 10px;
  margin-left: 35%;
  font-size: 1.2rem;
}
.afk {
  background: #fc5230;
  border: 0px solid black;
  border-radius: 10px;
  width: 30%;
  height: 60%;
  padding: 10px 10px;
  margin-left: 35%;
  font-size: 1.2rem;
}
.normal {
  background: #3a7de2;
  border: 0px solid black;
  border-radius: 10px;
  font-size: 1.2rem;
  width: 30%;
  height: 60%;
  padding: 10px 10px;
  margin-left: 35%;
}
/* .attend-time-detail .title-attend {
  border: 2px solid #f4f4f4;
  border-radius: 10px;


  font-weight: bold;
  width: 40%;
  color: #000;
  font-size: 1.2rem;
  height: 40%;
  letter-spacing: 2px;
  padding: 10px 0px 10px 0px;
} */
.label-attend {
  font-size: 1.5rem;
  font-weight: bold;
}
</style>
