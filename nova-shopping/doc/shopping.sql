create table if not exists shopping.my_goods
(
    id          bigint auto_increment
        primary key,
    name        varchar(64)                              null comment '用户名',
    price       decimal(10, 2) default 0.00              not null comment '金额',
    total       int(8)                                   null comment '总数',
    stock       int(8)                                   null comment '库存',
    status      tinyint(2)     default 0                 not null comment '售卖状态：0售卖，1停售',
    remark      varchar(64)                              null comment '备注',
    operator    varchar(64)                              null comment '操作人',
    update_time datetime       default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    create_time datetime       default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '商品表';

create table if not exists shopping.my_order
(
    id              bigint auto_increment
        primary key,
    user_id         bigint                           null comment '用户id',
    goods_id        bigint default 0                 not null comment '商品id',
    price           decimal(10, 2)                       null comment '价格',
    status          tinyint(2) default 0                 not null comment '订单状态：0默认，1成功，2已发货，3失败，4退款，5过期',
    pay_status      tinyint(2)                           null comment '支付状态：0默认，1成功，2处理中，3失败，4退款',
    expiration_time datetime   default CURRENT_TIMESTAMP not null comment '过期时间',
    remark          varchar(64)                          null comment '备注',
    operator        varchar(64)                          null comment '操作人',
    create_time     datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间'
)
    comment '订单表';

create table if not exists shopping.my_pay_config
(
    id                bigint auto_increment
        primary key,
    source            varchar(128)                       null,
    sid               varchar(4096)                      null comment '渠道号',
    pay_way           tinyint(2)                         null comment '1支付宝 2微信 3苹果 4易宝 5谷歌支付 6快手支付 7华为支付 99兑换 ',
    app_id            varchar(32)                        null comment '微信appId/支付宝appId/易宝appKey/华为client_id',
    app_secret        varchar(255)                       null comment '微信应用秘钥/华为client_secret',
    mch_id            varchar(32)                        null comment '微信商户号/易宝商户编号/谷歌应用名称',
    pay_secret        varchar(255)                       null comment '微信支付秘钥',
    serial_no         varchar(64)                        null comment '证书号',
    api_v3_key        varchar(64)                        null comment '商户支付v3key，有值微信v3支付，空就是v2支付',
    public_key        varchar(512)                       null comment '支付宝公钥/易宝公钥',
    private_key       varchar(2048)                      null comment '支付宝应用私钥/易宝平台私钥',
    key_path          varchar(128)                       null comment '微信p12证书地址/支付宝appCertPublicKey.crt/谷歌p12证书
',
    private_key_path  varchar(128)                       null comment '微信v3支付apiclient_key.pem证书地址/支付宝alipayCertPublicKey_RSA2.crt证书地址',
    private_cert_path varchar(128)                       null comment '微信v3支付apiclient_cert.pem证书地址/支付宝alipayRootCert.crt证书地址',
    notify_url        varchar(64)                        null comment '通知地址',
    weight            int(8)   default 1                 not null comment '权重',
    status            int(1)   default 0                 null comment '是否生效  0不生效 1生效  -1 作废',
    remark            varchar(128)                       null comment '备注',
    operator          varchar(64)                        null comment '操作人',
    create_time       datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间'
)
    comment '支付配置表';

create index PAY_CONFIG_INDEX01
    on shopping.my_pay_config (source);

create table if not exists shopping.my_pay_list
(
    id               bigint auto_increment
        primary key,
    source           varchar(64)                          null,
    sid              varchar(64)                          not null comment '渠道号',
    ali_h5           tinyint(2) default 0                 not null comment '支付宝h5 0关闭 1显示 备注：支付宝h5和app都走h5支付',
    ali_app          tinyint(2) default 0                 not null comment '支付宝app 0关闭 1显示',
    ali_applet       tinyint(2) default 0                 not null comment '支付宝小程序 0关闭 1显示',
    ali_logo_url     varchar(128)                         null comment '支付宝logo',
    wechat_h5        tinyint(2) default 0                 not null comment '微信h5 0关闭 1显示',
    wechat_app       tinyint(2) default 0                 not null comment '微信客户端 0关闭 1显示',
    wechat_jsapi     tinyint(2) default 0                 not null comment '微信内原生、小程序 0关闭 1显示',
    wechat_logo_url  varchar(128)                         null comment '微信logo',
    apple            tinyint(2) default 0                 not null comment '苹果支付 0关闭 1显示',
    apple_logo_url   varchar(128)                         null comment '苹果logo',
    yee_pay_quick    tinyint(2) default 0                 not null comment '易宝快捷 0关闭 1显示',
    yee_pay_wallet   tinyint(2) default 0                 not null comment '易宝钱包 0关闭 1显示',
    yee_pay_logo_url varchar(128)                         null comment '易宝logo',
    google_pay       tinyint(2) default 0                 not null comment '谷歌支付',
    google_logo_url  varchar(128)                         null comment '谷歌支付logo',
    operator         varchar(64)                          null comment '操作人',
    remark           varchar(255)                         null comment '备注',
    create_time      datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_time      datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间'
)
    comment '支付列表';

create index PAY_LIST_INDEX01
    on shopping.my_pay_list (source);

create index PAY_LIST_INDEX02
    on shopping.my_pay_list (sid);

create index UNION_KEY
    on shopping.my_pay_list (source, sid);

create table if not exists shopping.my_pay_order
(
    id            bigint auto_increment
        primary key,
    source        varchar(64)                              null,
    sid           varchar(64)                              null comment '渠道号',
    pay_config_id bigint                               null comment '支付配置表id',
    product_id    varchar(64)                              null comment '产品id',
    user_name     varchar(64)                              null comment '用户名',
    order_id      varchar(64)                              not null comment '订单id、聚合订单id',
    trade_no      varchar(64)                              null comment '交易订单号 支付宝同字段、微信和苹果：transaction_id',
    trade_status  tinyint(2)     default 0                 not null comment '0支付中，1成功，2失败，3退款， 4处理中，5关单',
    fee           decimal(10, 2) default 0.00              null comment '金额',
    type          tinyint(2)                               not null comment '1h5 2小程序 3app 4微信原生jsapi 5沙盒 6钱包 7快捷 8球币兑换 9微信(四方支付) 10支付宝(四方支付) 11扫码(微信、支付宝)',
    pay_way       tinyint(2)                               not null comment '1支付宝 2微信 3苹果 4yeePay 5谷歌 6快手 7华为支付 99金币兑换',
    currency_type varchar(16)    default 'CNY'             not null comment '货币种类   CNY：人民币,USD：美元,HKD：港币,JPY：日元,GBP：英镑,EUR：欧元',
    sign          blob                                     null comment '苹果支付 sign、易宝支付token、快手的小程序平台订单号',
    business_code tinyint(2)     default 0                 not null comment '充当开通业务code，例如支付成功通知后，发送mq自动处理',
    remark        varchar(64)                              null comment '备注
1.易宝1级收款商户
2.易宝2级默认收款方式 银行卡、钱包
3.苹果支付验签结果
4.快手是否结算',
    operator      varchar(64)                              null comment '操作人
备注：快手存入用户openId',
    update_time   datetime       default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    create_time   datetime       default CURRENT_TIMESTAMP not null comment '创建时间',
    constraint UNION_KEY
        unique (order_id, pay_way) comment '订单id、支付方式联合索引'
)
    comment '支付订单表明细表';

create index PAY_ORDER_INDEX01
    on shopping.my_pay_order (trade_status);

create index PAY_ORDER_INDEX02
    on shopping.my_pay_order (user_name);

create index PAY_ORDER_INDEX03
    on shopping.my_pay_order (trade_no);

create index PAY_ORDER_INDEX04
    on shopping.my_pay_order (create_time);

create table if not exists shopping.my_seckill_order
(
    id          bigint auto_increment
        primary key,
    user_id     bigint                         null comment '用户id',
    goods_id    bigint                         null comment '商品id',
    order_id    bigint                         null comment '订单id',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '秒杀订单表';

create table if not exists shopping.my_user
(
    id          bigint auto_increment
        primary key,
    name        varchar(64)                          null comment '用户名',
    status      tinyint(2) default 0                 not null comment '用户状态：0正常，1封号',
    remark      varchar(64)                          null comment '备注',
    operator    varchar(64)                          null comment '操作人',
    update_time datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    create_time datetime   default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '用户表';

