package mx.ipn.upiiz.hlhtz.utils
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

object Time {
    //declara una funcion timestamp  que devuelva una cadena de tiempo
    fun timeStamp(): String{
        //declara una variable que este en mmilisegundos
        val timeStamp = Timestamp(System.currentTimeMillis())
        //variable que este enformato de  24 horas
        val sdf = SimpleDateFormat("HH:mm")
        //formatear en formato espesificado
        val time = sdf.format(Date(timeStamp.time))
//lo devuelve en formato "HH:MM"
        return time.toString()
    }
}