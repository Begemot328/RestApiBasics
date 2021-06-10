package com.epam.esm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * User entity class.
 *
 * @author Yury Zmushko
 * @version 2.0
 */
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SecondaryTable(name = "account", pkJoinColumns=@PrimaryKeyJoinColumn(name="id"))
public class User extends CustomEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(table = "account", nullable = false, unique = true)
    private String login;

    @Column(table = "account", nullable = false)
    private String password;

    @Column(table = "account", columnDefinition = "is_active")
    private boolean isActive = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

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

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

    /**
     * Activity getter.
     *
     * @return First name of the user.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Activity setter.
     *
     * @param active First name of the user.
     */
    public void setActive(boolean active) {
        this.isActive = active;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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
        return isActive == user.isActive && Objects.equals(firstName, user.firstName)
                && Objects.equals(lastName, user.lastName) && Objects.equals(login, user.login)
                && Objects.equals(password, user.password) && Objects.equals(roles, user.roles);
    }

    /**
     * Hash code calculator.
     *
     * @return Hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, login, password, isActive, roles);
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
                ", isActive=" + isActive +
                ", roles=" + roles +
                '}';
    }
}
