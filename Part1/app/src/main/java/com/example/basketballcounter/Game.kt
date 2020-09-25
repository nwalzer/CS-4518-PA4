package com.example.basketballcounter

import java.util.*

data class Game (val index: Int){
    var r = Random()
    var teamA = BasketballTeam(r.nextInt(120))
    var teamB = BasketballTeam(r.nextInt(120))
}