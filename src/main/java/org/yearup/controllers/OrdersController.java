package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.*;
import org.yearup.models.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("orders")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class OrdersController
{
    private OrderDao orderDao;
    private OrderLineItemDao orderLineItemDao;
    private ShoppingCartDao shoppingCartDao;
    private ProfileDao profileDao;
    private UserDao userDao;

    @Autowired
    public OrdersController(OrderDao orderDao, OrderLineItemDao orderLineItemDao, 
                           ShoppingCartDao shoppingCartDao, ProfileDao profileDao, UserDao userDao)
    {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.shoppingCartDao = shoppingCartDao;
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    @PostMapping
    public Order checkout(Principal principal)
    {
        try
        {
            // Get the currently logged in username
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            
            if (user == null)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
            }
            
            int userId = user.getId();

            // Get the user's profile for shipping address
            Profile profile = profileDao.getByUserId(userId);
            
            if (profile == null)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found. Please update your profile before checking out.");
            }

            // Get the user's shopping cart
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);
            
            if (cart.getItems().isEmpty())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shopping cart is empty.");
            }

            // Create the order
            Order order = new Order();
            order.setUserId(userId);
            order.setDate(LocalDateTime.now());
            order.setAddress(profile.getAddress());
            order.setCity(profile.getCity());
            order.setState(profile.getState());
            order.setZip(profile.getZip());
            order.setShippingAmount(BigDecimal.ZERO); // Can be calculated based on business rules

            // Insert the order into the database
            Order createdOrder = orderDao.create(order);
            
            if (createdOrder == null)
            {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create order.");
            }

            // Create order line items from shopping cart items
            Map<Integer, ShoppingCartItem> cartItems = cart.getItems();
            
            for (ShoppingCartItem cartItem : cartItems.values())
            {
                OrderLineItem lineItem = new OrderLineItem();
                lineItem.setOrderId(createdOrder.getOrderId());
                lineItem.setProductId(cartItem.getProduct().getProductId());
                lineItem.setSalesPrice(cartItem.getProduct().getPrice());
                lineItem.setQuantity(cartItem.getQuantity());
                lineItem.setDiscount(cartItem.getDiscountPercent());
                
                orderLineItemDao.create(lineItem);
            }

            // Clear the shopping cart
            shoppingCartDao.clearCart(userId);

            // Return the created order
            return createdOrder;
        }
        catch (ResponseStatusException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
