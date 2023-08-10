package com.gjglobal.hms_gj.domain.domain.model.login

data class Authorization(
    val data: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)