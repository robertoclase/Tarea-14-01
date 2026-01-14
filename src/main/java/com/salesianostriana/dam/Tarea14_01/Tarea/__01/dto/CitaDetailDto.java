package com.salesianostriana.dam.Tarea14_01.Tarea.__01.dto;

import com.salesianostriana.dam.Tarea14_01.Tarea.__01.model.Cita;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.model.Consulta;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.model.Estado;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.model.Paciente;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.model.Profesional;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CitaDetailDto(
        Long id,
        LocalDateTime fechaHora,
        Estado estado,
        PacienteDto paciente,
        ProfesionalDto profesional,
        ConsultaDto consulta
) {

    public static CitaDetailDto from(Cita cita) {
        return CitaDetailDto.builder()
                .id(cita.getId())
                .fechaHora(cita.getFechaHora())
                .estado(cita.getEstado())
                .paciente(PacienteDto.from(cita.getPaciente()))
                .profesional(ProfesionalDto.from(cita.getProfesional()))
                .consulta(ConsultaDto.from(cita.getConsulta()))
                .build();
    }

    @Builder
    public record PacienteDto(Long id, String nombre, String email) {
        public static PacienteDto from(Paciente paciente) {
            if (paciente == null) {
                return null;
            }
            return new PacienteDto(paciente.getId(), paciente.getNombre(), paciente.getEmail());
        }
    }

    @Builder
    public record ProfesionalDto(Long id, String nombre, String especialidad) {
        public static ProfesionalDto from(Profesional profesional) {
            if (profesional == null) {
                return null;
            }
            return new ProfesionalDto(profesional.getId(), profesional.getNombre(), profesional.getEspecialidad());
        }
    }

    @Builder
    public record ConsultaDto(Long id, String observaciones, String diagnostico, LocalDateTime fecha) {
        public static ConsultaDto from(Consulta consulta) {
            if (consulta == null) {
                return null;
            }
            return new ConsultaDto(
                    consulta.getId(),
                    consulta.getObservaciones(),
                    consulta.getDiagnostico(),
                    consulta.getFecha()
            );
        }
    }
}
