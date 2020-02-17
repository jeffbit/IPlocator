package com.example.jeff.iplocator.network


import com.nhaarman.mockito_kotlin.mock
import org.junit.Before

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




}