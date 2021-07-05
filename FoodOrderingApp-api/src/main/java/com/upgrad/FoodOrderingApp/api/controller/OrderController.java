package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.business.OrderService;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ashishvats
 */

@RestController
@CrossOrigin
@RequestMapping("/")

public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * This method  takes customer's access token and a coupon name as inputs and the returns the coupon
     * details
     *
     * @param authorization Customer's access token as request parameter
     * @param couponName Coupon name
     * @return ResponseEntity with details of the matching coupon
     * @throws AuthorizationFailedException on incorrect/invalid access token
     * @throws CouponNotFoundException on incorrect/invalid/non-existent coupon name
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/order/coupon/{coupon_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCouponByName(@PathVariable(value = "coupon_name", required = false) String couponName, @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException, CouponNotFoundException {

        CouponEntity couponEntity = orderService.getCouponByName(couponName, authorization);
        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse();
        couponDetailsResponse.setCouponName(couponEntity.getCouponName());
        couponDetailsResponse.setId(UUID.fromString(couponEntity.getUuid()));
        couponDetailsResponse.setPercent(couponEntity.getPercent());

        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse,HttpStatus.OK);
    }

    /**
     * This method  takes customers access token and retrieves a list of all of the customer's orders
     *
     * @param authorization Customer's access token as request header parameter
     * @return ResponseEntity with list of all of the customer's orders
     * @throws AuthorizationFailedException on invalid/incorrect access token
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public  ResponseEntity<CustomerOrderResponse> getOrderItemsByCustomerId(@RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException {

        //@PathVariable("customer_id") String customerId

        List<OrdersEntity> orderEntityList = orderService.getOrdersByCustomers(authorization);

        // Create response
        CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse();

                OrderListCoupon  orderListCoupon = new OrderListCoupon()
                            .id(UUID.fromString(orderEntity.getCoupon().getUuid()))
                            .couponName(orderEntity.getCoupon().getCouponName())
                            .percent(orderEntity.getCoupon().getPercent());


                OrderListCustomer orderListCustomer = new OrderListCustomer()
                        .id(UUID.fromString(orderEntity.getCustomer().getUuid()))
                        .firstName(orderEntity.getCustomer().getFirstName())
                        .lastName(orderEntity.getCustomer().getLastName())
                        .emailAddress(orderEntity.getCustomer().getEmail())
                        .contactNumber(orderEntity.getCustomer().getContactNum());

                OrderListAddressState orderListAddressState = new OrderListAddressState()
                        .id(UUID.fromString(orderEntity.getAddress().getState().getUuid()))
                        .stateName(orderEntity.getAddress().getState().getStateName());

                OrderListPayment orderListPayment = new OrderListPayment()
                        .id(UUID.fromString(orderEntity.getPayment().getUuid()))
                        .paymentName(orderEntity.getPayment().getPaymentName());

                OrderListAddress orderListAddress = new OrderListAddress()
                        .id(UUID.fromString(orderEntity.getAddress().getUuid()))
                        .flatBuildingName(orderEntity.getAddress().getFlatBuilNumber())
                        .locality(orderEntity.getAddress().getLocality())
                        .city(orderEntity.getAddress().getCity())
                        .pincode(orderEntity.getAddress().getPinCode())
                        .state(orderListAddressState);

                OrderList orderList = new OrderList()
                        .id(UUID.fromString(orderEntity.getUuid()))
                        .bill(new BigDecimal(orderEntity.getBill()))
                        .coupon(orderListCoupon)
                        .discount(new BigDecimal(orderEntity.getDiscount()))
                        .date(orderEntity.getDate().toString())
                        .payment(orderListPayment)
                        .customer(orderListCustomer)
                        .address(orderListAddress);

                for (OrderItemEntity orderItemEntity : itemService.getItemsByOrder(orderEntity)) {

                    ItemQuantityResponseItem itemQuantityResponseItem = new ItemQuantityResponseItem()
                            .id(UUID.fromString(orderItemEntity.getItem().getUuid()))
                            .itemName(orderItemEntity.getItem().getItemName())
                            .itemPrice(orderItemEntity.getItem().getPrice())
                            .type(ItemQuantityResponseItem.TypeEnum.fromValue(orderItemEntity.getItem().getType().getValue()));

                    ItemQuantityResponse itemQuantityResponse = new ItemQuantityResponse()
                            .item(itemQuantityResponseItem)
                            .quantity(orderItemEntity.getQuantity())
                            .price(orderItemEntity.getPrice());

                    orderList.addItemQuantitiesItem(itemQuantityResponse);
                }

                customerOrderResponse.addOrdersItem(orderList);

        return new ResponseEntity<CustomerOrderResponse>(customerOrderResponse, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/order", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> saveOrder(final SaveOrderRequest saveOrderRequest, @RequestHeader("authorization") final String authorization)
            throws  AuthorizationFailedException, CouponNotFoundException, AddressNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException, ItemNotFoundException {

        OrdersEntity requestOrdersEntity = new OrdersEntity();
        requestOrdersEntity.setBill(saveOrderRequest.getBill().doubleValue());
        requestOrdersEntity.setDiscount(saveOrderRequest.getDiscount().doubleValue());

        List<OrderItemEntity> orderItemEntities = new ArrayList<>();

        for (ItemQuantity item: saveOrderRequest.getItemQuantities()) {
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            String uuid = item.getItemId().toString();
            Integer qty = item.getQuantity();
            Integer price = item.getPrice();
            ItemEntity itemEntity = new ItemEntity();
            itemEntity.setUuid(uuid);
            //itemEntity.setPrice(price);
            orderItemEntity.setItem(itemEntity);
            orderItemEntity.setQuantity(qty);
            orderItemEntity.setPrice(price);

            orderItemEntities.add(orderItemEntity);

        }

        final OrdersEntity ordersEntity = orderService.saveOrder
                (requestOrdersEntity,saveOrderRequest.getAddressId(),
                        saveOrderRequest.getRestaurantId().toString(),
                        saveOrderRequest.getPaymentId().toString(),
                        saveOrderRequest.getCouponId().toString(),
                        orderItemEntities,
                        authorization);
        final SaveOrderResponse saveOrderResponse = new SaveOrderResponse().id(ordersEntity.getUuid()).status("ORDER SUCCESSFULLY PLACED");


        return new ResponseEntity<SaveOrderResponse>(saveOrderResponse, HttpStatus.OK);
    }
}
