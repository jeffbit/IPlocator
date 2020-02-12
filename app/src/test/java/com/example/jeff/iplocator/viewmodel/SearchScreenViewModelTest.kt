package com.example.jeff.iplocator.viewmodel

import com.example.jeff.iplocator.network.Repository
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchScreenViewModelTest {


    @Mock
    lateinit var searchScreenViewModel: SearchScreenViewModel
    lateinit var repository: Repository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = mock()
        this.searchScreenViewModel = SearchScreenViewModel(repository)

    }





}