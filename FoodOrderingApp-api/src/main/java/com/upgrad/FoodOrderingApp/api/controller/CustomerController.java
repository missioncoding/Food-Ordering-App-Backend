package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.LoginResponse;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.Customer_AuthEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.UUID;

/**
 * Created by murarka on 06/30/21.
 */
@CrossOrigin
@RestController
@RequestMapping("/")
public class CustomerController {

@Autowired
    private CustomerBusinessService customerBusinessService;

    @RequestMapping(method = RequestMethod.POST,
            path = "/customer/signup" ,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

    public ResponseEntity<SignupCustomerResponse> customerSignup(@RequestBody(required = false) final SignupCustomerRequest signupCustomerRequest) throws SignUpRestrictedException {

        final CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setContactNumber(signupCustomerRequest.getContactNumber());
        customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
        customerEntity.setFirstName(signupCustomerRequest.getFirstName());
        customerEntity.setLastName(signupCustomerRequest.getLastName());
        customerEntity.setPassword(signupCustomerRequest.getPassword());

        final CustomerEntity createdCustomerEntity = customerBusinessService.signUp(customerEntity);

        SignupCustomerResponse customerResponse = new SignupCustomerResponse().id(createdCustomerEntity.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SignupCustomerResponse>(customerResponse, HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.POST,
            path = "/customer/login" ,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

    public ResponseEntity<LoginResponse> customerLogin( @RequestHeader("authorization") final String authorization ) throws AuthenticationFailedException {

        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");

        Customer_AuthEntity customerAuthEntity = customerBusinessService.login(decodedArray[0], decodedArray[1]);
        CustomerEntity user_entity = customerAuthEntity.getCustomer();
        LoginResponse loginResponse = new LoginResponse().id(user_entity.getUuid())
                                                          .firstName(user_entity.getFirstName())
                                                          .lastName(user_entity.getLastName())
                                                          .emailAddress(user_entity.getEmail())
                                                          .contactNumber(user_entity.getContactNumber())
                                                          .message("LOGGED IN SUCCESSFULLY");

        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", customerAuthEntity.getAccessToken());

        return new ResponseEntity<LoginResponse>(loginResponse,headers,HttpStatus.OK);
    }

    }
