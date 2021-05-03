<template>
  <div>
    <span>{{ this.$store.state.uuid }}의 학생관리</span>
    <v-row justify="center">
      <v-date-picker v-model="date" @click:date="classNameFetch" :landscape="landscape" locale="ko-kr" :allowed-dates="allowedDates" class="mt-4" min="1900-04-01" max="2100-10-30"></v-date-picker>
      <v-col class="d-flex" cols="12" sm="6">
        <v-select :items="items" :label="date" solo @change="showManage"></v-select>
      </v-col>
    </v-row>

    <div style="margin-top: 100px;">
      <h2>show Management</h2>
      <h2>{{ date }}</h2>
    </div>
  </div>
</template>

<script>
import { userEvalList } from '@/api/auth';
import { fetchRoomname, findByUidClass } from '@/api/class';
import { partinAll } from '@/api/entrant';

export default {
  data() {
    return {
      uuid: this.$store.state.uuid,
      token: this.$store.state.token,
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

      selectedRoomName: '',
      maxUser: '',
      firstUser: '',
      fetchRoomlen: 0,
      manageUsers: [],
      manageClasscheck: false,
    };
  },

  async created() {
    const { data } = await findByUidClass(this.token, this.uuid);
    console.log(data);
    for (var i = 0; i < data.length; i++) {
      var dates = data[i].startTime.slice(0, 10);
      this.arrayDates.push(dates);
    }
    this.manageClass = data;
    this.items = [];
    this.manageClasscheck = false;
  },
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
      for (var i = 0; i < this.manageClass.length; i++) {
        if (this.manageClass[i].room_name === selected) {
          const { data } = await fetchRoomname(this.manageClass[i].room_name);
          const rid = data[0].rid;
          const res = await partinAll();
          for (var j = 0; j < res.data.length; j++) {
            if (res.data[j].rid === rid) {
              this.manageUsers.push({ uid: res.data[j].uid, eid: res.data[j].eid });
            }
          }
        }
      }
    },
  },
};
</script>
