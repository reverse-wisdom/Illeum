import json
from pika import BlockingConnection, BasicProperties, ConnectionParameters, PlainCredentials

from models.QueueMessage import QueueMessage

credentials = PlainCredentials(username="illeum", password="illeum123")  # NOSONAR
props = BasicProperties(content_type='application/json', delivery_mode=2)


def send_message(exchange: str, message: QueueMessage):
    global credentials, props
    with BlockingConnection(
            ConnectionParameters(host="k4d106.p.ssafy.io", port=5672, credentials=credentials)) as connection:
        connection.channel().basic_publish(
            exchange=exchange,
            routing_key='',
            body=message.to_json().encode('utf-8'),
            properties=props
        )


if __name__ == "__main__":
    send_message("room.1", QueueMessage("test", "테스트 메세지입니다", None))
