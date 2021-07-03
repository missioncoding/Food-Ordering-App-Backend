package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import org.springframework.stereotype.Service;

/**
 * Created by murarka on 06/30/21.
 */
@Service
public class CustomerBusinessService {

    public CustomerEntity signUp(CustomerEntity customerEntity){
    return new CustomerEntity();}

    public CustomerAuthEntity login(String customerName , String password){
        return new CustomerAuthEntity();}

    public CustomerAuthEntity logout(String customerName){
        return new CustomerAuthEntity();}


    public CustomerEntity updateCustomer(String autorization, CustomerEntity updatedCustomer){
        return new CustomerEntity();}


    public CustomerEntity updatePassword(String autorization,String oldpsw, String newpsw){
        return new CustomerEntity();}

}
