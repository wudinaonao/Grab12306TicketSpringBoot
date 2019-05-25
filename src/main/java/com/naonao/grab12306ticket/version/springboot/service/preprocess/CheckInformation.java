package com.naonao.grab12306ticket.version.springboot.service.preprocess;

import com.alibaba.fastjson.JSONArray;
import com.naonao.grab12306ticket.version.springboot.entity.database.GrabTicketInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.database.NotificationInformationEntity;
import com.naonao.grab12306ticket.version.springboot.resultclass.service.preprocess.CheckInformationReturnResult;
import com.naonao.grab12306ticket.version.springboot.constants.ConvertMap;
import com.naonao.grab12306ticket.version.springboot.service.base.AbstractPreprocess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-15 13:08
 **/
@Slf4j
@Component
public class CheckInformation extends AbstractPreprocess {

    private List<String> cityNameList;
    private List<String> seatTypeNameList;

    public CheckInformation(){
        cityNameList = ConvertMap.cityNameList();
        seatTypeNameList = ConvertMap.seatTypeNameList();
    }

    /**
     * check if the data valid.
     * @param grabTicketInformationEntity   grabTicketInformationEntity
     * @param notificationInformationEntity       notificationInformationEntity
     * @return                              CheckInformationReturnResult
     */
    public CheckInformationReturnResult check(GrabTicketInformationEntity grabTicketInformationEntity, NotificationInformationEntity notificationInformationEntity){
        CheckInformationReturnResult checkInformationReturnResult;
        // check grabTicketInformationEntity
        checkInformationReturnResult = checkGrabTicketInformationEntity(grabTicketInformationEntity);
        if (!checkInformationReturnResult.getStatus()){
            return checkInformationReturnResult;
        }
        // check notificationInformationEntity
        checkInformationReturnResult = checkNotificationInformationEntity(notificationInformationEntity);
        if (!checkInformationReturnResult.getStatus()){
            return checkInformationReturnResult;
        }
        return checkInformationReturnResult;
    }

    private CheckInformationReturnResult checkGrabTicketInformationEntity(GrabTicketInformationEntity grabTicketInformationEntity){
         CheckInformationReturnResult checkInformationReturnResult;

         checkInformationReturnResult = checkTime(grabTicketInformationEntity.getAfterTime());
         if (!checkInformationReturnResult.getStatus()){
             return checkInformationReturnResult;
         }
         checkInformationReturnResult = checkTime(grabTicketInformationEntity.getBeforeTime());
         if (!checkInformationReturnResult.getStatus()){
             return checkInformationReturnResult;
         }
        checkInformationReturnResult = checkDate(grabTicketInformationEntity.getTrainDate());
        if (!checkInformationReturnResult.getStatus()){
            return checkInformationReturnResult;
        }
        checkInformationReturnResult = checkStation(grabTicketInformationEntity.getFromStation());
        if (!checkInformationReturnResult.getStatus()){
            return checkInformationReturnResult;
        }
        checkInformationReturnResult = checkStation(grabTicketInformationEntity.getToStation());
        if (!checkInformationReturnResult.getStatus()){
            return checkInformationReturnResult;
        }
        checkInformationReturnResult = checkMobile(grabTicketInformationEntity.getMobile());
        if (!checkInformationReturnResult.getStatus()){
            return checkInformationReturnResult;
        }
        checkInformationReturnResult = checkSeatType(grabTicketInformationEntity.getSeatType());
        if (!checkInformationReturnResult.getStatus()){
            return checkInformationReturnResult;
        }
        return checkInformationReturnResult;
    }

    private CheckInformationReturnResult checkNotificationInformationEntity(NotificationInformationEntity notificationInformationEntity){
        CheckInformationReturnResult checkInformationReturnResult;

        checkInformationReturnResult = checkEmail(notificationInformationEntity.getReceiverEmail());
        if (!checkInformationReturnResult.getStatus()){
            return checkInformationReturnResult;
        }
        checkInformationReturnResult = checkEmail(notificationInformationEntity.getSendEmail());
        if (!checkInformationReturnResult.getStatus()){
            return checkInformationReturnResult;
        }
        checkInformationReturnResult = checkEmailPort(notificationInformationEntity.getSendEmailPort());
        if (!checkInformationReturnResult.getStatus()){
            return checkInformationReturnResult;
        }
        checkInformationReturnResult = checkNotificationMode(notificationInformationEntity.getNotificationMode());
        if (!checkInformationReturnResult.getStatus()){
            return checkInformationReturnResult;
        }
        return checkInformationReturnResult;
    }

    private CheckInformationReturnResult checkTime(String text){
        if (text == null){
            return checkInformationReturnResult(false, TIME_IS_NULL);
        }
        try{
            String[] textArray = text.split(":");
            for (String element: textArray) {
                if (element.trim().length() > 2){
                    return checkInformationReturnResult(false, TIME_FORMAT_IS_ERROR);
                }
            }
            Integer h = Integer.valueOf(textArray[0].trim());
            Integer min = Integer.valueOf(textArray[1].trim());
            if (!(0 <= h && h < HOUR_MAX_VALUE)){
                return checkInformationReturnResult(false, TIME_FORMAT_IS_ERROR);
            }
            if (!(0 <= min && min < MIN_MAX_VALUE)){
                return checkInformationReturnResult(false, TIME_FORMAT_IS_ERROR);
            }
        } catch (Exception e){
            return checkInformationReturnResult(false, TIME_PARSE_FAILED);
        }
        return checkInformationReturnResult(true, SUCCESS);
    }

    private CheckInformationReturnResult checkDate(String text){
        if (text == null){
            return checkInformationReturnResult(false, DATE_IS_NULL);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentData = new Date();
        try{
            Date inputDate = dateFormat.parse(text);
            if (inputDate.getTime() < currentData.getTime()){
                return checkInformationReturnResult(false, SET_DATE_IT_IS_LESS_THAN_THE_CURRENT_DATE);
            }
            return checkInformationReturnResult(true, SUCCESS);
        }catch (ParseException e) {
            return checkInformationReturnResult(false, DATE_PARSE_FAILED);
        }
    }

    private CheckInformationReturnResult checkStation(String text){
        if (text == null){
            return checkInformationReturnResult(false, STATION_NAME_IS_NULL);

        }
        return checkInformationReturnResult(cityNameList.contains(text), cityNameList.contains(text)? SUCCESS:NOT_FOUND_STATION_NAME);
    }

    private CheckInformationReturnResult checkMobile(String text){
        if (text == null){
            return checkInformationReturnResult(false, MOBILE_IS_NULL);
        }
        for (int i = text.length(); --i >=0;) {
            if (!Character.isDigit(text.charAt(i))){
                return checkInformationReturnResult(false, MOBILE_HAS_INVALID_SYMBOL);
            }
        }
        return checkInformationReturnResult(true, SUCCESS);
    }

    private CheckInformationReturnResult checkSeatType(String text){
        if (text == null){
            return checkInformationReturnResult(false, SEAT_TYPE_IS_NULL);
        }
        boolean status = true;
        for (String seatTypeName: text.split(",")){
            seatTypeName = seatTypeName.trim();
            status &= seatTypeNameList.contains(seatTypeName);
        }
        return checkInformationReturnResult(status, status? SUCCESS:SEAT_TYPE_INVALID);
    }

    private CheckInformationReturnResult checkSeatType(JSONArray jsonArray){
        return checkSeatType(StringUtils.join(jsonArray, ","));
    }

    private CheckInformationReturnResult checkEmail(String text){
        for (String string: text.split("")){
            if ("@".equals(string)){
                return checkInformationReturnResult(true, SUCCESS);
            }
        }
        return checkInformationReturnResult(false, EMAIL_ADDRESS_INVALID);
    }

    private CheckInformationReturnResult checkEmailPort(String text){
        try{
            Integer port = Integer.valueOf(text);
            if (port > PORT_MAX_VALUE){
                return checkInformationReturnResult(false, EMAIL_PORT_INVALID);
            }
        }catch (Exception e){
            return checkInformationReturnResult(false, EMAIL_PORT_INVALID);
        }
        return checkInformationReturnResult(true, SUCCESS);
    }

    private CheckInformationReturnResult checkNotificationMode(String text){
        String[] nameArray = text.split(",");
        for(String name: nameArray){
            name = name.trim().toUpperCase();
            if (!Arrays.asList(NOTIFICATION_MODE_NAME_ARRAY).contains(name)){
                return checkInformationReturnResult(false, NOTIFICATION_MODE_INVAILD);
            }
        }
        return checkInformationReturnResult(true, SUCCESS);
    }

    private CheckInformationReturnResult checkInformationReturnResult(Boolean status, String message){
        CheckInformationReturnResult checkInformationReturnResult = new CheckInformationReturnResult();
        checkInformationReturnResult.setStatus(status);
        checkInformationReturnResult.setMessage(message);
        return checkInformationReturnResult;
    }
}
