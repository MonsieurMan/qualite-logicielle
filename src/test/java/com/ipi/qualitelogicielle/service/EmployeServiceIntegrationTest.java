package com.ipi.qualitelogicielle.service;

import com.ipi.qualitelogicielle.exception.EmployeException;
import com.ipi.qualitelogicielle.model.Employe;
import com.ipi.qualitelogicielle.model.NiveauEtude;
import com.ipi.qualitelogicielle.model.Poste;

import lombok.val;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EmployeServiceIntegrationTest {
    @Autowired
    private EmployeService employeService;

    @Test
    void embaucheEmploye() throws EmployeException {
        // Given
        val nom = "Doe";
        val prenom = "John";
        val poste = Poste.MANAGER;
        val niveau = NiveauEtude.DOCTORAT;
        val tempsPartiel = 1D;

        // When
        val employe = this.employeService.embaucheEmploye(nom, prenom, poste, niveau, tempsPartiel);

        // Then
        assertThat(employe).extracting(Employe::getMatricule)
                .asString().containsPattern(Pattern.compile("^M\\d{5}"));
    }
}

