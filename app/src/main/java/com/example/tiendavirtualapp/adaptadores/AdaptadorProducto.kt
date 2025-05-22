package com.example.tiendavirtualapp.adaptadores

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.tiendavirtualapp.R
import com.example.tiendavirtualapp.databinding.ItemProductoBinding
import com.example.tiendavirtualapp.modelo.ModeloProducto
import com.example.tiendavirtualapp.vendedor.MainActivityVendedor
import com.example.tiendavirtualapp.vendedor.productos.AgregarProductoActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdaptadorProducto : Adapter<AdaptadorProducto.HolderProducto>{

    private lateinit var binding : ItemProductoBinding

    private var mContext : Context
    private var productosArrayList : ArrayList<ModeloProducto>

    constructor(mContext: Context, productosArrayList: ArrayList<ModeloProducto>) {
        this.mContext = mContext
        this.productosArrayList = productosArrayList
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProducto {
        binding = ItemProductoBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderProducto(binding.root)
    }

    override fun getItemCount(): Int {
        return productosArrayList.size
    }

    override fun onBindViewHolder(holder: HolderProducto, position: Int) {
        val modeloProducto = productosArrayList[position]

        val nombre = modeloProducto.nombre


        cargarPrimeraImagen(modeloProducto, holder)
        visualizarDescuento(modeloProducto, holder)

        holder.item_nombre_p.text = "${nombre}"

        holder.Ib_editar.setOnClickListener {
            val intent = Intent(mContext, AgregarProductoActivity::class.java)
            intent.putExtra("Edicion", true)
            intent.putExtra("idProducto", modeloProducto.id)
            mContext.startActivity(intent)
        }

        holder.Ib_eliminar.setOnClickListener {
            val mAlertDialog = MaterialAlertDialogBuilder(mContext)
            mAlertDialog.setTitle("Eliminar producto")
                .setMessage("¿Estás seguro(a) de eliminar el producto?")
                .setPositiveButton("Eliminar"){dialog,which->
                    eliminarProductoBD(modeloProducto)
                }
                .setNegativeButton("Cancelar"){dialog,which->
                    dialog.dismiss()
                }
                .show()

        }


    }

    private fun eliminarProductoBD(modeloProducto: ModeloProducto) {
        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(modeloProducto.id)
            .removeValue()
            .addOnSuccessListener {
                val intent = Intent(mContext, MainActivityVendedor::class.java)
                mContext.startActivity(intent)
                Toast.makeText(mContext, "Producto eliminado", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { e->
                Toast.makeText(mContext, "${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun visualizarDescuento(modeloProducto: ModeloProducto , holder: AdaptadorProducto.HolderProducto) {
        if (!modeloProducto.precioDesc.equals("0") && !modeloProducto.notaDesc.equals("")){
            //Habilitar las vistas
            holder.item_nota_p.visibility = View.VISIBLE
            holder.item_precio_p_desc.visibility = View.VISIBLE

            //Setear la información
            holder.item_nota_p.text = "${modeloProducto.notaDesc}"
            holder.item_precio_p_desc.text = "${modeloProducto.precioDesc}${" COP"}"
            holder.item_precio_p.text = "${modeloProducto.precio}${" COP"}"
            holder.item_precio_p.paintFlags = holder.item_precio_p.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG //Tachando el precio original
        }else{
            //El producto no tiene descuento
            //Ocultar las vistas
            holder.item_nota_p.visibility = View.GONE
            holder.item_precio_p_desc.visibility = View.GONE

            //Setear la información
            holder.item_precio_p.text = "${modeloProducto.precio}${" COP"}"
            holder.item_precio_p.paintFlags = holder.item_precio_p.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv() //Quitar el tachado
        }
    }

    private fun cargarPrimeraImagen(modeloProducto: ModeloProducto, holder: AdaptadorProducto.HolderProducto) {

        val idProducto = modeloProducto.id

        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto).child("Imagenes")
            .limitToFirst(1)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children){
                        val imagenUrl = "${ds.child("imagenUrl").value}"

                        try {
                            Glide.with(mContext)
                                .load(imagenUrl)
                                .placeholder(R.drawable.item_img_producto)
                                .into(holder.imagenP)

                        }catch (e:Exception){

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }

    inner class HolderProducto(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imagenP = binding.imagenP
        var item_nombre_p = binding.itemNombreP
        var item_precio_p = binding.itemPrecioP
        var item_precio_p_desc = binding.itemPrecioPDesc
        var item_nota_p = binding.itemNotaP
        var Ib_editar = binding.IbEditar
        var Ib_eliminar = binding.IbEliminar
    }

}