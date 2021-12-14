package com.beyond.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
@ApiModel(value = "统一返回类")
public class ResponseBean<T> implements Serializable {
    private static final long serialVersionUID = -476704379868340963L;
    // 状态码
    @ApiModelProperty(value = "状态码", name = "code", example = "20000")
    private int code;

    // 返回信息
    @ApiModelProperty(value = "返回信息", name = "msg", example = "success")
    private String msg;

    // 返回的数据
    @ApiModelProperty(value = "返回数据", name = "data")
    private T data;

    public ResponseBean(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

}
