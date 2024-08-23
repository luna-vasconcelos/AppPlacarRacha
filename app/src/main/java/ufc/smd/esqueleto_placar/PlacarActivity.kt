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

class PlacarActivity : AppCompatActivity() {
    lateinit var placar: Placar
    lateinit var tvResultado: Array<TextView>
    val pilhaPlacar = java.util.Stack<Placar>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placar)
        Log.d("d","ping")
        placar = getIntent().getExtras()?.getSerializable("placar") as Placar
        tvResultado = arrayOf(findViewById(R.id.tvPlacar1), findViewById(R.id.tvPlacar2))        
        //Mudar o nome da partida
        val tvNomePartida=findViewById(R.id.tvNomePartida2) as TextView
        //tvNomePartida.text=placar.nome_partida
        ultimoJogos()
        updatePlacar()
    }

    fun updatePlacar() {
        val tvGames: Array<TextView> = arrayOf(findViewById(R.id.tvGames1), findViewById(R.id.tvGames2))
        val tvSets: Array<TextView> = arrayOf(findViewById(R.id.tvSets1), findViewById(R.id.tvSets2))
        for (i in 0..1) {
            tvGames[i].text = placar.games[i].toString()
            tvSets[i].text = placar.sets[i].toString()
            tvResultado[i].text = placar.pontos[i].toString()
        }
        val tvNomePartida=findViewById(R.id.tvNomePartida2) as TextView

        when {
            placar.regras is NormalStrategy -> {
                tvNomePartida.text = "normal"
            }
            placar.regras is TiebreakerStrategy -> {
                tvNomePartida.text = "empate"
            }
            placar.regras is SupertieStrategy -> {
                tvNomePartida.text = "supertie"
            }
            placar.regras is EndgameStrategy ->{
                tvNomePartida.text = "acabou"
            }
            else -> {
                tvNomePartida.text = "bug"
            }
        }
    }

    fun alteraPlacar(v: View) {
       if (v is TextView && !placar.jogoFinalizado()) {
           pilhaPlacar.push(placar.copy())
           val time = if (v.id == tvResultado[0].id) 0 else 1
           placar.pontua(time)
           tvResultado[time].text = placar.pontos[time].toString()
           updatePlacar()
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

    fun vibrar (v:View){
        val buzzer = this.getSystemService<Vibrator>()
         val pattern = longArrayOf(0, 200, 100, 300)
         buzzer?.let {
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
             } else {
                 //deprecated in API 26
                 buzzer.vibrate(pattern, -1)
             }
         }

    }

    fun saveGame(v: View) {
        val sharedFilename = "PreviousGames"
        val sp: SharedPreferences = getSharedPreferences(sharedFilename, Context.MODE_PRIVATE)
        var edShared = sp.edit()
        //Salvar o número de jogos já armazenados
        var numMatches= sp.getInt("numberMatch",0) + 1
        edShared.putInt("numberMatch", numMatches)

        //Escrita em Bytes de Um objeto Serializável
        var dt= ByteArrayOutputStream()
        var oos = ObjectOutputStream(dt);
        oos.writeObject(placar);

        //Salvar como "match"
        edShared.putString("match"+numMatches, dt.toString(StandardCharsets.ISO_8859_1.name()))
        edShared.commit() //Não esqueçam de comitar!!!

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

    fun ultimoJogos () {
        val sharedFilename = "PreviousGames"
        val sp:SharedPreferences = getSharedPreferences(sharedFilename,Context.MODE_PRIVATE)
        var matchStr:String=sp.getString("match1","").toString()
       // Log.v("PDM22", matchStr)
        if (matchStr.length >=1){
            var dis = ByteArrayInputStream(matchStr.toByteArray(Charsets.ISO_8859_1))
            var oos = ObjectInputStream(dis)
            var prevPlacar:Placar = oos.readObject() as Placar
            Log.v("PDM22", "Jogo Salvo:"+ prevPlacar.resultado)
        }
    }
}
