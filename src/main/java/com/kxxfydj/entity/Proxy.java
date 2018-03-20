package com.kxxfydj.entity;

/**
 * Created by kxxfydj on 2018/3/14.
 */
public class Proxy implements Comparable{

    public Proxy(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Proxy(){}

    private String ip;

    private int port;

    private String type;

    private long speed;

    private int usedTimes;

    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getUsedTimes() {
        return usedTimes;
    }

    public void setUsedTimes(int usedTimes) {
        this.usedTimes = usedTimes;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Proxy proxy = (Proxy) o;

        return ip != null ? ip.equals(proxy.ip) : proxy.ip == null;
    }

    @Override
    public int hashCode() {
        return ip != null ? ip.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AutoProxy{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        Proxy proxy = (Proxy) o;
        long result = proxy.getSpeed() - this.getSpeed();
        if(result > 0){
            return 1;
        }else if(result == 0){
            return 0;
        }
        return -1;
    }
}
