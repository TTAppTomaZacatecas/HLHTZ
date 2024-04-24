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
        .baseUrl("http://192.168.100.254:8001/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val scope = CoroutineScope(Dispatchers.IO)
    private val responseSaludo = Channel<String>()
    private val responsePistas = Channel<List<String>>()



    suspend fun sendMessageToGPT(message: String): String {
        val apiService = retrofit.create(ApiService::class.java)
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
        val apiService = retrofit.create(ApiService::class.java)

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


    suspend fun getPistas(): List <String> {
        val apiService = retrofit.create(ApiService::class.java)

        // Start a new coroutine to make the network call
        scope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getPistas().await()
                }
                responsePistas.send(response)
            } catch (e: Exception) {
                responsePistas.send(listOf("Error: ${e.message}"))
            }
        }

        // Wait for the response to be received on the channel
        return responsePistas.receive()
    }

}
