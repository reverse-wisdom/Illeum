<template>
  <div>
    <div class="class-name">
      <h2>클래스이름</h2>
    </div>
    <div class="drag-container">
      <div class="panel-one" id="drag-left">
        <div class="videos-container"></div>
        <v-text-field v-model="roomid" label="방번호 입력"></v-text-field>
        <br />
        <v-text-field v-model="userName" label="닉네임 입력"></v-text-field>
        <v-btn depressed color="primary" @click="openRoom">open or join</v-btn>
        <v-btn depressed color="warning" @click="outRoom">퇴장</v-btn>
      </div>
      <div class="dragbar" id="dragbar"></div>
      <div class="panel-two" id="drag-right">
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
      <v-btn depressed color="primary" @click="onVideo">비디오 켜기</v-btn>
      <v-btn depressed color="warning" @click="offVideo">비디오 끄기</v-btn>
    </div>
    <!-- <div id="widget-container" style="position: fixed;bottom: 0;right: 0;left: 20%;height: 100%;border: 1px solid black; border-top:0; border-bottom: 0;"></div> -->
    <div id="widget-container"></div>
  </div>
</template>

<script>
import push from 'push.js';

export default {
  mounted() {
    var ref = this;
    let cdn1 = document.createElement('script');
    cdn1.setAttribute('src', 'https://cdn.jsdelivr.net/npm/rtcmulticonnection@latest/dist/RTCMultiConnection.min.js');
    cdn1.setAttribute('id', 'cdn1');
    document.body.appendChild(cdn1);

    let cdn2 = document.createElement('script');
    cdn2.setAttribute('src', 'https://rtcmulticonnection.herokuapp.com/socket.io/socket.io.js');
    cdn2.setAttribute('id', 'cdn2');
    document.body.appendChild(cdn2);

    // let cdn3 = document.createElement('script');
    // cdn2.setAttribute('src', 'https://www.webrtc-experiment.com/Canvas-Designer/canvas-designer-widget.js');
    // cdn2.setAttribute('id', 'cdn3');
    // document.body.appendChild(cdn3);

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

    this.designer = new window.CanvasDesigner();
    this.designer.widgetHtmlURL = 'https://www.webrtc-experiment.com/Canvas-Designer/widget.html'; // you can place this file anywhere
    this.designer.widgetJsURL = 'https://www.webrtc-experiment.com/Canvas-Designer/widget.js';

    this.designer.setSelected('pencil');

    this.designer.setTools({
      pencil: true,
      text: true,
      image: true,
      pdf: true,
      eraser: true,
      line: true,
      arrow: true,
      dragSingle: true,
      dragMultiple: true,
      arc: true,
      rectangle: true,
      quadratic: false,
      bezier: true,
      marker: true,
      zoom: false,
      lineWidth: false,
      colorsPicker: false,
      extraOptions: false,
      code: false,
      undo: true,
    });
    this.designer.appendTo(document.getElementById('widget-container'));

    this.designer.addSyncListener(function(data) {
      console.log('sync canvas');
      ref.connection.send(data);
    });
  },
  created() {},
  data() {
    return {
      roomid: '',
      userName: '',
      connection: null,
      message: '',
      designer: null,
    };
  },
  methods: {
    offVideo() {
      let localStream = this.connection.attachStreams[0];
      localStream.mute('video');
    },
    onVideo() {
      this.connection.session.video = true;
      let localStream = this.connection.attachStreams[0];
      localStream.unmute('video');
    },
    chat() {
      var chatMessage = this.message;
      console.log(this.userName);

      if (!chatMessage || !chatMessage.replace(/ /g, '').length) return;

      this.appendChatMessage(chatMessage, this.userName);

      this.connection.send({
        chatMessage: chatMessage,
      });

      this.connection.send({
        typing: false,
      });
    },
    appendChatMessage(event, userName) {
      var conversationPanel = document.getElementById('conversation-panel');
      var div = document.createElement('div');

      div.className = 'message';

      if (event.data) {
        div.innerHTML = '<b>' + userName + ':</b><br>' + event.data.chatMessage;
      } else {
        div.innerHTML = '<b>' + this.userName + '(당신)</b> <br>' + event;
        div.style.background = '#cbffcb';
      }

      conversationPanel.appendChild(div);

      conversationPanel.scrollTop = conversationPanel.clientHeight;
      conversationPanel.scrollTop = conversationPanel.scrollHeight - conversationPanel.scrollTop;
    },
    openRoom() {
      var ref = this;

      this.connection = new RTCMultiConnection();
      this.connection.socketMessageEvent = 'canvas-test';

      this.connection.session = {
        audio: true,
        video: true,
        data: true,
        // screen: true,
        // oneway: true,
      };

      this.connection.socketURL = 'https://rtcmulticonnection.herokuapp.com:443/';
      this.connection.extra.userFullName = this.userName;
      this.connection.sdpConstraints.mandatory = {
        OfferToReceiveAudio: true,
        OfferToReceiveVideo: true,
      };

      // 콘솔로그 출력 해제
      // this.connection.enableLogs = false; // to disable logs
      this.connection.enableLogs = true; // to enable logs

      this.connection.openOrJoin(this.roomid);
      this.connection.videosContainer = document.querySelector('.videos-container');

      push.create(this.connection.extra.userFullName + '님이 ' + this.roomid + '방에 입장했습니다');

      // 채팅부분영역 시작

      this.connection.onopen = function() {
        if (ref.designer.pointsLength <= 0) {
          // you seems having data to be synced with new user!
          setTimeout(function() {
            ref.connection.send('plz-sync-points');
          }, 1000);
        }
      };
      this.connection.onmessage = function(event) {
        if (event.data === 'plz-sync-points') {
          console.log(ref.designer);
          ref.designer.sync();
          return;
        }

        ref.designer.syncData(event.data);

        if (event.data.chatMessage) {
          ref.appendChatMessage(event, event.extra.userFullName);
          return;
        }
      };

      // this.designer.addSyncListener(function(data) {
      //   console.log("sync canvas")
      //   ref.connection.send(data);
      // });
    },

    outRoom() {
      this.connection.getAllParticipants().forEach((participantId) => {
        this.connection.disconnectWith(participantId);
      });

      this.connection.attachStreams.forEach(function(localStream) {
        localStream.stop();
      });

      this.connection.closeSocket();
    },
  },

  destroyed() {
    // cdn 제거
    var el1 = document.querySelector('#cdn1');
    el1.remove();
    var el2 = document.querySelector('#cdn2');
    el2.remove();
    // var el3 = document.querySelector('#cdn3');
    // el3.remove();
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
}
</style>
