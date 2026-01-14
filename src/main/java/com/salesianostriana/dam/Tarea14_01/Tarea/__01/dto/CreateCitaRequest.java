package com.salesianostriana.dam.Tarea14_01.Tarea.__01.dto;

import java.time.LocalDateTime;

public record CreateCitaRequest(Long pacienteId, Long profesionalId, LocalDateTime fechaHora) {
}
