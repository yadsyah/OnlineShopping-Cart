package com.diansetiyadi.myapp.dao;

import com.diansetiyadi.myapp.model.CartInfo;
import com.diansetiyadi.myapp.model.OrderDetailInfo;
import com.diansetiyadi.myapp.model.OrderInfo;
import com.diansetiyadi.myapp.model.PaginationResult;

import java.util.List;

public interface OrderDAO {

    public void saveOrder (CartInfo cartInfo);

    public PaginationResult<OrderInfo> listOrderInfo(int page,int maxResult, int maxNavigationPage);

    public OrderInfo getOrderInfo(String orderId);

    public List<OrderDetailInfo> listOrderDetailInfos(String orderId);

}
