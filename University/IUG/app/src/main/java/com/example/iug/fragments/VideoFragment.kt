package com.example.iug.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.iug.databinding.FragmentVideoBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener


class VideoFragment(private val videoID: String) : BaseFragment()
{
    private lateinit var binding: FragmentVideoBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.videoPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener()
        {
            override fun onReady(youTubePlayer: YouTubePlayer)
            {
                youTubePlayer.loadVideo(videoID, 0f)
            }
        })

        lifecycle.addObserver(binding.videoPlayer)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = FragmentVideoBinding.inflate(layoutInflater)
        return binding.root
    }
}