package com.example.apiassesment2app.data.model

// Generic map so it works for any topic (e.g., forex) without guessing property names
data class DashboardResponse(
    val entities: List<Map<String, Any?>> = emptyList(),
    val entityTotal: Int = 0
)
