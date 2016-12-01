package graham.com.codesearch.search.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Graham Murray on 17/11/16.
 */

public class Results {

    @SerializedName("items")
    private List<Repo> repoList;

    public Results() {
        this.repoList = new ArrayList<>();
    }

    public List<Repo> getRepos() {
        return repoList;
    }
}
