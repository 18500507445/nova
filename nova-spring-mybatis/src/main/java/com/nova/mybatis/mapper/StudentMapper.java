package com.nova.mybatis.mapper;

import com.nova.mybatis.entity.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @description:
 * @author: wzh
 * @date: 2022/12/31 20:35
 */
@Mapper
public interface StudentMapper {

    @Select("select * from student where id = 1")
    Student getStudent();

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
}
