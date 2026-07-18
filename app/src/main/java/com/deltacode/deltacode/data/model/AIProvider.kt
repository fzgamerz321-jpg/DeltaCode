package com.deltacode.deltacode.data.model

/**
 * Represents an AI service configuration.
 */
data class AIProvider(
    val id: String,
    val name: String,
    val apiKey: String,
    val defaultModel: String,
    val isEnabled: Boolean = false
)
