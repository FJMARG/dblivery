package ar.edu.unlp.info.bd2.services;

import ar.edu.unlp.info.bd2.config.AppConfig;
import ar.edu.unlp.info.bd2.config.MongoDBConfiguration;
import ar.edu.unlp.info.bd2.model.*;
import ar.edu.unlp.info.bd2.repositories.DBliveryException;
import com.mongodb.client.MongoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {AppConfig.class, MongoDBConfiguration.class},
        loader = AnnotationConfigContextLoader.class)
@Rollback(true)
public class DBliveryMongoTestCase {

    @Autowired
    private MongoClient client;

    @Autowired
    private DBliveryService service;

    @BeforeEach
    public void setUp() {
        this.client.getDatabase("dblivery").drop();
    }

    @Test
    public void testCreateUser() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1982);
        cal.set(Calendar.MONTH, Calendar.MAY);
        cal.set(Calendar.DAY_OF_MONTH, 17);
        Date dob = cal.getTime();
        User u1 = this.service.createUser("hugo.gamarra@testmail.com", "123456", "hgamarra", "Hugo Gamarra", dob);
        assertNotNull(u1.getObjectId());
    }

    @Test
    public void testCreateProduct() {
        Supplier s1 = this.service.createSupplier("Burger King", "30710256443", "Av. Corrientes 956", Float.valueOf(-53.45F), Float.valueOf(-60.22F));
        assertNotNull(s1.getObjectId());
        assertEquals("Burger King",s1.getName());
        Product p1 = this.service.createProduct("Combo Stacker ATR", Float.valueOf(2521.2F), Float.valueOf(2.5F),s1);
        assertNotNull(p1.getObjectId());
        assertEquals("Combo Stacker ATR",p1.getName());
        assertEquals(1,p1.getPrices().size());
    }

    @Test
    public void testUpdateProductPrice() throws DBliveryException {
        Calendar cal = Calendar.getInstance();
        Date startDate = cal.getTime();
        Supplier s1 = this.service.createSupplier("Burger King", "30710256443", "Av. Corrientes 956", Float.valueOf(-53.45F), Float.valueOf(-60.22F));
        Product p1 = this.service.createProduct("Combo Stacker ATR", Float.valueOf(2521.2F), Float.valueOf(2.5F),s1);
        assertNotNull(p1.getObjectId());
        assertEquals(1,p1.getPrices().size());
        Product p2 = this.service.updateProductPrice(p1.getObjectId(),Float.valueOf(3000.0F),startDate);
        assertEquals(Float.valueOf(3000.0F),p2.getPrice());
        assertEquals(2,p2.getPrices().size());
    }

    @Test
    public void testCreateOrder() throws DBliveryException {
        Calendar cal = Calendar.getInstance();
        Date orderDate = cal.getTime();
        Supplier s1 = this.service.createSupplier("Burger King", "30710256443", "Av. Corrientes 956", Float.valueOf(-53.45F), Float.valueOf(-60.22F));
        Product p1 = this.service.createProduct("Combo Stacker ATR", Float.valueOf(2521.2F), Float.valueOf(2.5F),s1);
        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.YEAR, 1982);
        cal2.set(Calendar.MONTH, Calendar.MAY);
        cal2.set(Calendar.DAY_OF_MONTH, 17);
        Date dob = cal.getTime();
        User u1 = this.service.createUser("hugo.gamarra@testmail.com", "123456", "hgamarra", "Hugo Gamarra", dob);
        Order o1 = this.service.createOrder(orderDate,"Av. Corrientes 1405 2° B", Float.valueOf(-54.45F), Float.valueOf(-62.22F),u1);
        Order o2 = this.service.addProduct(o1.getObjectId(), 1L, p1);
        Optional<Order> o3 = this.service.getOrderById(o2.getObjectId());
        if (!o3.isPresent()) {
            throw new DBliveryException("Order doesn't exists");
        }
        Order ord = o3.get();
        assertNotNull(ord.getObjectId());
        assertEquals(1,ord.getStatus().size());
        assertEquals(u1.getObjectId(),ord.getClient().getObjectId());
        assertEquals(1,ord.getProducts().size());
    }

    @Test
    public void testDeliverOrder() throws DBliveryException {
        Calendar cal = Calendar.getInstance();
        Date orderDate = cal.getTime();
        Supplier s1 = this.service.createSupplier("Burger King", "30710256443", "Av. Corrientes 956", Float.valueOf(-53.45F), Float.valueOf(-60.22F));
        Product p1 = this.service.createProduct("Combo Stacker ATR", Float.valueOf(2521.2F), Float.valueOf(2.5F),s1);
        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.YEAR, 1982);
        cal2.set(Calendar.MONTH, Calendar.MAY);
        cal2.set(Calendar.DAY_OF_MONTH, 17);
        Date dob = cal.getTime();
        User u1 = this.service.createUser("hugo.gamarra@testmail.com", "123456", "hgamarra", "Hugo Gamarra", dob);
        cal2.set(Calendar.YEAR, 1988);
        cal2.set(Calendar.MONTH, Calendar.JUNE);
        cal2.set(Calendar.DAY_OF_MONTH, 23);
        Date dob2 = cal.getTime();
        User u2 = this.service.createUser("delivery@info.unlp.edu.ar", "123456", "delivery", "Delivery", dob2);
        Order o1 = this.service.createOrder(orderDate,"Av. Corrientes 1405 2° B", Float.valueOf(-54.45F), Float.valueOf(-62.22F),u1);
        assertFalse(this.service.canDeliver(o1.getObjectId()));
        assertThrows(DBliveryException.class, () -> this.service.deliverOrder(o1.getObjectId(),u2),"The order can't be delivered");
        Order o2 = this.service.addProduct(o1.getObjectId(), 1L, p1);
        assertTrue(this.service.canDeliver(o2.getObjectId()));
        Order o3 = this.service.deliverOrder(o2.getObjectId(),u2);
        assertNotNull(o3.getObjectId());
        assertEquals(2,o3.getStatus().size());
        assertEquals(u2,o3.getDeliveryUser());
    }



    @Test
    public void testCancelOrder() throws Exception {
        Calendar cal = Calendar.getInstance();
        Date orderDate = cal.getTime();
        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.YEAR, 1982);
        cal2.set(Calendar.MONTH, Calendar.MAY);
        cal2.set(Calendar.DAY_OF_MONTH, 17);
        Date dob = cal.getTime();
        User u1 = this.service.createUser("hugo.gamarra@testmail.com", "123456", "hgamarra", "Hugo Gamarra", dob);
        Supplier s1 = this.service.createSupplier("Burger King", "30710256443", "Av. Corrientes 956", Float.valueOf(-53.45F), Float.valueOf(-60.22F));
        Order o1 = this.service.createOrder(orderDate,"Av. Corrientes 1405 2° B", Float.valueOf(-54.45F), Float.valueOf(-62.22F),u1);
        assertTrue(this.service.canCancel(o1.getObjectId()));
        cal2.set(Calendar.YEAR, 1988);
        cal2.set(Calendar.MONTH, Calendar.JUNE);
        cal2.set(Calendar.DAY_OF_MONTH, 23);
        Date dob2 = cal.getTime();
        User u2 = this.service.createUser("delivery@info.unlp.edu.ar", "123456", "delivery", "Delivery", dob2);
        Product p1 = this.service.createProduct("Combo Stacker ATR", Float.valueOf(2521.2F), Float.valueOf(2.5F),s1);
        Order o2 = this.service.addProduct(o1.getObjectId(), 1L, p1);
        Order o3 = this.service.deliverOrder(o2.getObjectId(), u2);
        assertFalse(this.service.canCancel(o3.getObjectId()));
        assertThrows(DBliveryException.class, () -> this.service.cancelOrder(o3.getObjectId()),"The order can't be cancelled");
        Order o4 = this.service.createOrder(orderDate,"Av. Corrientes 1405 2° B", Float.valueOf(-54.45F), Float.valueOf(-62.22F),u1);
        Order o5 = this.service.cancelOrder(o4.getObjectId());
        assertEquals(this.service.getActualStatus(o5.getObjectId()).getStatus(),"Cancelled");
        assertEquals(2,o5.getStatus().size());
    }

    @Test
    public void testFinishOrder() throws DBliveryException {
        Calendar cal = Calendar.getInstance();
        Date orderDate = cal.getTime();
        Supplier s1 = this.service.createSupplier("Burger King", "30710256443", "Av. Corrientes 956", Float.valueOf(-53.45F), Float.valueOf(-60.22F));
        Product p1 = this.service.createProduct("Combo Stacker ATR", Float.valueOf(2521.2F), Float.valueOf(2.5F),s1);
        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.YEAR, 1982);
        cal2.set(Calendar.MONTH, Calendar.MAY);
        cal2.set(Calendar.DAY_OF_MONTH, 17);
        Date dob = cal.getTime();
        User u1 = this.service.createUser("hugo.gamarra@testmail.com", "123456", "hgamarra", "Hugo Gamarra", dob);
        cal2.set(Calendar.YEAR, 1988);
        cal2.set(Calendar.MONTH, Calendar.JUNE);
        cal2.set(Calendar.DAY_OF_MONTH, 23);
        Date dob2 = cal.getTime();
        User u2 = this.service.createUser("delivery@info.unlp.edu.ar", "123456", "delivery", "Delivery", dob2);
        Order o1 = this.service.createOrder(orderDate,"Av. Corrientes 1405 2° B", Float.valueOf(-54.45F), Float.valueOf(-62.22F),u1);
        Order o2 = this.service.addProduct(o1.getObjectId(), 1L, p1);
        assertThrows(DBliveryException.class, () -> this.service.finishOrder(o2.getObjectId()),"The order can't be finished");
        Order o3 = this.service.deliverOrder(o2.getObjectId(),u2);
        assertTrue(this.service.canFinish(o3.getObjectId()));
        Order o4 = this.service.finishOrder(o3.getObjectId());
        assertNotNull(o4.getObjectId());
        assertEquals(3,o4.getStatus().size());
        assertEquals(this.service.getActualStatus(o4.getObjectId()).getStatus(),"Delivered");
    }

    @Test
    public void testGetUser() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1982);
        cal.set(Calendar.MONTH, Calendar.MAY);
        cal.set(Calendar.DAY_OF_MONTH, 17);
        Date dob = cal.getTime();
        User u1 = this.service.createUser("hugo.gamarra@testmail.com", "123456", "hgamarra", "Hugo Gamarra", dob);
        assertNotNull(u1.getObjectId());
        assertEquals("hgamarra", u1.getUsername());
        Optional<User> u2 = this.service.getUserByUsername("hgamarra");
        if (u2.isPresent()) {
            User u3 = u2.get();
            assertEquals("hgamarra", u3.getUsername());
            assertEquals("hugo.gamarra@testmail.com", u3.getEmail());
        }
        Optional<User> u4 = this.service.getUserByEmail("hugo.gamarra@testmail.com");
        if (u4.isPresent()) {
            User u5 = u4.get();
            assertEquals("hgamarra", u5.getUsername());
            assertEquals("hugo.gamarra@testmail.com", u5.getEmail());
        }
        Optional<User> u6= this.service.getUserById(u1.getObjectId());
        if (u6.isPresent()) {
            User u7 = u6.get();
            assertEquals("hgamarra", u7.getUsername());
            assertEquals("hugo.gamarra@testmail.com", u7.getEmail());
            assertEquals(u7.getObjectId(), u1.getObjectId());
        }
    }

    @Test
    public void testGetProduct() {
        Supplier s1 = this.service.createSupplier("Burger King", "30710256443", "Av. Corrientes 956", Float.valueOf(-53.45F), Float.valueOf(-60.22F));
        assertNotNull(s1.getObjectId());
        assertEquals("Burger King",s1.getName());
        Product p1 = this.service.createProduct("Combo Stacker ATR", Float.valueOf(2521.2F), Float.valueOf(2.5F),s1);
        Product p2 = this.service.createProduct("Combo Tostado de Campo", Float.valueOf(2210.2F), Float.valueOf(2.2F), s1);
        Product p3 = this.service.createProduct("Combo Stacker ATR triple", Float.valueOf(1210F), Float.valueOf(1.8F), s1);
        assertEquals(this.service.getProductsByName("Combo Stacker ATR").size(),2);
        assertEquals(this.service.getProductsByName("Combo Tostado de Campo").size(),1);
        assertEquals(this.service.getProductsByName("triple").size(),1);

    }
}
