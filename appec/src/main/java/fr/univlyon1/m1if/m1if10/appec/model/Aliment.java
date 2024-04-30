package fr.univlyon1.m1if.m1if10.appec.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Aliment entity.
 */
@JacksonXmlRootElement(localName = "aliment")
@Entity
@Table(name = "aliments")
public class Aliment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alimentid")
    private Integer alimentId;

    @Column(name = "nomlegume", nullable = false)
    private String nomLegume;

    @Column(name = "ec", nullable = false)
    private Float ec;

    @Column(name = "groupe", nullable = false)
    private String groupe;

    @Column(name = "sous_groupe", nullable = false)
    private String sousGroupe;

    public Aliment() {}

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

    public Float getEc() {
        return ec;
    }

    public String getGroupe() {
        return groupe;
    }

    public String getSousGroupe() {
        return sousGroupe;
    }

    public void setNomLegume(String nomLegume) {
        this.nomLegume = nomLegume;
    }
    public void setAlimentid(Integer alimentId) {
        this.alimentId = alimentId;
    }

}
