package com.mironov.coursework

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mironov.coursework.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClickListeners()
        initialState()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setClickListeners() = with(binding) {
        messageViewGroup.setOnAddClickListener {
            val emoji = Random.nextInt(0x1F600, 0x1f650)
            val count = Random.nextInt(1, 3)
            messageViewGroup.addReaction(emoji, count, count % 2 == 1)
        }

        btAvatar.setOnClickListener {
            messageViewGroup.setAvatar(R.drawable.ic_avatar)
        }

        btName.setOnClickListener {
            val name = etName.text.toString()
            if (name.isNotBlank()) {
                messageViewGroup.setUsername(name)
            }
        }
        btMessage.setOnClickListener {
            val message = etMessage.text.toString()
            if (message.isNotBlank()) {
                messageViewGroup.setMessage(message)
            }
        }
    }

    private fun initialState() = with(binding.messageViewGroup) {
        setUsername("User Name")
        setMessage("Message")
    }
}