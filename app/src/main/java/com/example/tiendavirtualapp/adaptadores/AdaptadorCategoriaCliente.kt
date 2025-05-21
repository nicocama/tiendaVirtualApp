package com.example.tiendavirtualapp.adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tiendavirtualapp.R
import com.example.tiendavirtualapp.cliente.ProductosC.ProductosCatCActivity
import com.example.tiendavirtualapp.databinding.ItemCategoriaCBinding
import com.example.tiendavirtualapp.modelo.ModeloCategoria

class AdaptadorCategoriaCliente : RecyclerView.Adapter<AdaptadorCategoriaCliente.HolderCategoriasCliente> {

    private lateinit var binding : ItemCategoriaCBinding

    private var mContext : Context
    private var categoriasArrayList : ArrayList<ModeloCategoria>

    constructor(mContext: Context, categoriasArrayList: ArrayList<ModeloCategoria>) : super() {
        this.mContext = mContext
        this.categoriasArrayList = categoriasArrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategoriasCliente {
        binding = ItemCategoriaCBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderCategoriasCliente(binding.root)
    }

    override fun getItemCount(): Int {
        return categoriasArrayList.size
    }

    override fun onBindViewHolder(holder: HolderCategoriasCliente, position: Int) {
        val modelo = categoriasArrayList[position]

        val categoria = modelo.categoria
        val imagen = modelo.imagenUrl

        holder.itemNombreCategoria.text = categoria

        Glide.with(mContext)
            .load(imagen)
            .placeholder(R.drawable.categorias)
            .into(holder.item_img_cat)

        //Evento para ver productos de la categoria
        holder.item_ver_productos.setOnClickListener{
            val intent = Intent(mContext, ProductosCatCActivity::class.java)
            intent.putExtra("nombreCat", categoria)
            Toast.makeText(mContext, "Categoria seleccionada ${categoria}", Toast.LENGTH_SHORT).show()
            mContext.startActivity(intent)
        }
    }

    inner class HolderCategoriasCliente (itemView: View) : RecyclerView.ViewHolder(itemView){
        var itemNombreCategoria = binding.itemNombreCategoriaC
        var item_img_cat = binding.imagenCateg
        var item_ver_productos = binding.itemVerProductos
    }

}