<template>
  <div>
    <h2>강의자관리페이지</h2>
  </div>
</template>

<script>
export default {
  async created() {
    console.log(this.$store.state.password);
    console.log(this.$store.state.uuid);
  },
};
</script>

<style></style>
