package data.strategy

import data.Placar

class NormalStrategy : ScoringStrategy {
    override fun pontua(placar: Placar, time: Int) : ScoringStrategy {
        placar.pontos[time]++;

        // Team does not score a game
        if (placar.pontos[time] != 4) return this

        // Team scores
        placar.pontos = arrayOf(0, 0)
        placar.games[time]++

        // No risk of tiebreaker
        if (placar.games[time] < placar.gamesToSet) return  this

        // Wins set regularly
        if(placar.games[time]-placar.games[1-time] >= 2) return updateSets(placar, time)

        // Both are at 6 games, tiebreaker required
        if (placar.games[time] == placar.games[1-time] ) return TiebreakerStrategy()

        // Either 5 x 6 or 6 x 5
        return this
    }
}