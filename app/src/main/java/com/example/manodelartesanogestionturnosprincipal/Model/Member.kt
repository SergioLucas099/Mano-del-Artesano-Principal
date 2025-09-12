package com.example.manodelartesanogestionturnosprincipal.Model

class Member {
    var videoUri: String? = null

    private constructor()

    constructor(videoUri: String) {
        var videoUri = videoUri
        if (videoUri.trim { it <= ' ' } == "") {
            videoUri = "No disponible"
        }
        this.videoUri = videoUri
    }
}