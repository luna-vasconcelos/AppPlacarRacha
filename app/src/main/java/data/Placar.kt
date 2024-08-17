package data

import ufc.smd.esqueleto_placar.R
import java.io.Serializable

data class Placar(var nome_partida: String, var resultado: String, var resultadoLongo: String, var has_timer: Boolean) : Serializable {
    var nomeJogadores: Array<Pair<String, String>> = arrayOf(Pair("Jogador", "Jogador"), Pair("Jogador", "Jogador"))
    var pontos: Array<Int> = arrayOf(0, 0)

    fun pontua(time: Int) {
        pontos[time]++;
    }

    fun copy() : Placar{
        var answ: Placar = Placar(nome_partida, resultado, resultadoLongo, has_timer)
        answ.pontos = pontos.copyOf()
        answ.nomeJogadores = nomeJogadores.copyOf()
        return answ
    }

}
