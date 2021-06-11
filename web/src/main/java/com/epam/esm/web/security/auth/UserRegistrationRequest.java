package com.epam.esm.web.security.auth;

import com.epam.esm.persistence.model.entity.User;

import java.util.Objects;

/**
 * {@link User} registration request DTO class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class UserRegistrationRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    /**
     * Default constructor.
     */
    public UserRegistrationRequest() {
        // Default constructor for Model mapper purposes.
    }

    /**
     * Constructor.
     *
     * @param username Login of the user.
     * @param password Password of the user.
     * @param firstName Firstname of the user.
     * @param lastName Lastname of the user.
     */
    public UserRegistrationRequest(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRegistrationRequest that = (UserRegistrationRequest) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
