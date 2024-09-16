package com.epam.homework.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createStoredProcedure(){
        String sql = """
            CREATE OR REPLACE PROCEDURE process_order(
                IN customer_id INT,
                IN product_ids INT[],
                IN quantities INT[],
                INOUT total_amount DECIMAL(10, 2)
            )
            LANGUAGE plpgsql
            AS $$
            DECLARE
                product_price DECIMAL(10, 2);
                discount DECIMAL(10,2) := 0;
                stock_available INT;
                product_id INT;
                quantity INT;
                order_id INT;
                loyalty_points INT;
                i INT;
            BEGIN
                BEGIN
                    SELECT loyalty_points INTO loyalty_points
                    FROM Customers
                    WHERE id = customer_id;
                    
                    INSERT INTO Orders(customer_id, order_date, total_amount)
                    VALUES (customer_id, NOW(), 0)
                    RETURNING id INTO order_id;
                    
                    FOR i IN 1..array_length(product_ids, 1) LOOP
                        product_id := product_ids[i];
                        quantity := quantities[i];
                        SELECT price, stock INTO product_price, stock_available
                        FROM Products
                        WHERE id = product_id;
                        
                        INSERT INTO Order_Items (order_id, product_id, quantity, price)
                        VALUES (order_id, product_id, quantity, product_price);
                        
                        total_amount := total_amount + (product_price * quantity);
                        
                        UPDATE Products
                        SET stock = stock - quantity
                        WHERE id = product_id;
                    END LOOP;
                    
                    IF loyalty_points > 100 THEN
                        discount := total_amount * 0.1;
                    END IF;
                    
                    total_amount := total_amount - discount;
                    
                    UPDATE Orders
                    SET total_amount = total_amount
                    WHERE id = order_id;
                    
                    COMMIT;
                    
                EXCEPTION WHEN OTHERS THEN
                    ROLLBACK;
                END;
            END;
            $$;
        """;

        jdbcTemplate.execute(sql);

        System.out.println("Stored procedure created successfully...");
    }

    public void createTables() {
        String customers = """
            CREATE TABLE IF NOT EXISTS Customers(
                id SERIAL PRIMARY KEY,
                name TEXT,
                loyalty_points INT,
                email TEXT
            );
        """;

        String products = """

            CREATE TABLE IF NOT EXISTS Products(
                id SERIAL PRIMARY KEY,
                title TEXT,
                price DECIMAL(10, 2),
                stock INT
            );
        """;

        String orders = """
            CREATE TABLE IF NOT EXISTS Orders(
                id SERIAL PRIMARY KEY,
                customer_id INT REFERENCES Customers(id),
                date_time TIMESTAMP,
                total_amount DECIMAL(10, 2)
            );
        """;

        String orderItems = """
            CREATE TABLE IF NOT EXISTS Order_Items(
                id SERIAL PRIMARY KEY,
                order_id INT REFERENCES Orders(id),
                product_id INT REFERENCES Products(id),
                quantity INT,
                price DECIMAL(10, 2)
            );
        """;

        jdbcTemplate.execute(customers);
        jdbcTemplate.execute(products);
        jdbcTemplate.execute(orders);
        jdbcTemplate.execute(orderItems);
        System.out.println("Successfully created tables...");
    }
}
