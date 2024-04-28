package com.nova.search.elasticsearch.dao;

import com.nova.search.elasticsearch.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: wzh
 * @description 接口
 * @date: 2023/07/13 22:57
 */
@Repository
public interface UserRepository extends ElasticsearchRepository<User, Integer> {

}
