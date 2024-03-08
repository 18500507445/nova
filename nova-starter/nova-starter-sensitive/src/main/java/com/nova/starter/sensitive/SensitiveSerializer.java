package com.nova.starter.sensitive;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.nova.starter.sensitive.annotation.Sensitive;
import com.nova.starter.sensitive.utils.SensitiveUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Objects;

/**
 * @author: wzh
 * @description: SensitiveSerializer
 * @date: 2024/03/08 13:24
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public final class SensitiveSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private Sensitive.Type type;

    private int start;

    private int end;

    private int overlay;

    public SensitiveSerializer(final Sensitive sensitive) {
        this.type = sensitive.value();
        this.start = sensitive.start();
        this.end = sensitive.end();
        this.overlay = sensitive.overlay();
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (StrUtil.isEmpty(value)) {
            gen.writeString(value);
        } else {
            switch (this.type) {
                case MOBILE:
                    gen.writeString(SensitiveUtil.handlerMobile(value));
                    break;
                case ID_CARD:
                    gen.writeString(SensitiveUtil.handlerIdCard(value));
                    break;
                case BANK_CARD:
                    gen.writeString(SensitiveUtil.handlerBankCard(value));
                    break;
                case CHINESE_NAME:
                    gen.writeString(SensitiveUtil.handlerUsername(value));
                    break;
                case FIXED_PHONE:
                    gen.writeString(SensitiveUtil.handlerFixedPhone(value));
                    break;
                case ADDRESS:
                    gen.writeString(SensitiveUtil.handlerAddress(value));
                    break;
                case EMAIL:
                    gen.writeString(SensitiveUtil.handlerEmail(value));
                    break;
                case CUSTOM_HIDE:
                    gen.writeString(SensitiveUtil.hide(value, start, end));
                    break;
                case CUSTOM_RETAIN_HIDE:
                    gen.writeString(SensitiveUtil.hide(value, start, (value.length() - end)));
                    break;
                case CUSTOM_OVERLAY:
                    gen.writeString(SensitiveUtil.overlay(value, StringPool.ASTERISK, overlay, start, end));
                    break;
                default:
                    gen.writeString(value);
            }
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (Objects.isNull(property)) {
            return prov.getDefaultNullValueSerializer();
        }
        if (Objects.equals(property.getType().getRawClass(), String.class)) {
            Sensitive sensitive = property.getAnnotation(Sensitive.class);
            if (Objects.isNull(sensitive)) {
                sensitive = property.getContextAnnotation(Sensitive.class);
            }
            if (Objects.nonNull(sensitive)) {
                return new SensitiveSerializer(sensitive);
            }
        }
        return prov.findValueSerializer(property.getType(), property);
    }
}
