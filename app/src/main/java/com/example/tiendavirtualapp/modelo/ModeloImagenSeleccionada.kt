package com.example.tiendavirtualapp.modelo

import android.net.Uri

class ModeloImagenSeleccionada {

    var id = ""
    var imagenUri : Uri?= null
    var imagenURL : String ?= null
    var deInternet = false

    constructor()
    constructor(id: String, imagenUri: Uri?, imagenURL: String?, deInternet: Boolean) {
        this.id = id
        this.imagenUri = imagenUri
        this.imagenURL = imagenURL
        this.deInternet = deInternet
    }


}