package mx.ipn.upiiz.hlhtz.ul
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar;
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.ipn.upiiz.hlhtz.R
import mx.ipn.upiiz.hlhtz.date.Message
import mx.ipn.upiiz.hlhtz.utils.ApiHandler
import mx.ipn.upiiz.hlhtz.utils.Constants.RECEIVE_ID
import mx.ipn.upiiz.hlhtz.utils.Constants.SEND_ID
import mx.ipn.upiiz.hlhtz.utils.Time
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity (){
//Coroutine

    private lateinit var adapter: MessagingAdapter
    private lateinit var btn_send: Button
    private lateinit var et_message: EditText
    private lateinit var rv_messages: RecyclerView
    private lateinit var progressBar: ProgressBar
    //private lateinit var job:Job


    var messagesList = mutableListOf<Message>()
    //override val coroutineContext: CoroutineContext
    //   get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_messages = findViewById(R.id.rv_messages)
        btn_send = findViewById(R.id.btn_send)
        et_message = findViewById(R.id.et_message)
        progressBar = findViewById(R.id.progressBar)
        // job = Job()


        recyclerview()

        clickEvents()

        customBotMessage("¡Bienvenido! Soy Pancho, juguemos Adivina Quién, " +
                "yo te daré las pistas y tú intenta adivinar de quién estoy hablando.\n" +
                "¡A divertirnos aprendiendo de nuestra historia!")

        pistas()

    }

    private fun clickEvents() {
        //enviar mensage
        btn_send.setOnClickListener {
            sendMessage()
        }
        // al darle click en espacio de texto se desencadena un desplazamiento  para el campo de texto
        // al darle click en espacio de texto se desencadena un desplazamiento  para el campo de text
        et_message.setOnClickListener {
            GlobalScope.launch {
                delay(100)

                withContext(Dispatchers.Main) {
                    rv_messages.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }
    private fun recyclerview() {
        adapter = MessagingAdapter()
        rv_messages.adapter = adapter
        rv_messages.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                rv_messages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun sendMessage() {
        val message = et_message.text.toString()
        val timestamp = Time.timeStamp()



        if (message.isNotEmpty()){
            messagesList.add(Message(message, SEND_ID, timestamp))
            et_message.setText("")

            adapter.insertMessage(Message(message, SEND_ID, timestamp))
            rv_messages.scrollToPosition(adapter.itemCount - 1)

            botResponse(message)
        }


    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun botResponse(message: String) {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val response: String = ApiHandler.sendUserResponse(message)
                Log.i("ADIVINA","Respuesta del bot: $response")
                adaptarBotResponse(response)
            }
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun customBotMessage(message: String) {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                adaptarBotResponse(message)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun saludar() {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val response = ApiHandler.saludoDelBot()
                Log.i("ADIVINA","MENSAJE DE RESPUESTA: $response")
                adaptarBotResponse(response)
            }
        }
    }
    /*
       override fun onDestroy() {
           super.onDestroy()
           job.cancel()
       }

     @OptIn(DelicateCoroutinesApi::class)
       private fun pistas() {
           launch {
               progressBar.visibility = View.VISIBLE
               val pistas = withContext(Dispatchers.IO) {
                   ApiHandler.getPistas()
               }
               for (pista in pistas) {
                   adaptarBotResponse(pista)
                   Log.i("ADIVINA", "MENSAJE DE RESPUESTA: $pista")
               }
               progressBar.visibility = View.GONE
           }
       }*/


    @OptIn(DelicateCoroutinesApi::class)
    private fun pistas() {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                progressBar.visibility = View.VISIBLE
            }
            val pistas = ApiHandler.getPistas()
            withContext(Dispatchers.Main){
                for (pista in pistas ) {
                    adaptarBotResponse(pista)
                    Log.i("ADIVINA", "MENSAJE DE RESPUESTA: $pista")
                }
                progressBar.visibility = View.GONE
            }

        }
    }

    private fun adaptarBotResponse(response: String) {
        val timeStamp = Time.timeStamp()


        messagesList.add(Message(response, RECEIVE_ID, timeStamp))
        adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))
        rv_messages.scrollToPosition(adapter.itemCount - 1)
    }



}