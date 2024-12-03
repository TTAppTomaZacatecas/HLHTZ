package mx.ipn.upiiz.hlhtz.ul

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import mx.ipn.upiiz.hlhtz.R
import mx.ipn.upiiz.hlhtz.date.Message
import mx.ipn.upiiz.hlhtz.utils.Constants.RECEIVE_ID
import mx.ipn.upiiz.hlhtz.utils.Constants.SEND_ID
import kotlin.random.Random
class MessagingAdapter : RecyclerView.Adapter<MessagingAdapter.MessageViewHolder>() {

    var messageList = mutableListOf<Message>()
    // varibale de random para gif e iconos
    var randomGif = Random.nextInt(4)
    var contadorRespuestasIncorrectas = 0
    var numMaxRespuestasIncorrectasPermitidas = 3
    var gamingInstance = -1

    // Lista de recursos de GIFs
    private val gifList = listOf(
        R.drawable.militar_saludando_unscreen,
        R.drawable.mujer_saludo_unscreen,
        R.drawable.adelita_saludando_unscreen,
        R.drawable.hombre_saludo_unscreen
    )
    //Lista de recursos iconos
    private  val iconList = listOf(
        R.drawable.layer_drawable_militar,
        R.drawable.layer_drawable_mujer,
        R.drawable.layer_drawable_adelita,
        R.drawable.layer_drawable_gerrillero

    )
    //lista de gif correcto
    private val gifListCorrect = listOf(
        R.drawable.militar_celebrando_unscreen,
        R.drawable.epoca_correcta_unscreen,
        R.drawable.adelita_celebrando_unscreen,
        R.drawable.gerrillero_celebrando


    )
    //Lista de incorrecto
    private val gifListIncorrect = listOf(
        R.drawable.militar_incorrecto_unscreen,
        R.drawable.epoca_incorrecta_unscreen,
        R.drawable.adelita_triste_unscreen,
        R.drawable.geriller_triste

    )
    //lista de string
    private val mensajePrincipal = listOf(
        "¡Soldados, al frente! Aquí está Crecencio,el General, un hombre que lucha por el orden y la paz en el país desde las trincheras federales.",
        "Buenas tardes, paisanos. Soy Carmen una mujer de esta tierra herida por la guerra, pero llena de esperanza. En el campo y en el hogar, lucho por un mejor porvenir para nuestras familias y por la paz que tanto anhelamos.",
        "¡Saludos, camaradas! Soy Lupita una Adelita, valiente y leal. Lucho junto a mis compañeros para forjar un México más justo, llevando en mi corazón la fuerza y la dignidad de las mujeres revolucionarias.",
        "¡Viva la lucha por la libertad! Soy Urbano un guerrillero al servicio de la causa, armado con coraje y esperanza, listo para defender la tierra y la justicia del pueblo."
    )

    val patronCorrecto = listOf(
        "¡Correcto!",
        "Has adivinado",
        "100%",
        "Respuesta correcta",
        "¡Exacto!",
        "¡Eso es!",
        "¡Perfecto!",
        "¡Así es!",
        "¡Muy bien, lo lograste!",
        "¡Lo clavaste!",
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
        "incorrecto",
        "incorrecta",
        "no es",
        "intenta de nuevo",
        "no es correcto",
        "no, esa no es la respuesta",
        "lo siento, pero no es correcto",
        "esa no es la opción correcta",
        "no es la respuesta que buscábamos",
        "sigue intentando, esa no es",
        "por ahí no va",
        "no acertaste esta vez",
        "eso no es, prueba otra vez",
        "estás lejos de la respuesta correcta",
        "no, intenta con otra opción",
        "no es lo que esperaba",
        "esa no es la solución correcta",
        "cerca, pero no es esa",
        "no, sigue pensando",
        "todavía no, dale otra vuelta",
        "no, no va por ahí",
        "lamentablemente, no es correcto",
        "esa no es la respuesta adecuada",
        "no, prueba de nuevo",
        "negativo, esa no es"
    )

    val patronCasiCorrecto = listOf(
        "vas por el camino correcto",
        "estás en el camino correcto",
        "¿Tienes más pistas o te atreves a dar una respuesta más específica?",
        "probabilidad de acierto es",
        "cerca",
        "estás muy cerca, sigue pensando",
        "eso tiene sentido, pero falta un poco más",
        "vas bien, ¿quieres ajustar un poco más la idea?",
        "no estás lejos de la respuesta correcta",
        "¡casi lo tienes! dale otra vuelta",
        "es un buen intento, pero necesitas afinar un poco más",
        "estás rozando la respuesta, pero aún no llegas",
        "¡muy buen razonamiento! solo falta un detalle",
        "casi perfecto, estás a un paso",
        "buena dirección, pero puedes ser más preciso",
        "¡estás cerca! ¿qué crees que falta?",
        "eso tiene mucho sentido, pero no es la respuesta completa",
        "un poco más y lo consigues",
        "tienes la idea, pero no del todo. ¿quieres intentarlo de nuevo?",
        "interesante enfoque, pero no es exactamente lo que buscábamos. ¡casi!"
    )

    val patronRendirse = listOf(
        "te has rendido",
        "no puedo adivinar",
        "el personaje era",
        "te diste por vencido, ¿verdad?",
        "parece que ya no quieres intentarlo más",
        "está bien, no siempre se puede acertar",
        "¿lo dejamos aquí? la respuesta era",
        "no te preocupes, la solución es",
        "veo que te rendiste, el personaje era",
        "está bien, aquí va la respuesta",
        "se acabaron los intentos, la respuesta correcta es",
        "no pasa nada, la respuesta era",
        "darte por vencido está bien a veces, y la solución es",
        "¿te rendiste? bueno, aquí está la respuesta",
        "es normal rendirse a veces. la respuesta es",
        "no te preocupes por rendirte. la solución es",
        "está bien no saberlo todo, la respuesta era",
        "parece que esta vez no se pudo. la respuesta es"
    )


    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_message: TextView = itemView.findViewById(R.id.tv_message)
        val tv_bot_message: TextView = itemView.findViewById(R.id.tv_bot_message)
        val imgGif: ImageView = itemView.findViewById(R.id.gifImageView)
        val icono: ImageView = itemView.findViewById(R.id.icono)
        //val respuesta : ImageView = itemView.findViewById(R.id.)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false))
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    //Funcion para verificar si se encuentra la palabra correcto/incorrecto
    fun checkMessageKeywords(holder: MessageViewHolder, message: Message) {
        val result = identificarPatronResponseAI(message.message)
        when (result) {
            "CORRECTA" -> {
                contadorRespuestasIncorrectas = 0
                holder.imgGif.apply {
                    setImageResource(gifListCorrect[randomGif])  // Asigna GIF de correcto
                    visibility = View.VISIBLE
                }
                holder.icono.visibility = View.GONE  // Opcional: ocultar el ícono si es correcto
            }
            "INCORRECTA" -> {
                if (contadorRespuestasIncorrectas < numMaxRespuestasIncorrectasPermitidas) {
                    contadorRespuestasIncorrectas++
                }
                else {
                    holder.imgGif.apply {
                        setImageResource(gifListIncorrect[randomGif])  // Asigna GIF de incorrecto
                        visibility = View.VISIBLE
                    }
                    holder.icono.visibility = View.GONE  // Opcional: ocultar el ícono si es incorrecto
                }
            }
            "PARCIALMENTE_CORRECTA" -> {
                // Acción para respuesta parcialmente correcta
                if (contadorRespuestasIncorrectas > 0) {
                    contadorRespuestasIncorrectas--
                }
                // Aquí puedes agregar lógica para dar pistas
            }
            "USUARIO_RENDIDO" -> {
                // Acción para cuando el usuario se rinde
                contadorRespuestasIncorrectas = 0
                // Aquí puedes agregar lógica para finalizar el juego o actividad
            }
            "SIN_PATRON" -> {
                // Acción para respuesta sin patrón

                // Aquí puedes agregar lógica para solicitar una nueva entrada
            }
            else -> {
                // Acción para respuestas no reconocidas
                println("Respuesta no válida. Por favor, proporciona una respuesta válida.")
            }
        }
    }

    fun identificarPatronResponseAI(responseAI: String?): String {
        // Validamos si la respuesta es nula o vacía
        if (responseAI.isNullOrBlank()) return "SIN_PATRON"
        // Convertimos la respuesta a minúsculas para comparación
        val respuestaLower = responseAI.lowercase()
        // Verificamos cada patrón
        return when {
            patronCorrecto.any { it.lowercase() in respuestaLower } -> "CORRECTA"
            patronIncorrecto.any { it.lowercase() in respuestaLower } -> "INCORRECTA"
            patronCasiCorrecto.any { it.lowercase() in respuestaLower } -> "PARCIALMENTE_CORRECTA"
            patronRendirse.any { it.lowercase() in respuestaLower } -> "USUARIO_RENDIDO"
            else -> "SIN_PATRON"
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = messageList[position]
        Log.d("Pista: ",currentMessage.message)

        //val  palabra = "incorrecto"

        when (currentMessage.id) {
            SEND_ID -> { // Mensaje enviado
                holder.tv_message.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.tv_bot_message.visibility = View.GONE
                holder.imgGif.layoutParams.width = 400
                holder.imgGif.layoutParams.height= 0
                holder.icono.visibility = View.GONE
               // holder.imgGif.visibility = View.GONE // Ocultar el GIF en mensajes enviados
            }
            RECEIVE_ID -> { // Mensaje recibido  //tipo de respuesya
                holder.tv_bot_message.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.tv_message.visibility = View.GONE
                //Seleccionar y mostrar un GIF aleatorio solo para el primer mensaje
                if (position == 0) {
                    holder.tv_bot_message.apply {
                        text = mensajePrincipal.get(randomGif)
                        visibility = View.VISIBLE
                    }
                    holder.imgGif.apply {
                        setImageResource(gifList.get(randomGif))
                        visibility = View.VISIBLE
                    }
                    holder.icono.visibility = View.GONE

                } else {
                    holder.imgGif.visibility = View.GONE
                    holder.icono.visibility = View.VISIBLE
                    holder.icono.apply {
                        setImageResource(iconList.get(randomGif))
                    }
                    // Selecciona GIF o ícono según el contenido del mensaje
                    checkMessageKeywords(holder, currentMessage)


                }


            }
        }
    }
//insertar mensaje
    fun insertMessage(message: Message, gamingInstance: Int) {
        this.messageList.add(message)
        this.gamingInstance = gamingInstance
        notifyItemInserted(messageList.size)
    }
}

// Añadir ItemTouchHelper para manejar los swipes y prevenir eliminación
fun attachSwipeHandler(recyclerView: RecyclerView) {
    val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, 0) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false // No permitir el movimiento de arrastre
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            // No hacer nada al hacer swipe
        }
    }

    val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
    itemTouchHelper.attachToRecyclerView(recyclerView)
}