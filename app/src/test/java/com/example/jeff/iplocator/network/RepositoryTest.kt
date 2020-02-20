package com.example.jeff.iplocator.network


import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RepositoryTest {

    private lateinit var repository: Repository
    private lateinit var mockRetrofitClientInstance : RetrofitClientInstance
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
        val givenResponse = IP_RESPONSE
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
    fun invalidStringLookup_returnNull() = runBlocking {
        val givenResponse = null
        given { runBlocking { mockIPAddressAPIService.getIpAddress(INVALID_IP) } }.willReturn(null)
        //when
        runBlocking {
            val response = repository.getIp(INVALID_IP)
            verify(mockIPAddressAPIService.getIpAddress(INVALID_IP))
            Assert.assertNull(response)
        }

    }


}