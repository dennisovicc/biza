create table if not exists creditos (
  id uuid primary key,
  cliente_id uuid not null,
  montante numeric(18,2) not null,
  taxa_juros_anual numeric(5,2) not null,
  prazo_meses int not null,
  status varchar(20) not null,
  data_inicio timestamp with time zone,
  saldo_devedor numeric(18,2) not null,
  created_at timestamp with time zone not null default now(),
  updated_at timestamp with time zone not null default now()
);

create index if not exists idx_creditos_cliente on creditos (cliente_id);
create index if not exists idx_creditos_status  on creditos (status);
