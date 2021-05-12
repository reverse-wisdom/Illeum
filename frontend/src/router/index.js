import Vue from 'vue';
import VueRouter from 'vue-router';
import LandingComp from '../views/LandingComp.vue';
import Landing from '../views/Landing.vue';
import About from '../views/About.vue';
import Navbar from '../components/layout/Navbar.vue';
import Guide from '../views/components/Guide.vue';
import Clock from '../views/components/Clock.vue';

//user
import Sign from '../views/user/Sign.vue';
import MyClass from '../views/MyClass.vue';

//class
import ClassTest from '../views/class/ClassTest.vue';
import ClassSearch from '../views/class/ClassSearch.vue';
import ClassCreate from '../views/class/ClassCreate.vue';
import UserClasslist from '../views/class/UserClasslist.vue';
import ClassList from '../views/class/ClassList.vue';
import ClassJoin from '../views/class/ClassJoin.vue';

//WebRTC
import WebRTCList from '../views/webRTC/WebRTCList.vue';
import TeacherWebRTC from '../views/webRTC/TeacherWebRTC.vue';
import StudentWebRTC from '../views/webRTC/StudentWebRTC.vue';

//evaluation
import UserEval from '../views/evaluation/UserEval.vue';
import UserEvaluation from '../views/UserEvaluation.vue';
import LecturerManage from '../views/evaluation/LecturerManage.vue';
import LecSelectedEval from '../views/evaluation/LecSelectedEval.vue';

// 테스트용 임포트 페이지
import navTest from '../views/testpage/navTest.vue';
import calendarTest from '../views/testpage/calendarTest.vue';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Landing',
    components: { default: Landing },
  },
  {
    path: '/home',
    name: 'Home',
    components: { default: About, header: Navbar },
  },
  {
    path: '/guide',
    name: 'Guide',
    components: { default: Guide },
  },
  {
    path: '/landingcomp',
    name: 'LandingComp',
    components: { default: LandingComp },
  },
  {
    path: '/clock',
    name: 'Clock',
    components: { default: Clock },
  },
  {
    path: '/lecturermanage',
    name: 'LecturerManage',
    components: { default: LecturerManage, header: Navbar },
  },

  {
    path: '/lecselectedeval',
    name: 'LecSelectedEval',
    components: { default: LecSelectedEval, header: Navbar },
  },
  {
    path: '/sign',
    name: 'Sign',
    components: { default: Sign },
  },
  {
    path: '/myclass',
    name: 'MyClass',
    components: { default: MyClass, header: Navbar },
  },
  //class
  {
    path: '/classtest',
    name: 'ClassTest',
    components: { default: ClassTest, header: Navbar },
  },
  {
    path: '/classsearch',
    name: 'ClassSearch',
    components: { default: ClassSearch, header: Navbar },
  },
  {
    path: '/classcreate',
    name: 'ClassCreate',
    components: { default: ClassCreate, header: Navbar },
  },
  {
    path: '/userclasslist',
    name: 'UserClasslist',
    components: { default: UserClasslist, header: Navbar },
  },
  {
    path: '/classlist',
    name: 'ClassList',
    components: { default: ClassList, header: Navbar },
  },
  {
    path: '/classjoin',
    name: 'ClassJoin',
    components: { default: ClassJoin, header: Navbar },
  },
  //webRTC
  {
    path: '/webrtclist',
    name: 'WebRTCList',
    components: { default: WebRTCList, header: Navbar },
  },
  {
    path: '/teacherwebrtc',
    name: 'TeacherWebRTC',
    components: { default: TeacherWebRTC },
  },
  {
    path: '/studentwebrtc',
    name: 'StudentWebRTC',
    components: { default: StudentWebRTC },
  },
  //evaluation
  {
    path: '/usereval',
    name: 'UserEval',
    components: { default: UserEval, header: Navbar },
  },
  {
    path: '/userevaluation',
    name: 'UserEvaluation',
    // components: { default: UserEvaluation },
    components: { default: UserEvaluation, header: Navbar },
  },

  // 테스트용 라우터 세팅
  {
    path: '/navTest',
    name: 'navTest',
    component: navTest,
  },
  {
    path: '/calendarTest',
    name: 'calendarTest',
    component: calendarTest,
  },
  // // end
  {
    path: '/about',
    name: 'About',
    component: () => import('../views/About.vue'),
  },
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
});

export default router;
