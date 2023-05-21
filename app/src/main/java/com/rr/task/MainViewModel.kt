package com.rr.task

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.rr.task.model.VideoListData
import java.io.IOException
import java.io.InputStream


class MainViewModel : ViewModel() {

    private val videoLiveData = MutableLiveData<VideoListData>()

    private val editTextLiveData = MutableLiveData<String>()

    fun getEditTextLiveData(): MutableLiveData<String>? {
        return editTextLiveData
    }

    fun setEditTextLiveData(text: String) {
        editTextLiveData.value = text
    }

    val video : LiveData<VideoListData>
        get() = videoLiveData

    fun loadData(context: Context){

        var result = loadJSONFromAsset(context)
        val gson = Gson()
        val data: VideoListData = gson.fromJson(result, VideoListData::class.java)

        videoLiveData.postValue(data)
    }

    fun loadJSONFromAsset(context:Context): String? {
        var json: String? = null
        json = try {
            val `is`: InputStream = context.assets.open("data.txt")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }


}