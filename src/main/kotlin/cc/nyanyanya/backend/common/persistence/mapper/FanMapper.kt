package cc.nyanyanya.backend.common.persistence.mapper

import cc.nyanyanya.backend.common.persistence.model.Fan
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.github.yulichang.base.MPJBaseMapper
import org.apache.ibatis.annotations.Mapper

@Mapper
interface FanMapper : MPJBaseMapper<Fan?>