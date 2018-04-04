package mysh.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Nets
 *
 * @author 凯泓(zhixian.zzx@alibaba-inc.com)
 * @since 2017/11/28
 */
public class Nets {

    public static int ipv4Address2Int(InetAddress a) {
        byte[] addr = a.getAddress();
        if (addr.length != 4)
            throw new IllegalArgumentException("not-ipv4-addr:" + a);
        int ip = addr[3] & 0xFF;
        ip |= ((addr[2] << 8) & 0xFF00);
        ip |= ((addr[1] << 16) & 0xFF0000);
        ip |= ((addr[0] << 24) & 0xFF000000);
        return ip;
    }

    public static Inet4Address int2Ipv4Address(int ip) {
        byte[] addr = new byte[4];
        addr[0] = (byte) ((ip >>> 24) & 0xFF);
        addr[1] = (byte) ((ip >>> 16) & 0xFF);
        addr[2] = (byte) ((ip >>> 8) & 0xFF);
        addr[3] = (byte) (ip & 0xFF);
        try {
            return (Inet4Address) Inet4Address.getByAddress(addr);
        } catch (UnknownHostException e) {
            // impossible
            throw new RuntimeException("int2Ipv4Address-error:" + ip, e);
        }
    }
}