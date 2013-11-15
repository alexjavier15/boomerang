package epfl.sweng.servercomm;

import org.apache.http.HttpResponse;

public class QueryHelper {

	private static QueryHelper instance = null;
	private String query = null;
	
	public static QueryHelper getInstance() {
		if (instance == null) {
			instance  = new QueryHelper();
		}
		return instance;
	}
	
	private QueryHelper(){
	}
	
	public void setQuery(String nwQuery) {
		query = nwQuery;
	}

	public HttpResponse processQuery(String mQuery) {
		// TODO Auto-generated method stub
		
	}
}
