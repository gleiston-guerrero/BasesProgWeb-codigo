// Archivo: src/main/java/com/comercializadora/sgpc/service/ProveedorService.java
// Estado al final de la Parte II: Capítulo 7 + Práctica 6 (actualizar)
// + Capítulo 8 (@Auditable) + Ejercicios E2.1 (darDeBaja) y E2.2 (resumen).
package com.comercializadora.sgpc.service;

import com.comercializadora.sgpc.audit.Auditable;
import com.comercializadora.sgpc.dto.*;
import com.comercializadora.sgpc.exception.*;
import com.comercializadora.sgpc.model.Proveedor;
import com.comercializadora.sgpc.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional   // cada método público se ejecuta en una transacción
public class ProveedorService {

    private final ProveedorRepository repositorio;

    public ProveedorService(ProveedorRepository repositorio) {
        this.repositorio = repositorio;
    }

    // readOnly=true optimiza las consultas que no modifican datos
    @Transactional(readOnly = true)
    public List<ProveedorDTO> listarTodos() {
        return repositorio.findAll().stream().map(this::aDTO).toList();
    }

    @Transactional(readOnly = true)
    public ProveedorDTO buscarPorId(Long id) {
        Proveedor p = repositorio.findById(id)
                .orElseThrow(() -> new ProveedorNoEncontradoException(id));
        return aDTO(p);
    }

    @Auditable   // el aspecto AuditoriaAspect envolverá cada llamada a crear
    public ProveedorDTO crear(ProveedorRequest req) {
        // Regla de negocio: el RUC no puede repetirse
        if (repositorio.findByRuc(req.ruc()).isPresent()) {
            throw new RucDuplicadoException(req.ruc());
        }
        Proveedor p = new Proveedor();
        p.setRuc(req.ruc());
        p.setRazonSocial(req.razonSocial());
        p.setLimiteCredito(req.limiteCredito());
        return aDTO(repositorio.save(p));
    }

    // Práctica 6
    @Auditable   // también la actualización de la Práctica 6
    public ProveedorDTO actualizar(Long id, ProveedorRequest req) {
        Proveedor p = repositorio.findById(id)
                .orElseThrow(() -> new ProveedorNoEncontradoException(id));   // 404
        if (!p.getRuc().equals(req.ruc())
                && repositorio.findByRuc(req.ruc()).isPresent()) {
            throw new RucDuplicadoException(req.ruc());                     // 409
        }
        p.setRuc(req.ruc());
        p.setRazonSocial(req.razonSocial());
        p.setLimiteCredito(req.limiteCredito());
        return aDTO(repositorio.save(p));
    }

    // Ejercicio E2.1
    // Baja FÍSICA provisional; el Capítulo 12 la sustituye por la baja lógica.
    @Auditable                       // RNF-04: también se audita la baja
    public void darDeBaja(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ProveedorNoEncontradoException(id);                 // 404
        }
        repositorio.deleteById(id);      // por sí solo ignoraría un id inexistente
    }

    // Ejercicio E2.2
    @Transactional(readOnly = true)
    public ResumenProveedores resumen() {
        List<BigDecimal> limites = repositorio.findAll().stream()
                .map(Proveedor::getLimiteCredito)
                .toList();
        long total = limites.size();
        BigDecimal limiteTotal = limites.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);           // 0 si no hay
        BigDecimal limiteMedio = (total == 0)
                ? BigDecimal.ZERO                                   // sin dividir entre 0
                : limiteTotal.divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
        return new ResumenProveedores(total, limiteTotal, limiteMedio);
    }

    // Método privado de mapeo entidad -> DTO
    private ProveedorDTO aDTO(Proveedor p) {
        return new ProveedorDTO(p.getId(), p.getRuc(),
                p.getRazonSocial(), p.getLimiteCredito());
    }
}
