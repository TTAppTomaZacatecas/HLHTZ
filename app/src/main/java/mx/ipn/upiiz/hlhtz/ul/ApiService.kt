package mx.ipn.upiiz.hlhtz.ul
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/")
    fun getSaludo(): Call<String>

    @GET("/chat/{message}")
    fun getGPTResponse(@Path("message") message: String): Call<String>

    @GET ("/create-new-game-instance")
    fun getGamingInstance(): Call<Int>

    @GET("/{index_instance}/get-pistas")
    fun getPistas(@Path("index_instance") gaming_instance: Int): Call<List<String>>

    @GET("/{index_instance}/procesar-respuesta-usuario/{respuesta_user}")
    fun getUserResponseResult(@Path("index_instance") gaming_instance: Int, @Path("respuesta_user") respuesta_user: String): Call<String>

    @GET("/{index_instance}/nuevo_juego")
    fun initNewPlay(@Path("index_instance") gaming_instance: Int): Call<List<String>>

}