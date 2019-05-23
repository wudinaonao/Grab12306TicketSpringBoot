package com.naonao.grab12306ticket.version.springboot.service.base;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-15 13:19
 **/
public class AbstractPreprocess extends AbstractService {

    protected static final Integer HOUR_MAX_VALUE = 24;
    protected static final Integer MIN_MAX_VALUE = 60;

    protected static final Integer PORT_MAX_VALUE = 65535;

    protected static final String TIME_IS_NULL = "time is null.";
    protected static final String TIME_FORMAT_IS_ERROR = "time format is error.";
    protected static final String TIME_PARSE_FAILED = "time parse failed.";
    protected static final String DATE_IS_NULL = "date is null.";
    protected static final String SET_DATE_IT_IS_LESS_THAN_THE_CURRENT_DATE = "set date it is less than the current date.";
    protected static final String DATE_PARSE_FAILED = "date parse failed.";
    protected static final String STATION_NAME_IS_NULL = "station name is null.";
    protected static final String NOT_FOUND_STATION_NAME = "not found station name.";
    protected static final String MOBILE_IS_NULL = "mobile is null.";
    protected static final String MOBILE_HAS_INVALID_SYMBOL = "mobile has invalid symbol.";
    protected static final String SEAT_TYPE_IS_NULL = "seat type is null.";
    protected static final String SEAT_TYPE_INVALID = "seat type invalid.";

    protected static final String EMAIL_ADDRESS_INVALID = "email address invalid.";
    protected static final String EMAIL_PORT_INVALID = "email port invalid.";
    protected static final String NOTIFICATION_MODE_INVAILD = "notification mode invalid.";

    protected static final String[] NOTIFICATION_MODE_NAME_ARRAY = {"EMAIL", "PHONE", "SMS"};


}
