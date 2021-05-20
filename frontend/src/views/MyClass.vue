<template>
  <div class="my-class">
    <v-alert
      class="text-start font-weight-black"
      border="left"
      color="#2E95FF"
      dark
      id="alert"
      style="margin-top:6%; font-size: 1.5rem; letter-spacing: 2.3px; line-height: 45px;"
      elevation="3"
      height="70"
      width="1400"
    >
      # 내가 수강/강의하고 있는 클래스
    </v-alert>

    <div>
      <input type="checkbox" class="checkbox" v-model="checked" />
    </div>
    <div class="toggle-text" style="">
      <div id="learner">
        수강생
      </div>
      <div id="lecturer">강의자</div>
    </div>

    <LearnerMode v-if="checked == false"></LearnerMode>
    <LecturerMode v-else></LecturerMode>
  </div>
</template>

<script>
import LecturerMode from '@/views/user/LecturerMode';
import LearnerMode from '@/views/user/LearnerMode';
export default {
  components: {
    LecturerMode,
    LearnerMode,
  },
  data() {
    return {
      checked: false,
    };
  },
};
</script>

<style scoped>
@font-face {
  font-family: 'NEXON Lv1 Gothic OTF';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-04@2.1/NEXON Lv1 Gothic OTF.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}
.my-class {
  margin: 10% 2%;
  font-family: 'NEXON Lv1 Gothic OTF';
}

.checkbox {
  position: absolute;
  top: 5%;
  right: 5%;

  -webkit-appearance: none;
  width: 14rem;
  height: 3.5rem;
  background-color: #41ea93;
  border-radius: 50px;
  cursor: pointer;
  outline: none;
  z-index: 2;
}

.checkbox:checked {
  background-color: #41ea93;
  outline: none;
  cursor: pointer;
}

.checkbox::before {
  content: '';
  position: absolute;
  width: 7.2rem;
  height: 3.5rem;
  border-radius: 50px;
  background-color: #2e95ff;

  cursor: pointer;

  /* transition: 0.6s linear; */
}

.checkbox:checked::before {
  left: 7rem;
  content: '';
}
.toggle-text {
  top: 5rem;
  /* z-index: 3; */
}

#learner {
  position: absolute;
  top: 4rem;
  right: 16.5%;
  color: #fff;
  font-size: 20px;
  font-weight: 400;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;

  outline: none;
  z-index: 3;
}
#lecturer {
  position: absolute;
  outline: none;
  top: 4rem;
  right: 7%;
  color: #fff;
  font-size: 20px;
  font-weight: 400;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  z-index: 3;
}
</style>
