package com.nova.shopping.pay.service.pay.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.nova.shopping.common.config.redis.RedisService;
import com.nova.shopping.common.constant.Constants;
import com.nova.shopping.pay.repository.mapper.PayConfigDao;
import com.nova.shopping.pay.repository.entity.PayConfig;
import com.nova.shopping.pay.service.pay.PayConfigService;
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
public class PayConfigServiceImpl implements PayConfigService {

    private final RedisService redisService;

    private final PayConfigDao payConfigDao;

    /**
     * 查询支付配置
     *
     * @param id 支付配置主键
     * @return 支付配置
     */
    @Override
    public PayConfig selectMyPayConfigById(Long id) {
        return payConfigDao.selectMyPayConfigById(id);
    }

    /**
     * 查询支付配置列表
     *
     * @param payConfig 支付配置
     * @return 支付配置
     */
    @Override
    public List<PayConfig> selectMyPayConfigList(PayConfig payConfig) {
        return payConfigDao.selectMyPayConfigList(payConfig);
    }

    /**
     * 新增支付配置
     *
     * @param payConfig 支付配置
     * @return 结果
     */
    @Override
    public int insertMyPayConfig(PayConfig payConfig) {
        return payConfigDao.insertMyPayConfig(payConfig);
    }

    /**
     * 修改支付配置
     *
     * @param payConfig 支付配置
     * @return 结果
     */
    @Override
    public int updateMyPayConfig(PayConfig payConfig) {
        String key = Constants.REDIS_KEY + "randomConfig_" + payConfig.getSource() + "_" + payConfig.getSid() + "_" + payConfig.getPayWay();
        redisService.del(key);
        return payConfigDao.updateMyPayConfig(payConfig);
    }

    /**
     * 批量删除支付配置
     *
     * @param ids 需要删除的支付配置主键
     * @return 结果
     */
    @Override
    public int deleteMyPayConfigByIds(String ids) {
        return payConfigDao.deleteMyPayConfigByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除支付配置信息
     *
     * @param id 支付配置主键
     * @return 结果
     */
    @Override
    public int deleteMyPayConfigById(Long id) {
        return payConfigDao.deleteMyPayConfigById(id);
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
    public PayConfig getRandomConfigData(String source, String sid, String payWay) {
        String key = Constants.REDIS_KEY + "randomConfig_" + source + "_" + sid + "_" + payWay;
        PayConfig payConfig = null;
        List<PayConfig> payConfigList;
        try {
            Object o = redisService.get(key);
            if (ObjectUtil.isNotNull(o)) {
                payConfigList = JSONUtil.toList(o.toString(), PayConfig.class);
            } else {
                PayConfig query = new PayConfig();
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
                WeightRandom<PayConfig> random = WeightRandom.create();
                for (PayConfig myPayConfig : payConfigList) {
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
    public PayConfig getConfigData(Long payConfigId) {
        //获取支付配置
        String key = Constants.REDIS_KEY + "payConfig_" + payConfigId;
        PayConfig payConfig = null;
        try {
            Object o = redisService.get(key);
            if (ObjectUtil.isNotNull(o)) {
                payConfig = JSONUtil.toBean(o.toString(), PayConfig.class);
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
