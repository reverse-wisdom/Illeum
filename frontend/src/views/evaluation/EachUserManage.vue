<template>
  <tr>
    <td>{{ idx + 1 }}</td>
    <td><img src="http://www.foodnmed.com/news/photo/201903/18296_3834_4319.jpg" style="width:50px; height:50px;" alt="" /></td>
    <td>{{ name }}</td>
    <td>{{ email }}</td>

    <td>
      <!-- <span class="action_btn">
        <a href="#" @click="eachShow">자세히보기</a>
      </span> -->
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
                  <v-toolbar-title>User Profile</v-toolbar-title>
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
                      <LecUserPartin :each="each" :roomData="roomData" :manageUsers="manageUsers" :rid="rid"></LecUserPartin>
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
import { getUsers } from '@/api/auth';
import { findByRidClass } from '@/api/class';
export default {
  components: {
    LecUserAttend,
    LecUserEval,
    LecUserPartin,
  },
  data() {
    return {
      name: '',
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
    manageUsers: {
      type: Array,
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
    const { data } = await findByRidClass(this.rid);
    this.roomData = data;
    console.log(data);
    console.log(this.roomData.start_time);
    this.saveTime = this.$moment(this.roomData.start_time).format('YYYY-MM-DD h:mm:ss ');
    this.saveTime = this.$moment(this.saveTime).add('m', 10);
    this.saveTime = this.$moment(this.saveTime).format('YYYY-MM-DD h:mm:ss ');
    console.log(this.saveTime);
    console.log(this.each.attendTime);
    if (this.each.attendTime <= this.saveTime) {
      this.attend = '지각';
    } else if (this.each.attendTime == '') {
      this.attend = '조퇴';
    } else {
      this.attend = '정상';
    }
  },
  methods: {},
};
</script>

<style></style>
