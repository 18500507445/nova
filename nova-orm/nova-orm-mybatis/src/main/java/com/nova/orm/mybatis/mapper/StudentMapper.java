package com.nova.orm.mybatis.mapper;

import com.nova.orm.mybatis.entity.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @description:
 * @author: wzh
 * @date: 2022/12/31 20:35
 */
@Mapper
public interface StudentMapper {

    @Select("select * from student where id = #{id}")
    Student getStudent(Long id);

    @Insert("insert into student(name, age) values('事务1', 18)")
    void insertOne();

    @Insert("insert into student(name, age) values('事务2', 18)")
    void insertTwo();

    @Insert("insert into student(name, age) values('事务3', 18)")
    void insertThree();

    @Insert("insert into student(name, age) values('事务4', 18)")
    void insertFour();

    @Insert("insert into student(name, age) values('事务5', 18)")
    void insertFive();

    @Insert("insert into student(name, age) values('事务6', 18)")
    void insertSix();

    @Insert("insert into student(name, age) values('事务7', 18)")
    void insertSeven();

    @Insert("insert into student(name, age) values('insertA', 18)")
    void insertA();

    @Insert("insert into student(name, age) values('insertB', 18)")
    int insertB();

    @Insert("insert into student(name, age) values('insertC', 18)")
    void insertC();

    @MapKey("name")
    @Select("select id, name, age, create_time as createTime from student")
    Map<String,Student> selectMap();
}
