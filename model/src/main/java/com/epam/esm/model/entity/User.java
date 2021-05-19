package com.epam.esm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import java.util.Objects;

/**
 * User entity class.
 *
 * @author Yury Zmushko
 * @version 2.0
 */
@Entity
@Table(name = "user")
@SecondaryTable(name = "account", pkJoinColumns=@PrimaryKeyJoinColumn(name="id"))
public class User extends CustomEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(table = "account", nullable = false)
    private String login;

    @Column(table = "account", nullable = false)
    private String password;

    /**
     * Constructor.
     *
     * @param firstName First name of the user.
     * @param lastName Last name of the user.
     * @param login Login of the user.
     * @param password Password of the user.
     */
    public User(String firstName, String lastName, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
    }

    /**
     * Default constructor.
     */
    public User() {
        // Default constructor for JPA purposes.
        }

    /**
     * First name getter.
     *
     * @return First name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * First name setter.
     *
     * @param firstName First name of the user.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Last name getter.
     *
     * @return Last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Last name setter.
     *
     * @param lastName Last name of the user.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Login getter.
     *
     * @return Login of the user.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Login setter.
     *
     * @param login Login of the user.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Password getter.
     *
     * @return Password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Password setter.
     *
     * @param password Password of the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Equality check.
     *
     * @param o Object to check equality.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return firstName.equals(user.firstName)
                && lastName.equals(user.lastName)
                && login.equals(user.login)
                && password.equals(user.password);
    }

    /**
     * Hach code calculator.
     *
     * @return Hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, login, password);
    }

    /**
     * String value generator.
     *
     * @return {@link String} interpretation of object.
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
