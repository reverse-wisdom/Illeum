<template>
  <div id="">
    <v-navigation-drawer permanent app width="320">
      <img src="../../assets/img/textlogo_2.png" class="Navlogo" alt="" style="width:240px; height:100px; margin: 4% 0;" />

      <v-sheet color="" class="pa-1">
        <v-avatar class="mb-3" color="grey darken-1" size="90">
          <v-img :src="'/profile/' + this.$store.state.uuid + '/256'" id="preview" alt=""></v-img>
        </v-avatar>
        <div>
          <span class="name-info">{{ this.$store.state.name }}님!</span>
          <span>환영합니다.</span>
        </div>
      </v-sheet>

      <!-- <v-btn v-if="this.$store.state.token" @click="signoutUser" tile color="#FFCD4A">
        <v-icon left>
          mdi-logout
        </v-icon>
        LOGOUT
      </v-btn> -->
      <!-- <v-divider></v-divider> -->
      <!-- 프로필 모달 -->
      <v-list class="">
        <v-row class="mx-2 pa-2">
          <v-btn tile outlined @click="dialog = true">
            <v-icon left>
              mdi-account
            </v-icon>
            PROFILE
          </v-btn>
          <v-btn tile outlined style="margin-left:2%;">
            <v-icon left>
              mdi-account
            </v-icon>
            <router-link to="/myclass" style="margin:auto; text-decoration:none; color:#000;">MY CLASS</router-link>
          </v-btn>
        </v-row>
      </v-list>

      <v-list rounded class="nav-link">
        <v-list-item v-if="loginchk === null || loginchk === undefined || loginchk === ''">
          <router-link to="/sign">Sign</router-link>
        </v-list-item>

        <!-- 강의자 -->
        <v-list-group prepend-icon="mdi-account-circle" color="dark" v-model="active1">
          <template v-slot:activator>
            <v-list-item-title>강의자</v-list-item-title>
          </template>
          <template>
            <v-list-item-content>
              <router-link to="/classcreate">클래스개설</router-link>
            </v-list-item-content>
            <v-list-item-content>
              <router-link to="/webrtclistteacher">수업시작</router-link>
            </v-list-item-content>
            <v-list-item-content>
              <router-link to="/lecturermanage">평가관리</router-link>
            </v-list-item-content>
          </template>
        </v-list-group>
        <!-- 수강생 -->
        <v-list-group prepend-icon="mdi-account-circle" color="dark" v-model="active2">
          <template v-slot:activator>
            <v-list-item-title>수강생</v-list-item-title>
          </template>

          <template>
            <v-list-item-content>
              <router-link to="/classjoin">수강신청</router-link>
            </v-list-item-content>
            <v-list-item-content>
              <router-link to="/webrtcliststudent">수업참여</router-link>
            </v-list-item-content>
            <v-list-item-content>
              <router-link to="/userevaluation">평가조회</router-link>
            </v-list-item-content>
          </template>
        </v-list-group>
        <v-list-item></v-list-item>
      </v-list>

      <v-list style="margin-bottom: 0;">
        <v-list-item-content>
          <router-link to="/webrtclistall">전체수업리스트</router-link>
        </v-list-item-content>
      </v-list>
      <v-list style="margin-bottom: 0;">
        <v-row class="mx-2 pa-2">
          <v-btn v-if="this.$store.state.token" @click="signoutUser" tile outlined>
            <v-icon left>
              mdi-logout
            </v-icon>
            LOGOUT
          </v-btn>
          <v-btn class="px-7" style="margin-left:2%;" @click="gohome" tile outlined>
            <v-icon left>
              mdi-home
            </v-icon>
            HOME
          </v-btn>
        </v-row>
      </v-list>
    </v-navigation-drawer>

    <v-dialog v-if="this.$store.state.token" v-model="dialog" persistent max-width="800px">
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
              <v-col cols="12" sm="6">
                <v-row>
                  <label for="password">비밀번호:</label>
                  <input type="password" id="password" v-model="password" disabled placeholder="*********" />
                </v-row>
                <v-row>
                  <label for="passwordcheck">비밀번호확인:</label>
                  <input type="password" id="passwordcheck" v-model="passwordchk" disabled value="" />
                </v-row>
                <v-row>
                  <template v-if="correspond">
                    <v-btn id="completed" @click="editPassword">수정완료</v-btn>
                  </template>
                  <template v-else>
                    <v-btn @click="updatePassword">수정하기</v-btn>
                  </template>
                </v-row>
              </v-col>
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
  </div>
</template>

<script>
import { logoutUser, editUser, createThumbnails } from '@/api/auth';
export default {
  data() {
    return {
      active1: true,
      active2: true,
      cards: ['Today', 'Yesterday'],
      // drawer: null,

      dialog: false,
      image: null,
      url: null,
      loginchk: this.$store.state.token,
      password: '',
      passwordchk: '',
      correspond: false,
    };
  },
  created() {
    this.url = `/profile/${this.$store.state.uuid}/256?t=${Date.now()}`;
  },
  watch: {
    passwordchk(value) {
      this.passwordchk = value;
      if (this.password == this.passwordchk && this.password != '' && this.passwordchk != '') {
        this.correspond = true;
      }
    },
  },
  methods: {
    Preview_image() {
      if (this.image != null) this.url = URL.createObjectURL(this.image);
    },
    gohome() {
      this.$router.push({ name: 'Landing' });
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

        this.$store.state.alertSocket.disconnect();
      } else {
        this.$swal({
          icon: 'error',
          title: '로그아웃 실패',
        });
      }
    },
    updatePassword() {
      const password = document.querySelector('#password');
      const passwordcheck = document.querySelector('#passwordcheck');
      password.disabled = false;
      password.focus();
      passwordcheck.disabled = false;
      passwordcheck.classList.add('pwdchk-input');
    },
    async editPassword() {
      const userData = {
        email: this.$store.state.email,
        name: this.$store.state.name,
        paswword: this.password,
      };
      const { data } = await editUser(userData);
      if (data == 'success') {
        this.$swal({
          icon: 'success',
          title: '수정완료되었습니다',
        });
      }
    },
  },
};
</script>
<style scoped>
.name-info {
  font-weight: bold;
  margin-right: 0.2rem;
}
.nav-link {
  display: flex;
  flex-direction: column;
}
.nav-link a {
  text-decoration: none;
  color: #24252a;
  font-size: 1rem;
}
</style>
