package mx.ipn.upiiz.hlhtz

import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {


    private lateinit var  btn_send: Button
    private lateinit var et_message: EditText
    private lateinit var rv_messages : RecyclerView

    var messagesList = mutableListOf<Message>()

    private val botList = listOf("Terminator", "Robocop", "BuzzLigthyear", "Han Solo")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_messages = findViewById(R.id.rv_messages)
        btn_send = findViewById(R.id.btn_send)
        et_message = findViewById(R.id.et_message)




    }
}
