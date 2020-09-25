package com.example.basketballcounter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

private const val TAG = "BasketballCounter"

class BasketballCounterFragment() : Fragment() {
    private var isA: Boolean = true
    private lateinit var pointLabel: TextView
    private var team: BasketballTeam = BasketballTeam(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "BC fragment created")

        if(savedInstanceState != null){
            team.points = savedInstanceState.getInt("POINTS")
            isA = savedInstanceState.getBoolean("TEAM_DESIG")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "BC fragment view inflated")
        val view = inflater.inflate(R.layout.fragment_basketballteam, container, false)
        if(isA){
            val textView = view.findViewById<TextView>(R.id.team_label)
            textView.text = "Team A"
        } else {
            val textView = view.findViewById<TextView>(R.id.team_label)
            textView.text = "Team B"
        }
        pointLabel = view.findViewById(R.id.point_label)
        pointLabel.text = team.points.toString()
        return view
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "BC fragment started")

        view?.findViewById<Button>(R.id.three_point)?.setOnClickListener {
            updatePoints(  3)
        }

        view?.findViewById<Button>(R.id.two_point)?.setOnClickListener {
            updatePoints(  2)
        }

        view?.findViewById<Button>(R.id.free_throw)?.setOnClickListener {
            updatePoints(  1)
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)

        savedInstanceState.putInt(
            "POINTS",
            getPoints()
        )

        savedInstanceState.putBoolean(
            "TEAM_DESIG",
            isA
        )
        Log.d(TAG, "BasketballCounter $isA saved ${getPoints()} to Bundle")
    }

    fun setIsA(value: Boolean){
        isA = value
    }

    fun reset(){
        team.reset()
        updateTeamStanding()
    }

    fun getPoints(): Int{
        return team.points
    }

    //increments the given team's points by the supplied amount, updates UI
    private fun updatePoints( points: Int){
        team.increasePoints(points)
        updateTeamStanding()
    }

    //Changes one team's displayed points
    private fun updateTeamStanding(){
        pointLabel.text = team.points.toString()
    }
}