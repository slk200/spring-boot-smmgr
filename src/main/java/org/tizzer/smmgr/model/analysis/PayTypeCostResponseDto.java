package org.tizzer.smmgr.model.analysis;

import org.tizzer.smmgr.model.response.ResultResponse;

public class PayTypeCostResponseDto extends ResultResponse {
    private String name;
    private Double value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
