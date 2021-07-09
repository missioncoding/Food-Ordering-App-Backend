package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.service.business.AddressService;
import com.upgrad.FoodOrderingApp.service.business.CustomerService;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * @author zeelani,ashish,vipin,madhuri
 * Controller class to handlle the address apis
 */

@CrossOrigin
@RestController
@RequestMapping("/")
public class AddressController {

    // injecting services classes for customer and address to
    // handle business logic operations
    @Autowired
    AddressService addressService;

    @Autowired
    CustomerService customerService;


    /**
     * Post request to save the address of the customer
     * @param authorization
     * @param saveAddressRequest
     * @return saveAddressResponse
     * @throws AuthorizationFailedException
     * @throws AddressNotFoundException
     * @throws SaveAddressException
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST,path = "/address",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestHeader("authorization") final String authorization, @RequestBody(required = false)SaveAddressRequest saveAddressRequest)
                throws AuthorizationFailedException, AddressNotFoundException, SaveAddressException {
        // getting the authorization token from parameter
        String accessToken = authorization.split("Bearer ")[1];
        // get the customer entity using the access token.
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        // generating the address entity object to persist in DB
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setFlatBuilNo(saveAddressRequest.getFlatBuildingName());
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setPincode(saveAddressRequest.getPincode());
        addressEntity.setUuid(UUID.randomUUID().toString());

        // calling the service logic operation to get the state entity
        StateEntity stateEntity = addressService.getStateByUUID(saveAddressRequest.getStateUuid());

        // now apply the service operation to save the address in db
        AddressEntity savedAddress = addressService.saveAddress(addressEntity,stateEntity);
        // now assigning the saved address to the customer
        CustomerAddressEntity customerAddressEntity = addressService.saveCustomerAddress(customerEntity,savedAddress);
        // generating the final api response
        SaveAddressResponse saveAddressResponse = new SaveAddressResponse()
                .id(savedAddress.getUuid())
                .status("ADDRESS SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SaveAddressResponse>(saveAddressResponse,HttpStatus.CREATED);
    }

    /**
     * Get request to retrieve all the cusomter address
     * @param authorization
     * @return AddressListResponse
     * @throws AuthorizationFailedException
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "/address/customer",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAllAddress(@RequestHeader("authorization")final String authorization)
                 throws AuthorizationFailedException {
        // getting the authorization token from parameter
        String accessToken = authorization.split("Bearer ")[1];

        // get the customer entity using the access token.
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        // calling the service logic to gather all the address entities related to the customer entity
        List<AddressEntity> addressEntities = addressService.getAllAddress(customerEntity);

        // need to show the most recent address on top
        Collections.reverse(addressEntities);

        List<AddressList> addressLists = new LinkedList<>();
        // looping through the address entities and create the address lists
        addressEntities.forEach(addressEntity -> {
            AddressListState addressListState = new AddressListState()
                    .id(UUID.fromString(addressEntity.getState().getStateUuid()))
                    .stateName(addressEntity.getState().getStateName());
            // creating the address list object
            AddressList addressList = new AddressList()
                    .id(UUID.fromString(addressEntity.getUuid()))
                    .city(addressEntity.getCity())
                    .flatBuildingName(addressEntity.getFlatBuilNo())
                    .locality(addressEntity.getLocality())
                    .pincode(addressEntity.getPincode())
                    .state(addressListState);
            addressLists.add(addressList);
        });

        // generating the final address list response
        AddressListResponse addressListResponse = new AddressListResponse().addresses(addressLists);
        return new ResponseEntity<AddressListResponse>(addressListResponse,HttpStatus.OK);
    }

    /**
     * Delete rest method to remove a saved customer address
     * @param authorization
     * @param addressUuid
     * @return DeleteAddressResponse
     * @throws AuthorizationFailedException
     * @throws AddressNotFoundException
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.DELETE,path = "/address/{address_id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteAddress(@RequestHeader ("authorization") final String authorization,@PathVariable(value = "address_id")final String addressUuid)
            throws AuthorizationFailedException,AddressNotFoundException {
        // getting the authorization token from parameter
        String accessToken = authorization.split("Bearer ")[1];

        // get the customer entity using the access token.
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        // fetching the adress entity using the UUID input and customer entity quried
        AddressEntity addressEntity = addressService.getAddressByUUID(addressUuid,customerEntity);

        // calling deleting service logic
        AddressEntity deletedAddressEntity = addressService.deleteAddress(addressEntity);
        DeleteAddressResponse deleteAddressResponse = new DeleteAddressResponse()
                .id(UUID.fromString(deletedAddressEntity.getUuid()))
                .status("ADDRESS DELETED SUCCESSFULLY");

        // creating the final response
        return new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse,HttpStatus.OK);
    }

    /**
     * Get request to retrieve all the states
     * @return StatesListResponse
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "/states",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> getAllStates() {
        // fetching all the states from service logic
        List<StateEntity> stateEntities = addressService.getAllStates();

        // check whether the state entities list is empty or not for looping
        if(!stateEntities.isEmpty()) {
            List<StatesList> statesLists = new LinkedList<>();
            stateEntities.forEach(stateEntity -> {
                StatesList statesList = new StatesList()
                        .id(UUID.fromString(stateEntity.getStateUuid()))
                        .stateName(stateEntity.getStateName());
                statesLists.add(statesList);
            });
            StatesListResponse statesListResponse = new StatesListResponse().states(statesLists);
            return new ResponseEntity<StatesListResponse>(statesListResponse, HttpStatus.OK);
        } else
            return new ResponseEntity<StatesListResponse>(new StatesListResponse(),HttpStatus.OK);
    }
}
