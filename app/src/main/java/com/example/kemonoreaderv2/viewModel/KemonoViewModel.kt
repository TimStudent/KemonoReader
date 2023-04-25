package com.example.kemonoreaderv2.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kemonoreaderv2.utils.UIState
import com.example.kemonoreaderv2.utils.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.*
import java.net.URL

class KemonoViewModel: ViewModel() {
    var link = URL("https://kemono.party/fanbox/user/49494721/post/4516154")
    var newFileName = ""
    var mp4State = false
    var jpgState = false
    var pngState = false
    var zipState = false
    private var a = 0
    var listOfLinks = mutableListOf<String>()
    private val _data: MutableLiveData<UIState> = MutableLiveData(UIState.LOADING)
    val data: LiveData<UIState> get() = _data

    fun downloadMp4FromUrl() {
        Log.d("Download", "Start")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                readMp4FromUrl(link).let {
                    it.forEach { link ->
                        val body = ApiService.providesRetrofitClient().downloadFile(link).body()
                        val directory = "/storage/emulated/0/Download/" +
                                newFileName + String.format("%02d", a) + ".mp4"
                        saveFile(body, directory)
                        a++
                    }
                }
                a = 0
            }
            catch (e:Exception) {
                println(e)
            }
        }
    }
    fun downloadJpgFromUrl() {
        Log.d("Download", "Start")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                readJpgFromUrl(link).let {
                    for (link in it) {
                        val body = ApiService.providesRetrofitClient().downloadFile(link).body()
                        val directory = "/storage/emulated/0/Download/" +
                                newFileName + String.format("%02d", a) + ".jpg"
                        saveFile(body, directory)
                        a++
                        Log.d("Download", directory)
                        Log.d("Download", a.toString())
                    }
                }
                a = 0
            }
            catch (e:Exception) {
                println(e)
            }
        }
    }
//    fun downloadJpgFromUrl() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val flowHolder: Flow<UIState> = flow {
//                emit(UIState.LOADING)
//                try {
//                    readJpgFromUrl(link).let {
//                        for (link in it) {
//                            val response = ApiService.providesRetrofitClient().downloadFile(link)
//                            if (response.isSuccessful) {
//                                response.body()?.let { body ->
//                                    emit(UIState.SUCCESS())
//                                    val directory = "/storage/emulated/0/Download/" +
//                                    newFileName + String.format("%02d", a) + ".jpg"
//                                    saveFile(body, directory)
//                                }
//                            }
//                        }
//                    }
//                }
//                catch (e: Exception) {
//                    emit(UIState.ERROR(e))
//                }
//            }
//            flowHolder.collect {
//                withContext(Dispatchers.Main) {
//                }
//                _data.postValue(it)
//            }
//        }
//    }
    fun downloadPngFromUrl() {
        Log.d("Download", "Start")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                readPngFromUrl(link).let {
                    it.forEach { link ->
                        val body = ApiService.providesRetrofitClient().downloadFile(link).body()
                        val directory = "/storage/emulated/0/Download/" +
                                newFileName + String.format("%02d", a) + ".png"
                        saveFile(body, directory)
                        a++
                        Log.d("Download", directory)
                        Log.d("Download", a.toString())
                    }
                }
                a = 0
            }
            catch (e:Exception) {
                println(e)
            }
        }
    }
    fun downloadZipFromUrl() {
        Log.d("Download", "Start")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                readZipFromUrl(link).let {
                    it.forEach { link ->
                        val body = ApiService.providesRetrofitClient().downloadFile(link).body()
                        val directory = "/storage/emulated/0/Download/" +
                                newFileName + String.format("%02d", a) + ".zip"
                        saveFile(body, directory)
                        a++
                        Log.d("Download", directory)
                        Log.d("Download", a.toString())
                    }
                }
                a = 0
            }
            catch (e:Exception) {
                println(e)
            }
        }
    }
    fun readAllFromUrl(link: URL) {
        viewModelScope.launch(Dispatchers.IO) {
            val newList = mutableListOf<String>()
            val br = BufferedReader(InputStreamReader(link.openStream())).use { it.readLines().filter { link->
                link.contains(".mp4") ||
                link.contains(".png") ||
                link.contains(".jpg") ||
                link.contains(".zip") &&
                link.contains("href=")
                !link.contains("thumbnail")
            }}
            for (url in br.filter {
                it.contains("mp4") ||
                it.contains(".png") ||
                it.contains(".jpg") ||
                it.contains(".zip") &&
                it.contains("href=") &&
                !it.contains("thumbnail")

            }) {
                if (!url.contains("download") && !url.contains("Download") && !url.contains("summary") && !url.contains("type") && !url.contains("src")) {
                    newList.add(url
                        .replace("href=","")
                        .replace("src=/","")
                        .replace("https://c1.kemono.party/","")
                        .replace("https://c2.kemono.party/","")
                        .replace("https://c3.kemono.party/","")
                        .replace("https://c4.kemono.party/","")
                        .replace("https://c5.kemono.party/","")
                        .replace("https://c6.kemono.party/","")
                        .replace("\"", "")
                        .replace(" ", "")
                    )
                }

            }
            val job1 = async {
                for (links in newList) {
                    if (links.contains(".mp4")) {
                        mp4State = true
                    }
                    if (links.contains(".jpg")) {
                        jpgState = true
                    }
                    if (links.contains(".png")) {
                        pngState = true
                    }
                    if (links.contains(".zip")) {
                        zipState = true
                    }
                }
            }
            job1.await()
            val job2 = async {
                listOfLinks.clear()
                newList.forEach {
                    listOfLinks.add("https://kemono.party/$it")
                }
            }
            job2.await()
        }
    }
}


private fun readMp4FromUrl(link: URL): List<String> {
    val br = BufferedReader(InputStreamReader(link.openStream())).use { it.readLines().filter { link->
        link.contains(".mp4")
                &&
                link.contains("href=")
    }}
    val newList = mutableListOf<String>()
    for (url in br) {
        newList.add(url
            .replace("href=","")
            .replace("https://c1.kemono.party/","")
            .replace("https://c2.kemono.party/","")
            .replace("https://c3.kemono.party/","")
            .replace("https://c4.kemono.party/","")
            .replace("https://c5.kemono.party/","")
            .replace("https://c6.kemono.party/","")
            .replace("\"", "")
        )
        Log.d("Links2",url)
    }
    return newList
}

private fun readJpgFromUrl(link: URL): List<String> {
    val br = BufferedReader(InputStreamReader(link.openStream())).use { it.readLines().filter { link->
        link.contains(".jpg")
                &&
                link.contains("href=")
    }}
    val newList = mutableListOf<String>()
    for (url in br) {
        newList.add(url
            .replace("href=","")
            .replace("https://c1.kemono.party/","")
            .replace("https://c2.kemono.party/","")
            .replace("https://c3.kemono.party/","")
            .replace("https://c4.kemono.party/","")
            .replace("https://c5.kemono.party/","")
            .replace("https://c6.kemono.party/","")
            .replace("\"", "")
        )
    }
    return newList
}

private fun readPngFromUrl(link: URL): List<String> {
    val br = BufferedReader(InputStreamReader(link.openStream())).use { it.readLines().filter { link->
        link.contains(".png")
                &&
                link.contains("href=")
    }}
    val newList = mutableListOf<String>()
    for (url in br) {
        newList.add(url
            .replace("href=","")
            .replace("https://c1.kemono.party/","")
            .replace("https://c2.kemono.party/","")
            .replace("https://c3.kemono.party/","")
            .replace("https://c4.kemono.party/","")
            .replace("https://c5.kemono.party/","")
            .replace("https://c6.kemono.party/","")
            .replace("\"", "")
        )
    }
    return newList
}

private fun readZipFromUrl(link: URL): List<String> {
    val br = BufferedReader(InputStreamReader(link.openStream())).use { it.readLines().filter { link->
        link.contains(".zip")
                &&
                link.contains("href=")
    }}
    val newList = mutableListOf<String>()
    for (url in br) {
        newList.add(url
            .replace("href=","")
            .replace("https://c1.kemono.party/","")
            .replace("https://c2.kemono.party/","")
            .replace("https://c3.kemono.party/","")
            .replace("https://c4.kemono.party/","")
            .replace("https://c5.kemono.party/","")
            .replace("https://c6.kemono.party/","")
            .replace("\"", "")
        )
    }
    return newList
}

private fun saveFile(body: ResponseBody?, saveLocation: String) {
    val save = File(saveLocation)
    body?.byteStream().use { inputStream ->
        save.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }
    }

}