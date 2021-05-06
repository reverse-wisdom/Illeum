<template>
  <div>
    <h2>{{ each.uid }}의 출결시간</h2>
    <div>
      <div>출석시간:{{ each.attendTime }}</div>
      <div>지각여부:</div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    each: {
      type: Object,
    },
    rid: {
      type: Number,
    },
  },
  created() {
    console.log(this.each);
    console.log(this.rid);
  },
};
</script>

<style></style>
