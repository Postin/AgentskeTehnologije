package app;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
public class MyApp extends Application{
	/* private Set<Object> singletons = new HashSet<>();

	    public MyApp() {
	        singletons.add(new CORSFilter());
	    }

	    @Override
	    public Set<Object> getSingletons() {
	        return singletons;
	    }*/
}
