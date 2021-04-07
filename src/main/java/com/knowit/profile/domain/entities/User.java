package com.knowit.profile.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;

@Entity
@Table(name = "users")
public class User {

    private String id;

    private String email;

    private String username;

    private String firstName;

    private String lastName;

    @Id
    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    @Email
    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    @Column(unique = true, nullable = false)
    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    @Column(nullable = false)
    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    @Column(nullable = false)
    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }
}
