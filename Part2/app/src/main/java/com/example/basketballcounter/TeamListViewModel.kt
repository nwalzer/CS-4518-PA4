package com.example.basketballcounter

import android.util.Log
import androidx.lifecycle.ViewModel

class TeamListViewModel: ViewModel() {
    var gameList = mutableListOf<Game>()
    var AWinnerList = mutableListOf<Game>()
    var BWinnerList = mutableListOf<Game>()

    init {
        for (i in 0 until 100) {
            val game = Game(i)
            gameList.add(game)
            if(game.AisWinning()){
                AWinnerList.add(game)
            } else {
                BWinnerList.add(game)
            }
        }
    }
}