package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.Customer_AuthEntity;

/**
 * Created by murarka on 06/30/21.
 */
public class CustomerBusinessService {

    public CustomerEntity signUp(CustomerEntity customerEntity){
    return new CustomerEntity();}

    public Customer_AuthEntity login(String customerName ,String password){
        return new Customer_AuthEntity();}

    public Customer_AuthEntity logout(String customerName){
        return new Customer_AuthEntity();}


    public CustomerEntity updateCustomer(String autorization, CustomerEntity updatedCustomer){
        return new CustomerEntity();}

}
