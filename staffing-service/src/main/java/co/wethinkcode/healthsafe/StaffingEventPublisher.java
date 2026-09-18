package co.wethinkcode.healthsafe;

import co.wethinkcode.healthsafe.mq.MqConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class StaffingEventPublisher {
    private final ConnectionFactory connectionFactory = 
        new ActiveMQConnectionFactory(MqConfig.BROKER_URL);
    private final ObjectMapper mapper = new ObjectMapper();

    public void publish(Schedule schedule){
        try(Connection connection = connectionFactory.createConnection()){
            connection.start();
            try (Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)){
                Topic topic = session.createTopic(MqConfig.TOPIC);
                try (MessageProducer producer = session.createProducer(topic)){
                    String json = mapper.writeValueAsString(schedule);
                    TextMessage message = session.createTextMessage(json);
                    producer.send(message);
                }
            }

        }catch(Exception e){
            System.err.println("Failed to publish staffing event: " + e.getMessage());
        }
    }
}
