package com.epam.homework.dao;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDAO {

    private final SimpleJdbcCall processOrderCall;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderDAO(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.processOrderCall = new SimpleJdbcCall(dataSource)
                .withProcedureName("process_order")
                .declareParameters(
                        new SqlParameter("customer_id", Types.INTEGER),
                        new SqlParameter("product_ids", Types.ARRAY),
                        new SqlParameter("quantities", Types.ARRAY),
                        new SqlOutParameter("total_amount", Types.DECIMAL)
                );
    }

    public double processOrder(int customerId, Integer[] productIds, Integer[] quantities) {
        try (var connection = jdbcTemplate.getDataSource().getConnection()) {
            Map<String, Object> result = processOrderCall.execute(
                    customerId,
                    connection.createArrayOf("INTEGER", productIds),
                    connection.createArrayOf("INTEGER", quantities)
            );
            return (double) result.get("total_amount");
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("An error occurred while calling stored procedure", ex);
        }
    }
}
