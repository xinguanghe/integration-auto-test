package test;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataRead {
    public static Object[][] getDataProvider(String excelPath,String sheet,String runType){
        List<String[]> testDatas = getExcel(excelPath,sheet,runType);
        int count = testDatas.size();
        int len = 6;//testDatas.get(0).length;
        Object[][] objs = new Object[count][len];
        for(int i=0;i<count;i++){
            String[] strs = testDatas.get(i);
            for(int j=0;j<len;j++){
                if (strs[j]==null) objs[i][j] = ""; else objs[i][j] = strs[j].trim();
            }
        }
        return objs;
    }
    public static HashMap getXPath(String excelPath){
        System.setProperty("file.encoding", "UTF-8");
        HashMap xpath = new HashMap();
        try {
            // 创建对Excel工作簿文件的引用
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelPath));
            // 创建对工作表的引用。
            // 本例是按名引用（让我们假定那张表有着缺省名"Sheet1"）
            HSSFSheet sheet = workbook.getSheet("EMDC");
            for(int i=2;i<=sheet.getLastRowNum();i++){
                String id = "";
                if(sheet.getRow(i).getCell(3) != null) id = sheet.getRow(i).getCell(3).getStringCellValue().trim();
                String value = "";
                if(sheet.getRow(i).getCell(4) != null) value = sheet.getRow(i).getCell(4).getStringCellValue().trim();
                System.out.println("getXPath========> : " + id + "=" + value);
                xpath.put(id,value);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return xpath;
    }
    public static List<String[]> getExcel(String excelPath,String sheetName,String runType){
        List<String[]> datas = new ArrayList<String[]>();
        System.setProperty("file.encoding", "UTF-8");
        int auto, tcId, url, client, operates, asserts, parametric, module, title;
        try {
            // 创建对Excel工作簿文件的引用
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelPath));
            // 创建对工作表的引用。
            // 本例是按名引用（让我们假定那张表有着缺省名"Sheet1"）
            HSSFSheet sheet = workbook.getSheet("Main");
            // 也可用getSheetAt(int index)按索引引用，
            // 在Excel文档中，第一张工作表的缺省索引是0，
            // 其语句为：HSSFSheet sheet = workbook.getSheetAt(0);
            auto = (int)sheet.getRow(1).getCell(1).getNumericCellValue();
            tcId = (int)sheet.getRow(1).getCell(2).getNumericCellValue();
            client = (int)sheet.getRow(1).getCell(3).getNumericCellValue();
            url = (int)sheet.getRow(1).getCell(4).getNumericCellValue();
            operates = (int)sheet.getRow(1).getCell(5).getNumericCellValue();
            asserts = (int)sheet.getRow(1).getCell(6).getNumericCellValue();
            parametric = (int)sheet.getRow(1).getCell(7).getNumericCellValue();
            module = (int)sheet.getRow(1).getCell(8).getNumericCellValue();
            title = (int)sheet.getRow(1).getCell(9).getNumericCellValue();

            System.out.println("getExcel========>sheetName: " + sheetName);
            sheet = workbook.getSheet(sheetName);
            for(int i=1;i<=sheet.getLastRowNum();i++){
                String autov = null;
                if(sheet.getRow(i).getCell(auto)!=null) autov = sheet.getRow(i).getCell(auto).getStringCellValue().trim();
                if(autov!=null && autov.length()>0 && runType.contains(autov.trim())) {
                    String[] strs = new String[6];
                    if(sheet.getRow(i).getCell(tcId)!=null) strs[0] = "|" + autov + "|" + sheet.getRow(i).getCell(tcId).getStringCellValue();
                    if(sheet.getRow(i).getCell(url)!=null) strs[1] = sheet.getRow(i).getCell(url).getStringCellValue();
                    if(sheet.getRow(i).getCell(operates)!=null) strs[2] = sheet.getRow(i).getCell(operates).getStringCellValue();
                    if(sheet.getRow(i).getCell(asserts)!=null) strs[3] = sheet.getRow(i).getCell(asserts).getStringCellValue();
                    if(sheet.getRow(i).getCell(parametric)!=null) strs[4] = sheet.getRow(i).getCell(parametric).getStringCellValue();
                    if(sheet.getRow(i).getCell(client)!=null) strs[5] = sheet.getRow(i).getCell(client).getStringCellValue();
                    if(module > 0 && sheet.getRow(i).getCell(module)!=null) strs[0] = strs[0] + " | " + sheet.getRow(i).getCell(module).getStringCellValue();
                    if(title > 0 && sheet.getRow(i).getCell(title)!=null) strs[0] = strs[0] + " | " + sheet.getRow(i).getCell(title).getStringCellValue();
                    datas.add(strs);
                    System.out.println("getExcel========>TestCaseID: "+strs[0]);
                    //System.out.println("getExcel========>URL: "+strs[1]);
                    //System.out.println("getExcel========>Operates: \n"+strs[2]);
                    //System.out.println("getExcel========>Asserts: \n"+strs[3]);
                    //System.out.println("getExcel========>Parametric: \n"+strs[4]);
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return datas;
    }
}