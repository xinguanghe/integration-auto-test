#!/usr/bin/python3
import sys
import requests

JOB_URL = sys.argv[1]
url = 'https://open.feishu.cn/open-apis/bot/v2/hook/9197a73f-d2dc-4984-b2e7-23590c1cc44e'
method = 'post'
headers = {'Content-Type': 'application/json'}

#src = "./target/surefire-reports/TestSuite.txt"
src = "./target/surefire-reports/emailable-report.html"
with open(src, "r", encoding="utf_8") as f:  # 打开文件
    report = f.read()  # 读取文件
start = report.index("<table>")
end = report.index("</table>")+8
report = report[start:end]
print(report)

text = "All Tests:\n"
while report.find("<a href=") > 1:
	report = report[report.index("<a href="):len(report)]
	text = text + report[report.index("\">")+2:report.index("</a>")] + " - Passed:"
	report = report[report.index("</td>")+2:len(report)]
	text = text + report[report.index("\">")+2:report.index("</td>")] + ", Skipped:"
	report = report[report.index("</td>")+2:len(report)]
	text = text + report[report.index("\">")+2:report.index("</td>")] + ", Retried:"
	report = report[report.index("</td>")+2:len(report)]
	text = text + report[report.index("\">")+2:report.index("</td>")] + ", Failed:"
	report = report[report.index("</td>")+2:len(report)]
	text = text + report[report.index("\">")+2:report.index("</td>")] + ", Time (ms):"
	report = report[report.index("</td>")+2:len(report)]
	text = text + report[report.index("\">")+2:report.index("</td>")] + " \n"

if report.find("Total") > 1:
	report = report[report.index("Total"):len(report)]
	report = report[report.index("</th>")+2:len(report)]
	text = text + "\nTotal - Passed:" + report[report.index("\">")+2:report.index("</th>")] + ", Skipped:"
	report = report[report.index("</th>")+2:len(report)]
	text = text + report[report.index("\">")+2:report.index("</th>")] + ", Retried:"
	report = report[report.index("</th>")+2:len(report)]
	text = text + report[report.index("\">")+2:report.index("</th>")] + ", Failed:"
	report = report[report.index("</th>")+2:len(report)]
	text = text + report[report.index("\">")+2:report.index("</th>")] + ", Time (ms):"
	report = report[report.index("</th>")+2:len(report)]
	text = text + report[report.index("\">")+2:report.index("</th>")] + " \n"

print(text)

json = {
    "msg_type": "interactive",
    "card": {
        "config": {
            "wide_screen_mode": True,
            "enable_forward": True
        },
        "elements": [{
            "tag": "div",
            "text": {
                "content": text,
                "tag": "lark_md"
            }
        }, {
            "actions": [{
                "tag": "button",
                "text": {
                    "content": "查看测试报告",
                    "tag": "lark_md"
                },
                "url": JOB_URL + "testngreports",
                "type": "default",
                "value": {}
            }],
            "tag": "action"
        }],
        "header": {
            "title": {
                "content": "归心(App&运营工具)-pre环境-API接口自动化测试报告",
                "tag": "plain_text"
            },
            "template": "blue"
        }
    }
}
requests.request(method=method, url=url, headers=headers, json=json)



