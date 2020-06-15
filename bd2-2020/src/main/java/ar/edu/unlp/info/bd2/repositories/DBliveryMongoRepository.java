package ar.edu.unlp.info.bd2.repositories;

import static com.mongodb.client.model.Aggregates.lookup;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.replaceRoot;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Aggregates.limit;
import static com.mongodb.client.model.Aggregates.unwind;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.sort;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Filters.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
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
            ObjectId objectId, 
            Class<T> objectClass, 
            String association, 
            String destCollection) {
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
    
    public List<Order> getDeliveredOrdersForUser( User user ){
    	AggregateIterable<Order> iterable =
                this.getDb()
                        .getCollection("orders_clients", Order.class)
                        .aggregate(
                                Arrays.asList(
                                        match(eq("destination", user.getObjectId())),
                                        lookup("orders", "source", "_id", "_matches"),                    
                                        unwind("$_matches"),                     
                                        replaceRoot("$_matches"),
                                        match(eq("actualStatus.status", "Delivered"))));
        Stream<Order> stream =
                StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterable.iterator(), 0), false);
        return stream.collect(Collectors.toList());
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
    
	public List<Order> getDeliveredOrdersInPeriod( Date startDate, Date endDate ){
    	AggregateIterable<Order> iterable = this.getDb().getCollection("orders", Order.class)
    			.aggregate(Arrays.asList(
    					match(
							and(
								eq("actualStatus.status", "Delivered"),
								gte("actualStatus.date", startDate ), 
								lte("actualStatus.date", endDate )))
    					));
    	
    	Stream<Order> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterable.iterator(), 0), false);
        return stream.collect(Collectors.toList());
    }
	
	public List<Order> getOrdersNear(String point){
		String query = "{$geoNear: {near: { type: 'Point', coordinates: "+point+"}, distanceField: 'coordinates', maxDistance: 400 }}";
    	Document doc = Document.parse(query);
    	List<Document> l = new ArrayList<Document>();
    	l.add(doc);
    	AggregateIterable<Order> iterable = this.getDb().getCollection("orders", Order.class).aggregate(l);
    	Stream<Order> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterable.iterator(), 0), false);
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
    
    public List<Product> getProductsOnePrice(){
    	String d1 = "{ \n" + 
    			"       $group: { \n" + 
    			"           _id: \"$source\",\n" + 
    			"           count: { $sum: 1 }\n" + 
    			"       }\n" + 
    			"   }";
    	String d2 = "{\n" + 
    			"       $match: {\n" + 
    			"           count: { $eq: 1 }\n" + 
    			"       }\n" + 
    			"   }";
    	String d3 = "{\n" + 
    			"       $lookup:{\n" + 
    			"           from: \"products\",\n" + 
    			"           localField: \"_id\",\n" + 
    			"           foreignField: \"_id\",\n" + 
    			"           as: \"p\"\n" + 
    			"     }\n" + 
    			"    }";
    	String d4 = "{\n" + 
    			"        $unwind:'$p'\n" + 
    			"    }";
    	String d5 = "{\n" + 
    			"        \"$group\": {\n" + 
    			"            \"_id\": \"$p._id\",\n" + 
    			"            \"p\": {\n" + 
    			"                \"$push\": {\n" + 
    			"                    \"_id\": \"$p._id\",\n" + 
    			"                    \"actualPrice\": \"$p.actualPrice\",\n" + 
    			"                    \"name\": \"$p.name\",\n" + 
    			"                    \"price\": \"$p.price\",\n" + 
    			"                    \"weight\": \"$p.weight\"\n" + 
    			"                }\n" + 
    			"            }\n" + 
    			"        }\n" + 
    			"    }";
    	String d6 = "{\n" + 
    			"        \"$unwind\": \"$p\"\n" + 
    			"    }";
    	String d7 = "{\n" + 
    			"        \"$project\": {\n" + 
    			"            \"_id\": \"$p._id\",\n" + 
    			"            \"actualPrice\": \"$p.actualPrice\",\n" + 
    			"            \"name\": \"$p.name\",\n" + 
    			"            \"price\": \"$p.price\",\n" + 
    			"            \"weight\": \"$p.weight\"\n" + 
    			"       }\n" + 
    			"    }";
    	Document doc1 = Document.parse(d1);
    	Document doc2 = Document.parse(d2);
    	Document doc3 = Document.parse(d3);
    	Document doc4 = Document.parse(d4);
    	Document doc5 = Document.parse(d5);
    	Document doc6 = Document.parse(d6);
    	Document doc7 = Document.parse(d7);
    	List<Document> l = new ArrayList<Document>();
    	l.add(doc1);
    	l.add(doc2);
    	l.add(doc3);
    	l.add(doc4);
    	l.add(doc5);
    	l.add(doc6);
    	l.add(doc7);
    	AggregateIterable<Product> iterable = this.getDb().getCollection("products_prices", Product.class).aggregate(l);
    	Stream<Product> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterable.iterator(), 0), false);
        return stream.collect(Collectors.toList());
    }
    
    
    public List<Product> getSoldProductsOn( Date day ) {
    	Date endDay = new Date(day.getYear(), day.getMonth(), day.getDate());
    	endDay.setHours(24);
    	
		AggregateIterable<Product> iterable = this.getDb().getCollection("orders", Product.class)
    			.aggregate(Arrays.asList(
    					match(
							and(
								gte("date", day), 
								lte("date", endDay )
							)
						),
						unwind("$products"),
						replaceRoot("$products.product")
    					));
    	Stream<Product> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterable.iterator(), 0), false);
        return stream.collect(Collectors.toList());
	}
	
    public Product getBestSellingProduct() {
    	AggregateIterable<Product> iterable = this.getDb().getCollection("orders", Product.class)
    			.aggregate(Arrays.asList(
						unwind("$products"),
						replaceRoot("$products"),
						group(
							"$product", 
							Arrays.asList(
								sum("totalQuantity", "$quantity")
							)
						),
						sort(descending("totalQuantity")),
						limit(1),
						replaceRoot("$_id")
    					));
    	Stream<Product> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterable.iterator(), 0), false);
        return stream.collect(Collectors.toList()).get(0);
    }

}
