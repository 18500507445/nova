package com.nova.tools;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONUtil;
import com.nova.common.utils.common.LinuxUtils;
import com.nova.tools.demo.entity.People;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: wzh
 * @description: MapStruct测试类
 * @date: 2023/12/11 10:34
 */
@Slf4j(topic = "ConvertTest")
public class ConvertTest {

    @Mapper
    public interface PeopleConvert {

        /**
         * 获取该类自动生成的实现类的实例
         * 接口中的属性都是 public static final 的 方法都是public abstract的
         */
        PeopleConvert INSTANCES = Mappers.getMapper(PeopleConvert.class);

        @Mappings({
                @Mapping(source = "age", target = "ageA"),
                @Mapping(source = "createTime", target = "createTimeA", dateFormat = "yyyy-MM-dd")
        })
        PeopleVO convertVO(People people);

        List<PeopleVO> convertVOList(List<People> list);

        @Mapping(source = "age", target = "ageA")
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

    }

    @Data
    @Accessors(chain = true)
    public static class PeopleVO {
        private Integer id;

        private Integer ageA;

        public Date createTimeA;
    }

    @Data
    @Accessors(chain = true)
    public static class PeopleDTO {
        private List<Integer> idList;
        private Integer age;
    }


    /**
     * 1对1转换
     */
    @Test
    public void demoA() {
        People people = new People();
        people.setId(1).setAge(18).setCreateTime(new Date());

        PeopleVO peopleVO = PeopleConvert.INSTANCES.convertVO(people);
        String jsonStr = JSONUtil.toJsonStr(peopleVO);
        System.err.println(jsonStr);
    }


    /**
     * 1对多转换
     */
    @Test
    public void demoB() {
        PeopleDTO peopleDTO = new PeopleDTO();
        peopleDTO.setAge(18);
        peopleDTO.setIdList(ListUtil.of(1, 2, 3));

        List<PeopleVO> list = PeopleConvert.INSTANCES.toList(peopleDTO);

        String jsonStr = JSONUtil.toJsonStr(list);

        System.err.println(jsonStr);
    }

    public static void main(String[] args) {
        String s = LinuxUtils.executeLinux("git config user.name");
        System.err.println(s);
    }

}