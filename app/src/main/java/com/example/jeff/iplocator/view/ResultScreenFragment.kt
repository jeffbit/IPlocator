package com.example.jeff.iplocator.view

import android.os.Bundle
import android.view.*
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
import com.example.jeff.iplocator.viewmodel.ResultScreenViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.result_screen_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ResultScreenFragment : Fragment(), OnMapReadyCallback {

    private val myViewModel by viewModel<ResultScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        //todo: call observe viewmodel in oncreate so its always observing the viewmodel class


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
//        val searchItem = menu.findItem(R.id.app_bar_search)
//        val searchView = searchItem.actionView as SearchView
//        searchView.queryHint = getString(R.string.enter_ip)
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                enterOnClick(query!!)
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//        })

        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_about -> {
                Navigation.findNavController(view!!)
                    .navigate(
                        ResultScreenFragmentDirections.actionResultScreenFragmentToAboutScreenFragment()
                    )
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.result_screen_fragment, container, false)

        val mapFragment: SupportMapFragment? =
            childFragmentManager.findFragmentById(R.id.mapview_fragment) as SupportMapFragment
        mapFragment?.getMapAsync(this)
//        observeResult()
        enterOnClick(arguments.let { ResultScreenFragmentArgs.fromBundle(it!!).searchQuery })

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideMapInfo()

    }


    //move this fun to new searchscreen
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
        hideTextIfNull(ip_title, ip_input, it.ip)
        hideTextIfNull(threat_title, threat_input, it.threat.isThreat.toString())
        hideTextIfNull(proxy_title, proxy_input, it.threat.isProxy.toString())
        hideTextIfNull(isp_title, isp_input, it.asn.name)
        hideTextIfNull(city_title, city_input, it.city)
        hideTextIfNull(region_title, region_input, it.region)
        hideTextIfNull(country_title, country_input, it.countryName)
        hideTextIfNull(continent_title, continent_input, it.continentName)
        hideTextIfNull(postalcode_title, postalcode_input, it.postal)
        hideTextIfNull(language_title, language_input, it.languages[0].name)
        hideTextIfNull(currency_title, currency_input, it.currency.name)
        hideTextIfNull(timezone_title, timezone_input, it.timeZone.name)
        hideTextIfNull(
            currenttime_title,
            currenttime_input,
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
