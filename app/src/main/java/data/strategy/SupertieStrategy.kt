package data.strategy

import data.Placar

class SupertieStrategy : ScoringStrategy {
    override fun pontua(placar: Placar, time: Int): ScoringStrategy {
        placar.pontos[time]++;

        if (placar.pontos[time] != 10 || placar.pontos[time] - placar.pontos[1-time] < 2) return this

        return EndgameStrategy()
    }
}