package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.Order;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao
{
    public MySqlOrderDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public Order create(Order order)
    {
        String sql = "INSERT INTO orders (user_id, date, address, city, state, zip, shipping_amount) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, order.getUserId());
            statement.setTimestamp(2, Timestamp.valueOf(order.getDate()));
            statement.setString(3, order.getAddress());
            statement.setString(4, order.getCity());
            statement.setString(5, order.getState());
            statement.setString(6, order.getZip());
            statement.setBigDecimal(7, order.getShippingAmount());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0)
            {
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next())
                {
                    int orderId = generatedKeys.getInt(1);
                    order.setOrderId(orderId);
                    return order;
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Order getById(int orderId)
    {
        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, orderId);

            ResultSet row = statement.executeQuery();

            if (row.next())
            {
                return mapRow(row);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return null;
    }

    private Order mapRow(ResultSet row) throws SQLException
    {
        int orderId = row.getInt("order_id");
        int userId = row.getInt("user_id");
        LocalDateTime date = row.getTimestamp("date").toLocalDateTime();
        String address = row.getString("address");
        String city = row.getString("city");
        String state = row.getString("state");
        String zip = row.getString("zip");
        var shippingAmount = row.getBigDecimal("shipping_amount");

        return new Order(orderId, userId, date, address, city, state, zip, shippingAmount);
    }
}
