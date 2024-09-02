package ufc.smd.esqueleto_placar.adapters

import android.content.Intent
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ufc.smd.esqueleto_placar.GameDetailsActivity
import ufc.smd.esqueleto_placar.data.Placar
import ufc.smd.esqueleto_placar.R

class CustomAdapter(private val mList: List<Placar>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    // Criação de Novos ViewHolders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // infla o card_previous_games
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_previous_game, parent, false)

        return ViewHolder(view)
    }

    // Ligando o Recycler view a um View Holder
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = ItemView.findViewById(R.id.imageview)
        val tvNomePartida: TextView = ItemView.findViewById(R.id.tvNomePartida)
        val tvResultadoJogo: TextView = ItemView.findViewById(R.id.tvResultadoJogo)
        val tvDataJogo: TextView = ItemView.findViewById(R.id.tvDataJogo)
        val tvTimeVencedor: TextView = ItemView.findViewById(R.id.tvTimeVencedor)
        val lnCell: LinearLayout = ItemView.findViewById(R.id.lnCell)
    }

    // faz o bind de uma ViewHolder a um Objeto da Lista
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val placarAnterior = mList[position]

        //alimentando os elementos a partir do objeto placar
        holder.tvNomePartida.text = placarAnterior.nome_partida
        holder.tvResultadoJogo.text = "${placarAnterior.sets[0]} - ${placarAnterior.sets[1]}"
        holder.tvDataJogo.text = placarAnterior.dataJogo
        holder.tvTimeVencedor.text = placarAnterior.timeVencedor

        // Abre os detalhes do jogo ao clicar
        holder.lnCell.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, GameDetailsActivity::class.java)

            // Passa dados pro GameDetailsActivity usando Intent extras
            intent.putExtra("nome_partida", placarAnterior.nome_partida)
            intent.putExtra("time_vencedor", placarAnterior.timeVencedor)
            intent.putExtra("time_A", placarAnterior.timeA)
            intent.putExtra("time_B", placarAnterior.timeB)
            intent.putExtra("resultado_jogo_games_A", "${placarAnterior.games[0]}")
            intent.putExtra("resultado_jogo_games_B", "${placarAnterior.games[1]}")
            intent.putExtra("resultado_jogo_sets_A", "${placarAnterior.sets[0]}")
            intent.putExtra("resultado_jogo_sets_B", "${placarAnterior.sets[1]}")
            intent.putExtra("team1_player1",  "${placarAnterior.nomeJogadores[0].first}")
            intent.putExtra("team1_player2",  "${placarAnterior.nomeJogadores[0].second}")
            intent.putExtra("team2_player1",  "${placarAnterior.nomeJogadores[1].first}")
            intent.putExtra("team2_player2",  "${placarAnterior.nomeJogadores[1].second}")

            ContextCompat.startActivity(context, intent, null)
        }
    }

    // retorna o número dos itens na lista
    override fun getItemCount(): Int {
        return mList.size
    }
}
