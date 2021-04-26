import Vue from 'vue';
import VueRouter from 'vue-router';
import Landing from '../views/Landing.vue';
import Navbar from '../components/layout/Navbar.vue';

//user
import Sign from '../views/user/Sign.vue';

//class
import ClassSearch from '../views/class/ClassSearch.vue';
// 테스트용 임포트 페이지

import webrtcTest from '../views/testpage/webrtcTest.vue';
import webrtcTest2 from '../views/testpage/webrtcTest2.vue';
import webrtcTest3 from '../views/testpage/webrtcTest3.vue';
import webrtcTest4 from '../views/testpage/webrtcTest4.vue';
import componentTest from '../views/testpage/componentTest.vue';
import chartTest from '../views/testpage/chartTest.vue';
Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Home',
    components: { default: Navbar },
  },
  {
    path: '/landing',
    name: 'Landing',
    components: { default: Landing },
  },
  {
    path: '/sign',
    name: 'Sign',
    components: { default: Sign },
  },
  //class
  {
    path: '/classsearch',
    name: 'ClassSearch',
    components: { default: ClassSearch, header: Navbar },
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
    path: '/chartTest',
    name: 'chartTest',
    component: chartTest,
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
