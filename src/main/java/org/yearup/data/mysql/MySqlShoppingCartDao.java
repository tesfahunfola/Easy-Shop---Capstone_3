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
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao 
{
    public MySqlShoppingCartDao(DataSource dataSource) 
    {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) 
    {
        ShoppingCart cart = new ShoppingCart();
        
        String sql = "SELECT sc.product_id, sc.quantity, " +
                     "       p.product_id, p.name, p.price, p.category_id, p.description, " +
                     "       p.subcategory, p.stock, p.featured, p.image_url " +
                     "FROM shopping_cart sc " +
                     "JOIN products p ON p.product_id = sc.product_id " +
                     "WHERE sc.user_id = ?";

        try (Connection connection = getConnection()) 
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            
            ResultSet row = statement.executeQuery();
            
            while (row.next()) 
            {
                Product product = new Product();
                product.setProductId(row.getInt("product_id"));
                product.setName(row.getString("name"));
                product.setPrice(row.getBigDecimal("price"));
                product.setCategoryId(row.getInt("category_id"));
                product.setDescription(row.getString("description"));
                product.setSubCategory(row.getString("subcategory"));
                product.setStock(row.getInt("stock"));
                product.setFeatured(row.getBoolean("featured"));
                product.setImageUrl(row.getString("image_url"));

                ShoppingCartItem item = new ShoppingCartItem();
                item.setProduct(product);
                item.setQuantity(row.getInt("quantity"));
                
                cart.add(item);
            }
        } 
        catch (SQLException e) 
        {
            throw new RuntimeException(e);
        }

        return cart;
    }

    @Override
    public void addProduct(int userId, int productId) 
    {
        // Check if product already exists in cart
        String checkSql = "SELECT quantity FROM shopping_cart WHERE user_id = ? AND product_id = ?";
        
        try (Connection connection = getConnection()) 
        {
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setInt(1, userId);
            checkStatement.setInt(2, productId);
            
            ResultSet row = checkStatement.executeQuery();
            
            if (row.next()) 
            {
                // Product exists, increment quantity
                String updateSql = "UPDATE shopping_cart SET quantity = quantity + 1 WHERE user_id = ? AND product_id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setInt(1, userId);
                updateStatement.setInt(2, productId);
                updateStatement.executeUpdate();
            } 
            else 
            {
                // Product doesn't exist, insert new row
                String insertSql = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, 1)";
                PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                insertStatement.setInt(1, userId);
                insertStatement.setInt(2, productId);
                insertStatement.executeUpdate();
            }
        } 
        catch (SQLException e) 
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateQuantity(int userId, int productId, int quantity) 
    {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
        
        try (Connection connection = getConnection()) 
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, productId);
            
            statement.executeUpdate();
        } 
        catch (SQLException e) 
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearCart(int userId) 
    {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";
        
        try (Connection connection = getConnection()) 
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            
            statement.executeUpdate();
        } 
        catch (SQLException e) 
        {
            throw new RuntimeException(e);
        }
    }
}
