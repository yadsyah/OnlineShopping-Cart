package com.diansetiyadi.myapp.dao;

import com.diansetiyadi.myapp.entity.ProductsEntity;
import com.diansetiyadi.myapp.model.PaginationResult;
import com.diansetiyadi.myapp.model.ProductInfo;

public interface ProductDAO {

public ProductsEntity findProduct(String code);

public ProductInfo findProductInfo(String code);

public PaginationResult<ProductInfo> queryProduct(int page,int maxResult,int maxNavigationPage);

public PaginationResult<ProductInfo> queryProducts(int page,int maxResult,int maxNavigationPage,String likeName);

public void save(ProductInfo productInfo);
}
