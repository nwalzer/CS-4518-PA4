package com.example.basketballcounter

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

private const val TAG = "BasketballCounter"

class DisplayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)

        var currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_a)
        if (currentFragment == null) {
            val fragment = GameListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container_a, fragment)
                .commit()
        }

    }

    companion object {
        fun newIntent(packageContext: Context): Intent {
            return Intent(packageContext, DisplayActivity::class.java).apply {
            }
        }
    }
}