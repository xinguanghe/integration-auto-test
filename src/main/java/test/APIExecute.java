package test;

import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONArray;
import org.testng.Assert;
import org.testng.Reporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class APIExecute {

    public static String execute(String url, String heads, String input) throws Exception {
        String type = Json.getJsonValue(input, "Type");
        String data = Json.getJsonValue(input, "Data");
        JSONArray names = null;
        HttpRequest hrq;
        String uri = url.substring(url.indexOf(":")+1).trim();
        if ("URL".equals(type)) uri = uri + data;
        if(uri.indexOf("http")!=0){
//            URL tmURL = new URL(BasicTest.apiHost + uri);
//            URI tmURI = new URI(tmURL.getProtocol(), tmURL.getHost(), tmURL.getPath(), tmURL.getQuery(), null);
//            hrq = new HttpRequest(tmURI);
            hrq = new HttpRequest(new URI(BasicTest.apiHost + uri));

        }else{
            hrq = new HttpRequest(new URI(uri));
        }
        hrq.setMethod(url.substring(0,url.indexOf(":")).trim());
        Common.logPrinter("execute========>URL: "+hrq.getURI());
        Common.logPrinter("execute========>Method: "+hrq.getMethod());

        if(heads!=null && heads.trim().length()>2) {
            names = Json.getNames(heads);
            for (int i = 0; i < names.length(); i++) {
                hrq.setHeader(names.getString(i), Json.getJsonValue(heads, names.getString(i)));
                Common.logPrinter("execute========>SetHeader: " + names.getString(i) + " = " + Json.getJsonValue(heads, names.getString(i)));
            }
        }

        if ("Json".equals(type)) {
            hrq.setEntity(new StringEntity(data, "utf-8"));
            Common.logPrinter("execute========>SetEntity Json: " + data);
        }

        if ("K&V".equals(type)){
            List<NameValuePair> nameValues = new ArrayList<NameValuePair>();
            names = Json.getNames(data);
            if(names!=null){
                for(int i=0;i<names.length();i++) {
                    String value = Json.getJsonValue(data, names.getString(i));
                    Common.logPrinter("execute========>SetEntity: "+names.getString(i)+" = "+value);
                    NameValuePair nameValue = new BasicNameValuePair(names.getString(i), value);
                    nameValues.add(nameValue);
                }
            }
            hrq.setEntity(new UrlEncodedFormEntity(nameValues, "utf-8"));
        }

        if ("File".equals(type)){
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            names = Json.getNames(data);
            if(names!=null) {
                for (int i = 0; i < names.length(); i++) {
                    String src = Json.getJsonValue(data, names.getString(i));
                    Common.logPrinter("execute========>SetEntity: " + names.getString(i) + " = " + src);
                    File file = new File(src);
                    if (!file.exists()){
                        Common.logPrinter("execute========>File Not Exists: " + src);
                    } else {
                        FileInputStream inputStream = new FileInputStream(file);
                        builder.addBinaryBody(names.getString(i), inputStream, ContentType.create("multipart/form-data", Consts.UTF_8), src.substring(src.lastIndexOf("\\")+1));
                    }
                }
            }
            hrq.setEntity(builder.build());
        }

        if ("Form".equals(type)){
            String files = Json.getJsonValue(input, "File");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            if(data != null && data.length()>10) {
                names = Json.getNames(data);
                if(names!=null) {
                    for (int i = 0; i < names.length(); i++) {
                        String value = Json.getJsonValue(data, names.getString(i));
                        Common.logPrinter("execute========>SetEntity: " + names.getString(i) + " = " + value);
                        builder.addTextBody(names.getString(i), value, ContentType.create("multipart/form-data", Consts.UTF_8));
                    }
                }
            }

            if(files != null && files.length()>10) {
                names = Json.getNames(files);
                if(names!=null) {
                    for (int i = 0; i < names.length(); i++) {
                        String[] src = Json.getJsonValue(files, names.getString(i)).split(";");
                        for (int j = 0; j < src.length; j++) {
                            Common.logPrinter("execute========>SetEntity: " + names.getString(i) + " = " + src[j]);
                            File file = new File(src[j]);
                            if (!file.exists()){
                                Common.logPrinter("execute========>File Not Exists: " + src[j]);
                            } else {
                                FileInputStream inputStream = new FileInputStream(file);
                                builder.addBinaryBody(names.getString(i), inputStream, ContentType.create("multipart/form-data", Consts.UTF_8), src[j].substring(src[j].lastIndexOf("\\")+1));
                            }
                        }
                    }
                }
            }
            hrq.setEntity(builder.build());
        }

        HttpResponse response = HttpClients.createDefault().execute(hrq);
        HttpEntity entity = response.getEntity();

        String httpResults = response.getStatusLine().toString() + " - ";
        String save = Json.getJsonValue(input, "Save");
        if (save != null && save.length()>5){
            OutputStream fos = new FileOutputStream(save);
            entity.writeTo(fos);
            Reporter.log("<a href='" + save + "'>" + save + "</a>");
        } else {
            httpResults = httpResults + EntityUtils.toString(entity);
        }
        Common.logPrinter("execute========>httpResults: " + httpResults);

        return httpResults;
    }

    public static void assertResult(String str,String ass) throws InterruptedException {
        String start = Json.getJsonValue(ass,"Start");
        String end = Json.getJsonValue(ass,"End");
        String mold = Json.getJsonValue(ass,"Mold");
        String value = Json.getJsonValue(ass,"Value");

        int b=0, e=str.length();
        if (start != null && start.length() > 0)  b = str.indexOf(start) + start.length();
        if (end != null && end.length() > 0)  e = str.indexOf(end, b);
        //logPrinter("assertResult========>b: " + b + " - e: " + e);
        String sub = str.substring(b, e);
        Common.logPrinter("assertResult========>substring: " + sub);

        if ("Equal".equals(mold)) Assert.assertEquals(sub,value);
        else if ("Exclude".equals(mold)) Assert.assertTrue(!sub.contains(value));
        else if ("Sleep".equals(mold)) Thread.sleep(Integer.parseInt(value));
        else Assert.assertTrue(sub.contains(value));
    }

    public static int parametric(String str,String param,int index){
        String name = Json.getJsonValue(param,"Name");
        String start = Json.getJsonValue(param,"Start");
        String end = Json.getJsonValue(param,"End");

        int b=index, e=str.length();
        if (start != null && start.length() > 0)  b = str.indexOf(start,index) + start.length();
        if (end != null && end.length() > 0)  e = str.indexOf(end, b);
        BasicTest.paramMap.put(name,str.substring(b,e));
        Common.logPrinter("parametric========> " + name + "=" + BasicTest.paramMap.get(name));

        return e;
    }
}
