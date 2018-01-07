package com.diansetiyadi.myapp.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "orders", schema = "shoppingcart")
public class OrdersEntity implements Serializable {
    private String id;
    private double amount;
    private String customerAddress;
    private String customerEmail;
    private String customerName;
    private String customerPhone;
    private Timestamp orderDate;
    private int orderNum;
    private Collection<OrderDetailsEntity> orderDetailsById;

    @Id
    @Column(name = "ID", nullable = false, length = 50)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Amount", nullable = false, precision = 0)
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "Customer_Address", nullable = false, length = 255)
    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    @Basic
    @Column(name = "Customer_Email", nullable = false, length = 128)
    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    @Basic
    @Column(name = "Customer_Name", nullable = false, length = 255)
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Basic
    @Column(name = "Customer_Phone", nullable = false, length = 128)
    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    @Basic
    @Column(name = "Order_Date", nullable = false)
    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Basic
    @Column(name = "Order_Num", nullable = false)
    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrdersEntity that = (OrdersEntity) o;

        if (Double.compare(that.amount, amount) != 0) return false;
        if (orderNum != that.orderNum) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (customerAddress != null ? !customerAddress.equals(that.customerAddress) : that.customerAddress != null)
            return false;
        if (customerEmail != null ? !customerEmail.equals(that.customerEmail) : that.customerEmail != null)
            return false;
        if (customerName != null ? !customerName.equals(that.customerName) : that.customerName != null) return false;
        if (customerPhone != null ? !customerPhone.equals(that.customerPhone) : that.customerPhone != null)
            return false;
        if (orderDate != null ? !orderDate.equals(that.orderDate) : that.orderDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (customerAddress != null ? customerAddress.hashCode() : 0);
        result = 31 * result + (customerEmail != null ? customerEmail.hashCode() : 0);
        result = 31 * result + (customerName != null ? customerName.hashCode() : 0);
        result = 31 * result + (customerPhone != null ? customerPhone.hashCode() : 0);
        result = 31 * result + (orderDate != null ? orderDate.hashCode() : 0);
        result = 31 * result + orderNum;
        return result;
    }

    @OneToMany(mappedBy = "ordersByOrderId")
    public Collection<OrderDetailsEntity> getOrderDetailsById() {
        return orderDetailsById;
    }

    public void setOrderDetailsById(Collection<OrderDetailsEntity> orderDetailsById) {
        this.orderDetailsById = orderDetailsById;
    }
}
