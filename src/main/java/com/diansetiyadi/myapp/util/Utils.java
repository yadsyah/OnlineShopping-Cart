package com.diansetiyadi.myapp.util;

import com.diansetiyadi.myapp.model.CartInfo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public class Utils {



    //Prorducts in CartInfo stored in Session
    public static CartInfo getCartInSession(HttpServletRequest request) {
        //Get Cart from Session
        CartInfo cartInfo = (CartInfo) request.getSession().getAttribute("myCart");
        //if null, create it
        if (cartInfo == null) {
            cartInfo = new CartInfo();
            //amd store to Session
            request.getSession().setAttribute("myCart", cartInfo);
        }
        return cartInfo;
    }

    public static void removeCartInSession(HttpServletRequest request){
        request.getSession().removeAttribute("myCart");
    }

    public static void storedLastOrderedCartInSession(HttpServletRequest request,CartInfo cartInfo){
        request.getSession().setAttribute("lastOrderedCart",cartInfo);
    }

    public static CartInfo getLastOrderedCartInSession(HttpServletRequest request){
        return (CartInfo) request.getSession().getAttribute("lastOrderedCart");
    }

}

