package com.example.jeff.iplocator.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.jeff.iplocator.R
import com.example.jeff.iplocator.util.loadImageToDisplay
import com.example.jeff.iplocator.util.validateIpAddress
import com.example.jeff.iplocator.viewmodel.SearchScreenViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.search_screen_fragment.*

class SearchScreenFragment : Fragment(), OnMapReadyCallback {

//    val myViewModel: SearchScreenViewModel by viewModel()

    private lateinit var myViewModel: SearchScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

//        //call to start Koin
//        startKoin {
//            androidLogger()
//            androidContext(this@SearchScreenFragment.context?.applicationContext!!)
//            modules(networkModule)
//
//    }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Enter valid IP"

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

        myViewModel = ViewModelProvider(this).get(SearchScreenViewModel::class.java)



        observeResult()

    }


    private fun observeResult() {

        myViewModel.ipAddress.observe(viewLifecycleOwner, Observer {

            if (it.equals(null)) {
                Toast.makeText(context, "No Address found", Toast.LENGTH_SHORT).show();
            } else {

                isp_textView.text = it.toString()
                myViewModel.displayMap(it.latitude, it.longitude, it.asn.name)
                //map title
                loadImageToDisplay(it.flag, flag_imageview, view!!, 60, 40)
                hideTextviewIfNull(country_textview, it.countryName)
                hideTextviewIfNull(
                    lon_textview,
                    it.longitude.toString()
                )
                hideTextviewIfNull(
                    lat_textview,
                    it.latitude.toString()
                )
                hideTextviewIfNull(timezone_textview, it.timeZone.offset)
                //location
                hideTextviewIfNull(ip_textview, it.ip)
                hideTextviewIfNull(isp_textView, it.asn.name)
                hideTextviewIfNull(city_textview, it.city)
                hideTextviewIfNull(region_textview, it.region)
                hideTextviewIfNull(continentname_textview, it.continentName)
                hideTextviewIfNull(postal_textview, it.postal)
                hideTextviewIfNull(language_textview, it.languages.get(0).name)
                hideTextviewIfNull(currency_textview, it.currency.code)
                hideTextviewIfNull(timezone_name_textview, it.timeZone.name)
                hideTextviewIfNull(current_time_textview, it.timeZone.currentTime)
                hideTextviewIfNull(countryname_textview, it.countryName)
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

    private fun hideTextviewIfNull(textView: TextView, value: String) {
        if (value.isNullOrBlank()) {
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
                ip_textview.text = getString(R.string.search_error)
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
