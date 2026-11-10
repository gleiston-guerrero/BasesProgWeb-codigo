// Archivo: src/main/java/com/comercializadora/sgpc/service/InformeService.java
package com.comercializadora.sgpc.service;

import com.comercializadora.sgpc.dto.ContratoLinea;
import com.comercializadora.sgpc.dto.LineaCartera;
import com.comercializadora.sgpc.dto.ProveedorResumen;
import com.comercializadora.sgpc.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

// Práctica 14: informe de cartera sin el problema N+1
@Service
public class InformeService {

    private final ProveedorRepository repositorio;

    public InformeService(ProveedorRepository repositorio) {
        this.repositorio = repositorio;
    }

    // Antes de la corrección: repositorio.findAll() -> 1 + N consultas
    @Transactional(readOnly = true)
    public List<LineaCartera> cartera() {
        return repositorio.buscarTodosConContratos().stream()   // 1 consulta
                .map(p -> new LineaCartera(p.getRazonSocial(),
                        p.getContratos().stream()
                         .map(c -> new ContratoLinea(c.getNumero(),
                                 c.getMonto(), c.getFechaVencimiento()))
                         .toList()))
                .toList();
    }

    // Recuento por proveedor con una proyección (COUNT ... GROUP BY)
    @Transactional(readOnly = true)
    public List<ProveedorResumen> conteo() {
        return repositorio.resumenConConteo();                  // 1 consulta
    }
}
