# Biza Microcrédito – API

API REST para gestão de microcréditos, construída em **Spring Boot**.  
Permite gerir **clientes**, **créditos** e **pagamentos**, com regras de negócio claras e controlo de estados do crédito.

Por defeito, a aplicação arranca com o perfil **H2 em memória**. :contentReference[oaicite:0]{index=0}  

---

## 🚀 Tecnologias principais

- Java 17
- Spring Boot (Web, Data JPA, Validation)
- Springdoc OpenAPI (Swagger)
- H2 em memória (desenvolvimento)
- PostgreSQL (produção)   
- Flyway (migrações para Postgres)
- Postman (colecção de testes) :contentReference[oaicite:2]{index=2}  

---

## 🧱 Domínio funcional

A API implementa um fluxo simples de microcrédito:

1. **Clientes**
   - Criar, listar, actualizar (PUT/PATCH) e remover.
2. **Créditos**
   - Criar crédito para um cliente.
   - Controlar estados:
     - `SOLICITADO` → `APROVADO` → `EM_CURSO` → `LIQUIDADO` / `REJEITADO`.
3. **Pagamentos**
   - Registar pagamentos associados a um crédito.
   - Actualizar automaticamente o **saldo devedor**.
   - Liquidar o crédito quando o saldo chega a zero.

Existem ainda endpoints de **health check** / **ping** à base de dados.

---

## 📁 Estrutura geral (alto nível)

```text
src/main/java/...
  ├─ config/        # OpenApiConfig, WebConfig, etc.
  ├─ controller/    # ClienteController, CreditoController, PagamentoController, DbPingController
  ├─ domain/        # Entities: Cliente, Credito, Pagamento, enums
  ├─ dto/           # Requests e Responses
  ├─ repo/          # Repositórios JPA
  ├─ service/       # CreditoService, PagamentoService, etc.
  └─ exception/     # GlobalExceptionHandler, DomainExceptions

---

## 🔄 4. Fluxograma do sistema (texto + Mermaid)

Podes incluir este fluxograma no próprio README (o GitHub já suporta Mermaid) ou num documento à parte.

### a) Fluxo funcional (alto nível)

```mermaid
flowchart TD

A[Cliente não registado] --> B[Criar Cliente]
B --> C[Cliente registado]

C --> D[Criar Crédito para Cliente]
D --> E{Estado do Crédito}

E -->|Inicial| F[SOLICITADO]

F -->|Regra de negócio OK| G[APROVADO]
F -->|Análise negativa| H[REJEITADO]

G --> I[LIBERAR CRÉDITO]
I --> J[EM_CURSO]

J --> K[Registar Pagamento]
K --> L{Saldo Devedor > 0?}

L -->|Sim| J
L -->|Não (0)| M[LIQUIDADO]

J --> N[Consultar Pagamentos / Créditos / Cliente]

