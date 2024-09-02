package ufc.smd.esqueleto_placar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Switch
import ufc.smd.esqueleto_placar.data.Placar

class ConfigActivity : AppCompatActivity() {
    var placar: Placar = Placar("Jogo sem Config","", "",false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        openConfig()
        initInterface()

    }
    fun saveConfig(){
        val sharedFilename = "configPlacar"
        val sp:SharedPreferences = getSharedPreferences(sharedFilename,Context.MODE_PRIVATE)
        var edShared = sp.edit()

        edShared.putString("matchname",placar.nome_partida)

        edShared.putString("teamA",placar.timeA)
        edShared.putString("teamB",placar.timeB)

        edShared.putBoolean("has_timer",placar.has_timer)

        edShared.commit()
    }
    fun openConfig()
    {
        val sharedFilename = "configPlacar"
        val sp:SharedPreferences = getSharedPreferences(sharedFilename,Context.MODE_PRIVATE)
        placar.nome_partida = sp.getString("matchname","Jogo Padrão").toString()
        placar.timeA = sp.getString("teamA","TimeA").toString()
        placar.timeB = sp.getString("teamB","TimeB").toString()
        placar.has_timer=sp.getBoolean("has_timer",false)
    }
    fun initInterface(){
        val tv = findViewById<EditText>(R.id.editTextGameName)
        tv.setText(placar.nome_partida)
        val teamA = findViewById<EditText>(R.id.team1)
        teamA.setText(placar.timeA)
        val teamB = findViewById<EditText>(R.id.team2)
        teamB.setText(placar.timeB)
        val sw = findViewById<Switch>(R.id.swTimer)
        sw.isChecked=placar.has_timer

        sw.setOnCheckedChangeListener { _, isChecked ->
            placar.has_timer = isChecked
            saveConfig()
        }
    }

    fun updatePlacarConfig(){
        val tv = findViewById<EditText>(R.id.editTextGameName)
        val sw = findViewById<Switch>(R.id.swTimer)
        val timeA = findViewById<EditText>(R.id.team1)
        val timeB = findViewById<EditText>(R.id.team2)

        placar.nome_partida = tv.text.toString()
        placar.timeA = timeA.text.toString()
        placar.timeB = timeB.text.toString()
        placar.has_timer = sw.isChecked
    }

    fun openPlacar(v: View){ //Executa ao click do Iniciar Jogo
        updatePlacarConfig() //Pega da Interface e joga no placar
        saveConfig() //Salva no Shared preferences
        val intent = Intent(this, PlacarActivity::class.java).apply{
            putExtra("placar", placar)
        }
        startActivity(intent)
    }
}