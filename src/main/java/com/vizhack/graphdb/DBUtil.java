package com.vizhack.graphdb;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class DBUtil {

    private static final String SERVER_ROOT_URI = "http://10.0.246.2:7474/db/data/";
    private static final String TXN_URI = SERVER_ROOT_URI
            + "transaction/commit";

    private WebResource resource;

    public DBUtil() {
        resource = Client.create().resource(TXN_URI);
    }

    private ClientResponse runQuery(String query) {
        String payload = "{\"statements\" : [ {\"statement\" : \"" + query
                + "\"} ]}";
        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON).entity(payload)
                .post(ClientResponse.class);

        return response;
    }

    public Boolean insertNode(String domain) {
        domain = escapeDomain(domain);
        if (isNodeExist(domain)) {
            return null;
        }

        String query = String.format("CREATE (%s:Domain {domain:'%s'})",
                domain, domain);
        ClientResponse response = runQuery(query);
        int status = response.getStatus();

        if (status >= 200 && status < 300) {
            return true;
        }
        return false;
    }

    public Boolean isNodeExist(String domain) {
        String query = String.format(
                "MATCH (n) WHERE n.domain='%s' RETURN 'yes'", domain);

        ClientResponse response = runQuery(query);
        int status = response.getStatus();

        if (status >= 200 && status < 300) {
            return response.getEntity(String.class).contains(
                    "{\"row\":[\"yes\"]}");
        }

        return false;
    }

    public Long getLastNode(String cookie) {
        return null;
    }

    public Boolean addRelation(String src, String dest, String cookie) {
        src = escapeDomain(src);
        dest = escapeDomain(dest);

        String query = String
                .format("MATCH (u:Domain), (r:Domain) WHERE u.domain='%s' and r.domain='%s' CREATE (u)-[%s:VISITED {cookie:'%s'}]->(r)",
                        src, dest, cookie, cookie);
        ClientResponse response = runQuery(query);
        int status = response.getStatus();

        if (status >= 200 && status < 300) {
            return true;
        }

        return false;
    }

    public void flushDB() {
        String query = "MATCH (n)-[r]->(m) DELETE n,r,m";
        String query1 = "MATCH (n) DELETE n";

        runQuery(query);
        runQuery(query1);
    }

    private String escapeDomain(String domain) {
        return domain.replaceAll("\\.", "\\$");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        DBUtil db = new DBUtil();
        // System.out.println(db.isNodeExist("test3"));
        db.flushDB();
    }

}
