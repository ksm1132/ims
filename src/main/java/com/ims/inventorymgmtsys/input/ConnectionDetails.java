package com.ims.inventorymgmtsys.input;

public class ConnectionDetails {
    private String url;
    private String username;
    private String password;
    private String dbname;
    private String schemaname;
    private String tablename;

    public ConnectionDetails(String url, String username, String password, String dbname, String schemaname, String tablename) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.dbname = dbname;
        this.schemaname = schemaname;
        this.tablename = tablename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getSchemaname() {
        return schemaname;
    }

    public void setSchemaname(String schemaname) {
        this.schemaname = schemaname;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }
}
