# integration-auto-test
自动化集成测试工具：无代码 自动化测试 框架，支持 API、安卓、iOS、Web 等 多客户端 同时操作的自动化测试工具。

运行条件：
1. 需要安装配置 Java JDK 1.8 版本。
    * 下载安装：https://www.oracle.com/java/technologies/downloads/#java8
    * “我的电脑”右键菜单--->属性--->高级--->环境变量--->系统变量-->新建..
        * 变量名：JAVA_HOME 
        * 变量值：D:\Program Files (x86)\Java\jdk1.8.0_25 
        * 变量名：CALSS_PATH
        * 变量值：.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar; 
    * 找到path变量名—>“编辑”添加：
        * 变量名：PATH
        * 变量值：%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;
    * 参考：https://www.cnblogs.com/fnng/p/4552438.html
2. ﻿Web测试需要下载安装浏览器和对应驱动（支持 Edge、Chrome、Firefox）。
3. Android手机测试需要安装配置 Android SDK 。
    * 下载安装：http://tools.android-studio.org/index.php/sdk
    * “我的电脑”右键菜单--->属性--->高级--->环境变量--->系统变量-->新建..
        * 变量名：ANDROID_HOME 
        * 变量值：D:\android\android-sdk-windows
    * 找到path变量名—>“编辑”添加：
        * 变量名：PATH
        * 变量值：;%ANDROID_HOME%\platform-tools;%ANDROID_HOME%\tools;
    * 参考：https://www.cnblogs.com/fnng/p/4552438.html
4. iOS手机测试还需要安装 XCode。参考：https://blog.csdn.net/weixin_33733742/article/details/114076020
5. 需要安卓Appium Server，可直接安装：Appium-windows-1.15.1.exe（包含 Appium server、app inspector）
6. Linux 需要安装 Node JS。
    * 下载安装：https://nodejs.org/en/download/
    * npm切换淘宝原
        * 查看原：npm config get registry
        * 切换淘宝原：npm config set registry https://registry.npm.taobao.org/
        * 更新npm版本：npm install -g npm@8.10.0
7. Linux 需要安装 Appium server。
    * 安装Appium server：npm install -g appium
    * 安装检查appium环境：npm install -g appium-doctor
    * 参考：https://www.cnblogs.com/nancy-kou/p/12973855.html
8. 需要安装安卓模拟器（雷电模拟器比较好用）。 
    * 下载安装MuMu：https://mumu.163.com/
9. 配置 "testng.xml" 文件 和 编写 "Integration-Auto-TestCase.xls" 测试用例。
10. 建议 "testng.xml"、"integration-auto-test.jar"、"Integration-Auto-TestCase.xls" 放在同一个目录里。


运行命令范例：
* D:\Work\WebTest> java -jar .\integration-auto-test-1.0-SNAPSHOT-jar-with-dependencies.jar > log.txt
* D:\Work\WebTest> java -jar .\integration-auto-test-1.0-SNAPSHOT-jar-with-dependencies.jar .\testng.xml > log.txt


运行结果：
1. 可参考自动生成的 "test-output" 目录里的报告。
2. 可参考运行时输出的Log "log.txt" 文件。
3. 可参考"screen"目录里的操作结果截图。


Excel测试用例编写规则和方法：(支持本地Excel和金山云Excel)
* Auto、TestCaseID、Client、URL、Operates、Asserts、Parametric 等红色项目必须设定。									
* 隐式等待 设置为5秒。（无法定位到页面元素时等待页面加载元素的时间）									
* 每个测试用例执行操作（Operates）完后自动截图保存，调试类型的测试用例每一步操作后都截图保存。									
* 测试用例中不填写StartURL时在上一个用例执行完的页面上继续执行测试操作。									
* 测试用例里输入内容中 "[$" 和 "$]" 设为参数替代标识符，只有提前参数化的变量才可以使用。									
* 系统提供一个特定的参数 "[$Stamp$]" 代表系统当前的时间戳。									
* 系统提供一个特定的参数 "[$SRC$]" 代表本工程在当前系统中的路径。									
* 系统提供一个特定的参数 "[$Today$]" 代表系统当前的日期。									
* 增加了 “EMDC” 元素字典Sheet，字典里注册的元素在测试用例中可以用ID替代Value。除了页面元素之外，“URL | Activity”也可以注册到元素字典。									
* 元素定位方式："ID", "XPath", "Name", "Tag", "Class" 等5种。“Local”属性必须是EMDC里的“ID”值。定位到多个元素时"Index"属性值从"0"开始，"Index"值为"Last"时代表最后一个元素。									

* TestCaseID：	代表测试用例的代号，不能重复。	

* Client：	填写执行测试用例的客户端（例如：API、Web、iOS、Android、DB），这些客户端是 WebClients.properties、iOSClients.properties、AndroidClients.properties 等配置文件里配置。								

* URL | Activity：	测试用例起始的页面，就是测试用例开始执行后在客户端上打开的第一个页面。必须在EMDC里注册，并且要填写EMDC的ID。
    * API	    POST:/api/auth/app/member/login (必填，可以是http开头的全路径）								
    * Web	    /api/auth/app/member/login (非必填，可以是http开头的全路径）								
    * Android	com.up72.sunacliving:com.up72.sunacliving.activity.MainActivity (非必填）								

* Auto：自动化实现标注列，测试用例区分重要度执行，可多种测试用例选择执行，建议使用如下分类。
    * 0 - 初始化或调试中的测试用例
    * 1 - 正向功能验证类测试用例
    * 2 - 业务流程验证类测试用例
    * 3 - 反向功能验证类测试用例
    * 4 - 数据验证类测试用例
    * 5 - 不重要的其他测试用例
    * \# - 调试中的测试用例								

* 所有操作步骤、参数化操作、断言 必须以 { 开头，以 }; 结束，不然跳过该操作或认为书写错误。
* 操作 Operates： "Click"-单击（元素）, "ClickXY"-单击（像素）, "Set"-赋值, "Clear"-清除, "Submit"-表单提交, "Alert"-弹出框, "Move"-鼠标移动, "Sleep"-等待, "Assert"-断言, "Parametric"-参数化,  "Shot"-屏幕截图, "Swipe"-"划动"。每个操作都可以加“Sleep”属性，代表该操作完成后硬性等待设置的时间（秒）。
    * （Web、Android、iOS）	赋值：{"Type":"Set","Local":"EMDC ID","Value":"xinguanghe","Index":"0"};								
    * （Web、Android、iOS）	清除：{"Type":"Clear","Local":"EMDC ID","Index":"0"}; //清除元素里的输入内容								
    * （Web、Android、iOS）	单击（元素）：{"Type":"Click","Local":"EMDC ID","Index":"1"};								
    * （Web、Android、iOS）	单击（存在）：{"Type":"ClickIF","Local":"EMDC ID","Index":"1"};								
    * （Android、iOS）	单击（像素）：{"Type":"ClickXY","BaseX":"Top","BaseY":"Mid","Wide":"100","High":"100","Count":"1"};								
    * （Android、iOS）	单击（比例）：{"Type":"ClickPre","WidePre":"100","HighPre":"100","Count":"1"};								
    * （Web、Android、iOS）	单击（文本）：{"Type":"ClickText","Text":"新建","Index":"1"};//Top：屏幕左上角基准，Mid：屏幕中间点基准，"Count"是单击次数								
    * （Web、Android、iOS）	打开页面：{"Type":"Open","Local":"EMDC ID"};//"Local"是"EMDC"里注册的URL或Activity								
    * （Web）	提交：{"Type":"Submit","Local":"EMDC ID","Index":"Last"};								
    * （Web）	弹出框：{"Type":"Alert","Value":"Accept"}; {"Type":"Alert","Value":"Dismiss"}; //弹出框 确定和取消 操作								
    * （Web）	鼠标移动：{"Type":"Move","Local":"EMDC ID","Index":"2"};								
    * （Web、Android、iOS）	等待：{"Type":"Sleep","Value":"3000"}; //页面等待3秒								
    * （Web、Android、iOS）	断言：{"Type":"Assert","AssertType":"ID","Local":"EMDC ID","Value":"xintest","Mold":"Equal"};								
    * （Web、Android、iOS）	参数化：{"Type":"Parametric","Name":"param1","ParamType":"ID","Local":"EMDC ID","Start":"xintest","End":"Equal"};								
    * （Web、Android、iOS）	屏幕截图：{"Type":"Shot","Name":"test"}; //屏幕上有弹出框时无法截图								
    * （Web）	执行js：{"Type":"JS","JSCode":"document.getElementById(\"kw\").value=\"yeetrack\""};								
    * （Web）	鼠标滚动：{"Type":"Scroll","Local":"EMDC ID","Index":"2","Value":"1000"};
    * （Web）	下拉选择框：{"Type":"Select","Local":"EMDC ID","Index":"2","Value":"游泳"};	//"Value"是下拉选择框选项的text值							
    * （Android、iOS）	"划动：{"Type":"Swipe","SWide":"100","SHigh":"100","EWide":"100","EHigh":"100","Count":"1"};//SWide、SHigh：划动开始点，EWide、EHigh：划动结束点，"Count"是划动次数。
    * （Web）	拖动：{"Type":"Drag","Local":"EMDC ID","Wide":"1","High":"1","Index":"1"};								
    * （Web）	选择窗口：{"Type":"Window","Value":"新收费","Index":"1"};//Value：窗口标题所包含的关键词，Index：窗口序号（从0开始）					
    * （Web）	选择Frame：{"Type":"Frame","Value":"新收费"};//Value：Frame的 id 或 name 属性
    * （API）	"支持5种接口参数传递方式："Json"-Json字符串, "K&V"-Key Value 对, "File"-文件, "Form"-表单, "Down"-文件下载, "URL"-URL传参 等6种。"File" 和 "Form" 类型时 不需要再Header里设定 "Content-Type":"multipart/form-data" 。所有类型里只要设置 "Save" 属性，就会把接口返回内容以 "Save" 值里的路径保存为一个文件。"								
    * （API）	Json：{"Headers":{"token":"1234567890","Content-Type":"application/json"},"Inputs":{"Type":"Json","Data":{"Name":"el-input__user","Value":"xinguanghe","Index":"0"}}}								
    * （API）	K&V：{"Headers":{"token":"1234567890","Content-Type":"application/json"},"Inputs":{"Type":"K&V","Data":{"ID":"12345678","Index":"1"}}}								
    * （API）	File：{"Headers":{"token":"1234567890","Content-Type":"application/json"},"Inputs":{"Type":"File","Data":{"banner1":"<$SRC$>/banner1.jpg","banner2":"<$SRC$>/banner2.jpg"}}}								
    * （API）	Form：{"Headers":{"token":"1234567890","Content-Type":"application/json"},"Inputs":{"Type":"Form","Data":{"ID":"12345678","Index":"1"},"File":{"banner1":"<$SRC$>/banner1.jpg;<$SRC$>/banner2.jpg;","banner2":"<$SRC$>/banner2.jpg"}}}								
    * （API）	Down：{"Headers":{"token":"1234567890","Content-Type":"application/json"},"Inputs":{"Type":"Down","Save":"<$SRC$>\\files\\function.gif"}}								
    * （API)	URL：{"Headers":{"token":"1234567890","Content-Type":"application/json"},"Inputs":{"Type":"URL","Data":"?id=user1&name=name1"}}								
    * （DB)  	Select: select * from abcd where name = 'test';								
    * （DB)  	Insert: insert into abcd (name,ID) values ('test','1234');								
    * （DB)  	Update: update abcd set name = '' where name = 'test';								
    * （DB)  	Delete: delete from abcd where name = 'test';								
    * （Web、Android)  	CMD: {"Type":"CMD","CMD":"adb shell am start -S -W com.ss.android.lark","Sleep":"5"};

* 断言 Asserts：断言类型（"AssertType"）：不填写时默认为"String"类。支持3种断言模式："Equal"-相等, "Include"-包含, "Exclude"-不包含，默认为包含模式。
    * （Web、Android、iOS）	{"AssertType":"String","Local":"EMDC ID","Value":"登录成功","Mold":"Include","Index":"0"};								
    * （Web）               {"AssertType":"Alert","Value":"登录成功","Mold":"Include"};						
    * （Web）               {"AssertType":"File","Value":"登录成功","Mold":"Include","Delete":"Yes"};//Value:文件路径，Mold: Include-存在、Exclude-不存在，Delete：Yes-删除、No-不删除	
    * （Web、Android、iOS）	{"AssertType":"EMCount","Local":"EMDC ID","Count":"5","Mold":"Equal"}; //Equal：count=5；Include：count>=5；Exclude：count<5；								
    * （Web、Android、iOS）	{"AssertType":"Toast","Value":"创建成功！"};								
    * （Web、Android、iOS）	{"AssertType":"Text","Value":"新建","Count":"5","Mold":"Equal"}; //Equal：count=5；Include：count>=5；Exclude：count<5；								
    * （API、DB）	        {"Mold":"Exclude","Start":"","End":"Exclude","Value":"ID"};								
    * （API、DB）	        {"Mold":"Sleep","Value":"3000"};								
    * （Web、Android）	    {"AssertType":"ADB","Value":"微信","Count":"5","Mold":"Equal"}; //Equal：count=5；Include：count>=5；Exclude：count<5；								

* 参数化 Parametric："Start"为参数化取值开始位置，值为"时从头开始取值，值为"ID"时第一次出现"ID"之后的位置开始取值。"End"为参数化取值结束位置，值为"时取值到最后，值为"ID"时取值开始位置起第一次出现"ID"之前的位置取值结束。
    * （Web、Android、iOS）	{"Name":"param1","Local":"EMDCID","Start":"ID","End":"Equal","Index":"0"};								
    * （Web、Android、iOS）	{"Name":"param2","Local":"EMDCID","Start":"","End":"","Index":"1"};								
    * （API、DB）	{"Name":"param1","Start":"ID","End":"Equal"};								


待完善事项：
1. 丰富操作类型和断言方式
2. 测试报告优化（目前是TestNG自带的报告，优化为Allure或Publish HTML或其他）								
				
