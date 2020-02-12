package com.example.jeff.iplocator.network


import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

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