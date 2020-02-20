package com.example.jeff.iplocator.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.example.jeff.iplocator.model.IpAddress
import com.example.jeff.iplocator.network.*
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ResultScreenViewModelTest {


    @get:Rule
    val rule = InstantTaskExecutorRule()



    private lateinit var mockRetrofitClientInstance: RetrofitClientInstance
    private lateinit var mockRepository: Repository
    private lateinit var mockIPAddressAPIService: IPAddressAPIService
    private lateinit var mockIpAddress: IpAddress
    private lateinit var resultScreenViewModel: ResultScreenViewModel
    private lateinit var mockLiveData : LiveData<Result<IpAddress>>

    @Before
    fun setup() {
        mockIPAddressAPIService = mock()
        mockRetrofitClientInstance= mock()
        mockIpAddress = mock()
        mockRepository = mock()
        resultScreenViewModel = ResultScreenViewModel(mockRepository)
        mockLiveData = mock()


    }


    @Test
    fun validIpAddress_returnSuccess() = runBlocking{
        //given
        val expectedResult = Result.Success(VALID_IP)

        //when
        whenever(mockRepository.getIp(VALID_IP)).thenReturn(VALID_IP_RESPONSE)

        //then
        val result = resultScreenViewModel.returnIpAddress(VALID_IP)
        Assert.assertEquals(expectedResult, result)

    }






}