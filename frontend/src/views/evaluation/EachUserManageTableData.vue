<template>
  <tr>
    <td>{{ idx + 1 }}</td>
    <td><img src="http://www.foodnmed.com/news/photo/201903/18296_3834_4319.jpg" style="width:50px; height:50px;" alt="" /></td>
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
export default {
  components: {
    LecUserAttend,
    LecUserEval,
    LecUserPartin,
  },
  props: ['idx', 'roomData', 'name', 'email', 'evalUserCnt', 'rid', 'each', 'attend'],
  data() {
    return {
      dialog: false,
    };
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
.chip-search {
  margin-left: 10em;
}
</style>
