package com.example.tiendavirtualapp.cliente.Nav_Fragments_Cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tiendavirtualapp.R
import com.example.tiendavirtualapp.cliente.Bottom_Nav_Fragments_Cliente.FragmentCarritoC
import com.example.tiendavirtualapp.cliente.Bottom_Nav_Fragments_Cliente.FragmentFavoritosC
import com.example.tiendavirtualapp.cliente.Bottom_Nav_Fragments_Cliente.FragmentMisOrdenesC
import com.example.tiendavirtualapp.cliente.Bottom_Nav_Fragments_Cliente.FragmentTiendaCliente
import com.example.tiendavirtualapp.databinding.FragmentInicioCBinding

class FragmentInicioC : Fragment() {

    private lateinit var binding : FragmentInicioCBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInicioCBinding.inflate(inflater, container, false)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.op_tienda_c ->{
                    replaceFragment(FragmentTiendaCliente())
                }
                R.id.op_mis_favoritos_c ->{
                    replaceFragment(FragmentFavoritosC())
                }
                R.id.op_carrito_c ->{
                    replaceFragment(FragmentCarritoC())
                }
                R.id.op_mis_ordenes_c ->{
                    replaceFragment(FragmentMisOrdenesC())
                }
            }
            true
        }

        replaceFragment(FragmentTiendaCliente())
        binding.bottomNavigation.selectedItemId = R.id.op_tienda_c

        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment, fragment)
            .commit()
    }
}