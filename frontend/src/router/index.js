import Vue from 'vue';
import VueRouter from 'vue-router';
import Home from '../views/Home.vue';

// 테스트용 임포트 페이지
import webrtcTest from '../views/webrtcTest.vue';
import webrtcTest2 from '../views/webrtcTest2.vue';
import componentTest from '../views/componentTest.vue';
import chartTest from '../views/chartTest.vue';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
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
    path: '/componentTest',
    name: 'componentTest',
    component: componentTest,
  },
  {
    path: '/chartTest',
    name: 'chartTest',
    component: chartTest,
  },
  // end
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
