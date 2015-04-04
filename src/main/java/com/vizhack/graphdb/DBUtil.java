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

    private int runQuery(String query) {
        String payload = "{\"statements\" : [ {\"statement\" : \"" + query
                + "\"} ]}";
        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON).entity(payload)
                .post(ClientResponse.class);

        System.out
                .println(String.format(
                        "POST [%s] to [%s], status code [%d], returned data: "
                                + System.getProperty("line.separator") + "%s",
                        payload, TXN_URI, response.getStatus(),
                        response.getEntity(String.class)));

        int status = response.getStatus();
        response.close();
        return status;
    }

    public Boolean insertNode(String cookie, String domain, String time) {
        String query = "";
        return true;
    }

    public Long getNode(String domain) {
        String query = "";
        return 1l;
    }
    
    public Long getLastNode(String cookie) {
        return 1l;
    }

    public Long addRelation(long src, long dest, String cookie) {
        String query = "";
        return 1l;
    }

    

    /**
     * @param args
     */
    public static void main(String[] args) {

    }

}
