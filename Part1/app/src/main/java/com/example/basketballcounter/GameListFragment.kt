package com.example.basketballcounter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "BasketballCounter"

class GameListFragment : Fragment() {

    private lateinit var gameRecyclerView: RecyclerView
    private var adapter: GameAdapter? = null

    private val teamListViewModel: TeamListViewModel by lazy {
        ViewModelProviders.of(this).get(TeamListViewModel::class.java)
    }

    private inner class GameHolder(view: View)
        : RecyclerView.ViewHolder(view) {
        val gameTitle: TextView = itemView.findViewById(R.id.game_title)
        val teamATitle: TextView = itemView.findViewById(R.id.team_a)
        val teamAPTS: TextView = itemView.findViewById(R.id.team_a_pts)
        val teamBTitle: TextView = itemView.findViewById(R.id.team_b)
        val teamBPTS: TextView = itemView.findViewById(R.id.team_b_pts)
        val teamLogo: ImageView = itemView.findViewById(R.id.team_picture)
    }

    private inner class GameAdapter(var games: List<Game>)
        : RecyclerView.Adapter<GameHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : GameHolder {
            val view = layoutInflater.inflate(R.layout.list_item_game, parent, false)
            return GameHolder(view)
        }
        override fun getItemCount() = games.size

        override fun onBindViewHolder(holder: GameHolder, position: Int) {
            val game = games[position]
            holder.apply {
                gameTitle.text = "Game #${game.index + 1}"
                teamATitle.text = "Team ${game.teamA.teamName}"
                teamAPTS.text = "${game.teamA.points}"
                teamBTitle.text = "Team ${game.teamB.teamName}"
                teamBPTS.text = "${game.teamB.points}"
                if(game.teamA.points >= game.teamB.points){
                    teamLogo.setImageResource(R.drawable.ic_teama);
                } else {
                    teamLogo.setImageResource(R.drawable.ic_teamb);
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "GLF: Total crimes: ${teamListViewModel.gameList.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_list, container, false)

        gameRecyclerView =
            view.findViewById(R.id.game_recycler_view) as RecyclerView
        gameRecyclerView.layoutManager = LinearLayoutManager(context)
        updateUI()
        return view
    }

    private fun updateUI() {
        val crimes = teamListViewModel.gameList
        adapter = GameAdapter(crimes)
        gameRecyclerView.adapter = adapter
    }

    companion object {
        fun newInstance(): GameListFragment {
            return GameListFragment()
        }
    }
}