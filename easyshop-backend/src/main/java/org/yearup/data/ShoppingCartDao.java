package org.yearup.data;

import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    Product addProductById(int userId, int productId);
    boolean updateProductQuantity(int userId, int productId, int newQuantity);
    public void clearCart(int userId);
}
