package mx.ipn.upiiz.hlhtz.ul
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mx.ipn.upiiz.hlhtz.R
import android.media.MediaPlayer
    class Start_2 : AppCompatActivity() {
        private var mediaPlayer: MediaPlayer? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.activity_star2)

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
            // Reproducir audio al iniciar la actividad
            mediaPlayer = MediaPlayer.create(this, R.raw.audio1) // Cambia 'mi_audio' por el nombre de tu archivo
            mediaPlayer?.start()

            // Botón para iniciar MainActivity
            val btnInicio = findViewById<Button>(R.id.btnInicio)
            btnInicio.setOnClickListener {
                // Detener y liberar el audio
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }
        override fun onDestroy() {
            super.onDestroy()
            mediaPlayer?.release() // Liberar recursos al destruir la actividad
        }
    }
