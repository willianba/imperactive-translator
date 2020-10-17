package com.tcc.translator.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.tcc.translator.dto.GitHubContentForTranslation;
import com.tcc.translator.dto.TranslatedFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TranslationService {

  private static final Logger logger = LoggerFactory.getLogger(TranslationService.class);

  @Autowired
  private AmazonTranslate translateClient;

  public TranslatedFile translateFile(GitHubContentForTranslation file) {
    logger.info("Translating files with AWS Translate");
    return translateWithAws(file);
  }

  private TranslatedFile translateWithAws(GitHubContentForTranslation file) {
    List<String> chunks = splitContentIntoChunks(file.getContent());

    String translatedText = chunks.stream()
      .map(chunk -> {
        TranslateTextRequest request = new TranslateTextRequest()
          .withText(chunk)
          .withSourceLanguageCode(file.getSourceLanguage())
          .withTargetLanguageCode(file.getTargetLanguage());
        return request;
      })
      .map(request -> translateClient.translateText(request).getTranslatedText())
      .collect(Collectors.joining());

    TranslatedFile translatedFile = new TranslatedFile();
    translatedFile.setFileName(generateFileName(file.getName(), file.getTargetLanguage()));
    translatedFile.setContent(translatedText);

    return translatedFile;
  }

  private List<String> splitContentIntoChunks(String fileContent) {
    return Lists.newArrayList(Splitter.fixedLength(4859).split(fileContent));
  }

  private String generateFileName(String fileName, String targetLanguage) {
    StringBuilder sb = new StringBuilder();
    return sb.append(LocalDateTime.now().hashCode())
      .append("_")
      .append(targetLanguage)
      .append("_")
      .append(fileName)
      .toString();
  }
}
