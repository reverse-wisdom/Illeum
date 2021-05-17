<template>
  <div id="app">
    <full-page ref="fullpage" :options="options" id="fullpage">
      <div class="section section1" :style="image_1">
        <div class="">
          <div class="content" v-html="serviceText"></div>
        </div>

        <button style="margin-bottom:0;" @click="$refs.fullpage.api.moveSectionDown()">NEXT ▼</button>
      </div>
      <div class="section section2" :style="image_2">
        <!-- 화상플랫폼(webRTC) 기능

        쌍방향 음성, 영상, 화이트보드, 화면공유, 채팅, 실시간알림 , -->
        <div class="slide">
          <h3>Slide 2.1</h3>
        </div>
        <div class="slide">
          <h3>Slide 2.2</h3>
        </div>
        <div class="slide">
          <h3>Slide 2.3</h3>
        </div>
        <div class="slide">
          <h3>Slide 2.4</h3>
        </div>
        <div class="slide">
          <h3>Slide 2.5</h3>
        </div>
      </div>
      <div class="section section3" :style="image_3">
        <!-- <h4 style="color: #535353;  letter-spacing:4px; font-size:2rem;">실시간 학습태도관리</h4> -->
        <ul class="features">
          <div style="display:flex; flex-direction:row;">
            <li>
              <em class="fa fa-3x fa-address-book"></em>
              <h3>집중</h3>
              <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Laudantium aut,</p>
            </li>

            <li>
              <em class="fa fa-3x fa-address-book"></em>
              <h3>딴짓</h3>
              <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Laudantium aut,</p>
            </li>
          </div>
          <div style="display:flex; flex-direction:row;">
            <li>
              <em class="fa fa-3x fa-address-book"></em>
              <h3>졸음</h3>
              <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Laudantium aut,</p>
            </li>
            <li>
              <em class="fa fa-3x fa-address-book"></em>
              <h3>자리비움</h3>
              <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Laudantium aut,</p>
            </li>
          </div>
        </ul>
        <!-- <div class="text-box">
          <p class="section3-p">
            학생들의 카메라에 비치는 얼굴, 시선, 상태에 따라서 현재 학생이 집중하는지, 딴짓을 하는지, 졸고 있는지, 자리를 비웠는지 등의 4가지 상태로 나누어서 AI로 실시간 학습태도를 관리할 수 있습니다.
          </p>
        </div> -->
        <div>
          <LandingComp></LandingComp>
        </div>
      </div>
      <div class=" section section4" :style="image_4">
        <!-- 데이터 시각화 도구 
        - 출결
        - 학습태도 데이터
        - 참여도 -->
        <!-- <div class="slide">
          <h3>Slide 4.1</h3>
        </div>
        <div class="slide">
          <h3>Slide 4.2</h3>
        </div>
        <div class="slide">
          <h3>Slide 4.3</h3>
        </div> -->
        <div class="section2a">
          <div class="explanation">
            <v-row>
              <v-col cols="10">
                <img src="@/assets/img/onlinestudy.jpeg" class="monitor" alt="computer" />
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="4">
                <div>
                  <h3>출결관리</h3>
                  <p>
                    Lorem ipsum dolor sit amet consectetur adipisicing elit. Enim magni veritatis, expedita maiores error.
                  </p>
                </div>
              </v-col>
              <v-col cols="4">
                <div>
                  <h3>출결관리</h3>
                  <p>
                    Lorem ipsum dolor sit amet consectetur adipisicing elit. Enim magni veritatis, expedita maiores error.
                  </p>
                </div>
              </v-col>
              <v-col cols="4">
                <div>
                  <h3>출결관리</h3>
                  <p>
                    Lorem ipsum dolor sit amet consectetur adipisicing elit. Enim magni veritatis, expedita maiores error.
                  </p>
                </div>
              </v-col>
            </v-row>
          </div>
        </div>
      </div>
    </full-page>
  </div>
</template>

<script>
import LandingComp from '@/views/LandingComp.vue';
export default {
  components: {
    LandingComp,
  },
  name: 'app',
  data() {
    return {
      options: {
        navigation: true,
        sectionsColor: ['require(', '#ff5f45', '#0798ec', '#fec401', '#1bcee6', '#ee1a59', '#2c3e4f', '#ba5be9', '#b4b8ab'],
      },
      serviceText: `<h3 style="color: #535353;"><span style="color:#38A897;">원격수업의 질</span>을 높여드리는<span style="color:#6173FF;"> '일름' </span>서비스</h3>
        <p class="section1-p">언택트시대 온라인 학습 일름이 해답입니다.</p>`,
    };
  },
  props: {
    image1: {
      type: String,
      default: require('@/assets/img/bg5.jpg'),
    },
    image2: {
      type: String,
      default: require('@/assets/img/bg2.jpg'),
    },
    image3: {
      type: String,
      default: require('@/assets/img/bg4.jpg'),
    },
    image4: {
      type: String,
      default: require('@/assets/img/bg1.jpg'),
    },
  },
  computed: {
    image_1() {
      return {
        backgroundImage: `url(${this.image1})`,
      };
    },
    image_2() {
      return {
        backgroundImage: `url(${this.image2})`,
      };
    },
    image_3() {
      return {
        backgroundImage: `url(${this.image3})`,
      };
    },
    image_4() {
      return {
        backgroundImage: `url(${this.image4})`,
      };
    },
  },
};
</script>
<style scoped>
@font-face {
  font-family: 'GongGothicMedium';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-10@1.0/GongGothicMedium.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}
@font-face {
  font-family: 'GongGothicLight';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-10@1.0/GongGothicLight.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}
.section {
  text-align: center;
  font-size: 1rem;
}
.section1 {
  z-index: -1;
  opacity: 0.9;
  background-size: cover;
}
.section2 {
  opacity: 0.4;
}
.section3 {
  /* opacity: 0.5; */
  display: flex;
  flex-direction: row;
}
/* .section4 {

} */

/* .backdrop {
  background: url('https://i.imgbox.com/JGXLxgQb.jpg');
  background-size: contain;
  height: 320px;
  margin-bottom: 40px;
  margin-top: -30px;
} */

.content {
  position: relative;
  top: 30%;
  padding: 20px 15px;
  margin: 15% auto;
  background: rgba(223, 223, 230, 0.7);

  /* filter: blur(10px); */
}

.content >>> .section1-p {
  color: #535353;
  font-size: 1.25em;
  font-family: 'GongGothicMedium', 'Serif';
}
.content >>> h3 {
  color: rgb(250, 248, 248);
  margin-bottom: 15px;
  font-size: 2em;
  font-family: 'GongGothicLight', 'Serif';
}

.section3-p {
  margin-top: 0px;
  color: hsl(210, 2%, 22%);
  font-size: 0.8em;
  line-height: 1eem;
  padding: 20px 20px;
  max-width: 44%;
  margin: auto;
  line-height: 32px;
  font-family: 'GongGothicLight';
}
.text-box {
  width: 100%;
  display: flex;
}
/* .fp-controlArrow {
  position: absolute;
  z-index: 4;
  top: 50%;
  cursor: pointer;
  width: 0;
  height: 0;
  border-style: solid;
  margin-top: -38px;
}
.fp-controlArrow.fp-prev {
  left: 30;
  border: none;
  width: 50px;
  height: 101px;

  cursor: pointer;
}
.fp-controlArrow.fp-prev {
  right: 30;
  border: none;
  width: 50px;
  height: 101px;

  cursor: pointer;
} */

.features {
  padding: 0;
  margin: 15% auto;
  display: flex;
  align-items: flex-start;
  flex-direction: column;
  justify-content: center;
}

.features h3 {
  color: hsl(210, 10%, 33%);
  font-size: 1.5em;
  padding-top: 20px;
}
.features li {
  list-style-type: none;
  width: 330px;
  margin: auto 15px;
  position: relative;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  flex-direction: column;
}

/* .section2 {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  padding-top: 50px;
} */

.section2a {
  text-align: left;
  line-height: 1.5em;
  display: flex;
  justify-content: space-around;
  align-items: center;
  flex-direction: row;
  margin-top: 30px;
  /* margin-right: 350px; */
}

.monitor {
  height: 50vh;
  width: auto;
  /* margin-top: 30px; */
  margin: 2% 30% 2% 30%;
}

.explanation {
  margin-left: 100px;
  width: 80%;
}
</style>
