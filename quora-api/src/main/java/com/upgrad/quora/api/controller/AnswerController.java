package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class AnswerController {

  @Autowired
  AnswerService answerService;

  /**
   * Api for creating an answer
   *
   * @param answerRequest
   * @param questionId
   * @param authorization
   * @return
   * @throws AuthorizationFailedException
   * @throws InvalidQuestionException
   */
  @PostMapping(path = "/question/{questionId}/answer/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<AnswerResponse> createAnswer(final AnswerRequest answerRequest,
      @PathVariable("questionId") final String questionId,
      @RequestHeader("authorization") final String authorization)
      throws AuthorizationFailedException, InvalidQuestionException {

    final AnswerEntity answerEntity = new AnswerEntity();
    answerEntity.setAnswer(answerRequest.getAnswer());

    // Return response with created answer entity
    final AnswerEntity createdAnswerEntity =
        answerService.createAnswer(answerEntity, questionId, authorization);
    AnswerResponse answerResponse = new AnswerResponse().id(createdAnswerEntity.getUuid())
        .status("ANSWER CREATED");
    return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
  }

  /**
   * Api for creating an answer
   *
   * @param answerId
   * @param authorization
   * @return
   * @throws AuthorizationFailedException
   * @throws AnswerNotFoundException
   */
  @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<com.upgrad.quora.api.model.AnswerDeleteResponse> deleteAnswer(
          @PathVariable("answerId") final String answerId,
          @RequestHeader("authorization") final String authorization)
          throws AuthorizationFailedException, AnswerNotFoundException {

    // Delete requested answer
    answerService.deleteAnswer(answerId, authorization);

    // Return response
    com.upgrad.quora.api.model.AnswerDeleteResponse answerDeleteResponse = new com.upgrad.quora.api.model.AnswerDeleteResponse().id(answerId)
            .status("ANSWER DELETED");
    return new ResponseEntity<com.upgrad.quora.api.model.AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
  }

}
