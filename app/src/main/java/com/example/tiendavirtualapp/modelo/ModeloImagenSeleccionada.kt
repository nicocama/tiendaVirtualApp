package com.example.tiendavirtualapp.modelo

import android.net.Uri

class ModeloImagenSeleccionada {

    var id = ""
    var imageUri : Uri?= null
    var imageURL : String ?= null
    var deInternet = false

    constructor()
    constructor(id: String, imageUri: Uri?, imageURL: String?, deInternet: Boolean) {
        this.id = id
        this.imageUri = imageUri
        this.imageURL = imageURL
        this.deInternet = deInternet
    }


}