package data.strategy

import data.Placar

class NormalStrategy : ScoringStrategy {
    override fun getPontos(placar: Placar, time: Int): String {
        return when(placar.pontos[time]){
            0 -> "00"
            1 -> "15"
            2 -> "30"
            3 -> "40"
            else -> ""
        }
    }

    override fun pontua(placar: Placar, time: Int) : ScoringStrategy {
        placar.pontos[time]++;

        // Team does not score a game
        if (placar.pontos[time] != 4) return this

        // Team scores
        placar.pontos = arrayOf(0, 0)
        placar.games[time]++

        // Change ends
        if ((placar.games[time] + placar.games[1-time])%2 == 1)
            placar.ladosTrocados = 1 - placar.ladosTrocados

        // Wins set regularly
        if(placar.games[time]-placar.games[1-time] >= 2) return updateSets(placar, time)

        // Both are at 6 games, tiebreaker required
        if (placar.games[time] == placar.games[1-time] ) return TiebreakerStrategy()

        // Either 5 x 6 or 6 x 5
        return this
    }
}