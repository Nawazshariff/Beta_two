package com.example.nawazshariff.beta_two.Model;

/**
 * Created by nawazshariff on 07-10-2017.
 */

public class User {
    public String address;
    public String contactNumber;
    public String name;
    public String spinnerValue;

    public User()
    {
        }

    public User(String address, String contactNumber, String name, String spinnerValue) {
        this.address = address;
        this.contactNumber = contactNumber;
        this.name = name;
        this.spinnerValue = spinnerValue;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpinnerValue() {
        return spinnerValue;
    }

    public void setSpinnerValue(String spinnerValue) {
        this.spinnerValue = spinnerValue;
    }
}
