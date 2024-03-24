package net.azarquiel.gammemory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity(), OnClickListener {
    private var isTapando: Boolean = false
    private var inicioTime: Long = 0
    private lateinit var ivprimera: ImageView
    private lateinit var linearv: LinearLayout
    private lateinit var random: Random
    val vpokemons = Array(809) { i -> i+1}
    val vjuego = Array(30) {0}
    var isFirst = true
    var aciertos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        inicioTime = System.currentTimeMillis()
        
        linearv = findViewById<LinearLayout>(R.id.linearv)

        random = Random(System.currentTimeMillis())
        newGame()
    }

    private fun newGame() {
        isFirst = true
        aciertos = 0
        vpokemons.shuffle(random)
        var x = 0
        for (v in 0 until 2) {
            for (p in 0 until 15) {
                vjuego[x] = vpokemons[p]
                x++
            }
        }
        vjuego.shuffle()
        var c = 0
        for (i in 0 until linearv.childCount) {
            var linearh = linearv.getChildAt(i) as LinearLayout
            for (j in 0 until linearh.childCount) {
                var ivpokemon = linearh.getChildAt(j) as ImageView
                ivpokemon.isEnabled = true
                ivpokemon.setOnClickListener(this)
                val foto = "pokemon${vjuego[c]}"
                ivpokemon.tag = vjuego[c]
                c++
                val id = resources.getIdentifier(foto, "drawable",packageName)
                ivpokemon.setBackgroundResource(id)
                ivpokemon.setImageResource(R.drawable.tapa)
            }
        }
    }

    override fun onClick(v: View?) {
        val ivpulsada = v as ImageView
        val pokemopulsado = ivpulsada.tag as Int

        if (isTapando) return
        ivpulsada.setImageResource(android.R.color.transparent)
        if (isFirst) {
            ivprimera = ivpulsada
        }
        else {
            if (ivpulsada==ivprimera) {
                tostada("No me engañes gorrión......")
                return
            }
            if (pokemopulsado == ivprimera.tag as Int) {
                aciertos++
                tostada("$aciertos aciertos")
                ivpulsada.isEnabled = false
                ivprimera.isEnabled = false
                checkGameOver()
            }
            else {
                GlobalScope.launch() {
                    isTapando=true
                    SystemClock.sleep(1000)
                    launch(Main) {
                        ivprimera.setImageResource(R.drawable.tapa)
                        ivpulsada.setImageResource(R.drawable.tapa)
                        isTapando=false
                    }
                }
            }
        }
        isFirst = !isFirst
    }

    private fun checkGameOver() {
        if (aciertos==15) {
            val finTime = System.currentTimeMillis()
            val segundos = (finTime-inicioTime) / 1000
            AlertDialog.Builder(this)
            .setTitle("Felicidades.")
            .setMessage("Lo has conseguido en $segundos segundos.")
            .setPositiveButton("New Game") { dialog, which ->
                newGame()
            }
            .setNegativeButton("Exit") { dialog, which ->
                finish()
            }
            .show()

        }
    }


    fun tostada (msg:String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}