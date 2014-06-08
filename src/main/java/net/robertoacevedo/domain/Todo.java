package net.robertoacevedo.domain;

import java.sql.Timestamp;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by roberto on June 07, 2014.
 */
public class Todo {

    private UUID id;
    private String name;
    private String description;
    private Boolean checked;
    private Timestamp dateCreated;

    @Override
    public String toString() {
        String result =
                "[id: %s,\n" +
                " name: %s,\n" +
                " description: %s,\n" +
                " checked: %s,\n" +
                " dateCreated: %s]";
        return String.format(Locale.US, result, id, name, description, checked, dateCreated);

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }
}
