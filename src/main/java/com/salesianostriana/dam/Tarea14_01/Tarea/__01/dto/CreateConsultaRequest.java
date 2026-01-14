package com.salesianostriana.dam.Tarea14_01.Tarea.__01.dto;

import java.time.LocalDateTime;

public record CreateConsultaRequest(String observaciones, String diagnostico, LocalDateTime fecha) {
}
