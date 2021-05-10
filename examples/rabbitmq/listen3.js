#!/usr/bin/env node

import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'

const socket  = new SockJS('http://localhost:8080');
// var ws = 'http://k4d106.p.ssafy.io:5672';
var client = Stomp.over(socket);
var headers = {
    login: 'illeum',
    passcode: 'illeum123',
    // additional header
    // 'client-id': 'my-client-id'
  };
client.connect(
    // headers,
  (frame) => {
    // 소켓 연결 성공
    console.log('소켓 연결 성공', frame);
    // 서버의 메시지 전송 endpoint를 구독합니다.
    // 이런형태를 pub sub 구조라고 합니다.
    client.subscribe('/member.10', (res) => {
      console.log('구독으로 받은 메시지 입니다.', res.body);
    //   console.log('구독으로 받은 메시지 입니다.', res.body);

      // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
    //   this.recvList.push(JSON.parse(res.body));
    });
  },
  (error) => {
    // 소켓 연결 실패
    console.log('소켓 연결 실패', error);
  }
);
