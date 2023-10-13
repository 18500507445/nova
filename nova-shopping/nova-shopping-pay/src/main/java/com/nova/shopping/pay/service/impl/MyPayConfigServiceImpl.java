package com.nova.shopping.pay.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.nova.shopping.common.config.RedisService;
import com.nova.shopping.common.constant.Constants;
import com.nova.shopping.pay.dao.MyPayConfigDao;
import com.nova.shopping.pay.entity.MyPayConfig;
import com.nova.shopping.pay.service.MyPayConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wzh
 * @description: 支付配置表(MyPayConfig)表服务实现类
 * @date 2023-04-14 19:27
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "myPayConfigService")
public class MyPayConfigServiceImpl implements MyPayConfigService {

    private final RedisService redisService;

    private final MyPayConfigDao myPayConfigDao;

    /**
     * 查询支付配置
     *
     * @param id 支付配置主键
     * @return 支付配置
     */
    @Override
    public MyPayConfig selectMyPayConfigById(Long id) {
        return myPayConfigDao.selectMyPayConfigById(id);
    }

    /**
     * 查询支付配置列表
     *
     * @param myPayConfig 支付配置
     * @return 支付配置
     */
    @Override
    public List<MyPayConfig> selectMyPayConfigList(MyPayConfig myPayConfig) {
        return myPayConfigDao.selectMyPayConfigList(myPayConfig);
    }

    /**
     * 新增支付配置
     *
     * @param myPayConfig 支付配置
     * @return 结果
     */
    @Override
    public int insertMyPayConfig(MyPayConfig myPayConfig) {
        return myPayConfigDao.insertMyPayConfig(myPayConfig);
    }

    /**
     * 修改支付配置
     *
     * @param myPayConfig 支付配置
     * @return 结果
     */
    @Override
    public int updateMyPayConfig(MyPayConfig myPayConfig) {
        String key = Constants.REDIS_KEY + "randomConfig_" + myPayConfig.getSource() + "_" + myPayConfig.getSid() + "_" + myPayConfig.getPayWay();
        redisService.del(key);
        return myPayConfigDao.updateMyPayConfig(myPayConfig);
    }

    /**
     * 批量删除支付配置
     *
     * @param ids 需要删除的支付配置主键
     * @return 结果
     */
    @Override
    public int deleteMyPayConfigByIds(String ids) {
        return myPayConfigDao.deleteMyPayConfigByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除支付配置信息
     *
     * @param id 支付配置主键
     * @return 结果
     */
    @Override
    public int deleteMyPayConfigById(Long id) {
        return myPayConfigDao.deleteMyPayConfigById(id);
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
    public MyPayConfig getRandomConfigData(String source, String sid, String payWay) {
        String key = Constants.REDIS_KEY + "randomConfig_" + source + "_" + sid + "_" + payWay;
        MyPayConfig payConfig = null;
        List<MyPayConfig> payConfigList;
        try {
            Object o = redisService.get(key);
            if (ObjectUtil.isNotNull(o)) {
                payConfigList = JSONUtil.toList(o.toString(), MyPayConfig.class);
            } else {
                MyPayConfig query = new MyPayConfig();
                query.setSource(source);
                query.setSid(sid);
                query.setPayWay(Integer.parseInt(payWay));
                query.setStatus(1);
                payConfigList = selectMyPayConfigList(query);
                if (CollUtil.isNotEmpty(payConfigList)) {
                    redisService.set(key, JSONUtil.toJsonStr(payConfigList), 60 * 30L);
                }
            }
            //进行权重随机
            if (CollUtil.isNotEmpty(payConfigList)) {
                WeightRandom<MyPayConfig> random = WeightRandom.create();
                for (MyPayConfig myPayConfig : payConfigList) {
                    random.add(myPayConfig, myPayConfig.getWeight());
                }
                payConfig = random.next();
            }
        } catch (Exception e) {
            log.error("getRandomConfig异常：{}", e.getMessage());
        }
        return payConfig;
    }

    @Override
    public MyPayConfig getConfigData(Long payConfigId) {
        //获取支付配置
        String key = Constants.REDIS_KEY + "payConfig_" + payConfigId;
        MyPayConfig payConfig = null;
        try {
            Object o = redisService.get(key);
            if (ObjectUtil.isNotNull(o)) {
                payConfig = JSONUtil.toBean(o.toString(), MyPayConfig.class);
            } else {
                payConfig = selectMyPayConfigById(payConfigId);
                if (ObjectUtil.isNotNull(payConfig)) {
                    redisService.set(key, JSONUtil.toJsonStr(payConfig), 60 * 30L);
                }
            }
        } catch (Exception e) {
            log.error("getRandomConfig异常：{}", e.getMessage());
        }
        return payConfig;
    }

}
