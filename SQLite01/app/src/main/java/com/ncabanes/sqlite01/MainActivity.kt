package com.ncabanes.sqlite01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ncabanes.sqlite01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var amigosDBHelper: miSQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        amigosDBHelper = miSQLiteHelper(this)

        binding.btGuardar.setOnClickListener {
            if (binding.etNombre.text.isNotBlank() &&
                    binding.etEmail.text.isNotBlank()) {
                amigosDBHelper.anyadirDato(binding.etNombre.text.toString(),
                    binding.etEmail.text.toString())
                binding.etNombre.text.clear()
                binding.etEmail.text.clear()
                Toast.makeText(this, "Guardado",
                    Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "No se ha podido guardar",
                    Toast.LENGTH_LONG).show()
            }
        }




    }
}