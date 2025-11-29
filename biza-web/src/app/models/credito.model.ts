export type StatusCredito =
  | 'SOLICITADO'
  | 'APROVADO'
  | 'REJEITADO'
  | 'EM_CURSO'
  | 'LIQUIDADO';

export interface Credito {
  id?: string;
  clienteId: string;
  montante: number;
  taxaJurosAnual: number;
  prazoMeses: number;
  status?: StatusCredito;
  saldoDevedor?: number;
  dataInicio?: string;
  createdAt?: string;
  updatedAt?: string;
}
