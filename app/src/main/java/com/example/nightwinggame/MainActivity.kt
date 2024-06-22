
package com.example.nightwinggame

import GameView
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), GameTask {
    private lateinit var rootLayout: RelativeLayout
    private lateinit var startBtn: Button
    private lateinit var mGameView: GameView
    private lateinit var scoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rootLayout = findViewById(R.id.rootLayout)
        startBtn = findViewById(R.id.startBtn)
        scoreTextView = findViewById(R.id.Score)
        mGameView = GameView(this, this)

        startBtn.setOnClickListener {
            startGame()
        }
    }

    private fun startGame() {
        mGameView.setBackgroundResource(R.drawable.background_2)
        rootLayout.addView(mGameView)
        startBtn.visibility = View.GONE
        scoreTextView.visibility = View.GONE
    }

    override fun closeGame(score: Int) {
        showGameOverMessage(score)
        scoreTextView.text = "Score: $score"
        rootLayout.removeView(mGameView)
        startBtn.visibility = View.VISIBLE
        scoreTextView.visibility = View.VISIBLE
    }

    private fun showGameOverMessage(score: Int) {
        val message = "Game Over! Your score is $score"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
