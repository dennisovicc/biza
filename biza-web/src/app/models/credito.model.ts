export type TipoCredito = 'RAPIDO' | 'LONGO';

export type StatusCredito =
  | 'SOLICITADO'
  | 'APROVADO'
  | 'REJEITADO'
  | 'EM_CURSO'
  | 'LIQUIDADO';

export interface Credito {
  id: string;              // UUID
  clienteId: number;       // Long
  tipoCredito: TipoCredito;
  montante: number;
  prazoMeses: number;
  taxaJurosMensal: number; // 15 ou 30
  status: StatusCredito;
  dataInicio?: string | null;
  saldoDevedor: number;
  mesesEmAtraso: number;
  createdAt: string;
  updatedAt: string;
}
