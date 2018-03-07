package org.tizzer.smmgr.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "resultResponse", description = "返回对象")
public class ResultResponse {
    @ApiModelProperty(value = "状态码")
    private int code;
    @ApiModelProperty(value = "响应信息")
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
