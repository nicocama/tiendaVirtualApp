package com.example.tiendavirtualapp.cliente.ProductosC

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tiendavirtualapp.R
import com.example.tiendavirtualapp.adaptadores.AdaptadorProductoC
import com.example.tiendavirtualapp.databinding.ActivityProductosCatCactivityBinding
import com.example.tiendavirtualapp.modelo.ModeloProducto
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductosCatCActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProductosCatCactivityBinding
    private lateinit var productoArrayList: ArrayList<ModeloProducto>
    private lateinit var adaptadorProductos : AdaptadorProductoC

    private var nombreCat = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductosCatCactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Estamos obteniendo el nombre de la categoria
    nombreCat = intent.getStringExtra("nombreCat").toString()
        listarProductos(nombreCat)

        binding.etBuscarProducto.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(filtro: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    val consulta = filtro.toString()
                    adaptadorProductos.filter.filter(consulta)
                }catch (e:Exception){

                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.IbLimpiarCampo.setOnClickListener{
            val consulta = binding.etBuscarProducto.text.toString().trim()
            if (consulta.isNotEmpty()){
                binding.etBuscarProducto.setText("")
                Toast.makeText(this, "Campo limpiado",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "No se ha ingresado ninguna consulta",Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun listarProductos(nombreCat: String) {
        productoArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.orderByChild("categoria").equalTo(nombreCat)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    productoArrayList.clear()
                    for (ds in snapshot.children){
                        val modeloProducto = ds.getValue(ModeloProducto::class.java)
                        productoArrayList.add(modeloProducto!!)
                    }
                    adaptadorProductos = AdaptadorProductoC(this@ProductosCatCActivity, productoArrayList)
                    binding.productosRV.adapter = adaptadorProductos
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }


}