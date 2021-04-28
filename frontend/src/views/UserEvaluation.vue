<template>
  <div id="user-eval">
    <div class="user-evaldata">
      <v-alert type="info" elevation="3" color="#304FFE" style="margin-top:10%; width:60vw;">
        <header>{{ this.$store.state.name }}의 평가</header>
      </v-alert>
      <input type="radio" name="slider" checked id="attend" />
      <input type="radio" name="slider" id="learn" />
      <input type="radio" name="slider" id="partin" />

      <nav>
        <label for="attend" class="attend">
          출결현황
        </label>
        <label for="learn" class="learn">
          학습관리
        </label>
        <label for="partin" class="partin">
          참여도
        </label>

        <div class="slider"></div>
      </nav>
      <section>
        <div class="content content-1"></div>
        <div class="content content-2">
          <UserEval></UserEval>
        </div>
        <div class="content content-3"></div>
      </section>
    </div>
  </div>
</template>
<script>
import UserEval from '@/views/evaluation/UserEval';
export default {
  components: {
    UserEval,
  },
  data() {
    return {
      showcheck2: false,
    };
  },
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
#user-eval {
  overflow: hidden;
  /* margin-top: 0; */
  padding: 0 20px;
}
::selection {
  background: rgba(23, 162, 184, 0.3);
}
.user-evaldata {
  max-width: 700px;
  width: 100%;
  margin: auto;
  padding: 25px 30px 30px 30px;
  border-radius: 5px;
  background: #fff;
  /* box-shadow: 0px 10px 15px rgba(0, 0, 0, 0.1); */
}
.user-evaldata header {
  font-size: 30px;
  font-weight: 600;
  padding-bottom: 20px;
}
.user-evaldata nav {
  position: relative;
  width: 80%;
  height: 50px;
  display: flex;
  align-items: center;
}
.user-evaldata nav label {
  display: block;
  height: 100%;
  width: 100%;
  text-align: center;
  line-height: 50px;
  cursor: pointer;
  position: relative;
  z-index: 1;
  color: #000;
  font-size: 17px;
  border-radius: 5px;
  margin: 0 5px;
  transition: all 0.3s ease;
}
.user-evaldata nav label:hover {
  background: rgba(117, 120, 121, 0.3);
}
#attend:checked ~ nav label.attend,
#learn:checked ~ nav label.learn,
#partin:checked ~ nav label.partin {
  color: #fff;
}
nav label i {
  padding-right: 7px;
}
nav .slider {
  position: absolute;
  height: 100%;
  width: 20vh;
  left: 0;
  bottom: 0;
  z-index: 0;
  border-radius: 5px;
  background: #304ffe;
  transition: all 0.3s ease;
}
input[type='radio'] {
  display: none;
}
#learn:checked ~ nav .slider {
  left: 31%;
}
#partin:checked ~ nav .slider {
  left: 67%;
}

section .content {
  display: none;
  background: #fff;
}
#attend:checked ~ section .content-1,
#learn:checked ~ section .content-2,
#partin:checked ~ section .content-3 {
  display: block;
}
section .content .title {
  font-size: 21px;
  font-weight: 500;
  margin: 30px 0 10px 0;
}
section .content p {
  text-align: justify;
}
</style>
