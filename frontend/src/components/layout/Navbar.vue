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
        <!-- <v-btn v-if="this.$store.state.token" @click="profileUser">PROFILE</v-btn> -->

        <div class="sidebar_inner">
          <ul>
            <li>
              <a href="#">
                <span class="Navtext">Home</span>
              </a>
            </li>
            <li>
              <a href="/sign">
                <span class="Navtext">Sign</span>
              </a>
            </li>
            <li>
              <a href="/classtest">
                <span class="Navtext">class</span>
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
import { logoutUser } from '@/api/auth';
export default {
  components: {},
  created() {},
  methods: {
    async signoutUser() {
      const userData = this.$store.state.token;
      localStorage.clear();
      sessionStorage.clear();
      const { data } = await logoutUser(userData);
      console.log(data);

      if (data == 'success') {
        this.$store.commit('clearToken');
        this.$store.commit('clearUuid');
        this.$store.commit('clearEmail');
        this.$store.commit('clearRole');
        this.$store.commit('clearName');
        this.$router.push('/sign');
        console.log('로그아웃 성공');
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
