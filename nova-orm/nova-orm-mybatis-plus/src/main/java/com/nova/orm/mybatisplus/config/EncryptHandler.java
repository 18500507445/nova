package com.nova.orm.mybatisplus.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author: wzh
 * @description: 数据库字段加解密handler, JdbcType.VARCHAR：表示该处理器处理的数据类型
 * @date: 2024/01/25 16:27
 * @see <a href="https://www.cnblogs.com/zhangzhixi/p/17158797.html#_label1_0">mybatis、mybatisplus对数据库字段进行加密、脱敏</a>
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
public class EncryptHandler extends BaseTypeHandler<String> {

    /**
     * 线上运行后勿修改，会影响已加密数据解密
     */
    private static final byte[] KEYS = "mybatisPlus".getBytes(StandardCharsets.UTF_8);

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if (StrUtil.isEmpty(parameter)) {
            ps.setString(i, null);
            return;
        }
        AES aes = SecureUtil.aes(KEYS);
        String encrypt = aes.encryptHex(parameter);
        ps.setString(i, encrypt);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return decrypt(rs.getString(columnName));
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return decrypt(rs.getString(columnIndex));
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return decrypt(cs.getString(columnIndex));
    }

    public String decrypt(String value) {
        if (null == value) {
            return null;
        }
        return SecureUtil.aes(KEYS).decryptStr(value);
    }
}
