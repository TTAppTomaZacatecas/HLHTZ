package mx.ipn.upiiz.hlhtz.ul

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.motion.widget.Debug
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import mx.ipn.upiiz.hlhtz.R
import mx.ipn.upiiz.hlhtz.date.Message
import mx.ipn.upiiz.hlhtz.utils.Constants.RECEIVE_ID
import mx.ipn.upiiz.hlhtz.utils.Constants.SEND_ID
import kotlin.random.Random

class MessagingAdapter : RecyclerView.Adapter<MessagingAdapter.MessageViewHolder>() {

    var messageList = mutableListOf<Message>()

    // Lista de recursos de GIFs
    private val gifList = listOf(
        R.drawable.militar_saludando_unscreen,
        R.drawable.mujer_saludo_unscreen,
        R.drawable.adelita_saludando_unscreen,
        R.drawable.hombre_saludo_unscreen
    )

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_message: TextView = itemView.findViewById(R.id.tv_message)
        val tv_bot_message: TextView = itemView.findViewById(R.id.tv_bot_message)
        val imgGif: ImageView = itemView.findViewById(R.id.gifImageView)

        // Elimina el listener que elimina los mensajes
        init {
            // itemView.setOnClickListener {
            //     messageList.removeAt(adapterPosition)
            //     notifyItemRemoved(adapterPosition)
            // }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false))
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = messageList[position]

        when (currentMessage.id) {
            SEND_ID -> { // Mensaje enviado
                holder.tv_message.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.tv_bot_message.visibility = View.GONE
                holder.imgGif.visibility = View.GONE // Ocultar el GIF en mensajes enviados
            }
            RECEIVE_ID -> { // Mensaje recibido
                holder.tv_bot_message.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.tv_message.visibility = View.GONE

                // Seleccionar y mostrar un GIF aleatorio solo para el primer mensaje
                if (position == 0) {
                    val randomGif = gifList[Random.nextInt(gifList.size)]
                    holder.imgGif.apply {
                        setImageResource(randomGif)
                        visibility = View.VISIBLE
                    }
                } else {
                    holder.imgGif.visibility = View.GONE
                }
            }
        }
    }



        /*if (position>=1){
          holder.tv_bot_message.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
               LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply{
               //gravity = Gravity.START
           }*/






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