package id.derysudrajat.storyapp.repo.remote.response

import com.google.gson.annotations.SerializedName
import id.derysudrajat.storyapp.data.model.LoginResult

data class LoginResponse(

    @field:SerializedName("loginResult")
    val loginResultResponse: LoginResultResponse? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class LoginResultResponse(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("token")
    val token: String? = null
)

fun LoginResultResponse?.toLoginResult() = LoginResult(
    this?.name ?: "",
    this?.userId ?: "",
    this?.token ?: ""
)
