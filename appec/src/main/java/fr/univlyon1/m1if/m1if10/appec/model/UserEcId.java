package fr.univlyon1.m1if.m1if10.appec.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserEcId implements Serializable {

    private int utilisateurId;
    private int alimentId;

    // Constructeur, getters, setters, etc.
    public UserEcId() {
    }

    public UserEcId(int utilisateurId,int alimentId) {
        this.utilisateurId = utilisateurId;
        this.alimentId = alimentId;
    }

    public int getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public int getAlimentId() {
        return alimentId;
    }

    public void setAlimentId(int alimentId) {
        this.alimentId = alimentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEcId)) return false;
        UserEcId that = (UserEcId) o;
        return utilisateurId == that.utilisateurId && alimentId == that.alimentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(utilisateurId, alimentId);
    }

}
