<template>
  <v-app id="app">
    <router-view name="header" />
    <router-view />
  </v-app>
</template>

<script>

var ws = new WebSocket('ws://k4d106.p.ssafy.io:15674/ws');
var client = Stomp.over(ws);
var headers = {
  login: 'illeum-guest',
  passcode: 'illeum-guest',
  // additional header
  // 'client-id': 'my-client-id'
};
var connectCallback = function() {
  client.subscribe('/amq/queue/member.10', (res) => {
    console.log('구독으로 받은 메시지', res.body);
  }); //큐명을 지정한경우 시 사용
};

var errorCallback = function(error) {
  console.log('소켓 연결 실패', error);
  //alert(error.headers.message);
};

var id = 'illeum';
var pass = 'illeum123';

client.connect(id, pass, connectCallback, errorCallback, '/');
</script>

<style>
body {
  -ms-overflow-style: none;
  overflow-y: hidden;
}
body::-webkit-scrollbar {
  display: none;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
