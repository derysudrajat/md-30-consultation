package id.derysudrajat.storyapp.repo.remote.body

import com.google.gson.annotations.SerializedName

data class RegisterBody(

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("email")
    val email: String? = null
)
