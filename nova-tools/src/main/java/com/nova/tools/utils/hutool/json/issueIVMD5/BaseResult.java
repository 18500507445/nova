package com.nova.tools.utils.hutool.json.issueIVMD5;

import lombok.Data;

import java.util.List;

@Data
public class BaseResult<E> {

    public BaseResult() {
    }

    private int result;
    private List<E> data;
    private E data2;
    private String nextDataUri;
    private String message;
    private int dataCount;

}