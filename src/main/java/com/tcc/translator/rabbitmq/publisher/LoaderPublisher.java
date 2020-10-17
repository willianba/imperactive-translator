package com.tcc.translator.rabbitmq.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcc.translator.dto.TranslatedFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LoaderPublisher {

  private static final Logger logger = LoggerFactory.getLogger(LoaderPublisher.class);

  @Autowired
  private AmqpTemplate amqpTemplate;

  @Autowired
  private ObjectMapper mapper;

  @Value("${direct.exchange}")
  private String directExchange;

  @Value("${translated.routing.key}")
  private String translatedRoutingKey;

  public void sendTranslatedFile(TranslatedFile file) {
    try {
      String jsonMessage = mapper.writeValueAsString(file);
      Message message = MessageBuilder.withBody(jsonMessage.getBytes())
        .setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
      amqpTemplate.send(directExchange, translatedRoutingKey, message);
      logger.info("Message sent to loader");
    } catch (JsonProcessingException e) {
      logger.error("Error converting message to JSON: {}", e.getMessage());
    }
  }
}
