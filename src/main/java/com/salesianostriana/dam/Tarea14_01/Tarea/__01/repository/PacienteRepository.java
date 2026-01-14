package com.salesianostriana.dam.Tarea14_01.Tarea.__01.repository;

import com.salesianostriana.dam.Tarea14_01.Tarea.__01.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}