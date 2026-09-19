package co.wethinkcode.healthsafe;

import co.wethinkcode.healthsafe.mq.MqConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;

public class EquipmentAlertConsumer {
        private final ObjectMapper mapper = new ObjectMapper();

    public void start() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(MqConfig.BROKER_URL);
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            Queue queue = session.createQueue(MqConfig.QUEUE);
            MessageConsumer consumer = session.createConsumer(queue);

            consumer.setMessageListener(message -> {
                try {
                    if (message instanceof TextMessage textMessage) {
                        String json = textMessage.getText();
                        EquipmentFailure failure = mapper.readValue(json, EquipmentFailure.class);

                        System.out.println("[equipment-alert-service] ALERT: equipment failure on ward "
                                + failure.wardId + " — " + failure.description);
                        message.acknowledge();
                    }
                } catch (Exception e) {
                    System.err.println("Failed to process equipment failure alert: " + e.getMessage());
                }
            });

            System.out.println("[equipment-alert-service] Consuming from " + MqConfig.QUEUE);
        } catch (Exception e) {
            System.err.println("Failed to start equipment alert consumer: " + e.getMessage());
        }
    }
}
