package com.beyond.utils;

import java.io.Serializable;
import java.util.List;

public class DataGridResult implements Serializable {

    private static final long serialVersionUID = 8035022090659673900L;
    private Long total;
    private List rows;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

}
