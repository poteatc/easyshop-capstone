package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
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
// PUT method to update an existing product's quantity in the cart
    @PutMapping("/products/{productId}")
    public ResponseEntity<String> updateProductInCart(@PathVariable Integer productId,
                                                      @RequestBody ShoppingCartItem cartItem,
                                                      Principal principal) {
        try {
            // Get the currently logged-in username
            String userName = principal.getName();
            // Find database user by userName
            User user = userDao.getByUserName(userName);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            int userId = user.getId();

            // Check if the product exists in the user's cart
            boolean isUpdated = shoppingCartDao.updateProductQuantity(userId, productId, cartItem.getQuantity());

            if (isUpdated) {
                return ResponseEntity.ok("Product quantity updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in cart");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating product in cart");
        }
    }

    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart
    // DELETE method to clear all products from the current user's cart
    @DeleteMapping
    public ResponseEntity<String> clearCart(Principal principal) {
        try {
            // Get the currently logged-in username
            String userName = principal.getName();
            // Find database user by userName
            User user = userDao.getByUserName(userName);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            int userId = user.getId();

            // Clear the user's cart
            shoppingCartDao.clearCart(userId);
            return ResponseEntity.ok("All products have been removed from the cart");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error clearing the cart");
        }
    }



}
