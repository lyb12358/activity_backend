package com.beyond.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectOption<T> implements Serializable {

    private static final long serialVersionUID = 1469718220350429747L;
    private String label;
    private Integer value;
    private Integer parentId;
    private String image;
    private String avatar;
    private String name;
    private String url;
    private String icon;
    private Integer status;
    private Boolean isSync;
    private Boolean isDel;
    private Integer orderId;
    private T data;

}
