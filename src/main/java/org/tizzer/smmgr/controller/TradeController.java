package org.tizzer.smmgr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.RequestContext;
import org.tizzer.smmgr.common.Log;
import org.tizzer.smmgr.constant.ResultCode;
import org.tizzer.smmgr.entity.TradeRecord;
import org.tizzer.smmgr.entity.TradeSpec;
import org.tizzer.smmgr.model.request.QueryRefundRecordRequestDto;
import org.tizzer.smmgr.model.request.QueryTradeRecordRequestDto;
import org.tizzer.smmgr.model.request.QueryTradeSpecRequestDto;
import org.tizzer.smmgr.model.request.SaveTradeRecordRequestDto;
import org.tizzer.smmgr.model.response.*;
import org.tizzer.smmgr.repository.PayTypeRepository;
import org.tizzer.smmgr.repository.TradeRecordRepository;
import org.tizzer.smmgr.repository.TradeSpecRepository;
import org.tizzer.smmgr.utils.SerialUtil;
import org.tizzer.smmgr.utils.TimeUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/smmgr")
public class TradeController {

    private final PayTypeRepository payTypeRepository;
    private final TradeRecordRepository tradeRecordRepository;
    private final TradeSpecRepository tradeSpecRepository;

    @Autowired
    public TradeController(PayTypeRepository payTypeRepository, TradeRecordRepository tradeRecordRepository, TradeSpecRepository tradeSpecRepository) {
        this.payTypeRepository = payTypeRepository;
        this.tradeRecordRepository = tradeRecordRepository;
        this.tradeSpecRepository = tradeSpecRepository;
    }

    /**
     * 查询所有付款类型
     *
     * @return
     */
    @GetMapping("/query/pay/type")
    public QueryPayTypeResponseDto<String> queryPayType() {
        QueryPayTypeResponseDto<String> queryPayTypeResponseDto = new QueryPayTypeResponseDto<>();
        try {
            List<String> payTypes = payTypeRepository.findAllPayType();
            queryPayTypeResponseDto.setData(payTypes);
            queryPayTypeResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            queryPayTypeResponseDto.setMessage(e.getMessage());
            queryPayTypeResponseDto.setCode(ResultCode.ERROR);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return queryPayTypeResponseDto;
    }

    /**
     * 保存交易记录
     *
     * @param saveTradeRecordRequestDto
     * @return
     */
    @Transactional
    @PostMapping(path = "/save/trade/record")
    public SaveTradeRecordResponseDto tradeGoods(SaveTradeRecordRequestDto saveTradeRecordRequestDto) {
        SaveTradeRecordResponseDto saveTradeRecordResponseDto = new SaveTradeRecordResponseDto();
        try {
            List<Object> params;
            if (saveTradeRecordRequestDto.getType()) {
                params = SerialUtil.getIncomeSerialNo();
            } else {
                params = SerialUtil.getRefundSerialNo();
            }
            TradeRecord tradeRecord = new TradeRecord();
            tradeRecord.setSerialNo((String) params.get(0));
            tradeRecord.setSoldTime((Date) params.get(1));
            tradeRecord.setMarkNo((String) params.get(2));
            tradeRecord.setStaffNo(saveTradeRecordRequestDto.getStaffNo());
            tradeRecord.setDiscount(saveTradeRecordRequestDto.getDiscount());
            tradeRecord.setPayType(saveTradeRecordRequestDto.getPayType());
            tradeRecord.setCardNo(saveTradeRecordRequestDto.getCardNo());
            tradeRecord.setPhone(saveTradeRecordRequestDto.getPhone());
            tradeRecord.setCost(saveTradeRecordRequestDto.getCost());
            tradeRecord.setType(saveTradeRecordRequestDto.getType());
            tradeRecord.setOriginalSerial(saveTradeRecordRequestDto.getSerialNo());
            tradeRecordRepository.save(tradeRecord);
            TradeSpec tradeSpec;
            int length = saveTradeRecordRequestDto.getUpc().length;
            for (int i = 0; i < length; i++) {
                tradeSpec = new TradeSpec();
                tradeSpec.setUpc(saveTradeRecordRequestDto.getUpc()[i]);
                tradeSpec.setName(saveTradeRecordRequestDto.getName()[i]);
                tradeSpec.setPrimeCost(saveTradeRecordRequestDto.getPrimeCost()[i]);
                tradeSpec.setPresentCost(saveTradeRecordRequestDto.getPresentCost()[i]);
                tradeSpec.setQuantity(saveTradeRecordRequestDto.getQuantity()[i]);
                tradeSpec.setSerialNo(tradeRecord.getSerialNo());
                tradeSpecRepository.save(tradeSpec);
            }
            saveTradeRecordResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            saveTradeRecordResponseDto.setMessage(e.getMessage());
            saveTradeRecordResponseDto.setCode(ResultCode.ERROR);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return saveTradeRecordResponseDto;
    }

    /**
     * 查询满足条件的所有交易记录
     *
     * @param queryTradeRecordRequestDto
     * @return
     */
    @GetMapping("/query/trade/record")
    public ResultListResponse<QueryTradeRecordResponseDto> queryAllRecord(QueryTradeRecordRequestDto queryTradeRecordRequestDto) {
        ResultListResponse<QueryTradeRecordResponseDto> res = new ResultListResponse<>();
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "soldTime");
            Pageable pageable = new PageRequest(queryTradeRecordRequestDto.getCurrentPage(), queryTradeRecordRequestDto.getPageSize(), sort);
            Specification<TradeRecord> specification = new Specification<TradeRecord>() {
                @Override
                public Predicate toPredicate(Root<TradeRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicates = new ArrayList<>();
                    if (!queryTradeRecordRequestDto.getStaffNo().equals("")) {
                        predicates.add(cb.equal(root.get("staffNo"), queryTradeRecordRequestDto.getStaffNo()));
                    }
                    if (queryTradeRecordRequestDto.getStartDate() != null) {
                        predicates.add(cb.greaterThanOrEqualTo(root.get("soldTime"), TimeUtil.string2DateTime(queryTradeRecordRequestDto.getStartDate())));
                    }
                    if (queryTradeRecordRequestDto.getEndDate() != null) {
                        predicates.add(cb.lessThanOrEqualTo(root.get("soldTime"), TimeUtil.string2DateTime(queryTradeRecordRequestDto.getEndDate())));
                    }
                    if (!queryTradeRecordRequestDto.getKeyword().equals("")) {
                        predicates.add(cb.like(root.get("serialNo"), "%" + queryTradeRecordRequestDto.getKeyword() + "%"));
                    }
                    if (!predicates.isEmpty()) {
                        query.where(predicates.toArray(new Predicate[0]));
                    }
                    return null;
                }
            };
            Page<TradeRecord> page = tradeRecordRepository.findAll(specification, pageable);
            for (TradeRecord tradeRecord : page.getContent()) {
                QueryTradeRecordResponseDto queryTradeRecordResponseDto = new QueryTradeRecordResponseDto();
                queryTradeRecordResponseDto.setSerialNo(tradeRecord.getSerialNo());
                queryTradeRecordResponseDto.setMarkNo(tradeRecord.getMarkNo());
                queryTradeRecordResponseDto.setType(tradeRecord.isType());
                queryTradeRecordResponseDto.setSoldTime(tradeRecord.getSoldTime());
                res.setData(queryTradeRecordResponseDto);
            }
            res.setPageCount(page.getTotalPages());
            res.setCode(ResultCode.OK);
        } catch (Exception e) {
            res.setMessage(e.getMessage());
            res.setCode(ResultCode.ERROR);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 查询交易记录的详情
     *
     * @param queryTradeSpecRequestDto
     * @return
     */
    @GetMapping("/query/trade/spec")
    public QueryTradeSpecResponseDto<TradeSpec> queryTradeSpec(QueryTradeSpecRequestDto queryTradeSpecRequestDto) {
        QueryTradeSpecResponseDto<TradeSpec> queryTradeSpecResponseDto = new QueryTradeSpecResponseDto<>();
        try {
            TradeRecord tradeRecord = tradeRecordRepository.findOne(queryTradeSpecRequestDto.getSerialNo());
            List<TradeSpec> tradeSpecs = tradeSpecRepository.findAllBySerialNo(queryTradeSpecRequestDto.getSerialNo());
            queryTradeSpecResponseDto.setData(tradeSpecs);
            queryTradeSpecResponseDto.setCardNo(tradeRecord.getCardNo());
            queryTradeSpecResponseDto.setPhone(tradeRecord.getPhone());
            queryTradeSpecResponseDto.setCost(tradeRecord.getCost());
            queryTradeSpecResponseDto.setPayType(tradeRecord.getPayType());
            queryTradeSpecResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            queryTradeSpecResponseDto.setMessage(e.getMessage());
            queryTradeSpecResponseDto.setCode(ResultCode.ERROR);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return queryTradeSpecResponseDto;
    }

    /**
     * 查询可退货记录
     *
     * @param request
     * @param queryRefundRecordRequestDto
     * @return
     */
    @GetMapping("/query/refund/record")
    public QueryRefundRecordResponseDto<TradeSpec> queryRefundRecord(HttpServletRequest request, QueryRefundRecordRequestDto queryRefundRecordRequestDto) {
        RequestContext requestContext = new RequestContext(request);
        QueryRefundRecordResponseDto<TradeSpec> queryRefundRecordResponseDto = new QueryRefundRecordResponseDto<>();
        try {
            TradeRecord tradeRecord = tradeRecordRepository.findOne(queryRefundRecordRequestDto.getSerialNo());
            if (tradeRecord != null) {
                String result = tradeRecordRepository.isExist(queryRefundRecordRequestDto.getSerialNo());
                if (result == null) {
                    List<TradeSpec> tradeSpecs = tradeSpecRepository.findAllBySerialNo(queryRefundRecordRequestDto.getSerialNo());
                    queryRefundRecordResponseDto.setData(tradeSpecs);
                    queryRefundRecordResponseDto.setCardNo(tradeRecord.getCardNo());
                    queryRefundRecordResponseDto.setPhone(tradeRecord.getPhone());
                    queryRefundRecordResponseDto.setCost(tradeRecord.getCost());
                    queryRefundRecordResponseDto.setPayType(tradeRecord.getPayType());
                    queryRefundRecordResponseDto.setDiscount(tradeRecord.getDiscount());
                    queryRefundRecordResponseDto.setCode(ResultCode.OK);
                } else {
                    queryRefundRecordResponseDto.setMessage(requestContext.getMessage("msg.query.refund.result.stop"));
                    queryRefundRecordResponseDto.setCode(ResultCode.ERROR);
                }
            } else {
                queryRefundRecordResponseDto.setMessage(requestContext.getMessage("msg.query.record.result.none"));
                queryRefundRecordResponseDto.setCode(ResultCode.ERROR);
            }
        } catch (Exception e) {
            queryRefundRecordResponseDto.setMessage(e.getMessage());
            queryRefundRecordResponseDto.setCode(ResultCode.ERROR);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return queryRefundRecordResponseDto;
    }

}
