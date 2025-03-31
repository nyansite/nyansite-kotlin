package cc.nyanyanya.backend.common.util

import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.MappedJdbcTypes
import org.apache.ibatis.type.MappedTypes
import org.springframework.util.StringUtils
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*


@MappedTypes(UUID::class)
@MappedJdbcTypes(JdbcType.VARCHAR)
class UuidTypeHandler : BaseTypeHandler<UUID?>() {
    @Throws(SQLException::class)
    override fun getNullableResult(rs: ResultSet, strArg: String): UUID? {
        val strVal = rs.getString(strArg)
        return this.getUuidFromString(strVal)
    }

    @Throws(SQLException::class)
    override fun getNullableResult(rs: ResultSet, intArg: Int): UUID? {
        val strVal = rs.getString(intArg)
        return this.getUuidFromString(strVal)
    }

    @Throws(SQLException::class)
    override fun getNullableResult(cs: CallableStatement, intArg: Int): UUID? {
        val strVal = cs.getString(intArg)
        return this.getUuidFromString(strVal)
    }

    @Throws(SQLException::class)
    override fun setNonNullParameter(
        ps: PreparedStatement, intArg: Int,
        uuid: UUID?,
        jdbcType: JdbcType?
    ) {
        if (null != uuid) {
            ps.setObject(intArg, uuid)
        }
    }

    private fun getUuidFromString(uuidStr: String): UUID? {
        if (StringUtils.hasLength(uuidStr)) {
            return UUID.fromString(uuidStr)
        }
        return null
    }
}