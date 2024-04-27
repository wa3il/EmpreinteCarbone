package fr.univlyon1.m1if.m1if10.appec.model;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "posseder")
public class Posseder {

    @EmbeddedId
    private UserEcId userEcId;

    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @MapsId("utilisateurId") // Associe "utilisateurId" à l'attribut "utilisateur" dans la classe composite
    @JoinColumn(name = "utilisateurid")
    private User user;

    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @MapsId("alimentId") // Associe "alimentId" à l'attribut "aliment" dans la classe composite
    @JoinColumn(name = "alimentid")
    private Aliment aliment;

    @Column(name = "quantite")
    private float quantity;

    @Column(name = "dateconsomation")
    private Date date;

    public Posseder() {
        this.date = new Date(System.currentTimeMillis());
    }

    public Posseder(User user, Aliment aliment, float quantity) {
        this.user = user;
        this.aliment = aliment;
        this.quantity = quantity;
        this.date = new Date(System.currentTimeMillis());
        this.userEcId = new UserEcId(user.getUid(), aliment.getAlimentId());
    }

    public UserEcId getId() {
        return userEcId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Aliment getAliment() {
        return aliment;
    }

    public void setAliment(Aliment aliment) {
        this.aliment = aliment;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public Date getDate() {
        return date;
    }


}

