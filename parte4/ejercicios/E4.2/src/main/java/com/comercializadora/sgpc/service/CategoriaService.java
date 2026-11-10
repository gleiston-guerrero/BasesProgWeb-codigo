// Archivo: src/main/java/com/comercializadora/sgpc/service/CategoriaService.java
package com.comercializadora.sgpc.service;

import com.comercializadora.sgpc.audit.Auditable;
import com.comercializadora.sgpc.dto.CategoriaDTO;
import com.comercializadora.sgpc.dto.CategoriaRequest;
import com.comercializadora.sgpc.exception.CategoriaDuplicadaException;
import com.comercializadora.sgpc.model.Categoria;
import com.comercializadora.sgpc.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoriaService {
    private final CategoriaRepository categorias;
    public CategoriaService(CategoriaRepository categorias) {
        this.categorias = categorias;
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listar() {
        return categorias.findAll().stream().map(CategoriaDTO::desde).toList();
    }

    @Auditable
    public CategoriaDTO crear(CategoriaRequest req) {
        if (categorias.existsByNombre(req.nombre())) {
            throw new CategoriaDuplicadaException(req.nombre());        // 409
        }
        Categoria c = new Categoria();
        c.setNombre(req.nombre());
        return CategoriaDTO.desde(categorias.save(c));
    }
}
