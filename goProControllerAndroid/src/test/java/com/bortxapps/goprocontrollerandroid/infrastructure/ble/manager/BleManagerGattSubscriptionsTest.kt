package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothStatusCodes
import android.os.Build
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.feature.commands.data.BLE_DESCRIPTION_BASE_UUID
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProUUID
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleNetworkMessageProcessor
import com.bortxapps.goprocontrollerandroid.urils.BuildVersionProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.util.UUID

class BleManagerGattSubscriptionsTest {

    private val bleNetworkMessageProcessorMock = mockk<BleNetworkMessageProcessor>(relaxed = true)
    private val bluetoothDeviceMock = mockk<BluetoothDevice>(relaxed = true)
    private val bluetoothDeviceMock2 = mockk<BluetoothDevice>(relaxed = true)

    private val bluetoothGattMock: BluetoothGatt by lazy { mockk<BluetoothGatt>(relaxed = true) }
    private val bluetoothGattServiceMock = mockk<android.bluetooth.BluetoothGattService>(relaxed = true)
    private val bluetoothCharacteristicMock = mockk<BluetoothGattCharacteristic>(relaxed = true)
    private val bleBluetoothGattDescriptorMock = mockk<BluetoothGattDescriptor>(relaxed = true)
    private val buildVersionProviderMock = mockk<BuildVersionProvider>(relaxed = true)

    private val bleManagerDeviceConnectionMock = mockk<BleManagerDeviceSearchOperations>(relaxed = true)


    private lateinit var bleManagerGattSubscriptions: BleManagerGattSubscriptions
    private lateinit var bleManagerGattCallBacks: BleManagerGattCallBacks
    private lateinit var mutex: Mutex
    private val goProName = "GoPro123456"
    private val goProAddress = "568676970987986"
    private val callbackSlot = slot<BluetoothGattCallback>()

    private val enableIndicationValue = byteArrayOf(1)
    private val enableNotificationValue = byteArrayOf(2)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mutex = Mutex()
        bleManagerGattCallBacks = spyk(BleManagerGattCallBacks(bleNetworkMessageProcessorMock))
        bleManagerGattSubscriptions = spyk(
            BleManagerGattSubscriptions(
                bleManagerGattCallBacks,
                buildVersionProviderMock,
                mutex
            )
        )
        every { bluetoothDeviceMock.name } returns goProName
        every { bluetoothDeviceMock.address } returns goProAddress
        every { bluetoothDeviceMock2.name } returns "Xiaomi123456"
        every { bleManagerGattSubscriptions invokeNoArgs "getEnableIndicationValue" } returns enableIndicationValue
        every { bleManagerGattSubscriptions invokeNoArgs "getEnableNotificationValue" } returns enableNotificationValue


        every { bluetoothDeviceMock.connectGatt(any(), any(), capture(callbackSlot)) } answers {
            bluetoothGattMock
        }

        bluetoothDeviceMock.connectGatt(null, false, bleManagerGattCallBacks)
    }

    @After
    fun tearDown() {
        if (mutex.isLocked) {
            mutex.unlock()
        }
    }

    @Test
    fun oneCharacteristics_Subscription_NoDescriptor_expectException() = runTest {
        coEvery { bluetoothCharacteristicMock.properties } returns BluetoothGattCharacteristic.PROPERTY_NOTIFY
        coEvery { bluetoothCharacteristicMock.uuid } returns GoProUUID.CQ_COMMAND_RSP.uuid
        coEvery { bluetoothCharacteristicMock.getDescriptor(UUID.fromString(BLE_DESCRIPTION_BASE_UUID)) } returns null
        coEvery { bluetoothGattServiceMock.characteristics } returns listOf(bluetoothCharacteristicMock)
        coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)
        coEvery { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) } returns true


        coEvery {
            bluetoothGattMock.writeDescriptor(bleBluetoothGattDescriptorMock, enableNotificationValue)
        } answers {
            callbackSlot.captured.onDescriptorWrite(bluetoothGattMock, bleBluetoothGattDescriptorMock, BluetoothGatt.GATT_SUCCESS)
            0
        }

        assertThrows(GoProException::class.java) {
            runBlocking {
                bleManagerGattSubscriptions.subscribeToNotifications(bluetoothGattMock)
            }
        }
    }

    @Test
    fun oneCharacteristics_SubscriptionIndicationProperty_CQ_SETTING_RSP_newSdk_NoDescriptionEventReceived_expectTimeOutException() =
        runTest {
            every { buildVersionProviderMock.getSdkVersion() } returns Build.VERSION_CODES.TIRAMISU
            coEvery { bleManagerDeviceConnectionMock.getDetectedDevices() } returns mutableListOf(bluetoothDeviceMock)
            coEvery { bluetoothCharacteristicMock.properties } returns BluetoothGattCharacteristic.PROPERTY_INDICATE
            coEvery { bluetoothCharacteristicMock.uuid } returns GoProUUID.CQ_COMMAND_RSP.uuid
            coEvery { bluetoothCharacteristicMock.getDescriptor(UUID.fromString(BLE_DESCRIPTION_BASE_UUID)) } returns bleBluetoothGattDescriptorMock
            coEvery { bluetoothGattServiceMock.characteristics } returns listOf(bluetoothCharacteristicMock)
            coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)
            coEvery { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) } returns true

            coEvery {
                bluetoothGattMock.writeDescriptor(bleBluetoothGattDescriptorMock, enableIndicationValue)
            } answers {
                0
            }

            assertThrows(GoProException::class.java) {
                runBlocking {
                    bleManagerGattSubscriptions.subscribeToNotifications(bluetoothGattMock)
                }
            }

            verify { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) }
            verify { bluetoothGattMock.writeDescriptor(bleBluetoothGattDescriptorMock, enableIndicationValue) }
        }

    @Test
    fun oneCharacteristics_SubscriptionIndicationProperty_CQ_SETTING_RSP_newSdk_DescriptorReceived_expectSuccess() = runTest {
            every { buildVersionProviderMock.getSdkVersion() } returns Build.VERSION_CODES.TIRAMISU
            coEvery { bleManagerDeviceConnectionMock.getDetectedDevices() } returns mutableListOf(bluetoothDeviceMock)
            coEvery { bluetoothCharacteristicMock.properties } returns BluetoothGattCharacteristic.PROPERTY_INDICATE
            coEvery { bluetoothCharacteristicMock.uuid } returns GoProUUID.CQ_COMMAND_RSP.uuid
            coEvery { bluetoothCharacteristicMock.getDescriptor(UUID.fromString(BLE_DESCRIPTION_BASE_UUID)) } returns bleBluetoothGattDescriptorMock
            coEvery { bluetoothGattServiceMock.characteristics } returns listOf(bluetoothCharacteristicMock)
            coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)
            coEvery { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) } returns true

            coEvery {
                bluetoothGattMock.writeDescriptor(bleBluetoothGattDescriptorMock, enableIndicationValue)
            } answers {
                callbackSlot.captured.onDescriptorWrite(bluetoothGattMock, bleBluetoothGattDescriptorMock, BluetoothGatt.GATT_SUCCESS)
                0
            }

            bleManagerGattSubscriptions.subscribeToNotifications(bluetoothGattMock)

            verify { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) }
            verify { bluetoothGattMock.writeDescriptor(bleBluetoothGattDescriptorMock, enableIndicationValue) }
        }

    @Suppress("DEPRECATION")
    @Test
    fun oneGattService_OneCharacteristics_SubscriptionNotificationProperty_CQ_COMMAND_RSP_oldSdk_expectConnected() =
        runTest {
            coEvery { bleManagerDeviceConnectionMock.getDetectedDevices() } returns mutableListOf(bluetoothDeviceMock)
            coEvery { bluetoothCharacteristicMock.properties } returns BluetoothGattCharacteristic.PROPERTY_NOTIFY
            coEvery { bluetoothCharacteristicMock.uuid } returns GoProUUID.CQ_COMMAND_RSP.uuid
            coEvery { bluetoothCharacteristicMock.getDescriptor(UUID.fromString(BLE_DESCRIPTION_BASE_UUID)) } returns bleBluetoothGattDescriptorMock
            coEvery { bluetoothGattServiceMock.characteristics } returns listOf(bluetoothCharacteristicMock)
            coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)
            coEvery { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) } returns true

            coEvery {
                bluetoothGattMock.writeDescriptor(bleBluetoothGattDescriptorMock)
            } answers {
                callbackSlot.captured.onDescriptorWrite(bluetoothGattMock, bleBluetoothGattDescriptorMock, BluetoothGatt.GATT_SUCCESS)
                true
            }

            bleManagerGattSubscriptions.subscribeToNotifications(bluetoothGattMock)

            verify { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) }
            verify { bluetoothGattMock.writeDescriptor(bleBluetoothGattDescriptorMock) }
        }

    @Test
    fun testConnectToDeviceSuccess_OneGattService_OneCharacteristics_SubscriptionNotificationProperty_CQ_COMMAND_RSP_newSdk_expectConnected() =
        runTest {
            every { buildVersionProviderMock.getSdkVersion() } returns Build.VERSION_CODES.TIRAMISU
            coEvery { bleManagerDeviceConnectionMock.getDetectedDevices() } returns mutableListOf(bluetoothDeviceMock)
            coEvery { bluetoothCharacteristicMock.properties } returns BluetoothGattCharacteristic.PROPERTY_NOTIFY
            coEvery { bluetoothCharacteristicMock.uuid } returns GoProUUID.CQ_COMMAND_RSP.uuid
            coEvery { bluetoothCharacteristicMock.getDescriptor(UUID.fromString(BLE_DESCRIPTION_BASE_UUID)) } returns bleBluetoothGattDescriptorMock
            coEvery { bluetoothGattServiceMock.characteristics } returns listOf(bluetoothCharacteristicMock)
            coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)
            coEvery { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) } returns true

            coEvery { bluetoothGattMock.writeDescriptor(bleBluetoothGattDescriptorMock, enableNotificationValue) } answers {
                callbackSlot.captured.onDescriptorWrite(bluetoothGattMock, bleBluetoothGattDescriptorMock, 0)
                BluetoothStatusCodes.SUCCESS
            }

            bleManagerGattSubscriptions.subscribeToNotifications(bluetoothGattMock)

            verify { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) }
            verify { bluetoothGattMock.writeDescriptor(bleBluetoothGattDescriptorMock, enableNotificationValue) }
        }

    @Test
    fun oneGattService_OneCharacteristics_SubscriptionIndicationProperty_CQ_SETTING_RSP_newSdk_expectConnected() =
        runTest {
            every { buildVersionProviderMock.getSdkVersion() } returns Build.VERSION_CODES.TIRAMISU
            coEvery { bleManagerDeviceConnectionMock.getDetectedDevices() } returns mutableListOf(bluetoothDeviceMock)
            coEvery { bluetoothCharacteristicMock.properties } returns BluetoothGattCharacteristic.PROPERTY_INDICATE
            coEvery { bluetoothCharacteristicMock.uuid } returns GoProUUID.CQ_COMMAND_RSP.uuid
            coEvery { bluetoothCharacteristicMock.getDescriptor(UUID.fromString(BLE_DESCRIPTION_BASE_UUID)) } returns bleBluetoothGattDescriptorMock
            coEvery { bluetoothGattServiceMock.characteristics } returns listOf(bluetoothCharacteristicMock)
            coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)
            coEvery { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) } returns true

            coEvery { bluetoothGattMock.writeDescriptor(bleBluetoothGattDescriptorMock, enableIndicationValue) } answers {
                callbackSlot.captured.onDescriptorWrite(bluetoothGattMock, bleBluetoothGattDescriptorMock, 0)
                BluetoothStatusCodes.SUCCESS
            }

            bleManagerGattSubscriptions.subscribeToNotifications(bluetoothGattMock)

            verify { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) }
            verify { bluetoothGattMock.writeDescriptor(bleBluetoothGattDescriptorMock, enableIndicationValue) }
        }

    @Suppress("DEPRECATION")
    @Test
    fun oneGattService_OneCharacteristics_SubscriptionNotificationProperty_CQ_COMMAND_RSP_oldSdk_descriptorKO_expectException() =
        runTest {
            coEvery { bleManagerDeviceConnectionMock.getDetectedDevices() } returns mutableListOf(bluetoothDeviceMock)
            coEvery { bluetoothCharacteristicMock.properties } returns BluetoothGattCharacteristic.PROPERTY_NOTIFY
            coEvery { bluetoothCharacteristicMock.uuid } returns GoProUUID.CQ_COMMAND_RSP.uuid
            coEvery { bluetoothCharacteristicMock.getDescriptor(UUID.fromString(BLE_DESCRIPTION_BASE_UUID)) } returns bleBluetoothGattDescriptorMock
            coEvery { bluetoothGattServiceMock.characteristics } returns listOf(bluetoothCharacteristicMock)
            coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)
            coEvery { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) } returns true
            coEvery { bluetoothGattMock.writeDescriptor(bleBluetoothGattDescriptorMock) } returns false

            assertThrows(GoProException::class.java) {
                runBlocking {
                    bleManagerGattSubscriptions.subscribeToNotifications(bluetoothGattMock)
                }
            }
        }

    @Test
    fun testConnectToDeviceSuccess_OneGattService_OneCharacteristics__SubscriptionProperties_NoResponse_expectConnected() = runTest {
        coEvery { bleManagerDeviceConnectionMock.getDetectedDevices() } returns mutableListOf(bluetoothDeviceMock)
        coEvery { bluetoothCharacteristicMock.properties } returns BluetoothGattCharacteristic.PROPERTY_INDICATE
        coEvery { bluetoothGattServiceMock.characteristics } returns listOf(bluetoothCharacteristicMock)
        coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)

        coEvery { bluetoothGattMock.writeDescriptor(bleBluetoothGattDescriptorMock, enableIndicationValue) } answers {
            callbackSlot.captured.onDescriptorWrite(bluetoothGattMock, bleBluetoothGattDescriptorMock, 0)
            BluetoothStatusCodes.SUCCESS
        }

        bleManagerGattSubscriptions.subscribeToNotifications(bluetoothGattMock)
    }

    @Test
    fun testConnectToDeviceSuccess_OneGattService_OneCharacteristics__NoSubscriptionProperties_expectConnected() = runTest {
        coEvery { bleManagerDeviceConnectionMock.getDetectedDevices() } returns mutableListOf(bluetoothDeviceMock)
        coEvery { bluetoothGattServiceMock.characteristics } returns listOf(bluetoothCharacteristicMock)
        coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)

        coEvery { bluetoothGattMock.writeDescriptor(bleBluetoothGattDescriptorMock, enableIndicationValue) } answers {
            callbackSlot.captured.onDescriptorWrite(bluetoothGattMock, bleBluetoothGattDescriptorMock, 0)
            BluetoothStatusCodes.SUCCESS
        }

        bleManagerGattSubscriptions.subscribeToNotifications(bluetoothGattMock)
    }

    @Test
    fun testConnectToDeviceSuccess_OneGattService_NoCharacteristics_expectConnected() = runTest {
        coEvery { bleManagerDeviceConnectionMock.getDetectedDevices() } returns mutableListOf(bluetoothDeviceMock)
        coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)

        coEvery { bluetoothGattMock.writeDescriptor(bleBluetoothGattDescriptorMock, enableIndicationValue) } answers {
            callbackSlot.captured.onDescriptorWrite(bluetoothGattMock, bleBluetoothGattDescriptorMock, 0)
            BluetoothStatusCodes.SUCCESS
        }

        bleManagerGattSubscriptions.subscribeToNotifications(bluetoothGattMock)
    }

    @Test
    fun setCharacteristicNotification_throwsException_expectException() = runTest {
        coEvery { bluetoothCharacteristicMock.properties } returns BluetoothGattCharacteristic.PROPERTY_NOTIFY
        coEvery { bluetoothCharacteristicMock.uuid } returns GoProUUID.CQ_COMMAND_RSP.uuid
        coEvery { bluetoothCharacteristicMock.getDescriptor(UUID.fromString(BLE_DESCRIPTION_BASE_UUID)) } returns null
        coEvery { bluetoothGattServiceMock.characteristics } returns listOf(bluetoothCharacteristicMock)
        coEvery { bluetoothGattMock.services } returns listOf(bluetoothGattServiceMock)
        coEvery { bluetoothGattMock.setCharacteristicNotification(bluetoothCharacteristicMock, true) } throws Exception()

        assertThrows(GoProException::class.java) {
            runBlocking {
                bleManagerGattSubscriptions.subscribeToNotifications(bluetoothGattMock)
            }
        }
    }

}