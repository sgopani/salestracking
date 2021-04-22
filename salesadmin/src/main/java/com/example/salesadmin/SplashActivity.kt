package com.example.salesadmin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var image: ImageView
    private lateinit var topAnim: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation)
        image=findViewById(R.id.splash_image)
        image.animation=topAnim
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },2000)
    }
}