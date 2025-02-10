package com.netflixclone.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.netflixclone.databinding.FragmentplayerBinding


class VideoPlayerActivity: ComponentActivity() {
    companion object {
        fun start(context: Context, videoUrl: String) {
            val intent = Intent(context, VideoPlayerActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putString(VIDEO_URL, videoUrl)
                })
            }
            context.startActivity(intent)
        }

        const val VIDEO_URL = "video_url"
    }
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videoUrl = intent?.extras?.getString(VIDEO_URL)
        setContent {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AndroidViewBinding(
                        factory = FragmentplayerBinding::inflate, modifier = Modifier.padding(innerPadding),
                        update = {
                            this.playerView.player = ExoPlayer.Builder(this.rootlayout.context).build().apply {

                                addMediaItem(
                                    MediaItem
                                        .Builder()
                                        .setUri(videoUrl)
                                        .build()
                                )
                                play()
                            }
                        })
                }
            }
    }
}