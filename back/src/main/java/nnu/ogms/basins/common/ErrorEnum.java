package nnu.ogms.basins.common;

import lombok.Data;

@Data
public enum ErrorEnum {
    QUERY_SCOPE_ERROR(10001,400,"查询范围错误"),
    LONGITUDE_SCOPE_ERROR(10002,400,"纬度范围错误"),
    LATITUDE_SCOPE_ERROR(10002,400,"经度范围错误"),
    ;

    private Integer code;
    private Integer httpCode;
    private String message;

    ErrorEnum(Integer code, Integer httpCode, String message) {
        this.code = code;
        this.httpCode = httpCode;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
