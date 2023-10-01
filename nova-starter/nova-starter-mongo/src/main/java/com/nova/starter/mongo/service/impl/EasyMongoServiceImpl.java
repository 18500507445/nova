package com.nova.starter.mongo.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.nova.starter.mongo.entity.Page;
import com.nova.starter.mongo.service.EasyMongoService;
import com.nova.starter.mongo.utils.ClassUtil;
import com.nova.starter.mongo.utils.QueryBuildUtils;
import com.nova.starter.mongo.wrapper.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 默认的服务实现类
 *
 * @author wzh
 * @date 2023/6/13
 */
@SuppressWarnings("all")
public class EasyMongoServiceImpl<T> implements EasyMongoService<T> {

    /**
     * 服务类对应的mongo实体类
     */
    private final Class<T> targetClass = (Class<T>) ClassUtil.getTClass(this);

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Override
    public T getOne(LambdaQueryWrapper<T> queryWrapper) {
        Query query = QueryBuildUtils.buildQuery(queryWrapper);
        return mongoTemplate.findOne(query, targetClass);

    }

    @Override
    public boolean save(T entity) {
        return Objects.nonNull(mongoTemplate.save(entity));
    }

    @Override
    public boolean saveBatch(Collection<T> entityList) {

        entityList.forEach(item -> mongoTemplate.save(item));
        return true;

    }

    @Override
    public boolean removeById(Serializable id) {

        Criteria criteria = Criteria.where("_id").is(id);
        Query query = new Query(criteria);
        DeleteResult deleteResult = mongoTemplate.remove(query, targetClass);
        return deleteResult.getDeletedCount() > 0;

    }

    @Override
    public boolean remove(LambdaQueryWrapper<T> queryWrapper) {

        Query query = QueryBuildUtils.buildQuery(queryWrapper);
        DeleteResult remove = mongoTemplate.remove(query, targetClass);
        return remove.getDeletedCount() > 0;

    }

    @Override
    public boolean updateById(T entity) {

        Criteria criteria = Criteria.where("_id").is(ClassUtil.getId(entity));
        Query query = new Query(criteria);
        Update update = getUpdate(entity);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, targetClass);
        return updateResult.getModifiedCount() > 0;

    }

    /**
     * 通过反射获取需要更新的字段
     */
    private Update getUpdate(T entity) {

        Update update = new Update();
        for (Field field : entity.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object result = field.get(entity);
                if (Objects.nonNull(result)) {
                    update.set(field.getName(), result);
                }
            } catch (Exception ignore) {
            }
        }
        return update;

    }

    @Override
    public boolean update(T entity, LambdaQueryWrapper<T> queryWrapper) {

        Query query = QueryBuildUtils.buildQuery(queryWrapper);
        Update update = getUpdate(entity);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, targetClass);
        return updateResult.getModifiedCount() > 0;

    }

    @Override
    public T getById(Serializable id) {

        Criteria criteria = Criteria.where("_id").is(id);
        Query query = new Query(criteria);
        return mongoTemplate.findOne(query, targetClass);

    }

    @Override
    public Collection<T> listByIds(Collection<? extends Serializable> idList) {

        Criteria criteria = Criteria.where("_id").in(idList);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, targetClass);

    }

    @Override
    public long count(LambdaQueryWrapper<T> queryWrapper) {

        Query query = QueryBuildUtils.buildQuery(queryWrapper);
        return mongoTemplate.count(query, targetClass);

    }

    @Override
    public List<T> list(LambdaQueryWrapper<T> queryWrapper) {

        Query query = QueryBuildUtils.buildQuery(queryWrapper);
        return mongoTemplate.find(query, targetClass);

    }

    @Override
    public Page<T> page(LambdaQueryWrapper<T> queryWrapper, int pageNo, int pageSize) {
        Query query = QueryBuildUtils.buildQuery(queryWrapper);
        Page<T> page = new Page<>();
        page.setPageSize(pageSize);
        page.setPageNum(pageNo);
        long total = mongoTemplate.count(query, targetClass);
        page.setTotal(total);
        if (total <= 0) {
            return page;
        }
        query.skip((long) (pageNo - 1) * pageSize).limit(pageSize);
        List<T> list = mongoTemplate.find(query, targetClass);
        page.setRecords(list);
        return page;

    }

    @Override
    public boolean exist(LambdaQueryWrapper<T> queryWrapper) {
        Query query = QueryBuildUtils.buildQuery(queryWrapper);
        return mongoTemplate.exists(query, targetClass);
    }

}
