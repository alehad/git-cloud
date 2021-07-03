package alehad.cloud.test.msg.db;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import alehad.cloud.test.msg.model.IMessageStore;
import alehad.cloud.test.msg.model.Message;

public class MongoDatabaseImpl implements IMessageStore {

	// for now we will statically initialize mongo db
	// once this gets containerized, we will update the initialization bit
	
	private static MongoDatabaseImpl instance;
	
	private static MongoClient mongodbClient;
	private static MongoDatabase mongodb;
	private static MongoCollection<Document> mongodbCollection;
	
	private static String _mongodbName = "MessengerDB";
	private static String _mongodbCollectionName = "messages";
	
	private static String _msgId = "msgId";
	private static String _msg = "msg";

	
	public static MongoDatabaseImpl getInstance() {
		if (instance == null) {
			instance = new MongoDatabaseImpl();
		}
		return instance;
	}
	
	private MongoDatabaseImpl() {
		// will need to update host/port once moving to K8
		mongodbClient = new MongoClient("localhost", 27017);
		
		mongodb = mongodbClient.getDatabase(_mongodbName); // if not present, Mongo will create it
		// do we need to authenticate?
		
		boolean createCollection = true;
		
		MongoIterable<String> collections = mongodb.listCollectionNames();
		
		while (collections.iterator().hasNext() && createCollection) {
			if (collections.iterator().next().equalsIgnoreCase(_mongodbCollectionName)) {
				createCollection = false;
			}
		}

		if (createCollection) {
			mongodb.createCollection(_mongodbCollectionName); // create a table -- in Mongo terms that is a Collection
			mongodbCollection = mongodb.getCollection(_mongodbCollectionName);
			
			Document document = new Document();
			document.put(_msgId, 1);
			document.put(_msg, "hey mongo!");
			mongodbCollection.insertOne(document);
		}
		else {
			mongodbCollection = mongodb.getCollection(_mongodbCollectionName);
		}
	}
	
	@Override
	public List<Message> getMessages() {

		List<Message> messages = new ArrayList<Message>();
		
		FindIterable<Document> iterable = mongodbCollection.find();
		MongoCursor<Document> cursor = iterable.iterator();
		
		while (cursor.hasNext()) {
			Document doc = cursor.next();
			
			if (doc.containsKey(_msgId) && doc.containsKey(_msg)) {
				Message msg = new Message();
				int id = doc.getInteger(_msgId);
				msg.setId(id);
				msg.setMessage(doc.getString(_msg));
				messages.add(msg);
			}
		}
		return messages;
	}

	@Override
	public Message getMessage(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message createMessage(Message msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message updateMessage(int id, Message msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteMessage(int id) {
		// TODO Auto-generated method stub
		
	}
	
}