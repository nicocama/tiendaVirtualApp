package com.example.tiendavirtualapp.adaptadores

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tiendavirtualapp.Constantes
import com.example.tiendavirtualapp.R
import com.example.tiendavirtualapp.databinding.ItemProductoBinding
import com.example.tiendavirtualapp.databinding.ItemProductoCBinding
import com.example.tiendavirtualapp.filtro.FiltroProducto
import com.example.tiendavirtualapp.modelo.ModeloProducto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdaptadorProductoC : RecyclerView.Adapter<AdaptadorProductoC.HolderProducto>, Filterable{

    private lateinit var binding : ItemProductoCBinding

    private var mContext : Context
    var productosArrayList : ArrayList<ModeloProducto>
    private var filtroLista : ArrayList<ModeloProducto>
    private var filtro : FiltroProducto ?= null
    private var firebaseAuth : FirebaseAuth

    constructor(mContext: Context, productosArrayList: ArrayList<ModeloProducto>) {
        this.mContext = mContext
        this.productosArrayList = productosArrayList
        this.filtroLista = productosArrayList
        firebaseAuth = FirebaseAuth.getInstance()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProducto {
        binding = ItemProductoCBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderProducto(binding.root)
    }

    override fun getItemCount(): Int {
        return productosArrayList.size
    }

    override fun onBindViewHolder(holder: HolderProducto, position: Int) {
        val modeloProducto = productosArrayList[position]

        val nombre = modeloProducto.nombre
        val precio = modeloProducto.precio
        val precioDesc = modeloProducto.precioDesc
        val notaDesc = modeloProducto.notaDesc

        cargarPrimeraImagen(modeloProducto, holder)
        comprobarFavorito(modeloProducto, holder)

        //Evento al presionar en el imageButton
        holder.Ib_fav.setOnClickListener{
            val favorito = modeloProducto.favorito
            if (favorito){
                Constantes().eliminarProductoFav(mContext, modeloProducto.id)
            }else{
                Constantes().agregarProductoFav(mContext, modeloProducto.id)
            }
        }

        holder.item_nombre_p.text = "${nombre}"
        holder.item_precio_p.text = "${precio}${" COP"}"
        holder.item_precio_p_desc.text = "${precioDesc}"
        holder.item_nota_p.text = "${notaDesc}"

        /*Si el precio con desc. y nota no son campos vacios*/
        if(precioDesc.isNotEmpty() && notaDesc.isNotEmpty()){
            visualizarDescuento(holder)
        }

    }

    private fun comprobarFavorito(modeloProducto: ModeloProducto, holder: AdaptadorProductoC.HolderProducto) {
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!).child("Favoritos").child(modeloProducto.id)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val favorito = snapshot.exists()
                    modeloProducto.favorito = favorito

                    if (favorito){
                        holder.Ib_fav.setImageResource(R.drawable.icono_favorito)
                    }else{
                        holder.Ib_fav.setImageResource(R.drawable.icono_no_favorito)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })



    }

    private fun visualizarDescuento(holder: AdaptadorProductoC.HolderProducto) {
        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children){
                    val nota_Desc = "${ds.child("notaDesc").value}"
                    val precio_Desc = "${ds.child("precioDesc").value}"

                    if (nota_Desc.isNotEmpty() && precio_Desc.isNotEmpty()){
                        //Habilitamos las vistas
                        holder.item_nota_p.visibility = View.VISIBLE
                        holder.item_precio_p_desc.visibility = View.VISIBLE

                        //Seteamos la información
                        holder.item_nota_p.text = "${nota_Desc}"
                        holder.item_precio_p_desc.text = "${precio_Desc}${" COP"}"
                        holder.item_precio_p.paintFlags = holder.item_precio_p.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

    private fun cargarPrimeraImagen(modeloProducto: ModeloProducto, holder: AdaptadorProductoC.HolderProducto) {

        val idProducto = modeloProducto.id

        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto).child("Imagenes")
            .limitToFirst(1)
            .addValueEventListener(object : ValueEventListener {
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
        var Ib_fav = binding.IbFav
    }

    override fun getFilter(): Filter {
        if (filtro == null){
            filtro = FiltroProducto(this, filtroLista)
        }

        return filtro as FiltroProducto
    }

}