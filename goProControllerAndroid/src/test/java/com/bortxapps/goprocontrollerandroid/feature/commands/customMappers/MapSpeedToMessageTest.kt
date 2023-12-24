package com.bortxapps.goprocontrollerandroid.feature.commands.customMappers

import com.bortxapps.goprocontrollerandroid.domain.data.Speed
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProBleCommands
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class MapSpeedToMessageTest(
    private val speed: Speed,
    private val expectedByteArray: ByteArray
) {
    @Test
    fun `mapSpeedToMessage should map Speed to ByteArray`() {
        val actualByteArray = mapSpeedToMessage(speed)
        assertEquals(expectedByteArray, actualByteArray)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with Speed={0}, expected ByteArray={1}")
        fun data(): List<Array<Any>> {
            return listOf(
                arrayOf(Speed.REGULAR_1X, GoProBleCommands.SetSpeedNormal.byteArray),
                arrayOf(Speed.SLOW_MO_2X, GoProBleCommands.SetSpeedSlow.byteArray),
                arrayOf(Speed.SUPER_SLOW_MO_4X, GoProBleCommands.SetSpeedSuperSLowMo.byteArray),
                arrayOf(Speed.ULTRA_SLOW_MO_8X, GoProBleCommands.SetSpeedUltraSlowMo.byteArray),
                arrayOf(Speed.SLOW_MO_LONG_BATTERY_2X, GoProBleCommands.SetSpeedSlowMoLongBattery.byteArray),
                arrayOf(Speed.SUPER_SLOW_MO_LONG_BATTERY_4X, GoProBleCommands.SetSpeedSuperSlowMoLongBattery.byteArray),
                arrayOf(Speed.ULTRA_SLOW_MO_LONG_BATTERY_8X, GoProBleCommands.SetSpeedUltraSlowMoLongBattery.byteArray),
                arrayOf(Speed.SLOW_MO_2X_4K, GoProBleCommands.SetSpeedSlow4K.byteArray),
                arrayOf(Speed.SUPER_SLOW_MO_4X_17K, GoProBleCommands.SetSpeedSuperSLowMo27k.byteArray)
            )
        }
    }
}
