package com.knowit.profile.domain.models;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

public class UpdateUserModel {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private LocalDate bornOn;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBornOn() {
        return bornOn;
    }

    public void setBornOn(LocalDate bornOn) {
        this.bornOn = bornOn;
    }
}
