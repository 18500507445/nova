package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.AliPayUtil;
import utils.WeChatPayUtil;

import javax.annotation.Resource;

/**
 * @Description: 支付controller
 * @Author: wangzehui
 * @Date: 2022/6/18 21:45
 */
@RestController
@RequestMapping("/api/pay/")
public class PayController {

    private static final Logger logger = LoggerFactory.getLogger(PayController.class);

    @Resource
    private AliPayUtil aliPayUtil;

    @Resource
    private WeChatPayUtil weChatPayUtil;


}
