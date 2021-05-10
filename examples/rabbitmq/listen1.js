#!/usr/bin/env node

import mqtt from 'mqtt';
// import push from 'push.js';
var wsbroker = 'http://k4d106.p.ssafy.io:5672'; // mqtt websocket enabled broker
var wsport = 5672; // port for above
var client = new Paho.MQTT.Client(wsbroker, wsport, '/ws', 'myclientid_' + parseInt(Math.random() * 100, 10));
// client.onConnectionLost = function (responseObject) {
//   debug('CONNECTION LOST - ' + responseObject.errorMessage);
// };
// client.onMessageArrived = function (message) {
//   debug('RECEIVE ON ' + message.destinationName + ' PAYLOAD ' + message.payloadString);
//   print_first(message.payloadString);
// //   push.create('Test' + msg.content.toString());
// };
var options = {
    timeout: 3,
    keepAliveInterval: 30,
    onSuccess: function () {
        debug("CONNECTION SUCCESS");
        // client.subscribe('/topic/test', {qos: 1});
    },
    onFailure: function (message) {
        debug("CONNECTION FAILURE - " + message.errorMessage);
    }
};
if (location.protocol == "https:") {
    options.useSSL = true;
}
debug("CONNECT TO " + wsbroker + ":" + wsport);
client.connect(options);