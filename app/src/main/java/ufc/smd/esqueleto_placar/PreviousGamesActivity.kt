package ufc.smd.esqueleto_placar

import ufc.smd.esqueleto_placar.adapters.CustomAdapter
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ufc.smd.esqueleto_placar.data.Placar
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PreviousGamesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_games)

//        // Clear the SharedPreferences data
//        // Snnipet útil para erro de seriarilazable data incompatível (quando muda no Placar)
//        val sp = getSharedPreferences("PreviousGames", Context.MODE_PRIVATE)
//        val editor = sp.edit()
//        editor.clear()
//        editor.apply()

        // Trazendo o Recycler View
        val recyclerview = findViewById<RecyclerView>(R.id.rcPreviousGames)

        // Tipo de Layout Manager será Linear
        recyclerview.layoutManager = LinearLayoutManager(this)

        // O ArrayList de Placares
        val data = readPLacarDataSharedPreferences()

        // ArrayList enviado ao Adapter
        val adapter = CustomAdapter(data)

        // Setando o Adapter no Recyclerview
        recyclerview.adapter = adapter
    }

    fun readPLacarDataSharedPreferences(): ArrayList<Placar> {
        Log.v("PDM", "Lendo o Shared Preferences")
        val data = ArrayList<Placar>()
        val sharedFileName = "PreviousGames"
        var aux: String
        var sp: SharedPreferences = getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
        if (sp != null) {
            var numMatches = sp.getInt("numberMatch", 0)
            Log.v("PDM", "numMatchs:" + numMatches)
            for (i in 1..numMatches) {
                aux = sp.getString("match" + i, "vazio")!!
                if (!aux.equals("vazio")) {

                    var bis: ByteArrayInputStream
                    bis = ByteArrayInputStream(aux.toByteArray(Charsets.ISO_8859_1))
                    var obi: ObjectInputStream
                    obi = ObjectInputStream(bis)

                    var placar: Placar = obi.readObject() as Placar
                    data.add(placar)
                    Log.v("PDM", "Placar: " + placar.nome_partida + " Pontuação:" + placar.pontos + " Res:" + placar.resultadoLongo)
                }
            }
        }
        return data
    }
}
