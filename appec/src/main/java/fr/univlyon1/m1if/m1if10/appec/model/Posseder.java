package fr.univlyon1.m1if.m1if10.appec.model;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "possede")
public class Posseder {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userecid")
    private int userEcId;

    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @JoinColumn(name = "utilisateurid")
    private User user;

    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @JoinColumn(name = "alimentid")
    private Aliment aliment;

    @Column(name = "quantite")
    private float quantity;

    @Column(name = "dateconsomation")
    private Date date;

    public Posseder() {}

    public Posseder(User user, Aliment aliment, float quantity, Date date) {
        this.user = user;
        this.aliment = aliment;
        this.quantity = quantity;
        this.date = date;
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


    public void setDate(Date date) {this.date = date;}

}

