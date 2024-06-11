package com.nova.tools.common;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONUtil;
import com.nova.tools.demo.entity.People;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: wzh
 * @description: MapStruct测试类
 * @date: 2023/12/11 10:34
 */
@Slf4j(topic = "ConvertTest")
public class ConvertTest {

    @Mapper
    public interface Convert {

        /**
         * 获取该类自动生成的实现类的实例
         * 接口中的属性都是 public static final 的 方法都是public abstract的
         */
        Convert INSTANCES = Mappers.getMapper(Convert.class);

        @Mappings({
                @Mapping(target = "ageA", source = "age"),
                @Mapping(target = "createTimeA", dateFormat = "yyyy-MM-dd", source = "createTime")
        })
        PeopleVO convertVO(People people);

        List<PeopleVO> convertVOList(List<People> list);

        @Mapping(target = "ageA", source = "age")
        default List<PeopleVO> toList(PeopleDTO peopleDTO) {
            return peopleDTO.getIdList().stream()
                    .map(id -> {
                        PeopleVO peopleVO = new PeopleVO();
                        peopleVO.setId(id);
                        peopleVO.setAgeA(peopleDTO.getAge());
                        return peopleVO;
                    })
                    .collect(Collectors.toList());
        }

        /**
         * 字段值转换，可以使用表达式
         *
         * @Mapping(target = "status", expression = "java( show.getDelFlag() == 0 ? 1 : 0 )")
         */


        // 普通转换
        Target convertTwo(People people);

        //map转bean
        Target fromMap(Map<String, String> map);

        @Mappings({
                @Mapping(source = "teacher.id", target = "id"),
                @Mapping(source = "student.age", target = "age")
        })
        Target manyToOne(Teacher teacher, Student student);

        //更新标识MappingTarget的Target实体类
        void mappingTarget(@MappingTarget Target target, Student student);

        //status转换
        @Mapping(target = "delFlag", expression = "java( teacher.getStatus() == 0 ? 1 : 0 )")
        Student status(Teacher teacher);
    }

    @Data
    @Accessors(chain = true)
    public static class PeopleVO {
        private Integer id;

        private Integer ageA;

        public String createTimeA;
    }

    //1对1转换
    @Test
    public void demoA() {
        People people = new People();
        people.setId(1).setAge(18).setCreateTime(new Date());
        PeopleVO peopleVO = Convert.INSTANCES.convertVO(people);

        String jsonStr = JSONUtil.toJsonStr(peopleVO);
        System.err.println(jsonStr);
    }

    @Data
    @Accessors(chain = true)
    public static class PeopleDTO {
        private List<Integer> idList;
        private Integer age;
    }

    //1对多转换
    @Test
    public void demoB() {
        PeopleDTO peopleDTO = new PeopleDTO();
        peopleDTO.setAge(18);
        peopleDTO.setIdList(ListUtil.of(1, 2, 3));
        List<PeopleVO> list = Convert.INSTANCES.toList(peopleDTO);

        String jsonStr = JSONUtil.toJsonStr(list);
        System.err.println(jsonStr);
    }

    @Data
    @Accessors(chain = true)
    public static class Target {
        private String id;
        private String age;
    }

    // 类型转换
    @Test
    public void demoC() {
        People people = new People();
        people.setId(1).setAge(18).setCreateTime(new Date());
        Target target = Convert.INSTANCES.convertTwo(people);
        System.err.println("target = " + JSONUtil.toJsonStr(target));
    }


    //map转bean
    @Test
    public void demoD() {
        Map<String, String> map = new HashMap<>();
        map.put("id", "1234");
        map.put("name", "Tester");

        Target target = Convert.INSTANCES.fromMap(map);
        System.err.println("target = " + JSONUtil.toJsonStr(target));
    }

    @Data
    @Accessors(chain = true)
    public static class Teacher {
        private Integer id;
        private Integer age;
        private Integer status;
    }

    @Data
    @Accessors(chain = true)
    public static class Student {
        private Integer id;
        private Integer age;
        private Integer delFlag;
    }

    //多参数映射
    @Test
    public void demoE() {
        //老师的id，学生的age
        Teacher teacher = new Teacher();
        teacher.setId(1);
        Student student = new Student();
        student.setAge(18);
        Target target = Convert.INSTANCES.manyToOne(teacher, student);
        System.err.println("target = " + JSONUtil.toJsonStr(target));
    }


    //更新现有实例
    @Test
    public void demoF() {
        Student student = new Student();
        student.setAge(18);
        student.setId(1);

        Target target = new Target();
        target.setAge("1");
        target.setId("2");

        Convert.INSTANCES.mappingTarget(target, student);
        System.err.println("target = " + JSONUtil.toJsonStr(target));
    }

    /**
     * @description: 类型转换（java表达式处理）
     * status（0，1） ==> delFlag（1，0）
     */
    @Test
    public void demoG() {
        Teacher teacher = new Teacher();
        teacher.setStatus(0);

        Student student = Convert.INSTANCES.status(teacher);
        System.err.println("student = " + JSONUtil.toJsonStr(student));
    }

}
