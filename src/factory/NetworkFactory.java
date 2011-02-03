
package factory;

import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * Network information
 * @author Ahmed Ghanem.
 */
public class NetworkFactory {

    private InetAddress address;
/***
 * get address
 * @throws UnknownHostException error on host
 */
    public NetworkFactory() throws UnknownHostException {
        address = InetAddress.getLocalHost();
    }
    /***
     * get host name
     * @return host name
     */
    public String getHostName(){
        return address.getCanonicalHostName();
    }
    /***
     * to get ip address
     * @return IP address
     */
    public String getIp(){
        return address.getHostAddress();
    }
}
