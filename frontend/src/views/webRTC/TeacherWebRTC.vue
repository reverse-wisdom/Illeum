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
      <v-btn class="mr-4" color="cyan" @click="saveMessageLog">채팅기록저장</v-btn>
      <v-btn class="mr-4" color="cyan" @click="chatTest">채팅콘솔테스트</v-btn>
      <v-btn class="mr-4" color="error" @click="outRoom">종료</v-btn>
    </div>
  </div>
</template>

<script>
import push from 'push.js';
import { updateClass } from '@/api/class';
export default {
  data() {
    return {
      roomid: '',
      userName: '',
      connection: null,
      message: '',
      chatLog: '',
      chatResult: [],
      isAudio: true,
      isVideo: true,
    };
  },
  created() {
    const name = this.$store.state.name;
    const room_name = this.$route.query.room_name;
    this.roomid = room_name;
    this.userName = name;
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
      localStream.unmute('audio');
      this.isAudio = true;
    },
    offAudio() {
      let localStream = this.connection.attachStreams[0];
      localStream.unmute('audio');
      localStream.mute('audio');
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
        console.log(userName + ' ' + uuid + ' ' + event.data.chatMessage + ' ' + timestamp);
        this.chatResult.push({ uuid: uuid, userName: userName, chatMessage: event.data.chatMessage, timestamp: timestamp });
        this.chatLog += userName + ' ' + uuid + ' ' + event.data.chatMessage + ' ' + timestamp + '\r\n';
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

      this.connection.extra.userFullName = this.$store.state.name;
      this.connection.extra.userUUID = this.$store.state.uuid;
      this.connection.extra.type = 'cam';
      this.connection.extra.isMaster = true;

      this.connection.session = {
        audio: true,
        video: true,
        data: true,
        // screen: true,
        // oneway: true,
      };

      this.connection.beforeAddingStream = function(stream, peer) {
        ref.connection.extra.userFullName = ref.$store.state.name;
        ref.connection.extra.userUUID = ref.$store.state.uuid;
        ref.connection.extra.type = 'cam';
        ref.connection.extra.isMaster = true;
        return stream;
      };

      this.connection.autoCloseEntireSession = true;

      this.connection.socketURL = 'https://rtcmulticonnection.herokuapp.com:443/';
      // this.connection.socketURL = 'https://illeum-webRTC:9001/';

      this.connection.sdpConstraints.mandatory = {
        OfferToReceiveAudio: true,
        OfferToReceiveVideo: false,
      };

      // 콘솔로그 출력 해제
      this.connection.enableLogs = false; // to disable logs
      // this.connection.enableLogs = true; // to enable logs

      this.connection.checkPresence(this.roomid, function(isRoomExist, roomid) {
        if (isRoomExist === true) {
          ref.connection.join(roomid);
        } else {
          ref.connection.onUserStatusChanged();

          push.create(ref.connection.extra.userFullName + '님이 ' + ref.roomid + '화상수업을 개설하였습니다');
          ref.connection.open(roomid);
        }
      });

      // 리스너 영역
      this.connection.onmessage = function(event) {
        if (event.data.chatMessage) {
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
        infoBar.innerHTML = '<b>참여자 목록:</b> ' + names.join(', ');
      };

      this.connection.onclose = function(event) {
        console.log('close');
      };
    },

    screen() {
      var ref = this;

      this.connection.extra.type = 'share';
      this.connection.extra.typeAlpha = 'share';

      this.connection.updateExtraData();

      this.connection.addStream({
        screen: true,
      });

      this.connection.videosContainer = document.querySelector('.share-videos-container');
    },
    async outRoom() {
      var ref = this;
      await updateClass({ rid: this.$route.query.rid, room_state: '준비' })
        .then(({ data }) => {
          if (data == 'success') {
            this.$swal({
              icon: 'success',
              title: '<h2>화상수업이 종료되었습니다.!!</h2>',
              toast: true,
              width: 600,
              padding: '2em',
              position: 'top-end',
              showConfirmButton: false,
              timer: 5000,
              timerProgressBar: true,
              didOpen: (toast) => {
                toast.addEventListener('mouseenter', ref.$swal.stopTimer);
                toast.addEventListener('mouseleave', ref.$swal.resumeTimer);
              },
            });
            ref.connection.autoCloseEntireSession = true;

            ref.connection.getAllParticipants().forEach((participantId) => {
              ref.connection.disconnectWith(participantId);
            });

            ref.connection.attachStreams.forEach(function(localStream) {
              localStream.stop();
            });

            ref.connection.closeSocket();
            ref.connection.disconnect();

            this.$router.push({ name: 'WebRTCList' });
          }
        })
        .catch((err) => {
          this.$swal({
            icon: 'error',
            title: '화상수업 종료 오류.!!',
          });
        });
    },
    saveMessageLog() {
      var fileName = this.roomid;
      var content = this.chatLog;
      var blob = new Blob([content], { type: 'text/plain' });
      var objURL = window.URL.createObjectURL(blob);

      // 이전에 생성된 메모리 해제
      if (window.__Xr_objURL_forCreatingFile__) {
        window.URL.revokeObjectURL(window.__Xr_objURL_forCreatingFile__);
      }

      window.__Xr_objURL_forCreatingFile__ = objURL;
      // a링크로 다운로드 바로실행
      var a = document.createElement('a');
      a.download = fileName;
      a.href = objURL;
      a.click();
    },
    chatTest() {
      var rankArr = [];
      var rank = 1;
      for (let index = 0; index < this.chatResult.length; index++) {
        var indexOfchatResult = rankArr.findIndex((i) => i.uuid == this.chatResult[index].uuid);
        if (indexOfchatResult == -1) {
          rankArr.push({ uuid: this.chatResult[index].uuid, participation: 1, rank: rank });
          rank++;
        } else {
          var indexOfObj = rankArr.findIndex((i) => i.uuid == this.chatResult[index].uuid);
          rankArr[indexOfObj].participation++;
        }
      }

      console.log(rankArr);
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
