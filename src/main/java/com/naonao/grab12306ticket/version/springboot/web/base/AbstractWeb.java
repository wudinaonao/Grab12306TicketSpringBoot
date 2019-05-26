package com.naonao.grab12306ticket.version.springboot.web.base;

import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.springboot.base.AbstractSpringBoot;
import com.naonao.grab12306ticket.version.springboot.constants.TaskStatusName;
import com.naonao.grab12306ticket.version.springboot.entity.database.GrabTicketInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.database.NotificationInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.database.StatusInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.database.UserInformationEntity;
import com.naonao.grab12306ticket.version.springboot.mapper.*;
import com.naonao.grab12306ticket.version.springboot.service.tools.GeneralTools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: Grab12306Ticket-SpringBoot
 * @description: web view abstract class, these are general methods
 * @author: Wen lyuzhao
 * @create: 2019-05-14 23:26
 **/
public class AbstractWeb extends AbstractSpringBoot {

    @Autowired
    protected UserInformationMapper userInformationMapper;

    @Autowired
    protected GrabTicketInformationMapper grabTicketInformationMapper;

    @Autowired
    protected NotificationInformationMapper notificationInformationMapper;

    @Autowired
    protected StatusInformationMapper statusInformationMapper;

    @Autowired
    protected AllInformationMapper allInformationMapper;

    protected static final String USERNAME_AND_PASSWORD_HAVE_NOT_BEEN_VERIFIED = "12306 username and password have not been verified";
    protected static final String INFORMATION_INVALID = "information invalid.";

    /**
     * encapsulation userInformationEntity by username12306 and password12306,
     * input format is json
     * @param userInformationJson   userInformationJson
     * @param hash                  hash
     * @return                      userInformationEntity
     */
    protected UserInformationEntity userInformationEntity(JSONObject userInformationJson, String hash){
        UserInformationEntity userInformationEntity = new UserInformationEntity();
        userInformationEntity.setId(null);
        userInformationEntity.setUsername12306(userInformationJson.getString("username12306"));
        userInformationEntity.setPassword12306(userInformationJson.getString("password12306"));
        userInformationEntity.setHash(hash);
        return userInformationEntity;
    }

    /**
     * encapsulation userInformationEntity by username12306 and password12306,
     * input format is map
     * @param userInformationMap    userInformationMap
     * @param hash                  hash
     * @return                      userInformationEntity
     */
    protected UserInformationEntity userInformationEntity(Map<String, String> userInformationMap, String hash){
        UserInformationEntity userInformationEntity = new UserInformationEntity();
        userInformationEntity.setId(null);
        userInformationEntity.setUsername12306(userInformationMap.get("username12306"));
        userInformationEntity.setPassword12306(userInformationMap.get("password12306"));
        userInformationEntity.setHash(hash);
        return userInformationEntity;
    }

    /**
     * encapsulation grabTicketInformationEntity
     * json format:
     * {
     *     "id":null,
     *     "afterTime":"",
     *     "beforeTime":"",
     *     "trainDate":"",
     *     "fromStation":"",
     *     "purposeCode":"",
     *     "trainName":"",
     *     "backTrainDate":"",
     *     "passengerName":"",
     *     "documentType":"",
     *     "documentNumber":"",
     *     "mobile":"",
     *     "seatType":[],
     *     "expectSeatNumber":[],
     * }
     * @param grabTicketInformationJson     grabTicketInformationJson
     * @param hash                          hash
     * @return                              GrabTicketInformationEntity
     */
    protected GrabTicketInformationEntity grabTicketInformationEntity(JSONObject grabTicketInformationJson, String hash){
        GrabTicketInformationEntity grabTicketInformationEntity = new GrabTicketInformationEntity();
        grabTicketInformationEntity.setId(null);
        grabTicketInformationEntity.setAfterTime(grabTicketInformationJson.getString("afterTime"));
        grabTicketInformationEntity.setBeforeTime(grabTicketInformationJson.getString("beforeTime"));
        grabTicketInformationEntity.setTrainDate(grabTicketInformationJson.getString("trainDate"));
        grabTicketInformationEntity.setFromStation(grabTicketInformationJson.getString("fromStation"));
        grabTicketInformationEntity.setToStation(grabTicketInformationJson.getString("toStation"));
        grabTicketInformationEntity.setPurposeCode(grabTicketInformationJson.getString("purposeCode"));
        grabTicketInformationEntity.setTrainName(grabTicketInformationJson.getString("trainName"));
        grabTicketInformationEntity.setBackTrainDate(grabTicketInformationJson.getString("backTrainDate"));
        grabTicketInformationEntity.setPassengerName(grabTicketInformationJson.getString("passengerName"));
        grabTicketInformationEntity.setDocumentType(grabTicketInformationJson.getString("documentType"));
        grabTicketInformationEntity.setDocumentNumber(grabTicketInformationJson.getString("documentNumber"));
        grabTicketInformationEntity.setMobile(grabTicketInformationJson.getString("mobile"));
        grabTicketInformationEntity.setSeatType(StringUtils.join(grabTicketInformationJson.getJSONArray("seatType"), ","));
        grabTicketInformationEntity.setExpectSeatNumber(grabTicketInformationJson.getString("expectSeatNumber"));
        grabTicketInformationEntity.setHash(hash);
        return grabTicketInformationEntity;

    }

    /**
     * encapsulation notificationInformationEntity
     * json format:
     * {
     *     "id":null,
     *     "receiverEmail":"",
     *     "sendEmail":"",
     *     "sendEmailHost":"",
     *     "sendEmailPort":"",
     *     "sendEmailUsername":"",
     *     "sendEmailPassword":"",
     *     "receiverPhone":"",
     *     "notificationMode":[],
     * }
     * @param notificationInformationJson       notificationInformationJson
     * @param hash                              hash
     * @return                                  NotificationInformationEntity
     */
    protected NotificationInformationEntity notificationInformationEntity(JSONObject notificationInformationJson, String hash){
        NotificationInformationEntity notificationInformationEntity = new NotificationInformationEntity();
        notificationInformationEntity.setId(null);
        notificationInformationEntity.setReceiverEmail(notificationInformationJson.getString("receiverEmail"));
        notificationInformationEntity.setSendEmail(notificationInformationJson.getString("sendEmail"));
        notificationInformationEntity.setSendEmailHost(notificationInformationJson.getString("sendEmailHost"));
        notificationInformationEntity.setSendEmailPort(notificationInformationJson.getString("sendEmailPort"));
        notificationInformationEntity.setSendEmailUsername(notificationInformationJson.getString("sendEmailUsername"));
        notificationInformationEntity.setSendEmailPassword(notificationInformationJson.getString("sendEmailPassword"));
        notificationInformationEntity.setReceiverPhone(notificationInformationJson.getString("receiverPhone"));
        notificationInformationEntity.setNotificationMode(StringUtils.join(notificationInformationJson.getJSONArray("notificationMode"), ","));
        notificationInformationEntity.setHash(hash);
        return notificationInformationEntity;

    }

    /**
     * first join task to database status information is default
     * @param hash      hash
     * @return          StatusInformationEntity
     */
    protected StatusInformationEntity statusInformationEntity(String hash){
        StatusInformationEntity statusInformationEntity = new StatusInformationEntity();
        statusInformationEntity.setId(null);
        statusInformationEntity.setStatus(TaskStatusName.WAIT.getTaskStatusName());
        statusInformationEntity.setTaskStartTime(GeneralTools.currentDateAndTime());
        statusInformationEntity.setTaskEndTime("");
        statusInformationEntity.setTaskLastRunTime("");
        statusInformationEntity.setTryCount(0L);
        statusInformationEntity.setMessage(TaskStatusName.WAIT.getTaskStatusName());
        statusInformationEntity.setHash(hash);
        return statusInformationEntity;
    }

    /**
     * encapsulation input data to map, map element format is json
     * @param inputData     input data, format is string
     * @return              map
     */
    protected Map<String, Object> encapsulationJsonMap(String inputData){
        Map<String, Object> jsonMap = new HashMap<>(16);
        JSONObject jsonObject = JSONObject.parseObject(inputData);
        JSONObject grabticketJson = jsonObject.getJSONObject("grabticketinformation");
        JSONObject notificationJson = jsonObject.getJSONObject("notificationinformation");
        jsonMap.put("grabticketinforamtion", grabticketJson);
        jsonMap.put("notificationinformation", notificationJson);
        return jsonMap;
    }


    protected Map<String, ?> entityMap(Map<String, JSONObject> jsonMap, String hash){
        Map<String, Object> entityMap = new HashMap<>(16);
        entityMap.put("userInformationEntity", userInformationEntity(jsonMap.get("userinformation"), hash));
        entityMap.put("grabTicketInformationEntity", grabTicketInformationEntity(jsonMap.get("grabticketinforamtion"), hash));
        entityMap.put("notificationInformationEntity", notificationInformationEntity(jsonMap.get("notificationinformation"),hash));
        entityMap.put("statusInformationEntity", statusInformationEntity(hash));
        return entityMap;
    }

    /**
     * get username and password by session.
     * if username or password is null, then return null.
     * if session exist return a map, map element is username
     * and password.
     * map format:
     * {
     *     "username12306":"",
     *     "password12306":""
     * }
     * @param request   request
     * @return          map
     */
    protected Map<String, String> getUsernameAndPasswordBySession(HttpServletRequest request){
        Map<String, String> map = new HashMap<>(16);
        String username12306 = (String) request.getSession().getAttribute("username12306");
        String password12306 = (String) request.getSession().getAttribute("password12306");
        if (username12306 == null || password12306 == null){
            return null;
        }
        map.put("username12306", username12306);
        map.put("password12306", password12306);
        return map;
    }

    /**
     * check username and password based on the session,
     * if there is no match, then return false.
     * @param request   request
     * @return          Boolean
     */
    protected Boolean authentication(HttpServletRequest request){
        String username12306 = (String) request.getSession().getAttribute("username12306");
        String password12306 = (String) request.getSession().getAttribute("password12306");
        if (username12306 == null || password12306 == null){
            return false;
        }
        return true;
    }

}
