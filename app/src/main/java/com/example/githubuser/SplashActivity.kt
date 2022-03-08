package com.example.githubuser

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(mainLooper).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1000)
        addOnFinishListener(
            onError = {
                println("Datanya tenyata cuman $it")
            },
            onLoading = {
                println("Masih loading nih")
            },
            onFinish = {
                println("Udah selesai nih datanya lengkap")
            }
        )
    }


    // procedure / method
    fun printSomething(something: String) {
        println(something)
    }

    // function (there are return value)
    fun getSomething(): String {
        return "this for you something"
    }

    fun addOnFinishListener(
        onFinish: (String) -> Unit,
        onError: (Int) -> Unit,
        onLoading: (Boolean) -> Unit,
    ) {
        var isTen = 0
        repeat(10) {
            onLoading(true)
            isTen += 1
        }
        onLoading(false)
        if (isTen == 10) onFinish("Udah sepuluh")
        else onError(isTen)
    }
}