export interface User {
  id?: number;
  name: string;
  nickname?: string;
}

export interface RankingEntry {
  name: string;
  score: number;
  total: number;
  date: string;
  category: string;
}
