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
import kotlinx.coroutines.launch
import java.net.URL
import kotlin.Exception

class FirstFragment : Fragment() {

    /**
     * TODO: Rewrite Code so that you can choose to either download individual files or download as batch
     *
     */

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
                    AlertDialog.Builder(requireActivity())
                        .setTitle("Download Started")
                        .setMessage("Downloading...")
                        .setNegativeButton("Dismiss") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
                is UIState.LOADING2 -> {
                    binding.Loading.visibility = View.VISIBLE
                }
                is UIState.SUCCESS -> {
                    binding.Loading.visibility = View.INVISIBLE
                    state.success?.let { adapter.update(it) }
                }
                is UIState.SUCCESS2 -> {
                    check()
                    binding.Loading.visibility = View.INVISIBLE
                    state.success2?.let {
                        mKemonoViewModel.jpgState = it.jpgState
                        mKemonoViewModel.pngState = it.pngState
                        mKemonoViewModel.mp4State = it.mp4State
                        mKemonoViewModel.zipState = it.zipState
                        mKemonoViewModel.listOfLinks = it.links
                        println(mKemonoViewModel.jpgState)
                        println(mKemonoViewModel.pngState)
                        println(mKemonoViewModel.mp4State)
                        println(mKemonoViewModel.zipState)
                        println(mKemonoViewModel.listOfLinks)
                    }
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
        binding.Loading.visibility = View.INVISIBLE
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewLifecycleOwner.lifecycleScope.launch {
            binding.Setbutton.setOnClickListener {
                try {
                    mKemonoViewModel.readAllFromUrl(URL(binding.linkInput.text.toString()))
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
                mKemonoViewModel.downloadSwitchCase(URL(binding.linkInput.text.toString()), ".mp4")
            }
            binding.downloadJpg.setOnClickListener {
                mKemonoViewModel.newFileName = binding.nameInput.text.toString()
                mKemonoViewModel.downloadSwitchCase(URL(binding.linkInput.text.toString()), ".jpg")
            }
            binding.downloadPng.setOnClickListener {
                mKemonoViewModel.newFileName = binding.nameInput.text.toString()
                mKemonoViewModel.downloadSwitchCase(URL(binding.linkInput.text.toString()), ".png")
            }
            binding.downloadZip.setOnClickListener {
                mKemonoViewModel.newFileName = binding.nameInput.text.toString()
                mKemonoViewModel.downloadSwitchCase(URL(binding.linkInput.text.toString()), ".zip")
            }
        }
    }

    private fun check() {
        adapter.update(mKemonoViewModel.listOfLinks)
        if (mKemonoViewModel.mp4State) {
            binding.downloadMp4.visibility = View.VISIBLE
        } else {
            binding.downloadMp4.visibility = View.INVISIBLE
        }
        if (mKemonoViewModel.jpgState) {
            binding.downloadJpg.visibility = View.VISIBLE
        } else {
            binding.downloadJpg.visibility = View.INVISIBLE
        }
        if (mKemonoViewModel.pngState) {
            binding.downloadPng.visibility = View.VISIBLE
        } else {
            binding.downloadPng.visibility = View.INVISIBLE
        }
        if (mKemonoViewModel.zipState) {
            binding.downloadZip.visibility = View.VISIBLE
        } else {
            binding.downloadZip.visibility = View.INVISIBLE
        }
        println(adapter.itemCount.toString())
        println(mKemonoViewModel.jpgState.toString() + mKemonoViewModel.pngState.toString() + mKemonoViewModel.mp4State.toString() + mKemonoViewModel.zipState.toString())
    }
}