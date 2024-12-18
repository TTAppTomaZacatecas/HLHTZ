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
import mx.ipn.upiiz.hlhtz.utils.Constants.RESPONSE_AI_TYPE_MESSAGE
import mx.ipn.upiiz.hlhtz.utils.Constants.MESSAGE_USER_TYPE
import kotlin.math.log
import kotlin.random.Random
class MessagingAdapter : RecyclerView.Adapter<MessagingAdapter.MessageViewHolder>() {


    var messageList = mutableListOf<Message>()
    // varibale de random para gif e iconos
    var randomGif = Random.nextInt(4)
    var contadorRespuestasIncorrectas = 0
    var numMaxRespuestasIncorrectasPermitidas = 3
    var gamingInstance = -1
    var resultResponseAI = ""
    var contador = 0


    //insertar mensaje
    fun insertMessage(
        message: Message,
        gamingInstance: Int
    ) {
        this.messageList.add(message)
        this.gamingInstance = gamingInstance
        this.contadorRespuestasIncorrectas = contadorRespuestasIncorrectas
        this.resultResponseAI = resultResponseAI
        contador++
        //Log.d("PatronMatching1", "Valor del contador del ADAPTER: $contador")
        notifyItemInserted(messageList.size)
    }


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
        "¡Correcto!, has adivinado",
        "¡correcto!",
        "Adivinaste",
        "si",
        "¡Exacto!, Respuesta correcta",
        "¡Si!",
        "¡Has adivinado!",
        "¡Felicidades!",
        "¡Felicidades por adivinar!",
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
        "ups",
        "salido",
        "Incorrecto",
        "Incorrecta",
        "No es, intenta de nuevo",
        "No es correcto",
        "No, esa no es la respuesta",
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
        "Gracias por jugar",
        "el personaje era",
        "¡gracias por participar!",
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
        "Parece que esta vez no se pudo. la respuesta es",
        "Gracias por participar",
        "lo entiendo"
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
        Log.d("PatronMatching1", "Resultado de analisis de respuesta: $result")

        when (result) {
            "CORRECTO" -> {
                Log.d("PatronMatching1", "DEFINITIVAMENTE FUE CORRECTA")
                //Agregra gif de correcto
                holder.imgGif.apply {
                    setImageResource(gifListCorrect[randomGif])  // Asigna GIF de correcto
                    visibility = View.VISIBLE
                }
                // Opcional: ocultar el ícono si es correcto
                holder.icono.visibility = View.GONE


                contadorRespuestasIncorrectas = 0
            }
            "INCORRECTO" -> {
                Log.d("PatronMatching2", "DEFINITIVAMENTE FUE INCORRECTA")
                if (contadorRespuestasIncorrectas < numMaxRespuestasIncorrectasPermitidas) {
                    // Log.d("PatronMatching1", "AQUI DEBERIA CONTAR")
                    contadorRespuestasIncorrectas++
                    holder.imgGif.visibility = View.GONE
                    /*Log.d(
                        "PatronMatching2",
                        "Nuevo valor del contador: $contadorRespuestasIncorrectas"
                    )*/
                } else {
                    Log.d("PatronMatching3", "AQUI YA VALIO MAUSER XD")
                    holder.imgGif.apply {
                        setImageResource(gifListIncorrect[randomGif])  // Asigna GIF de incorrecto
                        visibility = View.VISIBLE
                    }
                    holder.icono.visibility =
                        View.GONE  // Opcional: ocultar el ícono si es incorrecto
                    contadorRespuestasIncorrectas = 0
                    /*Log.d(
                        "PatronMatching4",
                        "Nuevo valor del contador: $contadorRespuestasIncorrectas"
                    )*/
                }
                /* holder.tv_message.apply {
                        text = intentaloNuevamente.get(randomGif)//Asigna mensaje cuando el intento es menos de 3 intentos
                        visibility = View.VISIBLE
                    }*/
            }
            "PARCIALMENTE_CORRECTO" -> {
                // Acción para respuesta parcialmente correcta
                Log.d("PatronMaching4","aqui por poco y adivinabas")
                if (contadorRespuestasIncorrectas > 0) {
                    holder.tv_message.apply { //Asignar mensaje de exceso de respuesta
                        text = exedisteRespuesta.get(randomGif)
                        visibility = View.VISIBLE
                    }
                    holder.imgGif.apply {
                        setImageResource(gifListCorrect[randomGif])  // Asigna GIF de incorrecto
                        visibility = View.GONE
                    }

                    contadorRespuestasIncorrectas++
                }else if(contadorRespuestasIncorrectas == numMaxRespuestasIncorrectasPermitidas){//Accion cuando se pasa de 3 intentos
                    holder.imgGif.apply {
                        setImageResource(gifListIncorrect[randomGif])  // Asigna GIF de incorrecto
                        visibility = View.VISIBLE
                    }
                    holder.icono.visibility =
                        View.GONE  // Opcional: ocultar el ícono si es incorrecto
                    contadorRespuestasIncorrectas = 0// de casi correcto


                }
                else{
                    holder.tv_message.apply { //Asignar mensaje de casi correcto
                        text = patronCasiCorrecto.get(randomGif)
                        visibility = View.VISIBLE
                    }
                    holder.icono.apply {
                        setImageResource(iconList.get(randomGif)) //Asignar icono junto al mensaje
                        visibility = View.VISIBLE
                    }
                    contadorRespuestasIncorrectas --
                }
            }
            "USUARIO_RENDIDO" -> {
                // Acción para cuando el usuario se rinde
                Log.d("PatronMaching5", "el usuario se rindio")
                holder.tv_message.apply {
                    text = patronRendirse.get(randomGif)
                    visibility = View.VISIBLE
                }
                holder.imgGif.apply {
                    setImageResource(gifListIncorrect[randomGif])  // Asigna GIF de incorrecto
                    visibility = View.VISIBLE
                }
                holder.icono.visibility = View.GONE

            }
            "SIN_PATRON" -> {
                // Acción para respuesta sin patrón
                println("Respuesta no válida. Por favor, proporciona una respuesta válida.")
                holder.imgGif.visibility = View.GONE

            }
            else -> {
                // Acción para respuestas no reconocidas
                println("Respuesta no válida. Por favor, proporciona una respuesta válida.")
                holder.imgGif.apply {
                    setImageResource(gifListIncorrect[randomGif])  // Asigna GIF de incorrecto
                    visibility = View.GONE
                }

            }

        }
        Log.d("PatronMatching6", "Contador de respuestas incorrectas: $contadorRespuestasIncorrectas")
    }


    fun identificarPatronResponseAI(responseAI: String?): String {
        // Validamos si la respuesta es nula o vacía
        if (responseAI.isNullOrBlank()) return "SIN_PATRON"
        // Convertimos la respuesta a minúsculas para comparación
        val respuestaLower = responseAI.lowercase()
        Log.d("PatronMatching6", "Comparando: $respuestaLower")
        // Verificamos cada patrón
        return when {
            patronIncorrecto.any { it.lowercase() in respuestaLower } -> "INCORRECTO"
            patronCasiCorrecto.any { it.lowercase() in respuestaLower } -> "PARCIALMENTE_CORRECTO"
            patronRendirse.any { it.lowercase() in respuestaLower } -> "USUARIO_RENDIDO"
            patronCorrecto.any { it.lowercase() in respuestaLower } -> "CORRECTO"
            else -> "SIN_PATRON"
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = messageList[position]
        when (currentMessage.idTypeMessage) {
            MESSAGE_USER_TYPE -> { // Mensaje enviado
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
            RESPONSE_AI_TYPE_MESSAGE -> { // Mensaje recibido  //tipo de respuesya
                //executeActionByResponseAI(holder)
                if (currentMessage.isResultAItoResponseUser) {
                    checkMessageKeywords(holder, currentMessage)
                    Log.d("Debug", "isResultAItoResponseUser: ${currentMessage.isResultAItoResponseUser}")
                }
                holder.tv_bot_message.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.imgGif.visibility = View.GONE
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
                    holder.tv_message.visibility = View.GONE

                    // Selecciona GIF o ícono según el contenido del mensaje
                } else {
                    holder.imgGif.visibility = View.GONE
                    holder.icono.apply {
                        setImageResource(gifList.get(randomGif))
                        visibility = View.VISIBLE
                    }
                }

            }
        }
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