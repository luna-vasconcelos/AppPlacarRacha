package ufc.smd.esqueleto_placar.data.strategy

import ufc.smd.esqueleto_placar.data.Placar

class EndgameStrategy : ScoringStrategy {
    override fun getPontos(placar: Placar, time: Int): String {
        return ""
    }

    override fun pontua(placar: Placar, time: Int): ScoringStrategy {
        return this
    }
}