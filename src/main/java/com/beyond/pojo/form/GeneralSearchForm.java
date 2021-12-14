package com.beyond.pojo.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author lyb
 * @create 11/1/21 2:21 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralSearchForm implements Serializable {
    private static final long serialVersionUID = -884530103431319421L;
    private Integer page;
    private Integer row;
    private Integer UserId;
    private Integer sortFlag;
    private String name;
    private String shopName;
    private String activityName;
    private String mobile;
    private Integer type;
    private Integer status;
    private Date gmtCreateStart;
    private Date gmtCreateEnd;
    private Date gmtModifiedStart;
    private Date gmtModifiedEnd;
}
