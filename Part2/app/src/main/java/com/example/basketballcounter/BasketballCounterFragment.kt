package com.example.basketballcounter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

private const val TAG = "BasketballCounter"
private const val PTS_A = "TEAM_A_PTS"
private const val PTS_B = "TEAM_B_PTS"
private const val TN_A = "TEAM_A_NAME"
private const val TN_B = "TEAM_B_NAME"
private const val G_IDX = "GAME_IDX"

class BasketballCounterFragment() : Fragment() {
    private lateinit var pointLabelA: TextView
    private lateinit var pointLabelB: TextView
    private var teamA: BasketballTeam = BasketballTeam(0)
    private var teamB: BasketballTeam = BasketballTeam(0)
    private var gameIDX: Int = 0

    interface Callbacks {
        fun onDisplayClick(winnerA: Boolean)
    }

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
        Log.d(TAG, "BC Fragment set callbacks in onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
        Log.d(TAG, "BC Fragment removed callbacks in onDetach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "BC fragment created")

        if(savedInstanceState != null){
            teamA.points = savedInstanceState.getInt("POINTS_A")
            teamB.points = savedInstanceState.getInt("POINTS_B")
        }

        arguments?.getInt(PTS_A)?.let { teamA.increasePoints(it) }
        arguments?.getInt(PTS_B)?.let { teamB.increasePoints(it) }
        arguments?.getString(TN_A)?.let { teamA.teamName = it }
        arguments?.getString(TN_B)?.let { teamB.teamName = it }
        arguments?.getInt(G_IDX)?.let { gameIDX = it }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "BC fragment view inflated")
        val view = inflater.inflate(R.layout.fragment_basketballteam, container, false)
        val textViewA = view.findViewById<TextView>(R.id.team_label_a)
        textViewA.text = teamA.teamName
        val textViewB = view.findViewById<TextView>(R.id.team_label_b)
        textViewB.text = teamB.teamName
        pointLabelA = view.findViewById(R.id.point_label_a)
        pointLabelA.text = teamA.points.toString()
        pointLabelB = view.findViewById(R.id.point_label_b)
        pointLabelB.text = teamB.points.toString()
        val gameLabel = view.findViewById<TextView>(R.id.game_label)
        gameLabel.text = "Game #${gameIDX+1}"

        return view
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "BC fragment started")

        view?.findViewById<Button>(R.id.three_point_a)?.setOnClickListener {
            updatePoints(  3, true)
        }

        view?.findViewById<Button>(R.id.three_point_b)?.setOnClickListener {
            updatePoints(  3, false)
        }

        view?.findViewById<Button>(R.id.two_point_a)?.setOnClickListener {
            updatePoints(  2, true)
        }

        view?.findViewById<Button>(R.id.two_point_b)?.setOnClickListener {
            updatePoints(  2, false)
        }

        view?.findViewById<Button>(R.id.free_throw_a)?.setOnClickListener {
            updatePoints(  1, true)
        }

        view?.findViewById<Button>(R.id.free_throw_b)?.setOnClickListener {
            updatePoints(  1, false)
        }

        view?.findViewById<Button>(R.id.reset)?.setOnClickListener {
            reset()
        }

        view?.findViewById<Button>(R.id.save_game)?.setOnClickListener {
            //do something
        }

        view?.findViewById<Button>(R.id.next_activity)?.setOnClickListener {
            showGameList()
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)

        savedInstanceState.putInt(
            "POINTS_A",
            teamA.points
        )

        savedInstanceState.putInt(
            "POINTS_B",
            teamB.points
        )
        Log.d(TAG, "BasketballCounter saved A:${teamA.points} and B:${teamB.points} to Bundle")
    }

    fun reset(){
        teamA.reset()
        teamB.reset()
        updateTeamStanding(true)
        updateTeamStanding(false)
    }

    //increments the given team's points by the supplied amount, updates UI
    private fun updatePoints( points: Int, isA: Boolean){
        if(isA){
            teamA.increasePoints(points)
        } else {
            teamB.increasePoints(points)
        }

        updateTeamStanding(isA)
    }

    //Changes one team's displayed points
    private fun updateTeamStanding(isA: Boolean){
        if(isA){
            pointLabelA.text = teamA.points.toString()
        } else {
            pointLabelB.text = teamB.points.toString()
        }
    }

    private fun showGameList(){
        Log.d(TAG, "BC Fragment display button clicked")
        var winner : Boolean = true;
        if(teamB.points > teamA.points) {
            winner = false
        }
        callbacks?.onDisplayClick(winner)
    }

    companion object {
        fun newInstance(game: Game): BasketballCounterFragment {
            val args = Bundle().apply {
                putString(TN_A, game.teamA.teamName)
                putString(TN_B, game.teamB.teamName)
                putInt(PTS_A, game.teamA.points)
                putInt(PTS_B, game.teamB.points)
                putInt(G_IDX, game.index)
            }
            return BasketballCounterFragment().apply {
                arguments = args
            }
        }
    }
}