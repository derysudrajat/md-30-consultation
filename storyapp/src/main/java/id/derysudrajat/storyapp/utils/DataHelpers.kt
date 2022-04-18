package id.derysudrajat.storyapp.utils

import id.derysudrajat.storyapp.data.model.LoginResult

object DataHelpers {

    const val authIcon =
        "https://www.kindpng.com/picc/m/269-2697881_computer-icons-user-clip-art-transparent-png-icon.png"
    val LoginResult.tokenBearer: String get() = "Bearer ${this.token}"
}