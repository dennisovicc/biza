package com.biza.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente {

  @Id
  @Column(columnDefinition = "uuid")
  private UUID id;

  @PrePersist
  public void pre() {
    if (id == null) id = UUID.randomUUID();
    if (createdAt == null) createdAt = OffsetDateTime.now();
    if (updatedAt == null) updatedAt = createdAt;
  }

  @PreUpdate
  public void preUpdate() {
    updatedAt = OffsetDateTime.now();
  }  
  
  @Column(nullable = false)
  private String nome;

  @Column(unique = true)
  private String nuit;

  @Column(unique = true)
  private String bi;

  private String endereco;
  private String telefone;
  private String email;

  @Column(name = "tipo_cliente")
  private String tipoCliente; // "PESSOA" / "EMPRESA"

  @Column(name = "renda_mensal", precision = 14, scale = 2)
  private BigDecimal rendaMensal;

  @Column(name = "created_at")
  private OffsetDateTime createdAt;

  @Column(name = "updated_at")
  private OffsetDateTime updatedAt;

  // Getters e Setters
  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public String getNome() { return nome; }
  public void setNome(String nome) { this.nome = nome; }
  public String getNuit() { return nuit; }
  public void setNuit(String nuit) { this.nuit = nuit; }
  public String getBi() { return bi; }
  public void setBi(String bi) { this.bi = bi; }
  public String getEndereco() { return endereco; }
  public void setEndereco(String endereco) { this.endereco = endereco; }
  public String getTelefone() { return telefone; }
  public void setTelefone(String telefone) { this.telefone = telefone; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getTipoCliente() { return tipoCliente; }
  public void setTipoCliente(String tipoCliente) { this.tipoCliente = tipoCliente; }
  public BigDecimal getRendaMensal() { return rendaMensal; }
  public void setRendaMensal(BigDecimal rendaMensal) { this.rendaMensal = rendaMensal; }
  public OffsetDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
  public OffsetDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
