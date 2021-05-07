import Vue from 'vue';
import VueRouter from 'vue-router';
import LandingComp from '../views/LandingComp.vue';
import Landing from '../views/Landing.vue';
import About from '../views/About.vue';
import Navbar from '../components/layout/Navbar.vue';

//user
import Sign from '../views/user/Sign.vue';
import MyClass from '../views/MyClass.vue';

//class
import ClassTest from '../views/class/ClassTest.vue';
import ClassSearch from '../views/class/ClassSearch.vue';
import ClassCreate from '../views/class/ClassCreate.vue';
import UserClasslist from '../views/class/UserClasslist.vue';
import ClassList from '../views/class/ClassList.vue';
import Class from '../views/class/Class.vue';
import ClassMaster from '../views/class/ClassMaster.vue';
import ClassJoin from '../views/class/ClassJoin.vue';

//WebRTC
import WebRTCList from '../views/webRTC/WebRTCList.vue';

//evaluation
import UserEval from '../views/evaluation/UserEval.vue';
import UserEvaluation from '../views/UserEvaluation.vue';
import LecturerManage from '../views/evaluation/LecturerManage.vue';
// 테스트용 임포트 페이지

import webrtcTest from '../views/testpage/webrtcTest.vue';
import webrtcTest2 from '../views/testpage/webrtcTest2.vue';
import webrtcTest3 from '../views/testpage/webrtcTest3.vue';
import webrtcTest4 from '../views/testpage/webrtcTest4.vue';
import componentTest from '../views/testpage/componentTest.vue';
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
    path: '/landingcomp',
    name: 'LandingComp',
    components: { default: LandingComp },
  },
  // {
  //   path: '/landing',
  //   name: 'Landing',
  //   components: { default: Landing },
  // },
  {
    path: '/lecturermanage',
    name: 'LecturerManage',
    components: { default: LecturerManage, header: Navbar },
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
    components: { default: ClassCreate },
  },
  {
    path: '/userclasslist',
    name: 'UserClasslist',
    components: { default: UserClasslist, header: Navbar },
  },
  {
    path: '/classlist',
    name: 'ClassList',
    components: { default: ClassList },
  },
  {
    path: '/class',
    name: 'Class',
    components: { default: Class },
  },
  {
    path: '/classmaster',
    name: 'ClassMaster',
    components: { default: ClassMaster },
  },
  {
    path: '/classjoin',
    name: 'ClassJoin',
    components: { default: ClassJoin },
  },
  //webRTC
  {
    path: '/webrtclist',
    name: 'WebRTCList',
    components: { default: WebRTCList },
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
    components: { default: UserEvaluation, header: Navbar },
  },

  // 테스트용 라우터 세팅
  {
    path: '/webrtctest',
    name: 'webrtcTest',
    component: webrtcTest,
  },
  {
    path: '/webrtctest2',
    name: 'webrtcTest2',
    component: webrtcTest2,
  },
  {
    path: '/webrtctest3',
    name: 'webrtcTest3',
    component: webrtcTest3,
  },
  {
    path: '/webrtctest4',
    name: 'webrtcTest4',
    component: webrtcTest4,
  },
  {
    path: '/componentTest',
    name: 'componentTest',
    component: componentTest,
  },

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
