package mx.ipn.upiiz.hlhtz.ul

import android.annotation.SuppressLint
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
        val INCORRECT = "ncorrect"
        val CORRECT = "orrect"

        if (message.message.contains(INCORRECT)) {
           // println("Mensaje incorrecto detectado: ${message.message}")

            holder.imgGif.apply {
                setImageResource(gifListIncorrect[randomGif])  // Asigna GIF de incorrecto
                visibility = View.VISIBLE
            }
            holder.icono.visibility = View.GONE  // Opcional: ocultar el ícono si es incorrecto

        } else if (message.message.contains(CORRECT)) {
           // println("Mensaje correcto detectado: ${message.message}")

            holder.imgGif.apply {
                setImageResource(gifListCorrect[randomGif])  // Asigna GIF de correcto
                visibility = View.VISIBLE
            }
            holder.icono.visibility = View.GONE  // Opcional: ocultar el ícono si es correcto

        } else {
            //println("El mensaje no contiene ninguna palabra clave")
            holder.imgGif.visibility = View.GONE  // Ocultar GIF si no hay coincidencia
            holder.icono.visibility = View.VISIBLE  // Mostrar el ícono si no hay coincidencia
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = messageList[position]

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

    fun insertMessage(message: Message) {
        this.messageList.add(message)
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