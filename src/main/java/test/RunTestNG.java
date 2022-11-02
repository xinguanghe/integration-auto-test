package test;

import org.testng.TestNG;
import org.testng.collections.Lists;

import java.util.List;

public class RunTestNG {
    public static void main(String[] args) {
        TestNG tng = new TestNG();
        List<String> suites = Lists.newArrayList();
        //添加要执行的testng.xml文件
        if(args!=null && args.length>0 && args[0]!=null && args[0].indexOf(".xml")>0) suites.add(args[0]); else suites.add("./testng.xml");
        tng.setTestSuites(suites);
        tng.run();
    }
}
