package co.wethinkcode.healthsafe;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.wethinkcode.healthsafe.mq.MqConfig;

public class EquipmentFailurePublisher {
        private final ConnectionFactory connectionFactory =
            new ActiveMQConnectionFactory(MqConfig.BROKER_URL);
    private final ObjectMapper mapper = new ObjectMapper();

    public void publish(EquipmentFailure failure) throws JMSException {
        try (Connection connection = connectionFactory.createConnection()) {
            connection.start();
            try (Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
                Queue queue = session.createQueue(MqConfig.QUEUE);
                try (MessageProducer producer = session.createProducer(queue)) {
                    producer.setDeliveryMode(DeliveryMode.PERSISTENT);
                    String json;
                    try {
                        json = mapper.writeValueAsString(failure);
                    } catch (Exception e) {
                        throw new JMSException("Failed to serialize equipment failure: " + e.getMessage());
                    }
                    TextMessage message = session.createTextMessage(json);
                    producer.send(message);
                }
            }
        }
    }
}
