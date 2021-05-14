<template>
  <div class="learner">
    <v-tabs v-model="tab" background-color="#4cb4a5" slider-size="2" dark>
      <v-tabs-slider color="#fed327"></v-tabs-slider>
      <v-tab>
        준비
      </v-tab>
      <v-tab>
        진행
      </v-tab>
      <v-tab>
        완료
      </v-tab>
      <v-tab>
        종료
      </v-tab>
    </v-tabs>
    <v-tabs-items v-model="tab" width="1500" color="f4f4f4" style="background-color:#f4f4f4;">
      <v-tab-item>
        <v-card flat color="#f4f4f4">
          <LecturerReady>준비</LecturerReady>
        </v-card>
      </v-tab-item>
      <v-tab-item>
        <v-card flat>
          <LecturerDoing>진행</LecturerDoing>
        </v-card>
      </v-tab-item>
      <v-tab-item>
        <v-card flat>
          <LecturerCompleted>완료</LecturerCompleted>
        </v-card>
      </v-tab-item>
      <v-tab-item>
        <v-card flat>
          <LecturerEnd>종료</LecturerEnd>
        </v-card>
      </v-tab-item>
    </v-tabs-items>
  </div>
</template>

<script>
import LecturerReady from '@/views/user/LecturerReady.vue';
import LecturerDoing from '@/views/user/LecturerDoing.vue';
import LecturerCompleted from '@/views/user/LecturerCompleted.vue';
import LecturerEnd from '@/views/user/LecturerEnd.vue';

export default {
  components: { LecturerReady, LecturerDoing, LecturerCompleted, LecturerEnd },
  data() {
    return {
      tab: null,
    };
  },
};
</script>

<style scoped>
.learner {
  width: 1400px;
}
.v-tab {
  font-size: 1.2rem;
  font-weight: 900;
}
.v-tab-item {
  background-color: #000;
}
</style>
