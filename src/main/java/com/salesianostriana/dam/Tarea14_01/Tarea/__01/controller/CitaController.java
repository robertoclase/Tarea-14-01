package com.salesianostriana.dam.Tarea14_01.Tarea.__01.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salesianostriana.dam.Tarea14_01.Tarea.__01.dto.CitaDetailDto;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.dto.CitaListDto;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.dto.CreateCitaRequest;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.dto.CreateConsultaRequest;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.model.Estado;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.service.CitaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @PostMapping
    public ResponseEntity<CitaDetailDto> crear(@RequestBody CreateCitaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citaService.crearCita(request));
    }

    @PutMapping("/{id}/cancelar")
    public CitaDetailDto cancelar(@PathVariable Long id) {
        return citaService.cancelarCita(id);
    }

    @PostMapping("/{id}/consulta")
    public CitaDetailDto registrarConsulta(@PathVariable Long id, @RequestBody CreateConsultaRequest request) {
        return citaService.registrarConsulta(id, request);
    }

    @GetMapping
    public Page<CitaListDto> listar(@RequestParam(required = false) Estado estado,
                                    @PageableDefault(size = 10, sort = "fechaHora") Pageable pageable) {
        return citaService.buscarCitas(estado, pageable);
    }

    @GetMapping("/{id}")
    public CitaDetailDto detalle(@PathVariable Long id) {
        return citaService.detalle(id);
    }

    @GetMapping("/profesionales/{id}/agenda")
    public List<CitaListDto> agenda(@PathVariable Long id,
                                    @RequestParam(required = false)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dia) {
        LocalDate targetDay = dia != null ? dia : LocalDate.now();
        return citaService.agendaProfesional(id, targetDay);
    }
}
