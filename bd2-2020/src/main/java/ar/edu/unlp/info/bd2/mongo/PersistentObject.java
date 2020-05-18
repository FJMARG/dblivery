package ar.edu.unlp.info.bd2.mongo;

import org.bson.types.ObjectId;

public interface PersistentObject {
    public ObjectId getObjectId();
    public void setObjectId(ObjectId objectId);
}
