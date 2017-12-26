package tech.qi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Properties;


public class IPAddressUtil {

    private static final Logger logger = LoggerFactory.getLogger(IPAddressUtil.class);
    /***
     * 获取客户端IP地址;这里通过了Nginx获取;X-Real-IP,
     * @param request
     * @return
     */
    public static String getClientIP(HttpServletRequest request) {
        try {
            String ip = request.getHeader("X-Real-IP");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Forwarded-For");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            return ip;
        }catch (Exception e) {
            logger.error("获取客户端IP地址异常", e);
            return null;
        }
    }

    public static String getLocalhostIp() {
        try {
            String osname = getSystemOSName();
            String ip = null;
            // 针对window系统
            if (osname.contains("Windows")) {
                ip = getWinLocalIp();
                // 针对linux系统
            } else if (osname.equalsIgnoreCase("Linux")) {
                ip = getUnixLocalIp();
            }
            //logger.debug("本地操作系统为：" + osname + ",本机的ip=" + ip);
            return ip;
        } catch (Exception e) {
            logger.error("获取本地IP地址异常", e);
            return null;
        }
    }

    /**
     * 获取FTP的配置操作系统
     *
     * @return
     */
    public static String getSystemOSName() {
        // 获得系统属性集
        Properties props = System.getProperties();
        // 操作系统名称
        String osname = props.getProperty("os.name");
        return osname;
    }


    /**
     * 获取window 本地ip地址
     *
     * @return
     * @throws UnknownHostException
     */
    private static String getWinLocalIp() throws UnknownHostException
    {
        String ip = InetAddress.getLocalHost().getHostAddress();
        return ip;
    }

    /**
     *
     * 可能多多个ip地址只获取一个ip地址 获取Linux 本地IP地址
     *
     * @return
     * @throws SocketException
     */
    private static String getUnixLocalIp() throws SocketException {
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface
                .getNetworkInterfaces();
        InetAddress ip = null;
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface ni = (NetworkInterface) netInterfaces
                    .nextElement();
            ip = (InetAddress) ni.getInetAddresses().nextElement();
            if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                    && ip.getHostAddress().indexOf(":") == -1) {
                return ip.getHostAddress();
            } else {
                ip = null;
            }
        }
        return null;
    }
}
