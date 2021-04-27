<template>
  <div>
    <div class="drag-container">
      <div class="panel-one" id="drag-left">
        <div class="videos-container"></div>
        <input v-model="roomid" placeholder="Unique room ID" />
        <v-btn depressed color="primary" @click="openRoom">화면공유시작</v-btn>
        <v-btn depressed color="warning" @click="outRoom">퇴장</v-btn>
      </div>
      <div class="dragbar" id="dragbar"></div>
      <div class="panel-two" id="drag-right">
        <h2>Panel 2</h2>
        <img class="img" src="https://i0.wp.com/kiramonthly.com/wp-content/uploads/2020/02/1.jpg?fit=1000%2C1429" />
        <img class="img" src="https://img.huffingtonpost.com/asset/5d80b5133b00002efad5453a.jpeg?ops=scalefit_630_noupscale" />
      </div>
    </div>
    <div>
      <h2>Panel 3</h2>
    </div>
  </div>
</template>

<script>
export default {
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
  },
  data() {
    return {
      roomid: '',
      connection: null,
    };
  },
  methods: {
    openRoom() {
      this.connection = new RTCMultiConnection();

      this.connection.session = {
        screen: true,
        oneway: true,
      };

      this.connection.socketURL = 'https://rtcmulticonnection.herokuapp.com:443/';

      this.connection.sdpConstraints.mandatory = {
        OfferToReceiveAudio: true,
        OfferToReceiveVideo: true,
      };

      this.connection.openOrJoin(this.roomid);

      this.connection.iceServers = [
        {
          urls: ['stun:stun.l.google.com:19302', 'stun:stun1.l.google.com:19302', 'stun:stun2.l.google.com:19302', 'stun:stun.l.google.com:19302?transport=udp'],
        },
      ];

      this.connection.videosContainer = document.querySelector('.videos-container');

      console.log(document.querySelector('.videos-container'));

      this.connection.onstream = function(event) {
        var existing = document.getElementById(event.streamid);
        if (existing && existing.parentNode) {
          existing.parentNode.removeChild(existing);
        }

        event.mediaElement.removeAttribute('src');
        event.mediaElement.removeAttribute('srcObject');
        event.mediaElement.muted = true;
        event.mediaElement.volume = 0;

        var video = document.createElement('video');

        try {
          video.setAttributeNode(document.createAttribute('autoplay'));
          video.setAttributeNode(document.createAttribute('playsinline'));
        } catch (e) {
          video.setAttribute('autoplay', true);
          video.setAttribute('playsinline', true);
        }

        if (event.type === 'local') {
          video.volume = 0;
          try {
            video.setAttributeNode(document.createAttribute('muted'));
          } catch (e) {
            video.setAttribute('muted', true);
          }
        }
        video.srcObject = event.stream;

        var width = innerWidth - 80;
        var mediaElement = getHTMLMediaElement(video, {
          title: event.userid,
          buttons: ['full-screen'],
          width: width,
          showOnMouseEnter: false,
        });

        connection.videosContainer.appendChild(mediaElement);

        setTimeout(function() {
          mediaElement.media.play();
        }, 5000);

        mediaElement.id = event.streamid;
      };

      var ref = this;

      this.connection.onMediaError = function(e) {
        if (e.message === 'Concurrent mic process limit.') {
          if (DetectRTC.audioInputDevices.length <= 1) {
            alert('Please select external microphone. Check github issue number 483.');
            return;
          }

          var secondaryMic = DetectRTC.audioInputDevices[1].deviceId;
          connection.mediaConstraints.audio = {
            deviceId: secondaryMic,
          };

          ref.connection.join(ref.connection.sessionid);
        }
      };
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
  },
};
</script>

<style scoped>
.drag-container {
  display: flex;
  min-height: 100vh;
}

.panel-one {
  width: 80%;
}

.panel-two {
  flex: 1;
  width: 20%;
}

.dragbar {
  padding: 2px;
  cursor: col-resize;
  background-color: black;
}
.img {
  width: inherit;
}
</style>
