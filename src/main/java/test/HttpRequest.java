package test;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;


public class HttpRequest extends HttpEntityEnclosingRequestBase {
    public static String METHOD_NAME = "GET";

    public HttpRequest() {
    }

    public HttpRequest(URI uri) {
        this.setURI(uri);
    }

    public HttpRequest(String uri) {
        this.setURI(URI.create(uri));
    }

    public void setMethod(String method) { this.METHOD_NAME = method; }

    public String getMethod() {
        return METHOD_NAME;
    }
}
