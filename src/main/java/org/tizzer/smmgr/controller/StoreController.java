package org.tizzer.smmgr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tizzer.smmgr.common.Log;
import org.tizzer.smmgr.constant.ResultCode;
import org.tizzer.smmgr.entity.Store;
import org.tizzer.smmgr.model.request.*;
import org.tizzer.smmgr.model.response.*;
import org.tizzer.smmgr.repository.StoreRepository;
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
public class StoreController {

    private final StoreRepository storeRepository;

    @Autowired
    public StoreController(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    /**
     * 保存门店
     *
     * @param saveStoreRequestDto
     * @return
     */
    @PostMapping(path = "/save/store")
    public SaveStoreResponseDto saveStore(SaveStoreRequestDto saveStoreRequestDto) {
        SaveStoreResponseDto saveStoreResponseDto = new SaveStoreResponseDto();
        try {
            Store store = new Store();
            store.setName(saveStoreRequestDto.getName());
            store.setAddress(saveStoreRequestDto.getAddress());
            store.setFoundDate(new Date());
            storeRepository.save(store);
            saveStoreResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            saveStoreResponseDto.setMessage(e.getMessage());
            saveStoreResponseDto.setCode(ResultCode.ERROR);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return saveStoreResponseDto;
    }

    /**
     * 查询满足条件的所有门店
     *
     * @param queryStoreRequestDto
     * @return
     */
    @GetMapping(path = "/query/store")
    public ResultListResponse<QueryStoreResponseDto> querySomeStore(QueryStoreRequestDto queryStoreRequestDto) {
        ResultListResponse<QueryStoreResponseDto> res = new ResultListResponse<>();
        try {
            Pageable pageable = new PageRequest(queryStoreRequestDto.getCurrentPage(), queryStoreRequestDto.getPageSize());
            Specification<Store> storeSpecification = new Specification<Store>() {
                @Override
                public Predicate toPredicate(Root<Store> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicates = new ArrayList<>();
                    if (queryStoreRequestDto.getStartDate() != null) {
                        predicates.add(cb.greaterThanOrEqualTo(root.get("foundDate"), TimeUtil.string2Day(queryStoreRequestDto.getStartDate())));
                    }
                    if (queryStoreRequestDto.getEndDate() != null) {
                        predicates.add(cb.lessThanOrEqualTo(root.get("foundDate"), TimeUtil.string2Day(queryStoreRequestDto.getEndDate())));
                    }
                    if (!queryStoreRequestDto.getKeyword().equals("")) {
                        predicates.add(cb.or(cb.like(root.get("name"), "%" + queryStoreRequestDto.getKeyword() + "%"),
                                cb.like(root.get("address"), "%" + queryStoreRequestDto.getKeyword() + "%")));
                    }
                    if (!predicates.isEmpty()) {
                        query.where(cb.and(predicates.toArray(new Predicate[0])));
                    }
                    return null;
                }
            };
            Page<Store> page = storeRepository.findAll(storeSpecification, pageable);
            for (Store store : page.getContent()) {
                QueryStoreResponseDto queryStoreResponseDto = new QueryStoreResponseDto();
                queryStoreResponseDto.setId(store.getId());
                queryStoreResponseDto.setName(store.getName());
                queryStoreResponseDto.setAddress(store.getAddress());
                queryStoreResponseDto.setFoundDate(store.getFoundDate());
                res.setData(queryStoreResponseDto);
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
     * 查询除本店外的所有分店
     *
     * @param queryOtherStoreRequestDto
     * @return
     */
    @GetMapping(path = "/query/store/other")
    public QueryOtherStoreResponseDto<Store> queryOtherStore(QueryOtherStoreRequestDto queryOtherStoreRequestDto) {
        QueryOtherStoreResponseDto<Store> queryOtherStoreResponseDto = new QueryOtherStoreResponseDto<>();
        try {
            List<Store> stores = storeRepository.findAllByIdIsNot(queryOtherStoreRequestDto.getStoreId());
            queryOtherStoreResponseDto.setData(stores);
            queryOtherStoreResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            queryOtherStoreResponseDto.setMessage(e.getMessage());
            queryOtherStoreResponseDto.setCode(ResultCode.ERROR);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return queryOtherStoreResponseDto;
    }

    /**
     * 查询某个门店
     *
     * @param queryOneStoreRequestDto
     * @return
     */
    @GetMapping(path = "/query/store/one")
    public QueryOneStoreResponseDto queryOneStore(QueryOneStoreRequestDto queryOneStoreRequestDto) {
        QueryOneStoreResponseDto queryOneStoreResponseDto = new QueryOneStoreResponseDto();
        try {
            Store store = storeRepository.findOne(queryOneStoreRequestDto.getId());
            queryOneStoreResponseDto.setId(store.getId());
            queryOneStoreResponseDto.setName(store.getName());
            queryOneStoreResponseDto.setAddress(store.getAddress());
            queryOneStoreResponseDto.setFoundDate(store.getFoundDate());
            queryOneStoreResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            queryOneStoreResponseDto.setMessage(e.getMessage());
            queryOneStoreResponseDto.setCode(ResultCode.ERROR);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return queryOneStoreResponseDto;
    }

    /**
     * 修改门店资料
     *
     * @param updateStoreRequestDto
     * @return
     */
    @PostMapping(path = "/update/store")
    public UpdateStoreResponseDto updateStore(UpdateStoreRequestDto updateStoreRequestDto) {
        UpdateStoreResponseDto updateStoreResponseDto = new UpdateStoreResponseDto();
        try {
            storeRepository.updateStore(updateStoreRequestDto.getId(), updateStoreRequestDto.getName(), updateStoreRequestDto.getAddress());
            updateStoreResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            updateStoreResponseDto.setMessage(e.getMessage());
            updateStoreResponseDto.setCode(ResultCode.ERROR);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return updateStoreResponseDto;
    }

    /**
     * 删除门店
     *
     * @param deleteStoreRequestDto
     * @return
     */
    @Transactional
    @PostMapping(path = "/delete/store")
    public DeleteStoreResponseDto deleteStore(DeleteStoreRequestDto deleteStoreRequestDto) {
        DeleteStoreResponseDto deleteStoreResponseDto = new DeleteStoreResponseDto();
        try {
            for (Integer id : deleteStoreRequestDto.getId()) {
                storeRepository.delete(id);
            }
            deleteStoreResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            deleteStoreResponseDto.setMessage(e.getMessage());
            deleteStoreResponseDto.setCode(ResultCode.ERROR);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return deleteStoreResponseDto;
    }

}
