package com.nova.mybatis.mapper;

import com.nova.mybatis.entity.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @description:
 * @author: wangzehui
 * @date: 2022/12/31 20:35
 */
@Mapper
public interface StudentMapper {

    @Select("select * from student where id = 1")
    Student getStudent();

    @Insert("insert into student(name, age) values('测试', 18)")
    void insertStudent();
}
