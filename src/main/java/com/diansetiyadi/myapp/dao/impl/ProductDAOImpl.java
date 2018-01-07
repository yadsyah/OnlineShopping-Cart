package com.diansetiyadi.myapp.dao.impl;

import com.diansetiyadi.myapp.dao.ProductDAO;
import com.diansetiyadi.myapp.entity.ProductsEntity;
import com.diansetiyadi.myapp.model.PaginationResult;
import com.diansetiyadi.myapp.model.ProductInfo;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
public class ProductDAOImpl implements ProductDAO {

    @Autowired
    SessionFactory sessionFactory;



    public ProductsEntity findProduct(String code) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ProductsEntity.class);
        criteria.add(Restrictions.eq("code",code));
        return (ProductsEntity) criteria.uniqueResult();
    }

    public ProductInfo findProductInfo(String code) {
        ProductsEntity product = this.findProduct(code);
        if(product==null){
            return null;
        }
        return new ProductInfo(product.getCode(),product.getName(),product.getPrice());
    }

    public PaginationResult<ProductInfo> queryProduct(int page, int maxResult, int maxNavigationPage) {

        return queryProducts(page,maxResult,maxNavigationPage,null);
    }

    public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage, String likeName) {
        String sql = "Select new " + ProductInfo.class.getName() //
                + "(p.code, p.name, p.price) " + " from "//
                + ProductsEntity.class.getName() + " p ";
        if (likeName != null && likeName.length() > 0) {
            sql += " Where lower(p.name) like :likeName ";
        }
        sql += " order by p.createDate desc ";
        //
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery(sql);
        if (likeName != null && likeName.length() > 0) {
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        }
        return new PaginationResult<ProductInfo>(query, page, maxResult, maxNavigationPage);
    }

    public void save(ProductInfo productInfo) {
    String code = productInfo.getCode();

    ProductsEntity product = null;

    boolean isNew = false;
    if(code!=null){
        product = this.findProduct(code);
    }
    if(product==null){
        isNew = true;
        product = new ProductsEntity();
        product.setCreateDate(new Date());
    }
    product.setCode(code);
    product.setName(productInfo.getName());
    product.setPrice(productInfo.getPrice());

    if(productInfo.getFileData()!=null){
        byte[] image = productInfo.getFileData().getBytes();
        if(image != null && image.length > 0){
            product.setImage(image);
        }
    }
    if(isNew){
        this.sessionFactory.getCurrentSession().persist(product);
    }
    //if IN DB ERROR, will be thrown out
        this.sessionFactory.getCurrentSession().flush();
    }
}
