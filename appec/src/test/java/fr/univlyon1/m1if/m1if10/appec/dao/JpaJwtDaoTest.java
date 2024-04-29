/*package fr.univlyon1.m1if.m1if10.appec.dao;

import fr.univlyon1.m1if.m1if10.appec.model.Jwt;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import fr.univlyon1.m1if.m1if10.appec.securite.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JpaJwtDaoTest {

    @Autowired
    private JpaJwtDao jpaJwtDao;

    @Autowired
    private JpaUserDao jpaUserDao;

    @Autowired
    private JwtService jwtService;

    private Jwt jwt;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(); // Initialize user with your own values
        user.setUsername("testUnitaire");
        user.setName("test");
        user.setPassword("test");
        jpaUserDao.save(user);
        jwtService.generateToken(user);
    }

    @AfterEach
    void tearDown() {
        jpaJwtDao.delete(jwt);
        jpaUserDao.delete(user);
    }

    @Test
    void whenGetById_thenReturnJwt() {
        Optional<Jwt> found = jpaJwtDao.findTokenValidByUser(user);

        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getJwtId()).isEqualTo(jwt.getJwtId());
    }

    @Test
    void whenGetAll_thenReturnAllJwts() {
        List<Jwt> jwts = jpaJwtDao.getAll();

        assertThat(jwts).isNotEmpty();
        assertThat(jwts).contains(jwt);
    }

    @Test
    void whenSave_thenJwtIsPersisted() {
        Jwt newJwt = new Jwt(); // Initialize newJwt with your own values
        newJwt.setUser(user);
        jpaJwtDao.save(newJwt);

        Optional<Jwt> found = jpaJwtDao.get(newJwt.getJwtId());
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get()).isEqualTo(newJwt);
    }

    @Test
    void whenUpdate_thenJwtIsUpdated() {
        String[] params = {"true", "false"}; // Initialize params with your own values
        jpaJwtDao.update(jwt, params);

        Optional<Jwt> found = jpaJwtDao.get(jwt.getJwtId());
        assertThat(found.isPresent()).isTrue();
        // Add assertions to check if the jwt has been updated with the new params
    }

    @Test
    void whenDelete_thenJwtIsDeleted() {
        jpaJwtDao.delete(jwt);

        Optional<Jwt> found = jpaJwtDao.get(jwt.getJwtId());
        assertThat(found.isPresent()).isFalse();
    }

    @Test
    void whenFindByValue_thenReturnJwt() {
        Optional<Jwt> found = jpaJwtDao.findByValue(jwt.getToken());

        assertThat(found.isPresent()).isTrue();
        assertThat(found.get()).isEqualTo(jwt);
    }

    @Test
    void whenFindTokenValidByUser_thenReturnJwt() {
        Optional<Jwt> found = jpaJwtDao.findTokenValidByUser(user);

        assertThat(found.isPresent()).isTrue();
        assertThat(found.get()).isEqualTo(jwt);
    }
}
*/