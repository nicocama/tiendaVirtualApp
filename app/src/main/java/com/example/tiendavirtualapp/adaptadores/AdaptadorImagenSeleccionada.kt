package com.example.tiendavirtualapp.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.tiendavirtualapp.R
import com.example.tiendavirtualapp.databinding.ItemImagenesSeleccionadasBinding
import com.example.tiendavirtualapp.modelo.ModeloImagenSeleccionada

class AdaptadorImagenSeleccionada (
    private val context : Context,
    private val imagenesSeleccionadaArrayList: ArrayList<ModeloImagenSeleccionada>
    ): Adapter<AdaptadorImagenSeleccionada.HolderImagenSeleccionada>(){

    private lateinit var binding : ItemImagenesSeleccionadasBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderImagenSeleccionada {
        binding = ItemImagenesSeleccionadasBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderImagenSeleccionada(binding.root)
    }

    override fun getItemCount(): Int {

        return imagenesSeleccionadaArrayList.size
    }

    override fun onBindViewHolder(holder: HolderImagenSeleccionada, position: Int) {
        val modelo = imagenesSeleccionadaArrayList[position]

        val imagenUri = modelo.imageUri

        //Leyendo imagenes
        try {
            Glide.with(context)
                .load(imagenUri)
                .placeholder(R.drawable.imagen_producto)/*Este apartado carga una imagen temporal*/
                .into(holder.item_imagen) /*Este apartado nos carga el diseño de la imagen que hemos creado*/

        }catch (e:Exception){

        }

        //Evento para eliminar una imagen de la lista
        holder.btn_borrar.setOnClickListener{
            imagenesSeleccionadaArrayList.remove(modelo)
            notifyDataSetChanged()
        }

    }

    inner class HolderImagenSeleccionada(itemView: View) : ViewHolder(itemView){

        var item_imagen = binding.itemImagen
        var btn_borrar = binding.borrarItem

    }




}