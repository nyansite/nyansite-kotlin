package cc.nyanyanya.backend.common.persistence.mapper

import cc.nyanyanya.backend.common.persistence.model.User
import com.github.yulichang.base.MPJBaseMapper
import org.apache.ibatis.annotations.Mapper

@Mapper
interface UserMapper : MPJBaseMapper<User?>