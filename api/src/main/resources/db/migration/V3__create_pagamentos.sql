create table if not exists pagamentos (
  id uuid primary key,
  credito_id uuid not null,
  valor numeric(18,2) not null,
  data_pagamento timestamp with time zone not null default now(),
  created_at timestamp with time zone not null default now(),
  updated_at timestamp with time zone not null default now()
);

create index if not exists idx_pagamentos_credito on pagamentos (credito_id);
