package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserProfileService;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/")
public class CommonController {

  @Autowired
  private UserDao userDao;

  @Autowired
  private UserProfileService userProfileService;

  /**
   * API to get userProfile details with UUID
   * @param uuid UUID of the User
   * @param accessToken Access Token
   * @return ResponseEntity
   * @throws AuthorizationFailedException ATHR-001 User has not signed in, ATHR-002 User is signed out.Sign in first to get user details
   * @throws UserNotFoundException USR-001 User with entered uuid does not exist
   */
  @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UserDetailsResponse> userProfile(@PathVariable("userId") final String uuid,
      @RequestHeader("authorization") final String accessToken)
      throws AuthorizationFailedException, UserNotFoundException {

    UserEntity userEntity = userProfileService.getUserByUuid(uuid, accessToken);
    UserDetailsResponse userDetailsResponse = new UserDetailsResponse()
        .firstName(userEntity.getFirstName())
        .lastName(userEntity.getLastName()).emailAddress(userEntity.getEmail())
        .contactNumber(userEntity.getContactNumber()).userName(userEntity.getUsername())
        .country(userEntity.getCountry()).aboutMe(userEntity.getAboutMe())
        .dob(userEntity.getDob());
    return new ResponseEntity<>(userDetailsResponse, HttpStatus.OK);

  }
}