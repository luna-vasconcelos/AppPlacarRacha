package ufc.smd.esqueleto_placar

import PopupMessageDialog
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator

import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.getSystemService
import ufc.smd.esqueleto_placar.data.Placar
import ufc.smd.esqueleto_placar.data.strategy.EndgameStrategy
import ufc.smd.esqueleto_placar.data.strategy.NormalStrategy
import ufc.smd.esqueleto_placar.data.strategy.SupertieStrategy
import ufc.smd.esqueleto_placar.data.strategy.TiebreakerStrategy
import org.w3c.dom.Text
import ufc.smd.esqueleto_placar.data.Timer
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.charset.StandardCharsets

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PlacarActivity : AppCompatActivity() {
    lateinit var placar: Placar
    lateinit var tvResultado: Array<TextView>
    val pilhaPlacar = java.util.Stack<Placar>()
    lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placar)

        placar = getIntent().getExtras()?.getSerializable("placar") as Placar
        tvResultado = arrayOf(findViewById(R.id.tvPlacar1), findViewById(R.id.tvPlacar2))

        val tvTimeA : TextView? = findViewById<TextView?>(R.id.tvTimeA)
            tvTimeA?.text = placar.timeA
        val tvTimeB : TextView? = findViewById<TextView?>(R.id.tvTimeB)
            tvTimeB?.text = placar.timeB

        updatePlacar()

        var timerTextView : TextView = findViewById(R.id.timerTextView)
        Log.d("pdm",timerTextView.toString())
        if(placar.has_timer) {
            timer = Timer(onTick = { elapsedTime ->
                val minutes = (elapsedTime) / 60
                val seconds = (elapsedTime) % 60
                timerTextView.text = String.format("%d:%02d", minutes, seconds)
            })

            var pauseButton: TextView = findViewById(R.id.timerTextView)
            pauseButton?.setOnClickListener() {
                timer.toggle()
            }

            timer.start()
        }
        else {
            timer = Timer(onTick = {elapsedTime -> elapsedTime})
        }
    }

    fun updatePlacar() {
        val tvNomePartida = findViewById(R.id.tvNomePartida2) as TextView?
        val tvGames: Array<TextView?> = arrayOf(findViewById(R.id.tvGames1), findViewById(R.id.tvGames2))
        val tvSets: Array<TextView?> = arrayOf(findViewById(R.id.tvSets1), findViewById(R.id.tvSets2))
        val tvNomeJogadores: Array<TextView?> = arrayOf(findViewById(R.id.tvNomeJogador1),findViewById(R.id.tvNomeJogador2),findViewById(R.id.tvNomeJogador3),findViewById(R.id.tvNomeJogador4))

        tvNomePartida?.text = placar.nome_partida

        for (i in 0..1) {
            tvGames[i]?.text = placar.games[i].toString()
            tvSets[i]?.text = placar.sets[i].toString()
            tvResultado[i].text = placar.getPontos(i)
            
            tvNomeJogadores[2*i]?.text = placar.nomeJogadores[i].first
            tvNomeJogadores[2*i+1]?.text = placar.nomeJogadores[i].second
        }

        placar.resultado = when {
            placar.regra is NormalStrategy -> "normal"
            placar.regra is TiebreakerStrategy -> "empate"
            placar.regra is SupertieStrategy -> "supertie"
            placar.regra is EndgameStrategy -> "acabou"
            else -> "bug"
        }

    }

    fun alteraPlacar(v: View) {
        var lados: Int = placar.ladosTrocados
        if (v is TextView && !placar.jogoFinalizado()) {
            pilhaPlacar.push(placar.copy())
            val time = if (v.id == tvResultado[0].id) 0 else 1
            placar.pontua(time)
            updatePlacar()
        }
        updatePlacar()
        if(lados != placar.ladosTrocados) {
            timer.pause()
            val popup = PopupMessageDialog("Troca de lados!") {
                timer.start()
            }
            popup.show(supportFragmentManager, "popup_message")

        }

    }

    fun  desfazer(v: View) {
        if (pilhaPlacar.empty()) {
            // TODO: mensagem de erro caso não haja movimento para desfazer
            return
        }
        placar = pilhaPlacar.pop()
        for (i in 0..1) {
            tvResultado[i].text = placar.pontos[i].toString()
        }
        updatePlacar()
    }

    fun saveGame(v: View) {
        val sharedFilename = "PreviousGames"
        val sp: SharedPreferences = getSharedPreferences(sharedFilename, Context.MODE_PRIVATE)
        val edShared = sp.edit()

        var numMatches = sp.getInt("numberMatch", 0) + 1
        if (numMatches > 10) {
            for (i in 1 until numMatches) {
                val game = sp.getString("match${i + 1}", "")
                val dateTime = sp.getString("matchDateTime${i + 1}", "")
                edShared.putString("match$i", game)
                edShared.putString("matchDateTime$i", dateTime)
            }
            numMatches = 5
        }

        edShared.putInt("numberMatch", numMatches)

        val date = Date()
        val dateTimeFormat = SimpleDateFormat("dd/MM/yy - HH:mm", Locale.getDefault())
        val formattedDate = dateTimeFormat.format(date)
        edShared.putString("matchDateTime$numMatches", formattedDate)
        placar.dataJogo = formattedDate

        val dt = ByteArrayOutputStream()
        val oos = ObjectOutputStream(dt)
        oos.writeObject(placar)

        edShared.putString("match$numMatches", dt.toString(StandardCharsets.ISO_8859_1.name()))
        edShared.commit()
        // TODO: confirmação de jogo salvo pro usuário
    }
}