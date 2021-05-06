<template>
  <div class="manage">
    <v-row justify="">
      <v-container>{{ this.$store.state.uuid }}의 학생관리</v-container>
      <v-date-picker v-model="date" @click:date="classNameFetch" :landscape="landscape" locale="ko-kr" :allowed-dates="allowedDates" class="mt-4" min="1900-04-01" max="2100-10-30"></v-date-picker>
    </v-row>
    <v-row>
      <div style="margin-top: 100px;">
        <h2>show Management</h2>
        <h2>{{ date }}</h2>
      </div>
    </v-row>
    <v-row>
      <v-col cols="12" sm="6">
        <v-select :items="items" :label="date" solo @change="showManage"></v-select>
      </v-col>
    </v-row>
    <v-row>
      <!-- <div v-for="(each, idx) in UsersEval" :key="idx">
      </div> -->
      <div class="table-body">
        <div class="table_responsive">
          <table>
            <thead>
              <tr>
                <th>No</th>
                <th>PROFILE</th>
                <th>NAME</th>
                <th>E-MAIL</th>
                <th>Evaluation</th>
                <th>Attendance</th>
              </tr>
            </thead>

            <tbody>
              <EachUserManage v-for="(each, idx) in UsersEval" :key="idx" :each="each" :idx="idx" :rid="rid" :evalUserCnt="evalUserCnt"></EachUserManage>
            </tbody>
          </table>
        </div>
      </div>
    </v-row>
  </div>
</template>

<script>
import { fetchRoomname, findByUidClass } from '@/api/class';
import { fetchEval } from '@/api/evaluation';
import { partinAll } from '@/api/entrant';
import { getUsers } from '@/api/auth';
import EachUserManage from '@/views/evaluation/EachUserManage.vue';
export default {
  components: {
    EachUserManage,
  },
  data() {
    return {
      name: '',
      manageClass: [],
      items: [],
      landscape: true,
      date: '',
      menu: false,
      mindate: '2021-04-01',
      maxdate: '2021-04-30',

      arrayDates: [],
      roomName: [],
      rid: '',
      selectedRoomName: '',
      maxUser: '',
      firstUser: '',
      evalUserCnt: '',
      fetchRoomlen: 0,
      manageUsers: [],
      UsersEval: [
        // {
        //   userEval: null,
        //   user: null,
        // },
      ],
      manageClasscheck: false,
    };
  },

  async created() {
    const token = this.$store.state.token;
    const uuid = this.$store.state.uuid;
    const { data } = await findByUidClass(token, uuid);
    console.log(data);
    for (var i = 0; i < data.length; i++) {
      var dates = data[i].startTime.slice(0, 10);
      this.arrayDates.push(dates);
    }
    console.log(this.arrayDates);
    this.manageClass = data;
    console.log(this.manageClass);
    this.items = [];
    this.manageClasscheck = false;
  },
  async mounted() {},
  methods: {
    classNameFetch() {
      this.roomName = [];
      for (var i = 0; i < this.manageClass.length; i++) {
        if (this.manageClass[i].startTime.slice(0, 10) === this.date && !this.roomName.includes(this.manageClass[i].roomName)) {
          this.roomName.push(this.manageClass[i].roomName);
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
    async showManage(selected) {
      this.manageClasscheck = true;
      this.manageUsers = [];
      this.UsersEval = [];
      for (var i = 0; i < this.manageClass.length; i++) {
        if (this.manageClass[i].roomName === selected) {
          const { data } = await fetchRoomname(this.manageClass[i].roomName);
          this.rid = data[0].rid;
          console.log('방번호', this.rid);
          const res = await partinAll();
          for (var j = 0; j < res.data.length; j++) {
            if (res.data[j].rid === this.rid) {
              this.manageUsers.push({ uid: res.data[j].uid, eid: res.data[j].eid });
            }
          }
          console.log(this.manageUsers);
          const res_2 = await fetchEval();
          console.log(res_2);
          for (var k = 0; k < this.manageUsers.length; k++) {
            for (var m = 0; m < res_2.data.length; m++) {
              if (res_2.data[m].eid == this.manageUsers[k].eid) {
                //나중에 entrant 유저 정보도 객체형태로 넣어야함
                this.UsersEval.push(res_2.data[m]);
                this.UsersEval[this.UsersEval.length - 1]['uid'] = this.manageUsers[k].uid;
              }
            }
          }
        }
        this.evalUserCnt = this.UsersEval.length;
      }
      console.log(this.UsersEval);
    },
  },
};
</script>
<style scoped>
.manage {
  width: 100%;
  margin-left: 30%;
  padding: 25px 30px 30px 30px;
  border-radius: 5px;
  background: #fff;
}
/* table */

* {
  box-sizing: border-box;
}

.table_responsive {
  width: 150%;
  padding: 15px;
  overflow: auto;
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
  background-color: #1976d2;
  color: #fff;
}

table > thead th {
  padding: 15px;
}

table th,
table td {
  border: 1px solid #00000017;
  padding: 10px 15px;
}

table > tbody > tr > td > img {
  display: inline-block;
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 50%;
  border: 4px solid #fff;
  box-shadow: 0 2px 6px #0003;
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
  filter: drop-shadow(0px 2px 6px #0002);
}
</style>
