package com.example.kemonoreaderv2

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kemonoreaderv2.adapter.RecyclerViewAdapter
import com.example.kemonoreaderv2.databinding.FragmentFirstBinding
import com.example.kemonoreaderv2.utils.UIState
import com.example.kemonoreaderv2.viewModel.KemonoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.lang.Exception
import java.net.URL

class FirstFragment : Fragment() {

    private val binding by lazy {
        FragmentFirstBinding.inflate(layoutInflater)
    }

    private val mKemonoViewModel by lazy {
        ViewModelProvider(this)[KemonoViewModel::class.java]
    }

    private val adapter by lazy {
        RecyclerViewAdapter {

        }
    }

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.downloadMp4.visibility = View.INVISIBLE
        binding.downloadPng.visibility = View.INVISIBLE
        binding.downloadZip.visibility = View.INVISIBLE
        binding.downloadJpg.visibility = View.INVISIBLE
        binding.RefreshButton.visibility = View.INVISIBLE
        mKemonoViewModel.data.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.LOADING -> {
                    binding.Loading.visibility = View.VISIBLE
                }
                is UIState.SUCCESS -> {
                    binding.Loading.visibility = View.INVISIBLE
                    state.success?.let { adapter.update(it) }
                }
                is UIState.ERROR -> {
                    binding.Loading.visibility = View.VISIBLE
                    AlertDialog.Builder(requireActivity())
                        .setTitle("Error Loading")
                        .setMessage(state.error.localizedMessage)
                        .setNegativeButton("DISMISS") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setPositiveButton("Retry") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewLifecycleOwner.lifecycleScope.launch {
            binding.Setbutton.setOnClickListener {
                try {
                    mKemonoViewModel.link = URL(binding.linkInput.text.toString())
                    check()
                } catch (e: Exception) {
                    AlertDialog.Builder(requireActivity())
                        .setTitle("Error")
                        .setMessage(e.toString())
                        .create()
                        .show()
                }
            }
            binding.downloadMp4.setOnClickListener {
                mKemonoViewModel.newFileName = binding.nameInput.text.toString()
                mKemonoViewModel.downloadMp4FromUrl()
            }
            binding.downloadJpg.setOnClickListener {
                mKemonoViewModel.newFileName = binding.nameInput.text.toString()
                mKemonoViewModel.downloadJpgFromUrl()
            }
            binding.downloadPng.setOnClickListener {
                mKemonoViewModel.newFileName = binding.nameInput.text.toString()
                mKemonoViewModel.downloadPngFromUrl()
            }
            binding.downloadZip.setOnClickListener {
                mKemonoViewModel.newFileName = binding.nameInput.text.toString()
                mKemonoViewModel.downloadZipFromUrl()
            }
        }

        /**
         * TODO: Implement ExoPlayer to Stream Videos to User in Second Fragment
         * TODO: Scroll Left to Right
         */
    }
    private fun check() {
        mKemonoViewModel.readAllFromUrl(mKemonoViewModel.link)
        adapter.update(mKemonoViewModel.listOfLinks)
        println(adapter.itemCount.toString())
        if (mKemonoViewModel.mp4State) {
            binding.downloadMp4.visibility = View.VISIBLE
        }else {
            binding.downloadMp4.visibility = View.INVISIBLE
        }
        if (mKemonoViewModel.jpgState) {
            binding.downloadJpg.visibility = View.VISIBLE
        }else {
            binding.downloadJpg.visibility = View.INVISIBLE
        }
        if (mKemonoViewModel.pngState) {
            binding.downloadPng.visibility = View.VISIBLE
        }else {
            binding.downloadPng.visibility = View.INVISIBLE
        }
        if (mKemonoViewModel.zipState) {
            binding.downloadZip.visibility = View.VISIBLE
        }else {
            binding.downloadZip.visibility = View.INVISIBLE
        }
        mKemonoViewModel.mp4State = false
        mKemonoViewModel.jpgState = false
        mKemonoViewModel.pngState = false
        mKemonoViewModel.zipState = false
        onResume()
        onResume()
    }
}