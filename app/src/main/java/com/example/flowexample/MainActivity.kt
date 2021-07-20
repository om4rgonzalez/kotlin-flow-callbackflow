package com.example.flowexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.flowexample.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Log.d("_FLOW", "Antes de ejecutar el FLOW")
        runFlow()
        Log.d("_FLOW", "Despues de ejecutar el FLOW")


    }

    fun runFlow(): Unit = runBlocking {
        val f = eFlow()
            .filter{ it > 5}
            .collect { Log.d("_FLOW", it.toString()) }
    }

    fun eFlow() = flow<Int> {
        for(i in 0..10){
            delay(200)
            Log.d("_FLOW", "Ejecutando el FLOW")
            emit(i)
        }

    }

    suspend fun getDataFromFirebase(): String {
        var data: String = ""
        val f = FirebaseData()
        f.getDataFromFirebase().single()
            .takeIf { it != "" }?.let {
                data = it
            }
        return data
    }
}