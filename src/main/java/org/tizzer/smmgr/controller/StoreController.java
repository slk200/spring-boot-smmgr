package org.tizzer.smmgr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tizzer.smmgr.common.LogLevel;
import org.tizzer.smmgr.common.Logcat;
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

    @Autowired
    StoreRepository storeRepository;

    /**
     * 查询全部门店
     *
     * @param queryAllStoreRequestDto
     * @return
     */
    @PostMapping(path = "/query/store/all")
    public ResultListResponse<QueryAllStoreResponseDto> queryAllStore(QueryAllStoreRequestDto queryAllStoreRequestDto) {
        ResultListResponse<QueryAllStoreResponseDto> res = new ResultListResponse<>();
        try {
            Pageable pageable = new PageRequest(queryAllStoreRequestDto.getCurrentPage(), queryAllStoreRequestDto.getPageSize());
            Page<Store> page = storeRepository.findAll(pageable);
            for (Store store : page.getContent()) {
                QueryAllStoreResponseDto queryAllStoreResponseDto = new QueryAllStoreResponseDto();
                queryAllStoreResponseDto.setId(store.getId());
                queryAllStoreResponseDto.setName(store.getName());
                queryAllStoreResponseDto.setAddress(store.getAddress());
                queryAllStoreResponseDto.setFoundDate(store.getFoundDate());
                res.setData(queryAllStoreResponseDto);
            }
            res.setPageCount(page.getTotalPages());
            res.setCurrentPage(queryAllStoreRequestDto.getCurrentPage());
            res.setCode(ResultCode.OK);
        } catch (Exception e) {
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
            res.setMessage(e.getMessage());
            res.setCode(ResultCode.ERROR);
        }
        return res;
    }

    /**
     * 查询满足条件的所有门店
     *
     * @param querySomeStoreRequestDto
     * @return
     */
    @PostMapping(path = "/query/store/some")
    public ResultListResponse<QuerySomeStoreResponseDto> querySomeStore(QuerySomeStoreRequestDto querySomeStoreRequestDto) {
        ResultListResponse<QuerySomeStoreResponseDto> res = new ResultListResponse<>();
        try {
            Pageable pageable = new PageRequest(querySomeStoreRequestDto.getCurrentPage(), querySomeStoreRequestDto.getPageSize());
            Specification<Store> storeSpecification = new Specification<Store>() {
                @Override
                public Predicate toPredicate(Root<Store> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicates = new ArrayList<>();
                    if (querySomeStoreRequestDto.getStartDate() != null) {
                        predicates.add(cb.greaterThanOrEqualTo(root.get("foundDate"), TimeUtil.startOfDay(querySomeStoreRequestDto.getStartDate())));
                    }
                    if (querySomeStoreRequestDto.getEndDate() != null) {
                        predicates.add(cb.lessThanOrEqualTo(root.get("foundDate"), TimeUtil.endOfDay(querySomeStoreRequestDto.getEndDate())));
                    }
                    if (!querySomeStoreRequestDto.getKeyWord().equals("")) {
                        predicates.add(cb.or(cb.like(root.get("name"), "%" + querySomeStoreRequestDto.getKeyWord() + "%"),
                                cb.like(root.get("address"), "%" + querySomeStoreRequestDto.getKeyWord() + "%")));
                    }
                    if (!predicates.isEmpty()) {
                        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
                    }
                    return null;
                }
            };
            Page<Store> page = storeRepository.findAll(storeSpecification, pageable);
            for (Store store : page.getContent()) {
                QuerySomeStoreResponseDto querySomeStoreResponseDto = new QuerySomeStoreResponseDto();
                querySomeStoreResponseDto.setId(store.getId());
                querySomeStoreResponseDto.setName(store.getName());
                querySomeStoreResponseDto.setAddress(store.getAddress());
                querySomeStoreResponseDto.setFoundDate(store.getFoundDate());
                res.setData(querySomeStoreResponseDto);
            }
            res.setPageCount(page.getTotalPages());
            res.setCurrentPage(querySomeStoreRequestDto.getCurrentPage());
            res.setCode(ResultCode.OK);
        } catch (Exception e) {
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
            res.setMessage(e.getMessage());
            res.setCode(ResultCode.ERROR);
        }
        return res;
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
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
            saveStoreResponseDto.setMessage(e.getMessage());
            saveStoreResponseDto.setCode(ResultCode.ERROR);
        }
        return saveStoreResponseDto;
    }

    /**
     * 查询某个门店
     *
     * @param queryOneStoreRequestDto
     * @return
     */
    @PostMapping(path = "/query/store/one")
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
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
            queryOneStoreResponseDto.setMessage(e.getMessage());
            queryOneStoreResponseDto.setCode(ResultCode.ERROR);
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
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
            updateStoreResponseDto.setMessage(e.getMessage());
            updateStoreResponseDto.setCode(ResultCode.ERROR);
        }
        return updateStoreResponseDto;
    }

    /**
     * 删除门店
     *
     * @param deleteStoreRequestDto
     * @return
     */
    @PostMapping(path = "/delete/store")
    public DeleteStoreResponseDto deleteStore(DeleteStoreRequestDto deleteStoreRequestDto) {
        DeleteStoreResponseDto deleteStoreResponseDto = new DeleteStoreResponseDto();
        try {
            for (Long id : deleteStoreRequestDto.getId()) {
                storeRepository.delete(id);
            }
            deleteStoreResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
            deleteStoreResponseDto.setMessage(e.getMessage());
            deleteStoreResponseDto.setCode(ResultCode.ERROR);
        }
        return deleteStoreResponseDto;
    }

}
