package com.example.chatapp.Modelos

class Chat {
    private var emisor: String? = null
    private var receptor: String? = null
    private var mensaje: String? = null

    constructor(emisor: String?, receptor: String?, mensaje: String?) {
        this.emisor = emisor
        this.receptor = receptor
        this.mensaje = mensaje
    }

    constructor() {}

    fun getEmisor(): String? {
        return emisor
    }

    fun setEmisor(emisor: String?) {
        this.emisor = emisor
    }

    fun getReceptor(): String? {
        return receptor
    }

    fun setReceptor(receptor: String?) {
        this.receptor = receptor
    }

    fun getMensaje(): String? {
        return mensaje
    }

    fun setMensaje(mensaje: String?) {
        this.mensaje = mensaje
    }
}