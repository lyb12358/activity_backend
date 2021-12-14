package com.beyond.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class TreeNode implements Serializable {

    private static final long serialVersionUID = -4775063084462955910L;
    private Integer id;
    private String label;
    private Integer status;
    private Integer depth;
    private Integer parentId;
    private Integer orderId;
    private Boolean isDel;
    private Boolean isSync;
    private Map<String, Object> attributes;
    private List<TreeNode> children;


}
