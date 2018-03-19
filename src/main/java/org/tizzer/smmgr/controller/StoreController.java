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
import org.tizzer.smmgr.model.request.QueryAllStoreRequestDto;
import org.tizzer.smmgr.model.response.QueryAllStoreResponseDto;
import org.tizzer.smmgr.model.response.ResultListResponse;
import org.tizzer.smmgr.repository.StoreRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/smmgr")
public class StoreController {
    private final static Class clazz = StoreController.class;
    private final StoreRepository storeRepository;

    @Autowired
    public StoreController(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @PostMapping(path = "/queryallstore")
    public ResultListResponse<QueryAllStoreResponseDto> queryAllStore(QueryAllStoreRequestDto queryAllStoreRequestDto) {
        ResultListResponse<QueryAllStoreResponseDto> res = new ResultListResponse<>();
        try {
            Pageable pageable = new PageRequest(queryAllStoreRequestDto.getCurrentPage(), queryAllStoreRequestDto.getPageSize());
//            Specification<Store> specification = new Specification<Store>() {
//                @Override
//                public Predicate toPredicate(Root<Store> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                    List<Predicate> predicates = new ArrayList<>();
//                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + queryAllStoreRequestDto.getKeyword() + "%"));
//                    predicates.add(criteriaBuilder.like(root.get("address"), "%" + queryAllStoreRequestDto.getKeyword() + "%"));
//                    criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));
//                    return null;
//                }
//            };
            Page<Store> page = storeRepository.findAll((Specification<Store>) null, pageable);
            Long total = page.getTotalElements();
            for (Store store : page.getContent()) {
                QueryAllStoreResponseDto queryAllStoreResponseDto = new QueryAllStoreResponseDto();
                queryAllStoreResponseDto.setId(store.getId());
                queryAllStoreResponseDto.setName(store.getName());
                queryAllStoreResponseDto.setAddress(store.getAddress());
                res.setData(queryAllStoreResponseDto);
            }
            res.setPageCount(page.getTotalPages());
            res.setCurrentPage(queryAllStoreRequestDto.getCurrentPage());
            res.setTotal(total);
            res.setCode(ResultCode.OK);
        } catch (Exception e) {
            Logcat.type(clazz, e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
            res.setMessage(res.getMessage());
            res.setCode(ResultCode.ERROR);
        }
        return res;
    }

}
