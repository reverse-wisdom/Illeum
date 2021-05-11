<template>
  <div id="nav">
    <div class="Navwrapper hover_collapse">
      <!-- sidebar -->
      <div>
        <img src="../../assets/img/navbar.jpg" class="Navbg" alt="" />
      </div>
      <div class="sidebar">
        <img src="../../assets/img/greenlogo.png" class="Navlogo" alt="" />
        <hr />
        <v-btn v-if="this.$store.state.token" @click="signoutUser">LOGOUT</v-btn>

        <!-- 프로필 모달 -->
        <v-dialog v-if="this.$store.state.token" v-model="dialog" persistent max-width="800px">
          <template v-slot:activator="{ on, attrs }">
            <v-btn color="primary" dark v-bind="attrs" v-on="on">
              PROFILE
            </v-btn>
          </template>
          <v-card>
            <v-card-title>
              <span class="headline">
                <span>{{ this.$store.state.name }}님의</span>
                Profile
              </span>
            </v-card-title>
            <v-card-text>
              <v-container>
                <v-row>
                  <v-col cols="12">
                    <!-- <v-col cols="12" sm="6" md="6"> -->
                    <div>PROFILE IMAGE</div>
                    <!-- <img src="../../assets/img/greenlogo.png" class="Navlogo" alt="" /> -->
                    <div class="formdata">
                      <v-file-input
                        id="thumbnail"
                        name="thumbnail"
                        v-model="image"
                        show-size
                        label="프로필 이미지 수정을 위해 입력창을 클릭해주세요"
                        @change="Preview_image($event)"
                        style=" cursor : pointer;"
                      ></v-file-input>
                      <v-img :src="url" id="preview" style="width:100px; height:100px;"></v-img>
                    </div>
                  </v-col>
                  <v-col cols="12">
                    <label for="">계정:</label>
                    {{ this.$store.state.email }}
                  </v-col>
                  <v-col cols="12">
                    <label for="">이름:</label>
                    {{ this.$store.state.name }}
                  </v-col>
                  <v-col cols="12" sm="6"></v-col>
                  <v-col cols="12" sm="6"></v-col>
                </v-row>
              </v-container>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="dialog = false">
                Close
              </v-btn>
              <v-btn color="blue darken-1" text @click="userUpdate">
                Save
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <div class="sidebar_inner">
          <ul>
            <li>
              <a href="/about">
                <span class="Navtext">Home</span>
              </a>
            </li>
            <li>
              <a href="/sign" v-if="loginchk === null || loginchk === undefined || loginchk === ''">
                <span class="Navtext">Sign</span>
              </a>
            </li>
            <li>
              <a href="/classlist">
                <span class="Navtext">class</span>
              </a>
            </li>
            <li>
              <a href="/classcreate">
                <span class="Navtext">클래스 오픈(강의자)</span>
              </a>
            </li>
            <li>
              <a href="/classjoin">
                <span class="Navtext">클래스 목록페이지(수강자)</span>
              </a>
            </li>
            <li>
              <a href="/webrtclist">
                <span class="Navtext">화상수업 목록페이지</span>
              </a>
            </li>
            <li>
              <a href="/userevaluation">
                <span class="Navtext">evaluation</span>
              </a>
            </li>
            <!-- <li>
              <a href="/classsearch">
                <span class="Navtext">classsearch</span>
              </a>
            </li> -->
            <li>
              <a href="/webrtcTest4">
                <span class="Navtext">webrtc Test4</span>
              </a>
            </li>
            <li>
              <a href="/componentTest">
                <span class="Navtext">component Test</span>
              </a>
            </li>
            <li>
              <a href="/chartTest">
                <span class="Navtext">Chart Test</span>
              </a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { logoutUser, editUser, createThumbnails } from '@/api/auth';
export default {
  components: {},
  data() {
    return {
      dialog: false,
      image: null,
      url: null,
      loginchk: this.$store.state.token,
    };
  },
  created() {
    this.url = 'https://k4d106.p.ssafy.io/profile/' + this.$store.state.uuid + '/256';
    console.log(this.loginchk);
  },
  methods: {
    Preview_image() {
      this.url = URL.createObjectURL(this.image);
    },
    async userUpdate() {
      const uuid = this.$store.state.uuid;
      var frm = new FormData();
      frm.append('file', this.image);
      const { data } = await createThumbnails(uuid, frm);
      this.dialog = false;
    },
    async signoutUser() {
      const userData = this.$store.state.token;

      const { data } = await logoutUser(userData);
      localStorage.clear();
      sessionStorage.clear();
      this.$router.push('/sign');

      if (data == 'success') {
        this.$store.commit('clearToken');
        this.$store.commit('clearUuid');
        this.$store.commit('clearEmail');
        this.$store.commit('clearRole');
        this.$store.commit('clearName');
        console.log('로그아웃 성공');

        this.$store.state.alertSocket.disconnect();

      } else {
        console.log('로그아웃실패');
      }
    },
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Baloo+Paaji+2:wght@400;600&display=swap');

* {
  /* font-family: 'Baloo Paaji 2', cursive; */
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  list-style: none;
  text-decoration: none;
}
.main-container {
  width: 100%;
  height: 100%;
  background: #000;
}
#nav {
  padding: 0;
}

.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  width: 22%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
}
.Navbg {
  position: fixed;
  top: 0;
  left: 0;
  width: 22%;
  height: 100%;
  /* background: rgba(0, 0, 0, 0.8) url(../../assets/img/navbar.jpg); */
}
hr {
  border-top: 1px solid rgba(248, 239, 239, 0.5);
  border-bottom: 1px solid rgba(0, 0, 0);
  margin-top: 20px;
}
.Navlogo {
  margin-top: 1.5rem;
  margin-left: 5%;
  width: 50%;
  /* height: 10%; */
}
.sidebar ul li a {
  margin-top: 1rem;
  display: block;
  padding: 25px 20px;
  /* border-bottom: 1px solid #fff; */
  color: #000;
  transition: all 0.2s ease;
}

/* .sidebar ul li a .icon {
  font-size: 18px;
  vertical-align: middle;
  transition: background 0.2s ease;
} */

.sidebar ul li a .Navtext {
  color: #fff;
  font-size: 15px;
  font-weight: 100;
  text-transform: uppercase;
  letter-spacing: 3px;
}

.sidebar ul li a:hover {
  background: #fff;
  color: #1cbd8f;
}
.sidebar ul li a:hover .Navtext {
  color: #1cbd8f;
}

.click_collapse .sidebar {
  transition: all 0.2s ease;
}
</style>
