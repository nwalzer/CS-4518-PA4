package com.example.basketballcounter

import java.util.*

//A simple data class for storing and updating points
data class BasketballTeam (var points: Int){
    val id: UUID = UUID.randomUUID()
    val teamName: String

    init {
        teamName = id.toString().substring(0, 5)
    }

    fun reset(){
        points = 0
    }

    fun increasePoints(amount: Int){
        points += amount
    }
}