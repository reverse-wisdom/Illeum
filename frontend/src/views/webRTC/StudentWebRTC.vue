<template>
  <div>
    <div class="class-name">
      <h2>{{ this.$route.query.room_name }}</h2>
    </div>
    <div class="drag-container">
      <div class="panel-one" id="drag-left">
        <div class="videos-container"></div>
        <div class="share-videos-container"></div>
      </div>
      <div class="dragbar" id="dragbar"></div>
      <div class="panel-two" id="drag-right">
        <div id="onUserStatusChanged"></div>
        <div id="conversation-panel"></div>
        <div id="key-press" style="text-align: right; display: none; font-size: 11px;">
          <span style="vertical-align: middle;"></span>
          <img src="https://www.webrtc-experiment.com/images/key-press.gif" style="height: 12px; vertical-align: middle;" />
        </div>
        <v-text-field id="txt-chat-message" sold v-model="message" dense label="채팅"></v-text-field>
        <button class="btn btn-primary" id="btn-chat-message" @click="chat">Send</button>
      </div>
    </div>
    <div class="panel-three">
      <template v-if="isAudio">
        <v-btn class="mr-4" color="error" @click="offAudio">오디오 끄기</v-btn>
      </template>
      <template v-else>
        <v-btn class="mr-4" color="primary" @click="onAudio">오디오 켜기</v-btn>
      </template>
      <template v-if="isVideo">
        <v-btn class="mr-4" color="error" @click="offVideo">비디오 끄기</v-btn>
      </template>
      <template v-else>
        <v-btn class="mr-4" color="primary" @click="onVideo">비디오 켜기</v-btn>
      </template>

      <v-btn class="mr-4" color="cyan" @click="screen">화면공유</v-btn>
      <v-btn class="mr-4" color="error" @click="outRoom">퇴장</v-btn>
      &nbsp;
    </div>
  </div>
</template>

<script>
import push from 'push.js';
import { faceAI } from '@/api/faceAI';
import { findUidAndRid } from '@/api/entrant';
import { exit, entrance } from '@/api/rabbit';
import { insertEvaluation } from '@/api/evaluation';

export default {
  data() {
    return {
      roomid: '',
      userName: '',
      connection: null,
      message: '',
      rid: '',
      isOutClicked: true,
      isAudio: true,
      isVideo: true,
    };
  },
  created() {
    this.roomid = this.$route.query.room_name;
    this.rid = this.$route.query.rid;
    this.userName = this.$store.state.name;
  },
  mounted() {
    let cdn1 = document.createElement('script');
    cdn1.setAttribute('src', 'https://cdn.jsdelivr.net/npm/rtcmulticonnection@latest/dist/RTCMultiConnection.min.js');
    cdn1.setAttribute('id', 'cdn1');
    document.body.appendChild(cdn1);

    let cdn2 = document.createElement('script');
    cdn2.setAttribute('src', 'https://rtcmulticonnection.herokuapp.com/socket.io/socket.io.js');
    cdn2.setAttribute('id', 'cdn2');
    document.body.appendChild(cdn2);

    var left = document.getElementById('drag-left');
    var right = document.getElementById('drag-right');
    var bar = document.getElementById('dragbar');

    const drag = (e) => {
      document.selection ? document.selection.empty() : window.getSelection().removeAllRanges();
      left.style.width = e.pageX - bar.offsetWidth / 2 + 'px';
    };

    bar.addEventListener('mousedown', () => {
      document.addEventListener('mousemove', drag);
    });

    bar.addEventListener('mouseup', () => {
      document.removeEventListener('mousemove', drag);
    });

    this.openRoom();
  },

  methods: {
    async screenCapture() {
      let video = document.querySelector('video');
      let canvas = document.createElement('canvas');
      canvas.width = video.videoWidth || video.clientWidth;
      canvas.height = video.videoHeight || video.clientHeight;

      var context = canvas.getContext('2d');
      context.drawImage(video, 0, 0, canvas.width, canvas.height);

      let img = canvas.toDataURL('image/png');

      let userData = {
        uid: this.$store.state.uuid,
        rid: this.$route.query.rid,
        snapshot: img,
      };

      const { data } = await faceAI(userData);
      console.log(data);
    },
    offVideo() {
      let localStream = this.connection.attachStreams[0];
      localStream.mute('video');
      this.isVideo = false;
    },
    onVideo() {
      this.connection.session.video = true;
      let localStream = this.connection.attachStreams[0];
      localStream.unmute('video');
      this.isVideo = true;
    },
    onAudio() {
      let localStream = this.connection.attachStreams[0];
      localStream.mute('audio');
      this.isAudio = true;
    },
    offAudio() {
      let localStream = this.connection.attachStreams[0];
      localStream.unmute('audio');
      this.connection.streamEvents.selectFirst('local').mediaElement.muted = true;
      this.isAudio = false;
    },
    chat() {
      var chatMessage = this.message;

      if (!chatMessage || !chatMessage.replace(/ /g, '').length) return;

      this.appendChatMessage(chatMessage, this.userName, this.$store.state.uuid);

      this.connection.send({
        chatMessage: chatMessage,
      });

      this.connection.send({
        typing: false,
      });

      this.message = '';
    },
    appendChatMessage(event, userName, uuid) {
      var conversationPanel = document.getElementById('conversation-panel');
      var div = document.createElement('div');
      var date = new Date();
      var timestamp = date.toLocaleTimeString();
      div.className = 'message';

      if (event.data) {
        div.innerHTML = '<b>' + userName + '&nbsp;' + timestamp + ':</b><br>' + event.data.chatMessage;
      } else {
        div.innerHTML = '<b>' + this.userName + '(당신)&nbsp;' + timestamp + '</b> <br>' + event;
        div.style.background = '#cbffcb';
      }

      conversationPanel.appendChild(div);

      conversationPanel.scrollTop = conversationPanel.clientHeight;
      conversationPanel.scrollTop = conversationPanel.scrollHeight - conversationPanel.scrollTop;
    },
    openRoom() {
      this.connection = new RTCMultiConnection();
      var ref = this;

      this.connection.session = {
        audio: true,
        video: true,
        data: true,
      };

      this.connection.socketURL = 'https://rtcmulticonnection.herokuapp.com:443/';
      // this.connection.socketURL = 'https://illeum-webRTC:9001/';
      this.connection.extra.userFullName = this.$store.state.name;
      this.connection.extra.userUUID = this.$store.state.uuid;
      this.connection.extra.type = 'cam';
      this.connection.sdpConstraints.mandatory = {
        OfferToReceiveAudio: true,
        OfferToReceiveVideo: true,
      };
      console.log(this.connection);

      // 콘솔로그 출력 해제
      this.connection.enableLogs = false; // to disable logs
      // this.connection.enableLogs = true; // to enable logs

      this.connection.autoCloseEntireSession = true;

      // 4초후 5초당 한번씩
      setTimeout(function() {
        setInterval(ref.screenCapture, 5000);
        console.log('캡쳐완료!');
      }, 4000);

      this.connection.checkPresence(this.roomid, function(isRoomExist, roomid) {
        if (isRoomExist === true) {
          ref.checkEntrant().then((result) => {
            if (!result) {
              ref.$swal({
                icon: 'error',
                title: '참여할 수 없는 화상수업입니다.!!',
              });
              ref.$router.push({ name: 'WebRTCList' });
              return;
            }
            ref.connection.onUserStatusChanged();
            push.create(ref.connection.extra.userFullName + '님이 ' + ref.roomid + '수업에 입장했습니다');
            ref.connection.join(roomid);
          });
        } else {
          ref.$swal({
            icon: 'error',
            title: '개설되지 않는 화상수업입니다.!!',
          });
          ref.$router.push({ name: 'WebRTCList' });
        }
      });

      // 리스너 영역
      this.connection.onmessage = function(event) {
        if (event.data.chatMessage) {
          console.log(event);
          ref.appendChatMessage(event, event.extra.userFullName, event.extra.userUUID);
          return;
        }
      };

      this.connection.onstream = function(event) {
        var video = event.mediaElement;

        if (event.extra.type == 'cam') {
          document.querySelector('.videos-container').appendChild(video);
          video.removeAttribute('controls');
        } else {
          document.querySelector('.share-videos-container').appendChild(video);
        }
      };

      this.connection.onUserStatusChanged = function(event) {
        try {
          if (typeof event.extra !== 'undefined') {
            if (event.extra.isMaster && event.status == 'offline') {
              ref.$swal({
                icon: 'warning',
                title: '현재 화상수업이 종료 되었습니다.!!',
              });

              ref.connection.getAllParticipants().forEach((participantId) => {
                ref.connection.disconnectWith(participantId);
              });

              ref.connection.attachStreams.forEach(function(localStream) {
                localStream.stop();
              });

              ref.connection.closeSocket();

              console.log(ref.connection);
              ref.$router.push({ name: 'WebRTCList' });
            }
          }
        } catch {
          console.log(event);
        }

        var infoBar = document.getElementById('onUserStatusChanged');
        var names = [];
        ref.connection.getAllParticipants().forEach(function(participantId) {
          var user = ref.connection.peers[participantId];
          names.push(user.extra.userFullName);
        });

        if (!names.length) {
          names = ['Only You'];
        } else {
          names = [ref.connection.extra.userFullName || 'You'].concat(names);
        }

        infoBar.innerHTML = '<b>Active users:</b> ' + names.join(', ');
      };

      this.connection.onEntireSessionClosed = function(event) {
        console.info('Entire session is closed: ', event.sessionid, event.extra);
      };
    },

    screen() {
      var ref = this;

      console.log(this.connection, '1');
      this.connection.extra.type = 'share';
      this.connection.extra.typeAlpha = 'share';

      this.connection.updateExtraData();
      console.log(this.connection.extra, '바뀐 엑스트라');

      this.connection.addStream({
        screen: true,
      });

      this.connection.videosContainer = document.querySelector('.share-videos-container');
      console.log('test when open', this.connection);
    },
    async outRoom() {
      var uid = this.$store.state.uuid;
      var rid = this.rid;
      await exit(uid, rid)
        .then(({ data }) => {
          if (data == 'success') {
            this.isOutClicked = true;

            this.connection.getAllParticipants().forEach((participantId) => {
              this.connection.disconnectWith(participantId);
            });

            this.connection.attachStreams.forEach(function(localStream) {
              localStream.stop();
            });

            this.connection.closeSocket();
            this.$swal({
              icon: 'warning',
              title: '화상수업 나가기.!!',
            });

            this.$router.push({ name: 'WebRTCList' });
          }
        })
        .catch((err) => {
          this.$swal({
            icon: 'error',
            title: '화상수업 나가기 오류.!!',
          });
        });
    },
    async checkEntrant() {
      var result = false;

      var uid = this.$store.state.uuid;
      var rid = this.rid;

      await findUidAndRid(uid, rid).then(async ({ data }) => {
        if (data != '') {
          await insertEvaluation({ uid, rid }).then(async ({ data }) => {
            if (data != '') {
              await entrance(uid, rid).then(async ({ data }) => {
                if (data != '') {
                  result = true;
                }
              });
            }
          });
        }
      });

      return result;
    },
  },

  destroyed() {
    // cdn 제거
    var el1 = document.querySelector('#cdn1');
    el1.remove();
    var el2 = document.querySelector('#cdn2');
    el2.remove();
  },
};
</script>

<style scoped>
body {
  -ms-overflow-style: none;
  overflow-y: hidden;
}
body::-webkit-scrollbar {
  display: none;
}

.class-name {
  text-align: center;
  border-bottom: 1px solid #e5e5e5;
}
.drag-container {
  display: flex;
  min-height: 85vh;
}

.panel-one {
  width: 80%;
  min-width: 500px;
}

.panel-two {
  flex: 1;
  width: 20%;
}
.panel-three {
  border-top: 2px solid black;
  padding-top: 5px;
}

.dragbar {
  padding: 2px;
  cursor: col-resize;
  background-color: black;
}
.img {
  width: inherit;
}

#txt-chat-message {
  width: 100%;
  resize: vertical;
  margin: 5px;
  margin-right: 0;
  min-height: 30px;
}

#btn-chat-message {
  margin: 5px;
}

#conversation-panel {
  margin-bottom: 20px;
  text-align: left;
  min-height: 700px;
  overflow: scroll;
  overflow-x: hidden;
  /* border-top: 1px solid #e5e5e5; */
  width: 100%;
}

#conversation-panel .message {
  border-bottom: 1px solid #e5e5e5;
  padding: 5px 10px;
}

#conversation-panel .message img,
#conversation-panel .message video,
#conversation-panel .message iframe {
  max-width: 100%;
}

.videos-container >>> video {
  display: inline;
  width: -webkit-fill-available;
  width: 30%;
  border: 1px solid;
  /* pointer-events: none; */
}
.share-videos-container >>> video {
  display: inline;
  width: -webkit-fill-available;
  width: 80%;
  border: 1px solid;
  /* pointer-events: none; */
}
</style>
