package com.example.jeff.iplocator.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.jeff.iplocator.R
import com.example.jeff.iplocator.util.validateIpAddress
import com.example.jeff.iplocator.viewmodel.SearchScreenViewModel
import kotlinx.android.synthetic.main.search_screen_fragment.*

class SearchScreenFragment : Fragment() {

    companion object {
        fun newInstance() = SearchScreenFragment()
    }

    private lateinit var myViewModel: SearchScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_screen_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        myViewModel = ViewModelProviders.of(this).get(SearchScreenViewModel::class.java)
        // TODO: Use the ViewModel

        observeSearchText()
        searchViewClicked()


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_about -> {
                Navigation.findNavController(view!!)
                    .navigate(
                        SearchScreenFragmentDirections.actionSearchScreenFragmentToAboutScreenFragment()
                    )
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun searchViewClicked() {
        searchview_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchQuery(query)


                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }
        })

    }

    private fun observeSearchText() {
        myViewModel.searchQuery.observe(this, Observer {
            if (it != null) {
                searchview_search.setQuery(it, false)
            }
        })

    }

    private fun searchQuery(query: String?) {
        if (query != null) {
            if (validateIpAddress(query)) {
                val action =
                    SearchScreenFragmentDirections.actionSearchScreenFragmentToResultScreenFragment(
                        query
                    )
                view?.findNavController()?.navigate(action)
                searchview_search.setQuery(null, false)
                myViewModel.setSearchQuery(query)


            } else {

//                        searchView_searchView.setQuery(null, false)
                searchview_search.clearFocus()
                createAlertDialog(context!!, "Invalid Search", "Search entered is not valid")
                myViewModel.setSearchQuery()


            }

        } else {
            Toast.makeText(context, "Search query cannot be null", Toast.LENGTH_SHORT)
                .show();
        }
    }


    private fun createAlertDialog(context: Context, title: String, message: String) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(title).setMessage(message)
            .setNeutralButton(
                "Dismiss",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })


        alertDialog.create().show()

    }
}







