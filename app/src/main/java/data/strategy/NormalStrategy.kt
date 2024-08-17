package data.strategy

import data.Placar

class NormalStrategy : ScoringStrategy {
    override fun pontua(placar: Placar, time: Int) : ScoringStrategy {
        placar.pontos[time]++;

        // Team does not score
        if (placar.pontos[time] != 4) return this

        // Team scores
        placar.pontos = arrayOf(0, 0)
        placar.games[time]++

        // No risk of tiebreaker
        if (placar.games[time] != placar.gamesToSet) return this

        // time wins set
        if(placar.games[1-time] != placar.gamesToSet) {
            return updateSets(placar, time)
        }
        else {
            return TiebreakerStrategy()
        }
    }
}