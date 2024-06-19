package test;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.android.AndroidDriver;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.tracing.opentelemetry.SeleniumSpanExporter;
import org.testng.Reporter;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Common {
    public static void setDBConnection(String json){
        Connection connection = null;
        logPrinter("setWebDriver=========>json: " + json);

        String driver = Json.getJsonValue(json,"driver");
        String clientName = Json.getJsonValue(json,"clientName");
        String url = Json.getJsonValue(json,"url");
        String user = Json.getJsonValue(json,"user").trim();
        String pass = Json.getJsonValue(json,"pass").trim();
        try {
            if(driver!=null && driver!="") Class.forName(driver);
            connection = DriverManager.getConnection(url,user,pass);
            //Statement statement = connection.createStatement();
        }catch (Exception e){
            logPrinter("getDatabaseConnect=========>Error: " + url + " - " + user + " - " + pass);
            e.printStackTrace();
        }
        BasicTest.clientMap.put(clientName,connection);
    }
    public static CloseableHttpClient buildSSLCloseableHttpClient() throws Exception {
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            // 信任所有
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }).build();
        // ALLOW_ALL_HOSTNAME_VERIFIER:这个主机名验证器基本上是关闭主机名验证的,实现的是一个空操作，并且不会抛出javax.net.ssl.SSLException异常。
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }
    public static void setWebDriver(String json) {
        WebDriver driver=null;
        logPrinter("setWebDriver=========>json: " + json);

        String clientName = Json.getJsonValue(json,"clientName");
        String driverName = Json.getJsonValue(json,"driverName").trim();
        String driverSRC = Json.getJsonValue(json,"driverSRC").trim();

        // 设置指定键对值的系统属性
        System.setProperty(driverName, driverSRC);
        if(driverName.indexOf("edge")>0) {
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--remote-allow-origins=*");
            driver = new EdgeDriver(options);
        }else if(driverName.indexOf("firefox")>0) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--remote-allow-origins=*");
            driver = new FirefoxDriver(options);
        }else if(driverName.indexOf("chrome")>0) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);
        }else {
            System.out.println("getWebDriver =======> WebClient: " + clientName + " 配置不正确。（\"driverName\"格式：webdriver.edge.driver）");
        }
        // 浏览器最大化
        driver.manage().window().maximize();
        //driver.manage().window().setSize(new Dimension(1280,1024));

        //隐式等待调用方式，5秒+时间单位(枚举类型)
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        BasicTest.clientMap.put(clientName,driver);
    }
    public static void setAndroidDriver(String json) throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        String clientName = Json.getJsonValue(json,"clientName");
        logPrinter("setAndroidDriver=========>json: " + json);

        if(Json.getJsonValue(json,"platformName").length()>0)
            desiredCapabilities.setCapability("platformName",Json.getJsonValue(json,"platformName"));
        if(Json.getJsonValue(json,"platformVersion").length()>0)
            desiredCapabilities.setCapability("platformVersion",Json.getJsonValue(json,"platformVersion"));
        if(Json.getJsonValue(json,"deviceName").length()>0)
            desiredCapabilities.setCapability("deviceName",Json.getJsonValue(json,"deviceName"));
        if(Json.getJsonValue(json,"app").length()>0)
            desiredCapabilities.setCapability("app",Json.getJsonValue(json,"app"));
        if(Json.getJsonValue(json,"appPackage").length()>0)
            desiredCapabilities.setCapability("app",Json.getJsonValue(json,"app"));
        if(Json.getJsonValue(json,"automationName").length()>0)
            desiredCapabilities.setCapability("automationName",Json.getJsonValue(json,"automationName"));
        if(Json.getJsonValue(json,"autoGrantPermissions").length()>0)
            desiredCapabilities.setCapability("autoGrantPermissions",Json.getJsonValue(json,"autoGrantPermissions"));
        if(Json.getJsonValue(json,"unicodeKeyboard").length()>0)
            desiredCapabilities.setCapability("unicodeKeyboard",Json.getJsonValue(json,"unicodeKeyboard"));
        if(Json.getJsonValue(json,"resetKeyboard").length()>0)
            desiredCapabilities.setCapability("resetKeyboard",Json.getJsonValue(json,"resetKeyboard"));

        AndroidDriver driver = new AndroidDriver(new URL(BasicTest.androidRemote),desiredCapabilities);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        BasicTest.clientMap.put(clientName,driver);
    }
    public static void setIOSDriver(String json) throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        String clientName = Json.getJsonValue(json,"clientName");
        logPrinter("setIOSDriver=========>json: " + json);

        if(Json.getJsonValue(json,"platformName").length()>0)
            desiredCapabilities.setCapability("platformName",Json.getJsonValue(json,"platformName"));
        if(Json.getJsonValue(json,"platformVersion").length()>0)
            desiredCapabilities.setCapability("platformVersion",Json.getJsonValue(json,"platformVersion"));
        if(Json.getJsonValue(json,"deviceName").length()>0)
            desiredCapabilities.setCapability("deviceName",Json.getJsonValue(json,"deviceName"));
        if(Json.getJsonValue(json,"app").length()>0)
            desiredCapabilities.setCapability("app",Json.getJsonValue(json,"app"));
        if(Json.getJsonValue(json,"udid").length()>0)
            desiredCapabilities.setCapability("udid",Json.getJsonValue(json,"udid"));
        if(Json.getJsonValue(json,"bundleId").length()>0)
            desiredCapabilities.setCapability("bundleId",Json.getJsonValue(json,"bundleId"));
        if(Json.getJsonValue(json,"automationName").length()>0)
            desiredCapabilities.setCapability("automationName",Json.getJsonValue(json,"automationName"));
        if(Json.getJsonValue(json,"autoGrantPermissions").length()>0)
            desiredCapabilities.setCapability("autoGrantPermissions",Json.getJsonValue(json,"autoGrantPermissions"));
        if(Json.getJsonValue(json,"unicodeKeyboard").length()>0)
            desiredCapabilities.setCapability("unicodeKeyboard",Json.getJsonValue(json,"unicodeKeyboard"));
        if(Json.getJsonValue(json,"resetKeyboard").length()>0)
            desiredCapabilities.setCapability("resetKeyboard",Json.getJsonValue(json,"resetKeyboard"));

        IOSDriver driver = new IOSDriver(new URL(BasicTest.iOSRemote),desiredCapabilities);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        BasicTest.clientMap.put(clientName,driver);
    }

    public static String executeCMD(String cmd) {
        Process process;
        ProcessBuilder builder;
        StringBuilder output = new StringBuilder();
        String osName = System.getProperty("os.name").toUpperCase(Locale.ENGLISH);
        logPrinter("executeCMD=========>osName: " + osName);
        try {
            if (osName.contains("WINDOWS")){
                builder = new ProcessBuilder("cmd.exe", "/c", cmd); // /c参数表示执行后关闭CMD窗口
                process = builder.start();
            }else {
                //builder = new ProcessBuilder("/bin/bash", "/c",cmd); // /c参数表示执行后关闭CMD窗口
                process = Runtime.getRuntime().exec(cmd);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int exitCode = process.waitFor(); // 等待进程结束并获取退出状态码
            output.append("ExitCode: ").append(exitCode).append("\n");

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            logPrinter("executeCMD=========>CMD Result: \n" + cmd + "\n" + output.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return output.toString();
    }

    public static void logPrinter(String str) {
        System.out.println(str);
        Reporter.log(str);
    }
}
