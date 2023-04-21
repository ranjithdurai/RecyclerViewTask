package com.example.recyclerviewtask



import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {
    @GET("/users")
    suspend fun login(): Response<List<Responces>>
    @GET("/users/{id}")
    suspend fun  details(@Path("id") id: Int?):Response<Responces>
}