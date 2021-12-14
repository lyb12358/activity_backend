package com.beyond.serviceImpl;

import com.beyond.pojo.AppointmentProd;
import com.beyond.pojo.Product;
import com.beyond.repository.AppointmentProdRepo;
import com.beyond.repository.ProductRepo;
import com.beyond.service.ProductService;
import com.beyond.utils.NormalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @Author lyb
 * @create 12/8/21 3:13 PM
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepo prodRepo;
    @Autowired
    private AppointmentProdRepo appProdRepo;

    @Override
    public List<Product> getActivityProductList(Integer id) {
        return prodRepo.findByActivityIdAndIsDel(id, false);
    }

    @Override
    @Transactional
    public void addActivityProduct(List<Product> list) throws NormalException {
        try {
            for (Product prod : list) {
                if (null == prod.getId() && null != prod.getName() && !"".equals(prod.getName())) {
                    prod.setGmtCreate(new Date());
                    prodRepo.save(prod);
                } else if (null != prod.getId() && null != prod.getName() && !"".equals(prod.getName())) {
                    Product prod1 = prodRepo.getById(prod.getId());
                    prod1.setName(prod.getName());
                    prodRepo.save(prod1);
                } else if (null != prod.getId() && (null == prod.getName() || "".equals(prod.getName()))) {
                    Product prod1 = prodRepo.getById(prod.getId());
                    prod1.setIsDel(true);
                    prodRepo.save(prod1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NormalException("操作失败");
        }

    }

    @Override
    @Transactional
    public void addActivityVerifyProduct(List<AppointmentProd> list) throws NormalException {
        try {
            for (AppointmentProd prod : list) {
                prod.setGmtCreate(new Date());
                appProdRepo.save(prod);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NormalException("操作失败");
        }

    }

}
