package epfl.sweng.dummy;

import org.apache.http.impl.client.AbstractHttpClient;

import epfl.sweng.servercomm.SwengHttpClientFactory;
import junit.framework.TestCase;

public class SwengHttpFactoryTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
	}
	
	public void testCreationSwengFactory() {
		
		SwengHttpClientFactory.setInstance(null);
		AbstractHttpClient factory = SwengHttpClientFactory.getInstance();
		
		assertTrue("Factory is not null", factory != null);
	}
	
}
