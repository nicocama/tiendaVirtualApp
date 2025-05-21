package com.example.tiendavirtualapp.cliente.Bottom_Nav_Fragments_Cliente

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tiendavirtualapp.R
import com.example.tiendavirtualapp.adaptadores.AdaptadorProductoC
import com.example.tiendavirtualapp.databinding.FragmentFavoritosCBinding
import com.example.tiendavirtualapp.modelo.ModeloProducto
import com.google.firebase.auth.FirebaseAuth

class FragmentFavoritosC : Fragment() {

    private lateinit var binding: FragmentFavoritosCBinding
    private lateinit var mContext : Context
    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var productosArrayList: ArrayList<ModeloProducto>
    private lateinit var productoAdaptador : AdaptadorProductoC

    override fun onAttach(context: Context) {
        this.mContext = context
        super.onAttach(context)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavoritosCBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fireBaseAuth = FirebaseAuth.getInstance()

        cargarProductoFav()
    }

    private fun cargarProductoFav() {

    }
}