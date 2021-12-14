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
import java.io.Serializable;
import java.util.Date;

/**
 * Description: 客户表
 *
 * @lyb
 */
@ApiModel(value = "custom")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_custom_lyb")
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Custom implements Serializable {
    private static final long serialVersionUID = -7604254423496766640L;
    /**
     * 唯一标识
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "客户id", name = "id", example = "1")
    private Integer id;
    @ApiModelProperty(value = "昵称", name = "name", example = "张三")
    private String name;
    private Integer status;
    @ApiModelProperty(value = "头像地址", name = "avatar", example = "http://xxx.jpg")
    private String avatar;
    private String mobile;
    @ApiModelProperty(value = "客户openid", name = "credential", example = "xxx")
    private String credential;
    private Boolean isDel;
    private Date gmtCreate;
    private Date gmtModified;
}
