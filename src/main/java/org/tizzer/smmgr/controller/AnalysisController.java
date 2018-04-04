package org.tizzer.smmgr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tizzer.smmgr.common.LogLevel;
import org.tizzer.smmgr.common.Logcat;
import org.tizzer.smmgr.constant.ResultCode;
import org.tizzer.smmgr.model.analysis.AnalysisResponseDto;
import org.tizzer.smmgr.repository.TradeRecordRepository;

@RestController
@RequestMapping(path = "/smmgr")
public class AnalysisController {

    @Autowired
    TradeRecordRepository tradeRecordRepository;

    @GetMapping(path = "/analysis")
    public AnalysisResponseDto analysis() {
        AnalysisResponseDto analysisResponseDto = new AnalysisResponseDto();
        try {
            double consumerCost = tradeRecordRepository.getConsumerCost();
            double insiderCost = tradeRecordRepository.getInsiderCost();
            analysisResponseDto.setConsumerCost(consumerCost);
            analysisResponseDto.setInsiderCost(insiderCost);
            analysisResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            analysisResponseDto.setMessage(e.getMessage());
            analysisResponseDto.setCode(ResultCode.ERROR);
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
        }
        return analysisResponseDto;
    }

}
