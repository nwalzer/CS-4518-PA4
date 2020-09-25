package com.example.basketballcounter

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "BasketballCounter"
private const val DIS_ACT = 0

class BasketballCounter : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "BasketballCounter instance created")

        /*var currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_a)
        if (currentFragment == null) {
            val fragment = GameListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container_a, fragment)
                .commit()
        }*/

        var currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_a)
        if (currentFragment == null) {
            val fragment =
                BasketballCounterFragment()
            fragment.setIsA(true)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container_a, fragment)
                .commit()
        }

        currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_b)
        if (currentFragment == null) {
            val fragment =
                BasketballCounterFragment()
            fragment.setIsA(false)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container_b, fragment)
                .commit()
        }

        findViewById<Button>(R.id.reset)?.setOnClickListener {
            resetPoints()
        }

        findViewById<Button>(R.id.save_game)?.setOnClickListener {
            saveGame()
        }

        findViewById<Button>(R.id.next_activity)?.setOnClickListener {
            nextActivity()
        }
    }

    //sets both team's points to 0, updates UI
    private fun resetPoints(){
        var currentFragment: BasketballCounterFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_a) as BasketballCounterFragment
        currentFragment.reset()

        currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_b) as BasketballCounterFragment
        currentFragment.reset()
    }

    private fun nextActivity(){
        val intent = DisplayActivity.newIntent(this@BasketballCounter)
        startActivityForResult(intent, DIS_ACT)
    }

    private fun saveGame(){
        //do something
    }

}