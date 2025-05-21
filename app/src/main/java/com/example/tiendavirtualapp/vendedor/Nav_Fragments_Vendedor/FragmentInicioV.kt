package com.example.tiendavirtualapp.vendedor.Nav_Fragments_Vendedor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tiendavirtualapp.R
import com.example.tiendavirtualapp.databinding.FragmentInicioVBinding
import com.example.tiendavirtualapp.vendedor.Bottom_Nav_Fragments_Vendedor.FragmentMisOrdenes
import com.example.tiendavirtualapp.vendedor.Bottom_Nav_Fragments_Vendedor.FragmentMisProductos
import com.example.tiendavirtualapp.vendedor.productos.AgregarProductoActivity

class FragmentInicioV : Fragment() {

    private lateinit var binding : FragmentInicioVBinding
    private lateinit var mContext : Context

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInicioVBinding.inflate(inflater,container, false)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.op_mis_productos_v -> {
                    replaceFragment(FragmentMisProductos())
                }

                R.id.op_mis_ordenes_v -> {
                    replaceFragment(FragmentMisOrdenes())
                }
            }

            true
        }

        replaceFragment(FragmentMisProductos())
        binding.bottomNavigation.selectedItemId = R.id.op_mis_productos_v

        binding.addFab.setOnClickListener {
            startActivity(Intent(context, AgregarProductoActivity::class.java))

        }

        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment, fragment)
            .commit()

    }
}