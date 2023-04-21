package com.example.recyclerviewtask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.recyclerviewtask.databinding.ActivityDetailsBinding
import com.example.recyclerviewtask.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailsActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.emplyoee_details)
        }
        val id = intent.getIntExtra("id", 0)
        Log.e("RANJITH", "onCreate: ${id}", )
        val api = retrofit.init().create(ApiInterface::class.java)
        launch {
            val response = api.details(id = id)
            if (response.isSuccessful) {
                binding.tvEidData.text = response.body()?.id.toString()
                binding.tvNameData.text=response.body()?.name?.uppercase()
                binding.tvEmailData.text=response.body()?.email?.toLowerCase()
                val address = response.body()?.address
                val addressText = "${address?.street}\n${address?.city}\n${address?.suite}\n${address?.zipcode}"
                binding.tvAddressData.text = addressText
                binding.tvPhoneNumberData.text=response.body()?.phone
                binding.tvCompanyNameData.text=response.body()?.company?.name
                binding.tvWebSiteData.text=response.body()?.website
            } else {

            }
        }
    }
}