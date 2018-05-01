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
import org.tizzer.smmgr.common.Log;
import org.tizzer.smmgr.constant.ResultCode;
import org.tizzer.smmgr.entity.Store;
import org.tizzer.smmgr.entity.TransRecord;
import org.tizzer.smmgr.entity.TransSpec;
import org.tizzer.smmgr.model.request.QueryTransRecordRequestDto;
import org.tizzer.smmgr.model.request.QueryTransSpecRequestDto;
import org.tizzer.smmgr.model.request.SaveTransRecordRequestDto;
import org.tizzer.smmgr.model.response.QueryTransRecordResponseDto;
import org.tizzer.smmgr.model.response.QueryTransSpecResponseDto;
import org.tizzer.smmgr.model.response.ResultListResponse;
import org.tizzer.smmgr.model.response.SaveTransRecordResponseDto;
import org.tizzer.smmgr.repository.StoreRepository;
import org.tizzer.smmgr.repository.TransRecordRepository;
import org.tizzer.smmgr.repository.TransSpecRepository;
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
public class TransController {

    private final StoreRepository storeRepository;
    private final TransRecordRepository transRecordRepository;
    private final TransSpecRepository transSpecRepository;

    @Autowired
    public TransController(StoreRepository storeRepository, TransRecordRepository transRecordRepository, TransSpecRepository transSpecRepository) {
        this.storeRepository = storeRepository;
        this.transRecordRepository = transRecordRepository;
        this.transSpecRepository = transSpecRepository;
    }

    /**
     * 保存调货记录
     *
     * @param saveTransRecordRequestDto
     * @return
     */
    @Transactional
    @PostMapping(path = "/save/trans/record")
    public SaveTransRecordResponseDto saveTransRecord(SaveTransRecordRequestDto saveTransRecordRequestDto) {
        SaveTransRecordResponseDto saveTransRecordResponseDto = new SaveTransRecordResponseDto();
        try {
            Store store = storeRepository.findOne(saveTransRecordRequestDto.getStoreId());
            TransRecord transRecord = new TransRecord();
            transRecord.setStore(store);
            transRecord.setCost(saveTransRecordRequestDto.getCost());
            transRecord.setCreateAt(new Date());
            transRecordRepository.save(transRecord);
            TransSpec transSpec;
            int length = saveTransRecordRequestDto.getUpc().length;
            for (int i = 0; i < length; i++) {
                transSpec = new TransSpec();
                transSpec.setUpc(saveTransRecordRequestDto.getUpc()[i]);
                transSpec.setName(saveTransRecordRequestDto.getName()[i]);
                transSpec.setPrimeCost(saveTransRecordRequestDto.getPrimeCost()[i]);
                transSpec.setQuantity(saveTransRecordRequestDto.getQuantity()[i]);
                transSpec.setSerialNo(transRecord.getId());
                transSpecRepository.save(transSpec);
            }
            saveTransRecordResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            saveTransRecordResponseDto.setMessage(e.getMessage());
            saveTransRecordResponseDto.setCode(ResultCode.OK);
            Log.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
        }
        return saveTransRecordResponseDto;
    }

    /**
     * 查询调货记录
     *
     * @param queryTransRecordRequestDto
     * @return
     */
    @GetMapping(path = "/query/trans/record")
    public ResultListResponse<QueryTransRecordResponseDto> queryTransRecord(QueryTransRecordRequestDto queryTransRecordRequestDto) {
        ResultListResponse<QueryTransRecordResponseDto> res = new ResultListResponse<>();
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "createAt");
            Pageable pageable = new PageRequest(queryTransRecordRequestDto.getCurrentPage(), queryTransRecordRequestDto.getPageSize(), sort);
            Specification<TransRecord> specification = new Specification<TransRecord>() {
                @Override
                public Predicate toPredicate(Root<TransRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicates = new ArrayList<>();
                    if (queryTransRecordRequestDto.getStartDate() != null) {
                        predicates.add(cb.greaterThanOrEqualTo(root.get("createAt"), TimeUtil.string2Day(queryTransRecordRequestDto.getStartDate())));
                    }
                    if (queryTransRecordRequestDto.getEndDate() != null) {
                        predicates.add(cb.lessThanOrEqualTo(root.get("createAt"), TimeUtil.string2Day(queryTransRecordRequestDto.getEndDate())));
                    }
                    if (!predicates.isEmpty()) {
                        query.where(predicates.toArray(new Predicate[0]));
                    }
                    return null;
                }
            };
            Page<TransRecord> page = transRecordRepository.findAll(specification, pageable);
            for (TransRecord transRecord : page.getContent()) {
                QueryTransRecordResponseDto queryTransRecordResponseDto = new QueryTransRecordResponseDto();
                queryTransRecordResponseDto.setId(transRecord.getId());
                queryTransRecordResponseDto.setCreateAt(transRecord.getCreateAt());
                res.setData(queryTransRecordResponseDto);
            }
            res.setPageCount(page.getTotalPages());
            res.setCode(ResultCode.OK);
        } catch (Exception e) {
            res.setMessage(e.getMessage());
            res.setCode(ResultCode.OK);
            Log.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 查询调货详情
     *
     * @param queryTransSpecRequestDto
     * @return
     */
    @GetMapping(path = "/query/trans/spec")
    public QueryTransSpecResponseDto<TransSpec> queryTransSpec(QueryTransSpecRequestDto queryTransSpecRequestDto) {
        QueryTransSpecResponseDto<TransSpec> queryTransSpecResponseDto = new QueryTransSpecResponseDto<>();
        try {
            TransRecord transRecord = transRecordRepository.findOne(queryTransSpecRequestDto.getId());
            List<TransSpec> transSpecs = transSpecRepository.findAllBySerialNo(queryTransSpecRequestDto.getId());
            queryTransSpecResponseDto.setData(transSpecs);
            queryTransSpecResponseDto.setCost(transRecord.getCost());
            queryTransSpecResponseDto.setName(transRecord.getStore().getName());
            queryTransSpecResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            queryTransSpecResponseDto.setMessage(e.getMessage());
            queryTransSpecResponseDto.setCode(ResultCode.OK);
            Log.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
        }
        return queryTransSpecResponseDto;
    }
}
