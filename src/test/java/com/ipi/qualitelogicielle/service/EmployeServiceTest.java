package com.ipi.qualitelogicielle.service;

import com.ipi.qualitelogicielle.repository.EmployeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class EmployeServiceTest {
    @Mock
    EmployeRepository employeRepository;

    @InjectMocks
    EmployeService employeService;

    @Test
    void embaucheEmploye() {

    }
}