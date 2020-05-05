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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, HibernateConfiguration.class, DBInitializerConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@Rollback(true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DBliveryStatisticsTestCase {

    @Autowired
    DBInitializer initializer;

    @Autowired
    DBliveryService service;
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @BeforeAll
    public void prepareDB() throws Exception {
//        this.initializer.prepareDB();
    }
    
//    @Test
//    public void testGetAllOrdersMadeByUser() {
//    	assertEquals(5,this.service.getAllOrdersMadeByUser("rubnpastor265").size());
//    }
//    
//    @Test
//    public void testGetUsersSpendingMoreThan() {
//    	List<User> users = this.service.getUsersSpendingMoreThan(6000F);
//    	assertEquals(7,users.size());
//    	this.assertListEquality(users.stream().map(property -> property.getUsername()).collect(Collectors.toList()),Arrays.asList("alfredomartnez114","paulasez791","eduardomartin114","carlabentez531","nataliocastro278","florenciacastillo698","nataliomartnez928"));
//    }
//    
//    @Test
//    public void testGetTopNSuppliers() {
//    	List<Supplier> suppliers = this.service.getTopNSuppliersInSentOrders(4);
//    	assertEquals(4,suppliers.size());
//    	this.assertListEquality(suppliers.stream().map(property -> property.getName()).collect(Collectors.toList()),Arrays.asList("La Trattoria", "Olivia Pizzas & Empanadas", "El Ladrillo", "Pizza Nova"));
//    }
//    
//    @Test
//    public void testGetTop10MoreExpensiveProducts() {
//    	List<Product> products = this.service.getTop10MoreExpensiveProducts();
//    	assertEquals(9,products.size());
//    	this.assertListEquality(products.stream().map(property -> property.getName()).collect(Collectors.toList()),Arrays.asList("Maxi hamburguesa completa","Milanesa napolitana","Ensalada César","Ensalada waldorf","Milanesa de pollo napolitana sola","Sándwich de bondiola de cerdo completo","Lomo al roquefort","Tortilla a la española","Choripán"));
//    }
//    
//    @Test
//    public void testGetTop6UsersMoreOrders() {
//    	List<User> users = this.service.getTop6UsersMoreOrders();
//    	assertEquals(6,users.size());
//    	this.assertListEquality(users.stream().map(property -> property.getUsername()).collect(Collectors.toList()),Arrays.asList("maravega596","maramuoz97","eduardoalonso677","rubnpastor265","nataliocruz598","luzmartnez660"));
//    }
//
//    @Test
//    public void testGetCancelledOrdersInPeriod() throws ParseException {
//    	List<Order> orders = this.service.getCancelledOrdersInPeriod(sdf.parse("1/1/2014"),sdf.parse("31/12/2014"));
//    	assertEquals(11,orders.size());
//    }
//    
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
//
//    @Test
//    public void testGetDeliveredOrdersInPeriod() throws ParseException {
//    	List<Order> orders = this.service.getDeliveredOrdersInPeriod(sdf.parse("1/1/2013"),sdf.parse("31/12/2013"));
//    	assertEquals(18,orders.size());
//    }
//    
    @Test
    public void testGetDeliveredOrdersForUser() {
    	List<Order> orders = this.service.getDeliveredOrdersForUser("luzmartnez660");
    	assertEquals(3,orders.size());
    }
//    
//    @Test
//    public void testGetSentMoreOneHour() {
//    	List<Order> orders = this.service.getSentMoreOneHour();
//    	assertEquals(123,orders.size());
//    }
//    
//    @Test
//    public void testGetDeliveredOrdersSameDay() {
//    	List<Order> orders = this.service.getDeliveredOrdersSameDay();
//    	assertEquals(8,orders.size());
//    }
//    
    @Test
    public void testGet5LessDeliveryUsers() {
    	List<User> users = this.service.get5LessDeliveryUsers();
    	assertEquals(5,users.size());
    	this.assertListEquality(users.stream().map(property -> property.getUsername()).collect(Collectors.toList()),Arrays.asList("luzpascual621","carlasantana949","juantorres331","rubnpastor499","mariorubio577"));
    }
//    
//    @Test
//    public void testGetBestSellingProduct() {
//    	Product product = this.service.getBestSellingProduct();
//    	assertEquals("Pizza napolitana",product.getName());
//    }
//    
//    @Test
//    public void testGetProductsOnePrice() {
//    	List<Product> products = this.service.getProductsOnePrice();
//    	assertEquals(27, products.size());
//    }
//    
//    @Test
//    public void testGetProductIncreaseMoreThan100() {
//    	List<Product> products = this.service.getProductIncreaseMoreThan100();
//    	assertEquals(6, products.size());
//    	this.assertListEquality(products.stream().map(property -> property.getName()).collect(Collectors.toList()),Arrays.asList("Sorrentinos de jamón y queso mozzarella","Sándwich de bondiola de cerdo, lechuga y tomate","Papas fritas con cheddar y panceta","Bondiola de cerdo a la riojana","Tabla fritas y fiambre","Ravioles de verdura"));
//    }
//
//    @Test
//    public void testGetSupplierLessExpensiveProduct() {
//    	assertEquals("Pancho Crazy", this.service.getSupplierLessExpensiveProduct().getName());
//    }
//    
//    @Test
//    public void testGetSuppliersDoNotSellOn() throws ParseException {
//    	List<Supplier> suppliers = this.service.getSuppliersDoNotSellOn(sdf.parse("28/2/2010"));
//    	assertEquals(7,suppliers.size());
//    	this.assertListEquality(suppliers.stream().map(property -> property.getName()).collect(Collectors.toList()),Arrays.asList("La Trattoria","Pancho Crazy","Kentucky","La Bodeguita","Foodie Special Burger","Lo de Carlos","Burger Bar"));
//    }
//    
//    @Test
//    public void testGetSoldProductsOn() throws ParseException {
//    	List<Product> products = this.service.getSoldProductsOn(sdf.parse("28/2/2010"));
//    	assertEquals(4, products.size());
//    	this.assertListEquality(products.stream().map(property -> property.getName()).collect(Collectors.toList()),Arrays.asList("Filet de merluza a la romana","Bife de chorizo grillado","Milanesa americana","Ensalada de hojas verdes y queso"));
//    }
//    
//    @Test
//    public void testGetOrdersCompleteMorethanOneDay() {
//    	assertEquals(99, this.service.getOrdersCompleteMorethanOneDay().size());
//    }
//
//    @Test
//    public void testGetProductsWithPriceAt() throws ParseException {
//    	List<Object[]> prices = this.service.getProductsWithPriceAt(sdf.parse("28/2/2013"));
//    	assertEquals(110, prices.size());
//    	assertEquals("Papas fritas a la provenzal", ((Product)prices.get(0)[0]).getName());
//    	assertEquals(426.0F, prices.get(0)[1]);
//    	assertEquals("Lomo a la mostaza con papas noisette", ((Product)prices.get(109)[0]).getName());
//    	assertEquals(459.0F, prices.get(109)[1]);
//    	assertEquals("Lomo a las cuatro pimientas", ((Product)prices.get(98)[0]).getName());
//    	assertEquals(227.0F, prices.get(98)[1]);
//    }
//    
//    @Test
//    public void testGetProductsNotSold() {
//    	List<Product> products = this.service.getProductsNotSold();
//    	assertEquals(5,products.size());
//    	this.assertListEquality(products.stream().map(property -> property.getName()).collect(Collectors.toList()),Arrays.asList("Bastoncitos de mozzarella a la milanesa","Milanesa Suiza","Sándwich de lomo completo","Tarta de pollo (2 porc.)","Lomo a la mostaza con papas noisette"));
//    }
//    
//    @Test
//    public void testGetOrderWithMoreQuantityOfProducts() throws ParseException {
//    	List<Order> ord = this.service.getOrderWithMoreQuantityOfProducts(sdf.parse("23/6/2014"));
//    	assertEquals(1,ord.size());
//    	Order o = ord.get(0);
//    	assertEquals("Calle 34 Nº661", o.getAddress());
//    	assertEquals(2,o.getProducts().size());
//    	assertEquals(Float.valueOf("1867"), o.getAmount());
//    }
//    
//
//    @Test
//    public void testOrderAmount() {
//    	Optional<Order> ord = this.service.getOrderById(Long.valueOf("77"));// Se corrigio de acuerdo al error reportado
//    	if (ord.isPresent()) {
//    		Order o = ord.get();
//    		assertEquals(Float.valueOf("2454"),o.getAmount());
//    	}
//    }
//    
//    
//    
    private <T> void assertListEquality(List<T> list1, List<T> list2) {
        if (list1.size() != list2.size()) {
          Assert.fail("Lists have different size");
        }
        
        for (T objectInList1 : list1) {
            System.out.print("nombre producto: ");
            System.out.println(objectInList1);
        }

        for (T objectInList1 : list1) {
          if (!list2.contains(objectInList1)) {
            Assert.fail(objectInList1 + " is not present in list2");
          }
        }
      }
    
}
