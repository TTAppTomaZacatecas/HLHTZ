package mx.ipn.upiiz.hlhtz.ul
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ProgressBar;
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.ipn.upiiz.hlhtz.R
import mx.ipn.upiiz.hlhtz.date.Message
import mx.ipn.upiiz.hlhtz.utils.ApiHandler
import mx.ipn.upiiz.hlhtz.utils.Constants.RESPONSE_AI_TYPE_MESSAGE
import mx.ipn.upiiz.hlhtz.utils.Constants.MESSAGE_USER_TYPE
import mx.ipn.upiiz.hlhtz.utils.Time

class MainActivity : AppCompatActivity () {


    private lateinit var adapter: MessagingAdapter
    private lateinit var btn_send: Button
    private lateinit var et_message: EditText
    private lateinit var rv_messages: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var fab: FloatingActionButton
    private var gamingInstance = -1



    var messagesList = mutableListOf<Message>()
    //override val coroutineContext: CoroutineContext
    //   get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fab = findViewById(R.id.fab)
        rv_messages = findViewById(R.id.rv_messages)
        btn_send = findViewById(R.id.btn_send)
        et_message = findViewById(R.id.et_message)
        progressBar = findViewById(R.id.progressBar)



        recyclerview()



        setGamingInstance()
        pistas()
        clickEvents()
        fab.setOnClickListener{
            showPopupMenu(it)
        }

    }

    private fun clickEvents() {
        //enviar mensage
        btn_send.setOnClickListener {
            sendMessage()
        }
        // al darle click en espacio de texto se desencadena un desplazamiento  para el campo de texto
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
        rv_messages.scrollToPosition(adapter.itemCount -1)

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
            messagesList.add(Message(message, MESSAGE_USER_TYPE, false, timestamp))
            et_message.setText("")
            adapter.insertMessage(
                Message(message, MESSAGE_USER_TYPE, false, timestamp),
                gamingInstance
            )
            rv_messages.scrollToPosition(adapter.itemCount -1)
            botResponse(message)
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun botResponse(message: String) {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val response: String = ApiHandler.sendUserResponse(gamingInstance, message)
                Log.i("ADIVINA","Respuesta del bot: $response")
                adaptarBotResponse(response, true)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun setGamingInstance() {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                gamingInstance = ApiHandler.getGamingInstance()
                Log.i("ADIVINA","Gaming instance: $gamingInstance")
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun customBotMessage(message: String) {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                adaptarBotResponse(message, false)
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
                adaptarBotResponse(response, false)
            }
        }
    }



    @OptIn(DelicateCoroutinesApi::class)
    private fun pistas() {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                progressBar.visibility = View.VISIBLE
            }
            val pistas = ApiHandler.getPistas(gamingInstance)
            withContext(Dispatchers.Main){
                adaptarBotResponse("-", false)
                for (pista in pistas ) {
                    // Verificar si el número de mensajes ya es mayor a 1 (no el primer mensaje)
                        adaptarBotResponse(pista, false)
                        Log.i("ADIVINA", "MENSAJE DE RESPUESTA: $pista")
                }
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun adaptarBotResponse(response: String, isResultAItoResponseUser: Boolean) {
        val timeStamp = Time.timeStamp()
        val message = Message(response, RESPONSE_AI_TYPE_MESSAGE, isResultAItoResponseUser, timeStamp)
        messagesList.add(message)
        //contador++
        //Log.d("PatronMatching1", "Valor del contador en MAIN: $contador")
        //setContadorRespuestasIncorrectas(response)
        adapter.insertMessage(
            message,
            gamingInstance
        )
        rv_messages.scrollToPosition(adapter.itemCount -1)
    }



    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu_emergente)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {


                R.id.menu_item_option1 -> {
                    //Se sale de la aplicacion y manda a la pantalla de inicio
                    restarApp()
                    true
                }

                R.id.menu_item_option2 -> {
                    // Mostrar TextView dinámico de menor tamaño
                    createSmallTextView()
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }
    private fun restarApp(){
        val intent = Intent(this, Start_2::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }



    private fun createSmallTextView() {
        val textContainer = findViewById<LinearLayout>(R.id.text_container)

        // Crear un nuevo TextView dinámico
        val smallTextView = TextView(this).apply {
            text =
                "¿Cómo se juega?                        El juego QUIEN HIZO QUE EN LA TOMA DE ZACATECAS consiste en que se te darán tres pistas que hacen referencia a las actividades mas representativas de un personaje histórico de la Toma de " +
                        "Zacatecas y tendrás que adivinar de quien se trata, puedes poner su nombre o su alias.                                            " +
                        "¡Buena Suerte!"
            textSize = 28f
            setBackgroundColor(Color.parseColor("#F5DEB3"))
            setTextColor(resources.getColor(android.R.color.black))
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
                setMargins(10, 10, 10, 10)
            }
        }

        // Configurar que desaparezca al tocarlo
        smallTextView.setOnClickListener {
            // Elimina el TextView del contenedor
            textContainer.removeAllViews()
            textContainer.visibility = View.GONE
        }

        // Agregar el TextView al contenedor y hacerlo visible
        textContainer.addView(smallTextView)
        textContainer.visibility = View.VISIBLE
    }



}