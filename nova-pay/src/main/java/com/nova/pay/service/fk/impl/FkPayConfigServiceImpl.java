package com.nova.pay.service.fk.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.nova.common.constant.Constants;
import com.nova.pay.entity.result.FkPayConfig;
import com.nova.pay.mapper.FkPayConfigMapper;
import com.nova.pay.service.fk.FkPayConfigService;
import com.nova.cache.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @description: 支付配置Service业务层处理
 * @author: wangzehui
 * @date: 2022/8/22 15:44
 */
@Slf4j
@Service
public class FkPayConfigServiceImpl implements FkPayConfigService {

    @Resource
    private RedisService redisService;

    @Autowired
    private FkPayConfigMapper fkPayConfigMapper;

    /**
     * 查询支付配置
     *
     * @param id 支付配置主键
     * @return 支付配置
     */
    @Override
    public FkPayConfig selectFkPayConfigById(Long id) {
        return fkPayConfigMapper.selectFkPayConfigById(id);
    }

    /**
     * 查询支付配置列表
     *
     * @param fkPayConfig 支付配置
     * @return 支付配置
     */
    @Override
    public List<FkPayConfig> selectFkPayConfigList(FkPayConfig fkPayConfig) {
        return fkPayConfigMapper.selectFkPayConfigList(fkPayConfig);
    }

    /**
     * 新增支付配置
     *
     * @param fkPayConfig 支付配置
     * @return 结果
     */
    @Override
    public int insertFkPayConfig(FkPayConfig fkPayConfig) {
        return fkPayConfigMapper.insertFkPayConfig(fkPayConfig);
    }

    /**
     * 修改支付配置
     *
     * @param fkPayConfig 支付配置
     * @return 结果
     */
    @Override
    public int updateFkPayConfig(FkPayConfig fkPayConfig) {
        return fkPayConfigMapper.updateFkPayConfig(fkPayConfig);
    }

    /**
     * 批量删除支付配置
     *
     * @param ids 需要删除的支付配置主键
     * @return 结果
     */
    @Override
    public int deleteFkPayConfigByIds(String ids) {
        return fkPayConfigMapper.deleteFkPayConfigByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除支付配置信息
     *
     * @param id 支付配置主键
     * @return 结果
     */
    @Override
    public int deleteFkPayConfigById(Long id) {
        return fkPayConfigMapper.deleteFkPayConfigById(id);
    }

    /**
     * 随机从缓存取一个支付配置信息
     *
     * @param source
     * @param sid
     * @param payWay
     * @return
     */
    @Override
    public FkPayConfig getRandomConfigData(String source, String sid, String payWay) {
        String key = Constants.REDIS_KEY + "randomConfig_" + source + "_" + sid + "_" + payWay;
        FkPayConfig payConfig = null;
        List<FkPayConfig> payConfigList;
        try {
            Object o = redisService.get(key);
            if (ObjectUtil.isNotNull(o)) {
                payConfigList = JSONUtil.toList(o.toString(), FkPayConfig.class);
            } else {
                FkPayConfig query = new FkPayConfig();
                query.setSource(source);
                query.setSid(sid);
                query.setPayWay(Integer.parseInt(payWay));
                query.setStatus(1);
                payConfigList = selectFkPayConfigList(query);
                if (CollUtil.isNotEmpty(payConfigList)) {
                    redisService.set(key, JSONUtil.toJsonStr(payConfigList), 60 * 5L);
                }
            }
            //进行权重随机
            if (CollUtil.isNotEmpty(payConfigList)) {
                WeightRandom<FkPayConfig> random = WeightRandom.create();
                for (FkPayConfig fkPayConfig : payConfigList) {
                    random.add(fkPayConfig,fkPayConfig.getWeight());
                }
                payConfig = random.next();
            }
        } catch (Exception e) {
            log.error("getRandomConfig异常：{}", e.getMessage());
        }
        return payConfig;
    }

    @Override
    public FkPayConfig getConfigData(Long payConfigId) {
        //获取支付配置
        String key = Constants.REDIS_KEY + "payConfig_" + payConfigId;
        FkPayConfig payConfig = null;
        try {
            Object o = redisService.get(key);
            if (o != null) {
                payConfig = JSONUtil.toBean(o.toString(), FkPayConfig.class);
            } else {
                payConfig = selectFkPayConfigById(payConfigId);
                if (ObjectUtil.isNotNull(payConfig)) {
                    redisService.set(key, JSONUtil.toJsonStr(payConfig), 60 * 5L);
                }
            }
        } catch (Exception e) {
            log.error("getRandomConfig异常：{}", e.getMessage());
        }
        return payConfig;
    }

    /**
     * 按照value的权重比例进行随机抽取
     *
     * @param map
     * @return
     */
    public static Object randomByWeight(Map<Object, Object> map) {
        Set<Map.Entry<Object, Object>> entries = map.entrySet();
        Random random = new Random();
        int sum = 0;
        for (Map.Entry<Object, Object> entry : entries) {
            int valueInt = Integer.parseInt(String.valueOf(entry.getValue()));
            sum += valueInt;
        }
        int randomInt = random.nextInt(sum) + 1;
        int sumTemp = 0;
        for (Map.Entry<Object, Object> entry : entries) {
            int valueInt = Integer.parseInt(String.valueOf(entry.getValue()));
            sumTemp += valueInt;
            if (randomInt <= sumTemp) {
                return entry.getKey();
            }
        }
        return "";
    }
}
