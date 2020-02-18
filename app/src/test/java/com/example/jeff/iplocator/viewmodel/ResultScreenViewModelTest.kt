package com.example.jeff.iplocator.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.jeff.iplocator.network.Repository
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ResultScreenViewModelTest {


    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var resultScreenViewModel: ResultScreenViewModel
    lateinit var repository: Repository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = mock()
        this.resultScreenViewModel = ResultScreenViewModel(repository)

    }


    @Test
    fun invalidIpAddress_returnNull() {
        runBlocking {
            val result = repository.getIp("10.56.14.13")

            Assert.assertEquals(result, null)

        }

    }

    @Test
    fun invalidStringLookup_returnNull() {
        runBlocking {
            whenever(repository.getIp("Test")).thenReturn(null)
        }
    }


}