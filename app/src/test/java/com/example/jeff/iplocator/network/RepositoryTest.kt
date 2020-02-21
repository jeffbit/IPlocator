package com.example.jeff.iplocator.network


import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RepositoryTest {

    private lateinit var repository: Repository
    private lateinit var mockRetrofitClientInstance: RetrofitClientInstance
    private lateinit var mockIPAddressAPIService: IPAddressAPIService


    @Before
    fun setup() {
        mockRetrofitClientInstance = mock()
        mockIPAddressAPIService = mock()
        repository = Repository(mockIPAddressAPIService)

    }

    @Test
    fun validIpAddressSearch_returnEquals() = runBlocking {
        //given
        val givenResponse = VALID_IP_RESPONSE
        whenever(mockIPAddressAPIService.getIpAddress(VALID_IP)).thenReturn(givenResponse)
        //when
        val response = repository.getIp(VALID_IP)
        //then
        verify(mockIPAddressAPIService).getIpAddress(VALID_IP)
        Assert.assertEquals(response, givenResponse)
        println(response)
        println(givenResponse)
    }


    @Test
    fun invalidIpAddressSearch_returnNull() = runBlocking {
        //given
        whenever(mockIPAddressAPIService.getIpAddress(INVALID_IP)).thenReturn(null)
        //when
        val response = repository.getIp(INVALID_IP)
        //then
        verify(mockIPAddressAPIService).getIpAddress(INVALID_IP)
        Assert.assertNull(response)
    }

}

