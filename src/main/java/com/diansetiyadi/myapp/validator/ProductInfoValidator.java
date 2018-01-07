package com.diansetiyadi.myapp.validator;

import com.diansetiyadi.myapp.dao.ProductDAO;
import com.diansetiyadi.myapp.entity.ProductsEntity;
import com.diansetiyadi.myapp.model.ProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ProductInfoValidator implements Validator {

    @Autowired
    private ProductDAO productDAO;

    public boolean supports(Class<?> clazz) {
        return clazz== ProductInfo.class;
    }

    public void validate(Object o, Errors errors) {
        ProductInfo productInfo = (ProductInfo) o;

        //Check the fields of productInfo
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"code","NotEmpty.productForm.code");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"name","NotEmpty.productForm.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"price","NotEmpty.productForm.price");

        String code = productInfo.getCode();
        if(code != null && code.length() > 0){
            if(code.matches("\\s+")){
                errors.rejectValue("code","Pattern.productForm.code");
            }   else if (productInfo.isNewProduct()){
                ProductsEntity product = productDAO.findProduct(code);
                if(product!=null){
                    errors.rejectValue("code","Duplicate.productForm.code");
                }
            }
        }

    }
}
