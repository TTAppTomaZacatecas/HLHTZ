package mx.ipn.upiiz.hlhtz.utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.ipn.upiiz.hlhtz.ul.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.await

object ApiHandler {


    val retrofit = Retrofit.Builder()
        .baseUrl("https://hlhtz-api-1.onrender.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val scope = CoroutineScope(Dispatchers.IO)
    private val responseSaludo = Channel<String>()
    private val responsePistas = Channel<List<String>>()
    private val responseResultProcessResponseUser = Channel<String>()
    private val responseGamingInstance = Channel<Int>()
    private val responseNewGame = Channel<List<String>>()

    private val apiService = retrofit.create(ApiService::class.java)



    suspend fun sendMessageToGPT(message: String): String {
        // Start a new coroutine to make the network call
        scope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getGPTResponse(message).await()
                }
                responseSaludo.send(response)
            } catch (e: Exception) {
                responseSaludo.send("Error: ${e.message}")
            }
        }
        // Wait for the response to be received on the channel
        return responseSaludo.receive()
    }

    suspend fun saludoDelBot(): String {
        // Start a new coroutine to make the network call
        scope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getSaludo().await()
                }
                responseSaludo.send(response)
            } catch (e: Exception) {
                responseSaludo.send("Error: ${e.message}")
            }
        }

        // Wait for the response to be received on the channel
        return responseSaludo.receive()
    }


    suspend fun getGamingInstance(): Int {
        // Start a new coroutine to make the network call
        scope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getGamingInstance().await()
                }
                responseGamingInstance.send(response)
            } catch (e: Exception) {
                responseGamingInstance.send(0)
            }
        }
        // Wait for the response to be received on the channel
        return responseGamingInstance.receive()
    }


    suspend fun getPistas(gamingInstance: Int): List <String> {
        // Start a new coroutine to make the network call
        scope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getPistas(gamingInstance).await()
                }
                responsePistas.send(response)
            } catch (e: Exception) {
                responsePistas.send(listOf("Error: ${e.message}"))
            }
        }

        // Wait for the response to be received on the channel
        return responsePistas.receive()
    }

    suspend fun sendUserResponse(gamingInstance: Int, message: String): String {
        // Start a new coroutine to make the network call
        scope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getUserResponseResult(gamingInstance, message).await()
                }
                responseResultProcessResponseUser.send(response)
            } catch (e: Exception) {
                responseResultProcessResponseUser.send("Error: ${e.message}")
            }
        }
        // Wait for the response to be received on the channel
        return responseResultProcessResponseUser.receive()
    }


    suspend fun initNewPlay(gamingInstance: Int): List <String> {
        // Start a new coroutine to make the network call
        scope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.initNewPlay(gamingInstance).await()
                }
                responseNewGame.send(response)
            } catch (e: Exception) {
                responseNewGame.send(listOf("Error: ${e.message}"))
            }
        }
        // Wait for the response to be received on the channel
        return responseNewGame.receive()
    }

}