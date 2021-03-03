package com.example.chatapp.Modelos

class Usuarios {
    var id: String? = null
    var nomUser: String? = null
    var urlPhoto: String? = null

    constructor() {}
    constructor(id: String?, nomUser: String?, urlPhoto: String?) {
        this.id = id
        this.nomUser = nomUser
        this.urlPhoto = urlPhoto
    }

    //Getter y Setter
    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getNomUser(): String? {
        return nomUser
    }

    fun setNomUser(nomUser: String?) {
        this.nomUser = nomUser
    }

    fun getUrlPhoto(): String? {
        return urlPhoto
    }

    fun setUrlPhoto(urlPhoto: String?) {
        this.urlPhoto = urlPhoto
    }
}