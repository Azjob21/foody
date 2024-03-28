package com.example.tictactoe

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    private lateinit var bu1: Button
    private lateinit var bu2: Button
    private lateinit var bu3: Button
    private lateinit var bu4: Button
    private lateinit var bu5: Button
    private lateinit var bu6: Button
    private lateinit var bu7: Button
    private lateinit var bu8: Button
    private lateinit var bu9: Button
    private lateinit var endgameButton: Button
    private lateinit var restButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize buttons
        bu1 = findViewById(R.id.bu1)
        bu2 = findViewById(R.id.bu2)
        bu3 = findViewById(R.id.bu3)
        bu4 = findViewById(R.id.bu4)
        bu5 = findViewById(R.id.bu5)
        bu6 = findViewById(R.id.bu6)
        bu7 = findViewById(R.id.bu7)
        bu8 = findViewById(R.id.bu8)
        bu9 = findViewById(R.id.bu9)
        endgameButton = findViewById(R.id.endgamebut)
        restButton = findViewById(R.id.restbut)
        var AutoPswitch:Switch
        AutoPswitch = findViewById(R.id.AutoPswitch)
        AutoPswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Auto mode is enabled
                Toast.makeText(this, "Auto Mode Enabled", Toast.LENGTH_SHORT).show()
                // Call AutoPlayer method or any other action you want
                autoplay=true

            } else {
                // Auto mode is disabled
                Toast.makeText(this, "Auto Mode Disabled", Toast.LENGTH_SHORT).show()
                // You may want to stop the auto-player or perform any other action
                autoplay=false
            }
        }
        // Set background colors for endgameButton and restButton
        endgameButton.setBackgroundColor(Color.parseColor("#FF6B58"))
        restButton.setBackgroundColor(Color.parseColor("#e8d656"))

        // Set OnClickListener for restButton
        restButton.setOnClickListener {
            restartGame()
        }
    }
      var autoplay:Boolean=false
    fun restartGame() {
        // Reset background color and enable all buttons
        val buttons = listOf(bu1, bu2, bu3, bu4, bu5, bu6, bu7, bu8, bu9)
        for (button in buttons) {
            button.setBackgroundColor(Color.parseColor("#673AB7"))
            button.text=""
            button.isEnabled = true

        }
        // Clear player lists and set active player to 1
        player1.clear()
        player2.clear()
        activeplayer = 1
    }
    fun AutoPlayer(){
        //scanning for empty cells
        var emptycell= arrayListOf<Int>()
        for (cellid in 1..9){
            if (!(player1.contains(cellid)||player2.contains(cellid))){
                emptycell.add(cellid)
                }
        }
        //random index generation
        val r = Random
        val randomIndex =r.nextInt(emptycell.size-0)+0
        val cellid = emptycell[randomIndex]
        //button select
        val buselect:Button?
        buselect = when(cellid){
            1-> bu1
            2-> bu2
            3-> bu3
            4-> bu4
            5-> bu5
            6-> bu6
            7-> bu7
            8-> bu8
            9-> bu9
            else->{
                bu1
            }
        }
        Log.e("cellid $cellid","buselect ${buselect.text}")
        playGame(cellid,buselect)

    }

    fun buselect(view: View) {
        val buchoice = view as Button
        var cellid = 0
        when (buchoice.id) {
            R.id.bu1 -> cellid = 1
            R.id.bu2 -> cellid = 2
            R.id.bu3 -> cellid = 3
            R.id.bu4 -> cellid = 4
            R.id.bu5 -> cellid = 5
            R.id.bu6 -> cellid = 6
            R.id.bu7 -> cellid = 7
            R.id.bu8 -> cellid = 8
            R.id.bu9 -> cellid = 9
        }
        Log.d("cellid : ", cellid.toString())
        playGame(cellid, buchoice)
    }

    var player1 = arrayListOf<Int>()
    var player2 = arrayListOf<Int>()
    var activeplayer = 1

    fun playGame(cellid: Int, buchoice: Button) {
        if (!checkWinner()) {
            if (activeplayer == 1) {
                buchoice.text = "X"
                buchoice.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
                player1.add(cellid)
                activeplayer = 2

                // Disable button to prevent further user interaction
                buchoice.isEnabled = false

                // Check for winner after UI animation (if any) finishes
                GlobalScope.launch(Dispatchers.Main) {
                    delay(500) // Adjust delay as neede
                    if (!checkWinner()&&autoplay) {
                        AutoPlayer()
                        Log.e("autoplayer","autoplayerislaunching")
                    }
                }
            } else {
                buchoice.text = "O"
                buchoice.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                player2.add(cellid)
                activeplayer = 1

                // Disable button to prevent further user interaction
                buchoice.isEnabled = false

                // Check for winner after UI animation (if any) finishes
                GlobalScope.launch(Dispatchers.Main) {
                    delay(500) // Adjust delay as needed
                    checkWinner()
                }
            }

        }
    }


    fun checkWinner(): Boolean {
        val winningCombinations = listOf(
            // Rows
            listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9),
            // Columns
            listOf(1, 4, 7), listOf(2, 5, 8), listOf(3, 6, 9),
            // Diagonals
            listOf(1, 5, 9), listOf(3, 5, 7)
        )

        for (combination in winningCombinations) {
            if (combination.all { it in player1 }) {
                Toast.makeText(this, "Player 1 won the game!", Toast.LENGTH_LONG).show()
                return true
            } else if (combination.all { it in player2 }) {
                Toast.makeText(this, "Player 2 won the game!", Toast.LENGTH_LONG).show()
                return true
            }
        }

        return false
    }
}
