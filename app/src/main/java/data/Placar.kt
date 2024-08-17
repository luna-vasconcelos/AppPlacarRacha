package data

import ufc.smd.esqueleto_placar.R
import java.io.Serializable

enum class EstadoPartida {
    CONTINUA,
    GAME_COMPLETO,
    SET_COMPLETO,
    PARTIDA_ENCERRADA
}

data class Placar(var nome_partida: String, var resultado: String, var resultadoLongo: String, var has_timer: Boolean, var gamesToSet: Int = 6, var totalSets: Int = 5) : Serializable {
    var nomeJogadores: Array<Pair<String, String>> = arrayOf(Pair("Jogador", "Jogador"), Pair("Jogador", "Jogador"))
    var pontos: Array<Int> = arrayOf(0, 0)
    var games: Array<Int> = arrayOf(0, 0)
    var sets: Array<Int> = arrayOf(0, 0)

    fun pontua(time: Int) : Int {
        val toInt: (Boolean) -> Int = { if (it) 1 else 0 }
        pontos[time] = (pontos[time]+1)%4;
        games[time] = (games[time] + toInt(pontos[time]==0))%gamesToSet
        sets[time] = (sets[time] + toInt(games[time]==0))
        return toInt(pontos[time]==0) + toInt(games[time]==0) + toInt(sets[time] > totalSets-sets[time])
    }

    fun copy() : Placar{
        var answ: Placar = Placar(nome_partida, resultado, resultadoLongo, has_timer)
        answ.pontos = pontos.copyOf()
        answ.nomeJogadores = nomeJogadores.copyOf()
        answ.games = games.copyOf()
        answ.sets = sets.copyOf()
        return answ
    }

}
