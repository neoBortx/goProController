package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothStatusCodes
import android.content.Context
import android.os.Build
import app.cash.turbine.test
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.feature.commands.data.BLE_DESCRIPTION_BASE_UUID
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProUUID
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleNetworkMessage
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleNetworkMessageProcessor
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.scanner.BleDeviceScannerManager
import com.bortxapps.goprocontrollerandroid.urils.BuildVersionProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Timer
import java.util.UUID
import kotlin.concurrent.schedule

@Suppress("DEPRECATION")
class BleManagerTest {

    private val bleScannerMock = mockk<BleDeviceScannerManager>(relaxed = true)
    private val bleNetworkMessageProcessorMock = mockk<BleNetworkMessageProcessor>(relaxed = true)
    private val bluetoothDeviceMock = mockk<BluetoothDevice>(relaxed = true)
    private val bluetoothDeviceMock2 = mockk<BluetoothDevice>(relaxed = true)
    private val bluetoothLeScannerMock = mockk<android.bluetooth.le.BluetoothLeScanner>(relaxed = true)
    private val bluetoothAdapterMock = mockk<android.bluetooth.BluetoothAdapter> {
        every { bluetoothLeScanner } returns bluetoothLeScannerMock
    }
    private val bluetoothManagerMock = mockk<BluetoothManager> {
        every { adapter } returns bluetoothAdapterMock
    }
    private val contextMock = mockk<Context>(relaxed = true)
    private val bluetoothGattMock: BluetoothGatt by lazy { mockk<BluetoothGatt>(relaxed = true) }
    private val bluetoothCharacteristicMock = mockk<BluetoothGattCharacteristic>(relaxed = true)
    private val bluetoothGattServiceMock = mockk<android.bluetooth.BluetoothGattService>(relaxed = true)
    private val bluetoothGattDescriptorMock = mockk<BluetoothGattDescriptor>(relaxed = true)
    private val buildVersionProviderMock = mockk<BuildVersionProvider>(relaxed = true)


    private lateinit var bleManager: BleManager
    private lateinit var mutex: Mutex
    private val serviceUUID = UUID.randomUUID()
    private val characteristicUUID = UUID.randomUUID()
    private val goProName = "GoPro123456"
    private val goProAddress = "568676970987986"
    private val callbackSlot = slot<BluetoothGattCallback>()

    @OptIn(ExperimentalUnsignedTypes::class)
    private val value = ByteArray(1).toUByteArray()
    private val enableIndicationValue = byteArrayOf(1)
    private val enableNotificationValue = byteArrayOf(2)

    @OptIn(ExperimentalUnsignedTypes::class)
    private val bleNetworkMessage = BleNetworkMessage(1, 1, value)

    @OptIn(ExperimentalUnsignedTypes::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mutex = Mutex()
        bleManager = spyk(BleManager(bleNetworkMessageProcessorMock, bleScannerMock, mutex, buildVersionProviderMock))
        every { bluetoothDeviceMock.name } returns goProName
        every { bluetoothDeviceMock.address } returns goProAddress
        every { bluetoothDeviceMock2.name } returns "Xiaomi123456"

        every { bluetoothGattMock.getService(serviceUUID)?.getCharacteristic(characteristicUUID) } returns bluetoothCharacteristicMock
        every { bleNetworkMessageProcessorMock.processMessage(value) } just runs
        every { bleNetworkMessageProcessorMock.processSimpleMessage(value) } just runs
        every { bleNetworkMessageProcessorMock.isReceived() } returns true
        every { bleNetworkMessageProcessorMock.getPacket() } returns bleNetworkMessage

        every { bleManager invokeNoArgs "getEnableIndicationValue" } returns enableIndicationValue
        every { bleManager invokeNoArgs "getEnableNotificationValue" } returns enableNotificationValue
    }

    @After
    fun tearDown() {
        if (mutex.isLocked) {
            mutex.unlock()
        }
    }

    //region getDevicesByService
    @Test
    fun testGetDevicesByServiceReturnListOfDevicesAndKeepRunning() = runTest {
        coEvery { bleScannerMock.scanBleDevicesNearby(serviceUUID) } returns flow {
            emit(bluetoothDeviceMock)
            emit(bluetoothDeviceMock)
            emit(bluetoothDeviceMock)
            emit(bluetoothDeviceMock)
        }

        bleManager.getDevicesByService(serviceUUID).test {
            assertEquals(bluetoothDeviceMock, awaitItem())
            assertEquals(bluetoothDeviceMock, awaitItem())
            assertEquals(bluetoothDeviceMock, awaitItem())
            assertEquals(bluetoothDeviceMock, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun testGetDevicesByServiceLaunchTwice_expectException() = runTest {
        coEvery { bleScannerMock.scanBleDevicesNearby(serviceUUID) } returns flow {
            emit(bluetoothDeviceMock)
        }

        bleManager.getDevicesByService(serviceUUID)

        assertThrows(GoProException::class.java) {
            bleManager.getDevicesByService(serviceUUID)
        }
    }

    @Test
    fun testGetDevicesByServiceConsumeTwice_expectNoException() = runTest {
        coEvery { bleScannerMock.scanBleDevicesNearby(serviceUUID) } returns flow {
            emit(bluetoothDeviceMock)
        }

        bleManager.getDevicesByService(serviceUUID).test {
            assertEquals(bluetoothDeviceMock, awaitItem())
            awaitComplete()
        }

        bleManager.getDevicesByService(serviceUUID).test {
            assertEquals(bluetoothDeviceMock, awaitItem())
            awaitComplete()
        }
    }
    //endregion

    //region getPairedDevicesByPrefix
    @Test
    fun testGetPairedDevicesByPrefix() = runTest {
        every { contextMock.getSystemService(BluetoothManager::class.java) } returns bluetoothManagerMock
        every { bluetoothAdapterMock.bondedDevices } returns setOf(bluetoothDeviceMock, bluetoothDeviceMock2)

        val result = bleManager.getPairedDevicesByPrefix(contextMock, "GoPro")

        assertEquals(1, result.size)
        assertEquals(bluetoothDeviceMock, result.first())
    }

    @Test
    fun testGetPairedDevicesByPrefixReturnsEmpty_expectEmpty() = runTest {
        every { contextMock.getSystemService(BluetoothManager::class.java) } returns bluetoothManagerMock
        every { bluetoothAdapterMock.bondedDevices } returns setOf()

        val result = bleManager.getPairedDevicesByPrefix(contextMock, "GoPro")

        assertTrue(result.isEmpty())
    }

    @Test
    fun testGetPairedDevicesByPrefixNullManager_expectEmpty() = runTest {
        every { contextMock.getSystemService(BluetoothManager::class.java) } returns null
        every { bluetoothAdapterMock.bondedDevices } returns setOf(bluetoothDeviceMock, bluetoothDeviceMock2)

        val result = bleManager.getPairedDevicesByPrefix(contextMock, "GoPro")

        assertTrue(result.isEmpty())
    }

    @Test
    fun testGetPairedDevicesByPrefixNullAdapter_expectEmpty() = runTest {
        every { contextMock.getSystemService(BluetoothManager::class.java) } returns bluetoothManagerMock
        every { bluetoothManagerMock.adapter } returns null
        every { bluetoothAdapterMock.bondedDevices } returns setOf(bluetoothDeviceMock, bluetoothDeviceMock2)

        val result = bleManager.getPairedDevicesByPrefix(contextMock, "GoPro")

        assertTrue(result.isEmpty())
    }

    @Test
    fun testGetPairedDevicesByPrefixNullBoundedDevices_expectEmpty() = runTest {
        every { contextMock.getSystemService(BluetoothManager::class.java) } returns bluetoothManagerMock
        every { bluetoothAdapterMock.bondedDevices } returns null

        val result = bleManager.getPairedDevicesByPrefix(contextMock, "GoPro")

        assertTrue(result.isEmpty())
    }

    @Test
    fun testGetPairedDevicesByPrefixNoMatchingFilter_expectEmpty() = runTest {
        every { contextMock.getSystemService(BluetoothManager::class.java) } returns bluetoothManagerMock
        every { bluetoothManagerMock.adapter } returns null
        every { bluetoothAdapterMock.bondedDevices } returns setOf(bluetoothDeviceMock2)

        val result = bleManager.getPairedDevicesByPrefix(contextMock, "GoPro")

        assertTrue(result.isEmpty())
    }
    //endregion

    //region stopSearchDevices
    @Test
    fun testStopSearchDevices() = runTest {
        every { bleScannerMock.stopSearch() } just runs

        bleManager.stopSearchDevices()

        verify { bleScannerMock.stopSearch() }
    }
    //endregion


    //region connectToDevice
    @Test
    fun testConnectToDeviceNotFoundDevice_expectFalse() = runTest {
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)


        assertFalse(bleManager.connectToDevice(contextMock, "another_device_address"))
        verify(exactly = 0) { bleManager["connect"](contextMock, any<BluetoothDevice>()) }
    }

    @Test
    fun testConnectToDeviceConnectFails_expectFalse() = runTest {
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)
        coEvery { bleManager["connect"](contextMock, bluetoothDeviceMock) } returns false
        coEvery { bleManager["subscribeToNotifications"](any<List<BluetoothGattCharacteristic>>()) } returns Unit


        assertFalse(bleManager.connectToDevice(contextMock, goProAddress))
        verify(exactly = 0) { bleManager["subscribeToNotifications"](any<List<BluetoothGattCharacteristic>>()) }
    }

    @Test
    fun testConnectToDeviceSuccess_noGattServices_expectConnected() = runTest {
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)
        coEvery {
            bluetoothDeviceMock.connectGatt(
                contextMock,
                false,
                capture(callbackSlot)
            )
        } answers {
            Timer().schedule(1000) {
                callbackSlot.captured.onConnectionStateChange(bluetoothGattMock, 0, BluetoothProfile.STATE_CONNECTED)
            }
            bluetoothGattMock
        }

        coEvery {
            bluetoothGattMock.discoverServices()

        } answers {
            callbackSlot.captured.onServicesDiscovered(bluetoothGattMock, 0)
            true
        }

        assertTrue(bleManager.connectToDevice(contextMock, goProAddress))
    }

    @Test
    fun testConnectToDeviceSuccess_OneGattService_NoCharacteristics_expectConnected() = runTest {
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)
        coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)
        coEvery {
            bluetoothDeviceMock.connectGatt(
                contextMock,
                false,
                capture(callbackSlot)
            )
        } answers {
            Timer().schedule(1000) {
                callbackSlot.captured.onConnectionStateChange(bluetoothGattMock, 0, BluetoothProfile.STATE_CONNECTED)
            }
            bluetoothGattMock
        }

        coEvery {
            bluetoothGattMock.discoverServices()

        } answers {
            callbackSlot.captured.onServicesDiscovered(bluetoothGattMock, 0)
            true
        }

        assertTrue(bleManager.connectToDevice(contextMock, goProAddress))
    }

    @Test
    fun testConnectToDeviceSuccess_OneGattService_OneCharacteristics__NoSubscriptionProperties_expectConnected() = runTest {
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)
        coEvery { bluetoothGattServiceMock.characteristics } returns listOf(bluetoothCharacteristicMock)
        coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)
        coEvery {
            bluetoothDeviceMock.connectGatt(
                contextMock,
                false,
                capture(callbackSlot)
            )
        } answers {
            Timer().schedule(1000) {
                callbackSlot.captured.onConnectionStateChange(bluetoothGattMock, 0, BluetoothProfile.STATE_CONNECTED)
            }
            bluetoothGattMock
        }

        coEvery {
            bluetoothGattMock.discoverServices()

        } answers {
            callbackSlot.captured.onServicesDiscovered(bluetoothGattMock, 0)
            true
        }

        assertTrue(bleManager.connectToDevice(contextMock, goProAddress))
    }

    @Test
    fun testConnectToDeviceSuccess_OneGattService_OneCharacteristics__SubscriptionProperties_NoResponse_expectConnected() = runTest {
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)
        coEvery { bluetoothCharacteristicMock.properties } returns BluetoothGattCharacteristic.PROPERTY_INDICATE
        coEvery { bluetoothGattServiceMock.characteristics } returns listOf(bluetoothCharacteristicMock)
        coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)
        coEvery {
            bluetoothDeviceMock.connectGatt(
                contextMock,
                false,
                capture(callbackSlot)
            )
        } answers {
            Timer().schedule(1000) {
                callbackSlot.captured.onConnectionStateChange(bluetoothGattMock, 0, BluetoothProfile.STATE_CONNECTED)
            }
            bluetoothGattMock
        }

        coEvery {
            bluetoothGattMock.discoverServices()

        } answers {
            callbackSlot.captured.onServicesDiscovered(bluetoothGattMock, 0)
            true
        }

        assertTrue(bleManager.connectToDevice(contextMock, goProAddress))
    }

    @Test
    fun testConnectToDeviceSuccess_OneGattService_OneCharacteristics_Subscription_NoDescriptor_expectException() = runTest {
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)
        coEvery { bluetoothCharacteristicMock.properties } returns BluetoothGattCharacteristic.PROPERTY_NOTIFY
        coEvery { bluetoothCharacteristicMock.uuid } returns GoProUUID.CQ_COMMAND_RSP.uuid
        coEvery { bluetoothCharacteristicMock.getDescriptor(UUID.fromString(BLE_DESCRIPTION_BASE_UUID)) } returns null
        coEvery { bluetoothGattServiceMock.characteristics } returns listOf(bluetoothCharacteristicMock)
        coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)
        coEvery { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) } returns true
        coEvery {
            bluetoothDeviceMock.connectGatt(
                contextMock,
                false,
                capture(callbackSlot)
            )
        } answers {
            Timer().schedule(1000) {
                callbackSlot.captured.onConnectionStateChange(bluetoothGattMock, 0, BluetoothProfile.STATE_CONNECTED)
            }
            bluetoothGattMock
        }

        coEvery {
            bluetoothGattMock.discoverServices()

        } answers {
            callbackSlot.captured.onServicesDiscovered(bluetoothGattMock, 0)
            true
        }

        assertThrows(GoProException::class.java) {
            runBlocking {
                bleManager.connectToDevice(contextMock, goProAddress)
            }
        }
    }

    @Test
    fun testConnectToDeviceSuccess_OneGattService_OneCharacteristics_SubscriptionNotificationProperty_CQ_COMMAND_RSP_oldSdk_descriptorKO_expectException() =
        runTest {
            coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)
            coEvery { bluetoothCharacteristicMock.properties } returns BluetoothGattCharacteristic.PROPERTY_NOTIFY
            coEvery { bluetoothCharacteristicMock.uuid } returns GoProUUID.CQ_COMMAND_RSP.uuid
            coEvery { bluetoothCharacteristicMock.getDescriptor(UUID.fromString(BLE_DESCRIPTION_BASE_UUID)) } returns bluetoothGattDescriptorMock
            coEvery { bluetoothGattServiceMock.characteristics } returns listOf(bluetoothCharacteristicMock)
            coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)
            coEvery { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) } returns true
            coEvery { bluetoothGattMock.writeDescriptor(bluetoothGattDescriptorMock) } returns false
            coEvery {
                bluetoothDeviceMock.connectGatt(
                    contextMock,
                    false,
                    capture(callbackSlot)
                )
            } answers {
                Timer().schedule(1000) {
                    callbackSlot.captured.onConnectionStateChange(bluetoothGattMock, 0, BluetoothProfile.STATE_CONNECTED)
                }
                bluetoothGattMock
            }

            coEvery {
                bluetoothGattMock.discoverServices()

            } answers {
                callbackSlot.captured.onServicesDiscovered(bluetoothGattMock, 0)
                true
            }

            assertThrows(GoProException::class.java) {
                runBlocking {
                    bleManager.connectToDevice(contextMock, goProAddress)
                }
            }
        }

    @Test
    fun testConnectToDeviceSuccess_OneGattService_OneCharacteristics_SubscriptionNotificationProperty_CQ_COMMAND_RSP_oldSdk_expectConnected() =
        runTest {
            coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)
            coEvery { bluetoothCharacteristicMock.properties } returns BluetoothGattCharacteristic.PROPERTY_NOTIFY
            coEvery { bluetoothCharacteristicMock.uuid } returns GoProUUID.CQ_COMMAND_RSP.uuid
            coEvery { bluetoothCharacteristicMock.getDescriptor(UUID.fromString(BLE_DESCRIPTION_BASE_UUID)) } returns bluetoothGattDescriptorMock
            coEvery { bluetoothGattServiceMock.characteristics } returns listOf(bluetoothCharacteristicMock)
            coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)
            coEvery { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) } returns true

            coEvery {
                bluetoothDeviceMock.connectGatt(
                    contextMock,
                    false,
                    capture(callbackSlot)
                )
            } answers {
                Timer().schedule(1000) {
                    callbackSlot.captured.onConnectionStateChange(bluetoothGattMock, 0, BluetoothProfile.STATE_CONNECTED)
                }
                bluetoothGattMock
            }

            coEvery {
                bluetoothGattMock.discoverServices()

            } answers {
                callbackSlot.captured.onServicesDiscovered(bluetoothGattMock, 0)
                true
            }

            coEvery { bluetoothGattMock.writeDescriptor(bluetoothGattDescriptorMock) } answers {
                callbackSlot.captured.onDescriptorWrite(bluetoothGattMock, bluetoothGattDescriptorMock, 0)
                true
            }

            assertTrue(bleManager.connectToDevice(contextMock, goProAddress))

            verify { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) }
            verify { bluetoothGattMock.writeDescriptor(bluetoothGattDescriptorMock) }
        }

    @Test
    fun testConnectToDeviceSuccess_OneGattService_OneCharacteristics_SubscriptionNotificationProperty_CQ_COMMAND_RSP_newSdk_expectConnected() =
        runTest {
            every { buildVersionProviderMock.getSdkVersion() } returns Build.VERSION_CODES.TIRAMISU
            coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)
            coEvery { bluetoothCharacteristicMock.properties } returns BluetoothGattCharacteristic.PROPERTY_NOTIFY
            coEvery { bluetoothCharacteristicMock.uuid } returns GoProUUID.CQ_COMMAND_RSP.uuid
            coEvery { bluetoothCharacteristicMock.getDescriptor(UUID.fromString(BLE_DESCRIPTION_BASE_UUID)) } returns bluetoothGattDescriptorMock
            coEvery { bluetoothGattServiceMock.characteristics } returns listOf(bluetoothCharacteristicMock)
            coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)
            coEvery { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) } returns true

            coEvery {
                bluetoothDeviceMock.connectGatt(
                    contextMock,
                    false,
                    capture(callbackSlot)
                )
            } answers {
                Timer().schedule(1000) {
                    callbackSlot.captured.onConnectionStateChange(bluetoothGattMock, 0, BluetoothProfile.STATE_CONNECTED)
                }
                bluetoothGattMock
            }

            coEvery {
                bluetoothGattMock.discoverServices()

            } answers {
                callbackSlot.captured.onServicesDiscovered(bluetoothGattMock, 0)
                true
            }

            coEvery { bluetoothGattMock.writeDescriptor(bluetoothGattDescriptorMock, enableNotificationValue) } answers {
                callbackSlot.captured.onDescriptorWrite(bluetoothGattMock, bluetoothGattDescriptorMock, 0)
                BluetoothStatusCodes.SUCCESS
            }

            assertTrue(bleManager.connectToDevice(contextMock, goProAddress))

            verify { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) }
            verify { bluetoothGattMock.writeDescriptor(bluetoothGattDescriptorMock, enableNotificationValue) }
        }

    @Test
    fun testConnectToDeviceSuccess_OneGattService_OneCharacteristics_SubscriptionIndicationProperty_CQ_SETTING_RSP_newSdk_expectConnected() =
        runTest {
            every { buildVersionProviderMock.getSdkVersion() } returns Build.VERSION_CODES.TIRAMISU
            coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)
            coEvery { bluetoothCharacteristicMock.properties } returns BluetoothGattCharacteristic.PROPERTY_INDICATE
            coEvery { bluetoothCharacteristicMock.uuid } returns GoProUUID.CQ_COMMAND_RSP.uuid
            coEvery { bluetoothCharacteristicMock.getDescriptor(UUID.fromString(BLE_DESCRIPTION_BASE_UUID)) } returns bluetoothGattDescriptorMock
            coEvery { bluetoothGattServiceMock.characteristics } returns listOf(bluetoothCharacteristicMock)
            coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)
            coEvery { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) } returns true

            coEvery {
                bluetoothDeviceMock.connectGatt(
                    contextMock,
                    false,
                    capture(callbackSlot)
                )
            } answers {
                Timer().schedule(1000) {
                    callbackSlot.captured.onConnectionStateChange(bluetoothGattMock, 0, BluetoothProfile.STATE_CONNECTED)
                }
                bluetoothGattMock
            }

            coEvery {
                bluetoothGattMock.discoverServices()

            } answers {
                callbackSlot.captured.onServicesDiscovered(bluetoothGattMock, 0)
                true
            }

            coEvery { bluetoothGattMock.writeDescriptor(bluetoothGattDescriptorMock, enableIndicationValue) } answers {
                callbackSlot.captured.onDescriptorWrite(bluetoothGattMock, bluetoothGattDescriptorMock, 0)
                BluetoothStatusCodes.SUCCESS
            }

            assertTrue(bleManager.connectToDevice(contextMock, goProAddress))

            verify { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) }
            verify { bluetoothGattMock.writeDescriptor(bluetoothGattDescriptorMock, enableIndicationValue) }
        }

    @Test
    fun testConnectToDeviceSuccess_OneGattService_OneCharacteristics_SubscriptionIndicationProperty_CQ_SETTING_RSP_newSdk_NoDescriptionEventReceived_expectTimeOutException() =
        runTest {
            every { buildVersionProviderMock.getSdkVersion() } returns Build.VERSION_CODES.TIRAMISU
            coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)
            coEvery { bluetoothCharacteristicMock.properties } returns BluetoothGattCharacteristic.PROPERTY_INDICATE
            coEvery { bluetoothCharacteristicMock.uuid } returns GoProUUID.CQ_COMMAND_RSP.uuid
            coEvery { bluetoothCharacteristicMock.getDescriptor(UUID.fromString(BLE_DESCRIPTION_BASE_UUID)) } returns bluetoothGattDescriptorMock
            coEvery { bluetoothGattServiceMock.characteristics } returns listOf(bluetoothCharacteristicMock)
            coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)
            coEvery { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) } returns true

            coEvery {
                bluetoothDeviceMock.connectGatt(
                    contextMock,
                    false,
                    capture(callbackSlot)
                )
            } answers {
                Timer().schedule(1000) {
                    callbackSlot.captured.onConnectionStateChange(bluetoothGattMock, 0, BluetoothProfile.STATE_CONNECTED)
                }
                bluetoothGattMock
            }

            coEvery {
                bluetoothGattMock.discoverServices()

            } answers {
                callbackSlot.captured.onServicesDiscovered(bluetoothGattMock, 0)
                true
            }

            coEvery { bluetoothGattMock.writeDescriptor(bluetoothGattDescriptorMock, enableIndicationValue) } answers {
                BluetoothStatusCodes.SUCCESS
            }

            assertThrows(GoProException::class.java) {
                runBlocking {
                    bleManager.connectToDevice(contextMock, goProAddress)
                }
            }

            verify { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) }
            verify { bluetoothGattMock.writeDescriptor(bluetoothGattDescriptorMock, enableIndicationValue) }
        }

    @Test
    fun testConnectToDeviceSuccessServiceNotTriggered_expectTimeoutException() = runTest {
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)
        coEvery {
            bluetoothDeviceMock.connectGatt(
                contextMock,
                false,
                capture(callbackSlot)
            )
        } answers {
            Timer().schedule(1000) {
                callbackSlot.captured.onConnectionStateChange(bluetoothGattMock, 0, BluetoothProfile.STATE_CONNECTED)
            }
            bluetoothGattMock
        }

        assertThrows(GoProException::class.java) {
            runBlocking {
                bleManager.connectToDevice(contextMock, goProAddress)
            }
        }
    }
    //endregion

    //region sendData
    @Test
    fun testSendData_GattNotInitialized_expectException() = runTest {
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)

        assertThrows(GoProException::class.java) {
            runBlocking {
                bleManager.sendData(serviceUUID, characteristicUUID, ByteArray(1), false)
            }
        }
    }

    @Test
    fun testSendData_simpleData_sendSuccess_oldAPI_expectTrue() = runTest {
        val value = ByteArray(1)
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)

        mockConnection()
        every { bluetoothGattMock.writeCharacteristic(bluetoothCharacteristicMock) } answers {
            callbackSlot.captured.onCharacteristicRead(bluetoothGattMock, bluetoothCharacteristicMock, value, BluetoothGatt.GATT_SUCCESS)
            true
        }

        bleManager.connectToDevice(contextMock, goProAddress)
        assertEquals(bleNetworkMessage, bleManager.sendData(serviceUUID, characteristicUUID, value, false))
    }

    @Test
    fun testSendData_simpleData_sendSuccess_newAPI_expectTrue() = runTest {
        val value = ByteArray(1)
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)
        every { buildVersionProviderMock.getSdkVersion() } returns Build.VERSION_CODES.TIRAMISU

        mockConnection()
        every {
            bluetoothGattMock.writeCharacteristic(
                bluetoothCharacteristicMock,
                value,
                BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
            )
        } answers {
            callbackSlot.captured.onCharacteristicRead(bluetoothGattMock, bluetoothCharacteristicMock, value, BluetoothGatt.GATT_SUCCESS)
            BluetoothStatusCodes.SUCCESS
        }

        bleManager.connectToDevice(contextMock, goProAddress)
        assertEquals(bleNetworkMessage, bleManager.sendData(serviceUUID, characteristicUUID, value, false))
    }

    @Test
    fun testSendData_simpleData_sendFail_oldAPI_expectTrue() = runTest {
        val value = ByteArray(1)
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)

        mockConnection()
        every { bluetoothGattMock.writeCharacteristic(bluetoothCharacteristicMock) } answers {
            callbackSlot.captured.onCharacteristicRead(bluetoothGattMock, bluetoothCharacteristicMock, value, BluetoothGatt.GATT_SUCCESS)
            false
        }

        bleManager.connectToDevice(contextMock, goProAddress)
        assertThrows(GoProException::class.java) {
            runBlocking {
                bleManager.sendData(serviceUUID, characteristicUUID, value, false)
            }
        }
    }

    @Test
    fun testSendData_simpleData_sendFail_newAPI_expectTrue() = runTest {
        val value = ByteArray(1)
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)

        mockConnection()
        every {
            bluetoothGattMock.writeCharacteristic(
                bluetoothCharacteristicMock,
                value,
                BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
            )
        } answers {
            callbackSlot.captured.onCharacteristicRead(bluetoothGattMock, bluetoothCharacteristicMock, value, BluetoothGatt.GATT_SUCCESS)
            BluetoothStatusCodes.FEATURE_NOT_SUPPORTED
        }

        bleManager.connectToDevice(contextMock, goProAddress)
        assertThrows(GoProException::class.java) {
            runBlocking {
                bleManager.sendData(serviceUUID, characteristicUUID, value, false)
            }
        }
    }

    @Test
    fun testSendData_simpleData_noResponse_oldAPI_expectTimeOutException() = runTest {
        val value = ByteArray(1)
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)

        mockConnection()
        every { bluetoothGattMock.writeCharacteristic(bluetoothCharacteristicMock) } answers {
            true
        }

        bleManager.connectToDevice(contextMock, goProAddress)
        assertThrows(GoProException::class.java) {
            runBlocking {
                bleManager.sendData(serviceUUID, characteristicUUID, value, false)
            }
        }
    }

    @Test
    fun testSendData_simpleData_NullCharacteristic_oldAPI_expectTimeOutException() = runTest {
        val value = ByteArray(1)
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)
        coEvery { bluetoothGattMock.getService(serviceUUID)?.getCharacteristic(characteristicUUID) } returns null

        mockConnection()


        bleManager.connectToDevice(contextMock, goProAddress)
        assertThrows(GoProException::class.java) {
            runBlocking {
                bleManager.sendData(serviceUUID, characteristicUUID, value, false)
            }
        }
    }

    @Test
    fun testSendData_simpleData_exception_oldAPI_expectException() = runTest {
        val value = ByteArray(1)
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)

        mockConnection()
        every { bluetoothGattMock.writeCharacteristic(bluetoothCharacteristicMock) } throws Exception()

        bleManager.connectToDevice(contextMock, goProAddress)
        assertThrows(GoProException::class.java) {
            runBlocking {
                bleManager.sendData(serviceUUID, characteristicUUID, value, false)
            }
        }
    }
    //endregion

    //region readData
    @Test
    fun testReadData_GattNotInitialized_expectException() = runTest {
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)

        assertThrows(GoProException::class.java) {
            runBlocking {
                bleManager.readData(serviceUUID, characteristicUUID)
            }
        }
    }

    @Test
    fun testReadData_simpleData_sendSuccess_expectNetworkMessage() = runTest {
        val value = ByteArray(1)
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)

        mockConnection()
        every { bluetoothGattMock.readCharacteristic(bluetoothCharacteristicMock) } answers {
            callbackSlot.captured.onCharacteristicRead(bluetoothGattMock, bluetoothCharacteristicMock, value, BluetoothGatt.GATT_SUCCESS)
            true
        }

        bleManager.connectToDevice(contextMock, goProAddress)
        assertEquals(bleNetworkMessage, bleManager.readData(serviceUUID, characteristicUUID))
    }

    @Test
    fun testReadData_simpleData_sendFail_expectException() = runTest {
        val value = ByteArray(1)
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)

        mockConnection()
        every { bluetoothGattMock.readCharacteristic(bluetoothCharacteristicMock) } answers {
            callbackSlot.captured.onCharacteristicRead(bluetoothGattMock, bluetoothCharacteristicMock, value, BluetoothGatt.GATT_SUCCESS)
            false
        }

        bleManager.connectToDevice(contextMock, goProAddress)
        assertThrows(GoProException::class.java) {
            runBlocking {
                bleManager.readData(serviceUUID, characteristicUUID)
            }
        }
    }

    @Test
    fun testReadData_simpleData_noResponse_expectTimeOutException() = runTest {
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)

        mockConnection()
        every { bluetoothGattMock.readCharacteristic(bluetoothCharacteristicMock) } answers {
            true
        }

        bleManager.connectToDevice(contextMock, goProAddress)
        assertThrows(GoProException::class.java) {
            runBlocking {
                bleManager.readData(serviceUUID, characteristicUUID)
            }
        }
    }

    @Test
    fun testReadData_simpleData_NullCharacteristic_expectTimeOutException() = runTest {
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)
        coEvery { bluetoothGattMock.getService(serviceUUID)?.getCharacteristic(characteristicUUID) } returns null

        mockConnection()

        assertThrows(GoProException::class.java) {
            runBlocking {
                bleManager.readData(serviceUUID, characteristicUUID)
            }
        }
    }
    //endregion

    private fun mockConnection() {
        coEvery { bleManager getProperty "detectedDevices" } returns mutableListOf(bluetoothDeviceMock)
        coEvery {
            bluetoothDeviceMock.connectGatt(
                contextMock,
                false,
                capture(callbackSlot)
            )
        } answers {
            Timer().schedule(1000) {
                callbackSlot.captured.onConnectionStateChange(bluetoothGattMock, 0, BluetoothProfile.STATE_CONNECTED)
            }
            bluetoothGattMock
        }

        coEvery {
            bluetoothGattMock.discoverServices()

        } answers {
            callbackSlot.captured.onServicesDiscovered(bluetoothGattMock, 0)
            true
        }

    }

}