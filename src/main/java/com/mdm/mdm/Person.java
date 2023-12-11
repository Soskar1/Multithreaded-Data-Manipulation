package com.mdm.mdm;

public class Person {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String gender;
    private final String country;
    private final String domainName;
    private final String birthDate;

    public Person(int id, String firstName, String lastName, String email, String gender, String country, String domainName, String birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.country = country;
        this.domainName = domainName;
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getCountry() {
        return country;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getBirthDate() {
        return birthDate;
    }
}
