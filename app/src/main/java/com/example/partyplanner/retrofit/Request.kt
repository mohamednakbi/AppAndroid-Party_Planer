package com.example.partyplanner.retrofit
import com.example.partyplanner.model.Event
import com.example.partyplanner.model.User
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface Request {
    @POST("signin")
    suspend fun Login2(@Body User: User): Response<User>

    @POST("signup")
    suspend fun Signup(@Body requestBody: RequestBody): Response<ResponseBody>

    // UpdateUser Information
    @POST("UpdateUser")
    suspend fun UpdateUser(@Body user: User): Response<User>

    // UpdateUser Password
    @POST("UpdatePassword")
    suspend fun updatePassword(@Body User: User): Response<User>

    @POST("addEvent")
    suspend fun addEvent(@Body requestBody: RequestBody): Response<ResponseBody>

    // SHOW ONLY MY POSTS
    @GET("loadMyEvents")
    fun loadMyEvents(@Header("email") email: String): Call<List<Event>>

    @HTTP(method = "DELETE", path = "deleteEvent", hasBody = true)
    suspend fun deleteEvent(@Body body: Event): Response<Event>

    @POST("updateEvent")
    suspend fun updateEvent(@Body User: Event): Response<Event>
}