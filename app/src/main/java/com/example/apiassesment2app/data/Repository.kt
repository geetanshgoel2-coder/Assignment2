package com.example.apiassesment2app.data

import com.example.apiassesment2app.data.model.DashboardResponse
import com.example.apiassesment2app.data.model.LoginRequest
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: ApiService
) {
    suspend fun login(campus: String, username: String, password: String) =
        api.login(campus, LoginRequest(username, password))

    suspend fun dashboard(keypass: String): DashboardResponse =
        api.getDashboard(keypass)
}
