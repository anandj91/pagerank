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

        System.out
                .println(String.format(
                        "POST [%s] to [%s], status code [%d], returned data: "
                                + System.getProperty("line.separator") + "%s",
                        payload, TXN_URI, response.getStatus(),
                        response.getEntity(String.class)));

        return response;
    }

    public Boolean insertNode(String domain) {
        String query = String.format("create (%s:Domain)", domain);

        ClientResponse response = runQuery(query);
        int status = response.getStatus();

        if (status >= 200 && status < 300) {
            return true;
        }
        return false;
    }

    public Long getNode(String domain) {
        String query = "";
        
        ClientResponse response = runQuery(query);
        int status = response.getStatus();

        if (status >= 200 && status < 300) {
            return null;
        }
        return null;
    }

    public Long getLastNode(String cookie) {
        return null;
    }

    public Long addRelation(long src, long dest, String cookie) {
        return null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

    }

}
