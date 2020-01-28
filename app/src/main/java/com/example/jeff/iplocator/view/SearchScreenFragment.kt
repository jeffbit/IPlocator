package com.example.jeff.iplocator.view

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.jeff.iplocator.R
import com.example.jeff.iplocator.util.hideKeyBoard
import com.example.jeff.iplocator.viewmodel.SearchScreenViewModel
import kotlinx.android.synthetic.main.search_screen_fragment.*

class SearchScreenFragment : Fragment() {

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




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_screen_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        myViewModel = ViewModelProvider(this).get(SearchScreenViewModel::class.java)

        enterOnClick()
        observeResult()

    }

    private fun observeResult() {

        myViewModel.ipAddress.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                search_textview.text = it.toString()
            }

        })
    }


    private fun enterOnClick() {
        search_button.setOnClickListener {
            myViewModel.returnIpAddress(search_input.text.toString())
            observeResult()
            showErrorMessage()
            showProgressBar()
            hideKeyBoard()


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
                search_textview.text = getString(R.string.search_error)

            } else {

            }
        })
    }


}
