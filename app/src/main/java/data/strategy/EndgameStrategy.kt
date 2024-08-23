package data.strategy

import data.Placar

class EndgameStrategy : ScoringStrategy{
    override fun getPontos(placar: Placar, time: Int): String {
        return ""
    }

    override fun pontua(placar: Placar, time: Int): ScoringStrategy {
        return this
    }
}