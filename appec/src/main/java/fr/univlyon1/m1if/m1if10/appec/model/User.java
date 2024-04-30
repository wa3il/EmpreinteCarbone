package fr.univlyon1.m1if.m1if10.appec.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * User entity.
 */
@JacksonXmlRootElement(localName = "user")
@Entity
@Table(name = "utilisateur")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "utilisateurid")
    private Integer uid;

    @Column(name = "nom", nullable = false)
    private String name;

    @Column(name = "mdp", nullable = false)
    private String password;

    @Column(name = "login", nullable = false)
    private String login;

    public User() {
    }

    public User(String name, String password, String login) {
        this.name = name;
        this.password = password;
        this.login = login;
    }

    @Override
    public String getUsername() {
        return login;
    }

    public void setUsername(String login) {
        this.login = login;
    }
    public void setuid(int id) {
        this.uid = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    public String getPassword() {
        return password;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUid() {
        return uid;
    }
}
