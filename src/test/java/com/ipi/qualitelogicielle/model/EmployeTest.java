package com.ipi.qualitelogicielle.model;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class EmployeTest {
    @ParameterizedTest
    @CsvSource({
            "2000-01-01, 2000-01-01, 0",
            "2000-01-01, 2001-01-01, 1",
            ",2000-01-01, 0",
            "2001-01-01, 2000-01-01, 0"
    })
    void getNombreAnneeAnciennete(LocalDate dateEmbauche, LocalDate now, int expectedYear) {
        // Given employe embauché en 2000
        val newEmploye = Employe.builder()
                .dateEmbauche(dateEmbauche)
                .build();
        // When getting nombre annee ancienneté 1 year after
        val anneeAnciennete = newEmploye.getNombreAnneeAnciennete(now);
        // Then it should return 1
        assertEquals(anneeAnciennete, expectedYear);
    }

    @ParameterizedTest
    @CsvSource({
            "T45632, 1, 0, 1.0, 400"
    })
    void getPrimeAnnuelle(String matricule, int anneeAnciennete, int performance, Double tempsPartiel, Double expectedPrime) {
        val dateEmbauche = LocalDate.of(2000, 1, 1);
        val dateActuelle = dateEmbauche.plusYears(anneeAnciennete);
        // Given
        val employe = Employe.builder()
                .matricule(matricule)
                .dateEmbauche(dateEmbauche)
                .salaire(1D)
                .performance(performance)
                .tempsPartiel(tempsPartiel)
                .build();
        // When
        Double prime = employe.getPrimeAnnuelle(dateActuelle);
        // Then
        assertEquals(expectedPrime, prime);
    }

    @ParameterizedTest
    @CsvSource({
            "M12345, true",
            "T12345, false"
    })
    void isManagerTest(String matricule, boolean expected) {
        // Given
        val employe = Employe.builder().matricule(matricule).build();
        // When
        val isManager = employe.isManager();
        // Then
        assertEquals(expected, isManager);
    }
}