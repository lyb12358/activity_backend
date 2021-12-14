package com.beyond.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Description: 活动
 *
 * @lyb
 */
@ApiModel(value = "activity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_activity_lyb")
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Activity implements Serializable {
    private static final long serialVersionUID = -7604254423496766640L;
    /**
     * 唯一标识
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "活动id", name = "id", example = "1")
    private Integer id;
    @ApiModelProperty(value = "活动主标题", name = "name")
    private String name;
    @ApiModelProperty(value = "活动副标题", name = "subTitle")
    private String subTitle;
    @ApiModelProperty(value = "音乐地址", name = "music")
    private String music;
    @ApiModelProperty(value = "音乐id", name = "musicId")
    private Integer musicId;
    private Integer status;
    @ApiModelProperty(value = "门店id", name = "shopId")
    private Integer shopId;
    @ApiModelProperty(value = "创建人id", name = "userId")
    private Integer userId;
    @ApiModelProperty(value = "主题颜色", name = "theme")
    private String theme;
    @ApiModelProperty(value = "活动主图", name = "mainImage")
    private String mainImage;
    @ApiModelProperty(value = "活动详情图", name = "detailImage")
    private String detailImage;
    @ApiModelProperty(value = "虚构浏览人数", name = "fakeCount")
    private Integer fakeCount;
    @ApiModelProperty(value = "虚构预约人数", name = "fakeAppointment")
    private Integer fakeAppointment;
    @ApiModelProperty(value = "单人预约限制", name = "customLimit")
    private Integer customLimit;
    @ApiModelProperty(value = "总预约限制（暂时不用，进度条永远60%）", name = "activityLimit")
    private Integer activityLimit;
    @ApiModelProperty(value = "产品名称", name = "prodName")
    private String prodName;
    private String prodCode;
    private String prodStyle;
    private String price;
    private String prodMat;
    private String prodDesc;
    @ApiModelProperty(value = "产品主图", name = "prodImage")
    private String prodImage;
    @ApiModelProperty(value = "产品详情图", name = "prodDetailImage")
    private String prodDetailImage;
    @ApiModelProperty(value = "是否结束", name = "isDel")
    private Boolean isDel;
    private Date gmtCreate;
    private Date gmtModified;
    @ApiModelProperty(value = "结束时间", name = "gmtExpiry")
    private Date gmtExpiry;
    @Transient
    @ApiModelProperty(value = "浏览次数", name = "count")
    private Integer count;
    @Transient
    @ApiModelProperty(value = "门店名称", name = "shopName")
    private String shopName;
    @Transient
    @ApiModelProperty(value = "门店地址", name = "shopAddr")
    private String shopAddr;
    @Transient
    @ApiModelProperty(value = "联系方式", name = "shopContact")
    private String shopContact;
    @Transient
    @ApiModelProperty(value = "总预约人数", name = "totalCustom")
    private Integer totalCustom;
    @Transient
    @ApiModelProperty(value = "评论列表", name = "appointmentList")
    private List<Appointment> appointmentList;
    @Transient
    @ApiModelProperty(value = "浏览人列表", name = "customList")
    private List<Object> customList;

}
