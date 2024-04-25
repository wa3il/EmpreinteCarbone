package fr.univlyon1.m1if.m1if10.appec.model;

import jakarta.persistence.*;

@Entity
@Table(name = "jwt")
public class Jwt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jwtid")
    private int jwtId;

    @Column(name =  "token")
    private String token;

    @Column(name = "desactive")
    private boolean desactive;

    @Column(name = "expire")
    private boolean expire;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "utilisateurid")
    private User user;

    public Jwt() {
        desactive = false;
        expire = false;
    }

    public Jwt(boolean desactive, boolean expire, User user) {
        this.desactive = desactive;
        this.expire = expire;
        this.user = user;
    }

    public int getJwtId() {
        return jwtId;
    }

    public void setJwtId(int jwtId) {
        this.jwtId = jwtId;
    }

    public boolean isDesactive() {return desactive;}

    public void setDesactive(boolean desactive) {
        this.desactive = desactive;
    }

    public boolean isExpire() {
        return expire;
    }

    public void setExpire(boolean expire) {
        this.expire = expire;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
