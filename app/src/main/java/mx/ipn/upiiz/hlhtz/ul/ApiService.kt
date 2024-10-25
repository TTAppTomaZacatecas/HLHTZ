package mx.ipn.upiiz.hlhtz.ul
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/")
    fun getSaludo(): Call<String>

    @GET("/chat/{message}")
    fun getGPTResponse(@Path("message") message: String): Call<String>

    @GET ("/pistas")
    fun getPistas(): Call<List<String>>

    @GET("/respuesta/{respuesta_user}")
    fun getUserResponseResult(@Path("respuesta_user") respuesta_user: String): Call<String>

}