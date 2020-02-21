package com.brown.jeff.iplocator.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.brown.jeff.iplocator.model.IpAddress
import com.brown.jeff.iplocator.network.IPAddressAPIService
import com.brown.jeff.iplocator.network.Repository
import com.brown.jeff.iplocator.network.Result
import com.brown.jeff.iplocator.network.*
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class ResultScreenViewModelTest {

    //Testing in progress. Issue may be that in resultscreenviewmodel I am trying to treat livedata as a stream?
    //may need to rebuild viewmodel in order to properly test

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var mockRepository: Repository
    private lateinit var mockIPAddressAPIService: IPAddressAPIService
    private lateinit var resultScreenViewModel: ResultScreenViewModel
    private lateinit var mockObserver: Observer<Result<IpAddress>>
    private val testDispatcher = TestCoroutineDispatcher()


    @Before
    fun setup() {
        mockIPAddressAPIService = mock()
        mockRepository = mock()
        mockObserver = mock()
        resultScreenViewModel =
            ResultScreenViewModel(
                mockRepository
            )
        Dispatchers.setMain(testDispatcher)


    }


    @Test
    fun validIpAddress_returnSuccess() = runBlockingTest {
        //given
        val expectedResult = Result.Success(VALID_IP_RESPONSE).data
        whenever(mockRepository.getIp(VALID_IP)).thenReturn(expectedResult)
        //when
        resultScreenViewModel.returnIpAddress(VALID_IP)
        val result = resultScreenViewModel.ipAddress.observeForever(mockObserver)
        //then
       Assert.assertEquals(expectedResult, result)


    }

    @Test
    fun invalidIpAddress_returnError() {
        //given
        val expectedResult = Result.Error<IpAddress>(NullPointerException())
        given { runBlocking { mockRepository.getIp(INVALID_IP) } }.willReturn(null)
        resultScreenViewModel.ipAddress.observeForever(mockObserver)
        //when
        runBlocking {
            resultScreenViewModel.returnIpAddress(INVALID_IP)
        }
        val result = resultScreenViewModel.ipAddress.value
        //then
        println(resultScreenViewModel.returnIpAddress(INVALID_IP))
        print(resultScreenViewModel.ipAddress.value)
        Assert.assertEquals(expectedResult, result)


    }

    @Test
    fun validIpAddress_returnLoading() {
        //given
        val expectedResult = Result.Loading<IpAddress>()
        resultScreenViewModel.ipAddress.observeForever(mockObserver)
        //when
        runBlocking {
            resultScreenViewModel.returnIpAddress(INVALID_IP)
        }

        val result = resultScreenViewModel.ipAddress.value
        //then
        println(resultScreenViewModel.returnIpAddress(INVALID_IP))
        print(resultScreenViewModel.ipAddress.value)
        Assert.assertEquals(expectedResult, result)


    }


    @After
    fun after() {
        Dispatchers.resetMain()
    }


    fun <T> getValue(liveData: LiveData<T>): T {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data[0] = o
                latch.countDown()
                liveData.removeObserver(this)
            }
        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)

        @Suppress("UNCHECKED_CAST")
        return data[0] as T
    }
}