package com.example.picassoandglide

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val url1 = "https://picsum.photos/id/237/200/300"
    val url2 = "https://picsum.photos/id/1025/200/300"
    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        picasso.setOnClickListener {
            progress_horizontal.visibility = View.VISIBLE
            Picasso.with(this)
                .load(url1)
                .into(img, object : Callback {
                    override fun onSuccess() {
                        progress_horizontal.visibility = View.GONE
                    }

                    override fun onError() {}
                })

        }
        glide.setOnClickListener {
            progress_circular.visibility = View.VISIBLE
//            Glide.with(this)
//                .load(url2)
//                .into(img)

            Glide.with(this).load(url2)
                .listener(
                    //Method 3
                    object : RequestListener<Drawable> {
                    override fun onLoadFailed( e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                       return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                            handler.sendMessage(Message().apply {
                                obj = resource
                                what = 1
                            })
//                        Method 1
//                        this@MainActivity.runOnUiThread {
//                            img.setImageDrawable(resource)
//                            progress_circular.visibility = View.GONE
//                        }
                        // Method 2
//                        Handler(Looper.getMainLooper()).post {
//                            img.setImageDrawable(resource)
//                            progress_circular.visibility = View.GONE
//                        }
                        return true
                    }
                }).submit()
        }
    }

    var handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
              1 -> changeUi(msg.obj as Drawable?)
            }
        }
    }

    fun changeUi(resource: Drawable?) {
        img.setImageDrawable(resource)
        progress_circular.visibility = View.GONE
    }
}
