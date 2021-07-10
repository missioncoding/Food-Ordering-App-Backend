package com.upgrad.FoodOrderingApp.service.common;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.stereotype.Component;

/**
 * @author zeelani
 * Utilty class to handle small util operations like validations
 * and verifications
 */

@Component
public class ApplicationUtil {

    /**
     * method to validate the contact number
     * @param contactNumber
     * @return
     */
    public boolean validateContactNumber(String contactNumber){
        Pattern p = Pattern.compile("(0/91)?[7-9][0-9]{9}");
        Matcher m = p.matcher(contactNumber);
        return (m.find() && m.group().equals(contactNumber));
    }

    /**
     * method to validate Email address
     * @param email
     * @return
     */
    public boolean validateEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    /**
     * Method to validate the pin code
     * @param pincode
     * @return
     */
    public boolean valdiatePinCode(String pincode){
        Pattern p = Pattern.compile("\\d{6}\\b");
        Matcher m = p.matcher(pincode);
        return (m.find() && m.group().equals(pincode));
    }

    /**
     * Method validate the sign up request
     * @param customerEntity
     * @return
     * @throws SignUpRestrictedException
     */
    public boolean validateSignUpRequest (CustomerEntity customerEntity) throws SignUpRestrictedException {
        if (customerEntity.getFirstName() == null || customerEntity.getFirstName() == "") {
            throw new SignUpRestrictedException("SGR-005","Except last name all fields should be filled");
        }
        if(customerEntity.getPassword() == null||customerEntity.getPassword() == "") {
            throw new SignUpRestrictedException("SGR-005","Except last name all fields should be filled");
        }
        if (customerEntity.getEmail() == null||customerEntity.getEmail() == "") {
            throw new SignUpRestrictedException("SGR-005","Except last name all fields should be filled");
        }
        if (customerEntity.getContactNumber() == null||customerEntity.getContactNumber() == "") {
            throw new SignUpRestrictedException("SGR-005","Except last name all fields should be filled");
        }
        return true;
    }

    /**
     * Method to validate the authorization
     * @param authorization
     * @return
     * @throws AuthenticationFailedException
     */
    public String[] valdiateAuthorization(String authorization)throws AuthenticationFailedException{
        try {
            byte[] authByteArray = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
            String authString = new String(authByteArray);
            String[] authArray = authString.split(":");
            if (authArray.length != 2) {
                // some issue with the auhorization field should be only of length 2
                throw new AuthenticationFailedException("ATH-003","Incorrect format of decoded customer name and password");
            } else {
                return authArray;
            }
        }catch (ArrayIndexOutOfBoundsException exc){
            throw new AuthenticationFailedException("ATH-003","Incorrect format of decoded customer name and password");
        }
    }

    /**
     * Method to do basic update customer update request
     * @param firstName
     * @return
     * @throws UpdateCustomerException
     */
    public boolean validateUpdateCustomer (String firstName)throws UpdateCustomerException {
        if (firstName == null || firstName == "") {
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        }
        return true;
    }

    /**
     * Method to do basic validation of the password
     * @param oldPassword
     * @param newPassword
     * @return
     * @throws UpdateCustomerException
     */
    public boolean validateChangePassword(String oldPassword,String newPassword) throws UpdateCustomerException{
        if (oldPassword == null || oldPassword == "") {
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }
        if (newPassword == null || newPassword == "") {
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }
        return true;
    }

    /**
     * Method to validate the customer 5 being the best
     * @param cutomerRating
     * @return
     */
    public boolean validateCustomerRating(String cutomerRating){
        if(cutomerRating.equals("5.0")){
            return true;
        }
        Pattern p = Pattern.compile("[1-4].[0-9]");
        Matcher m = p.matcher(cutomerRating);
        return (m.find() && m.group().equals(cutomerRating));
    }

    /**
     * Utility method to sort the entries in a map
     * @param entries
     * @return
     */
    public Map<String,Integer> sortMapByValues(Map<String,Integer> entries){
        List<Map.Entry<String,Integer>> list = new LinkedList<Map.Entry<String, Integer>>(entries.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue().compareTo(o1.getValue()));
            }
        });
        Map<String, Integer> sortedEntries = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> item : list) {
            sortedEntries.put(item.getKey(), item.getValue());
        }
        return sortedEntries;
    }

    /**
     * Method to validate the password for given conditions
     * 1 upper case latter, 1 Number, 1 Special Character, and
     * atleast 8 characters
     * @param password
     * @return
     */
    public boolean validatePassword(String password){
        Boolean lowerCase = false;
        Boolean upperCase = false;
        Boolean number = false;
        Boolean specialCharacter = false;

        if(password.length() < 8){
            return false;
        }
        if(password.matches("(?=.*[0-9]).*")){
            number = true;
        }
        if(password.matches("(?=.*[a-z]).*")){
            lowerCase = true;
        }
        if(password.matches("(?=.*[A-Z]).*")){
            upperCase = true;
        }
        if(password.matches("(?=.*[#@$%&*!^]).*")){
            specialCharacter = true;
        }
        if(lowerCase && upperCase && specialCharacter && number){
            return true;
        } else {
            return false;
        }
    }
}

