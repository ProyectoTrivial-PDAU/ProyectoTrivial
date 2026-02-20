export interface User {
  id?: number;
  email?: string;
  nombre_usuario: string;
  password?: string;
  name?: string; // For compatibility
  nickname?: string; // For compatibility
}

export interface RankingEntry {
  name: string;
  score: number;
  total: number;
  date: string;
  category: string;
}
