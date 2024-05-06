package com.nova.search.elasticsearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: wzh
 * @description 接口
 * @date: 2023/07/13 22:57
 */
@Repository
public interface UserRepository extends ElasticsearchRepository<User, Integer> {

    //高级玩法
    Page<User> getUserByUserNameLike(String userName, Pageable page);

}
