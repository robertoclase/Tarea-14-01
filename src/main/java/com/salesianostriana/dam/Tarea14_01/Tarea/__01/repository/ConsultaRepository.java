package com.salesianostriana.dam.Tarea14_01.Tarea.__01.repository;

import com.salesianostriana.dam.Tarea14_01.Tarea.__01.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    Optional<Consulta> findByCitaId(Long citaId);




}