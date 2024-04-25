package fr.univlyon1.m1if.m1if10.appec.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlimentTest {

    private final Aliment aliment = new Aliment("f", 3.3F,"a", "b");

    @Test
    void getNomLegume() {
        assertEquals(aliment.getNomLegume(), "f");
    }

    @Test
    void getEc() {
        assertEquals(aliment.getEc(), 3.3F);
    }

    @Test
    void getGroupe() {
        assertEquals(aliment.getGroupe(), "a");
    }

    @Test
    void getSousGroupe() {
        assertEquals(aliment.getSousGroupe(), "b");
    }
}