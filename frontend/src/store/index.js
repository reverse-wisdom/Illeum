import Vue from 'vue';
import Vuex from 'vuex';
import createPersistedState from 'vuex-persistedstate';
import { loginUser } from '@/api/auth';
import router from '@/router/index';
Vue.use(Vuex);

export default new Vuex.Store({
  plugins: [createPersistedState()],
  state: {
    token: '',
    email: '',
    name: '',
    uuid: '',
    role: '',
  },
  getters: {
    isLogin(state) {
      return state.token !== '';
    },
  },
  mutations: {
    //토큰
    setToken(state, token) {
      state.token = token;
    },
    clearToken(state) {
      state.token = '';
    },

    //이메일
    setEmail(state, email) {
      state.email = email;
    },
    clearEmail(state) {
      state.email = '';
    },

    //비밀번호
    // setPassword(state, password) {
    //   state.password = password;
    // },
    // clearPassword(state) {
    //   state.password = '';
    // },

    //이름
    setName(state, name) {
      state.name = name;
    },
    clearName(state) {
      state.name = '';
    },

    //uuid
    setUuid(state, uuid) {
      state.uuid = uuid;
    },
    clearUuid(state) {
      state.uuid = '';
    },
    //role
    setRole(state, role) {
      state.role = role;
    },
    clearRole(state) {
      state.role = '';
    },
  },

  actions: {
    async LOGIN({ commit }, userData) {
      const { data } = await loginUser(userData);
      // console.log(data.accessToken);
      console.log(data);
      if (data.accessToken !== '') {
        commit('setToken', data.accessToken);
        commit('setUuid', data.member.uid);
        commit('setEmail', data.member.email);
        commit('setName', data.member.name);
        commit('setRole', data.member.role);

        router.push('/home');
      } else {
        Vue.swal({
          icon: 'error',
          title: '로그인 실패! 이메일 및 비밀번호를 확인해 주세요!',
        });
      }
    },
  },
});
