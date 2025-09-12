package com.example.manodelartesanogestionturnosprincipal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.manodelartesanogestionturnosprincipal.Model.Member
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class SubirVideoFirebase : AppCompatActivity() {

    private var atrasSubirVideos: ImageView? = null

    companion object {
        const val PICK_VIDEO_REQUEST = 1
    }
    private var choosebtn: Button? = null
    private var uploadbtn: Button? = null
    private var videoView: VideoView? = null
    private var videouri: Uri? = null
    var mediaController: MediaController? = null
    private var storageReference: StorageReference? = null
    private var databaseReference: DatabaseReference? = null
    private val videoname: EditText? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subir_video_firebase)

        atrasSubirVideos = findViewById<ImageView?>(R.id.atrasSubirVideos)
        choosebtn = findViewById<Button?>(R.id.btnSeleccVideo)
        uploadbtn = findViewById<Button?>(R.id.btnSubirVideo)
        videoView = findViewById<VideoView?>(R.id.videoView)
        progressBar = findViewById<ProgressBar?>(R.id.progressBar)

        mediaController = MediaController(this)
        storageReference = FirebaseStorage.getInstance().getReference("Videos")
        databaseReference = FirebaseDatabase.getInstance().getReference("videoActual")
        videoView!!.setMediaController(mediaController)
        mediaController!!.setAnchorView(videoView)

        atrasSubirVideos!!.setOnClickListener {
            val intent: Intent = Intent(this@SubirVideoFirebase, GestionAppTV::class.java)
            startActivity(intent)
            finish()
        }
        choosebtn!!.setOnClickListener(View.OnClickListener { v: View? -> ChooseVideo() })
        uploadbtn!!.setOnClickListener(View.OnClickListener { v: View? -> UploadVideo() })

    }

    private fun ChooseVideo() {
        val intent = Intent()
        intent.setType("video/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, PICK_VIDEO_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videouri = data.getData()
            videoView!!.setVideoURI(videouri)
            videoView!!.start()
        }
    }

    private fun getfileExt(videouri: Uri): String? {
        val contentResolver = getContentResolver()
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(videouri))
    }

    private fun UploadVideo() {
        progressBar!!.setVisibility(View.VISIBLE)

        if (videouri != null) {
            val reference = storageReference!!.child(
                (System.currentTimeMillis()
                    .toString() + "." + getfileExt(videouri!!))
            )

            reference.putFile(videouri!!)
                .addOnSuccessListener(OnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                    reference.getDownloadUrl().addOnSuccessListener(OnSuccessListener { uri: Uri? ->
                        progressBar!!.setVisibility(View.INVISIBLE)
                        Toast.makeText(
                            getApplicationContext(),
                            "Video subido exitosamente",
                            Toast.LENGTH_SHORT
                        ).show()

                        val member = Member(uri.toString())
                        databaseReference!!.setValue(member)
                    })
                }
                )
                .addOnFailureListener(OnFailureListener { e: Exception? ->
                    progressBar!!.setVisibility(View.INVISIBLE)
                    Toast.makeText(
                        getApplicationContext(),
                        "Error: " + e!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                })
        } else {
            progressBar!!.setVisibility(View.INVISIBLE)
            Toast.makeText(
                getApplicationContext(),
                "No hay ningun archivo seleccionado",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}