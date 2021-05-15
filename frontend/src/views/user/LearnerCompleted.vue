<template>
  <div>
    <div v-if="learnerDoing == true">
      <v-row no-gutters>
        <template v-for="(item, idx) in classLi">
          <v-col :key="idx">
            <v-card flat color="#f4f4f4" width="700">
              <div v-if="new Date(item.end_time) < Date.now() && item.room_state == '준비'">
                <div class="blog-card">
                  <input type="radio" name="select" id="tap-1" checked />
                  <input type="checkbox" id="imgTap" />

                  <div class="inner-part">
                    <label for="imgTap" class="img">
                      <v-img :src="'/profile/' + item.founderUid + '/256'" id="preview" alt=""></v-img>
                    </label>
                    <div class="content content-1">
                      <span>클래스명:{{ item.room_name }}</span>

                      <div class="title">강의자:{{ item.founder }}</div>
                      <div class="text">진행단계:{{ item.room_state }}</div>
                      <div class="text">공개여부:{{ item.room_type }}</div>
                      <div class="text">시작시간:{{ item.start_time }}</div>
                      <div class="text">종료시간:{{ item.end_time }}</div>

                      <v-dialog v-if="$store.state.token" v-model="dialog" persistent max-width="1200px" :retain-focus="false">
                        <template v-slot:activator="{ on, attrs }">
                          <v-btn color="primary" dark v-bind="attrs" v-on="on" @click="evalShow(item)">
                            내평가보기
                          </v-btn>
                        </template>
                        <v-card>
                          <v-card-title>
                            <span class="headline">
                              <span>{{ name }}님의</span>
                              Evaluation
                            </span>
                          </v-card-title>
                          <v-card-text>
                            <v-container>
                              <v-card>
                                <v-toolbar flat color="primary" dark>
                                  <v-toolbar-title>
                                    <span>{{ name }}님의</span>
                                    Evaluation
                                  </v-toolbar-title>
                                </v-toolbar>
                                <v-tabs vertical>
                                  <v-tab>
                                    <v-icon left>
                                      mdi-account
                                    </v-icon>
                                    출결
                                  </v-tab>
                                  <v-tab>
                                    <v-icon left>
                                      mdi-lock
                                    </v-icon>
                                    학습태도
                                  </v-tab>
                                  <v-tab>
                                    <v-icon left>
                                      mdi-access-point
                                    </v-icon>
                                    참여현황
                                  </v-tab>

                                  <v-tab-item>
                                    <v-card flat>
                                      <h2>출결</h2>
                                      <h3>출석시간:{{ attend_time }}</h3>
                                      <h3>지각여부:{{ attend }}</h3>
                                    </v-card>
                                  </v-tab-item>
                                  <v-tab-item>
                                    <v-card flat>
                                      <MyPagePieChart :learnData="learnData" :key="change" />
                                      <MyPageRadarChart :learnData="learnData" :averageData="averageData" :key="renderKey" />
                                    </v-card>
                                  </v-tab-item>
                                  <v-tab-item>
                                    <v-card flat>
                                      <div>{{ name }}님</div>
                                      <div>{{ roomName }} 수업에서 수업참여도</div>
                                      <div>총 수강생 {{ evalUserCnt }}명중에 {{ partuidRank }}위입니다</div>
                                      <div>{{ roomName }} 수업에서 출석을</div>
                                      <div>총 수강생 {{ evalUserCnt }}명중에 {{ attenduidRank }}위입니다</div>
                                    </v-card>
                                  </v-tab-item>
                                </v-tabs>
                              </v-card>
                            </v-container>
                          </v-card-text>
                          <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="blue darken-1" text @click="dialog = false">
                              Close
                            </v-btn>
                          </v-card-actions>
                        </v-card>
                      </v-dialog>
                    </div>
                  </div>
                </div>
              </div>
            </v-card>
          </v-col>
          <v-responsive v-if="idx % 2 == 1" :key="`width-${idx}`" width="100%"></v-responsive>
        </template>
      </v-row>
    </div>
    <div v-else>
      <p>수강진행중인 강좌가 없음</p>
    </div>
  </div>
</template>

<script>
import { userEvalList } from '@/api/auth.js';
import { fetchRoomname, evaluateList } from '@/api/class';
import MyPagePieChart from '@/views/components/MyPagePieChart';
import MyPageRadarChart from '@/views/components/MyPageRadarChart';
export default {
  components: { MyPagePieChart, MyPageRadarChart },
  data() {
    return {
      learnerDoing: false,
      classLi: [],
      dialog: false,
      name: this.$store.state.name,
      attend_time: '',
      attend: '',
      roomName: '',
      change: 0,
      renderKey: 1,
      evalUserCnt: '',
      partinRank: [],
      partuidRank: '',
      attendRank: [],
      attenduidRank: '',
      fetchRoomlen: 0,
      evalcheck: false,
      averageData: [
        {
          data: '집중',
          per: 0,
        },
        {
          data: '딴짓',
          per: 0,
        },
        {
          data: '졸음',
          per: 0,
        },
        {
          data: '자리비움',
          per: 0,
        },
        {
          data: '참여도',
          per: 0,
        },
      ],

      learnData: [
        {
          data: '집중',
          per: 0,
        },
        {
          data: '딴짓',
          per: 0,
        },
        {
          data: '졸음',
          per: 0,
        },
        {
          data: '자리비움',
          per: 0,
        },
        {
          data: '참여도',
          per: 0,
        },
      ],
      per1: 0,
      per2: 0,
      per3: 0,
      per4: 0,
      per5: 0,
      resDatalen: 0,
    };
  },
  async created() {
    this.classLi = [];
    this.partinRank = [];
    this.attendRank = [];

    const { data } = await userEvalList(this.$store.state.uuid);
    console.log(data);
    if (data) {
      this.learnerDoing = true;
      this.classLi = data;
    }
  },

  methods: {
    async evalShow(value) {
      console.log('value', value);
      //출결
      this.attend_time = value.attend_time;
      this.attend = value.attend;
      //평가
      this.learnData[0].per = value.attention;
      this.learnData[1].per = value.distracted;
      this.learnData[2].per = value.asleep;
      this.learnData[3].per = value.afk;
      this.learnData[4].per = value.participation;
      const { data } = await fetchRoomname(value.room_name);
      const roomPartinUser = data[0].rid;
      const res = await evaluateList(roomPartinUser);

      for (var i = 0; i < res.data.length; i++) {
        this.per1 += res.data[i].attention;
        this.per2 += res.data[i].distracted;
        this.per3 += res.data[i].asleep;
        this.per4 += res.data[i].afk;
        this.per5 += res.data[i].participation;
      }

      //소수둘째자리에서 반올림해서 소수첫째자리까지 보여줌
      this.averageData[0].per = (this.per1 / res.data.length).toFixed(1);
      this.averageData[1].per = (this.per2 / res.data.length).toFixed(1);
      this.averageData[2].per = (this.per3 / res.data.length).toFixed(1);
      this.averageData[3].per = (this.per4 / res.data.length).toFixed(1);
      this.averageData[4].per = (this.per5 / res.data.length).toFixed(1);

      //-------------참여도구하기
      // this.evalcheck = true;

      this.roomName = value.room_name;
      this.partinRank = [];
      this.attendRank = [];
      this.fetchRoomlen = 0;

      var maxPartin = 0;
      var first = 100000;
      //채팅참여도1등, 출석1등 구하기
      for (var j = 0; j < res.data.length; j++) {
        this.partinRank.push({ uid: res.data[j].uid, participation: res.data[j].participation });
        // attendRank push 메소드 시간순으로 정렬됨
        this.attendRank.push({ uid: res.data[j].uid, attend_time: res.data[j].attend_time });
        this.attendRank.sort(function(a, b) {
          return a.attend_time < b.attend_time ? -1 : a.attend_time > b.attend_time ? 1 : 0;
        });
        if (maxPartin < res.data[j].participation) {
          var maxPartin = res.data[j].participation;
          this.maxUser = res.data[j].name;
        }

        if (first > res.data[j].ranking) {
          var first = res.data[j].ranking;
          this.firstUser = res.data[j].name;
        }
        if (this.$store.state.uuid === res.data[j].uid) {
          this.name = this.$store.state.name;
          this.fetchRoomlen = res.data.length;
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
        if (this.partinRank[k].uid === this.$store.state.uuid) {
          this.partuidRank = this.partinRank.indexOf(this.partinRank[k]) + 1;
        }
      }
      //로그인한 청강자 출석시간 순위 구하기

      for (var m = 0; m < this.attendRank.length; m++) {
        if (this.attendRank[m].uid === this.$store.state.uuid) {
          this.attenduidRank = this.attendRank.indexOf(this.attendRank[m]) + 1;
        }
      }
      this.evalUserCnt = this.attendRank.length;
      this.change++;
      this.renderKey++;
    },
  },
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.blog-card {
  display: grid;
  /* position: absolute; */
  height: 270px;
  width: 80%;
  max-width: 1200px;
  margin: 20px auto;
  border-radius: 25px;
  background: white;
  box-shadow: 0px 5px 10px rgba(0, 0, 0, 0.5);
}
.inner-part {
  position: absolute;
  display: flex;
  height: 280px;
  align-items: center;
  justify-content: center;
  padding: 0 25px;
}
#imgTap:checked ~ .inner-part {
  padding: 0;
  transition: 0.1s ease-in;
}
.inner-part .img {
  height: 220px;
  width: 220px;
  flex-shrink: 0;
  overflow: hidden;
  border-radius: 20px;
  box-shadow: 2px 3px 15px rgba(0, 0, 0, 0.1);
}
#imgTap:checked ~ .inner-part .img {
  height: 370px;
  width: 850px;
  z-index: 99;
  margin-top: 10px;
  transition: 0.3s 0.2s ease-in;
}
.img img {
  height: 100%;
  width: 100%;
  object-fit: cover;
  cursor: pointer;
  opacity: 0;
  transition: 0.6s;
}
#tap-1:checked ~ .inner-part .img-1 {
  opacity: 1;
  transition-delay: 0.2s;
}
.content {
  padding: 0 10px 0 10px;
  width: 380px;
  margin-left: 40px;

  transition: 0.6s;
}
#imgTap:checked ~ .inner-part .content {
  display: none;
}
#tap-1:checked ~ .inner-part .content-1 {
  opacity: 1;
  margin-left: 20px;
  z-index: 100;
  transition-delay: 0.3s;
}
.content span {
  display: block;
  color: #6173ff;
  margin-bottom: 10px;
  font-size: 22px;
  font-weight: 900;
}
.content .title {
  font-size: 30px;
  font-weight: 700;
  color: #0d0925;
  margin-bottom: 10px;
}
.content .text {
  color: #4e4a67;
  font-size: 15px;
  margin-bottom: 10px;
  line-height: 1.2em;
  text-align: justify;
  overflow: hidden;
}
.content .button {
  display: inline-flex;
  padding: 35px 40px;
  font-size: 16px;
  text-transform: uppercase;
  color: #fff0e6;
  font-weight: 600;
  letter-spacing: 1px;
  border-radius: 50px;
  cursor: pointer;
  outline: none;
  border: 1px solid #1976d2;
  background: #1976d2;
}
.content .button:hover {
  background: #1976d2;
}

input[type='radio'],
input[type='checkbox'] {
  display: none;
}
</style>
