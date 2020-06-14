package ar.edu.unlp.info.bd2.repositories;

import static com.mongodb.client.model.Aggregates.lookup;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.replaceRoot;
import static com.mongodb.client.model.Aggregates.unwind;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.bson.BsonDocument;
import org.bson.BsonElement;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.joda.time.chrono.LimitChronology;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import ar.edu.unlp.info.bd2.model.Order;
import ar.edu.unlp.info.bd2.model.Price;
import ar.edu.unlp.info.bd2.model.Product;
import ar.edu.unlp.info.bd2.model.User;
import ar.edu.unlp.info.bd2.mongo.Association;
import ar.edu.unlp.info.bd2.mongo.PersistentObject;

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
    
    public <T extends PersistentObject> List<T> getObjectsAssociatedWith(
            ObjectId objectId, Class<T> objectClass, String association, String destCollection) {
        AggregateIterable<T> iterable =
                this.getDb()
                        .getCollection(association, objectClass)
                        .aggregate(
                                Arrays.asList(
                                        match(eq("destination", objectId)),
                                        lookup(destCollection, "source", "_id", "_matches"),
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
       
    @SuppressWarnings("unchecked")
    public PersistentObject updateOn(String collectionName, Class collectionClass, PersistentObject object) {
    	Bson filter = Filters.eq(object.getObjectId());
    	return (PersistentObject) this.getDb().getCollection(collectionName, collectionClass).findOneAndReplace(filter, object);
    }

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
    }
    
    public List<Order> getOrdersWithActualStatus(String status){
    	String query = "{ 'actualStatus.status': '"+status+"' }";
    	Document doc = Document.parse(query);
    	FindIterable<Order> f = this.getDb().getCollection("orders", Order.class).find(doc);
    	Stream<Order> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(f.iterator(), 0), false);
        return stream.collect(Collectors.toList());
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
    public Product getProductMaxWeigth() {
    	String query = "{ weight:-1 }";
    	Document doc = Document.parse(query);
    	return this.getDb().getCollection("products", Product.class).find().sort(doc).limit(1).first();
    }

}
