package com.example.jeff.iplocator.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.jeff.iplocator.R
import com.example.jeff.iplocator.util.loadImageToDisplay
import com.example.jeff.iplocator.util.validateIpAddress
import com.example.jeff.iplocator.viewmodel.SearchScreenViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.search_screen_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchScreenFragment : Fragment(), OnMapReadyCallback {

    private val myViewModel by viewModel<SearchScreenViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Enter IP"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                enterOnClick(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })




        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_about -> {
                Navigation.findNavController(view!!)
                    .navigate(
                        SearchScreenFragmentDirections
                            .actionSearchScreenFragmentToAboutScreenFragment()
                    )
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.search_screen_fragment, container, false)

        val mapFragment: SupportMapFragment? =
            childFragmentManager.findFragmentById(R.id.mapview_fragment) as SupportMapFragment
        mapFragment?.getMapAsync(this)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        observeResult()

    }


    private fun observeResult() {

        myViewModel.ipAddress.observe(viewLifecycleOwner, Observer {

            if (it == null) {
                Toast.makeText(context, "No Address found", Toast.LENGTH_SHORT).show();
            } else {

                isp_textView.text = it.toString()
                myViewModel.displayMap(it.latitude, it.longitude, it.asn.name)
                //map title
                loadImageToDisplay(it.flag, flag_imageview, view!!, 60, 40)
                hideTextviewIfNull(ip_textview, it.ip)

            }


        })
    }


    private fun enterOnClick(input: String) {
        if (validateIpAddress(input)) {
            myViewModel.returnIpAddress(ip = input)
            //clears previous map lat and lon along with markers
            myViewModel.clearMap()
            myViewModel.clearLatAndLon()

            observeResult()
            showErrorMessage()
            showProgressBar()
        } else {
            Toast.makeText(context, "NOT VALID", Toast.LENGTH_SHORT).show();
        }


    }

    fun hideTextviewIfNull(textView: TextView, value: String) {
        if (value.isNullOrEmpty()) {
            textView.visibility = View.GONE
        } else {
            textView.visibility = View.VISIBLE
            textView.text = value
        }
    }


    private fun showProgressBar() {

        myViewModel.loadingScreen.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                search_progressBar.visibility = View.GONE
            } else {
                search_progressBar.visibility = View.VISIBLE
            }
        })
    }

    private fun showErrorMessage() {
        myViewModel.showErrorMessage.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                isp_textView.text = getString(R.string.search_error)
            }
        })
    }

    private fun showResults() {
        myViewModel.showResults.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                searchscreen_scrollview.visibility = View.VISIBLE
            } else {
                searchscreen_scrollview.visibility = View.GONE

            }
        })
    }

    override fun onMapReady(map: GoogleMap) {
        Log.e("Search", "Map Ready")
        myViewModel.myMap = map
        //puts marker before orientation change on map if value is not null
        if (myViewModel.lat.value != null) {
            myViewModel.displayMap(myViewModel.lat.value!!, myViewModel.lon.value!!)
        }


    }


}
