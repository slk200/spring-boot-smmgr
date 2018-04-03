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
import org.tizzer.smmgr.common.LogLevel;
import org.tizzer.smmgr.common.Logcat;
import org.tizzer.smmgr.constant.ResultCode;
import org.tizzer.smmgr.entity.LossRecord;
import org.tizzer.smmgr.entity.LossSpec;
import org.tizzer.smmgr.model.request.QueryLossRecordRequestDto;
import org.tizzer.smmgr.model.request.QueryLossSpecRequestDto;
import org.tizzer.smmgr.model.request.SaveLossRecordRequestDto;
import org.tizzer.smmgr.model.response.QueryLossRecordResponseDto;
import org.tizzer.smmgr.model.response.QueryLossSpecResponseDto;
import org.tizzer.smmgr.model.response.ResultListResponse;
import org.tizzer.smmgr.model.response.SaveLossRecordResponseDto;
import org.tizzer.smmgr.repository.LossRecordRepository;
import org.tizzer.smmgr.repository.LossSpecRepository;
import org.tizzer.smmgr.utils.SerialUtil;
import org.tizzer.smmgr.utils.TimeUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/smmgr")
public class LossController {

    @Autowired
    LossRecordRepository lossRecordRepository;

    @Autowired
    LossSpecRepository lossSpecRepository;

    /**
     * 保存报损记录
     *
     * @param saveLossRecordRequestDto
     * @return
     */
    @Transactional
    @PostMapping(path = "/save/loss/record")
    public SaveLossRecordResponseDto saveLossRecord(SaveLossRecordRequestDto saveLossRecordRequestDto) {
        SaveLossRecordResponseDto saveLossRecordResponseDto = new SaveLossRecordResponseDto();
        try {
            List<Object> params = SerialUtil.getLossSerialNo();
            LossRecord lossRecord = new LossRecord();
            lossRecord.setId((String) params.get(0));
            lossRecord.setCreateAt((Date) params.get(1));
            lossRecord.setMarkNo((String) params.get(2));
            lossRecord.setStaffNo(saveLossRecordRequestDto.getStaffNo());
            lossRecord.setCost(saveLossRecordRequestDto.getCost());
            lossRecord.setNote(saveLossRecordRequestDto.getNote());
            lossRecordRepository.save(lossRecord);
            LossSpec lossSpec;
            int length = saveLossRecordRequestDto.getUpc().length;
            for (int i = 0; i < length; i++) {
                lossSpec = new LossSpec();
                lossSpec.setUpc(saveLossRecordRequestDto.getUpc()[i]);
                lossSpec.setName(saveLossRecordRequestDto.getName()[i]);
                lossSpec.setPrimeCost(saveLossRecordRequestDto.getPrimeCost()[i]);
                lossSpec.setQuantity(saveLossRecordRequestDto.getQuantity()[i]);
                lossSpec.setSerialNo(lossRecord.getId());
                lossSpecRepository.save(lossSpec);
            }
            saveLossRecordResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            saveLossRecordResponseDto.setMessage(e.getMessage());
            saveLossRecordResponseDto.setCode(ResultCode.ERROR);
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
        }
        return saveLossRecordResponseDto;
    }

    /**
     * 查询报损记录
     *
     * @param queryLossRecordRequestDto
     * @return
     */
    @GetMapping(path = "/query/loss/record")
    public ResultListResponse<QueryLossRecordResponseDto> queryLossRecord(QueryLossRecordRequestDto queryLossRecordRequestDto) {
        ResultListResponse<QueryLossRecordResponseDto> res = new ResultListResponse<>();
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "createAt");
            Pageable pageable = new PageRequest(queryLossRecordRequestDto.getCurrentPage(), queryLossRecordRequestDto.getPageSize(), sort);
            Specification<LossRecord> specification = new Specification<LossRecord>() {
                @Override
                public Predicate toPredicate(Root<LossRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicates = new ArrayList<>();
                    if (queryLossRecordRequestDto.getStartDate() != null) {
                        predicates.add(cb.greaterThanOrEqualTo(root.get("createAt"), TimeUtil.string2Day(queryLossRecordRequestDto.getStartDate())));
                    }
                    if (queryLossRecordRequestDto.getEndDate() != null) {
                        predicates.add(cb.lessThanOrEqualTo(root.get("createAt"), TimeUtil.string2Day(queryLossRecordRequestDto.getEndDate())));
                    }
                    if (!queryLossRecordRequestDto.getKeyword().equals("")) {
                        predicates.add(cb.like(root.get("id"), "%" + queryLossRecordRequestDto.getKeyword() + "%"));
                    }
                    if (!predicates.isEmpty()) {
                        query.where(predicates.toArray(new Predicate[0]));
                    }
                    return null;
                }
            };
            Page<LossRecord> page = lossRecordRepository.findAll(specification, pageable);
            for (LossRecord lossRecord : page.getContent()) {
                QueryLossRecordResponseDto queryLossRecordResponseDto = new QueryLossRecordResponseDto();
                queryLossRecordResponseDto.setId(lossRecord.getId());
                queryLossRecordResponseDto.setMarkNo(lossRecord.getMarkNo());
                queryLossRecordResponseDto.setCreateAt(lossRecord.getCreateAt());
                res.setData(queryLossRecordResponseDto);
            }
            res.setPageCount(page.getTotalPages());
            res.setCode(ResultCode.OK);
        } catch (Exception e) {
            res.setMessage(e.getMessage());
            res.setCode(ResultCode.ERROR);
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 查询报损详情
     *
     * @param queryLossSpecRequestDto
     * @return
     */
    @GetMapping(path = "/query/loss/spec")
    public QueryLossSpecResponseDto<LossSpec> queryLossSpec(QueryLossSpecRequestDto queryLossSpecRequestDto) {
        QueryLossSpecResponseDto<LossSpec> queryLossSpecResponseDto = new QueryLossSpecResponseDto<>();
        try {
            LossRecord lossRecord = lossRecordRepository.findOne(queryLossSpecRequestDto.getId());
            List<LossSpec> lossSpecs = lossSpecRepository.findAllBySerialNo(queryLossSpecRequestDto.getId());
            queryLossSpecResponseDto.setData(lossSpecs);
            queryLossSpecResponseDto.setCost(lossRecord.getCost());
            queryLossSpecResponseDto.setNote(lossRecord.getNote());
            queryLossSpecResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            queryLossSpecResponseDto.setMessage(e.getMessage());
            queryLossSpecResponseDto.setCode(ResultCode.ERROR);
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
        }
        return queryLossSpecResponseDto;
    }

}
