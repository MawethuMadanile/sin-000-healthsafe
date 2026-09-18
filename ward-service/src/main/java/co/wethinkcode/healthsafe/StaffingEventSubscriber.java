package co.wethinkcode.healthsafe;

import co.wethinkcode.healthsafe.mq.MqConfig;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;


public class StaffingEventSubscriber {
    public void start() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(MqConfig.BROKER_URL);
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(MqConfig.TOPIC);
            MessageConsumer consumer = session.createConsumer(topic);

            consumer.setMessageListener(message -> {
                try {
                    if (message instanceof TextMessage textMessage) {
                        String json = textMessage.getText();
                        System.out.println("[ward-service] Received staffing event: " + json);
                    }
                } catch (JMSException e) {
                    System.err.println("Failed to read staffing event: " + e.getMessage());
                }
            });

            System.out.println("[ward-service] Subscribed to " + MqConfig.TOPIC);
        } catch (Exception e) {
            System.err.println("Failed to subscribe to staffing events: " + e.getMessage());
        }
    }  
}
