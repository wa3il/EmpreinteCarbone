
# README - Application "Esprit Carbone"

---

## Description :

"appec" est une application sur l'empreinte carbone, développée dans le cadre du projet "multi-mif12" par le groupe 12 de la M1IF10 à l'Université Lyon 1.

---

## Dépendances :

- **Spring Boot :** Framework Java pour créer des applications Spring.
- **Spring Boot Starter Thymeleaf :** Intégration de Thymeleaf avec Spring Boot pour les modèles HTML.
- **Spring Boot Starter Data JPA :** Intégration de JPA (Java Persistence API) avec Spring Boot pour l'accès aux données.
- **PostgreSQL :** Pilote JDBC pour PostgreSQL.
- **Spring Boot Starter Web :** Starter pour le développement d'applications web avec Spring Boot.
- **Spring Boot Starter Tomcat :** Starter pour le déploiement d'applications Spring Boot sur Tomcat.
- **Spring Boot Starter Test :** Starter pour les tests unitaires et d'intégration avec Spring Boot.
- **Jackson Dataformat XML :** Bibliothèque pour la manipulation de données XML avec Jackson.
- **Spring Boot Starter Security :** Intégration de la sécurité avec Spring Boot pour l'authentification et l'autorisation.
- **JJWT API, Impl, Jackson :** Bibliothèque pour la génération et la vérification de JWT (JSON Web Tokens).
- **Springdoc OpenAPI Starter WebMvc UI :** Intégration de l'interface utilisateur Springdoc OpenAPI pour la documentation API.
- **Maven Checkstyle Plugin :** Plugin Maven pour la vérification de la conformité du code avec les règles de style.
- **Jacoco Maven Plugin :** Plugin Maven pour la génération de rapports de couverture de code.

---

## Procédure de build :

Pour construire l'application, exécutez la commande suivante :

```mvn package```

Cela générera un fichier `.war` dans le dossier `target`. Vous pouvez ensuite déployer ce fichier `.war` dans le répertoire `webapps` de votre serveur Tomcat.

---

## Intégration continue :

L'intégration continue est gérée via GitLab CI.

---

## Reverse Proxy :

Nginx est utilisé comme reverse proxy pour rediriger les requêtes vers l'application.

---

## Lien vers l'application :

Vous pouvez accéder à l'application à l'adresse suivante : https://192.168.75.106


