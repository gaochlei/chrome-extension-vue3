package com.gcl.navigation.mapper;

import com.gcl.navigation.domain.Dir;
import com.gcl.navigation.domain.DirExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DirMapper {
    long countByExample(DirExample example);

    int deleteByExample(DirExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Dir record);

    int insertSelective(Dir record);

    List<Dir> selectByExample(DirExample example);

    Dir selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Dir record, @Param("example") DirExample example);

    int updateByExample(@Param("record") Dir record, @Param("example") DirExample example);

    int updateByPrimaryKeySelective(Dir record);

    int updateByPrimaryKey(Dir record);
}