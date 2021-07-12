package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.business.CustomerService;
import com.upgrad.FoodOrderingApp.service.common.ApplicationUtil;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * @author muraka
 */
@CrossOrigin
@RestController
@RequestMapping("/")
public class CustomerController {

    // injecting customer service for business logic operations
    @Autowired
    private CustomerService customerService;

    @Autowired
    private ApplicationUtil applicationUtil;

    /**
     * Post method to sign up the customer
     * @param signupCustomerRequest
     * @return SignupCustomerResponse
     * @throws SignUpRestrictedException
     */
    @RequestMapping(method = RequestMethod.POST, path = "/customer/signup",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> customerSignup(@RequestBody(required = false) SignupCustomerRequest signupCustomerRequest)
            throws SignUpRestrictedException {
        // creating the customer entity object
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setContactNumber(signupCustomerRequest.getContactNumber());
        customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
        customerEntity.setFirstName(signupCustomerRequest.getFirstName());
        customerEntity.setLastName(signupCustomerRequest.getLastName());
        customerEntity.setPassword(signupCustomerRequest.getPassword());
        // validating the customer entity for mandatory fields
        applicationUtil.validateSignUpRequest(customerEntity);
        // creating the customer auth entity
        CustomerEntity createdCustomerEntity = customerService.saveCustomer(customerEntity);
        // creating the final customer response
        SignupCustomerResponse customerResponse = new SignupCustomerResponse().id(createdCustomerEntity.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupCustomerResponse>(customerResponse, HttpStatus.CREATED);
    }


    /**
     * Post method to login the customer
     * @param authorization
     * @return LoginResponse
     * @throws AuthenticationFailedException
     */
    @RequestMapping(method = RequestMethod.POST, path = "/customer/login",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> customerLogin( @RequestHeader("authorization") final String authorization )
            throws AuthenticationFailedException {
        // check whether the authorization is in correct format
        String[] authArray = applicationUtil.valdiateAuthorization(authorization);
        CustomerAuthEntity customerAuthEntity = customerService.authenticate(authArray[0],authArray[1]);

        // generating the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", customerAuthEntity.getAccessToken());
        List<String> listHeader = new ArrayList<>();
        listHeader.add("access-token");
        headers.setAccessControlExposeHeaders(listHeader);

        // generating the final login response
        LoginResponse loginResponse = new LoginResponse()
                .id(customerAuthEntity.getCustomer().getUuid())
                .contactNumber(customerAuthEntity.getCustomer().getContactNumber())
                .emailAddress(customerAuthEntity.getCustomer().getEmail())
                .firstName(customerAuthEntity.getCustomer().getFirstName())
                .lastName(customerAuthEntity.getCustomer().getLastName())
                .message("LOGGED IN SUCCESSFULLY");

        return new ResponseEntity<LoginResponse>(loginResponse,headers,HttpStatus.OK);
    }

    /**
     *
     * @param authorization
     * @return LogoutResponse
     * @throws AuthorizationFailedException
     */
    @RequestMapping(method = RequestMethod.POST,path = "/customer/logout" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> customerLogout(@RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException {
        // fetching the bearer token
        String[] bearerToken = authorization.split("Bearer ");
        CustomerAuthEntity customer_authEntity = customerService.logout(bearerToken[1]);

        // Generating the final logout response
        LogoutResponse logoutResponse = new LogoutResponse().id(customer_authEntity.getCustomer().getUuid()).message("LOGGED OUT SUCCESSFULLY");
        return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);

    }

    /**
     * Put method to update the customer details
     * @param authorization
     * @param updateCustomerRequest
     * @return UpdateCustomerResponse
     * @throws UpdateCustomerException
     * @throws AuthorizationFailedException
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/customer" ,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdateCustomerResponse> updateCustomer(@RequestHeader("authorization") final String authorization,@RequestBody(required = false) UpdateCustomerRequest updateCustomerRequest)
            throws UpdateCustomerException,AuthorizationFailedException {
        // first fetching the bearer token and validating it
        String[] bearerToken = authorization.split("Bearer ");
        CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);
        applicationUtil.validateUpdateCustomer(updateCustomerRequest.getFirstName());
        customerEntity.setFirstName(updateCustomerRequest.getFirstName());
        customerEntity.setLastName(updateCustomerRequest.getLastName());

        // updating the new customer details in the DB
        CustomerEntity newCustomerEntity = customerService.updateCustomer(customerEntity);
        // generating the final response
        UpdateCustomerResponse updateCustomerResponse = new UpdateCustomerResponse().id(newCustomerEntity.getUuid())
                .status("USER DETAILS SUCCESSFULLY UPDATED")
                .firstName(newCustomerEntity.getFirstName())
                .lastName(newCustomerEntity.getLastName());
        return new ResponseEntity<UpdateCustomerResponse>(updateCustomerResponse, HttpStatus.OK);
    }

    /**
     * Put method to update the customer password
     * @param authorization
     * @param updatePasswordRequest
     * @return UpdatePasswordResponse
     * @throws UpdateCustomerException
     * @throws AuthorizationFailedException
     */
    @RequestMapping(method = RequestMethod.PUT,path = "/customer/password" ,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdatePasswordResponse> updatePassword(@RequestHeader("authorization") final String authorization,@RequestBody(required = false) UpdatePasswordRequest updatePasswordRequest)
            throws UpdateCustomerException,AuthorizationFailedException {
        // fetching the bearer token
        String accessToken = authorization.split("Bearer ")[1];
        String oldPassword = updatePasswordRequest.getOldPassword();
        String newPassword = updatePasswordRequest.getNewPassword();

        // validating the password request
        applicationUtil.validateChangePassword(oldPassword,newPassword);

        // now getting the customer entity from db using the access token
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        //updating the password using the service method updatePassword
        CustomerEntity newcustomerEntity = customerService.updateCustomerPassword(oldPassword,newPassword,customerEntity);

        // setting the final response
        UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse().id(newcustomerEntity.getUuid())
                .status("USER PASSWORD SUCCESSFULLY UPDATED");
        return new ResponseEntity<UpdatePasswordResponse>(updatePasswordResponse, HttpStatus.OK);

    }

}