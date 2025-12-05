/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;

/**
 * Complete Address class with validation
 * Stores street, city, state, zip code
 */
public class Address implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Properties
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    
    /**
     * Constructor with all fields
     */
    public Address(String streetAddress, String city, String state, String zipCode, String country) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }
    
    /**
     * Default constructor
     */
    public Address() {
        this.streetAddress = "";
        this.city = "";
        this.state = "";
        this.zipCode = "";
        this.country = "USA";
    }
    
    // Getters
    public String getStreetAddress() {
        return streetAddress;
    }
    
    public String getCity() {
        return city;
    }
    
    public String getState() {
        return state;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public String getCountry() {
        return country;
    }
    
    // Setters
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    /**
     * Get complete formatted address
     */
    public String getFormattedAddress() {
        return streetAddress + ", " + city + ", " + state + " " + zipCode + ", " + country;
    }
    
    /**
     * Validate if address is complete
     */
    public boolean isValid() {
        return !streetAddress.isEmpty() && 
               !city.isEmpty() && 
               !state.isEmpty() && 
               !zipCode.isEmpty() &&
               zipCode.matches("\\d{5}"); // 5 digit zip code
    }
    
    /**
     * Validate zip code format
     */
    public static boolean isValidZipCode(String zipCode) {
        return zipCode != null && zipCode.matches("\\d{5}");
    }
    
    @Override
    public String toString() {
        return getFormattedAddress();
    }
}