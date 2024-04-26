package fr.univlyon1.m1if.m1if10.appec.model;

import jakarta.persistence.*;

import java.io.Serializable;

/*@Entity
@Table(name = "posseder")
public class UserEc {

    @EmbeddedId
    private UserEcId id;

    @ManyToOne
    @MapsId("utilisateurId") // Associe "utilisateurId" à l'attribut "utilisateur" dans la classe composite
    @JoinColumn(name = "utilisateurid")
    private User user;

    @ManyToOne
    @MapsId("alimentId") // Associe "alimentId" à l'attribut "aliment" dans la classe composite
    @JoinColumn(name = "alimentid")
    private Aliment aliment;

    @Embeddable
    public static class UserEcId implements Serializable {

        private Long utilisateurId;
        private Long alimentId;

        // Constructeur, getters, setters, etc.
        public UserEcId() {
        }

        public UserEcId(Long utilisateurId, Long alimentId) {
            this.utilisateurId = utilisateurId;
            this.alimentId = alimentId;
        }

        public Long getUtilisateurId() {
            return utilisateurId;
        }

        public void setUtilisateurId(Long utilisateurId) {
            this.utilisateurId = utilisateurId;
        }

        public Long getAlimentId() {
            return alimentId;
        }

        public void setAlimentId(Long alimentId) {
            this.alimentId = alimentId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserEcId userEcId = (UserEcId) o;
            return utilisateurId.equals(userEcId.utilisateurId) && alimentId.equals(userEcId.alimentId);
        }

        @Override
        public int hashCode() {
            return utilisateurId.hashCode() + alimentId.hashCode();
        }

    }

}
*/

