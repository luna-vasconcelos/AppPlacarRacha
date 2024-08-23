package data

import data.strategy.EndgameStrategy
import data.strategy.NormalStrategy
import data.strategy.ScoringStrategy
import java.io.Serializable

data class Placar(var nome_partida: String, var resultado: String, var resultadoLongo: String, var has_timer: Boolean, var gamesToSet: Int = 6, var totalSets: Int = 5) : Serializable {
    var regra: ScoringStrategy = NormalStrategy()
    var nomeJogadores: Array<Pair<String, String>> = arrayOf(Pair("Jogador", "Jogador"), Pair("Jogador", "Jogador"))
    var pontos: Array<Int> = arrayOf(0, 0)
    var games: Array<Int> = arrayOf(0, 0)
    var sets: Array<Int> = arrayOf(0, 0)

    fun jogoFinalizado(): Boolean {
        return regra is EndgameStrategy
    }

    fun getPontos(time: Int): String {
        return regra.getPontos(this, time)
    }

    fun pontua(time: Int) {
        regra = regra.pontua(this, time)
        if(jogoFinalizado()) {
            resultadoLongo = String.format("%s e %s venceram a partida!", nomeJogadores[time].first, nomeJogadores[time].second)
        }
    }

    fun copy() : Placar{
        var answ: Placar = Placar(nome_partida, resultado, resultadoLongo, has_timer)
        answ.regra = regra
        answ.pontos = pontos.copyOf()
        answ.nomeJogadores = nomeJogadores.copyOf()
        answ.games = games.copyOf()
        answ.sets = sets.copyOf()
        return answ
    }

}
