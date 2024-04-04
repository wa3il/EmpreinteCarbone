package fr.univlyon1.m1if.m1if10.appec.model;

import jakarta.persistence.*;

@Entity
@Table(name = "aliments")
public class Aliment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alimentID")
    private Integer alimentId;

    @Column(name = "nomLegume", nullable = false)
    private String nomLegume;

    @Column(name = "EC", nullable = false)
    private Float ec;

    @Column(name = "groupe", nullable = false)
    private String groupe;

    @Column(name = "sous_groupe", nullable = false)
    private String sousGroupe;

    public Aliment() {
    }

    public Aliment(String nomLegume, Float ec, String groupe, String sousGroupe) {
        this.nomLegume = nomLegume;
        this.ec = ec;
        this.groupe = groupe;
        this.sousGroupe = sousGroupe;
    }

    public Integer getAlimentId() {
        return alimentId;
    }

    public String getNomLegume() {
        return nomLegume;
    }

    public void setNomLegume(String nomLegume) {
        this.nomLegume = nomLegume;
    }

    public Float getEc() {
        return ec;
    }

    public void setEc(Float ec) {
        this.ec = ec;
    }

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public String getSousGroupe() {
        return sousGroupe;
    }

    public void setSousGroupe(String sousGroupe) {
        this.sousGroupe = sousGroupe;
    }
}
