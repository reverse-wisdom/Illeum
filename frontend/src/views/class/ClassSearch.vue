<template>
  <div class="class-search">
    <!-- search -->
    <div class="box">
      <h1 class="title-search" style="">클래스명을 검색하세요!</h1>
      <v-row class="text-center" style="height: 10px">
        <!-- <div class="text-center searchRank">
            <v-card color="dark" max-height="100px">
              <Ranking></Ranking>
            </v-card>
          </div> -->
      </v-row>
      <div>
        <input type="text" placeholder="검색" id="searchWord" v-model="word" />
        <input type="button" name="" value="Search" @click="detailSearch" />
      </div>
    </div>
  </div>
</template>

<script>
export default {};
</script>

<style scoped>
/* 검색창 */
h1 {
  margin-bottom: 20px;
  padding: 0;
  color: #000;
  font-size: 24px;
}
.box {
  position: absolute;
  /* top: 25%; */
  left: 30%;
  width: auto;
  margin: 0 auto;
  margin-bottom: 200px;
}
input {
  position: relative;
  display: inline-block;
  font-size: 20px;
  box-sizing: border-box;
}
input[type='text'] {
  /* border: #000; */
  background: #eaeaea;
  width: 50vw;
  height: 50px;
  border: none;
  border-radius: 25px 0 0 25px;
  padding: 0 25px;
  outline: none;
  color: #000;
}
input[type='button'] {
  position: relative;
  /* left: -5px; */
  border-radius: 0 25px 25px 0;
  height: 50px;
  width: 11vw;
  border: none;
  outline: none;
  cursor: pointer;
  background: rgb(252, 79, 79);
  color: #fff;
}
.class-search {
  margin-left: 20%;
  width: 100%;
  height: 100vh;
  background-size: cover;
  background: url(../../assets/img/desk2_1.jpg) no-repeat;
}
</style>
