package com.nova.starter.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @author: wzh
 * @date: 2022/9/8 21:37
 */
@Component
public class MongoService {

    @Autowired
    @Qualifier("primaryMongoTemplate")
    private MongoTemplate primaryMongoTemplate;

    /**
     * 保存数据对象，集合为数据对象中@Document 注解所配置的collection
     *
     * @param obj 数据对象
     */
    public void save(Object obj) {
        primaryMongoTemplate.save(obj);
    }

    /**
     * 指定集合保存数据对象
     *
     * @param obj            数据对象
     * @param collectionName 集合名
     */
    public void save(Object obj, String collectionName) {
        primaryMongoTemplate.save(obj, collectionName);
    }

    /**
     * 根据数据对象中的id删除数据，集合为数据对象中@Document 注解所配置的collection
     *
     * @param obj 数据对象
     */
    public void remove(Object obj) {
        primaryMongoTemplate.remove(obj);
    }

    /**
     * 指定集合 根据数据对象中的id删除数据
     *
     * @param obj            数据对象
     * @param collectionName 集合名
     */
    public void remove(Object obj, String collectionName) {
        primaryMongoTemplate.remove(obj, collectionName);
    }

    /**
     * 根据key，value到指定集合删除数据
     *
     * @param key            键
     * @param value          值
     * @param collectionName 集合名
     */
    public void removeById(String key, Object value, String collectionName) {
        Criteria criteria = Criteria.where(key).is(value);
        criteria.and(key).is(value);
        Query query = Query.query(criteria);
        primaryMongoTemplate.remove(query, collectionName);
    }

    /**
     * 指定集合 修改数据，且仅修改找到的第一条数据
     *
     * @param accordingKey   修改条件 key
     * @param accordingValue 修改条件 value
     * @param updateKeys     修改内容 key数组
     * @param updateValues   修改内容 value数组
     * @param collectionName 集合名
     */
    public void updateFirst(String accordingKey, Object accordingValue, String[] updateKeys, Object[] updateValues, String collectionName) {
        Criteria criteria = Criteria.where(accordingKey).is(accordingValue);
        Query query = Query.query(criteria);
        Update update = new Update();
        for (int i = 0; i < updateKeys.length; i++) {
            update.set(updateKeys[i], updateValues[i]);
        }
        primaryMongoTemplate.updateFirst(query, update, collectionName);
    }

    /**
     * 指定集合 修改数据，且修改所找到的所有数据
     *
     * @param accordingKey   修改条件 key
     * @param accordingValue 修改条件 value
     * @param updateKeys     修改内容 key数组
     * @param updateValues   修改内容 value数组
     * @param collectionName 集合名
     */
    public void updateMulti(String accordingKey, Object accordingValue, String[] updateKeys, Object[] updateValues, String collectionName) {
        Criteria criteria = Criteria.where(accordingKey).is(accordingValue);
        Query query = Query.query(criteria);
        Update update = new Update();
        for (int i = 0; i < updateKeys.length; i++) {
            update.set(updateKeys[i], updateValues[i]);
        }
        primaryMongoTemplate.updateMulti(query, update, collectionName);
    }

    /**
     * 根据条件查询出所有结果集 集合为数据对象中@Document 注解所配置的collection
     *
     * @param clazz      数据对象
     * @param findKeys   查询条件 key
     * @param findValues 查询条件 value
     * @return
     */
    public <T> List<T> find(Class<T> clazz, String[] findKeys, Object[] findValues) {
        Criteria criteria = null;
        for (int i = 0; i < findKeys.length; i++) {
            if (i == 0) {
                criteria = Criteria.where(findKeys[i]).is(findValues[i]);
            } else {
                criteria.and(findKeys[i]).is(findValues[i]);
            }
        }
        Query query = Query.query(criteria);
        return primaryMongoTemplate.find(query, clazz);
    }

    /**
     * 指定集合 根据条件查询出所有结果集
     *
     * @param clazz          数据对象
     * @param findKeys       查询条件 key
     * @param findValues     查询条件 value
     * @param collectionName 集合名
     * @return
     */
    public <T> List<T> find(Class<T> clazz, String[] findKeys, Object[] findValues, String collectionName) {
        Criteria criteria = null;
        for (int i = 0; i < findKeys.length; i++) {
            if (i == 0) {
                criteria = Criteria.where(findKeys[i]).is(findValues[i]);
            } else {
                criteria.and(findKeys[i]).is(findValues[i]);
            }
        }
        assert criteria != null;
        Query query = Query.query(criteria);
        return primaryMongoTemplate.find(query, clazz, collectionName);
    }

    /**
     * 指定集合 根据条件查询出所有结果集 并排倒序
     *
     * @param clazz          数据对象
     * @param findKeys       查询条件 key
     * @param findValues     查询条件 value
     * @param collectionName 集合名
     * @param sort           排序字段
     * @return
     */
    public <T> List<T> find(Class<T> clazz, String[] findKeys, Object[] findValues, String collectionName, String sort) {
        Criteria criteria = null;
        for (int i = 0; i < findKeys.length; i++) {
            if (i == 0) {
                criteria = Criteria.where(findKeys[i]).is(findValues[i]);
            } else {
                criteria.and(findKeys[i]).is(findValues[i]);
            }
        }
        assert criteria != null;
        Query query = Query.query(criteria);
        query.with(Sort.by(Sort.Direction.DESC, sort));
        return primaryMongoTemplate.find(query, clazz, collectionName);
    }

    /**
     * 根据条件查询出符合的第一条数据 集合为数据对象中 @Document 注解所配置的collection
     *
     * @param clazz      数据对象
     * @param findKeys   查询条件 key
     * @param findValues 查询条件 value
     * @return
     */
    public <T> T findOne(Class<T> clazz, String[] findKeys, Object[] findValues) {
        Criteria criteria = null;
        for (int i = 0; i < findKeys.length; i++) {
            if (i == 0) {
                criteria = Criteria.where(findKeys[i]).is(findValues[i]);
            } else {
                criteria.and(findKeys[i]).is(findValues[i]);
            }
        }
        assert criteria != null;
        Query query = Query.query(criteria);
        return primaryMongoTemplate.findOne(query, clazz);
    }

    /**
     * 指定集合 根据条件查询出符合的第一条数据
     *
     * @param clazz          数据对象
     * @param findKeys       查询条件 key
     * @param findValues     查询条件 value
     * @param collectionName 集合名
     * @return
     */
    public <T> T findOne(Class<T> clazz, String[] findKeys, Object[] findValues, String collectionName) {
        Criteria criteria = null;
        for (int i = 0; i < findKeys.length; i++) {
            if (i == 0) {
                criteria = Criteria.where(findKeys[i]).is(findValues[i]);
            } else {
                criteria.and(findKeys[i]).is(findValues[i]);
            }
        }
        assert criteria != null;
        Query query = Query.query(criteria);
        return primaryMongoTemplate.findOne(query, clazz, collectionName);
    }

    /**
     * 根据id查询集合
     *
     * @param clazz
     * @param value
     * @param collectionName
     * @param <T>
     */
    public <T> T findById(Class<T> clazz, String value, String collectionName) {
        Criteria criteria = Criteria.where("id").is(value);
        Query query = Query.query(criteria);
        return primaryMongoTemplate.findOne(query, clazz, collectionName);
    }

    /**
     * 查询出所有结果集 集合为数据对象中 @Document 注解所配置的collection
     *
     * @param obj 数据对象
     * @return
     */
    public List<?> findAll(Object obj) {
        return primaryMongoTemplate.findAll(obj.getClass());
    }

    /**
     * 查询出所有结果集 集合为数据对象中 @Document 注解所配置的collection
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> findAll(Class<T> clazz) {
        return primaryMongoTemplate.findAll(clazz);
    }

    /**
     * 指定集合 查询出所有结果集
     *
     * @param obj            数据对象
     * @param collectionName 集合名
     * @return
     */
    public List<?> findAll(Object obj, String collectionName) {
        return primaryMongoTemplate.findAll(obj.getClass(), collectionName);
    }

    /**
     * 指定集合 查询出所有结果集
     *
     * @param clazz
     * @param collectionName
     * @param <T>
     * @return
     */
    public <T> List<T> findAll(Class<T> clazz, String collectionName) {
        return primaryMongoTemplate.findAll(clazz, collectionName);
    }
}
