package com.example.apiassesment2app.data.model

data class DashboardResponse(
    val entities: List<Map<String, Any?>> = emptyList(),
    val entityTotal: Int = 0
)
