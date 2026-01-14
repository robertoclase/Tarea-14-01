package com.salesianostriana.dam.Tarea14_01.Tarea.__01.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.salesianostriana.dam.Tarea14_01.Tarea.__01.dto.CitaDetailDto;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.dto.CitaListDto;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.dto.CreateCitaRequest;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.dto.CreateConsultaRequest;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.model.Cita;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.model.Consulta;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.model.Estado;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.model.Paciente;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.model.Profesional;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.repository.CitaRepository;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.repository.ConsultaRepository;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.repository.PacienteRepository;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.repository.ProfesionalRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CitaService {

	private final CitaRepository citaRepository;
	private final PacienteRepository pacienteRepository;
	private final ProfesionalRepository profesionalRepository;
	private final ConsultaRepository consultaRepository;

	public CitaDetailDto crearCita(CreateCitaRequest request) {
		Paciente paciente = pacienteRepository.findById(request.pacienteId())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));
		Profesional profesional = profesionalRepository.findById(request.profesionalId())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesional no encontrado"));

		LocalDateTime fechaHora = request.fechaHora();

		if (fechaHora == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe indicar fecha y hora");
		}
		if (fechaHora.isBefore(LocalDateTime.now())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pueden crear citas en el pasado");
		}
		if (citaRepository.existsByProfesionalIdAndFechaHora(profesional.getId(), fechaHora)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El profesional ya tiene una cita en ese momento");
		}
		if (citaRepository.tieneCitaElMismoDia(paciente.getId(), fechaHora)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El paciente ya tiene una cita ese día");
		}

		Cita cita = Cita.builder()
				.fechaHora(fechaHora)
				.estado(Estado.PROGRAMADA)
				.paciente(paciente)
				.profesional(profesional)
				.build();

		return CitaDetailDto.from(citaRepository.save(cita));
	}

	public CitaDetailDto cancelarCita(Long citaId) {
		Cita cita = obtenerCita(citaId);

		if (cita.getEstado() == Estado.ATENDIDA) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede cancelar una cita ya atendida");
		}

		cita.setEstado(Estado.CANCELADA);
		return CitaDetailDto.from(cita);
	}

	public CitaDetailDto registrarConsulta(Long citaId, CreateConsultaRequest request) {
		Cita cita = obtenerCita(citaId);

		if (cita.getEstado() != Estado.PROGRAMADA) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se puede registrar consulta para citas programadas");
		}

		Consulta consulta = Consulta.builder()
				.observaciones(request.observaciones())
				.diagnostico(request.diagnostico())
				.fecha(request.fecha() != null ? request.fecha() : LocalDateTime.now())
				.build();

		consultaRepository.save(consulta);

		cita.setConsulta(consulta);
		cita.setEstado(Estado.ATENDIDA);

		return CitaDetailDto.from(cita);
	}

	@Transactional(readOnly = true)
	public Page<CitaListDto> buscarCitas(Estado estado, Pageable pageable) {
		if (estado != null) {
			return citaRepository.findByEstado(estado, pageable).map(CitaListDto::from);
		}

		return citaRepository.findAll(pageable).map(CitaListDto::from);
	}

	@Transactional(readOnly = true)
	public Page<CitaListDto> citasDePaciente(Long pacienteId, Pageable pageable) {
		if (!pacienteRepository.existsById(pacienteId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado");
		}

		return citaRepository.findByPacienteId(pacienteId, pageable).map(CitaListDto::from);
	}

	@Transactional(readOnly = true)
	public CitaDetailDto detalle(Long id) {
		return CitaDetailDto.from(obtenerCita(id));
	}

	@Transactional(readOnly = true)
	public List<CitaListDto> agendaProfesional(Long profesionalId, LocalDate dia) {
		if (!profesionalRepository.existsById(profesionalId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesional no encontrado");
		}

		return citaRepository.findAgendaDiariaProfesional(profesionalId, dia)
				.stream()
				.map(CitaListDto::from)
				.toList();
	}

	private Cita obtenerCita(Long citaId) {
		return citaRepository.findById(citaId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));
	}
}