package com.epam.esm.web.dto.user;

import com.epam.esm.persistence.model.entity.User;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;
import java.util.Set;

/**
 * {@link User} DTO class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class UserDTO extends RepresentationModel<UserDTO> {
    private int id;
    private String firstName;
    private String lastName;
    private String login;

    private Set<String> roles;

    /**
     * Constructor.
     *
     * @param firstName Firstname of the user.
     * @param lastName Lastname of the user.
     * @param login Login of the user.
     */
    public UserDTO(String firstName, String lastName, String login) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
    }

    /**
     * Default constructor.
     */
    public UserDTO() {
        // Default constructor for Model mapper purposes.
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(firstName, userDTO.firstName)
                && Objects.equals(lastName, userDTO.lastName)
                && Objects.equals(login, userDTO.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, login);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
