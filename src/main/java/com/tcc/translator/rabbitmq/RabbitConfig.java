package com.tcc.translator.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

  @Value("${direct.exchange}")
  private String directExchange;

  @Value("${translate.routing.key}")
  private String translateRoutingKey;

  @Value("${translate.queue}")
  private String translateQueue;

  @Bean
  public DirectExchange translationDirectExchange() {
    return new DirectExchange(directExchange);
  }

  @Bean
  public Queue translateQueue() {
    return QueueBuilder.durable(translateQueue).build();
  }

  @Bean
  public Binding translationBinding(DirectExchange translationDirectExchange, Queue translateQueue) {
    return BindingBuilder.bind(translateQueue).to(translationDirectExchange).with(translateRoutingKey);
  }

  @Bean
  public Jackson2JsonMessageConverter consumerJackson2MessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
