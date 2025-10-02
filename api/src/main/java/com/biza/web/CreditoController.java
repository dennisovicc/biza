package com.biza.web;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.biza.domain.Credito;
import com.biza.domain.enums.StatusCredito;
import com.biza.dto.CreditoRequest;
import com.biza.dto.CreditoResponse;
import com.biza.service.CreditoService;
import com.biza.util.PageResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/creditos")
@CrossOrigin(origins = "*")
public class CreditoController {

    private final CreditoService service;

    public CreditoController(CreditoService service) {
        this.service = service;
    }

    @PostMapping
    public CreditoResponse criar(@RequestBody @Valid CreditoRequest req) {
        var c = service.criar(req);
        return toResponse(c);
    }

    @GetMapping("/{id}")
    public CreditoResponse obter(@PathVariable UUID id) {
        return toResponse(service.obter(id));
    }

    // Lista com filtros opcionais e paginação/ordenação manual (robusto contra 400 por binding)
    @GetMapping
    public PageResponse<CreditoResponse> listar(@RequestParam(required = false) String clienteId,
                                                @RequestParam(required = false) String status,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "createdAt,desc") String sort) {

        // sort: "campo,asc|desc"
        String[] parts = sort.split(",", 2);
        String campo = parts[0];
        Sort.Direction dir = (parts.length > 1 && parts[1].equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        var pageable = PageRequest.of(page, size, Sort.by(dir, campo));

        UUID clienteUUID = toUuid(clienteId);
        StatusCredito statusEnum = toStatus(status);

        var pagina = service.listar(clienteUUID, statusEnum, pageable);
        return PageResponse.of(pagina.map(this::toResponse));
    }

    @PatchMapping("/{id}/aprovar")
    public CreditoResponse aprovar(@PathVariable UUID id) {
        return toResponse(service.aprovar(id));
    }
  
    @PatchMapping("/{id}/rejeitar")
    public CreditoResponse rejeitar(@PathVariable UUID id) {
        return toResponse(service.rejeitar(id));
    }

    @PatchMapping("/{id}/liberar")
    public CreditoResponse liberar(@PathVariable UUID id) {
        return toResponse(service.liberar(id));
    }

    @PatchMapping("/{id}/liquidar")
    public CreditoResponse liquidar(@PathVariable UUID id) {
        return toResponse(service.liquidar(id));
    }

    // --------- helpers ---------
    private UUID toUuid(String s) {
        if (s == null || s.isBlank()) return null;
        try { return UUID.fromString(s); }
        catch (IllegalArgumentException ex) { throw new IllegalArgumentException("clienteId inválido"); }
    }

    private StatusCredito toStatus(String s) {
        if (s == null || s.isBlank()) return null;
        try { return StatusCredito.valueOf(s.toUpperCase()); }
        catch (IllegalArgumentException ex) { throw new IllegalArgumentException("status inválido"); }
    }

    private CreditoResponse toResponse(Credito c) {
        return new CreditoResponse(
                c.getId(),
                c.getClienteId(),
                c.getMontante(),
                c.getTaxaJurosAnual(),
                c.getPrazoMeses(),
                c.getStatus(),
                c.getDataInicio(),
                c.getSaldoDevedor(),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }
}
