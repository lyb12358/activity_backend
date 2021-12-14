package com.beyond.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Description: 预约表
 *
 * @lyb
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "viw_appointment_lyb")
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ViwAppointment implements Serializable {
    private static final long serialVersionUID = -7604254423496766640L;
    /**
     * 唯一标识
     */
    @Id
    @Excel(name = "id", isColumnHidden = true)
    private Integer id;
    @Excel(name = "activityId", isColumnHidden = true)
    private Integer activityId;
    @Excel(name = "shopId", isColumnHidden = true)
    private Integer shopId;
    @Excel(name = "门店名称")
    private String shopName;
    @Excel(name = "活动名称")
    private String activityName;
    @Excel(name = "customId", isColumnHidden = true)
    private Integer customId;
    @Excel(name = "userId", isColumnHidden = true)
    private Integer userId;
    @Excel(name = "客户昵称")
    private String customName;
    @Excel(name = "手机号")
    private String mobile;
    @Excel(name = "customAvatar", isColumnHidden = true)
    private String customAvatar;
    @Excel(name = "code", isColumnHidden = true)
    private String code;
    @Excel(name = "comment", isColumnHidden = true)
    private String comment;
    @Excel(name = "isVerified", replace = {"未核销_0", "已核销_1"})
    private Boolean isVerified;
    @Excel(name = "核销人名称")
    private String verifiedUserName;
    @Excel(name = "verifiedUser", isColumnHidden = true)
    private Integer verifiedUser;
    @Excel(name = "核销日期")
    private Date gmtVerified;
    @Excel(name = "预约日期")
    private Date gmtCreate;
    @Excel(name = "gmtModified", isColumnHidden = true)
    private Date gmtModified;
}
