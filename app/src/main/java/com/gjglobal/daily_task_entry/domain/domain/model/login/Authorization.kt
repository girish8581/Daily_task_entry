package com.gjglobal.daily_task_entry.domain.domain.model.login

data class Authorization(
    val `data`: List<LoginData>,
    val message: String,
    val status: Int
)