package com.example.udacity_project_two.overview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.udacity_project_two.R
import com.example.udacity_project_two.databinding.FragmentOverviewBinding
import kotlinx.android.synthetic.main.fragment_overview.*

class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by lazy {
        ViewModelProvider(this).get(OverviewViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentOverviewBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        return binding.root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        neo_recycler.visibility = View.GONE
        status_loading_wheel.visibility = View.VISIBLE

        getAsteroids()
        //viewModel.refreshDisplay()

        val asteroidAdapter = RecyclerAdapter(context!!) {
            val actionShowDetail = OverviewFragmentDirections.actionShowDetail(it)
            findNavController().navigate(actionShowDetail)
        }

        neo_recycler.adapter = asteroidAdapter
        viewModel.displayList.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                asteroidAdapter.setAsteroidList(it)
                neo_recycler.visibility = View.VISIBLE
                status_loading_wheel.visibility = View.GONE
            }
        })
    }

    private fun getAsteroids() {
        viewModel.loadTodaysAsteroids().observe(viewLifecycleOwner, Observer {
            viewModel.loadAsteroidList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.show_saved_menu -> {
                viewModel.loadSavedAsteroids().observe(
                    viewLifecycleOwner, Observer {
                        viewModel.loadAsteroidList(it) })
                true
            }
            R.id.show_today_menu -> {
                viewModel.loadTodaysAsteroids().observe(
                        viewLifecycleOwner, Observer {
                            viewModel.loadAsteroidList(it)
                })
                true
            }
            R.id.show_week_menu -> {
                viewModel.loadWeeksAsteroids().observe(
                        viewLifecycleOwner,
                        Observer { viewModel.loadAsteroidList(it) })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}