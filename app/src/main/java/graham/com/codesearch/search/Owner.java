package graham.com.codesearch.search;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Graham Murray on 18/11/16.
 */

public class Owner implements Serializable {

    @SerializedName("html_url")
    private String htmlUrl;
    @SerializedName("avatar_url")
    private String avatarUrl;
    @SerializedName("login")
    private String username;
    private byte[] image;

    public Owner() {}

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

}
