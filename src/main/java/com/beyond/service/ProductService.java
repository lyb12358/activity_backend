package com.beyond.service;


import com.beyond.pojo.AppointmentProd;
import com.beyond.pojo.Product;

import java.util.List;

public interface ProductService {


    //DataGridResult getProductList(ProductSearchForm form);

    List<Product> getActivityProductList(Integer id);

    void addActivityProduct(List<Product> list);

    void addActivityVerifyProduct(List<AppointmentProd> list);


}
