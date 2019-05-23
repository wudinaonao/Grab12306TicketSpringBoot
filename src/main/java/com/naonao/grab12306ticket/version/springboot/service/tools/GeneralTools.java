package com.naonao.grab12306ticket.version.springboot.service.tools;

import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.springboot.service.base.AbstractService;
import lombok.extern.log4j.Log4j;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

/**
 * @program: 12306grabticket_java
 * @description: GeneralTools
 * @author: Wen lyuzhao
 * @create: 2019-04-29 17:31
 **/
@Log4j
public class GeneralTools extends AbstractService {



    /**
     * get json result status, success or failed
     * @param resultStr result string
     * @return boolean
     */
    public static boolean getResultJsonStatus(String resultStr) {
        JSONObject jsonObject = JSONObject.parseObject(resultStr);
        return jsonObject.getBoolean("status");
    }

    /**
     * url encode
     * @param url url
     * @return string
     */
    public static String encodeURL(String url) {
        return URLEncoder.encode(url, StandardCharsets.UTF_8);
    }

    /**
     * url decode
     * @param url url
     * @return string
     */
    public static String decodeURL(String url) {
        return URLDecoder.decode(url, StandardCharsets.UTF_8);
    }


    /**
     * read, write, append file, text mode
     * @param filePath  file path
     * @return          String
     */
    public static String readFileText(String filePath) {
        // List<String> lineList = new ArrayList<String>();
        StringBuilder content = new StringBuilder("");
        try {
            String encoding = "UTF-8";
            URL filePathUrl = ClassLoader.getSystemResource(filePath);
            File file = new File(filePathUrl.getFile());
            // 判断文件是否存在
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    content.append(lineTxt + "\n");
                }
                bufferedReader.close();
                read.close();
            } else {
                log.error("not found file");
            }
        } catch (Exception e) {
            log.error("read file failed");
            e.printStackTrace();
        }
        return content.toString().substring(0, content.toString().length() - 1);
    }

    public static boolean appendFileText(String filePath, String text) {

        FileWriter fileWriter = null;

        try {
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            File file = new File(filePath);
            fileWriter = new FileWriter(file, true);
        } catch (IOException e) {
            return false;
        }
        PrintWriter PrintWriter = new PrintWriter(fileWriter);
        PrintWriter.println(text);
        PrintWriter.flush();

        try {
            PrintWriter.flush();
            PrintWriter.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean writeFileText(String filePath, String text) {
        try {
            File file = new File(filePath);
            file.delete();
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static Properties getConfig() {
        try {
            // InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream("config.properties"), StandardCharsets.UTF_8);
            InputStreamReader inputStreamReader = new InputStreamReader(ClassLoader.getSystemResourceAsStream("config.properties"), StandardCharsets.UTF_8);

            Properties properties = new Properties();
            properties.load(inputStreamReader);
            return properties;
        } catch (IOException e){
            log.error(e.getMessage());
        } catch (NullPointerException e) {
            log.error(e.getMessage());
        }
        return null;
    }


    public static String currentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    public static String currentDateAndTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    /**
     * Determine if it is in system maintenance time.
     * this is a general method, he will automatically
     * set the time zone and daylight saving time.
     * if system in maintenance time return true
     * @return  Boolean
     */
    public static Boolean systemMaintenanceTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // get Beijing date for later comparison.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(changeTimeZone(new Date(System.currentTimeMillis()), TimeZone.getDefault(), TimeZone.getTimeZone("GMT+8")));
        Date nowTime = null;
        Date beginTime = null;
        Date endTime = null;
        // get timestamp, if is summer time, then subtract 3600s.
        long timestamp = TimeZone.getDefault().inDaylightTime(new Date(System.currentTimeMillis()))? System.currentTimeMillis() - (3600 * 1000): System.currentTimeMillis();
        try {
            // get date object, time zone is GMT+8, is Beijing time.
            nowTime = changeTimeZone(new Date(timestamp), TimeZone.getDefault(), TimeZone.getTimeZone("GMT+8"));
            // this is 12306 system working time interval.
            beginTime = simpleDateFormat.parse(date + " 06:00:00");
            endTime = simpleDateFormat.parse(date + " 23:00:00");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        if (nowTime == null || beginTime == null || endTime == null){
            return false;
        }
        // what i got above is Beijing time.
        // create Calendar instance no time zone specified, if the specified time zone
        // will set the local time zone to the specified time zone.
        Calendar now = Calendar.getInstance();
        now.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        // if the current time is in this interval, then system is not maintenance,
        // otherwise system is maintenance.
        if (now.after(begin) && now.before(end)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * get date string after change time zone.
     * @param date      date
     * @param oldZone   old time zone
     * @param newZone   new time zone
     * @return          date object
     */
    private static Date changeTimeZone(Date date, TimeZone oldZone, TimeZone newZone) {
        Date dateTmp = null;
        if (date != null) {
            int timeOffset = oldZone.getRawOffset() - newZone.getRawOffset();
            dateTmp = new Date(date.getTime() - timeOffset);
        }
        return dateTmp;
    }

}
