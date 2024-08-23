package data.strategy

import data.Placar

class SupertieStrategy : ScoringStrategy {
    override fun getPontos(placar: Placar, time: Int): String {
        return String.format("%02d",placar.pontos[time])
    }

    override fun pontua(placar: Placar, time: Int): ScoringStrategy {
        placar.pontos[time]++;

        // Change ends
        if ((placar.pontos[time] + placar.pontos[1-time])%5 == 1)
            placar.ladosTrocados = 1 - placar.ladosTrocados

        if (placar.pontos[time] < 10 || placar.pontos[time] - placar.pontos[1-time] < 2) return this

        return EndgameStrategy()
    }
}