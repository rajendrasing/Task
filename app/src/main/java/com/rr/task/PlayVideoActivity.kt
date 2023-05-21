package com.rr.task

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import androidx.media3.exoplayer.SimpleExoPlayer
import com.rr.task.databinding.ActivityMainBinding
import com.rr.task.databinding.ActivityPlayVideoBinding
import com.rr.task.utils.FileUtils

class PlayVideoActivity : AppCompatActivity() {

    lateinit var binding : ActivityPlayVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play_video)

        val videoPath = intent.extras?.getString("videopath")
        val videoUri = Uri.parse(videoPath)


        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding.playerView)
        binding.playerView.setMediaController(mediaController)

        binding.playerView.setVideoURI(videoUri)
        binding.playerView.start()

    }

}