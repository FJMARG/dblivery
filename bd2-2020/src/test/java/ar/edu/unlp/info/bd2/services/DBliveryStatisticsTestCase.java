package ar.edu.unlp.info.bd2.services;

import ar.edu.unlp.info.bd2.config.*;
import ar.edu.unlp.info.bd2.model.*;
import ar.edu.unlp.info.bd2.utils.DBInitializer;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Rollback(false)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {SpringDataConfiguration.class},
        loader = AnnotationConfigContextLoader.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class DBliveryStatisticsTestCase {

    @Autowired
    DBInitializer initializer;

    @Autowired
    @Qualifier("springDataJpaService")
    DBliveryStatisticsService service;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @BeforeAll
    public void prepareDB() throws Exception {
        this.initializer.prepareDB();
    }

    @Test
    public void testGetMaxWeigth() {
        Product product = this.service.getMaxWeigth();
        assertEquals("Milanesa con rúcula",product.getName());
    }

    @Test
    public void testGetAllOrdersMadeByUser() {
        assertEquals(5,this.service.getAllOrdersMadeByUser("rubnpastor265").size());
    }

    @Test
    public void testGetPendingOrders() {
        List<Order> orders = this.service.getPendingOrders();
        assertEquals(54,orders.size());
    }

    @Test
    public void testGetSentOrders() {
        List<Order> orders = this.service.getSentOrders();
        assertEquals(65,orders.size());
    }

    @Test
    public void testGetDeliveredOrdersInPeriod() throws ParseException {
        List<Order> orders = this.service.getDeliveredOrdersInPeriod(sdf.parse("1/1/2013"),sdf.parse("31/12/2013"));
        assertEquals(18,orders.size());
    }

    @Test
    public void testGetDeliveredOrdersForUser() {
        List<Order> orders = this.service.getDeliveredOrdersForUser("luzmartnez660");
        assertEquals(3,orders.size());
    }

    @Test
    public void testGetProductsOnePrice() {
        List<Product> products = this.service.getProductsOnePrice();
        assertEquals(27, products.size());
    }

    @Test
    public void testGetSoldProductsOn() throws ParseException {
        List<Product> products = this.service.getSoldProductsOn(sdf.parse("28/2/2010"));
        assertEquals(4, products.size());
        this.assertListEquality(products.stream().map(property -> property.getName()).collect(Collectors.toList()),Arrays.asList("Filet de merluza a la romana","Bife de chorizo grillado","Milanesa americana","Ensalada de hojas verdes y queso"));
    }

    private <T> void assertListEquality(List<T> list1, List<T> list2) {
        if (list1.size() != list2.size()) {
            Assert.fail("Lists have different size");
        }

        for (T objectInList1 : list1) {
            if (!list2.contains(objectInList1)) {
                Assert.fail(objectInList1 + " is not present in list2");
            }
        }
    }


}