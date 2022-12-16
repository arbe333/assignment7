import java.util.Date;

/**
 * class entity
 * song, artist, album are subclasses of entity
 */
public class Entity {
    /**
     * name of entity
     */
    protected String name;
    /**
     * ID of entity
     */
    protected String entityID;
    /**
     * date of creation of entity
     */
    protected Date dateCreated;

    /**
     * getter of entityID
     * @return entityID
     */
    public String getEntityID() {
        return entityID;
    }

    /**
     * setter of entityID
     * @param entityID set this id as the entity's id
     */
    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    /**
     * constructor of entity
     * @param name name of entity
     * @param id id of entity
     */
    public Entity(String name, String id) {
        this.name = name;
        this.entityID = id;
        dateCreated = new Date();
    }

    /**
     * check if this and the other entity are equaled
     * @param otherEntity the other entity
     * @return true if they are equaled
     */
    public boolean equals(Entity otherEntity) {
        return entityID.equals(otherEntity.entityID);
    }

    /**
     * getter of name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * setter of name
     * @param name name of entity
     */
    public void setName(String name) {
        this.name = name;
    }
}
