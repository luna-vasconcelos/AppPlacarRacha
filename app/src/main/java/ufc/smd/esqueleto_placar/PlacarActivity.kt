package ufc.smd.esqueleto_placar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.getSystemService
import data.Placar
import data.strategy.EndgameStrategy
import data.strategy.NormalStrategy
import data.strategy.SupertieStrategy
import data.strategy.TiebreakerStrategy
import org.w3c.dom.Text
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.charset.StandardCharsets

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class PlacarActivity : AppCompatActivity() {
    lateinit var placar: Placar
    lateinit var tvResultado: Array<TextView>
    val pilhaPlacar = java.util.Stack<Placar>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placar)

        placar = getIntent().getExtras()?.getSerializable("placar") as Placar
        tvResultado = arrayOf(findViewById(R.id.tvPlacar1), findViewById(R.id.tvPlacar2))
        //Mudar o nome da partida
        val tvNomePartida=findViewById(R.id.tvNomePartida2) as TextView
        //tvNomePartida.text=placar.nome_partida
        val matchName = "match1"
//        ultimoJogos()
        updatePlacar()
    }

    fun updatePlacar() {
        val tvGames: Array<TextView> = arrayOf(findViewById(R.id.tvGames1), findViewById(R.id.tvGames2))
        val tvSets: Array<TextView> = arrayOf(findViewById(R.id.tvSets1), findViewById(R.id.tvSets2))
        for (i in 0..1) {
            tvGames[i].text = placar.games[i].toString()
            tvSets[i].text = placar.sets[i].toString()
            tvResultado[i].text = placar.getPontos(i)
        }
        val tvNomePartida=findViewById(R.id.tvNomePartida2) as TextView

        when {
            placar.regra is NormalStrategy -> {
                tvNomePartida.text = "normal"
                placar.resultado = "normal"
            }
            placar.regra is TiebreakerStrategy -> {
                tvNomePartida.text = "empate"
                placar.resultado = "empate"
            }
            placar.regra is SupertieStrategy -> {
                tvNomePartida.text = "supertie"
                placar.resultado = "supertie"
            }
            placar.regra is EndgameStrategy ->{
                tvNomePartida.text = "acabou"
                placar.resultado = "acabou"
            }
            else -> {
                tvNomePartida.text = "bug"
                placar.resultado = "bug"
            }
        }
    }

    fun alteraPlacar(v: View) {
       if (v is TextView && !placar.jogoFinalizado()) {
           pilhaPlacar.push(placar.copy())
           val time = if (v.id == tvResultado[0].id) 0 else 1
           placar.pontua(time)
           updatePlacar()
       }
        Log.v("placar_alterado",placar.resultado)
        updatePlacar()
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
        val dateTimeFormat = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())
        val formattedDate = dateTimeFormat.format(date)
        edShared.putString("matchDateTime$numMatches", formattedDate)
        Log.d("DateTimeSave,test", "Saved date and time: $formattedDate")

        placar.resultadoLongo = "${placar.resultadoLongo} - $formattedDate"

        val dt = ByteArrayOutputStream()
        val oos = ObjectOutputStream(dt)
        oos.writeObject(placar)

        Log.v("placar_pro_save",placar.resultadoLongo)

        edShared.putString("match$numMatches", dt.toString(StandardCharsets.ISO_8859_1.name()))
        edShared.commit()
    }

    fun lerUltimosJogos(v: View){
        val sharedFilename = "PreviousGames"
        val sp: SharedPreferences = getSharedPreferences(sharedFilename, Context.MODE_PRIVATE)

        var meuObjString:String= sp.getString("match1","").toString()
        if (meuObjString.length >=1) {
            var dis = ByteArrayInputStream(meuObjString.toByteArray(Charsets.ISO_8859_1))
            var oos = ObjectInputStream(dis)
            var placarAntigo:Placar=oos.readObject() as Placar
            Log.v("SMD26",placar.resultado)
        }
    }

    fun ultimoJogos (matchName: String) {
        val sharedFilename = "PreviousGames"
        val sp:SharedPreferences = getSharedPreferences(sharedFilename,Context.MODE_PRIVATE)

        val matchStr:String = sp.getString(matchName, "").toString()
        Log.v("Jogo do histórico:", matchStr)

        if (matchStr.isNotEmpty()) {
            val dis = ByteArrayInputStream(matchStr.toByteArray(Charsets.ISO_8859_1))
            val oos = ObjectInputStream(dis)
            val prevPlacar: Placar = oos.readObject() as Placar

            // TODO: passar o último jogo pra UI também
//            tvResultado[0].text = prevPlacar.pontos[0].toString()
//            tvResultado[1].text = prevPlacar.pontos[1].toString()
//            tvNomePartida.text = prevPlacar.nome_partida
            Log.v("PDM22", "Jogo Salvo:"+ prevPlacar.resultadoLongo)
        }
    }
}
