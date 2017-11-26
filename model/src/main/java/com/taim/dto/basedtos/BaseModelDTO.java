package com.taim.dto.basedtos;

import javafx.beans.property.*;
import org.joda.time.DateTime;

public class BaseModelDTO {
    private IntegerProperty id;
    private ObjectProperty<DateTime> dateCreated;
    private ObjectProperty<DateTime> dateModified;
    private BooleanProperty deleted;

    public BaseModelDTO(){
        id = new SimpleIntegerProperty();
        dateCreated = new SimpleObjectProperty<>();
        dateModified = new SimpleObjectProperty<>();
        deleted = new SimpleBooleanProperty();
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public DateTime getDateCreated() {
        return dateCreated.get();
    }

    public ObjectProperty<DateTime> dateCreatedProperty() {
        return dateCreated;
    }

    public void setDateCreated(DateTime dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public DateTime getDateModified() {
        return dateModified.get();
    }

    public ObjectProperty<DateTime> dateModifiedProperty() {
        return dateModified;
    }

    public void setDateModified(DateTime dateModified) {
        this.dateModified.set(dateModified);
    }

    public boolean isDeleted() {
        return deleted.get();
    }

    public BooleanProperty deletedProperty() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted.set(deleted);
    }
}
