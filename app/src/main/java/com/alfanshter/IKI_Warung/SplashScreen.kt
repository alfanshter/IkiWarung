package com.alfanshter.IKI_Warung

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.alfanshter.IKI_Warung.auth.LoginActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_splash_screen.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class SplashScreen : AppCompatActivity() {
    lateinit var handler: Handler
    lateinit var ref : DatabaseReference
    lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        handler = Handler()
        handler.postDelayed({
            startActivity<LoginActivity>()
            finish()
        }, 3000)

        //val versionName = BuildConfig.VERSION_NAME.toFloat()
        //version.text = "Versi Aplikasi ${versionName.toString()}"
        //ref = FirebaseDatabase.getInstance().reference.child("VersiAplikasi").child("warung")
//        ref.addListenerForSingleValueEvent(object :ValueEventListener{
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val minimapk = snapshot.child("minimumversi").value.toString().toFloat()
//                if (minimapk <= versionName){
//                    handler = Handler()
//                    handler.postDelayed({
//                        startActivity<LoginActivity>()
//                        finish()
//                    }, 3000)
//                }
//                else{
//                    showHome()
//                }
//                  }
//
//        })

    }

    fun showHome() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Silahkan update dulu aplikasinya ")
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    val appPackageName =
                        packageName // getPackageName() from Context or Activity object

                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=$appPackageName")
                            )
                        )
                    } catch (anfe: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                            )
                        )
                    }
                    toast("muask")
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    showHome()
                }
                DialogInterface.BUTTON_NEUTRAL -> {
                }
            }
        }


        // Set the alert dialog positive/yes button
        builder.setPositiveButton("YES", dialogClickListener)

        // Set the alert dialog negative/no button
        builder.setNegativeButton("NO", dialogClickListener)

        // Set the alert dialog neutral/cancel button
        builder.setNeutralButton("CANCEL", dialogClickListener)


        // Initialize the AlertDialog using builder object
        dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }

}