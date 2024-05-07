package com.mironov.hw1.activity

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mironov.hw1.R
import com.mironov.hw1.adapter.ContactAdapter
import com.mironov.hw1.databinding.ActivityMainBinding
import com.mironov.hw1.model.Contact

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var contactAdapter: ContactAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initAdapter()

        val contract = ActivityResultContracts.StartActivityForResult()
        val launcher = registerForActivityResult(contract) {
            if (it.resultCode == RESULT_OK) {
                val contacts = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it?.data?.getParcelableArrayListExtra(
                        SecondActivity.EXTRA_CONTACTS,
                        Contact::class.java
                    ) ?: arrayListOf()
                } else {
                    it?.data?.getParcelableArrayListExtra(
                        SecondActivity.EXTRA_CONTACTS,
                    ) ?: arrayListOf()
                }

                showContacts(contacts)
            }
        }

        binding.btn.setOnClickListener {
            launcher.launch(SecondActivity.newIntent(this))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        contactAdapter = null
    }

    private fun showContacts(contacts: ArrayList<Contact>) {
        contactAdapter?.setItems(contacts)
    }

    private fun initAdapter() {
        contactAdapter = ContactAdapter()
        binding.contacts.adapter = contactAdapter
    }
}