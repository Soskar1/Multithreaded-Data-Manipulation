package com.mdm.mdm;

import java.time.LocalDate;

public class Person {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String gender;
    private final String country;
    private final String domainName;
    private final LocalDate birthDate;

    public Person(int id, String firstName, String lastName, String email, String gender, String country, String domainName, String birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.country = country;
        this.domainName = domainName;
        this.birthDate = LocalDate.parse(birthDate);
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

    public LocalDate getBirthDate() {
        return birthDate;
    }
}
