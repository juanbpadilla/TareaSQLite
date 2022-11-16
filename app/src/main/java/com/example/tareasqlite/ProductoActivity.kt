package com.example.tareasqlite

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_producto.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductoActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var producto: Producto
    private lateinit var productoLiveData: LiveData<Producto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)

        database = AppDatabase.getDatabase(this)

        val idProducto = intent.getIntExtra("id", 0)

        val imageUri = ImageController.getImageUri(this, idProducto.toLong())
        imagen.setImageURI(imageUri)

        productoLiveData = database.productos().get(idProducto)

        productoLiveData.observe(this, Observer {
            producto = it

            nombre_producto.text = producto.nombre
            precio_producto.text = "$${producto.precio}"
            detalles_producto.text = producto.descripcion
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.producto_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_item -> {
                val intent = Intent(this, NuevoProductoActivity::class.java)
                intent.putExtra("producto",producto)
                startActivity(intent)
            }

            R.id.delete_item -> {
                AlertDialog.Builder(this).apply {
                    setTitle("Eliminar producto")
                    setMessage("¿Está seguro que quiere eliminar ${producto.nombre}? Esta acción es irreversible.")
                    setPositiveButton("Si") { _: DialogInterface, _: Int ->
                        productoLiveData.removeObservers(this@ProductoActivity)

                        CoroutineScope(Dispatchers.IO).launch {
                            database.productos().delete(producto)
                            this@ProductoActivity.finish()
                        }
                    }
                    setNegativeButton("No", null)
                }.show()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}