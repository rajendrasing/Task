package com.rr.task


import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.material.button.MaterialButton
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.rr.s2sparent.application.MyApp.Companion.myRoomDb
import com.rr.task.adapter.VideoListAdapter
import com.rr.task.databinding.ActivityMainBinding
import com.rr.task.model.VideoList
import com.rr.task.model.VideoStatus
import com.rr.task.utils.FileUtils
import com.rr.task.utils.FileUtils.downloadVideo
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(),VideoListAdapter.onClickedListener {
    lateinit var binding : ActivityMainBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var adapter: VideoListAdapter
    var videoList = ArrayList<VideoList>()
    var newVideoList = ArrayList<VideoList>()
    val directory = "MyApp/MyVideos"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        initVariable()
        setUpObserver()


        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Update the live data when the text changes
                mainViewModel.setEditTextLiveData(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                // Not used
            }
        })

    }

    private fun setUpObserver() {
        mainViewModel.video.observe(this) {
            it?.let {
                adapter = VideoListAdapter(this,newVideoList,this )
                for (i in 0 until it.categories.get(0).videos.size){
                    videoList.add(VideoList(it.categories.get(0).videos.get(i).title,it.categories.get(0).videos.get(i).description,it.categories.get(0).videos.get(i).sources.get(0),it.categories.get(0).videos.get(i).thumb))
                }
                newVideoList.addAll(videoList)
                binding.apply {
                    rv.setItemAnimator(DefaultItemAnimator())
                    rv.setHasFixedSize(false)
                    rv.setAdapter(adapter)
                }
                adapter.notifyDataSetChanged()
            }
        }


        mainViewModel.getEditTextLiveData()?.observe(this){
            if (it.toString()!!.isNotEmpty()){
                newVideoList.clear()
                for (video in videoList){
                    if (video.title.lowercase().contains(it.toString().lowercase())){
                        newVideoList.add(video)
                    }
                }
                adapter.notifyDataSetChanged()
            }else{
                newVideoList.clear()
                newVideoList.addAll(videoList)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun initVariable() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.mainViewModel = mainViewModel
        binding.mainActivity = this
        loadData()
    }

    fun loadData(){
        mainViewModel.loadData(this)
    }

    override fun onItemClickedListener(itemView: View?, position: Int, list: ArrayList<VideoList>, btn: MaterialButton, playvideo:VideoView) {

        btn.setOnClickListener{

            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */

                        FileUtils.createCustomDirectory(directory,applicationContext)
                        val url = list.get(position).videourl
                        val fileName = list.get(position).title+".mp4"


                        when(btn.text.toString()){
                            "Download"->{
                                btn.isEnabled = false

                                downloadVideo(url, fileName, directory,applicationContext)

                                lifecycleScope.launch {
                                    myRoomDb?.videoDao()?.addStatus(VideoStatus(list.get(position).title,"Download"))
                                }
                            }
                            "Play"->{

                                lifecycleScope.launch{
                                    myRoomDb?.videoDao()?.addStatus(VideoStatus(list.get(position).title,"Play"))
                                }


                                val videoPath = FileUtils.createCustomDirectory(directory,applicationContext).toString()+"/"+fileName

                                var intent = Intent(Intent(this@MainActivity,PlayVideoActivity::class.java))

                                intent.putExtra("videopath",videoPath)
                                startActivity(intent)
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {

                        token.continuePermissionRequest()
                    }
                }).check()
        }
    }
}