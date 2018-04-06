package org.tizzer.smmgr.model.analysis;

import org.tizzer.smmgr.model.response.ResultResponse;

public class IdentityCostResponseDto extends ResultResponse {
    private Double consumerCost;
    private Double insiderCost;

    public Double getConsumerCost() {
        return consumerCost;
    }

    public void setConsumerCost(Double consumerCost) {
        this.consumerCost = consumerCost;
    }

    public Double getInsiderCost() {
        return insiderCost;
    }

    public void setInsiderCost(Double insiderCost) {
        this.insiderCost = insiderCost;
    }
}
