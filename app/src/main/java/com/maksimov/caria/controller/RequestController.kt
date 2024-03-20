//package com.maksimov.caria.controller
//
//import android.util.Log
//import androidx.lifecycle.MutableLiveData
//import com.google.firebase.messaging.FirebaseMessaging
//import com.maksimov.caria.BuildConfig
//import com.maksimov.caria.data.DefaultLinks
//import com.maksimov.caria.data.Failure
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.GET
//import retrofit2.http.Query
//import kotlin.coroutines.resume
//import kotlin.coroutines.suspendCoroutine
//
//class RequestController(private val networkController: NetworkController) {
//
//    fun make(result: MutableLiveData<DefaultLinks?>, failure: MutableLiveData<Failure>) {
//
//        val logging = HttpLoggingInterceptor()
//        logging.level = HttpLoggingInterceptor.Level.BODY
//
//        val client = OkHttpClient.Builder()
//            .addInterceptor(logging)
//            .build()
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl(URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(client)
//            .build()
//
//        val service = retrofit.create(ApiService::class.java)
//
//
//        CoroutineScope(Dispatchers.Default).launch {
//
//            if (networkController.isConnected) {
//                try {
//                    val call = service.getDefaultLinks(BuildConfig.APPLICATION_ID, getUUID(failure))
//                    call.enqueue(object : Callback<DefaultLinks?> {
//                        override fun onResponse(
//                            call: Call<DefaultLinks?>,
//                            response: Response<DefaultLinks?>
//                        ) {
//                            if (response.code() == 200) {
//                                val res = response.body()
//
//                                result.value = res
//                            } else {
//                                failure.value = Failure.UnknownError
//                            }
//                        }
//
//                        override fun onFailure(call: Call<DefaultLinks?>, t: Throwable) {
//                            t.printStackTrace()
//                            failure.value = Failure.UnknownError
//                        }
//                    })
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    failure.postValue(Failure.UnknownError)
//                }
//            } else {
//                failure.postValue(Failure.NetworkConnectionError)
//            }
//
//        }
//    }
//
//    private suspend fun getUUID(failure: MutableLiveData<Failure>): String {
//        return suspendCoroutine {
//            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
//                try {
//                    Log.e("getUUID", task.result)
//                    it.resume(task.result)
//                } catch (e: Exception) {
//                    failure.value = Failure.UnknownError
//                }
//            }
//        }
//    }
//
//    companion object {
//        private const val URL = "https://86ejdwv167.execute-api.eu-central-1.amazonaws.com/"
//    }
//
//    interface ApiService {
//
//        @GET("real")
//        fun getDefaultLinks(
//            @Query("app") app: String,
//            @Query("uuid") uuid: String
//        ): Call<DefaultLinks>
//    }
//}