package com.example.recyclerviewtask

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewtask.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext = Dispatchers.Main
    private lateinit var binding: ActivityMainBinding
    private val api = retrofit.init().create(ApiInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.emplyoee_list)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        doSomethingWithInternet(this)
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(this)
            .setTitle("No internet connection")
            .setMessage("Please check your internet connection and try again.")
            .setPositiveButton("Retry") { _, _ ->
                doSomethingWithInternet(this)
            }
            .setNegativeButton("Exit") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connManager.getNetworkCapabilities(connManager.activeNetwork)
        return networkCapabilities != null
    }

    fun doSomethingWithInternet(context: Context) {
        if (isInternetAvailable(context)) {
            launch {
                try {
                    val response = api.login()
                    if (response.isSuccessful) {
                        binding.recyclerView.adapter = Adapter(response.body()!!, this@MainActivity)
                    } else {
                        showErrorDialog()
                    }
                } catch (e: NoInternetException) {
                    showErrorDialog()
                }
            }
        } else {
            showErrorDialog()
        }
    }
}

object retrofit {
    fun init(): Retrofit {
        val baseUrl = "https://jsonplaceholder.typicode.com"
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

class NoInternetException(message: String) : Exception(message)
