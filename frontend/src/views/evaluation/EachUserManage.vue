<template>
  <tr>
    <td>{{ idx + 1 }}</td>
    <td><v-img :src="url" id="preview" style="width:50px; height:50px;"></v-img></td>
    <td>{{ name }}</td>
    <td>{{ email }}</td>

    <td>
      <v-dialog v-if="this.$store.state.token" v-model="dialog" persistent max-width="1200px">
        <template v-slot:activator="{ on, attrs }">
          <v-btn color="primary" dark v-bind="attrs" v-on="on">
            DETAIL
          </v-btn>
        </template>
        <v-card>
          <v-card-title>
            <span class="headline">
              <span>{{ name }}님의</span>
              Evaluation
            </span>
          </v-card-title>
          <v-card-text>
            <v-container>
              <v-card>
                <v-toolbar flat color="primary" dark>
                  <v-toolbar-title>
                    <span>{{ name }}님의</span>
                    Evaluation
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
                      <LecUserAttend :each="each" :attend="attend"></LecUserAttend>
                    </v-card>
                  </v-tab-item>
                  <v-tab-item>
                    <v-card flat>
                      <LecUserEval :each="each" :rid="rid"></LecUserEval>
                    </v-card>
                  </v-tab-item>
                  <v-tab-item>
                    <v-card flat>
                      <LecUserPartin :each="each" :roomData="roomData" :evalUserCnt="evalUserCnt" :rid="rid"></LecUserPartin>
                    </v-card>
                  </v-tab-item>
                </v-tabs>
              </v-card>
            </v-container>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="dialog = false">
              Close
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </td>

    <td>{{ attend }}</td>
  </tr>
</template>

<script>
import LecUserAttend from '@/views/evaluation/LecUserAttend';
import LecUserEval from '@/views/evaluation/LecUserEval';
import LecUserPartin from '@/views/evaluation/LecUserPartin';
import EachUserManageTableData from '@/views/evaluation/EachUserManageTableData';
import { getUsers } from '@/api/auth';
import { findByRidClass } from '@/api/class';
export default {
  components: {
    LecUserAttend,
    LecUserEval,
    LecUserPartin,
    EachUserManageTableData,
  },
  data() {
    return {
      name: '',
      url: null,
      email: '',
      eachli: [],
      dialog: false,
      attend: '',
      roomData: [],
      saveTime: '',
    };
  },
  props: {
    each: {
      type: Object,
    },
    idx: {
      type: Number,
    },
    rid: {
      type: Number,
    },
    evalUserCnt: {
      type: Number,
    },
    chipCheck: {
      type: Object,
    },
  },
  async created() {
    const res_3 = await getUsers();
    for (var p = 0; p < res_3.data.length; p++) {
      if (this.each.uid === res_3.data[p].uid) {
        this.name = res_3.data[p].name;
        this.email = res_3.data[p].email;
      }
    }

    this.url = 'https://k4d106.p.ssafy.io/profile/' + this.each.uid + '/256';

    const { data } = await findByRidClass(this.rid);
    this.roomData = data;

    console.log(this.roomData.start_time);
    this.saveTime = this.$moment(this.roomData.start_time)
      .add(10, 'm')
      .format('YYYY-MM-DD HH:mm:ss');

    console.log(this.saveTime);
    console.log(this.each.attendTime);
    if (this.each.attendTime > this.saveTime) {
      this.attend = '지각';
    } else if (this.each.attendTime == '' || this.each.attendTime == undefined || this.each.attendTime == null) {
      this.attend = '결석';
    } else {
      this.attend = '정상';
    }
    this.$emit('goSearch', this.attend);
  },
  methods: {},
};
</script>

<style></style>
