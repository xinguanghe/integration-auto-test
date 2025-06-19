package test;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.http.impl.client.CloseableHttpClient;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;
import org.testng.reporters.Files;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.LocalDate;
import java.util.*;
import java.time.Duration;

//@Listeners({TestResultListener.class})
public class BasicTest {//声明驱动对象
    public static  CloseableHttpClient apiClient = null;
    public static  WebDriver webDriver=null;
    public static  AndroidDriver androidDriver = null;
    public static  IOSDriver iOSDriver = null;
    public static  Connection connection = null;
    public static  JavascriptExecutor jse = null;
    public static  TouchAction action = null;
    public static  HashMap paramMap = new HashMap();
    public static  HashMap xpathMap = new HashMap();
    public static  HashMap clientMap = new HashMap();
    public static  String apiHost = "";
    public static  String webHost = "";
    public static  String androidRemote = "";
    public static  String iOSRemote = "";
    public static  String excel = "";
    public static  String sheet = "";
    public static  String runType = "";
    public static  String jenkinsWS = "";
    public static  String capabilities = "";
    public static  String device = "";
    public static  String app = "";
    public static  String tcID = "";
    public static  String down="";
    public static  String result="";

    @BeforeSuite()
    @Parameters(value = { "APIHost","WebClient","AndroidClient","iOSClient","DatabaseClient","Excel","Pass","Down","JenkinsWS","WebHost","sheet","runType" })
    public void setUp(String apiHost,String webClient,String androidClient,String iOSClient,String databaseClient,String excel,String pass,String down,String jenkinsWS,String webHost,String sheet,String runType) throws Exception {
        System.out.println("BeforeSuite setUp========>");
        this.webHost = webHost.trim();
        this.sheet = sheet.trim();
        this.runType = runType.trim();
        System.out.println("setUp========>sheet: "+sheet);
        System.out.println("setUp========>runType: "+runType);

        Properties prop = new Properties();
        this.apiHost = apiHost.trim();
        this.excel = excel.trim();
        this.down = down.trim();
        this.jenkinsWS = jenkinsWS.trim();
        apiClient = Common.buildSSLCloseableHttpClient();

        if(databaseClient!=null && databaseClient.trim().length()>0){
            // 通过输入缓冲流进行读取配置文件 加载输入流
            prop.load(new BufferedInputStream(new FileInputStream(new File(databaseClient))));

            int index = 1;
            while (prop.getProperty("Connection"+index) != null && prop.getProperty("Connection"+index).trim().length() > 0) {
                Common.setDBConnection(prop.getProperty("Connection"+index).trim());
                index += 1;
            }
        }
        if(webClient!=null && webClient.trim().length()>0){
            // 通过输入缓冲流进行读取配置文件 加载输入流
            prop.load(new BufferedInputStream(new FileInputStream(new File(webClient))));

            int index = 1;
            while (prop.getProperty("Client"+index) != null && prop.getProperty("Client"+index).trim().length() > 0) {
                Common.setWebDriver(prop.getProperty("Client"+index).trim());
                index += 1;
            }
        }
        if(androidClient!=null && androidClient.trim().length()>0){
            // 通过输入缓冲流进行读取配置文件 加载输入流
            prop.load(new BufferedInputStream(new FileInputStream(new File(androidClient))));
            this.androidRemote = prop.getProperty("Remote").trim();

            int index = 1;
            while (prop.getProperty("Capabilities"+index) != null && prop.getProperty("Capabilities"+index).trim().length() > 0) {
                Common.setAndroidDriver(prop.getProperty("Capabilities"+index).trim());
                index += 1;
            }
        }
        if(iOSClient!=null && iOSClient.trim().length()>0){
            // 通过输入缓冲流进行读取配置文件 加载输入流
            prop.load(new BufferedInputStream(new FileInputStream(new File(iOSClient))));
            this.iOSRemote = prop.getProperty("Remote").trim();

            int index = 1;
            while (prop.getProperty("Capabilities"+index) != null && prop.getProperty("Capabilities"+index).trim().length() > 0) {
                Common.setIOSDriver(prop.getProperty("Capabilities"+index).trim());
                index += 1;
            }
        }

        //下载WPS云文档测试用例
        if(excel.indexOf("http")==0){
            webDriver = (WebDriver) clientMap.get("Web");
            webDriver.get(excel);
            Thread.sleep(5000);
            this.excel = down;
            operation("{\"Type\":\"Set\",\"Local\":\"Class:password-input\",\"Value\":\""+pass+"\",\"Index\":\"0\"};","Web");
            operation("{\"Type\":\"Click\",\"Local\":\"XPath://button[text()='确定']\",\"Index\":\"0\",\"Sleep\":\"60\"};","Web");
            operation("{\"Type\":\"Click\",\"Local\":\"XPath://button[text()='下载']\",\"Index\":\"0\",\"Sleep\":\"60\"};","Web");
            //xpathMap = DataRead.getXPath(this.excel);
            //assertResult("{\"AssertType\":\"File\",\"Value\":\""+down+"\",\"Mold\":\"Include\",\"Delete\":\"Yes\"};","Web");
//            String url = "GET:https://wwww.kdocs.cn/api/office/file";
//            url = url + excel.substring(excel.lastIndexOf('/')) + "/download";
//            String result = APIExecute.execute(url,"","");
//            Common.logPrinter("setUp========>test case result1: " + result);
//            int index = result.indexOf("\"download_url\":\"")+16;
//            url = "GET:" + result.substring(index,result.indexOf("\"",index));
//            url = url.replaceAll("\\\\u0026","&");
//            result = APIExecute.execute(url,"","{\"Save\":\"./Integration-Auto-TestCase.xls\"}");
//            Common.logPrinter("setUp========>test case result2: " + result);
//            this.excel = "./Integration-Auto-TestCase.xls";
        }
        xpathMap = DataRead.getXPath(this.excel);
        //datas = DataRead.getDataProvider(this.excel,this.sheet,this.runType);
    }

    @BeforeTest()
    @Parameters(value = { "WebHost","sheet","runType"})
    public void setUp(String webHost,String sheet,String runType){
        System.out.println("BeforeTest setUp========>");
        this.webHost = webHost.trim();
        this.sheet = sheet.trim();
        this.runType = runType.trim();
        System.out.println("setUp========>sheet: "+sheet);
        System.out.println("setUp========>runType: "+runType);
    }

    @DataProvider(name = "createTestData")
    public Object[][] createTestData(){
        Common.logPrinter("createTestData========>excel: " + excel);
        Common.logPrinter("createTestData========>sheet: " + sheet);
        //return datas;
        return DataRead.getDataProvider(excel,sheet,runType);
    }

    @Test(dataProvider = "createTestData")
    public void runTest(String tcID,String url,String operates,String asserts,String parametric,String client) throws Exception {
        Common.logPrinter("runTest========> " + sheet + ": "+ tcID);
        this.tcID = tcID.substring(tcID.indexOf("|", 1) + 1, tcID.indexOf(" | "));
        Common.logPrinter("runTest========> client: " + client);
        Common.logPrinter("runTest========>Operates: \n" + operates );
        if(url.length()>1 && url.indexOf("/")<0) url = (String) xpathMap.get(url);
        url = paramReplace(url);
        Common.logPrinter("runTest========>URL: " + url );

        if("API".equals(client)){
            //执行非API测试的操作
            operates = paramReplace(operates);
            String httpResult = APIExecute.execute(url,Json.getJsonValue(operates,"Headers"),Json.getJsonValue(operates,"Inputs"));
            Common.logPrinter("runTest========>Parametric: \n"+parametric );
            String[] params = parametric.split(";" );
            int index = 0;
            for (int i = 0; i < params.length; i++) {
                if (params[i].trim().length() > 0 && params[i].trim().indexOf("{") == 0) {
                    params[i] = paramReplace(params[i]);
                    Common.logPrinter("runTest========>param: " + params[i]);
                    index = APIExecute.parametric(httpResult,params[i].trim(),index);
                }else if(params[i].trim().length() > 0){
                    Common.logPrinter("runTest========>跳过参数化操作: " + params[i]);
                }
            }

            Common.logPrinter("runTest========>Asserts: \n"+asserts );
            String[] ass = asserts.split(";" );
            for (int i = 0; i < ass.length; i++) {
                if (ass[i].trim().length() > 0 && ass[i].trim().indexOf("{") == 0) {
                    ass[i] = paramReplace(ass[i]);
                    Common.logPrinter("runTest========>assert: " + ass[i]);
                    APIExecute.assertResult(httpResult,ass[i].trim());
                }else if(ass[i].trim().length() > 0){
                    Common.logPrinter("runTest========>跳过断言: " + ass[i]);
                }
            }
        }else if(client.indexOf("DB")==0){
            connection = (Connection) clientMap.get(client);
            operates = paramReplace(operates);
            String result = "";
            if(operates.indexOf("Select:")==0) {
                operates = operates.substring(7).trim();
                Common.logPrinter("runTest========>SQL: \n"+operates );
                ResultSet rs = connection.prepareStatement(operates).executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int col = rsmd.getColumnCount();
                if (col > 100) col = 100;
                int row = 1;
                for (int i = 1; i <= col; i++) {
                    if (i == col) {
                        result = result + " | " + rsmd.getColumnName(i) + " |\n";
                    } else {
                        result = result + " | " + rsmd.getColumnName(i);
                    }
                }
                while(rs.next()){
                    result = result + "||" + (row++) + "|| ";
                    for (int i = 1; i <= col; i++) {
                        String value = rs.getString(i);
                        if (i == col) {
                            result = result + value + " |\n";
                        } else {
                            result = result + value + " | ";
                        }
                    }
                }
                Common.logPrinter("runTest========>ResultSet Data: \n" + result);
            }else {
                operates = operates.substring(7).trim();
                Common.logPrinter("runTest========>SQL: \n" + operates );
                int count = connection.prepareStatement(operates).executeUpdate();
                Common.logPrinter("runTest========>SQL 所影响的记录行数: " + count );
                Assert.assertTrue(count>=0);
                //Common.logPrinter("runTest========>ResultSet: \n"+result.toString() );
            }

            Common.logPrinter("runTest========>Parametric: \n" + parametric );
            String[] params = parametric.split(";" );
            int index = 0;
            for (int i = 0; i < params.length; i++) {
                if (params[i].trim().length() > 0 && params[i].trim().indexOf("{") == 0) {
                    params[i] = paramReplace(params[i]);
                    Common.logPrinter("runTest========>param: " + params[i]);
                    index = APIExecute.parametric(result,params[i].trim(),index);
                }else if(params[i].trim().length() > 0){
                    Common.logPrinter("runTest========>跳过参数化操作: " + params[i]);
                }
            }

            Common.logPrinter("runTest========>Asserts: \n"+asserts );
            String[] ass = asserts.split(";" );
            for (int i = 0; i < ass.length; i++) {
                if (ass[i].trim().length() > 0 && ass[i].trim().indexOf("{") == 0) {
                    ass[i] = paramReplace(ass[i]);
                    Common.logPrinter("runTest========>assert: " + ass[i]);
                    APIExecute.assertResult(result,ass[i].trim());
                }else if(ass[i].trim().length() > 0){
                    Common.logPrinter("runTest========>跳过断言: " + ass[i]);
                }
            }
        }else if(client.indexOf("Web")==0){
            webDriver = (WebDriver) clientMap.get(client);
            if ( url.length() > 2 ) {
                if (url.indexOf("http")!=0) url = webHost + url;
                webDriver.get(url);
                webDriver.navigate().refresh();
                Thread.sleep(3000);
                Common.logPrinter("runTest========>Open: " + url );
            }
        }else if(client.indexOf("Android")==0){
            androidDriver = (AndroidDriver) clientMap.get(client);
            if (url.length() > 2) {
                //url = (String) xpathMap.get(url);
                //Common.logPrinter("runTest========>StartActivity: " + url);
                String pkg = url.substring(0, url.indexOf(":")).trim();
                String act = url.substring(url.indexOf(":") + 1).trim();
                androidDriver.startActivity(new Activity(pkg, act));
                Thread.sleep(2000);
                Common.logPrinter("runTest========>Open: " + url );
            }
        }else if(client.indexOf("iOS")==0){
            iOSDriver = (IOSDriver) clientMap.get(client);
            if (url.length() > 2) {
                iOSDriver.terminateApp(url);
                iOSDriver.activateApp(url);
                Thread.sleep(2000);
                Common.logPrinter("runTest========>Open: " + url );
            }
        }

        //执行非API测试的操作
        if (client.indexOf("Web")==0 || client.indexOf("Android")==0 || client.indexOf("iOS")==0 || client.indexOf("CMD")==0) {
            if (operates.indexOf("[{")==0) {
                try {
                    JSONArray opts = new JSONArray(operates);
                    for (int i=0; i<opts.length();i++)
                        explainSIDE(opts.get(i).toString(),client);
                } catch (Exception e) {
                    System.out.println("runTest========>JsonString非法: " + operates);
                }
            } else {
                String[] opts = operates.split(";");
                for (int i = 0; i < opts.length; i++) {
                    try {
                        if (opts[i].trim().length() > 0 && opts[i].trim().indexOf("{") == 0) {
                            opts[i] = paramReplace(opts[i]);
                            Common.logPrinter("runTest========>operate: " + opts[i]);
                            operation(opts[i].trim(), client);
                            if (tcID.indexOf("#") == 1)
                                screenShotAsFile(sheet + "-" + this.tcID + "-" + i, client);
                        }else if (opts[i].trim().length() > 0){
                            Common.logPrinter("runTest========>跳过测试步骤: " + opts[i]);
                        }
                    } catch (Exception e) {
                        screenShotAsFile(sheet + "-" + this.tcID + "-Fail-" + i, client);
                        Common.logPrinter("runTest========>Exception: " + e.toString());
                        e.printStackTrace();
                    }
                }
            }
            screenShotAsFile(sheet + "-" + this.tcID, client);

            Common.logPrinter("runTest========>parametric: \n" + parametric);
            String[] params = parametric.split(";");
            for (int i = 0; i < params.length; i++) {
                if (params[i].trim().length() > 0 && params[i].trim().indexOf("{") == 0) {
                    params[i] = paramReplace(params[i]);
                    Common.logPrinter("runTest========>param: " + params[i]);
                    parametric(params[i].trim(),client);
                }else if (params[i].trim().length() > 0){
                    Common.logPrinter("runTest========>跳过参数化操作: " + params[i]);
                }
            }

            Common.logPrinter("runTest========>asserts: \n" + asserts);
            String[] ass = asserts.split(";");
            for (int i = 0; i < ass.length; i++) {
                if (ass[i].trim().length() > 0 && ass[i].trim().indexOf("{") == 0) {
                    ass[i] = paramReplace(ass[i]);
                    Common.logPrinter("runTest========>assert: " + ass[i]);
                    assertResult(ass[i].trim(),client);
                }else if (ass[i].trim().length() > 0){
                    Common.logPrinter("runTest========>跳过断言: " + ass[i]);
                }
            }
        }
    }

    @AfterSuite()
    public void after() {
        Iterator<Map.Entry<Object, Object>> iterator = clientMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            String key = (String)entry.getKey();
            if(key!=null && key.indexOf("Web")==0) {
                WebDriver driver = (WebDriver) entry.getValue();
                driver.close();
            }
            if(key!=null && key.indexOf("Android")==0) {
                AndroidDriver driver = (AndroidDriver) entry.getValue();
                driver.quit();
            }
            if(key!=null && key.indexOf("iOS")==0) {
                IOSDriver driver = (IOSDriver) entry.getValue();
                driver.quit();
            }
            if(key!=null && key.indexOf("DB")==0) {
                Connection connection = (Connection) entry.getValue();
                try {
                    connection.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            //if(key!=null && key.contains("Web")) driver.close(); else driver.quit();
        }
        clientMap.clear();
        if(down.length()>0) {
            File file = new File(down);
            file.delete();
        }
    }

    public void explainSIDE(String opt,String client) throws Exception {
        JSONObject json = new JSONObject();
        json.put("Index","0");
        Common.logPrinter("explainSIDE========>src opt: " + opt);

        String command = Json.getJsonValue(opt,"command");
        if("click".equals(command)) {json.put("Type","Click");json.put("Local",changeTarget(opt));}
        else if("open".equals(command)) {json.put("Type","Open");json.put("Local",changeTarget(opt));}
        else if("type".equals(command)) {json.put("Type","Set");json.put("Local",changeTarget(opt));json.put("Value",Json.getJsonValue(opt,"value"));}
        else if("select".equals(command)) {json.put("Type","Select");json.put("Local",changeTarget(opt));json.put("Value",Json.getJsonValue(opt,"value"));}
        else if("mouseOver".equals(command)) {json.put("Type","Move");json.put("Local",changeTarget(opt));}
        else {Common.logPrinter("explainSIDE========>无法解析此操作，请联系开发人员。");}

        Common.logPrinter("explainSIDE========>dest opt: " + json.toString());
        try {
            operation(json.toString(), client);
        }catch (Exception e){
            Common.logPrinter("explainSIDE========>操作失败: " + json.toString());
        }
    }
    public String changeTarget(String opt){
        String target = Json.getJsonValue(opt,"target").trim();
        Common.logPrinter("changeTarget========>src target: " + target);

        if(target.indexOf("id=")==0) target = target.replace("id=","ID:");
        if(target.indexOf("class=")==0) target = target.replace("class=","Class:");
        if(target.indexOf("xpath=")==0) target = target.replace("xpath=","XPath:");
        if(target.indexOf("css=")==0) target = target.replace("css=","CSS:");
        if(target.indexOf("name=")==0) target = target.replace("name=","Name:");
        if(target.indexOf("tag=")==0) target = target.replace("tag=","Tag:");

        Common.logPrinter("changeTarget========>dest target: " + target);
        return target;
    }
    public void operation(String opt,String client) throws Exception {
        String type = Json.getJsonValue(opt,"Type");
        if("Click".equals(type)) getElement(opt,client).click();
        if("ClickXY".equals(type)) clickXY(opt,client);
        if("ClickPre".equals(type)) clickPre(opt,client);
        if("ClickIF".equals(type) && getElement(opt,client)!=null) getElement(opt,client).click();
        if("ClickText".equals(type)) {
            List<WebElement> wes = getTextWebElement(opt,client);
            if("Last".equals(Json.getJsonValue(opt,"Index")))
                wes.get(wes.size()-1).click();
            else
                wes.get(Integer.parseInt(Json.getJsonValue(opt,"Index"))).click();
        }
        if("Set".equals(type)) {
            WebElement webElement = getElement(opt,client);
            webElement.clear();
            webElement.sendKeys(Json.getJsonValue(opt,"Value"));
        }
        if("Clear".equals(type)) getElement(opt,client).clear();
        if("Submit".equals(type)) getElement(opt,client).submit();
        if("Alert".equals(type)){
            if ("Accept".equals(Json.getJsonValue(opt,"Value"))) ((WebDriver)clientMap.get(client)).switchTo().alert().accept();
            if ("Dismiss".equals(Json.getJsonValue(opt,"Value"))) ((WebDriver)clientMap.get(client)).switchTo().alert().dismiss();
        }
        if("Move".equals(type)) (new Actions((WebDriver)clientMap.get(client))).moveToElement(getElement(opt,client)).perform();
        if("Sleep".equals(type)) Thread.sleep(Integer.parseInt(Json.getJsonValue(opt,"Value")));
        if("Assert".equals(type)) assertResult(opt,client);
        if("Parametric".equals(type)) parametric(opt,client);
        if("JS".equals(type)) ((JavascriptExecutor)clientMap.get(client)).executeScript(Json.getJsonValue(opt,"JSCode"));
        if("Shot".equals(type)) screenShotAsFile(sheet + "-" + Json.getJsonValue(opt,"Name"),client);
        if("Swipe".equals(type)) swipe(opt,client);
        if("Open".equals(type)) open(opt,client);
        if("Window".equals(type)) window(opt,client);
        if("Frame".equals(type)) frame(opt,client);
        if("Select".equals(type)) select(opt,client);
        if("CMD".equals(type)) runCMD(opt);

        String sleep = Json.getJsonValue(opt, "Sleep");
        if (sleep != null && sleep != "") {
            Common.logPrinter("operation========>Sleep: " + sleep);
            Thread.sleep(Integer.parseInt(sleep) * 1000);
        }else {
            Thread.sleep(1000);
        }
    }
    public void runCMD(String json){
        String cmd = Json.getJsonValue(json,"CMD").trim();
        Common.logPrinter("runCMD========>CMD: " + cmd);
        if(cmd.indexOf("adb ")==0)
            result = Common.executeCMD(cmd);
        else
            Common.logPrinter("runCMD========>只允许执行adb命令！");
        //Assert.assertEquals(result.indexOf("ExitCode: 0"),0);
    }
    public void select(String json,String client){
        Select se = new Select(getElement(json,client));
        String value = Json.getJsonValue(json,"Value");
        Common.logPrinter("select========>Text: " + value);
        se.selectByVisibleText(value);
        //se.selectByValue(value);
    }
    public void frame(String json,String client){
        WebDriver wd = (WebDriver)clientMap.get(client);
        Set<String> allWindowsId = wd.getWindowHandles();
        String value = Json.getJsonValue(json,"Value");
        Common.logPrinter("frame========>ID or Name: " + value);
        wd.switchTo().frame(value);
    }
    public void window(String json,String client) throws InterruptedException {
        WebDriver wd = (WebDriver)clientMap.get(client);
        Set<String> allWindowsId = wd.getWindowHandles();
        String value = Json.getJsonValue(json,"Value");
        int  index = Integer.parseInt(Json.getJsonValue(json,"Index"));

        // 获取所有的打开窗口的句柄
        int i = 0;
        for (String windowId : allWindowsId) {
            wd.switchTo().window(windowId);
            if(wd.getTitle().equals("帮助我们为你改进搜索")) continue;
            Common.logPrinter("window========>Window Title " + i + " : " + wd.getTitle());
            if(Json.getJsonValue(json,"Index")!=null && i==Integer.parseInt(Json.getJsonValue(json,"Index"))){
                Common.logPrinter("window========>Select Window" + i + " : " + wd.getTitle());
                //wd.switchTo().window(windowId);
                break;
            }
            if (wd.getTitle().equals(value)) {
                Common.logPrinter("window========>Select Window" + i + " : " + wd.getTitle());
                //wd.switchTo().window(windowId);
                break;
            }
            i += 1;
            Thread.sleep(1000);
        }
    }
    //Web、安卓、iOS
    public List<WebElement> getTextWebElement(String json,String client){
        List<WebElement> wes;
        String text = Json.getJsonValue(json,"Text");
        String attribute = Json.getJsonValue(json,"Attribute");
        Common.logPrinter("getTextWebElement========>Text: " + text);
        if(attribute!=null && attribute.length()>0){
            wes = ((WebDriver)clientMap.get(client)).findElements(By.xpath("//*[@"+attribute+"='"+text+"']"));
        } else {
            if(client.contains("Web")) wes = ((WebDriver)clientMap.get(client)).findElements(By.xpath("//*[text()='"+text+"']"));
            else wes = ((WebDriver)clientMap.get(client)).findElements(By.xpath("//*[@text='"+text+"']"));
        }
        Common.logPrinter("getTextWebElement========>WebElement Count: " + wes.size());
        return wes;
    }
    //Web、安卓、iOS
    public void open(String opt,String client) throws InterruptedException {
        String local = Json.getJsonValue(opt, "Local");
        if (local.indexOf("/")==0) local = webHost + local;
        else if (local.indexOf("http")==0) local = local;
        else local = (String)xpathMap.get(local);
        Common.logPrinter("open========>Local: " + local);
        if(client.contains("Web")){
            ((WebDriver)clientMap.get(client)).get(local);
        } else if(client.contains("Android")) {
            String pkg = local.substring(0, local.indexOf(":")).trim();
            String act = local.substring(local.indexOf(":") + 1).trim();
            ((AndroidDriver)clientMap.get(client)).startActivity(new Activity(pkg, act));
        } else if(client.contains("iOS")) {
            ((IOSDriver)clientMap.get(client)).terminateApp(local);
            ((IOSDriver)clientMap.get(client)).activateApp(local);
        }
    }
    //安卓
    public void swipe(String opt,String client) throws InterruptedException {
        int sWide = Integer.parseInt(Json.getJsonValue(opt, "SWide"));
        int sHigh = Integer.parseInt(Json.getJsonValue(opt, "SHigh"));
        int eWide = Integer.parseInt(Json.getJsonValue(opt, "EWide"));
        int eHigh = Integer.parseInt(Json.getJsonValue(opt, "EHigh"));
        int count = Integer.parseInt(Json.getJsonValue(opt, "Count"));
        for(int i=0;i<count;i++) {
            if(client.contains("Android"))
                (new TouchAction((AndroidDriver)clientMap.get(client))).longPress(PointOption.point(sWide, sHigh)).moveTo(PointOption.point(eWide, eHigh)).release().perform();
            else
                (new TouchAction((IOSDriver)clientMap.get(client))).longPress(PointOption.point(sWide, sHigh)).moveTo(PointOption.point(eWide, eHigh)).release().perform();
            Thread.sleep(1000);
        }
    }
    //安卓
    public void clickXY(String opt,String client) throws InterruptedException {
        String baseX = Json.getJsonValue(opt, "BaseX");
        String baseY = Json.getJsonValue(opt, "BaseY");
        int wide = Integer.parseInt(Json.getJsonValue(opt, "Wide"));
        int high = Integer.parseInt(Json.getJsonValue(opt, "High"));
        int count = Integer.parseInt(Json.getJsonValue(opt, "Count"));
        if(client.contains("Android")) {
            if ("Mid".equals(baseX)) wide += ((AndroidDriver) clientMap.get(client)).manage().window().getSize().width / 2;
            if ("Mid".equals(baseY)) high += ((AndroidDriver) clientMap.get(client)).manage().window().getSize().height / 2;
        } else {
            if ("Mid".equals(baseX)) wide += ((IOSDriver) clientMap.get(client)).manage().window().getSize().width / 2;
            if ("Mid".equals(baseY)) high += ((IOSDriver) clientMap.get(client)).manage().window().getSize().height / 2;
        }
        Common.logPrinter("clickXY========>Wide: " + wide + " ,high: " + high);

        for(int i=0;i<count;i++) {
            if(client.contains("Android"))
                (new TouchAction((AndroidDriver)clientMap.get(client))).tap(PointOption.point(wide, high)).release().perform();
            else
                (new TouchAction((IOSDriver)clientMap.get(client))).tap(PointOption.point(wide, high)).release().perform();
            Thread.sleep(1000);
        }
    }
    //安卓
    public void clickPre(String opt,String client) throws InterruptedException {
        int wide = Integer.parseInt(Json.getJsonValue(opt, "WidePre"));
        int high = Integer.parseInt(Json.getJsonValue(opt, "HighPre"));
        int count = Integer.parseInt(Json.getJsonValue(opt, "Count"));
        if(client.contains("Android")) {
            wide = ((AndroidDriver) clientMap.get(client)).manage().window().getSize().width * wide / 100;
            high = ((AndroidDriver) clientMap.get(client)).manage().window().getSize().height * high / 100;
        } else {
            wide = ((IOSDriver) clientMap.get(client)).manage().window().getSize().width * wide / 100;
            high = ((IOSDriver) clientMap.get(client)).manage().window().getSize().height * high / 100;
        }
        Common.logPrinter("clickPre========>Wide: " + wide + " ,high: " + high);
        for(int i=0;i<count;i++) {
            if(client.contains("Android"))
                (new TouchAction((AndroidDriver)clientMap.get(client))).tap(PointOption.point(wide, high)).release().perform();
            else
                (new TouchAction((IOSDriver)clientMap.get(client))).tap(PointOption.point(wide, high)).release().perform();
            Thread.sleep(1000);
        }
    }
    public void assertResult(String ass,String client) throws InterruptedException {
        String type = Json.getJsonValue(ass,"AssertType");
        String local = Json.getJsonValue(ass,"Local");
        String value = Json.getJsonValue(ass,"Value");
        String mold = Json.getJsonValue(ass,"Mold");
        if ("ADB".equals(type)) {
            String cnt = Json.getJsonValue(ass,"Count");
            Common.executeCMD("adb shell uiautomator dump");
            Thread.sleep(5000);
            String str = Common.executeCMD("adb shell cat /sdcard/window_dump.xml");
            Thread.sleep(5000);
            int count = str.split(value).length-1;
            if ("Equal".equals(mold)) Assert.assertTrue(count == Integer.parseInt(cnt));
            else if ("Exclude".equals(mold)) Assert.assertTrue(count < Integer.parseInt(cnt));
            else Assert.assertTrue(count >= Integer.parseInt(cnt));
        } else if ("EMCount".equals(type)) {
            String cnt = Json.getJsonValue(ass,"Count");
            int count = getElements(local,client).size();
            if ("Equal".equals(mold)) Assert.assertTrue(count == Integer.parseInt(cnt));
            else if ("Exclude".equals(mold)) Assert.assertTrue(count < Integer.parseInt(cnt));
            else Assert.assertTrue(count >= Integer.parseInt(cnt));
        } else if ("Toast".equals(type)) {
            toast(value,client);
        } else if ("Text".equals(type)) {
            String cnt = Json.getJsonValue(ass,"Count");
            int count = getTextWebElement(ass,client).size();
            if ("Equal".equals(mold)) Assert.assertTrue(count == Integer.parseInt(cnt));
            else if ("Exclude".equals(mold)) Assert.assertTrue(count > Integer.parseInt(cnt));
            else Assert.assertTrue(count >= Integer.parseInt(cnt));
        } else if ("File".equals(type)) {
            File file = new File(value);
            if(file.exists()) Common.logPrinter("assertResult========>File Exists: " + value);
            else Common.logPrinter("assertResult========>File Not Exists: " + value);

            if ("Exclude".equals(mold)) Assert.assertTrue(!file.exists());
            else Assert.assertTrue(file.exists());
            String delete = Json.getJsonValue(ass,"Delete");
            if ("Yes".equals(delete)) file.delete();
        } else if ("Alert".equals(type)) {
            String str = ((WebDriver) clientMap.get(client)).switchTo().alert().getText();
            Common.logPrinter("assertResult========>getText: " + str);

            if ("Equal".equals(mold)) Assert.assertEquals(str, value);
            else if ("Exclude".equals(mold)) Assert.assertTrue(!str.contains(value));
            else Assert.assertTrue(str.contains(value));
        } else {
            String str = this.result;
            if (local != null && local != "") str = getElement(ass, client).getText();
            Common.logPrinter("assertResult========>getText: " + str);

            if ("Equal".equals(mold)) Assert.assertEquals(str, value);
            else if ("Exclude".equals(mold)) Assert.assertTrue(!str.contains(value));
            else Assert.assertTrue(str.contains(value));
        }
    }
    public void toast(String toast,String client){
        try {
            final WebDriverWait wait = new WebDriverWait(((WebDriver) clientMap.get(client)),Duration.ofSeconds(2));
            if(client.contains("Web"))
                Assert.assertNotNull(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'"+ toast + "')]"))));
            else
                Assert.assertNotNull(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@text,'"+ toast + "')]"))));
            Common.logPrinter("assertResult========>toast=<" + toast + "> 捕获成功！");
        }catch (Exception e){
            Common.logPrinter("assertResult========>toast=<" + toast + "> 捕获失败！");
            Assert.assertTrue(false);
        }
    }
    public List<WebElement> getElements(String emId,String client) {
        List<WebElement> wes = null;

        String value = (String)xpathMap.get(emId);
        if(value == null || "".equals(value.trim())) value = emId;

        value = paramReplace(value);
        Common.logPrinter("getElement========>FindValue: " + value);
        String find = value.substring(0,value.indexOf(":")).trim();
        String local = value.substring(value.indexOf(":")+1).trim();

        Common.logPrinter("getElement========>FindType: " + find);
        Common.logPrinter("getElement========>FindLocal: " + local);

        if ("ID".equals(find)) wes = ((WebDriver) clientMap.get(client)).findElements(By.id(local));
        if ("XPath".equals(find)) wes = ((WebDriver) clientMap.get(client)).findElements(By.xpath(local));
        if ("Name".equals(find)) wes = ((WebDriver) clientMap.get(client)).findElements(By.name(local));
        if ("Class".equals(find)) wes = ((WebDriver) clientMap.get(client)).findElements(By.className(local));
        if ("Tag".equals(find)) wes = ((WebDriver) clientMap.get(client)).findElements(By.tagName(local));
        if ("CSS".equals(find)) wes = ((WebDriver) clientMap.get(client)).findElements(By.cssSelector(local));
        Common.logPrinter("getElement========>Find Element Size: " + wes.size());
        return wes;
    }
    public WebElement getElement(String json,String client) {
        String local = Json.getJsonValue(json,"Local");
        int index = 0;
        List<WebElement> wes = getElements(local,client);

        if (wes.size()==0) return null;
        if (wes.size()>1) {
            if("Last".equals(Json.getJsonValue(json,"Index")))
                index = wes.size() - 1;
            else
                index = Integer.parseInt(Json.getJsonValue(json,"Index"));
        }
        return wes.get(index);
    }

    public void parametric(String param,String client){
        String name = Json.getJsonValue(param,"Name");
        //String type = Json.getJsonValue(param,"ParamType");
        String local = Json.getJsonValue(param,"Local");
        String start = Json.getJsonValue(param,"Start");
        String end = Json.getJsonValue(param,"End");

        String str = this.result;
        if (local != null && local != "") {
            if ("Alert".equals(local))
                str = ((WebDriver) clientMap.get(client)).switchTo().alert().getText();
            else
                str = getElement(param, client).getText();
        }
        Common.logPrinter("parametric========>getText: " + str);

        int b=0, e=str.length();
        if (start != null && start.length() > 0)  b = str.indexOf(start) + start.length();
        if (end != null && end.length() > 0)  e = str.indexOf(end, b);
        paramMap.put(name,str.substring(b,e));
        Common.logPrinter("parametric========> " + name + "=" + paramMap.get(name));
    }
    public String paramReplace(String str) {
        String key = "";
        int b,e;
        b = str.indexOf("[$");
        e = str.indexOf("$]");
        while (b >= 0 && e >= 0 && e > b){
            key = str.substring(b+2,e).trim();
            if ("Stamp".equals(key)) {
                str = str.substring(0, b) + System.currentTimeMillis() + str.substring(e + 2);
            } else if("Today".equals(key)){
                str = str.substring(0, b) + LocalDate.now() + str.substring(e + 2);
            } else if("SRC".equals(key)){
                try {
                    File file = new File("");
                    str = str.substring(0, b) + file.getCanonicalPath().replaceAll("\\\\", "\\\\\\\\") + str.substring(e + 2);
                }catch (Exception ioe){
                    ioe.printStackTrace();
                }
            } else {
                str = str.substring(0, b) + paramMap.get(key) + str.substring(e + 2);
            }
            b = str.indexOf("[$");
            e = str.indexOf("$]");
        }
        return str;
    }

    public void screenShotAsFile(String fileName,String client) {
        if (client.indexOf("CMD")==0) return;
        File scrFile = ((TakesScreenshot) clientMap.get(client)).getScreenshotAs(OutputType.FILE);
        try {
            File destFile = new File("./screen/" + fileName + ".jpg");
            Files.copyFile(new FileInputStream(scrFile), destFile);

            System.out.println("screenShotAsFile========>ScreenshotImage: " + destFile.getAbsolutePath());
            if(jenkinsWS.length()>30)
                Reporter.log("<a href='" + jenkinsWS + "/screen/" + fileName + ".jpg" + "'> <img src='" + jenkinsWS + "/screen/" + fileName + ".jpg" + "' height='90' width='120'/> </a>");
            else
                Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath() + "' height='90' width='120'/> </a>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
