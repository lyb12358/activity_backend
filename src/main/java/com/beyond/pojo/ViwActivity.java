package com.beyond.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
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
@Table(name = "viw_activity_lyb")
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ViwActivity implements Serializable {
    private static final long serialVersionUID = -7604254423496766640L;
    /**
     * 唯一标识
     */
    @Id
    private Integer id;
    private String name;
    private String subTitle;
    private String musicName;
    private String musicUrl;
    private Integer musicId;
    private Integer status;
    private Integer shopId;
    private Integer userId;
    private String userName;
    private String theme;
    private String mainImage;
    private String detailImage;
    private Integer fakeCount;
    private Integer fakeAppointment;
    private Integer customLimit;
    private Integer activityLimit;
    private String prodName;
    private String prodImage;
    private String prodDetailImage;
    private Boolean isDel;
    private Date gmtCreate;
    private Date gmtModified;
    private Date gmtExpiry;
    @Transient
    private Integer count;
    @Transient
    private Integer appointmentCount;
    private String shopName;
    @Transient
    private String shopAddr;
    @Transient
    private String shopContact;
    @Transient
    private Integer totalCustom;
    @Transient
    private List<Appointment> appointmentList;
    @Transient
    private List<Custom> customList;

}
