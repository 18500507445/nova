package com.nova.mybatisplus.chapter4;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nova.mybatisplus.entity.UserActiveRecordDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: wzh
 * @description 领域模型mapper
 * @date: 2023/06/21 09:48
 */
@Mapper
public interface UserActiveRecordMapper extends BaseMapper<UserActiveRecordDO> {

}
