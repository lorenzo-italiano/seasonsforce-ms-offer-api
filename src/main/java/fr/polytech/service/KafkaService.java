package fr.polytech.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.polytech.model.ExperienceDTOWithUserId;
import fr.polytech.model.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class KafkaService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);
    private static final String OFFER_TOPIC = "offer-topic";
    private static final String EXPERIENCE_TOPIC = "experience-topic";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Send a message to the Kafka topic
     *
     * @param notification Notification to send
     * @throws HttpClientErrorException If the notification cannot be serialized
     */
    public void sendMessage(NotificationDTO notification) throws HttpClientErrorException {
        try {
            String message = objectMapper.writeValueAsString(notification);
            logger.info("Producing message: {}", message);
            kafkaTemplate.send(OFFER_TOPIC, message);
        } catch (JsonProcessingException e) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while serializing notification");
        }
    }

    /**
     * Send a message to the Kafka topic
     *
     * @param experience ExperienceDTO to send
     * @throws HttpClientErrorException If the notification cannot be serialized
     */
    public void sendExperience(ExperienceDTOWithUserId experience) throws HttpClientErrorException {
        try {
            String message = objectMapper.writeValueAsString(experience);
            logger.info("Producing message: {}", message);
            kafkaTemplate.send(EXPERIENCE_TOPIC, message);
        } catch (JsonProcessingException e) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while serializing notification");
        }
    }
}
