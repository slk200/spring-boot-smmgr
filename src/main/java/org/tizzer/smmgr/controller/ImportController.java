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
import org.tizzer.smmgr.common.Log;
import org.tizzer.smmgr.constant.ResultCode;
import org.tizzer.smmgr.entity.ImportRecord;
import org.tizzer.smmgr.entity.ImportSpec;
import org.tizzer.smmgr.model.request.QueryImportRecordRequestDto;
import org.tizzer.smmgr.model.request.QueryImportSpecRequestDto;
import org.tizzer.smmgr.model.request.SaveImportRecordRequestDto;
import org.tizzer.smmgr.model.response.QueryImportRecordResponseDto;
import org.tizzer.smmgr.model.response.QueryImportSpecResponseDto;
import org.tizzer.smmgr.model.response.ResultListResponse;
import org.tizzer.smmgr.model.response.SaveImportRecordResponseDto;
import org.tizzer.smmgr.repository.ImportRecordRepository;
import org.tizzer.smmgr.repository.ImportSpecRepository;
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
public class ImportController {

    private final ImportRecordRepository importRecordRepository;
    private final ImportSpecRepository importSpecRepository;

    @Autowired
    public ImportController(ImportRecordRepository importRecordRepository, ImportSpecRepository importSpecRepository) {
        this.importRecordRepository = importRecordRepository;
        this.importSpecRepository = importSpecRepository;
    }

    /**
     * 保存进货记录
     *
     * @param saveImportRecordRequestDto
     * @return
     */
    @Transactional
    @PostMapping(path = "/save/import/record")
    public SaveImportRecordResponseDto saveImportRecord(SaveImportRecordRequestDto saveImportRecordRequestDto) {
        SaveImportRecordResponseDto saveImportRecordResponseDto = new SaveImportRecordResponseDto();
        try {
            ImportRecord importRecord = new ImportRecord();
            importRecord.setCost(saveImportRecordRequestDto.getCost());
            importRecord.setNote(saveImportRecordRequestDto.getNote());
            importRecord.setCreateAt(new Date());
            importRecord = importRecordRepository.save(importRecord);
            ImportSpec importSpec;
            int length = saveImportRecordRequestDto.getUpc().length;
            for (int i = 0; i < length; i++) {
                importSpec = new ImportSpec();
                importSpec.setUpc(saveImportRecordRequestDto.getUpc()[i]);
                importSpec.setName(saveImportRecordRequestDto.getName()[i]);
                importSpec.setPrimeCost(saveImportRecordRequestDto.getPrimeCost()[i]);
                importSpec.setQuantity(saveImportRecordRequestDto.getQuantity()[i]);
                importSpec.setSerialNo(importRecord.getId());
                importSpecRepository.save(importSpec);
            }
            saveImportRecordResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            saveImportRecordResponseDto.setMessage(e.getMessage());
            saveImportRecordResponseDto.setCode(ResultCode.OK);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return saveImportRecordResponseDto;
    }

    /**
     * 查询进货记录
     *
     * @param queryImportRecordRequestDto
     * @return
     */
    @GetMapping(path = "/query/import/record")
    public ResultListResponse<QueryImportRecordResponseDto> queryImportRecord(QueryImportRecordRequestDto queryImportRecordRequestDto) {
        ResultListResponse<QueryImportRecordResponseDto> res = new ResultListResponse<>();
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "createAt");
            Pageable pageable = new PageRequest(queryImportRecordRequestDto.getCurrentPage(), queryImportRecordRequestDto.getPageSize(), sort);
            Specification<ImportRecord> specification = new Specification<ImportRecord>() {
                @Override
                public Predicate toPredicate(Root<ImportRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicates = new ArrayList<>();
                    if (queryImportRecordRequestDto.getStartDate() != null) {
                        predicates.add(cb.greaterThanOrEqualTo(root.get("createAt"), TimeUtil.string2Day(queryImportRecordRequestDto.getStartDate())));
                    }
                    if (queryImportRecordRequestDto.getEndDate() != null) {
                        predicates.add(cb.lessThanOrEqualTo(root.get("createAt"), TimeUtil.string2Day(queryImportRecordRequestDto.getEndDate())));
                    }
                    if (!predicates.isEmpty()) {
                        query.where(predicates.toArray(new Predicate[0]));
                    }
                    return null;
                }
            };
            Page<ImportRecord> page = importRecordRepository.findAll(specification, pageable);
            for (ImportRecord importRecord : page.getContent()) {
                QueryImportRecordResponseDto queryImportRecordResponseDto = new QueryImportRecordResponseDto();
                queryImportRecordResponseDto.setId(importRecord.getId());
                queryImportRecordResponseDto.setCreateAt(importRecord.getCreateAt());
                res.setData(queryImportRecordResponseDto);
            }
            res.setPageCount(page.getTotalPages());
            res.setCode(ResultCode.OK);
        } catch (Exception e) {
            res.setMessage(e.getMessage());
            res.setCode(ResultCode.OK);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 查询进货详情
     *
     * @param queryImportSpecRequestDto
     * @return
     */
    @GetMapping(path = "/query/import/spec")
    public QueryImportSpecResponseDto<ImportSpec> queryImportSpec(QueryImportSpecRequestDto queryImportSpecRequestDto) {
        QueryImportSpecResponseDto<ImportSpec> queryImportSpecResponseDto = new QueryImportSpecResponseDto<>();
        try {
            ImportRecord transRecord = importRecordRepository.findOne(queryImportSpecRequestDto.getId());
            List<ImportSpec> transSpecs = importSpecRepository.findAllBySerialNo(queryImportSpecRequestDto.getId());
            queryImportSpecResponseDto.setData(transSpecs);
            queryImportSpecResponseDto.setCost(transRecord.getCost());
            queryImportSpecResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            queryImportSpecResponseDto.setMessage(e.getMessage());
            queryImportSpecResponseDto.setCode(ResultCode.OK);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return queryImportSpecResponseDto;
    }
}
