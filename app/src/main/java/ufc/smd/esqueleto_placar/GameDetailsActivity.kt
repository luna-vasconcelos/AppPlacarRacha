package ufc.smd.esqueleto_placar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameDetailsActivity  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_details)

        // Pegando do intent que veio do CustomAdapter
        val nomePartida = intent.getStringExtra("nome_partida")
        val resultadoJogo = intent.getStringExtra("time_vencedor")
        val timeA = intent.getStringExtra("time_A")
        val timeB = intent.getStringExtra("time_B")
        val gamesA = intent.getStringExtra("resultado_jogo_games_A")
        val gamesB = intent.getStringExtra("resultado_jogo_games_B")
        val setsA = intent.getStringExtra("resultado_jogo_sets_A")
        val setsB = intent.getStringExtra("resultado_jogo_sets_B")
//        val dataJogo = intent.getStringExtra("data_jogo")

        // Pegando do layout
        val tvNomePartida = findViewById<TextView>(R.id.Details)
        val tvResultadoJogo = findViewById<TextView>(R.id.Resultado)
        val tvTimeA = findViewById<TextView>(R.id.team1_name)
        val tvTimeB = findViewById<TextView>(R.id.team2_name)
        val tvGamesTimeA = findViewById<TextView>(R.id.team1_games_won)
        val tvGamesTimeB = findViewById<TextView>(R.id.team2_games_won)
        val tvSetsTimeA = findViewById<TextView>(R.id.team1_sets_won)
        val tvSetsTimeB = findViewById<TextView>(R.id.team2_sets_won)
//    val tvDataJogo = findViewById<TextView>(R.id.tvDataJogoDetails)

        // Atualizando layout
        tvNomePartida.text = nomePartida
        tvResultadoJogo.text = "Time vencedor: $resultadoJogo!"
        tvTimeA.text = timeA
        tvTimeB.text = timeB
        tvGamesTimeA.text = "Games vencidos: $gamesA"
        tvGamesTimeB.text = "Games vencidos: $gamesB!"
        tvSetsTimeA.text = "Sets vencidos: $setsA!"
        tvSetsTimeB.text = "Sets vencidos: $setsB!"
//    tvDataJogo.text = dataJogo
    }
}