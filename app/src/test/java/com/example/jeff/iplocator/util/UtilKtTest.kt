package com.example.jeff.iplocator.util

import com.example.jeff.iplocator.network.INVALID_IP
import com.example.jeff.iplocator.network.VALID_IP
import com.example.jeff.iplocator.network.VALID_TIME_CONVERT
import com.example.jeff.iplocator.network.VALID_TIME_INPUT
import org.junit.Assert
import org.junit.Test

class UtilKtTest {

    @Test
  fun validateIpAddress_returnTrue(){
        //given
        //when
        val result =
            validateIpAddress(VALID_IP)
        //then
        Assert.assertTrue(result)

    }
    @Test
    fun validateIpAddress_returnFalse(){
        //given
        //when
        val result =
            validateIpAddress(INVALID_IP)
        //then
        Assert.assertFalse(result)

    }
    @Test
    fun convertTime_returnEquals(){
        //given
        val time = VALID_TIME_INPUT

        //when
        val result = convertTime(time)
        //then
        val expectedOutput = VALID_TIME_CONVERT

        Assert.assertEquals(result, expectedOutput)
        println(result)
        println(expectedOutput)
    }
    @Test
    fun convertTime_returnNotNull(){
        //given
val time = VALID_TIME_INPUT

        //when
        val result = convertTime(time)
        //then
        Assert.assertNotNull(result)

    }

}