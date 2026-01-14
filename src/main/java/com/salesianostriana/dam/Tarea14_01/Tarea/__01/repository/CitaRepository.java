package com.salesianostriana.dam.Tarea14_01.Tarea.__01.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.salesianostriana.dam.Tarea14_01.Tarea.__01.model.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.salesianostriana.dam.Tarea14_01.Tarea.__01.model.Cita;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.model.Estado;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    Page<Cita> findAll(Pageable pageable);

    Page<Cita> findByPacienteId(Long pacienteId, Pageable pageable);

    Page<Cita> findByEstado(Estado estado, Pageable pageable);

    List<Cita> findByFechaHoraBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente WHERE c.profesional.id = :id AND DATE(c.fechaHora) = :dia")
    List<Cita> findAgendaDiariaProfesional(@Param("id") Long profesionalId, @Param("dia") LocalDate dia);


    boolean existsByProfesionalIdAndFechaHora(Long profesionalId, LocalDateTime fechaHora);

    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE c.paciente.id = :pacienteId AND DATE(c.fechaHora) = DATE(:fecha)")
    boolean tieneCitaElMismoDia(@Param("pacienteId") Long pacienteId, @Param("fecha") LocalDateTime fecha);

}