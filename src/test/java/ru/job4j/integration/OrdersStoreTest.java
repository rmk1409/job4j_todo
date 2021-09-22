package ru.job4j.integration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OrdersStoreTest {
    private BasicDataSource pool = new BasicDataSource();

    @Before
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("./db/update_001.sql")))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @After
    public void destroy() throws SQLException {
        pool.getConnection().prepareStatement("DROP TABLE IF EXISTS orders").executeUpdate();
    }

    @Test
    public void whenSaveOrderAndFindAll() {
        OrdersStore store = new OrdersStore(pool);
        store.save(Order.of("name1", "description1"));
        List<Order> all = (List<Order>) store.findAll();
        assertThat(all.size(), is(1));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenSaveOrderAndFindByName() {
        OrdersStore store = new OrdersStore(pool);
        Order order1 = Order.of("name1", "description1");
        Order order2 = Order.of("name1", "description2");
        Order order3 = Order.of("name1", "description3");
        Order order4 = Order.of("name2", "description4");
        store.save(order1);
        store.save(order2);
        store.save(order3);
        store.save(order4);
        List<Order> foundOrders = store.findByName("name1");
        assertThat(foundOrders.size(), is(3));
        assertThat(foundOrders, is(List.of(order1, order2, order3)));
    }

    @Test
    public void whenSaveOrderAndFindById() {
        OrdersStore store = new OrdersStore(pool);
        store.save(Order.of("name1", "description1"));
        Order found = store.findById(1);
        assertThat(found.getName(), is("name1"));
        assertThat(found.getDescription(), is("description1"));
    }

    @Test
    public void whenSaveOrderAndUpdate() {
        OrdersStore store = new OrdersStore(pool);
        Order order = Order.of("name1", "description1");
        store.save(order);
        order.setName("newName");
        order.setDescription("newDesc");
        store.update(order);
        Order found = store.findById(1);
        assertThat(found.getName(), is("newName"));
        assertThat(found.getDescription(), is("newDesc"));
    }
}
