package com.diansetiyadi.myapp.dao.impl;

import com.diansetiyadi.myapp.dao.OrderDAO;
import com.diansetiyadi.myapp.dao.ProductDAO;
import com.diansetiyadi.myapp.entity.OrderDetailsEntity;
import com.diansetiyadi.myapp.entity.OrdersEntity;
import com.diansetiyadi.myapp.entity.ProductsEntity;
import com.diansetiyadi.myapp.model.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;
import java.util.UUID;


@Transactional
public class OrderDAOImpl implements OrderDAO {

    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    ProductDAO productDAO;


    private int getMaxOrderNumb() {
        String sql = "Select max(o.orderNum) from " + OrdersEntity.class.getName() + " o ";
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery(sql);
        Integer value = (Integer) query.uniqueResult();

        if (value == null) {
            return 0;
        }
        return value;
    }

    public void saveOrder(CartInfo cartInfo) {
        Session session = sessionFactory.getCurrentSession();

        int orderNumb = this.getMaxOrderNumb() + 1;
        OrdersEntity order = new OrdersEntity();

        order.setId(UUID.randomUUID().toString().replace("-", ""));
        order.setOrderNum(orderNumb);
        order.setOrderDate(new Date());
        order.setAmount(cartInfo.getAmountTotal());

        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        order.setCustomerName(customerInfo.getName());
        order.setCustomerAddress(customerInfo.getAddress());
        order.setCustomerEmail(customerInfo.getEmail());
        order.setCustomerPhone(customerInfo.getPhone());

        session.persist(order);

        List<CartLineInfo> lines = cartInfo.getCartLines();

        for (CartLineInfo line : lines) {
            OrderDetailsEntity orderDetail = new OrderDetailsEntity();
            orderDetail.setId(UUID.randomUUID().toString().replace("-", ""));
            orderDetail.setAmount(line.getAmount());
            orderDetail.setPrice(line.getProductInfo().getPrice());
            orderDetail.setQuanity(line.getQuantity());

            String code = line.getProductInfo().getCode();
            ProductsEntity product = this.productDAO.findProduct(code);

            session.persist(orderDetail);
        }

        cartInfo.setOrderNum(orderNumb);
    }

    public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage) {
       String sql = "Select new "+OrderInfo.class.getName() + "(ord.id, ord.orderDate, ord.orderNum, ord.amount,"
               + " ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone)" + " from " + OrdersEntity.class.getName()
               + " ord " + " order by ord.orderNum desc";

       Session session = this.sessionFactory.getCurrentSession();

       Query query = session.createQuery(sql);

        return new PaginationResult<OrderInfo>(query,page,maxResult,maxNavigationPage);
    }

    public OrdersEntity findOrder(String orderId){
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(OrdersEntity.class);

        criteria.add(Restrictions.eq("id",orderId));
        return (OrdersEntity) criteria.uniqueResult();
    }

    public OrderInfo getOrderInfo(String orderId) {
        OrdersEntity order = new OrdersEntity();

        if(order==null){
            return null;
        }

        return new OrderInfo(order.getId(),order.getOrderDate(),order.getOrderNum(),order.getAmount(),order.getCustomerName()
                ,order.getCustomerAddress(),order.getCustomerEmail(),order.getCustomerPhone());
    }

    public List<OrderDetailInfo> listOrderDetailInfos(String orderId) {
        String sql = "Select new " + OrderDetailInfo.class.getName() + "(d.id, d.product.code, d.product.name, d.quanity, d.price, d.amount)" + " from " +
                OrderDetailsEntity.class.getName() + " d " + " where d.order.id = :orderId";
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery(sql);
        query.setParameter("orderId", orderId);

        return query.list();
    }
}
