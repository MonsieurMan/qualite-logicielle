package com.ipi.qualitelogicielle.repository;

import com.ipi.qualitelogicielle.model.Employe;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.ipi.qualitelogicielle.utils.ListUtils.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
class EmployeRepositoryTest {
    @Autowired
    private EmployeRepository repository;

    @ParameterizedTest
    @MethodSource("matriculeTestCases")
    void findLastMatricule(List<String> matriculeToInsert, String expectedMatricule) {
        // Given
        matriculeToInsert.stream()
                .map(m -> Employe.builder().matricule(m).build())
                .forEach(repository::save);
        // When
        val lastMatricule = repository.findLastMatricule();
        // Then
        assertEquals(lastMatricule, expectedMatricule);
    }

    static Stream<Arguments> matriculeTestCases() {
        return Stream.of(
                arguments(Collections.EMPTY_LIST, null),
                arguments(of("M00001"), "00001"),
                arguments(of("M00001", "T00003", "T00002"), "00003")
        );
    }
}

