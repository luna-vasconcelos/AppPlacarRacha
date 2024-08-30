package ufc.smd.esqueleto_placar.data.strategy
import ufc.smd.esqueleto_placar.data.Placar
import java.io.Serializable

fun updateSets(placar: Placar, time: Int): ScoringStrategy {
    placar.pontos = arrayOf(0, 0)
    placar.games = arrayOf(0, 0)
    placar.sets[time]++
    when { // Endgame
        (placar.sets[time] > placar.totalSets - placar.sets[time]) -> {
            return EndgameStrategy()
        } // Supertiebreaker
        (placar.sets[time]+placar.sets[1-time] == placar.totalSets-1) -> {
            return SupertieStrategy()
        } // Just scored a set
        else -> {
            return  NormalStrategy()
        }
    }
}
interface ScoringStrategy : Serializable {
    fun getPontos(placar: Placar, time: Int): String
    fun pontua(placar: Placar, time: Int): ScoringStrategy
}