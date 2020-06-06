package ar.edu.unlp.info.bd2.repositories;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

import ar.edu.unlp.info.bd2.model.*;
import ar.edu.unlp.info.bd2.mongo.*;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.sun.xml.txw2.Document;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

public class DBliveryMongoRepository {

    @Autowired private MongoClient client;


    public void saveAssociation(PersistentObject source, PersistentObject destination, String associationName) {
        Association association = new Association(source.getObjectId(), destination.getObjectId());
        this.getDb()
                .getCollection(associationName, Association.class)
                .insertOne(association);
    }

    public MongoDatabase getDb() {
        return this.client.getDatabase("bd2_grupo21");
    }

    public <T extends PersistentObject> List<T> getAssociatedObjects(PersistentObject source, 
    																 Class<T> objectClass, 
    																 String association, 
    																 String destCollection) {
        AggregateIterable<T> iterable =
                this.getDb()
                        .getCollection(association, objectClass)
                        .aggregate(
                                Arrays.asList(
                                        match(eq("source", source.getObjectId())),
                                        lookup(destCollection, "destination", "_id", "_matches"),
                                        unwind("$_matches"),
                                        replaceRoot("$_matches")));
        Stream<T> stream =
                StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterable.iterator(), 0), false);
        return stream.collect(Collectors.toList());
    }
    
////////////////////    GENERIC METHODS   ///////////////////////////
    
    @SuppressWarnings("unchecked")
    public void insertInto(String collectionName, Class collectionClass, Object object) {
    	this.getDb().getCollection(collectionName, collectionClass).insertOne(object);
    }
    
//    @SuppressWarnings("unchecked")
//    public void updateOn(String collectionName, Class collectionClass, PersistentObject object) {
//    	
//    	Bson filter = Filters.eq(object.getObjectId());
//    	this.getDb().getCollection(collectionName, collectionClass).updateOne(filter, set("products"));
//    }
//    
    @SuppressWarnings("unchecked")
    public PersistentObject findById(String collectionName, Class collectionClass, ObjectId id){
    	PersistentObject obj = (PersistentObject) this.getDb().getCollection(collectionName, collectionClass).find(eq("_id", id)).first();
    	return obj;
    }
    
    
////////////////////    SPECIFIC METHODS   ///////////////////////////
    
//  for user
    public User getUserByField( String field, String value ) {
    	User user = (User)this.getDb().getCollection("users", User.class).find(eq(field, value)).first();
    	return user;
    }
    
//  for order
    public void insertOrder(Order order) {
    	this.getDb().getCollection("orders", Order.class).insertOne(order);
    	this.saveAssociation(order, order.getClient(), "orders_clients");
    	if(order.getDeliveryUser() != null) {
    		this.saveAssociation(order, order.getDeliveryUser(), "orders_deliveryUsers");
    	}
    	
    }
    
    public void insertProductOrder(ProductOrder productOrder) {
    	this.getDb().getCollection("productsOrders", ProductOrder.class).insertOne(productOrder);
    	this.saveAssociation(productOrder, productOrder.getProduct(), "productsOrders_products");
    }
    
//    for products   
    public void insertProduct(Product product) {
    	this.getDb().getCollection("prices", Price.class).insertOne(product.getActualPrice());
    	this.getDb().getCollection("products", Product.class).insertOne(product);
    	this.saveAssociation(product, product.getActualPrice(), "products_prices");
    	this.saveAssociation(product.getSupplier(), product, "suppliers_products");
    }
    
    public List<Product> getProductsByName( String name ) {
		AggregateIterable<Product> iterable =
                this.getDb()
                        .getCollection("products", Product.class)
                        .aggregate(
                                Arrays.asList(
                                        match(regex("name", name))));
        Stream<Product> stream =
                StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterable.iterator(), 0), false);
        return stream.collect(Collectors.toList());
    }
    
    public Product updateProductPrice(Product product) {
    	this.getDb().getCollection("prices", Price.class).insertOne(product.getActualPrice());
    	this.saveAssociation(product, product.getActualPrice(), "products_prices");
    	return product;
    }

}
