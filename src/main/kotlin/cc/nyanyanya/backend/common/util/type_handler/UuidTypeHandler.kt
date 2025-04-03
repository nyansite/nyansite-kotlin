package cc.nyanyanya.backend.common.util.type_handler

import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.MappedJdbcTypes
import org.apache.ibatis.type.MappedTypes
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*
import java.util.UUID




@MappedTypes(UUID::class)
@MappedJdbcTypes(JdbcType.VARCHAR)
class UuidTypeHandler : BaseTypeHandler<UUID?>() {
    @Throws(SQLException::class)
    override fun setNonNullParameter(ps: PreparedStatement?, i: Int, parameter: UUID?, jdbcType: JdbcType?) {
        ps?.setObject(i, parameter)
    }

    @Throws(SQLException::class)
    override fun getNullableResult(rs: ResultSet?, columnName: String?): UUID? {
        return rs?.getObject(columnName, UUID::class.java)
    }

    @Throws(SQLException::class)
    override fun getNullableResult(rs: ResultSet?, columnIndex: Int): UUID? {
        return rs?.getObject(columnIndex, UUID::class.java)
    }

    @Throws(SQLException::class)
    override fun getNullableResult(cs: CallableStatement?, columnIndex: Int): UUID? {
        return cs?.getObject(columnIndex, UUID::class.java)
    }
}