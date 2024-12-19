package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        String sql = """
        SELECT sc.product_id, sc.quantity, 
               p.name, p.price, p.category_id, p.description, 
               p.color, p.image_url, p.stock, p.featured
        FROM shopping_cart sc
        JOIN products p ON sc.product_id = p.product_id
        WHERE sc.user_id = ?
    """;

        ShoppingCart shoppingCart = new ShoppingCart();

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet row = statement.executeQuery();

            while (row.next()) {
                // Map product details
                Product product = new Product();
                product.setProductId(row.getInt("product_id"));
                product.setName(row.getString("name"));
                product.setPrice(row.getBigDecimal("price"));
                product.setCategoryId(row.getInt("category_id"));
                product.setDescription(row.getString("description"));
                product.setColor(row.getString("color"));
                product.setImageUrl(row.getString("image_url"));
                product.setStock(row.getInt("stock"));
                product.setFeatured(row.getBoolean("featured"));

                // Map shopping cart item
                ShoppingCartItem cartItem = new ShoppingCartItem();
                cartItem.setProduct(product);
                cartItem.setQuantity(row.getInt("quantity"));

                // Add item to the cart
                shoppingCart.add(cartItem);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return shoppingCart;
    }

    @Override
    public Product addProductById(int userId, int productId) {
        String sql = """
                INSERT INTO shopping_cart (user_id, product_id, quantity)
                VALUES (?, ?, 1)
                """;
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, productId);

            int rows = statement.executeUpdate();
            System.out.println(rows + " rows added");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
