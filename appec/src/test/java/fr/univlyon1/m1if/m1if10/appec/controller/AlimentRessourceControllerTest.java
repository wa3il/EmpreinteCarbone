package fr.univlyon1.m1if.m1if10.appec.controller;
import fr.univlyon1.m1if.m1if10.appec.dao.Dao;
import fr.univlyon1.m1if.m1if10.appec.model.Aliment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AlimentRessourceControllerTest {

    @Mock
    private Dao<Aliment> daoMock;

    @InjectMocks
    private AlimentRessourceController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllAliments() {
        // Prepare mock data
        List<Aliment> aliments = new ArrayList<>();
        aliments.add(new Aliment("Apple", 0.5f, "Fruit", "Apple Family"));
        aliments.add(new Aliment("Banana", 0.7f, "Fruit", "Banana Family"));

        // Mock DAO behavior
        when(daoMock.getAll()).thenReturn(aliments);

        // Call the controller method
        ResponseEntity<?> responseEntity = controller.getAllAliment();

        // Verify that DAO method was called
        verify(daoMock, times(1)).getAll();

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(aliments, responseEntity.getBody());
    }

    @Test
    void testGetAlimentById() {
        Aliment aliment = new Aliment("Apple", 0.5f, "Fruit", "Apple Family");

        when(daoMock.get(1)).thenReturn(Optional.of(aliment));
        when(daoMock.get(2)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntityFound = controller.getAliment(1);
        ResponseEntity<?> responseEntityNotFound = controller.getAliment(2);

        verify(daoMock, times(1)).get(1);
        verify(daoMock, times(1)).get(2);

        assertEquals(HttpStatus.OK, responseEntityFound.getStatusCode());
        assertEquals(aliment, responseEntityFound.getBody());

        assertEquals(HttpStatus.NOT_FOUND, responseEntityNotFound.getStatusCode());
    }

    @Test
    void testGetAlimentByIdNull() {
        ResponseEntity<?> responseEntity = controller.getAliment(null);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
