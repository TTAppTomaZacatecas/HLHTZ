package mx.ipn.upiiz.hlhtz.ul
import android.content.Intent
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
//Coroutine

    private lateinit var adapter: MessagingAdapter
    private lateinit var btn_send: Button
    private lateinit var et_message: EditText
    private lateinit var rv_messages: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var fab: FloatingActionButton
    private var gamingInstance = -1
    private var contadorRespuestasIncorrectas = 0
    var contador = 0
    var numMaxRespuestasIncorrectasPermitidas = 3
    var resultResponseAI = ""
    val patronCorrecto = listOf(
        "¡Correcto!, has adivinado",
        "¡Exacto!, Respuesta correcta",
        "¡Eso es!",
        "¡Perfecto!",
        "¡Así es!",
        "¡Muy bien, lo lograste!",
        "¡Justo en el blanco!",
        "¡Eso era lo que buscábamos!",
        "¡Correctísimo!",
        "¡Acertaste de lleno!",
        "¡Bingo!",
        "¡Eso está completamente correcto!",
        "¡Diste en el clavo!",
        "¡Es la respuesta exacta!",
        "¡Bravo, lo has conseguido!",
        "¡Bien hecho, es correcto!",
        "¡Sí, eso es lo que quería escuchar!",
        "¡Tienes toda la razón!",
        "¡En efecto, esa es la respuesta!",
        "¡Gran trabajo, diste en el punto exacto!"
    )

    val patronIncorrecto = listOf(
        "no",
        "error",
        "no es",
        "lo siento",
        "Incorrecto",
        "Incorrecta",
        "No es, intenta de nuevo",
        "No es correcto",
        "No, esa no es la respuesta",
        "Lo siento, pero no es correcto",
        "Esa no es la opción correcta",
        "No es la respuesta que buscábamos",
        "Sigue intentando",
        "Por ahí no va",
        "No acertaste esta vez",
        "Eso no es, prueba otra vez",
        "Estás lejos de la respuesta correcta",
        "No, intenta con otra opción",
        "No es lo que esperaba",
        "Esa no es la solución correcta",
        "Cerca, pero no es esa",
        "sigue pensando",
        "Todavía no, dale otra vuelta",
        "no va por ahí",
        "Lamentablemente, no es correcto",
        "Esa no es la respuesta adecuada",
        "No, prueba de nuevo",
        "Negativo, esa no es"
    )
    val intentaloNuevamente = listOf(
        "¡Estás cerca! Cada intento te hace mejorar. No te rindas, ¡tú puedes lograrlo!",
        "¡Gran avance! Ya estás más cerca de la solución. Inténtalo de nuevo, ¡esta vez puede ser la vencida!",
        "Tu dedicación es impresionante. ¡Sigue intentándolo, cada paso te lleva más cerca de la meta!",
        "Sé que este desafío no es fácil, pero tienes todo lo necesario para superarlo. ¡No te detengas ahora!",
        "¡Cada intento es una lección! Lo estás haciendo genial, y pronto verás los resultados."

    )
    val exedisteRespuesta = listOf(
        "Te has exedido de respuestas",
        "Suerte para la proxima",
        "¡No te rindas! La práctica te llevará a la victoria.",
        "¡Ups! Parece que has usado todas tus oportunidades por ahora.",
        "¡Se acabaron tus intentos!"
    )

    val patronCasiCorrecto = listOf(
        "Vas por el camino correcto",
        "Estás en el camino correcto",
        "¿Tienes más pistas o te atreves a dar una respuesta más específica?",
        "Cerca",
        "Estás muy cerca, sigue pensando",
        "Eso tiene sentido, pero falta un poco más",
        "Vas bien, ¿quieres ajustar un poco más la idea?",
        "No estás lejos de la respuesta correcta",
        "¡Casi lo tienes! dale otra vuelta",
        "Es un buen intento, pero necesitas afinar un poco más",
        "Estás rozando la respuesta, pero aún no llegas",
        "¡Muy buen razonamiento! solo falta un detalle",
        "Casi perfecto, estás a un paso",
        "Buena dirección, pero puedes ser más preciso",
        "¡Estás cerca! ¿qué crees que falta?",
        "Eso tiene mucho sentido, pero no es la respuesta completa",
        "Un poco más y lo consigues",
        "Tienes la idea, pero no del todo. ¿quieres intentarlo de nuevo?",
        "Interesante enfoque, pero no es exactamente lo que buscábamos. ¡casi!"
    )

    val patronRendirse = listOf(
        "Te has rendido",
        "No puedo adivinar",
        "el personaje era",
        "te diste por vencido, ¿verdad?",
        "Parece que ya no quieres intentarlo más",
        "Está bien, no siempre se puede acertar",
        "¿Lo dejamos aquí? la respuesta era",
        "No te preocupes, la solución es",
        "Veo que te rendiste, el personaje era",
        "Está bien, aquí va la respuesta",
        "Se acabaron los intentos, la respuesta correcta es",
        "No pasa nada, la respuesta era",
        "Darte por vencido está bien a veces, y la solución es",
        "¿Te rendiste? bueno, aquí está la respuesta",
        "Es normal rendirse a veces. la respuesta es",
        "No te preocupes por rendirte. la solución es",
        "Está bien no saberlo todo, la respuesta era",
        "Parece que esta vez no se pudo. la respuesta es"
    )
    //private lateinit var job:Job


    var messagesList = mutableListOf<Message>()
    //override val coroutineContext: CoroutineContext
    //   get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fab = findViewById(R.id.fab)
        setContentView(R.layout.activity_main)
        rv_messages = findViewById(R.id.rv_messages)
        btn_send = findViewById(R.id.btn_send)
        et_message = findViewById(R.id.et_message)
        progressBar = findViewById(R.id.progressBar)
        // job = Job()


        setUpRecyclerview()



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

    private fun setUpRecyclerview() {
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


    fun setContadorRespuestasIncorrectas(aiMessage: String) {
        resultResponseAI = identificarPatronResponseAI(aiMessage)
        //Log.d("PatronMatching1", "Resultado de analisis de respuesta: $resultResponseAI")
        when (resultResponseAI) {
            "CORRECTO" -> {
                contadorRespuestasIncorrectas = 0
            }
            "INCORRECTO" -> {
                contadorRespuestasIncorrectas++
            }
            "PARCIALMENTE_CORRECTA" -> {
                // Acción para respuesta parcialmente correcta
                if (contadorRespuestasIncorrectas > 0) {
                    contadorRespuestasIncorrectas--
                }
            }
            "USUARIO_RENDIDO" -> {
                // Acción para cuando el usuario se rinde
                contadorRespuestasIncorrectas = 0
            }
            "SIN_PATRON" -> {
                // Acción para respuesta sin patrón
                println("Respuesta no válida. Por favor, proporciona una respuesta válida.")
            }
            else -> {
                // Acción para respuestas no reconocidas
                println("Respuesta no válida. Por favor, proporciona una respuesta válida.")
            }
        }
        Log.d("PatronMatching1", "Contador de respuestas incorrectas: $contadorRespuestasIncorrectas")
    }


    fun identificarPatronResponseAI(responseAI: String?): String {
        // Validamos si la respuesta es nula o vacía
        if (responseAI.isNullOrBlank()) return "SIN_PATRON"
        // Convertimos la respuesta a minúsculas para comparación
        val respuestaLower = responseAI.lowercase()
        Log.d("PatronMatching1", "Comparando: $respuestaLower")
        // Verificamos cada patrón
        return when {
            patronCorrecto.any { it.lowercase() in respuestaLower } -> "CORRECTA"
            patronIncorrecto.any { it.lowercase() in respuestaLower } -> "INCORRECTA"
            patronCasiCorrecto.any { it.lowercase() in respuestaLower } -> "PARCIALMENTE_CORRECTA"
            patronRendirse.any { it.lowercase() in respuestaLower } -> "USUARIO_RENDIDO"
            else -> "SIN_PATRON"
        }
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
            setBackgroundColor(resources.getColor(android.R.color.holo_green_dark))
            setTextColor(resources.getColor(android.R.color.white))
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