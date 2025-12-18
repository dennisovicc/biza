DROP TABLE IF EXISTS creditos;

CREATE TABLE creditos (
  id uuid primary key,
  cliente_id bigint not null,
  tipo_credito varchar(20) not null,
  montante numeric(18,2) not null,
  prazo_meses int not null,
  taxa_juros_mensal numeric(5,2) not null,
  status varchar(20) not null,
  data_inicio timestamp with time zone,
  saldo_devedor numeric(18,2) not null,
  created_at timestamp with time zone not null default now(),
  updated_at timestamp with time zone not null default now()
);

CREATE INDEX IF NOT EXISTS idx_creditos_cliente      ON creditos (cliente_id);
CREATE INDEX IF NOT EXISTS idx_creditos_status       ON creditos (status);
CREATE INDEX IF NOT EXISTS idx_creditos_tipo_credito ON creditos (tipo_credito);
