package dao;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkData {

	private static NetworkData instance;
	public static String MASTER_ADRESS = "192.168.56.1";
	//public static String MASTER_ADRESS = "192.168.0.20";
	
	public NetworkData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getAddress() {
		Enumeration<NetworkInterface> net = null;
        try { 
            net = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
        	e.printStackTrace();
        }
        
        while(net.hasMoreElements()){
            NetworkInterface element = net.nextElement();
            // loop through and print the IPs
			Enumeration<InetAddress> addresses = element.getInetAddresses();
			while (addresses.hasMoreElements()){
			    InetAddress ip = addresses.nextElement();
			    if (ip instanceof Inet4Address){
			        if (ip.isSiteLocalAddress()){
			        	 if(ip.getHostAddress().contains("192.168.56") ||
			        			 ip.getHostAddress().contains("192.168.0")) {
			        		 return ip.getHostAddress();
			            }
			        }
			    }
			}
        }
        
        return null;
	}

	public static NetworkData getInstance() {
		if (instance == null)
			instance = new NetworkData();
		return instance;
	}

	
	
	
}
