package com.my.spring.enums;

import java.io.Serializable;

/**
 * Created by TIANCHENGYUAN103 on 2015-12-04.
 */
public enum ErrorCodeEnum implements Serializable {

    No_Error("No Error!", 0),
    Error("Unknown Error!", 1),
    Empty_Inputs("Empty Inputs Error!", 2),
    User_Existed("User Name Existed!",3),
    User_Not_Existed("User Does not Existed!",4),
    Password_Error("Password Error",5),
    User_Not_Logined("User Does not login",6),
    AUTH_Error("Auth Error",7),
    ;

    private String label;
    private Integer code;

    ErrorCodeEnum() {
    }

    ErrorCodeEnum(String label, Integer code) {
        this.label = label;
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code.toString();
    }

    public static ErrorCodeEnum parse(int code) {
        for (ErrorCodeEnum theEnum : ErrorCodeEnum.values()) {
            if (theEnum.getCode() == code) {
                return theEnum;
            }
        }
        return No_Error;
    }
}
