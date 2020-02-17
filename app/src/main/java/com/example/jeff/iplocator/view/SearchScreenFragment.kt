package com.example.jeff.iplocator.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.jeff.iplocator.R
import com.example.jeff.iplocator.util.validateIpAddress
import com.example.jeff.iplocator.viewmodel.SearchScreenViewModel
import kotlinx.android.synthetic.main.search_screen_fragment.*

class SearchScreenFragment : Fragment() {

    private lateinit var myViewModel: SearchScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

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
        observeSearchText()
        searchViewClicked()


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

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
        searchview_search.setOnClickListener { searchview_search.isIconified = false }

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

    private fun searchQuery(query: String?) = if (query != null) {
        if (validateIpAddress(query)) {
            val action =
                SearchScreenFragmentDirections.actionSearchScreenFragmentToResultScreenFragment(
                    query
                )
            view?.findNavController()?.navigate(action)
            searchview_search.setQuery(null, false)
           clearSearchQuery()


        } else {

            displayAlertDialog(
                context!!,
                getString(R.string.invalid_search),
                getString(R.string.invalid_address, query)
            )
            myViewModel.setSearchQuery(null)
            clearSearchQuery()


        }

    } else {
        Toast.makeText(context, getString(R.string.null_search), Toast.LENGTH_SHORT)
            .show()
    }

    private fun displayAlertDialog(context: Context, title: String, message: String) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(title).setMessage(message)
            .setNeutralButton(
                getString(R.string.dismiss)
            ) { dialog, which ->
                dialog.dismiss()
            }
        alertDialog.create().show()

    }

    private fun clearSearchQuery() {
        searchview_search.setQuery(null, false)
        searchview_search.clearFocus()
    }


}







