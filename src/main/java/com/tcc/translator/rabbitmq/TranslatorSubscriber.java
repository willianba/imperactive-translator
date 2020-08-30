package com.tcc.translator.rabbitmq;

import java.util.List;

import com.tcc.translator.dto.GitHubContentForTranslation;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TranslatorSubscriber {

  @RabbitListener(queues = "${translate.queue}")
  public void receiveGithubContent(List<GitHubContentForTranslation> files) {
    files.forEach(file -> System.out.println(file.getContent()));
  }
}
