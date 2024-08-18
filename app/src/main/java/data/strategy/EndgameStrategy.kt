package data.strategy

import data.Placar

class EndgameStrategy : ScoringStrategy{
    override fun pontua(placar: Placar, time: Int): ScoringStrategy {
        return this
    }
}