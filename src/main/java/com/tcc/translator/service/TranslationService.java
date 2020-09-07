package com.tcc.translator.service;

import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;
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

  public List<TranslatedFile> translateFile(List<GitHubContentForTranslation> files) {
    logger.info("Translating files with AWS Translate");

    return files.stream()
      .map(this::translateWithAws)
      .collect(Collectors.toList());
  }

  private TranslatedFile translateWithAws(GitHubContentForTranslation file) {
    TranslateTextRequest request = new TranslateTextRequest()
      .withText(file.getContent())
      .withSourceLanguageCode(file.getSourceLanguage())
      .withTargetLanguageCode(file.getTargetLanguage());

    TranslateTextResult translationResult = translateClient.translateText(request);

    TranslatedFile translatedFile = new TranslatedFile();
    translatedFile.setFileName(generateFileName(file.getName(), file.getTargetLanguage()));
    translatedFile.setContent(translationResult.getTranslatedText());

    return translatedFile;
  }

  private String generateFileName(String fileName, String targetLanguage) {
    StringBuilder sb = new StringBuilder();
    return sb.append(targetLanguage)
      .append("_")
      .append(fileName)
      .toString();
  }
}
