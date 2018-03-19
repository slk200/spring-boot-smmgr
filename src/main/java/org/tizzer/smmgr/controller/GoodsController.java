package org.tizzer.smmgr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tizzer.smmgr.constant.ResultCode;
import org.tizzer.smmgr.entity.Goods;
import org.tizzer.smmgr.model.request.QueryAllGoodsRequestDto;
import org.tizzer.smmgr.model.response.QueryAllGoodsResponseDto;
import org.tizzer.smmgr.model.response.ResultListResponse;
import org.tizzer.smmgr.repository.GoodsRepository;

@RestController
@RequestMapping(path = "/smmgr")
public class GoodsController {

    private final GoodsRepository goodsRepository;

    @Autowired
    public GoodsController(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    @PostMapping(path = "/queryallgoods")
    public ResultListResponse<QueryAllGoodsResponseDto> getSatisfy(QueryAllGoodsRequestDto queryAllGoodsRequestDto) {
        ResultListResponse<QueryAllGoodsResponseDto> res = new ResultListResponse<>();
        try {
            Pageable pageable = new PageRequest(queryAllGoodsRequestDto.getCurrentPage(), queryAllGoodsRequestDto.getPageSize());
//            Specification<Goods> specification = new Specification<Goods>() {
//                @Override
//                public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                    List<Predicate> predicates = new ArrayList<>();
//                    predicates.add(criteriaBuilder.like(root.get("upc"), "%" + queryAllGoodsRequestDto.getKeyword() + "%"));
//                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + queryAllGoodsRequestDto.getKeyword() + "%"));
//                    criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));
//                    return null;
//                }
//            };
            Page<Goods> page = goodsRepository.findAll((Specification<Goods>) null, pageable);
            Long total = page.getTotalElements();
            for (Goods goods : page.getContent()) {
                QueryAllGoodsResponseDto queryAllGoodsResponseDto = new QueryAllGoodsResponseDto();
                queryAllGoodsResponseDto.setUpc(goods.getUpc());
                queryAllGoodsResponseDto.setName(goods.getName());
                queryAllGoodsResponseDto.setType(goods.getGoodsType().getName());
                queryAllGoodsResponseDto.setoPrice(goods.getjPrice());
                queryAllGoodsResponseDto.setsPrice(goods.getsPrice());
                queryAllGoodsResponseDto.setInventory(goods.getInventory());
                res.setData(queryAllGoodsResponseDto);
            }
            res.setCurrentPage(queryAllGoodsRequestDto.getCurrentPage());
            res.setPageCount(page.getTotalPages());
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