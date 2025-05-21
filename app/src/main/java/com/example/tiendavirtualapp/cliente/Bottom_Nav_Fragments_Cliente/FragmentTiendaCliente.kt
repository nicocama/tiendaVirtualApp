package com.example.tiendavirtualapp.cliente.Bottom_Nav_Fragments_Cliente

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tiendavirtualapp.R
import com.example.tiendavirtualapp.adaptadores.AdaptadorCategoriaCliente
import com.example.tiendavirtualapp.databinding.FragmentTiendaClienteBinding
import com.example.tiendavirtualapp.modelo.ModeloCategoria
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentTiendaCliente : Fragment() {

    private lateinit var binding : FragmentTiendaClienteBinding
    private lateinit var mContext : Context

    private lateinit var categoriaArrayList: ArrayList<ModeloCategoria>
    private lateinit var adaptadorCategoria : AdaptadorCategoriaCliente

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTiendaClienteBinding.inflate(LayoutInflater.from(mContext), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listarCategorias()
    }

    private fun listarCategorias() {
        categoriaArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Categorias")
            .orderByChild("categorias")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoriaArrayList.clear()
                for (ds in snapshot.children){
                    val modeloCat = ds.getValue(ModeloCategoria::class.java)
                    categoriaArrayList.add(modeloCat!!)
                }

                adaptadorCategoria = AdaptadorCategoriaCliente(mContext, categoriaArrayList)
                binding.categoriasRV.adapter = adaptadorCategoria
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}