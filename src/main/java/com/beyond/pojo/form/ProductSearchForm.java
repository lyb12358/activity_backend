package com.beyond.pojo.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author lyb
 * @create 2019-08-01 14:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchForm implements Serializable {
    private static final long serialVersionUID = -884530103431319421L;
    private Integer page;
    private Integer row;
    private Integer orderId;
    private Integer speId;
    private Integer type;
    private Integer priceZoneId;
    private Integer reviewSeasonId;
    private Integer middleTypeId;
    private Integer sortFlag;
    private String name;
    private String middleName;
    private Date gmtCreateStart;
    private Date gmtCreateEnd;
    private Date gmtModifiedStart;
    private Date gmtModifiedEnd;
}
