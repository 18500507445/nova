package com.nova.excel.utils;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.data.WriteCellData;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 将时间格式转化为yyyy-MM-dd
 *
 * @author wzh
 */
public class DateConverter implements Converter<Date> {

    private static final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";

    @Override
    public Class<Date> supportJavaTypeKey() {
        return Date.class;
    }


    @Override
    public WriteCellData<String> convertToExcelData(WriteConverterContext<Date> context) {
        Date date = context.getValue();
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_YYYY_MM_DD);
        return new WriteCellData<>(sdf.format(date));
    }
}