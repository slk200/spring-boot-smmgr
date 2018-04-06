package org.tizzer.smmgr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tizzer.smmgr.common.LogLevel;
import org.tizzer.smmgr.common.Logcat;
import org.tizzer.smmgr.constant.ResultCode;
import org.tizzer.smmgr.model.analysis.IdentityCostResponseDto;
import org.tizzer.smmgr.model.analysis.PayTypeCostResponseDto;
import org.tizzer.smmgr.model.analysis.ResultListResponse;
import org.tizzer.smmgr.repository.PayTypeRepository;
import org.tizzer.smmgr.repository.TradeRecordRepository;

import java.util.List;

@RestController
@RequestMapping(path = "/smmgr")
public class AnalysisController {

    @Autowired
    TradeRecordRepository tradeRecordRepository;

    @Autowired
    PayTypeRepository payTypeRepository;

    @GetMapping(path = "/analysis/identity")
    public IdentityCostResponseDto identityCost() {
        IdentityCostResponseDto identityCostResponseDto = new IdentityCostResponseDto();
        try {
            double consumerCost = tradeRecordRepository.getConsumerCost();
            double insiderCost = tradeRecordRepository.getInsiderCost();
            identityCostResponseDto.setConsumerCost(consumerCost);
            identityCostResponseDto.setInsiderCost(insiderCost);
            identityCostResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            identityCostResponseDto.setMessage(e.getMessage());
            identityCostResponseDto.setCode(ResultCode.ERROR);
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
        }
        return identityCostResponseDto;
    }

    @GetMapping(path = "/analysis/pay")
    public ResultListResponse<PayTypeCostResponseDto> payTypeCost() {
        ResultListResponse<PayTypeCostResponseDto> res = new ResultListResponse<>();
        try {
            List<String> payTypes = payTypeRepository.findAllPayType();
            PayTypeCostResponseDto payTypeCostResponseDto;
            for (String payType : payTypes) {
                payTypeCostResponseDto = new PayTypeCostResponseDto();
                Double cost = tradeRecordRepository.getPayTypeCost(payType);
                payTypeCostResponseDto.setName(payType);
                payTypeCostResponseDto.setValue(cost);
                res.setData(payTypeCostResponseDto);
            }
            res.setCode(ResultCode.OK);
        } catch (Exception e) {
            res.setMessage(e.getMessage());
            res.setCode(ResultCode.ERROR);
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
        }
        return res;
    }

}
