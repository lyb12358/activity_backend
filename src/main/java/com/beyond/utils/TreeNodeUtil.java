package com.beyond.utils;

import java.util.ArrayList;
import java.util.List;

public class TreeNodeUtil {
    public static List<TreeNode> tree(List<TreeNode> nodes, Integer id) {
        // 递归转化为树形
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        for (TreeNode treeNode : nodes) {
            TreeNode node = new TreeNode();
            node.setId(treeNode.getId());
            node.setLabel(treeNode.getLabel());
            node.setAttributes(treeNode.getAttributes());
            node.setStatus(treeNode.getStatus());
            node.setDepth(treeNode.getDepth());
            node.setParentId(treeNode.getParentId());
            node.setIsDel(treeNode.getIsDel());
            node.setIsSync(treeNode.getIsSync());
            if (id.equals(treeNode.getParentId())) {
                node.setChildren(tree(nodes, node.getId()));
                treeNodes.add(node);
            }

        }
        return treeNodes;
    }

}
