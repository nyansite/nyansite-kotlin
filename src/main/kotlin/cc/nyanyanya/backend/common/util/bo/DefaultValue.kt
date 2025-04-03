package cc.nyanyanya.backend.common.util.bo

import org.apache.commons.lang3.time.DateUtils
import java.sql.Timestamp
import java.util.*


class DefaultValue {
    val DEFAULT_UUID = UUID(0L, 0L)
    val DEFAULT_DATE: Date = DateUtils.parseDate("0001-01-01", "yyyy-MM-dd")
    val DEFAULT_TIMESTAMP = Timestamp(
        DateUtils.parseDate("0001-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss").time
    )

    companion object {
        const val DEFAULT_BYTE = (-1).toByte()
        const val DEFAULT_SHORT = (-1).toShort()
        const val DEFAULT_INT = -1
        const val DEFAULT_LONG = -1L
    }
}