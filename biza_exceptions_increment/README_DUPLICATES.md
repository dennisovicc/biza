# Exceptions increment — Duplicados

Inclui:
- `GlobalExceptionHandler` com handlers para duplicidade:
  - `DomainExceptions.Duplicate` (programático, 409)
  - `DataIntegrityViolationException` (banco, 409)
- `DomainExceptions.Duplicate` com `field` e `value` opcionais.

## Uso rápido (exemplo no serviço de Cliente)
```java
if (repo.existsByNuit(req.getNuit())) {
    throw new DomainExceptions.Duplicate("nuit", req.getNuit(), "Já existe cliente com este NUIT.");
}
```

Banco:
- Garanta `UNIQUE` onde fizer sentido (ex.: `cliente.nuit`).

Resposta (exemplo):
```json
{
  "type": "https://biza.dev/errors/duplicate",
  "title": "Dados duplicados",
  "status": 409,
  "detail": "Já existe cliente com este NUIT.",
  "conflicts": [{"field": "nuit", "value": "123456789"}]
}
```
