export interface Cliente {
  id?: string;
  nome: string;
  nuit?: string | null;
  bi?: string | null;
  endereco?: string | null;
  telefone?: string | null;
  email?: string | null;
  tipoCliente?: string | null;
  rendaMensal?: number | null;
  status?: string;
  createdAt?: string;
  updatedAt?: string;
}
