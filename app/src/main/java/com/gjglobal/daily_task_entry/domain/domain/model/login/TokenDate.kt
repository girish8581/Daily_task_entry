package com.gjglobal.hms_gj.domain.domain.model.login

data class TokenDate(
    val access_token: String,
    val email: String,
    val error: Any,
    val error_description: Any,
    val expires_in: Int,
    val hospitalGroupId: Int,
    val hospitalid: Int,
    val id: Int,
    val jti: String,
    val phone: String,
    val refresh_token: String,
    val role: String,
    val roleId: Int,
    val scope: String,
    val staffid: String,
    val subcenterid: Int,
    val token_type: String,
    val username: String
)