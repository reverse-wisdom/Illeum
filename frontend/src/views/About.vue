<template>
  <div>
    <h2>about</h2>
    <v-btn @click="gouserEval">멤버의 평가목록</v-btn>
    <v-btn @click="gouserClassLi">멤버가 개설한 목록</v-btn>
    <router-link to="/lecturermanage">강의자관리</router-link>
  </div>
</template>

<script>
export default {
  methods: {
    gouserEval() {
      this.$router.push({ name: 'UserEval' });
    },
    gouserClassLi() {},
  },
};
</script>

<style></style>
