package fr.univlyon1.m1if.m1if10.appec.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlimentTest {

    private final Aliment aliment = new Aliment("f", 3.3F,"a", "b");

    @Test
    void getNomLegume() {
        assertEquals( "f", aliment.getNomLegume());
    }

    @Test
    void getEc() {
        assertEquals( 3.3F,aliment.getEc());
    }

    @Test
    void getGroupe() {
        assertEquals("a", aliment.getGroupe());
    }

    @Test
    void getSousGroupe() {
        assertEquals( "b", aliment.getSousGroupe());
    }
}