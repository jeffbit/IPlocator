package com.example.jeff.iplocator.view

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.jeff.iplocator.R
import com.example.jeff.iplocator.model.IpAddress
import com.example.jeff.iplocator.network.Result
import com.example.jeff.iplocator.util.convertTime
import com.example.jeff.iplocator.util.loadImageToDisplay
import com.example.jeff.iplocator.util.validateIpAddress
import com.example.jeff.iplocator.viewmodel.SearchScreenViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.search_screen_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

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

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        hideMapInfo()
        observeResult()


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
        } else {
            showError(getString(R.string.invalid_search))
        }


    }

    private fun loadData(it: IpAddress) {
        myViewModel.displayMap(it.latitude, it.longitude, it.asn.name)

        //map cardview
        loadImageToDisplay(it.flag, flag_imageview, view!!, 60, 40)
        myViewModel.hideTextIfNull(country_textview, it.countryName)
        myViewModel.hideTextIfNull(
            lat_textview,
            getString(R.string.lat, it.latitude.toString())
        )
        myViewModel.hideTextIfNull(
            lon_textview,
            getString(R.string.lon, it.longitude.toString())
        )

        //location cardview
        myViewModel.hideLinearLayoutIfNull(
            proxy_linearlayout,
            proxy_input_textview,
            it.threat.isProxy.toString()
        )
        myViewModel.hideLinearLayoutIfNull(
            threat_linearlayout,
            threat_input_textview,
            it.threat.isThreat.toString()
        )
        myViewModel.hideLinearLayoutIfNull(ip_linearlayout, ip_input_textview, it.ip)
        myViewModel.hideLinearLayoutIfNull(isp_linearlayout, isp_input_textview, it.asn.name)
        myViewModel.hideLinearLayoutIfNull(city_linearlayout, city_input_textview, it.city)
        myViewModel.hideLinearLayoutIfNull(region_linearlayout, region_input_textview, it.region)
        myViewModel.hideLinearLayoutIfNull(
            country_linearlayout,
            country_input_textview,
            it.countryName
        )
        myViewModel.hideLinearLayoutIfNull(
            continent_linearlayout,
            continent_input_textview,
            it.continentName
        )
        myViewModel.hideLinearLayoutIfNull(postal_linearlayout, postal_input_textview, it.postal)
        myViewModel.hideLinearLayoutIfNull(
            language_linearlayout,
            language_input_textview,
            it.languages[0].name
        )
        myViewModel.hideLinearLayoutIfNull(
            currency_linearlayout,
            currency_input_textview,
            it.currency.name
        )
        myViewModel.hideLinearLayoutIfNull(
            timezone_linearlayout,
            timezone_input_textview,
            it.timeZone.abbr
        )
        myViewModel.hideLinearLayoutIfNull(
            currentime_linearlayout,
            currenttime_input_textview,
            convertTime(it.timeZone.currentTime)
        )
    }


    //Display Error
    private fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
        myViewModel.setIpAddressEmpty()
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
        myViewModel.myMap = map
        //display marker after orientation change if value is not null
        if (myViewModel.lat.value != null && myViewModel.lon.value != null) {
            myViewModel.displayMap(myViewModel.lat.value!!, myViewModel.lon.value!!)
        }
    }

}
