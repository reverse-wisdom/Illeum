<template>
  <div>
    <h2>수강자-진행</h2>
    <div v-if="learnerDoing == true">
      <p>수강진행중인 강좌가 있음</p>
      <div v-for="(item, idx) in classLi" :key="idx">
        <div v-if="item.room_state == '진행'">
          <p>{{ item.rid }}</p>
          <p>{{ item.founder }}</p>
          <p>{{ item.room_name }}</p>

          <p>{{ item.room_type }}</p>
          <p>{{ item.start_time }}</p>
        </div>
      </div>
    </div>
    <div v-else>
      <p>수강진행중인 강좌가 없음</p>
    </div>
  </div>
</template>

<script>
import { usePartinClass } from '@/api/auth.js';
export default {
  data() {
    return {
      learnerDoing: false,
      classLi: [],
    };
  },
  async created() {
    const { data } = await usePartinClass(this.$store.state.uuid);
    console.log(data);
    if (data) {
      this.learnerDoing = true;
      this.classLi = data;
    } else {
    }
  },
};
</script>

<style></style>
