package org.yearup.data.mysql;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yearup.models.Product;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MySqlProductDaoTest extends BaseDaoTestClass
{
    private MySqlProductDao dao;

    @BeforeEach
    public void setup()
    {
        dao = new MySqlProductDao(dataSource);
    }

    @Test
    public void getById_shouldReturn_theCorrectProduct()
    {
        // arrange
        int productId = 1;
        Product expected = new Product()
        {{
            setProductId(1);
            setName("Smartphone");
            setPrice(new BigDecimal("499.99"));
            setCategoryId(1);
            setDescription("A powerful and feature-rich smartphone for all your communication needs.");
            setSubCategory("Black");
            setStock(50);
            setFeatured(false);
            setImageUrl("smartphone.jpg");
        }};

        // act
        var actual = dao.getById(productId);

        // assert
        assertEquals(expected.getPrice(), actual.getPrice(), "Because I tried to get product 1 from the database.");
    }

    @Test
    public void search_withNoCriteria_shouldReturnAllProducts()
    {
        // arrange - no filters applied
        Integer categoryId = null;
        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;
        String subCategory = null;

        // act
        List<Product> results = dao.search(categoryId, minPrice, maxPrice, subCategory);

        // assert
        assertNotNull(results, "Results should not be null");
        assertEquals(12, results.size(), "Should return all 12 products when no filters are applied");
    }

    @Test
    public void search_byCategoryId_shouldReturnProductsInCategory()
    {
        // arrange - filter by Electronics (category_id = 1)
        Integer categoryId = 1;
        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;
        String subCategory = null;

        // act
        List<Product> results = dao.search(categoryId, minPrice, maxPrice, subCategory);

        // assert
        assertNotNull(results, "Results should not be null");
        assertEquals(3, results.size(), "Should return 3 electronics products");
        assertTrue(results.stream().allMatch(p -> p.getCategoryId() == 1),
                "All products should be in category 1 (Electronics)");
    }

    @Test
    public void search_byMinPrice_shouldReturnProductsAboveMinimum()
    {
        // arrange - filter by minimum price of 100.00
        Integer categoryId = null;
        BigDecimal minPrice = new BigDecimal("100.00");
        BigDecimal maxPrice = null;
        String subCategory = null;

        // act
        List<Product> results = dao.search(categoryId, minPrice, maxPrice, subCategory);

        // assert
        assertNotNull(results, "Results should not be null");
        assertTrue(results.size() > 0, "Should return products with price >= 100.00");
        assertTrue(results.stream().allMatch(p -> p.getPrice().compareTo(minPrice) >= 0),
                "All products should have price >= 100.00");
    }

    @Test
    public void search_byMaxPrice_shouldReturnProductsBelowMaximum()
    {
        // arrange - filter by maximum price of 50.00
        Integer categoryId = null;
        BigDecimal minPrice = null;
        BigDecimal maxPrice = new BigDecimal("50.00");
        String subCategory = null;

        // act
        List<Product> results = dao.search(categoryId, minPrice, maxPrice, subCategory);

        // assert
        assertNotNull(results, "Results should not be null");
        assertTrue(results.size() > 0, "Should return products with price <= 50.00");
        assertTrue(results.stream().allMatch(p -> p.getPrice().compareTo(maxPrice) <= 0),
                "All products should have price <= 50.00");
        
        // Verify specific products (Men's T-Shirt at 29.99, Women's Blouse at 49.99)
        assertTrue(results.stream().anyMatch(p -> p.getName().equals("Men's T-Shirt")),
                "Should include Men's T-Shirt (29.99)");
        assertTrue(results.stream().anyMatch(p -> p.getName().equals("Women's Blouse")),
                "Should include Women's Blouse (49.99)");
    }

    @Test
    public void search_byPriceRange_shouldReturnProductsInRange()
    {
        // arrange - filter by price range 50.00 to 100.00
        Integer categoryId = null;
        BigDecimal minPrice = new BigDecimal("50.00");
        BigDecimal maxPrice = new BigDecimal("100.00");
        String subCategory = null;

        // act
        List<Product> results = dao.search(categoryId, minPrice, maxPrice, subCategory);

        // assert
        assertNotNull(results, "Results should not be null");
        assertTrue(results.size() > 0, "Should return products in price range 50-100");
        assertTrue(results.stream().allMatch(p -> 
                p.getPrice().compareTo(minPrice) >= 0 && p.getPrice().compareTo(maxPrice) <= 0),
                "All products should have price between 50.00 and 100.00");
        
        // Verify specific products in range
        assertTrue(results.stream().anyMatch(p -> p.getName().equals("Headphones")),
                "Should include Headphones (99.99)");
        assertTrue(results.stream().anyMatch(p -> p.getName().equals("Coffee Maker")),
                "Should include Coffee Maker (79.99)");
    }

    @Test
    public void search_bySubCategory_shouldReturnMatchingProducts()
    {
        // arrange - filter by subcategory "Blue"
        Integer categoryId = null;
        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;
        String subCategory = "Blue";

        // act
        List<Product> results = dao.search(categoryId, minPrice, maxPrice, subCategory);

        // assert
        assertNotNull(results, "Results should not be null");
        assertEquals(2, results.size(), "Should return 2 products with Blue subcategory");
        assertTrue(results.stream().allMatch(p -> p.getSubCategory().equals("Blue")),
                "All products should have subcategory 'Blue'");
        
        // Verify specific products
        assertTrue(results.stream().anyMatch(p -> p.getName().equals("Men's Jeans")),
                "Should include Men's Jeans");
        assertTrue(results.stream().anyMatch(p -> p.getName().equals("Women's Jeans")),
                "Should include Women's Jeans");
    }

    @Test
    public void search_byCategoryAndPriceRange_shouldReturnFilteredProducts()
    {
        // arrange - filter Electronics (category 1) with price between 400 and 600
        Integer categoryId = 1;
        BigDecimal minPrice = new BigDecimal("400.00");
        BigDecimal maxPrice = new BigDecimal("600.00");
        String subCategory = null;

        // act
        List<Product> results = dao.search(categoryId, minPrice, maxPrice, subCategory);

        // assert
        assertNotNull(results, "Results should not be null");
        assertEquals(1, results.size(), "Should return 1 product (Smartphone)");
        assertTrue(results.stream().allMatch(p -> 
                p.getCategoryId() == 1 && 
                p.getPrice().compareTo(minPrice) >= 0 && 
                p.getPrice().compareTo(maxPrice) <= 0),
                "Product should be in category 1 with price between 400-600");
        assertEquals("Smartphone", results.get(0).getName(),
                "Should return Smartphone (499.99 in Electronics)");
    }

    @Test
    public void search_byCategoryAndSubCategory_shouldReturnMatchingProducts()
    {
        // arrange - filter Fashion (category 2) with subcategory "White"
        Integer categoryId = 2;
        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;
        String subCategory = "White";

        // act
        List<Product> results = dao.search(categoryId, minPrice, maxPrice, subCategory);

        // assert
        assertNotNull(results, "Results should not be null");
        assertEquals(1, results.size(), "Should return 1 product");
        assertTrue(results.stream().allMatch(p -> 
                p.getCategoryId() == 2 && p.getSubCategory().equals("White")),
                "Product should be in Fashion category with White subcategory");
        assertEquals("Men's Dress Shirt", results.get(0).getName(),
                "Should return Men's Dress Shirt");
    }

    @Test
    public void search_withAllCriteria_shouldReturnMatchingProducts()
    {
        // arrange - filter Home & Kitchen (category 3), price 50-150, Red subcategory
        Integer categoryId = 3;
        BigDecimal minPrice = new BigDecimal("50.00");
        BigDecimal maxPrice = new BigDecimal("150.00");
        String subCategory = "Red";

        // act
        List<Product> results = dao.search(categoryId, minPrice, maxPrice, subCategory);

        // assert
        assertNotNull(results, "Results should not be null");
        assertEquals(1, results.size(), "Should return 1 product (Cookware Set)");
        Product product = results.get(0);
        assertEquals("Cookware Set", product.getName(), "Should return Cookware Set");
        assertEquals(3, product.getCategoryId(), "Should be in category 3");
        assertEquals("Red", product.getSubCategory(), "Should have Red subcategory");
        assertTrue(product.getPrice().compareTo(minPrice) >= 0 && 
                product.getPrice().compareTo(maxPrice) <= 0,
                "Price should be between 50 and 150");
    }

    @Test
    public void search_withNonExistentCategory_shouldReturnEmptyList()
    {
        // arrange - search for non-existent category
        Integer categoryId = 999;
        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;
        String subCategory = null;

        // act
        List<Product> results = dao.search(categoryId, minPrice, maxPrice, subCategory);

        // assert
        assertNotNull(results, "Results should not be null");
        assertEquals(0, results.size(), "Should return empty list for non-existent category");
    }

    @Test
    public void search_withNonExistentSubCategory_shouldReturnEmptyList()
    {
        // arrange - search for non-existent subcategory
        Integer categoryId = null;
        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;
        String subCategory = "NonExistent";

        // act
        List<Product> results = dao.search(categoryId, minPrice, maxPrice, subCategory);

        // assert
        assertNotNull(results, "Results should not be null");
        assertEquals(0, results.size(), "Should return empty list for non-existent subcategory");
    }

    @Test
    public void search_withPriceRangeNoMatches_shouldReturnEmptyList()
    {
        // arrange - search for price range with no products
        Integer categoryId = null;
        BigDecimal minPrice = new BigDecimal("1000.00");
        BigDecimal maxPrice = new BigDecimal("2000.00");
        String subCategory = null;

        // act
        List<Product> results = dao.search(categoryId, minPrice, maxPrice, subCategory);

        // assert
        assertNotNull(results, "Results should not be null");
        assertEquals(0, results.size(), "Should return empty list when no products in price range");
    }

    @Test
    public void search_withExactPriceMatch_shouldReturnMatchingProducts()
    {
        // arrange - search for exact price of 99.99 (Headphones)
        Integer categoryId = null;
        BigDecimal minPrice = new BigDecimal("99.99");
        BigDecimal maxPrice = new BigDecimal("99.99");
        String subCategory = null;

        // act
        List<Product> results = dao.search(categoryId, minPrice, maxPrice, subCategory);

        // assert
        assertNotNull(results, "Results should not be null");
        assertEquals(1, results.size(), "Should return 1 product at exactly 99.99");
        assertEquals("Headphones", results.get(0).getName(), "Should return Headphones");
        assertEquals(new BigDecimal("99.99"), results.get(0).getPrice(), "Price should be 99.99");
    }

    @Test
    public void listByCategoryId_shouldReturnAllProductsInCategory()
    {
        // arrange - list all Fashion products (category 2)
        int categoryId = 2;

        // act
        List<Product> results = dao.listByCategoryId(categoryId);

        // assert
        assertNotNull(results, "Results should not be null");
        assertEquals(6, results.size(), "Should return 6 fashion products");
        assertTrue(results.stream().allMatch(p -> p.getCategoryId() == 2),
                "All products should be in category 2 (Fashion)");
    }

}