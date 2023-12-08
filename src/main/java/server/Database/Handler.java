package server.Database;

import java.util.Properties;

public class Handler {

    private String uri;
    private Properties config;

    public Handler(String uri, Properties config) {
        this.uri = uri;
        this.config = config;
    }
}
