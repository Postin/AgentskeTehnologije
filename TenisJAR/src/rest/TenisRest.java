package rest;

import model.AID;

public interface TenisRest {

	public String runAgent(String type, String name);
	
	public String stopAgent(AID aid);
	
	public String test();
}
