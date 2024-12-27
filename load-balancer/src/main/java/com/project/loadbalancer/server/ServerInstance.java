package com.project.loadbalancer.server;

public class ServerInstance {
    private final String url;

    public ServerInstance(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "ServerInstance{" + "url='" + url + '\'' + '}';
    }
}
