package com.bortxapps.goprocontrollerandroid.feature.commands.customMappers

import com.bortxapps.goprocontrollerandroid.domain.data.Speed
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProBleCommands
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class MapSpeedTest(
    private val pair: Pair<UByte, Speed>
) {
    @Test
    fun `mapSpeed should map UByte to Speed`() {
        val dataToMap = pair.first
        val expectedSpeed = pair.second
        val actualSpeed = mapSpeed(dataToMap)
        assertEquals(expectedSpeed, actualSpeed)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with data={0}, expected={1}")
        fun data(): List<Pair<UByte, Speed>> {
            return listOf(
                Pair((GoProBleCommands.SetSpeedNormal.byteArray.last()).toUByte(), Speed.REGULAR_1X),
                Pair((GoProBleCommands.SetSpeedSlow.byteArray.last()).toUByte(), Speed.SLOW_MO_2X),
                Pair((GoProBleCommands.SetSpeedSuperSLowMo.byteArray.last()).toUByte(), Speed.SUPER_SLOW_MO_4X),
                Pair((GoProBleCommands.SetSpeedUltraSlowMo.byteArray.last()).toUByte(), Speed.ULTRA_SLOW_MO_8X),
                Pair((GoProBleCommands.SetSpeedSlowMoLongBattery.byteArray.last()).toUByte(), Speed.SLOW_MO_LONG_BATTERY_2X),
                Pair((GoProBleCommands.SetSpeedSuperSlowMoLongBattery.byteArray.last()).toUByte(), Speed.SUPER_SLOW_MO_LONG_BATTERY_4X),
                Pair((GoProBleCommands.SetSpeedUltraSlowMoLongBattery.byteArray.last()).toUByte(), Speed.ULTRA_SLOW_MO_LONG_BATTERY_8X),
                Pair((GoProBleCommands.SetSpeedSlow4K.byteArray.last()).toUByte(), Speed.SLOW_MO_2X_4K),
                Pair((GoProBleCommands.SetSpeedSuperSLowMo27k.byteArray.last()).toUByte(), Speed.SUPER_SLOW_MO_4X_17K)
            )
        }
    }
}
