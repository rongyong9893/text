package com.rong.demo.controller;

import com.rong.demo.pojo.User;
import com.rong.demo.utils.ExportExcelUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author ：rongyong
 * @ Date ：Created in 22:58 2019/3/15
 */
@Controller
public class controller
{
    @RequestMapping("get")
    public void get()
    {
        System.out.println("aaaaaaaaaaaaaaaa");
    }


    @RequestMapping("exportLog")
    public void exportLog(HttpServletResponse response){
//        //获取日志
//        List<DtmSystemLog> list = logService.getLogs();
//        //拼接字符串
//        StringBuffer text = new StringBuffer();
//        for(DtmSystemLog log:list){
//            text.append(log.getOpeuser());
//            text.append("|");
//            text.append(log.getOpedesc());
//            text.append("|");
//            text.append(dateString);
//            text.append("\r\n");//换行字符
//        }
//        exportTxt(response,"adasdasdasda");

        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, String> title = new HashMap<>();    // 表头
        // 设置表头信息
        title.put("name", "姓名");
        title.put("age", "年龄");
        title.put("gender", "性别");
        title.put("email", "邮箱");
        title.put("address", "地址");

        Map<String, Integer> position = new HashMap<>();    // 表头
        // 设置表头字段位置
        position.put("name", 0);
        position.put("age", 1);
        position.put("gender", 2);
        position.put("email", 3);
        position.put("address", 4);


        List<User> uList = new ArrayList<>();
        User u1 = new User();
        u1.setName("张三");
        u1.setAge(25);
        u1.setGender("男");
        u1.setEmail("shangsan@qq.com");
        u1.setAddress("上海市浦东区");

        User u2 = new User();
        u2.setName("李四");
        u2.setAge(28);
        u2.setGender("男");
        u2.setEmail("lisi@qq.com");
        u2.setAddress("深圳市罗湖区");

        User u3 = new User();
        u3.setName("贝尔·格里尔斯");
        u3.setAge(45);
        u3.setGender("男");
        u3.setEmail("beier@qq.com");
        uList.add(u1);
        uList.add(u2);
        uList.add(u3);

        Map<String, Object> userMap = null;
        // 遍历模拟的数据填充到userMap集合
        for (User user : uList)
        {
            userMap = new HashMap<>();
            userMap.put("name", user.getName());
            userMap.put("age", user.getAge());
            userMap.put("gender", user.getGender());
            userMap.put("email", user.getEmail());
            userMap.put("address", user.getAddress());
            data.add(userMap);     // 将userMap添加到List集合中
        }

            String excelName = "user列表.xlsx";
            String sheetName = "用户列表数据";
            try
            {
                excelName = URLEncoder.encode(excelName, "UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename="+excelName);
            response.setContentType("application/x-download");
            ExportExcelUtils.exportDataToExcel(title, position, data, sheetName, response.getOutputStream());

            } catch (Exception e) {
                e.printStackTrace();
            }

    }


    public void exportTxt(HttpServletResponse response,String text){
        response.setCharacterEncoding("utf-8");
        //设置响应的内容类型
        response.setContentType("json/plain");
        //设置文件的名称和格式
        response.addHeader("Content-Disposition","attachment;filename="
                + genAttachmentFileName( "文件名称", "JSON_FOR_UCC_")//设置名称格式，没有这个中文名称无法显示
                + ".json");
        BufferedOutputStream buff = null;
        ServletOutputStream outStr = null;
        try {
            outStr = response.getOutputStream();
            buff = new BufferedOutputStream(outStr);
            buff.write(text.getBytes("UTF-8"));
            buff.flush();
            buff.close();
        } catch (Exception e) {
            //LOGGER.error("导出文件文件出错:{}",e);
        } finally {try {
            buff.close();
            outStr.close();
        } catch (Exception e) {
            //LOGGER.error("关闭流对象出错 e:{}",e);
        }
        }
    }

    public  String genAttachmentFileName(String cnName, String defaultName) {
        try
        {
            cnName = new String(cnName.getBytes("gb2312"), "ISO8859-1");
        } catch (Exception e)
        {
            cnName = defaultName;
        }
        return cnName;
    }

//    public static void main(String[] args)
//    {
//        String data = "#在抖音，记录美好生活#这大概就是冰雪美人吧…… czxhttp://v.douyin.com/eUWYth/ czxczx复制此链接，打开【抖音短视频】，直接观看视频！";
//
//        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
//                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";
//
//        final List<String> list = new ArrayList<String>();
//        final Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
//        final Matcher ma = pa.matcher(data);
//        while (ma.find())
//        {
//            list.add(ma.group());
//        }
//        System.out.println(list.toString());
//
//        List<List<String>> bigs = new ArrayList<>();
//        for (int i = 0 ; i<=50000000; i++)
//        {
//            List<String> list = new ArrayList<>();
//            System.out.println("----------------i="+i);
//            bigs.add(list);
//        }
//    }
}
