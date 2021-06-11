package com.epam.esm.web.security.auth;

import com.epam.esm.persistence.model.entity.User;

import java.util.Objects;

/**
 * {@link User} autorization request DTO class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class UserAuthorizationRequest {
    private String username;
    private String password;

    /**
     * Default constructor.
     */
    public UserAuthorizationRequest() {
        // Default constructor for Model mapper purposes.
    }

    /**
     * Constructor.
     *
     * @param username Login of the user.
     * @param password Password of the user.
     */
    public UserAuthorizationRequest(String username, String password) {
        this.username = username;
        this.password = password;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAuthorizationRequest that = (UserAuthorizationRequest) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
