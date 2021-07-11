package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.business.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


/**
 * @author ashishvats, zeelani
 * Controller method to handle the order api calls
 */

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    // injecting all service classes for handling business logic operations
    @Autowired
    ItemService itemService;

    @Autowired
    OrderService orderService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    AddressService addressService;

    @Autowired
    CustomerService customerService;

    @Autowired
    RestaurantService restaurantService;

    /**
     * POST request to save the order placed
     * @param authorization
     * @param saveOrderRequest
     * @return SaveOrderResponse
     * @throws AuthorizationFailedException
     * @throws PaymentMethodNotFoundException
     * @throws AddressNotFoundException
     * @throws RestaurantNotFoundException
     * @throws CouponNotFoundException
     * @throws ItemNotFoundException
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST,path = "",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> saveOrder(@RequestHeader(value = "authorization")final String authorization, @RequestBody(required = false) final SaveOrderRequest saveOrderRequest)
            throws AuthorizationFailedException, PaymentMethodNotFoundException, AddressNotFoundException, RestaurantNotFoundException, CouponNotFoundException ,ItemNotFoundException{

        // access the auth token from the authorization
        String accessToken = authorization.split("Bearer ")[1];

        // fetching the customer entity using the access token this will throw exception in case of any invalid creds
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        // fetch the coupon entity using the coupon id
        CouponEntity couponEntity = orderService.getCouponByCouponId(saveOrderRequest.getCouponId().toString());

        // fetching the payment entity using the payment id
        PaymentEntity paymentEntity = paymentService.getPaymentByUUID(saveOrderRequest.getPaymentId().toString());

        // get the address entity using the address id
        AddressEntity addressEntity = addressService.getAddressByUUID(saveOrderRequest.getAddressId(),customerEntity);

        // fetch the restaurant entity using the restaurant id
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(saveOrderRequest.getRestaurantId().toString());

        // creating the order entity using the data fetched from the db
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        OrdersEntity ordersEntity = new OrdersEntity();
        ordersEntity.setUuid(UUID.randomUUID().toString());
        ordersEntity.setBill(saveOrderRequest.getBill().floatValue());
        ordersEntity.setDate(timestamp);
        ordersEntity.setCustomer(customerEntity);
        ordersEntity.setDiscount(saveOrderRequest.getDiscount().doubleValue());
        ordersEntity.setPayment(paymentEntity);
        ordersEntity.setAddress(addressEntity);
        ordersEntity.setRestaurant(restaurantEntity);
        ordersEntity.setCoupon(couponEntity);

        // saving the order entity in the db
        OrdersEntity finalOrderEntity = orderService.saveOrder(ordersEntity);

        // now looking at the order items and quantities
        List<ItemQuantity> itemQuantities = saveOrderRequest.getItemQuantities();
        // looping through each order item
        for(ItemQuantity itemQuantity : itemQuantities) {
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            ItemEntity itemEntity = itemService.getItemByUUID(itemQuantity.getItemId().toString());
            orderItemEntity.setItem(itemEntity);
            orderItemEntity.setOrder(ordersEntity);
            orderItemEntity.setPrice(itemQuantity.getPrice());
            orderItemEntity.setQuantity(itemQuantity.getQuantity());

            // saving the order item in the db
            OrderItemEntity savedOrderItem = orderService.saveOrderItem(orderItemEntity);
        }

        // creating the final response
        SaveOrderResponse saveOrderResponse = new SaveOrderResponse()
                .id(finalOrderEntity.getUuid())
                .status("ORDER SUCCESSFULLY PLACED");
        return new ResponseEntity<SaveOrderResponse>(saveOrderResponse,HttpStatus.CREATED);
    }

    /**
     * Get method to retrive the coupon using the coupon name
     * @param authorization
     * @param couponName
     * @return CouponDetailsResponse
     * @throws AuthorizationFailedException
     * @throws CouponNotFoundException
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "/coupon/{coupon_name}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCouponByCouponName(@RequestHeader(value = "authorization") final String authorization, @PathVariable(value = "coupon_name")final String couponName)
            throws AuthorizationFailedException, CouponNotFoundException {

        // access the auth token from the authorization
        String accessToken = authorization.split("Bearer ")[1];

        // fetching the customer entity using the access token this will throw exception in case of any invalid creds
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        // Calling getCouponByCouponName orderService method
        CouponEntity couponEntity = orderService.getCouponByCouponName(couponName);

        // Creating the couponDetailsResponse
        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse()
                .couponName(couponEntity.getCouponName())
                .id(UUID.fromString(couponEntity.getUuid()))
                .percent(couponEntity.getPercent());
        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse,HttpStatus.OK);
    }

    /**
     * Get method to retrieve the past orders of the user
     * @param authorization
     * @return CustomerOrderResponse
     * @throws AuthorizationFailedException
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CustomerOrderResponse> getPastOrderOfUser(@RequestHeader(value = "authorization")final String authorization) throws AuthorizationFailedException {

        // access the auth token from the authorization
        String accessToken = authorization.split("Bearer ")[1];

        // fetching the customer entity using the access token this will throw exception in case of any invalid creds
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        // Calling getOrdersByCustomers method from orderService to get all the past orders of the customer.
        List<OrdersEntity> ordersEntities =  orderService.getOrdersByCustomers(customerEntity.getUuid());

        // Creating List of OrderList
        List<OrderList> orderLists = new LinkedList<>();

        // if orderentities is null, then return empty list
        if(ordersEntities != null){
            //looping in for each orderentity received
            for(OrdersEntity ordersEntity:ordersEntities){
                // calling service method to recive order item entity for each order entity.
                List<OrderItemEntity> orderItemEntities = orderService.getOrderItemsByOrder(ordersEntity);

                //Creating ItemQuantitiesResponse List
                List<ItemQuantityResponse> itemQuantityResponseList = new LinkedList<>();
                orderItemEntities.forEach(orderItemEntity -> {
                    ItemQuantityResponseItem itemQuantityResponseItem = new ItemQuantityResponseItem()
                            .itemName(orderItemEntity.getItem().getitemName())
                            .itemPrice(orderItemEntity.getItem().getPrice())
                            .id(UUID.fromString(orderItemEntity.getItem().getUuid()))
                            .type(ItemQuantityResponseItem.TypeEnum.valueOf(orderItemEntity.getItem().getType().getValue()));
                    //Creating ItemQuantityResponse which will be added to the list
                    ItemQuantityResponse itemQuantityResponse = new ItemQuantityResponse()
                            .item(itemQuantityResponseItem)
                            .quantity(orderItemEntity.getQuantity())
                            .price(orderItemEntity.getPrice());
                    itemQuantityResponseList.add(itemQuantityResponse);
                });
                //Creating OrderListAddressState to add in the address
                OrderListAddressState orderListAddressState = new OrderListAddressState()
                        .id(UUID.fromString(ordersEntity.getAddress().getState().getUuid()))
                        .stateName(ordersEntity.getAddress().getState().getStateName());

                //Creating OrderListAddress to add address to the orderList
                OrderListAddress orderListAddress = new OrderListAddress()
                        .id(UUID.fromString(ordersEntity.getAddress().getUuid()))
                        .flatBuildingName(ordersEntity.getAddress().getFlatBuilNo())
                        .locality(ordersEntity.getAddress().getLocality())
                        .city(ordersEntity.getAddress().getCity())
                        .pincode(ordersEntity.getAddress().getPincode())
                        .state(orderListAddressState);

                //Creating OrderListCoupon to add Coupon to the orderList
                OrderListCoupon orderListCoupon = new OrderListCoupon()
                        .couponName(ordersEntity.getCoupon().getCouponName())
                        .id(UUID.fromString(ordersEntity.getCoupon().getUuid()))
                        .percent(ordersEntity.getCoupon().getPercent());

                //Creating OrderListCustomer to add Customer to the orderList
                OrderListCustomer orderListCustomer = new OrderListCustomer()
                        .id(UUID.fromString(ordersEntity.getCustomer().getUuid()))
                        .firstName(ordersEntity.getCustomer().getFirstName())
                        .lastName(ordersEntity.getCustomer().getLastName())
                        .emailAddress(ordersEntity.getCustomer().getEmail())
                        .contactNumber(ordersEntity.getCustomer().getContactNumber());

                //Creating OrderListPayment to add Payment to the orderList
                OrderListPayment orderListPayment = new OrderListPayment()
                        .id(UUID.fromString(ordersEntity.getPayment().getUuid()))
                        .paymentName(ordersEntity.getPayment().getPaymentName());

                //Craeting orderList to add all the above info and then add it orderLists to finally add it to CustomerOrderResponse
                OrderList orderList = new OrderList()
                        .id(UUID.fromString(ordersEntity.getUuid()))
                        .itemQuantities(itemQuantityResponseList)
                        .address(orderListAddress)
                        .bill(BigDecimal.valueOf(ordersEntity.getBill()))
                        .date(String.valueOf(ordersEntity.getDate()))
                        .discount(BigDecimal.valueOf(ordersEntity.getDiscount()))
                        .coupon(orderListCoupon)
                        .customer(orderListCustomer)
                        .payment(orderListPayment);
                orderLists.add(orderList);
            }

            //Creating CustomerOrderResponse by adding OrderLists to it
            CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse()
                    .orders(orderLists);
            return new ResponseEntity<CustomerOrderResponse>(customerOrderResponse,HttpStatus.OK);
        }else {
            //If no order created by customer empty array is returned.
            return new ResponseEntity<CustomerOrderResponse>(new CustomerOrderResponse(),HttpStatus.OK);
        }


    }


}
