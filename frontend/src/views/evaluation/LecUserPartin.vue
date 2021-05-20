<template>
  <div>
    <div>
      <div style="display:flex; justify-content:center; flex-direction:column; margin:2rem 0;">
        <div class="partin-each">
          <span style="font-size:1.5rem;">{{ roomData.room_name }}수업</span>
          에서 총
          <span style="font-size:1.5rem;">{{ evalUserCnt }}명</span>
          중
        </div>
        <div class="partin-each">
          <span style="color:#756BFF; font-size:1.5rem; font-weight: bold;">수업참여도</span>
          는
          <span style="color:#756BFF; font-size:1.5rem; font-weight: bold;">{{ partuidRank }}위</span>
          입니다
        </div>

        <div class="partin-each">
          <span style="color:#FF625C; font-size:1.5rem; font-weight: bold;">출석</span>
          은
          <span style="color:#FF625C; font-size:1.5rem; font-weight: bold;">{{ attenduidRank }}위</span>
          입니다
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { evaluateList } from '@/api/class';

export default {
  data() {
    return {
      uuid: this.$store.state.uuid,
      name: '',
      eval: [],
      items: [],
      landscape: true,
      date: '',
      menu: false,

      partinRank: [],
      partuidRank: '',
      attendRank: [],
      attenduidRank: '',
      roomName: [],
      partData: [],
      attendData: [],
      selectedRoomName: '',
      maxUser: '',
      firstUser: '',
      fetchRoomlen: 0,
      evalcheck: false,
      zeroPartinchk: false,
    };
  },
  props: {
    each: {
      type: Object,
    },
    roomData: {
      type: Object,
    },
    evalUserCnt: {
      type: String,
    },
    rid: {
      type: Number,
    },
  },
  async created() {
    this.evalcheck = true;
    this.partinRank = [];
    this.attendRank = [];
    this.fetchRoomlen = 0;
    const res = await evaluateList(this.rid);
    console.log(res.data);
    console.log(this.each);
    var maxPartin = 0;
    var first = 100000;
    //채팅참여도1등, 출석1등 구하기
    console.log(res.data);
    for (var j = 0; j < res.data.length; j++) {
      if (res.data[j].eval_date.slice(0, 10) == this.each.eval_date.slice(0, 10)) {
        this.partinRank.push({ uid: res.data[j].uid, participation: res.data[j].participation, vid: res.data[j].vid });
        this.attendRank.push({ uid: res.data[j].uid, attend_time: res.data[j].attend_time });
        if (maxPartin < res.data[j].participation) {
          var maxPartin = res.data[j].participation;
          this.maxUser = res.data[j].name;
        }

        if (this.each.uid == res.data[j].uid) {
          this.attenduidRank = res.data[j].ranking;
        }
      }
    }
    //채팅참여도 배열 제일 많은순으로 정렬하기
    this.partinRank.sort(function(a, b) {
      if (a.participation < b.participation) {
        return 1;
      }
      if (a.participation > b.participation) {
        return -1;
      }

      return 0;
    });

    //로그인한 청강자 채팅참여도 순위 구하기
    for (var k = 0; k < this.partinRank.length; k++) {
      if (this.partinRank[k].uid === this.each.uid && this.partinRank[k].vid === this.each.vid) {
        this.partuidRank = this.partinRank.indexOf(this.partinRank[k]) + 1;
        break;
      } else if (this.partinRank[k].uid === this.$store.state.uuid && this.partinRank[k].participation == 0) {
        this.zeroPartinchk = true;
      }
    }
    //로그인한 청강자 출석시간 순위 구하기
    // for (var m = 0; m < this.attendRank.length; m++) {
    //   if (this.attendRank[m].uid === this.each.uid && this.partinRank[k].vid === this.each.vid) {
    //     this.attenduidRank = this.attendRank.indexOf(this.attendRank[m]) + 1;
    //     break;
    //   }
    // }
  },
};
</script>

<style></style>
