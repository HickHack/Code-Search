package graham.com.codesearch.search;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by graham on 01/12/16.
 */
public class SuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "graham.com.codesearch.search.SuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
