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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import java.util.*


private const val TAG = "BasketballCounter"
private const val G_IDX = "GAME_IDX"

class BasketballCounterFragment() : Fragment() {
    private lateinit var pointLabelA: TextView
    private lateinit var pointLabelB: TextView
    private lateinit var teamLabelA: TextView
    private lateinit var teamLabelB: TextView
    private lateinit var dateLabel: TextView
    private var thisGame: Game = Game()
    private val gameDetailViewModel: GameDetailViewModel by lazy {
        ViewModelProviders.of(this).get(GameDetailViewModel::class.java)
    }
    private var dynamicLoad: Boolean = false

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
        arguments?.getString(G_IDX)?.let {
            gameDetailViewModel.loadGame(UUID.fromString(it))
            dynamicLoad = true
            Log.d(TAG, "BC fragment received UUID: $it")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "BC fragment view inflated")
        val view = inflater.inflate(R.layout.fragment_basketballteam, container, false)
        teamLabelA = view.findViewById(R.id.team_label_a)
        teamLabelB = view.findViewById(R.id.team_label_b)
        pointLabelA = view.findViewById(R.id.point_label_a)
        pointLabelB = view.findViewById(R.id.point_label_b)
        dateLabel = view.findViewById(R.id.game_label)
        updateUI()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameDetailViewModel.gameLiveData.observe(
            viewLifecycleOwner,
            Observer { game ->
                game?.let {
                    this.thisGame = game
                    updateUI()
                }
            })
        Log.d(TAG, "BC fragment liveData set")
    }

    private fun updateUI(){
        teamLabelA.text = thisGame.teamAName
        teamLabelB.text = thisGame.teamBName
        pointLabelA.text = thisGame.teamAScore.toString()
        pointLabelB.text = thisGame.teamBScore.toString()
        dateLabel.text = "${Date(thisGame.date)}"
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
            saveCurrentGame()
        }

        view?.findViewById<Button>(R.id.next_activity)?.setOnClickListener {
            showGameList()
        }
    }

    override fun onStop() {
        super.onStop()
        saveCurrentGame()
    }

    private fun reset(){
        thisGame.reset()
        updateTeamStanding(true)
        updateTeamStanding(false)
    }

    //increments the given team's points by the supplied amount, updates UI
    private fun updatePoints( points: Int, isA: Boolean){
        thisGame.increasePoints(points, isA)
        updateTeamStanding(isA)
    }

    //Changes one team's displayed points
    private fun updateTeamStanding(isA: Boolean){
        if(isA){
            pointLabelA.text = thisGame.teamAScore.toString()
        } else {
            pointLabelB.text = thisGame.teamBScore.toString()
        }
    }

    private fun showGameList(){
        Log.d(TAG, "BC Fragment display button clicked")
        callbacks?.onDisplayClick(thisGame.AisWinning())
    }

    private fun saveCurrentGame(){
        if(!dynamicLoad){
            gameDetailViewModel.addGame(thisGame)
            dynamicLoad = true
        } else {
            gameDetailViewModel.updateGame(thisGame)
        }
    }

    companion object {
        fun newInstance(id: UUID): BasketballCounterFragment {
            val args = Bundle().apply {
                putString(G_IDX, id.toString())
            }
            return BasketballCounterFragment().apply {
                arguments = args
            }
        }
    }
}
