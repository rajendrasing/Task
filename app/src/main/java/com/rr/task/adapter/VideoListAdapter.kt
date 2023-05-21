package com.rr.task.adapter

import android.content.Context
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.rr.task.databinding.RvVideoListBinding
import com.rr.task.model.VideoList
import com.rr.task.model.VideoListData
import com.rr.task.utils.FileUtils
import java.io.File

class VideoListAdapter (
    var context: Context,
    var list: ArrayList<VideoList>,
    private val listener: VideoListAdapter.onClickedListener
) : RecyclerView.Adapter<VideoListAdapter.MainView>(){

    val directory = "MyApp/MyVideos"
    val baseUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainView {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvVideoListBinding.inflate(inflater, parent, false)
        return MainView(binding)
    }

    override fun onBindViewHolder(holder: MainView, position: Int) {

        holder.binding.txttitle.text = list.get(position).title
        holder.binding.txtDes.text = list.get(position).des
        Glide.with(context).load(baseUrl+list.get(position).thumb).into(holder.binding.videoView)

        val fileName = list.get(position).title+".mp4"


        if (FileUtils.isFileExists(FileUtils.createCustomDirectory(directory,context),fileName)){
            holder.binding.btn.text = "Play"
        }else{
            holder.binding.btn.text = "Download"
        }

        listener.onItemClickedListener(holder.itemView, position,list,holder.binding.btn,holder.binding.playVideo)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MainView(var binding: RvVideoListBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    interface onClickedListener {
        fun onItemClickedListener(itemView: View?, position: Int, list : ArrayList<VideoList>, btn: MaterialButton, playVideo:VideoView)
    }

}