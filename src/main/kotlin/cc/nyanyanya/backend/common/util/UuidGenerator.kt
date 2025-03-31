package cc.nyanyanya.backend.common.util

import com.fasterxml.uuid.Generators
import java.util.*

object UuidGenerator {
    fun genUuidV4(): UUID {
        return Generators.randomBasedGenerator().generate()
    }

    fun genUuidV7(): UUID {
        return Generators.timeBasedEpochRandomGenerator().generate()
    }
}