package com.salesianostriana.dam.Tarea14_01.Tarea.__01.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salesianostriana.dam.Tarea14_01.Tarea.__01.dto.CitaListDto;
import com.salesianostriana.dam.Tarea14_01.Tarea.__01.service.CitaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final CitaService citaService;

    @GetMapping("/{id}/citas")
    public Page<CitaListDto> citasDePaciente(@PathVariable Long id,
                                             @PageableDefault(size = 10, sort = "fechaHora") Pageable pageable) {
        return citaService.citasDePaciente(id, pageable);
    }
}
