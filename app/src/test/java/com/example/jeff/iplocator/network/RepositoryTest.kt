package com.example.jeff.iplocator.network


import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RepositoryTest {

    private lateinit var mockRetrofitClientInstance: RetrofitClientInstance
    private lateinit var mockRepository: Repository
    private lateinit var mockIPAddressAPIService: IPAddressAPIService

    @Before
    fun setup() {
        mockRetrofitClientInstance = mock()
        mockIPAddressAPIService = mock()
        mockRepository = Repository(mockRetrofitClientInstance)

    }

    @Test
    fun invalidIpAddress_returnNull() {
        runBlocking {
           whenever(mockRepository.getIp("10.56.16.4")).thenReturn(null)

        }

    }

    @Test
    fun invalidStringLookup_returnNull() {
        runBlocking {
            whenever(mockRepository.getIp("Test")).thenReturn(null)
        }
    }



}