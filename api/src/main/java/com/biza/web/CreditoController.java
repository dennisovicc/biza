package com.biza.web;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    // OFICIAL_CREDITO cria pedido
    @PreAuthorize("hasRole('OFICIAL_CREDITO')")
    @PostMapping
    public CreditoResponse criar(@RequestBody @Valid CreditoRequest req) {
        return toResponse(service.criar(req));
    }

    // consulta (OFICIAL, GESTOR, ADMIN)
    @PreAuthorize("hasAnyRole('OFICIAL_CREDITO','GESTOR_CREDITO','ADMIN')")
    @GetMapping("/{id}")
    public CreditoResponse obter(@PathVariable UUID id) {
        return toResponse(service.obter(id));
    }

    // lista (OFICIAL, GESTOR, ADMIN)
    @PreAuthorize("hasAnyRole('OFICIAL_CREDITO','GESTOR_CREDITO','ADMIN')")
    @GetMapping
    public PageResponse<CreditoResponse> listar(@RequestParam(required = false) Long clienteId,
                                               @RequestParam(required = false) String status,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(defaultValue = "createdAt,desc") String sort) {

        String[] parts = sort.split(",", 2);
        String campo = parts[0];
        Sort.Direction dir = (parts.length > 1 && parts[1].equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        var pageable = PageRequest.of(page, size, Sort.by(dir, campo));

        StatusCredito statusEnum = toStatus(status);
        var pagina = service.listar(clienteId, statusEnum, pageable);

        return PageResponse.of(pagina.map(this::toResponse));
    }

    // GESTOR faz workflow
    @PreAuthorize("hasRole('GESTOR_CREDITO')")
    @PatchMapping("/{id}/aprovar")
    public CreditoResponse aprovar(@PathVariable UUID id) {
        return toResponse(service.aprovar(id));
    }

    @PreAuthorize("hasRole('GESTOR_CREDITO')")
    @PatchMapping("/{id}/rejeitar")
    public CreditoResponse rejeitar(@PathVariable UUID id) {
        return toResponse(service.rejeitar(id));
    }

    @PreAuthorize("hasRole('GESTOR_CREDITO')")
    @PatchMapping("/{id}/liberar")
    public CreditoResponse liberar(@PathVariable UUID id) {
        return toResponse(service.liberar(id));
    }

    @PreAuthorize("hasRole('GESTOR_CREDITO')")
    @PatchMapping("/{id}/liquidar")
    public CreditoResponse liquidar(@PathVariable UUID id) {
        return toResponse(service.liquidar(id));
    }

    @PreAuthorize("hasRole('GESTOR_CREDITO')")
    @PatchMapping("/{id}/atualizar-saldo")
    public CreditoResponse atualizarSaldo(@PathVariable UUID id) {
        return toResponse(service.atualizarSaldoDevedor(id));
    }

    // helpers
    private StatusCredito toStatus(String s) {
        if (s == null || s.isBlank()) return null;
        try { return StatusCredito.valueOf(s.toUpperCase()); }
        catch (IllegalArgumentException ex) { throw new IllegalArgumentException("status inválido"); }
    }

    private CreditoResponse toResponse(Credito c) {
        return new CreditoResponse(
                c.getId(),
                c.getClienteId(),
                c.getTipoCredito(),
                c.getMontante(),
                c.getPrazoMeses(),
                c.getTaxaJurosMensal(),
                c.getStatus(),
                c.getDataInicio(),
                c.getSaldoDevedor(),
                c.getMesesEmAtraso(),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }
}
