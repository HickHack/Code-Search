package graham.com.codesearch.search.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Graham Murray on 11/11/16.
 */

public class Repo implements Serializable {

    @SerializedName("html_url")
    private String profileUrl;

    @SerializedName("created_at")
    private String created;

    @SerializedName("updated_at")
    private String updated;

    private Owner owner;
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public String getCreated() {
        return cleanDate(created);
    }

    public String getUpdated() {
        return cleanDate(updated);
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getDescription() {
        return description;
    }

    public Owner getOwner() {
        return owner;
    }

    private String cleanDate(String data) {
        String[] exploded = data.split("T");
        String date = exploded[0];
        String time = exploded[1].split("Z")[0];

        return date + " " + time;
    }

}
