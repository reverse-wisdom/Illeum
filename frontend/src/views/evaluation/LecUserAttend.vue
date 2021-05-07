<template>
  <div>
    <h2>{{ each.uid }}의 출결시간</h2>
    <div>
      <div>출석시간:{{ each.attendTime }}</div>
      <div>지각여부: {{ attend }}</div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {};
  },
  props: {
    each: {
      type: Object,
    },

    attend: {
      type: String,
    },
  },
  async created() {},
};
</script>

<style></style>
