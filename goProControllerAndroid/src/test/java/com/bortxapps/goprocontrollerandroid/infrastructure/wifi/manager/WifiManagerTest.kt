package com.bortxapps.goprocontrollerandroid.infrastructure.wifi.manager

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.net.wifi.WifiNetworkSpecifier
import app.cash.turbine.test
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class WifiManagerTest {

    private val contextMock = mockk<Context>(relaxed = true)
    private val wifiManagerMockk = mockk<android.net.wifi.WifiManager>()
    private val intentMock = mockk<Intent>(relaxed = true)
    private val networkSpecifierMock = mockk<WifiNetworkSpecifier>(relaxed = true)
    private val networkRequestMock = mockk<NetworkRequest>(relaxed = true)
    private val connectivityManager = mockk<ConnectivityManager>(relaxed = true)
    private val networkMock = mockk<android.net.Network>(relaxed = true)

    private val ssid = "TestSSID"
    private val password = "TestPassword"
    private val wifiManager = spyk(WifiManager())

    @Before
    fun setUp() {
        every { contextMock.getSystemService(Context.WIFI_SERVICE) } returns wifiManagerMockk
    }

    @Test
    fun testEnableWIfiWhenWifiIsDisabled() {
        every { wifiManagerMockk.isWifiEnabled } returns false
        every { contextMock.startActivity(any()) } just Runs
        every { wifiManager invokeNoArgs "getIntent" } returns intentMock
        wifiManager.enableWifi(contextMock)
        verify { contextMock.startActivity(any()) }
    }

    @Test
    fun testEnableWIfiWhenWifiIsEnabled() {
        every { wifiManagerMockk.isWifiEnabled } returns true
        wifiManager.enableWifi(contextMock)
        verify(exactly = 0) { contextMock.startActivity(any()) }
    }

    @Test
    fun testExceptionWhileRequestingNetworks() = runTest {
        coEvery { contextMock.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        coEvery { contextMock.getSystemService(Context.WIFI_SERVICE) } returns wifiManagerMockk
        coEvery { wifiManager invoke "getNetworkSpecifier" withArguments listOf(ssid, password) } returns networkSpecifierMock
        coEvery { wifiManager invoke "getNetworkRequest" withArguments listOf(networkSpecifierMock) } returns networkRequestMock

        coEvery {
            connectivityManager.requestNetwork(
                any(),
                any<ConnectivityManager.NetworkCallback>()
            )
        } throws Exception()


        val flow = wifiManager.connectToWifi(ssid, password, contextMock)

        flow.test {
            val connecting = this.awaitItem()
            assertTrue(connecting.isSuccess)
            assertEquals(WifiStatus.CONNECTING, connecting.getOrNull())

            val error = this.awaitItem()
            assertTrue(error.isFailure)
            assertTrue(error.exceptionOrNull() is Exception)

            awaitComplete()
        }
    }

    @Test
    fun testOnAvailableCase() = runTest {
        coEvery { contextMock.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        coEvery { contextMock.getSystemService(Context.WIFI_SERVICE) } returns wifiManagerMockk
        coEvery { wifiManager invoke "getNetworkSpecifier" withArguments listOf(ssid, password) } returns networkSpecifierMock
        coEvery { wifiManager invoke "getNetworkRequest" withArguments listOf(networkSpecifierMock) } returns networkRequestMock
        coEvery { connectivityManager.bindProcessToNetwork(networkMock) } returns true

        val callbackSlot = slot<ConnectivityManager.NetworkCallback>()

        coEvery {
            connectivityManager.requestNetwork(
                any(),
                capture(callbackSlot)
            )
        } answers {
            callbackSlot.captured.onAvailable(networkMock)
        }

        val flow = wifiManager.connectToWifi(ssid, password, contextMock)

        flow.test {
            val connecting = this.awaitItem()
            assertTrue(connecting.isSuccess)
            assertEquals(WifiStatus.CONNECTING, connecting.getOrNull())

            val result = this.awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(WifiStatus.CONNECTED, result.getOrNull())
        }

        verify { connectivityManager.bindProcessToNetwork(networkMock) }
    }

    @Test
    fun testOnLostCase() = runTest {
        coEvery { contextMock.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        coEvery { contextMock.getSystemService(Context.WIFI_SERVICE) } returns wifiManagerMockk
        coEvery { wifiManager invoke "getNetworkSpecifier" withArguments listOf(ssid, password) } returns networkSpecifierMock
        coEvery { wifiManager invoke "getNetworkRequest" withArguments listOf(networkSpecifierMock) } returns networkRequestMock

        val callbackSlot = slot<ConnectivityManager.NetworkCallback>()

        coEvery {
            connectivityManager.requestNetwork(
                any(),
                capture(callbackSlot)
            )
        } answers {
            callbackSlot.captured.onLost(networkMock)
        }

        val flow = wifiManager.connectToWifi(ssid, password, contextMock)

        flow.test {
            val connecting = this.awaitItem()
            assertTrue(connecting.isSuccess)
            assertEquals(WifiStatus.CONNECTING, connecting.getOrNull())

            val result = this.awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(WifiStatus.CONNECTION_LOST, result.getOrNull())

            awaitComplete()
        }

        verify { connectivityManager.bindProcessToNetwork(null) }
        verify { connectivityManager.unregisterNetworkCallback(any<ConnectivityManager.NetworkCallback>()) }
    }

    @Test
    fun testOnUnavailable() = runTest {
        coEvery { contextMock.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        coEvery { contextMock.getSystemService(Context.WIFI_SERVICE) } returns wifiManagerMockk
        coEvery { wifiManager invoke "getNetworkSpecifier" withArguments listOf(ssid, password) } returns networkSpecifierMock
        coEvery { wifiManager invoke "getNetworkRequest" withArguments listOf(networkSpecifierMock) } returns networkRequestMock

        val callbackSlot = slot<ConnectivityManager.NetworkCallback>()

        coEvery {
            connectivityManager.requestNetwork(
                any(),
                capture(callbackSlot)
            )
        } answers {
            callbackSlot.captured.onUnavailable()
        }

        val flow = wifiManager.connectToWifi(ssid, password, contextMock)

        flow.test {
            val connecting = this.awaitItem()
            assertTrue(connecting.isSuccess)
            assertEquals(WifiStatus.CONNECTING, connecting.getOrNull())

            val result = this.awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(WifiStatus.CONNECTION_FAILED, result.getOrNull())

            awaitComplete()
        }

        verify { connectivityManager.bindProcessToNetwork(null) }
        verify { connectivityManager.unregisterNetworkCallback(any<ConnectivityManager.NetworkCallback>()) }
    }

}