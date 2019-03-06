package com.ipi.qualitelogicielle.model;

import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDate;
import java.util.Collection;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class EmployeTest {
    @Parameterized.Parameter(value = 0)
    public LocalDate dateEmbauche;

    @Parameterized.Parameter(value = 1)
    public LocalDate dateActuelle;

    @Parameterized.Parameter(value = 2)
    public int expectedYear;

    @Parameterized.Parameters(name = "Embauche en {0} le {1} le nombre d'annee est : {2}")
    public static Collection<Object[]> data() {
        return asList(new Object[][]{
                {LocalDate.of(2000, 1, 1), LocalDate.of(2001, 1, 1), 1},
                {LocalDate.of(2000, 1, 1), LocalDate.of(2000, 1, 1), 0},
                {LocalDate.of(2000, 1, 1), LocalDate.of(1999, 1, 1), 0},
                {null, LocalDate.of(2001, 1, 1), 0},
        });
    }

    @Test
    public void getNombreAnneeAnciennete() {
        // Given employe embauché en 2000
        val newEmploye = Employe.builder()
                .dateEmbauche(this.dateEmbauche)
                .build();
        // When getting nombre annee ancienneté 1 year after
        val anneeAnciennete = newEmploye.getNombreAnneeAnciennete(this.dateActuelle);
        // Then it should return 1
        assertEquals(anneeAnciennete, this.expectedYear);
    }
}