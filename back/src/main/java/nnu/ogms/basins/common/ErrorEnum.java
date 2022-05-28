package nnu.ogms.basins.common;

import lombok.Data;
import lombok.Getter;

@Getter
public enum ErrorEnum {
    QUERY_SCOPE_ERROR(10001,400,"查询范围错误"),
    LONGITUDE_SCOPE_ERROR(10002,400,"纬度范围错误"),
    LATITUDE_SCOPE_ERROR(10002,400,"经度范围错误"),
    QUERY_SUBBASIN_SCOPE_ERROR(10003,400,"子流域查询范围错误"),
    UPLOAD_FILENAME_EMPTY(20001,400,"文件名为空"),
    FILE_MAX_SIZE(20002,400,"文件大小超出最大值"),
    FILE_EMPTY(20003,400,"文件为空"),
    NEW_PROJECT_PATH_EXIST(20004,400,"该项目名称已存在,请重新命名"),
    NEW_PROJECT_NAME_EMPTY(20005,400,"项目名称为空"),
    SAVE_GEOJSON_ERROR(20006,400,"保存流域geojson失败"),
    GEOJSON_TO_SHP_ERROR(20007,400,"流域范围geojson转shp失败"),
    FILE_COPY_ERROR(20008,400,"文件复制失败"),
    FILE_CONFIG_WRITE_ERROR(20009,400,"config文件修改失败"),
    FILE_POINT_SHP_ERROR(20010,400,"point点坐标生成shp文件失败"),
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
