package com.starter.mongo.entity;

/**
 * 查询的列
 *
 * @author wzh
 * @date 2023/6/13
 */
public class SelectField {

    /**
     * 集合对应的列
     */
    private String col;

    public SelectField() {
    }

    public SelectField(String col) {
        this.col = col;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }
}
