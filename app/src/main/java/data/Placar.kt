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

    fun pontua(time: Int) : EstadoPartida {
        var estado = EstadoPartida.CONTINUA

        pontos[time]++;
        if (pontos[time] == 4) {
            estado = EstadoPartida.GAME_COMPLETO
            pontos[time] = 0;

            games[time]++
            if (games[time] == gamesToSet) {
                estado = EstadoPartida.SET_COMPLETO
                games[time] = 0

                sets[time]++;

                if (sets[time] > totalSets-sets[time]) {
                    estado = EstadoPartida.PARTIDA_ENCERRADA
                }
            }
        }

        return estado
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
