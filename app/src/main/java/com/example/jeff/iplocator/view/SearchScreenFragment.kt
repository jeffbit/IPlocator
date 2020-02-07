package com.example.jeff.iplocator.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.jeff.iplocator.R
import com.example.jeff.iplocator.util.convertTime
import com.example.jeff.iplocator.util.getMobileIp
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
        flag_imageview.visibility = View.GONE

        observeResult()


    }


    private fun observeResult() {

        myViewModel.ipAddress.observe(viewLifecycleOwner, Observer {

            if (it == null) {
                Toast.makeText(context, "No Address found", Toast.LENGTH_SHORT).show();
            } else {
                myViewModel.displayMap(it.latitude, it.longitude, it.asn.name)
                //map cardview
                loadImageToDisplay(it.flag, flag_imageview, view!!, 60, 40)
                myViewModel.hideTextviewIfNull(country_textview, it.countryName)
                myViewModel.hideTextviewIfNull(
                    lat_textview,
                    getString(R.string.lat, it.latitude.toString())
                )
                myViewModel.hideTextviewIfNull(
                    lon_textview,
                    getString(R.string.lon, it.longitude.toString())
                )

                //location cardview
                myViewModel.hideTextviewIfNull(
                    proxy_textview,
                    proxy_input_textview,
                    it.threat.isProxy.toString()
                )
                myViewModel.hideTextviewIfNull(
                    threat_time_textview,
                    threat_input_textview,
                    it.threat.isThreat.toString()
                )
                myViewModel.hideTextviewIfNull(ipadress_textview, ip_input_textview, it.ip)
                myViewModel.hideTextviewIfNull(isp_textView, isp_input_textview, it.asn.name)
                myViewModel.hideTextviewIfNull(city_textview, city_input_textview, it.city)
                myViewModel.hideTextviewIfNull(region_textview, region_input_textview, it.region)
                myViewModel.hideTextviewIfNull(
                    country_textview,
                    country_input_textview,
                    it.countryName
                )
                myViewModel.hideTextviewIfNull(
                    continentname_textview,
                    continent_input_textview,
                    it.continentName
                )
                myViewModel.hideTextviewIfNull(postal_textview, postal_input_textview, it.postal)
                myViewModel.hideTextviewIfNull(
                    language_textview,
                    language_input_textview,
                    it.languages[0].name
                )
                myViewModel.hideTextviewIfNull(
                    currency_textview,
                    currency_input_textview,
                    it.currency.name
                )
                myViewModel.hideTextviewIfNull(
                    timezone_name_textview,
                    timezone_input_textview,
                    it.timeZone.abbr
                )
                myViewModel.hideTextviewIfNull(
                    current_time_textview,
                    currenttime_input_textview,
                    convertTime(it.timeZone.currentTime)
                )


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
            showProgressBar()
        } else {
            Toast.makeText(context, "NOT VALID", Toast.LENGTH_SHORT).show();
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


    //TODO: display empty textviews upon start, if ip is valid display textviews and map with informaiton, if invalid hide textviews and display error message


    override fun onMapReady(map: GoogleMap) {
        Log.e("Search", "Map Ready")
        myViewModel.myMap = map
        //puts marker before orientation change on map if value is not null
        if (myViewModel.lat.value != null) {
            myViewModel.displayMap(myViewModel.lat.value!!, myViewModel.lon.value!!)
        }


    }


}
