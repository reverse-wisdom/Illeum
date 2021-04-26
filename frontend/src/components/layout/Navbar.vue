<template>
  <div id="nav">
    <div class="Navwrapper hover_collapse">
      <div class="top_navbar">
        <div class="logo">
          <img src="@/assets/img/finallogo.png" style="width:100%; height 30%; margin-top:50%; margin-left: 25%" alt="" />
        </div>
        <div class="menu">
          <!-- <div class="hamburger" @click="toggleNav">
            <span>★</span>
          </div> -->
          <div class="profile_wrap">
            <div class="profile"></div>
          </div>
        </div>
      </div>

      <div class="sidebar" @mouseover="hoverNav">
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
              <a href="/webrtcTest4">
                <span class="Navtext">webrtcTest4</span>
              </a>
            </li>
            <li>
              <a href="/componentTest">
                <span class="Navtext">component Test</span>
              </a>
            </li>
            <li>
              <a href="/chartTest">
                <span class="Navtext">chartTest</span>
              </a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>

  <!-- <header>
      <a class="logo" href="/"><img src="@/assets/img/finallogo.png" alt="logo" /></a>
      <nav>
        <ul class="nav__links">

          <router-link to="/">Home</router-link>
          |
          <router-link to="/sign">Sign</router-link>
          |
          <router-link to="/webrtcTest">webrtcTest</router-link>
          |
          <router-link to="/webrtcTest2">webrtcTest2</router-link>
          |
          <router-link to="/webrtcTest3">webrtcTest3</router-link>
          |
          <router-link to="/webrtcTest4">webrtcTest4</router-link>
          |
          <router-link to="/componentTest">componentTest</router-link>
          |
          <router-link to="/chartTest">chartTest</router-link>
        </ul>
      </nav>
      <p class="menu cta" @click="menu">Menu</p>
    </header> -->
</template>
<script>
export default {
  components: {},
  created() {},
  methods: {
    // toggleNav() {
    //   var hamburger = document.querySelector('.hamburger');
    //   hamburger.addEventListener('click', () => {
    //     hamburger.closest('.Navwrapper').classList.toggle('click_collapse');
    //     hamburger.closest('.Navwrapper').classList.toggle('hover_collapse');
    //   });
    // },
    hoverNav() {
      var li_items = document.querySelectorAll('.sidebar ul li');

      var Navwrapper = document.querySelector('.Navwrapper');

      li_items.forEach((li_item) => {
        li_item.addEventListener('mouseenter', () => {
          if (Navwrapper.classList.contains('click_collapse')) {
            return;
          } else {
            li_item.closest('.Navwrapper').classList.remove('hover_collapse');
          }
        });
      });

      li_items.forEach((li_item) => {
        li_item.addEventListener('mouseleave', () => {
          if (Navwrapper.classList.contains('click_collapse')) {
            return;
          } else {
            li_item.closest('.Navwrapper').classList.add('hover_collapse');
          }
        });
      });
    },
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Baloo+Paaji+2:wght@400;600&display=swap');

* {
  font-family: 'Baloo Paaji 2', cursive;
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  list-style: none;
  text-decoration: none;
}

#nav {
  background: #f5f5f5;
}

.top_navbar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 60px;
  background: #fff;
  /* box-shadow: 1px 0 2px rgba(0, 0, 0, 0.125); */
  display: flex;
  align-items: center;
}

.top_navbar .logo {
  width: 250px;
  /* border-right: 1px solid #f5f5f5; */
}

.top_navbar .menu {
  width: calc(100% - 250px);
  padding: 0 25px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.top_navbar .hamburger {
  font-size: 25px;
  cursor: pointer;
  color: #005faf;
}

.top_navbar .hamburger:hover {
  color: #007dc3;
}

/* .hover_collapse .sidebar {
  width: 5%;
} */

/* .hover_collapse .sidebar ul li a .Navtext {
  display: none;
} */

.sidebar {
  position: fixed;
  top: 20%;
  left: 0;
  width: 20%;
  height: 100%;
  background: #1cbd8f;
}

.sidebar ul li a {
  display: block;
  padding: 30px 20px;
  /* border-bottom: 1px solid #fff; */
  color: #000;
  transition: all 0.2s ease;
}

.sidebar ul li a .icon {
  font-size: 18px;
  vertical-align: middle;
  transition: background 0.2s ease;
}

.sidebar ul li a .Navtext {
  margin-left: 10px;
  color: #fff;
  text-transform: uppercase;
  letter-spacing: 2px;
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

/* .click_collapse .main_container {
  width: calc(100% - 1000px);
  margin-left: 100px;
  transition: all 0.2s ease;
} */
</style>
