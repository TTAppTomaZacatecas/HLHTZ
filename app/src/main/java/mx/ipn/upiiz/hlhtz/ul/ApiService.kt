package mx.ipn.upiiz.hlhtz.ul
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/")
    fun getSaludo(): Call<String>

    @GET("/chat/{message}")
    fun getGPTResponse(@Path("message") message: String): Call<String>

}