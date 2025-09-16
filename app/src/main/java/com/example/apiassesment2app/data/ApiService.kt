package com.example.apiassesment2app.data

import com.example.apiassesment2app.data.model.DashboardResponse
import com.example.apiassesment2app.data.model.LoginRequest
import com.example.apiassesment2app.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    // POST /{campus}/auth where campus is footscray/sydney/br
    @POST("{campus}/auth")
    suspend fun login(
        @Path("campus") campus: String,
        @Body body: LoginRequest
    ): LoginResponse

    // GET /dashboard/{keypass}
    @GET("dashboard/{keypass}")
    suspend fun getDashboard(@Path("keypass") keypass: String): DashboardResponse
}
