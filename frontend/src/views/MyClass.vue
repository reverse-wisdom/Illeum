<template>
  <div class="my-class">
    <v-alert
      class="text-start font-weight-black"
      border="left"
      color="indigo"
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
  font-family: 'NEXON Lv2 Gothic';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-04@2.1/NEXON Lv2 Gothic.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}

* {
  font-family: 'NEXON Lv2 Gothic';
}
.my-class {
  margin-left: 7%;
}
.checkbox {
  position: absolute;
  top: 16px;
  right: 1%;

  -webkit-appearance: none;
  width: 14rem;
  height: 60px;
  background-color: #707070;

  border-radius: 50px;
  cursor: pointer;
  outline: none;
  z-index: 2;
}

.checkbox:checked {
  background-color: #8f8f8f;
  outline: none;
  cursor: pointer;
}

.checkbox::before {
  content: '';
  position: absolute;
  width: 6rem;
  height: 40px;
  background-color: #4cb4a5;
  border-radius: 50px;
  top: 10px;
  cursor: pointer;
  left: 1rem;
  transition: 0.3s linear;
}

.checkbox:checked::before {
  left: 7.5rem;
  content: '';
}

#learner {
  position: absolute;
  top: 1.9rem;
  right: 8.7%;
  /* transform: translateY(-50%); */
  color: #fff;
  font-size: 20px;
  font-weight: 400;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  margin-left: px;
  outline: none;
  z-index: 3;
}
#lecturer {
  position: absolute;
  outline: none;
  top: 1.9rem;
  right: 2.5%;
  color: #fff;
  font-size: 20px;
  font-weight: 400;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;

  z-index: 3;
}
#alert {
  background: linear-gradient(60deg, #4cb4a5, #38a897);
}
</style>
