package org.tizzer.smmgr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import org.tizzer.smmgr.constant.ResultCode;
import org.tizzer.smmgr.entity.Goods;
import org.tizzer.smmgr.model.request.QueryGoodsRequestDto;
import org.tizzer.smmgr.model.response.QueryGoodsResponseDto;
import org.tizzer.smmgr.model.response.ResultListResponse;
import org.tizzer.smmgr.repository.GoodsRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/smmgr")
public class GoodsController {

    private final GoodsRepository goodsRepository;

    @Autowired
    public GoodsController(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    @PostMapping(path = "/queryGoods")
    public ResultListResponse<QueryGoodsResponseDto> getSatisfy(QueryGoodsRequestDto queryGoodsRequestDto) {
        ResultListResponse<QueryGoodsResponseDto> res = new ResultListResponse<>();
        try {
            Pageable pageable = new PageRequest(queryGoodsRequestDto.getCurrentPage(), queryGoodsRequestDto.getPageSize());
            Specification<Goods> spec = new Specification<Goods>() {
                @Override
                public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    predicates.add(criteriaBuilder.like(root.get("upc"), "%" + queryGoodsRequestDto.getKeyword() + "%"));
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + queryGoodsRequestDto.getKeyword() + "%"));
                    criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));
                    return null;
                }
            };
            Page<Goods> goodsPage = goodsRepository.findAll(spec, pageable);
            Long total = goodsPage.getTotalElements();
            for (Goods goods : goodsPage.getContent()) {
                QueryGoodsResponseDto queryGoodsResponseDto = new QueryGoodsResponseDto();
                queryGoodsResponseDto.setUpc(goods.getUpc());
                queryGoodsResponseDto.setName(goods.getName());
                queryGoodsResponseDto.setType(goods.getGoodsType().getName());
                queryGoodsResponseDto.setoPrice(goods.getjPrice());
                queryGoodsResponseDto.setsPrice(goods.getsPrice());
                queryGoodsResponseDto.setInventory(goods.getInventory());
                res.setData(queryGoodsResponseDto);
            }
            res.setCurrentPage(queryGoodsRequestDto.getCurrentPage());
            res.setPageCount(Math.toIntExact(total / queryGoodsRequestDto.getPageSize() + (total % queryGoodsRequestDto.getPageSize() > 0 ? 1 : 0)));
            res.setTotal(total);
            res.setCode(ResultCode.OK);
        } catch (Exception e) {
            res.setMessage(e.getMessage());
            res.setCode(ResultCode.ERROR);
            e.printStackTrace();
        }
        return res;
    }
}