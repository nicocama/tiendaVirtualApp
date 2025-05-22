package com.example.tiendavirtualapp.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.tiendavirtualapp.R
import com.example.tiendavirtualapp.databinding.ItemImagenesSeleccionadasBinding
import com.example.tiendavirtualapp.modelo.ModeloImagenSeleccionada
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AdaptadorImagenSeleccionada (
    private val context : Context,
    private val imagenesSeleccionadaArrayList: ArrayList<ModeloImagenSeleccionada>,
    private val idProducto: String
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

        if (modelo.deInternet){
            try {
                val imagenUrl = modelo.imagenURL
                Glide.with(context)
                    .load(imagenUrl)
                    .placeholder(R.drawable.imagen_producto)
                    .into(holder.imagenItem)
            }catch (e:Exception){

            }
        }else{
            //Leyendo la imagen(es)
            val imagenUri = modelo.imagenUri
            try {
                Glide.with(context)
                    .load(imagenUri)
                    .placeholder(R.drawable.imagen_producto)
                    .into(holder.imagenItem)
            }catch (e:Exception){

            }
        }

        //Evento para eliminar una imagen de la lista
        holder.btn_borrar.setOnClickListener {
            if (modelo.deInternet){
                eliminarImagenFirebase(modelo, position)
            }
            imagenesSeleccionadaArrayList.remove(modelo)
            notifyDataSetChanged()
        }
    }

    private fun eliminarImagenFirebase(
        modelo: ModeloImagenSeleccionada,
        position: Int) {

        val idImagen = modelo.id

        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto).child("Imagenes").child(idImagen)
            .removeValue()
            .addOnSuccessListener {
                try {
                    imagenesSeleccionadaArrayList.remove(modelo)
                    notifyItemRemoved(position)
                    eliminarImagenStorage(modelo)
                }catch (e:Exception){
                    Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e->
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun eliminarImagenStorage(modelo: ModeloImagenSeleccionada) {
        val rutaImagen = "Productos/"+modelo.id

        val ref = FirebaseStorage.getInstance().getReference(rutaImagen)
        ref.delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Imagen eliminada",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                Toast.makeText(context, "${e.message}",Toast.LENGTH_SHORT).show()
            }

    }

    inner class HolderImagenSeleccionada(itemView: View) : ViewHolder(itemView){

        var imagenItem = binding.itemImagen
        var btn_borrar = binding.borrarItem

    }




}