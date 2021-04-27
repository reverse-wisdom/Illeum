<template>
  <div>
    <span>{{ this.$store.state.uuid }}의 평가목록</span>
    <v-row justify="center">
      <!-- <v-date-picker v-model="date" :allowed-dates="allowedDates" class="mt-4" min="mindate" max="maxdate"></v-date-picker> -->
      <v-date-picker v-model="date" :allowed-dates="allowedDates" class="mt-4" min="1900-04-01" max="2100-10-30"></v-date-picker>
      <v-col class="d-flex" cols="12" sm="6">
        <v-menu ref="menu" v-model="menu" :close-on-content-click="false" transition="scale-transition" offset-y min-width="auto">
          <template v-slot:activator="{ on, attrs }">
            <v-combobox v-model="date" chips small-chips label="Multiple picker in menu" prepend-icon="mdi-calendar" readonly v-bind="attrs" v-on="on"></v-combobox>
          </template>

          <v-btn text color="primary" @click="menu = false">
            Cancel
          </v-btn>
          <v-btn text color="primary" @click="$refs.menu.save(dates)">
            OK
          </v-btn>
        </v-menu>
      </v-col>
      <!-- <v-date-picker v-model="date"></v-date-picker> -->
    </v-row>
  </div>
</template>

<script>
import { userEvaluation } from '@/api/auth';
export default {
  data() {
    return {
      uuid: this.$store.state.uuid,
      eval: [],
      items: ['Foo', 'Bar', 'Fizz', 'Buzz'],
      date: '',
      menu: false,
      // date: '2018-03-02',
      mindate: '2021-04-01',
      maxdate: '2021-04-30',
      // arrayDates: ['2021-04-27', '2021-04-23'],
      arrayDates: [],
      roomName: [],
      // eval_date:''
    };
  },
  async created() {
    // const uuid = this.$store.state.uuid;
    // console.log(this.uuid);
    const { data } = await userEvaluation(this.uuid);

    for (var i = 0; i < data.length; i++) {
      var eval_date = data[i].eval_date.slice(0, 10);
      this.arrayDates.push(eval_date);
    }
    console.log(this.roomName);
    console.log(this.arrayDates);
  },
  methods: {
    // allowedDates(val) {
    //   var arrayDates = this.arrayDates;
    //   arrayDates.indexOf(val) !== -1;
    // },
    allowedDates(val) {
      for (var i = 0; i < this.arrayDates.length; i++) {
        if (this.arrayDates[i] == val) {
          // console.log(val);
          return true;
        }
      }
    },
    showClass() {
      console.log('test');
    },

    // allowedDates: (val) => parseInt(val.split('-')[2], 10) % 2 === 0,
  },
};
</script>

<style></style>
