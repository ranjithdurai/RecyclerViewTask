package com.example.recyclerviewtask

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewtask.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.emplyoee_list)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        doSomethingWithInternet(this)
        val api = retrofit.init().create(ApiInterface::class.java)
        launch {
            val response = api.login()
            if (response.isSuccessful) {
                binding.recyclerView.adapter = Adapter(response.body()!!, this@MainActivity)
            } else {
                showErrorDialog()
            }
        }
    }

    private fun showErrorDialog() {

    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    // To use this function, call it with the context of your activity or fragment.
// If the internet is available, the function will return true.
// If the internet is not available, the function will return false and show a toast message to the user.
    fun doSomethingWithInternet(context: Context) {
        if (isInternetAvailable(context)) {
            // Do something that requires internet
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
        }
    }
}

object retrofit {

    fun init(): Retrofit {
        val baseUrl = "https://jsonplaceholder.typicode.com"
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }
}
