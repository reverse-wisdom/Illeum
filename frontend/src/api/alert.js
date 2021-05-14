import Push from 'push.js';

class AlertRabbitMQSocket {
  constructor(uid) {
    this.uid = uid;
    let ws = new WebSocket(`${window.location.protocol.replace('http', 'ws')}//${window.location.host}/api/rabbitmq/`);
    this.client = Stomp.over(ws);
    this.client.debug = () => {};
    this.login = 'illeum-guest';
    this.passcode = 'illeum-guest';
  }

  connect() {
    this.client.connect(
      this.login,
      this.passcode,
      () => {
        this.client.subscribe(`/amq/queue/member.${this.uid}`, (res) => {
          console.log('큐 메세지', res);
          if (Push.Permission.has()) {
            if (res.headers['content-type'] == 'application/json') {
              Push.create(JSON.parse(res.body)['message']);
            } else {
              Push.create(res.body);
            }
          }
        }); //큐명을 지정한경우 시 사용
      },
      (error) => {
        console.dir(error);
      },
      '/'
    );
  }
  disconnect() {
    this.client.disconnect();
  }
}

let instance = null;
let isConnected = false;
class Notification {
  constructor(uid) {
    if (instance) {
      return instance;
    }
    if (!uid) return null;
    this.socket = new AlertRabbitMQSocket(uid);
    this.connect();
    instance = this;
  }

  connect() {
    if (isConnected == false) {
      console.log('rabbitmq 연결');
      this.socket.connect();
      isConnected = true;
    }
  }

  disconnect() {
    if (isConnected == true) {
      console.log('rabbitmq 연결중단');
      this.socket.disconnect();
      instance = null;
      isConnected = false;
    }
  }
}

function notification(uid) {
  return new Notification(uid);
}

export { notification };
