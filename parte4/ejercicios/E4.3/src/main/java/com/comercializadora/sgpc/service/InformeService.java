// Archivo: src/main/java/com/comercializadora/sgpc/service/InformeService.java
package com.comercializadora.sgpc.service;

import com.comercializadora.sgpc.dto.ContratoLinea;
import com.comercializadora.sgpc.dto.LineaCartera;
import com.comercializadora.sgpc.dto.PaginaCartera;
import com.comercializadora.sgpc.dto.ProveedorResumen;
import com.comercializadora.sgpc.model.Proveedor;
import com.comercializadora.sgpc.repository.ProveedorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // Ejercicio E4.3, primera parte: la misma carga con @EntityGraph
    @Transactional(readOnly = true)
    public List<LineaCartera> carteraConGrafo() {
        return repositorio.findAllByOrderByRazonSocialAsc().stream()
                .map(this::aLinea).toList();
    }

    // Ejercicio E4.3, segunda parte: el JOIN FETCH paginado directamente
    @Transactional(readOnly = true)
    public PaginaCartera carteraPaginadaDirecta(int pagina, int tamanio) {
        Pageable orden = PageRequest.of(pagina, tamanio, Sort.by("razonSocial", "id"));
        Page<Proveedor> p = repositorio.buscarConContratosPaginado(orden);
        return new PaginaCartera(p.getContent().stream().map(this::aLinea).toList(),
                p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages());
    }

    // Ejercicio E4.3, tercera parte: paginación portable en dos consultas
    @Transactional(readOnly = true)
    public PaginaCartera carteraPaginada(int pagina, int tamanio) {
        Pageable orden = PageRequest.of(pagina, tamanio, Sort.by("razonSocial", "id"));
        Page<Long> ids = repositorio.buscarIds(orden);           // select 1 (+ recuento)
        List<LineaCartera> lineas = ids.isEmpty() ? List.of()
                : repositorio.buscarConContratosPorIds(ids.getContent())   // select 2
                        .stream().map(this::aLinea).toList();
        return new PaginaCartera(lineas, ids.getNumber(), ids.getSize(),
                ids.getTotalElements(), ids.getTotalPages());
    }

    // La conversión de la Práctica 14, extraída a un método
    private LineaCartera aLinea(Proveedor p) {
        return new LineaCartera(p.getRazonSocial(), p.getContratos().stream()
                .map(c -> new ContratoLinea(c.getNumero(), c.getMonto(),
                        c.getFechaVencimiento()))
                .toList());
    }
}
