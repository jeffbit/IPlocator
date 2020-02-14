package com.example.jeff.iplocator.view

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.jeff.iplocator.R
import com.example.jeff.iplocator.model.IpAddress
import com.example.jeff.iplocator.network.Result
import com.example.jeff.iplocator.util.convertTime
import com.example.jeff.iplocator.util.loadImageToDisplay
import com.example.jeff.iplocator.viewmodel.SearchScreenViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.search_screen_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SearchScreenFragment : Fragment(), OnMapReadyCallback {

    private val myViewModel by viewModel<SearchScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        //todo: call observe viewmodel in oncreate so its always observing the viewmodel class


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.enter_ip)

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
        observeResult()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideMapInfo()

    }


    private fun observeResult() {
        myViewModel.ipAddress.observe(viewLifecycleOwner, Observer { it ->

            when (it) {
                is Result.Loading -> {
                    showProgressBar()
                }
                is Result.Success -> {
                    loadData(it.data)
                    hideProgressBar()
                }
                is Result.Error -> {
                    showError(it.throwable.message.toString())
                    hideProgressBar()
                    myViewModel.setIpAddressEmpty()
                }
                is Result.NullValue -> {
                    showError("Null value")
                    hideProgressBar()
                    myViewModel.setIpAddressEmpty()
                }

            }
        })
    }


    //Todo: add validation to viewmodel clas
    private fun enterOnClick(input: String) {
        myViewModel.returnIpAddress(ip = input)
        //clears previous map lat and lon along with markers
        myViewModel.clearLatAndLon()
        observeResult()


    }

    private fun loadData(it: IpAddress) {

        //map cardview
        loadImageToDisplay(it.flag, flag_imageview, view!!, 60, 40)
        hideTextIfNull(null, country_textview, it.countryName)
        hideTextIfNull(null, lon_textview, getString(R.string.lon, it.longitude.toString()))
        hideTextIfNull(null, lat_textview, getString(R.string.lat, it.latitude.toString()))

        //location cardview
        hideTextIfNull(ipaddress_textview, ip_input_textview, it.ip)
        hideTextIfNull(threat_time_textview, threat_input_textview, it.threat.isThreat.toString())
        hideTextIfNull(proxy_textview, proxy_input_textview, it.threat.isProxy.toString())
        hideTextIfNull(isp_textView, isp_input_textview, it.asn.name)
        hideTextIfNull(city_textview, city_input_textview, it.city)
        hideTextIfNull(region_textview, region_input_textview, it.region)
        hideTextIfNull(countryname_textview, country_input_textview, it.countryName)
        hideTextIfNull(continentname_textview, continent_input_textview, it.continentName)
        hideTextIfNull(postal_textview, postal_input_textview, it.postal)
        hideTextIfNull(language_textview, language_input_textview, it.languages[0].name)
        hideTextIfNull(currency_textview, currency_input_textview, it.currency.name)
        hideTextIfNull(timezone_name_textview, timezone_input_textview, it.timeZone.name)
        hideTextIfNull(
            current_time_textview,
            currenttime_input_textview,
            convertTime(it.timeZone.currentTime)
        )
    }


    //Display Error
    private fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
        myViewModel.setIpAddressEmpty()
    }


    private fun hideTextIfNull(
        textView1: TextView? = null, textView2: TextView,
        value: String?
    ) {
        if (value.isNullOrEmpty()) {
            textView1?.visibility = View.GONE
            textView2.visibility = View.GONE
        } else {
            textView1?.visibility = View.VISIBLE
            textView2.visibility = View.VISIBLE
            textView2.text = value
        }
    }

    private fun showProgressBar() {
        search_progressBar.visibility = View.VISIBLE

    }

    private fun hideProgressBar() {
        search_progressBar.visibility = View.GONE

    }

    private fun hideMapInfo() {
        flag_imageview.visibility = View.GONE
        lat_textview.visibility = View.GONE
        lon_textview.visibility = View.GONE

    }


    override fun onMapReady(map: GoogleMap) {
        Timber.d("onMapReady")
        myViewModel.ipAddress.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Success -> {
                    map.addMarker(
                        MarkerOptions().position(
                            LatLng(it.data.latitude, it.data.longitude)
                        ).title(it.data.asn.name)
                    )
                    map.moveCamera(
                        CameraUpdateFactory
                            .newLatLngZoom(LatLng(it.data.latitude, it.data.longitude), 10f)
                    )
                }
                is Result.Error -> {
                    map.clear()
                    myViewModel.clearLatAndLon()

                }


            }
        })

    }


}
