package com.kxxfydj.entity;

/**
 * Created by kxxfydj on 2018/3/14.
 */
public class Proxy {

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

        Proxy autoProxy = (Proxy) o;

        if (port != autoProxy.port) return false;
        if (ip != null ? !ip.equals(autoProxy.ip) : autoProxy.ip != null) return false;
        return type != null ? type.equals(autoProxy.type) : autoProxy.type == null;
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + port;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AutoProxy{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", type='" + type + '\'' +
                '}';
    }
}
