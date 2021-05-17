<template>
  <div class="manage">
    <v-alert
      class="text-start font-weight-black"
      border="left"
      dark
      id="alert"
      color="#2E95FF"
      style="margin-top:6%; font-size: 1.5rem; letter-spacing: 2.3px; padding-left:20px; line-height: 45px;"
      elevation="3"
      height="70"
    >
      <span>
        <v-icon>mdi-account-check</v-icon>
        {{ $store.state.name }}
      </span>
      님의 평가관리
    </v-alert>
    <v-row>
      <p>출결확인을 위해 1.날짜를 체크하고 2.클레스를 선택해주세요</p>
    </v-row>
    <v-row>
      <v-date-picker
        width="400"
        color="#FF625C"
        v-model="date"
        @click:date="classNameFetch"
        :landscape="true"
        locale="ko-kr"
        :allowed-dates="allowedDates"
        class="mt-4"
        min="1900-04-01"
        max="2100-10-30"
      ></v-date-picker>
      <v-col cols="6" sm="6">
        <label for="" style=" border: 1px solid #756BFF; padding:10px 20px;  font-size:1.2rem; color:#fff; background:#756BFF;">클래스선택</label>
        <v-select style="margin-top: 2%;" :items="roomNameList" :value="roomNameList[0]" solo @input="showAll"></v-select>
        <v-col cols="12">
          <v-card class="mx-auto" max-width="600">
            <v-toolbar flat color="">
              <v-toolbar-title>빠른검색</v-toolbar-title>
            </v-toolbar>

            <v-container class="py-0">
              <v-row align="center" justify="start">
                <v-col v-for="(selection, i) in selections" :key="selection.text" class="shrink">
                  <v-chip :disabled="loading" close @click:close="chipClose(selection, i)">
                    <v-icon left v-text="selection.icon"></v-icon>
                    {{ selection.text }}
                  </v-chip>
                </v-col>
              </v-row>
            </v-container>

            <v-divider v-if="!allSelected"></v-divider>

            <template v-for="item in categories">
              <v-list-item v-if="!selected.includes(item)" :key="item.text" :disabled="loading" @click="showSelected(item)">
                <v-list-item-avatar>
                  <v-icon :disabled="loading" v-text="item.icon"></v-icon>
                </v-list-item-avatar>
                <v-list-item-title v-text="item.text"></v-list-item-title>
              </v-list-item>
            </template>

            <!-- <v-divider></v-divider> -->
          </v-card>
        </v-col>
      </v-col>
    </v-row>
    <!-- <h2>{{ date }}</h2> -->

    <v-row>
      <div class="table-body">
        <div class="table_responsive">
          <table>
            <thead>
              <tr>
                <th scope="col">No</th>
                <th scope="col">PROFILE</th>
                <th scope="col">NAME</th>
                <th scope="col">E-MAIL</th>

                <th scope="col">date</th>
                <th scope="col">attend</th>
                <th scope="col">ranking</th>
                <th scope="col">participation</th>
                <th scope="col">모달</th>
              </tr>
            </thead>

            <tbody>
              <tr v-for="(each, idx) in userEval" :key="idx">
                <td>{{ idx + 1 }}</td>
                <td><v-img :src="`/profile/${each.uid}/256`" id="preview" style="width:50px; height:50px;" alt=""></v-img></td>
                <td>{{ each.name }}</td>
                <td>{{ each.email }}</td>

                <td>{{ each.attend_time }}</td>
                <td>{{ each.attend }}</td>
                <td>{{ each.ranking | checkChatRanking }}</td>
                <td>{{ each.participation }}회 채팅</td>
                <td><v-btn color="#756BFF" depressed dark @click="openModal(each)">평가조회</v-btn></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </v-row>

    <!-- modal -->
    <v-dialog v-if="$store.state.token" v-model="dialog" hide-overlay max-width="900px">
      <v-card flat>
        <v-toolbar flat color="#6173FF" dark>
          <v-toolbar-title>
            <span>{{ modalEach.name }}님의</span>
            평가조회
          </v-toolbar-title>
        </v-toolbar>
        <v-tabs vertical>
          <v-tab>
            <v-icon left>
              mdi-account
            </v-icon>
            출결
          </v-tab>
          <v-tab>
            <v-icon left>
              mdi-lock
            </v-icon>
            학습태도
          </v-tab>
          <v-tab>
            <v-icon left>
              mdi-access-point
            </v-icon>
            참여현황
          </v-tab>

          <v-tab-item>
            <v-card flat>
              <div style="margin:1.5rem 0;">
                <v-icon left x-large>
                  mdi-alarm
                </v-icon>
                <span style="font-size:2rem;">{{ modalEach.attend_time }}</span>
                에 출석하셨습니다!
              </div>
              <div>
                출결상태는
                <span style="font-size:2rem;">{{ modalEach.attend }}</span>
                입니다.
              </div>
            </v-card>
          </v-tab-item>
          <v-tab-item :key="modalEach.vid + 'A'">
            <v-card flat>
              <LecUserEval :each="modalEach" :rid="ridSelected"></LecUserEval>
            </v-card>
          </v-tab-item>
          <v-tab-item :key="modalEach.vid + 'B'">
            <v-card flat>
              <LecUserPartin :each="modalEach" :roomData="roomData" :evalUserCnt="userEvalLength" :rid="ridSelected"></LecUserPartin>
            </v-card>
          </v-tab-item>
        </v-tabs>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="dialog = false">
            Close
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import LecUserEval from '@/views/evaluation/LecUserEval';
import LecUserPartin from '@/views/evaluation/LecUserPartin';

import { fetchCondition } from '@/api/evaluation';
import { findByUidClass } from '@/api/class';
export default {
  components: {
    LecUserEval,
    LecUserPartin,
  },
  data() {
    return {
      chipCheck: {
        isLate: false,
        isAbsent: false,
        isAttendFirst: false,
        isChatFirst: false,
      },
      date: '',

      dateList: [], // rid date pair list
      classList: [], // for rid list
      roomNameList: [], // for select box list
      userEval: [], // for table list
      userEvalLength: '', // for table list length
      modalEach: {}, // for modal value

      userEvalAll: [],

      ridSelected: '',
      dialog: false,
      roomData: {},
      loading: false,
      selected: [],
      search: '',
      chips: [
        {
          text: '지각',
          icon: 'mdi-nature',
        },
        {
          text: '결석',
          icon: 'mdi-glass-wine',
        },
        {
          text: '출석1등',
          icon: 'mdi-calendar-range',
        },
        {
          text: '채팅참여1등',
          icon: 'mdi-bike',
        },
      ],
    };
  },
  computed: {
    allSelected() {
      return this.selected.length === this.chips.length;
    },
    categories() {
      const search = this.search.toLowerCase();

      if (!search) return this.chips;

      return this.chips.filter((item) => {
        const text = item.text.toLowerCase();

        return text.indexOf(search) > -1;
      });
    },
    selections() {
      const selections = [];

      for (const selection of this.selected) {
        selections.push(selection);
      }

      return selections;
    },
  },
  watch: {
    selected() {
      this.search = '';
    },
    userEval(val) {
      this.userEval = val;
    },
  },
  filters: {
    checkChatRanking: function(value) {
      if (value == 1000) return '채팅미참여';
      return value + '등';
    },
  },

  async created() {
    const token = this.$store.state.token;
    const uuid = this.$store.state.uuid;
    await findByUidClass(token, uuid).then(({ data }) => {
      for (let index = 0; index < data.length; index++) {
        this.classList.push(data[index]);
      }
    });
    for (let i = 0; i < this.classList.length; i++) {
      await fetchCondition({ rid: this.classList[i].rid }).then(({ data }) => {
        this.userEvalAll.push(data);
        for (let j = 0; j < data.length; j++) {
          if (!this.dateList.some((e) => e.date == data[j].attend_time.slice(0, 10))) {
            this.dateList.push({ date: data[j].eval_date.slice(0, 10), classInfo: this.classList[i], data });
          }
        }
      });
    }
  },
  methods: {
    openModal(each) {
      this.modalEach = each;
      this.dialog = true;
    },
    allowedDates(val) {
      for (var i = 0; i < this.dateList.length; i++) {
        if (this.dateList[i].date == val) {
          return true;
        }
      }
    },

    classNameFetch() {
      this.roomNameList = [];
      this.userEval = [];
      this.userEvalLength = '';
      this.ridSelected = '';
      this.selected = [];

      for (let index = 0; index < this.dateList.length; index++) {
        if (this.dateList[index].date == this.date) {
          this.roomNameList.push(this.dateList[index].classInfo.roomName);
          this.ridSelected = this.dateList[index].classInfo.rid;
        }
      }
    },

    showAll(roomName) {
      this.userEval = [];
      this.userEvalLength = '';
      this.roomData.room_name = roomName;
      for (let i = 0; i < this.dateList.length; i++) {
        if (this.dateList[i].classInfo.roomName == roomName) {
          if (this.dateList[i].date == this.date) {
            for (let j = 0; j < this.dateList[i].data.length; j++) {
              if (this.dateList[i].data[j].attend_time.slice(0, 10) == this.date) {
                this.userEval.push(this.dateList[i].data[j]);
              }
            }
          }
        }
      }
      this.userEvalLength = this.userEval.length;
    },
    async getList(chipCheck) {
      this.userEval = [];
      this.userEvalLength = '';
      var isAbsent = chipCheck.isAbsent ? 1 : 0;
      var isLate = chipCheck.isLate ? 1 : 0;
      var isAttendFirst = chipCheck.isAttendFirst ? 1 : 0;
      var isChatFirst = chipCheck.isChatFirst ? 1 : 0;
      await fetchCondition({ rid: this.ridSelected, isAbsent, isLate, isAttendFirst, isChatFirst }).then(({ data }) => {
        for (let i = 0; i < data.length; i++) {
          if (this.date == data[i].attend_time.slice(0, 10)) {
            this.userEval.push(data[i]);
          }
        }
      });
    },

    next() {
      this.loading = true;

      setTimeout(() => {
        this.search = '';
        this.selected = [];
        this.loading = false;
      }, 1000);
    },
    chipClose(selection, i) {
      this.selected.splice(i, 1);
      if (selection.text == '지각') this.chipCheck.isLate = false;
      if (selection.text == '결석') this.chipCheck.isAbsent = false;
      if (selection.text == '출석1등') this.chipCheck.isAttendFirst = false;
      if (selection.text == '채팅참여1등') this.chipCheck.isChatFirst = false;
      this.getList(this.chipCheck);
    },
    showSelected(item) {
      this.selected.push(item);

      if (item.text == '지각') this.chipCheck.isLate = true;
      if (item.text == '결석') this.chipCheck.isAbsent = true;
      if (item.text == '출석1등') this.chipCheck.isAttendFirst = true;
      if (item.text == '채팅참여1등') this.chipCheck.isChatFirst = true;

      this.getList(this.chipCheck);
      this.$emit('selected', this.selected);
    },
  },
};
</script>
<style scoped>
@font-face {
  font-family: 'GongGothicLight';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-10@1.0/GongGothicLight.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}
/* * {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'GongGothicLight';
} */
.manage {
  margin: 3% 2%;
  font-family: 'GongGothicLight';
}
.table_responsive {
  width: 100%;
  padding: 15px;
  /* overflow: auto; */
  margin: auto;
  border-radius: 4px;
}

table {
  width: 100%;
  font-size: 13px;
  color: #444;
  white-space: nowrap;
  border-collapse: collapse;
}

table > thead {
  background-color: #41ea93;
  color: #fff;
}

table > thead th {
  padding: 15px;
}

table th,
table td {
  border: 1px solid #0000;
  padding: 10px 5px;
}

table > tbody > tr > td > img {
  display: inline-block;
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 50%;
  border: 4px solid #fff;
  box-shadow: 0 2px 6px #000;
}

.action_btn {
  display: flex;
  justify-content: center;
  gap: 10px;
}

.action_btn > a {
  text-decoration: none;
  color: #444;
  background: #fff;
  border: 1px solid;
  display: inline-block;
  padding: 7px 20px;
  font-weight: bold;
  border-radius: 3px;
  transition: 0.3s ease-in-out;
}

.action_btn > a:nth-child(1) {
  border-color: #26a69a;
}

.action_btn > a:nth-child(2) {
  border-color: orange;
}

.action_btn > a:hover {
  box-shadow: 0 3px 8px #0003;
}

table > tbody > tr {
  background-color: #fff;
  transition: 0.3s ease-in-out;
}

table > tbody > tr:nth-child(even) {
  background-color: rgb(238, 238, 238);
}

table > tbody > tr:hover {
  filter: drop-shadow(0px 5px 10px #0002);
}
.chip-search {
  margin-left: 10em;
}
</style>
