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
                    출결현황
                  </v-tab>

                  <v-tab-item>
                    <v-card flat>
                      <LecUserAttend></LecUserAttend>
                    </v-card>
                  </v-tab-item>
                  <v-tab-item>
                    <v-card flat>
                      <LecUserAttend></LecUserAttend>
                    </v-card>
                  </v-tab-item>
                  <v-tab-item>
                    <v-card flat>
                      <LecUserAttend></LecUserAttend>
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
            <v-btn color="blue darken-1" text>
              Save
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </td>
  </tr>
</template>

<script>
import LecUserAttend from '@/views/evaluation/LecUserAttend';
import { getUsers } from '@/api/auth';
export default {
  components: {
    LecUserAttend,
  },
  data() {
    return {
      name: '',
      email: '',
      eachli: [],
      dialog: false,
    };
  },
  props: {
    each: {
      type: Object,
    },
    idx: {
      type: Number,
    },
  },
  async created() {
    console.log('앞', this.each);
    const res_3 = await getUsers();
    for (var p = 0; p < res_3.data.length; p++) {
      if (this.each.uid === res_3.data[p].uid) {
        this.name = res_3.data[p].name;
        this.email = res_3.data[p].email;
      }
    }
  },
  methods: {
    eachShow() {
      console.log('each');
    },
  },
};
</script>

<style></style>
