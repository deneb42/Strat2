import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public String getLocalIP(){
	    Enumeration<NetworkInterface> e;
	    
		try {
			e = NetworkInterface.getNetworkInterfaces();
			
		    while (e.hasMoreElements()){ 
		    	Enumeration<InetAddress> i = e.nextElement().getInetAddresses(); 
  
				while (i.hasMoreElements()) {
					InetAddress a = i.nextElement();

					if (a.isSiteLocalAddress() && (a instanceof Inet4Address)
							&& !a.isLoopbackAddress())
						return (a.getHostAddress());
				}
		    }
		} catch (SocketException e1) {
			e1.printStackTrace();
		} 
		
	    return null;
	}
