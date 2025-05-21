package com.example.tiendavirtualapp.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendavirtualapp.databinding.ItemCategoriaVBinding
import com.example.tiendavirtualapp.modelo.ModeloCategoria
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AdaptadorCategoriaVendedor : RecyclerView.Adapter<AdaptadorCategoriaVendedor.HolderCategoriaV> {

    private lateinit var binding : ItemCategoriaVBinding

    private val mContext : Context
    private val categoriasArrayList : ArrayList<ModeloCategoria>

    constructor(mContext: Context, categoriasArrayList: ArrayList<ModeloCategoria>) {
        this.mContext = mContext
        this.categoriasArrayList = categoriasArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategoriaV {
        binding = ItemCategoriaVBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderCategoriaV(binding.root)
    }

    override fun getItemCount(): Int {
        return categoriasArrayList.size
    }

    override fun onBindViewHolder(holder: HolderCategoriaV, position: Int) {
        val modelo = categoriasArrayList[position]

        val id = modelo.id
        val categoria = modelo.categoria

        holder.item_nombre_c_v.text = categoria

        holder.item_eliminar_c.setOnClickListener{
            //Toast.makeText(mContext, "Eliminar categoría", Toast.LENGTH_SHORT).show()
            val builder = AlertDialog.Builder(mContext)
            builder.setTitle("Eliminar categoría")
            builder.setMessage("Estas seguro de que deseas eliminar esta categoría?")
                .setPositiveButton("Confirmar"){a, d->
                    eliminarCategoria(modelo, holder)

                }
                .setNegativeButton("Cancelar"){a, d->
                    a.dismiss()
                }

            builder.show()

        }

    }

    private fun eliminarCategoria(modelo: ModeloCategoria, holder: AdaptadorCategoriaVendedor.HolderCategoriaV) {
        val idCat = modelo.id
        val ref = FirebaseDatabase.getInstance().getReference("Categorias")
        ref.child(idCat).removeValue()
            .addOnSuccessListener {
                Toast.makeText(mContext, "Categoria eliminada", Toast.LENGTH_SHORT).show()
                eliminarImgCat(idCat)
                
            }
            .addOnFailureListener { e->
                Toast.makeText(mContext, "La categoría no se eliminó debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun eliminarImgCat(idCat: String){
        val nombreimg = idCat
        val rutaImagen = "Categorias/$nombreimg"
        val storageRef = FirebaseStorage.getInstance().getReference(rutaImagen)
        storageRef.delete()
            .addOnSuccessListener {
                Toast.makeText(mContext, "Se eliminó la imagen de la categoría", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { e->
                Toast.makeText(mContext, "${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    inner class HolderCategoriaV(itemView : View) : RecyclerView.ViewHolder(itemView){
        var item_nombre_c_v = binding.itemNombreCV
        var item_eliminar_c = binding.itemEliminarC
    }
}