package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;

// convert this class to a REST controller
// only logged in users should have access to these actions
@RestController
@CrossOrigin
@RequestMapping("/cart")
public class ShoppingCartController
{
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    //
    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }


    // each method in this controller requires a Principal object as a parameter
    @GetMapping
    public ShoppingCart getCart(Principal principal)
    {
        // TODO : fix principal null pointer
        // FIXED : Make sure Bearer Token is used in Authorization tab of Postman
        // Mock authentication with username
//        if (principal == null) {
//            principal = new Principal() {
//                @Override
//                public String getName() {
//                    return "george";
//                    //return "admin"; // Mock username
//                }
//            };
//        }
        try
        {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // use the shoppingcartDao to get all items in the cart and return the cart
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);
            return cart;
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    //const url = `${config.baseUrl}/cart/products/${productId}`;
    @PostMapping("/products/{productId}")
    public Product addProductToCart(@PathVariable Integer productId, Principal principal) {
        try
        {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // use the shoppingcartDao to get add product to cart
            shoppingCartDao.addProductById(userId, productId);
            return productDao.getById(productId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated


    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart

}
