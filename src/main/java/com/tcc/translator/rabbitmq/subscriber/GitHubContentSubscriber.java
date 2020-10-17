package com.tcc.translator.rabbitmq.subscriber;

import com.tcc.translator.dto.GitHubContentForTranslation;
import com.tcc.translator.dto.TranslatedFile;
import com.tcc.translator.rabbitmq.publisher.LoaderPublisher;
import com.tcc.translator.service.TranslationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GitHubContentSubscriber {

  private static final Logger logger = LoggerFactory.getLogger(GitHubContentSubscriber.class);

  @Autowired
  private TranslationService service;

  @Autowired
  private LoaderPublisher publisher;

  @RabbitListener(queues = "${translate.queue}")
  public void receiveGithubContent(GitHubContentForTranslation file) {
    logger.info("Received file {} for translation", file.getName());
    TranslatedFile translatedFile = service.translateFile(file);
    publisher.sendTranslatedFile(translatedFile);
  }
}
