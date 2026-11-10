// Archivo: src/main/java/com/comercializadora/sgpc/service/ContratoService.java
package com.comercializadora.sgpc.service;

import com.comercializadora.sgpc.audit.Auditable;
import com.comercializadora.sgpc.dto.*;
import com.comercializadora.sgpc.exception.*;
import com.comercializadora.sgpc.model.*;
import com.comercializadora.sgpc.repository.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ContratoService {
    // Atributos por los que el cliente puede ordenar con ?sort=
    private static final Set<String> ORDENABLES =
        Set.of("numero", "monto", "fechaInicio", "fechaVencimiento", "id");

    private final ContratoRepository contratos;
    private final ProveedorRepository proveedores;
    public ContratoService(ContratoRepository contratos,
                           ProveedorRepository proveedores) {
        this.contratos = contratos;
        this.proveedores = proveedores;
    }

    @Transactional(readOnly = true)
    public PaginaContratos listarPorProveedor(Long proveedorId, Pageable pageable) {
        verificarProveedor(proveedorId);                    // 404 si no existe
        for (Sort.Order orden : pageable.getSort()) {       // 400 si no se admite
            if (!ORDENABLES.contains(orden.getProperty())) {
                throw new OrdenNoValidoException(orden.getProperty());
            }
        }
        Page<ContratoDTO> p = contratos.findByProveedorId(proveedorId, pageable)
            .map(ContratoDTO::desde);
        return new PaginaContratos(p.getContent(), p.getNumber(), p.getSize(),
            p.getTotalElements(), p.getTotalPages());
    }

    @Transactional(readOnly = true)
    public ContratoDTO buscar(Long proveedorId, Long id) {
        return ContratoDTO.desde(obtener(proveedorId, id));
    }

    @Auditable                       // RNF-04: escritura auditada
    @CacheEvict(value = "contratosVigentes", key = "#proveedorId")   // E4.1
    public ContratoDTO crear(Long proveedorId, ContratoRequest req) {
        Proveedor proveedor = proveedores.findById(proveedorId)
            .orElseThrow(() -> new ProveedorNoEncontradoException(proveedorId));
        if (proveedor.getEstado() != EstadoProveedor.ACTIVO) {  // 409
            throw new ProveedorNoActivoException(proveedorId);
        }
        if (contratos.existsByNumero(req.numero())) {       // 409
            throw new NumeroContratoDuplicadoException(req.numero());
        }
        verificarCredito(proveedor, req, null);              // 409 (RF-08)
        Contrato c = new Contrato();
        copiar(req, c);
        c.setProveedor(proveedor);                          // relación @ManyToOne
        return ContratoDTO.desde(contratos.save(c));
    }

    @Auditable
    @CacheEvict(value = "contratosVigentes", key = "#proveedorId")   // E4.1
    public ContratoDTO actualizar(Long proveedorId, Long id, ContratoRequest req) {
        Contrato c = obtener(proveedorId, id);
        if (contratos.existsByNumeroAndIdNot(req.numero(), id)) {   // 409
            throw new NumeroContratoDuplicadoException(req.numero());
        }
        verificarCredito(c.getProveedor(), req, c);          // antes de copiar
        copiar(req, c);    // entidad gestionada: se guarda al confirmar
        return ContratoDTO.desde(c);
    }

    @Auditable
    @CacheEvict(value = "contratosVigentes", key = "#proveedorId")   // E4.1
    public void eliminar(Long proveedorId, Long id) {
        contratos.delete(obtener(proveedorId, id));
    }

    // Ejercicio E4.1: vigentes sin paginar, cacheados por proveedor
    @Transactional(readOnly = true)
    @Cacheable(value = "contratosVigentes", key = "#proveedorId")
    public List<ContratoVigente> vigentesSinPaginar(Long proveedorId) {
        verificarProveedor(proveedorId);     // 404: una excepción no se cachea
        return contratos.findVigentesByProveedor(proveedorId, LocalDate.now())
                        .stream().map(ContratoVigente::desde).toList();
    }

    // Comprobación de pertenencia: el contrato debe ser de ESE proveedor.
    private Contrato obtener(Long proveedorId, Long id) {
        verificarProveedor(proveedorId);
        return contratos.findByIdAndProveedorId(id, proveedorId)
            .orElseThrow(() -> new ContratoNoEncontradoException(proveedorId, id));
    }

    private void verificarProveedor(Long proveedorId) {
        if (!proveedores.existsById(proveedorId)) {
            throw new ProveedorNoEncontradoException(proveedorId);
        }
    }

    // RF-08: el monto no puede superar el crédito disponible. Un contrato
    // que ya venció no compromete crédito y no se comprueba. Al actualizar,
    // el monto anterior del contrato, si aún no ha vencido, ya está
    // descontado en creditoDisponible() y se devuelve antes de comparar.
    private void verificarCredito(Proveedor p, ContratoRequest req,
                                  Contrato actual) {
        if (req.fechaVencimiento().isBefore(LocalDate.now())) {
            return;                    // vencido: no compromete crédito
        }
        BigDecimal monto = req.monto();
        BigDecimal disponible = p.creditoDisponible();
        if (actual != null && actual.comprometeCredito()) {
            disponible = disponible.add(actual.getMonto());
        }
        if (monto.compareTo(disponible) > 0) {
            throw new LimiteCreditoExcedidoException(p.getId(), monto, disponible);
        }
    }

    private void copiar(ContratoRequest req, Contrato c) {
        c.setNumero(req.numero());
        c.setMonto(req.monto());
        c.setFechaInicio(req.fechaInicio());
        c.setFechaVencimiento(req.fechaVencimiento());
    }
}
