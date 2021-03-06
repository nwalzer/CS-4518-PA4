package com.example.basketballcounter

import android.content.Context
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
private const val WINNER = "WINNING_TEAM"

class GameListFragment : Fragment() {

    private lateinit var gameRecyclerView: RecyclerView
    private var adapter: GameAdapter? = null
    private var aWinning: Boolean? = true

    private val teamListViewModel: TeamListViewModel by lazy {
        ViewModelProviders.of(this).get(TeamListViewModel::class.java)
    }

    interface Callbacks {
        fun onGameSelected(game: Game)
    }

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
        Log.d(TAG, "GLF: Attached and Callback set")
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
        Log.d(TAG, "GLF: Detached and Callback unset")
    }

    private inner class GameHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener{
        val gameTitle: TextView = itemView.findViewById(R.id.game_title)
        val teamATitle: TextView = itemView.findViewById(R.id.team_a)
        val teamAPTS: TextView = itemView.findViewById(R.id.team_a_pts)
        val teamBTitle: TextView = itemView.findViewById(R.id.team_b)
        val teamBPTS: TextView = itemView.findViewById(R.id.team_b_pts)
        val teamLogo: ImageView = itemView.findViewById(R.id.team_picture)
        var thisGame: Game? = null;

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            thisGame?.let { callbacks?.onGameSelected(it) }
        }
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
                thisGame = game
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
        aWinning = arguments?.getBoolean(WINNER)
        Log.d(TAG, "GLF: Team A is winning - $aWinning")
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
        var crimes: MutableList<Game> = if(aWinning == null || aWinning as Boolean){
            teamListViewModel.AWinnerList
        } else {
            teamListViewModel.BWinnerList
        }
        adapter = GameAdapter(crimes)
        gameRecyclerView.adapter = adapter
    }

    companion object {
        fun newInstance(winnerA: Boolean): GameListFragment {
            val args = Bundle().apply {
                putBoolean(WINNER, winnerA)
            }
            return GameListFragment().apply {
                arguments = args
            }
        }
    }
}