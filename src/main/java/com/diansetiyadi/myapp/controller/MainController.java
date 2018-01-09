package com.diansetiyadi.myapp.controller;

import com.diansetiyadi.myapp.dao.OrderDAO;
import com.diansetiyadi.myapp.dao.ProductDAO;
import com.diansetiyadi.myapp.entity.ProductsEntity;
import com.diansetiyadi.myapp.model.CartInfo;
import com.diansetiyadi.myapp.model.CustomerInfo;
import com.diansetiyadi.myapp.model.PaginationResult;
import com.diansetiyadi.myapp.model.ProductInfo;
import com.diansetiyadi.myapp.util.Utils;
import com.diansetiyadi.myapp.validator.CustomerInfoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@Transactional
@EnableWebMvc
public class MainController {


    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CustomerInfoValidator customerInfoValidator;

    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target = " + target);
        if (target.getClass() == CartInfo.class) {

        } else if (target.getClass() == CustomerInfo.class) {
            dataBinder.setValidator(customerInfoValidator);
        }
    }

    @RequestMapping("/403")
    public String accessDenied() {
        return "/403";
    }

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    //Product List Page
    @RequestMapping({"/productList"})
    public String listProductHandler(Model model, @RequestParam(value = "name", defaultValue = "") String likeName, @RequestParam(value = "page", defaultValue = "1") int page) {
        final int maxResult = 5;
        final int maxNavigationPage = 10;

        PaginationResult<ProductInfo> result = productDAO.queryProducts(page, maxResult, maxNavigationPage, likeName);

        model.addAttribute("paginationProducts", result);
        return "productList";
    }

    @RequestMapping({"/buyProduct"})
    public String listProductHandler(HttpServletRequest request, Model model, @RequestParam(value = "code", defaultValue = "") String code) {

        ProductsEntity product = null;

        if (code != null && code.length() > 0) {
            product = productDAO.findProduct(code);
        }
        if (product != null) {

            //CartInfo stored in Session
            CartInfo cartInfo = Utils.getCartInSession(request);

            ProductInfo productInfo = new ProductInfo(product);

            cartInfo.addProduct(productInfo, 1);
        }
        //Balik ke shoppingCart
        return "redirect:/shoppingCart";
    }

    @RequestMapping({"/shoppingCartRemoveProduct"})
    public String removeProductHandler(HttpServletRequest request, Model model, @RequestParam(value = "code", defaultValue = "") String code) {
        ProductsEntity product = null;

        if (code != null && code.length() > 0) {
            product = productDAO.findProduct(code);
        }
        if (product != null) {

            //Cart Info Session
            CartInfo cartInfo = Utils.getCartInSession(request);

            ProductInfo productInfo = new ProductInfo(product);

            cartInfo.removeProduct(productInfo);

        }

        return "redirect:/shoppingCart";
    }

    //POST: Update Quantity of product in Cart
    @RequestMapping(value = {"/shoppingCart"}, method = RequestMethod.POST)
    public String shoppingCartUpdateQty(HttpServletRequest request, Model model, @ModelAttribute("cartForm") CartInfo cartForm) {

        CartInfo cartInfo = Utils.getCartInSession(request);
        cartInfo.updateQuantity(cartForm);

        return "redirect:/shoppingCart";
    }

    //GET: Show Cart
    @RequestMapping(value = {"/shoppingCart"}, method = RequestMethod.GET)
    public String shoppingCarthHandler(HttpServletRequest request, Model model) {
        CartInfo myCart = Utils.getCartInSession(request);

        model.addAttribute("cartForm", myCart);
        return "shoppingCart";
    }

    //GET:Enter Customer Information
    @RequestMapping(value = {"/shoppingCartCustomer"}, method = RequestMethod.GET)
    public String shoppingCartCustomerForm(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);

        if (cartInfo.isEmpty()) {
            return "redirect:/shoppingCart";
        }
        CustomerInfo customerInfo = cartInfo.getCustomerInfo();

        if (customerInfo == null) {
            customerInfo = new CustomerInfo();

        }
        model.addAttribute("customerForm", customerInfo);

        return "shoppingCartCustomer";
    }

    //POST:Save Customer Information
    @RequestMapping(value = {"/shoppingCartCustomer"}, method = RequestMethod.POST)
    public String shoppingCartCustomerSave(HttpServletRequest request, Model model,
                                           @ModelAttribute("customerForm") @Validated CustomerInfo customerForm, BindingResult bindingResult, final RedirectAttributes redirectAttributes) {


        if (bindingResult.hasErrors()) {
            customerForm.setValid(false);

            return "shoppingCartCustomer";
        }

        customerForm.setValid(true);
        CartInfo cartInfo = Utils.getCartInSession(request);

        cartInfo.setCustomerInfo(customerForm);

        return "redirect:/shoppingCartConfirmation";

    }

    //GET: Review Cart to Confirmation
    @RequestMapping(value = {"/shoppingCartConfirmation"}, method = RequestMethod.GET)
    public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);

        if (cartInfo.isEmpty()) {
            return "redirect:/shoppingCart";
        } else if (!cartInfo.isValidCustomer()) {
            return "redirect:/shoppingCartCustomer";
        }
        return "shoppingCartConfirmation";

    }
    //POST: Send Cart (SAVE)
    @RequestMapping(value = {"/shoppingCartConfirmation"}, method = RequestMethod.POST)
    //Avoid Unexpected RollBack Exceptiion
    @Transactional(propagation = Propagation.NEVER)
    public String shoppingCartConfirmationSave(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);

        if (cartInfo.isEmpty()) {

            return "redirect:/shoppingCart";
        } else if (!cartInfo.isValidCustomer()) {
            return "redirect:/shoppingCartCustomer";
        }
        try {
            orderDAO.saveOrder(cartInfo);
        } catch (Exception e) {
            return "shoppingCartConfirmation";
        }
        //Remove Cart in session
        Utils.removeCartInSession(request);
        //Store Last Order In Session
        Utils.storedLastOrderedCartInSession(request,cartInfo);

        //Redirect Successful page
        return "redirect:/shoppingCartFinalize";

    }

    @RequestMapping(value = {"/shoppingCartFinalize"},method = RequestMethod.GET)
    public String shoppingCartFinalize(HttpServletRequest request,Model model){

        CartInfo lastOrderedCart = Utils.getLastOrderedCartInSession(request);

        if(lastOrderedCart==null){
            return "redirect:/shoppingCart";
        }
        return "shoppingCartFinalize";
    }

    @RequestMapping(value = {"/productImage"},method = RequestMethod.GET)
    public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,@RequestParam("code")String code) throws IOException {
        ProductsEntity product = null;
        if(code!=null){
            product = productDAO.findProduct(code);
        }
        if(product!=null && product.getImage()!=null){
            response.setContentType("image/jpeg,image/jpg,image/png,image/gif");
            response.getOutputStream().write(product.getImage());
        }
        response.getOutputStream().close();
    }
}
