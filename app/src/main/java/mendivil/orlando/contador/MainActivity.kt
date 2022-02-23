package mendivil.orlando.contador

import android.content.Context
import android.content.DialogInterface
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    var soundPool = SoundPool(30, AudioManager.STREAM_MUSIC, 1)
    var contador = 0
    lateinit var tv_contador: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_contador = findViewById(R.id.contadorClicks)
        val clicker_up: ImageButton = findViewById(R.id.clicker_up)
        val clicker_down: ImageButton = findViewById(R.id.clicker_down)
        val btn_borrar: ImageButton = findViewById(R.id.reset)

        val bonk = soundPool.load(this, R.raw.bonk, 1)
        val not_bonk = soundPool.load(this, R.raw.counterdown, 1)
        val level_up = soundPool.load(this, R.raw.levelup, 1)

        clicker_up.setOnClickListener(View.OnClickListener {
            contador++
            tv_contador.setText(contador.toString())
            soundPool.play(bonk, 1F, 1F, 1, 0, 0F)

            if (contador%100 == 0 )
                soundPool.play(level_up, 1F, 1F, 1, 0, 0F)

        })

        clicker_down.setOnClickListener(View.OnClickListener {
            contador--
            tv_contador.setText(contador.toString())
            soundPool.play(not_bonk, 1F, 1F, 1, 0, 0F)
        })

        btn_borrar.setOnClickListener(View.OnClickListener {
            val alertDialog: AlertDialog? = this?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setPositiveButton("Reiniciar",
                        DialogInterface.OnClickListener { dialog, id ->
                            // User clicked OK button
                            contador = 0
                            tv_contador.setText(contador.toString())
                        })
                    setNegativeButton("Cancelar",
                        DialogInterface.OnClickListener { dialog, id ->
                            // User cancelled the dialog
                        })
                }
                // Set other dialog properties
                builder?.setMessage("¿Seguro que desea reiniciar la cuenta a 0?").setTitle("AVISO")

                // Create the AlertDialog
                builder.create()
            }
            alertDialog?.show()
        })



    }

    override fun onPause() {
        super.onPause()

        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt("contador", contador)
        editor.commit()

    }

    override fun onResume() {
        super.onResume()

        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE)
        contador = sharedPref.getInt("contador", 0)
        tv_contador.setText(contador.toString())

    }



}