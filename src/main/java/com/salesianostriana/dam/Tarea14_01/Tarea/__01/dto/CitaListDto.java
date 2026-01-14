package com.salesianostriana.dam.Tarea14_01.Tarea.__01.dto;

import com.salesianostriana.dam.Tarea14_01.Tarea.__01.model.Cita;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.model.Estado;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CitaListDto(
        Long id,
        LocalDateTime fechaHora,
        Estado estado,
        String paciente,
        String profesional,
        String especialidad
) {
    public static CitaListDto from(Cita cita) {
        return CitaListDto.builder()
                .id(cita.getId())
                .fechaHora(cita.getFechaHora())
                .estado(cita.getEstado())
                .paciente(cita.getPaciente() != null ? cita.getPaciente().getNombre() : null)
                .profesional(cita.getProfesional() != null ? cita.getProfesional().getNombre() : null)
                .especialidad(cita.getProfesional() != null ? cita.getProfesional().getEspecialidad() : null)
                .build();
    }
}
