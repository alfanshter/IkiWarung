package com.alfanshter.IKI_Warung.Model

import com.google.gson.annotations.SerializedName

class StatusWarung {
    @SerializedName("status") private var status : Boolean? = null
    constructor(status: Boolean?) {
        this.status = status
    }
}