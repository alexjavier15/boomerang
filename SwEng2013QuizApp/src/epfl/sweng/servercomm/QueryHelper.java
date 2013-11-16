package epfl.sweng.servercomm;

import org.apache.http.HttpResponse;

public final class QueryHelper {

    private static QueryHelper instance = null;

    public static QueryHelper getInstance() {
        if (instance == null) {
            instance = new QueryHelper();
        }
        return instance;
    }

    private QueryHelper() {
    }

    public void setQuery(String nwQuery) {
    }

    public HttpResponse processQuery(String mQuery) {
        return null;
        // TODO Auto-generated method stub

    }
}
