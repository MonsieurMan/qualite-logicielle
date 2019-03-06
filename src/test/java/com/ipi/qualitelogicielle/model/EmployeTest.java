package com.ipi.qualitelogicielle.model;

import lombok.val;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class EmployeTest {
    @Test
    public void getNombreAnneeAnciennete() {
        // Given employe embauché en 2000
        val newEmploye = Employe.builder()
                .dateEmbauche(LocalDate.of(2000, 1, 1))
                .build();
        // When getting nombre annee ancienneté 1 year after
        val anneeAnciennete = newEmploye.getNombreAnneeAnciennete(
                LocalDate.of(2001, 1, 1)
        );
        // Then it should return 1
        assertEquals(anneeAnciennete, 1);
    }
}