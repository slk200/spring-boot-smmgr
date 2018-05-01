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
import org.tizzer.smmgr.entity.BookRecord;
import org.tizzer.smmgr.entity.BookSpec;
import org.tizzer.smmgr.model.request.QueryBookRecordRequestDto;
import org.tizzer.smmgr.model.request.QueryBookSpecRequestDto;
import org.tizzer.smmgr.model.request.SaveBookRecordRequestDto;
import org.tizzer.smmgr.model.response.QueryBookRecordResponseDto;
import org.tizzer.smmgr.model.response.QueryBookSpecResponseDto;
import org.tizzer.smmgr.model.response.ResultListResponse;
import org.tizzer.smmgr.model.response.SaveBookRecordResponseDto;
import org.tizzer.smmgr.repository.BookRecordRepository;
import org.tizzer.smmgr.repository.BookSpecRepository;
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
public class BookController {

    private final BookRecordRepository bookRecordRepository;
    private final BookSpecRepository bookSpecRepository;

    @Autowired
    public BookController(BookRecordRepository bookRecordRepository, BookSpecRepository bookSpecRepository) {
        this.bookRecordRepository = bookRecordRepository;
        this.bookSpecRepository = bookSpecRepository;
    }

    /**
     * 保存订货记录
     *
     * @param saveBookRecordRequestDto
     * @return
     */
    @Transactional
    @PostMapping(path = "/save/book/record")
    public SaveBookRecordResponseDto saveBookRecord(SaveBookRecordRequestDto saveBookRecordRequestDto) {
        SaveBookRecordResponseDto saveBookRecordResponseDto = new SaveBookRecordResponseDto();
        try {
            BookRecord bookRecord = new BookRecord();
            bookRecord.setCost(saveBookRecordRequestDto.getCost());
            bookRecord.setNote(saveBookRecordRequestDto.getNote());
            bookRecord.setCreateAt(new Date());
            bookRecord = bookRecordRepository.save(bookRecord);
            BookSpec bookSpec;
            int length = saveBookRecordRequestDto.getUpc().length;
            for (int i = 0; i < length; i++) {
                bookSpec = new BookSpec();
                bookSpec.setUpc(saveBookRecordRequestDto.getUpc()[i]);
                bookSpec.setName(saveBookRecordRequestDto.getName()[i]);
                bookSpec.setPrimeCost(saveBookRecordRequestDto.getPrimeCost()[i]);
                bookSpec.setQuantity(saveBookRecordRequestDto.getQuantity()[i]);
                bookSpec.setSerialNo(bookRecord.getId());
                bookSpecRepository.save(bookSpec);
            }
            saveBookRecordResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            saveBookRecordResponseDto.setMessage(e.getMessage());
            saveBookRecordResponseDto.setCode(ResultCode.OK);
            Log.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
        }
        return saveBookRecordResponseDto;
    }

    /**
     * 查询订货记录
     *
     * @param queryBookRecordRequestDto
     * @return
     */
    @GetMapping(path = "/query/book/record")
    public ResultListResponse<QueryBookRecordResponseDto> queryBookRecord(QueryBookRecordRequestDto queryBookRecordRequestDto) {
        ResultListResponse<QueryBookRecordResponseDto> res = new ResultListResponse<>();
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "createAt");
            Pageable pageable = new PageRequest(queryBookRecordRequestDto.getCurrentPage(), queryBookRecordRequestDto.getPageSize(), sort);
            Specification<BookRecord> specification = new Specification<BookRecord>() {
                @Override
                public Predicate toPredicate(Root<BookRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicates = new ArrayList<>();
                    if (queryBookRecordRequestDto.getStartDate() != null) {
                        predicates.add(cb.greaterThanOrEqualTo(root.get("createAt"), TimeUtil.string2Day(queryBookRecordRequestDto.getStartDate())));
                    }
                    if (queryBookRecordRequestDto.getEndDate() != null) {
                        predicates.add(cb.lessThanOrEqualTo(root.get("createAt"), TimeUtil.string2Day(queryBookRecordRequestDto.getEndDate())));
                    }
                    if (!predicates.isEmpty()) {
                        query.where(predicates.toArray(new Predicate[0]));
                    }
                    return null;
                }
            };
            Page<BookRecord> page = bookRecordRepository.findAll(specification, pageable);
            for (BookRecord bookRecord : page.getContent()) {
                QueryBookRecordResponseDto queryBookRecordResponseDto = new QueryBookRecordResponseDto();
                queryBookRecordResponseDto.setId(bookRecord.getId());
                queryBookRecordResponseDto.setCreateAt(bookRecord.getCreateAt());
                res.setData(queryBookRecordResponseDto);
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
     * 查询订单详情
     *
     * @param queryBookSpecRequestDto
     * @return
     */
    @GetMapping(path = "/query/book/spec")
    public QueryBookSpecResponseDto<BookSpec> queryBookRecord(QueryBookSpecRequestDto queryBookSpecRequestDto) {
        QueryBookSpecResponseDto<BookSpec> queryBookSpecResponseDto = new QueryBookSpecResponseDto<>();
        try {
            BookRecord bookRecord = bookRecordRepository.findOne(queryBookSpecRequestDto.getId());
            List<BookSpec> bookSpecs = bookSpecRepository.findAllBySerialNo(queryBookSpecRequestDto.getId());
            queryBookSpecResponseDto.setCost(bookRecord.getCost());
            queryBookSpecResponseDto.setNote(bookRecord.getNote());
            queryBookSpecResponseDto.setData(bookSpecs);
            queryBookSpecResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            queryBookSpecResponseDto.setMessage(e.getMessage());
            queryBookSpecResponseDto.setCode(ResultCode.OK);
            Log.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
        }
        return queryBookSpecResponseDto;
    }

}
