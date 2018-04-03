package org.tizzer.smmgr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tizzer.smmgr.common.LogLevel;
import org.tizzer.smmgr.common.Logcat;
import org.tizzer.smmgr.constant.ResultCode;
import org.tizzer.smmgr.entity.Goods;
import org.tizzer.smmgr.entity.GoodsType;
import org.tizzer.smmgr.model.request.QueryGoodsRequestDto;
import org.tizzer.smmgr.model.request.QueryOneGoodsRequestDto;
import org.tizzer.smmgr.model.request.QueryTradeGoodsRequestDto;
import org.tizzer.smmgr.model.request.SaveGoodsRequestDto;
import org.tizzer.smmgr.model.response.*;
import org.tizzer.smmgr.repository.GoodsRepository;
import org.tizzer.smmgr.repository.GoodsTypeRepository;
import org.tizzer.smmgr.utils.TimeUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/smmgr")
public class GoodsController {

    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    GoodsTypeRepository goodsTypeRepository;

    /**
     * 保存商品
     *
     * @param saveGoodsRequestDto
     * @return
     */
    @PostMapping(path = "/save/goods")
    public SaveGoodsResponseDto saveGoods(SaveGoodsRequestDto saveGoodsRequestDto) {
        SaveGoodsResponseDto saveGoodsResponseDto = new SaveGoodsResponseDto();
        try {
            GoodsType goodsType = new GoodsType();
            if (saveGoodsRequestDto.getId() == -1) {
                goodsType.setName(saveGoodsRequestDto.getName());
                goodsType = goodsTypeRepository.save(goodsType);
            } else {
                goodsType.setId(saveGoodsRequestDto.getId());
                goodsType.setName(saveGoodsRequestDto.getName());
            }
            Goods goods = new Goods();
            goods.setUpc(saveGoodsRequestDto.getUpc());
            goods.setName(saveGoodsRequestDto.getName());
            goods.setSpell(saveGoodsRequestDto.getSpell());
            goods.setGoodsType(goodsType);
            goods.setInventory(saveGoodsRequestDto.getInvention());
            goods.setjPrice(saveGoodsRequestDto.getjPrice());
            goods.setsPrice(saveGoodsRequestDto.getsPrice());
            goods.setScDate(TimeUtil.string2Day(saveGoodsRequestDto.getScDate()));
            goods.setBzDate(saveGoodsRequestDto.getBzDate());
            goodsRepository.save(goods);
            saveGoodsResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            saveGoodsResponseDto.setMessage(e.getMessage());
            saveGoodsResponseDto.setCode(ResultCode.ERROR);
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
        }
        return saveGoodsResponseDto;
    }

    /**
     * 查询所有商品类型
     *
     * @return
     */
    @GetMapping(path = "/query/goods/type")
    public QueryAllGoodsTypeResponseDto<GoodsType> queryAllGoodsType() {
        QueryAllGoodsTypeResponseDto<GoodsType> queryAllGoodsTypeResponseDto = new QueryAllGoodsTypeResponseDto<>();
        try {
            List<GoodsType> goodsTypes = goodsTypeRepository.findAll();
            queryAllGoodsTypeResponseDto.setData(goodsTypes);
            queryAllGoodsTypeResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            queryAllGoodsTypeResponseDto.setMessage(e.getMessage());
            queryAllGoodsTypeResponseDto.setCode(ResultCode.ERROR);
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
        }
        return queryAllGoodsTypeResponseDto;
    }

    /**
     * 查询满足条件的所有商品
     *
     * @param queryGoodsRequestDto
     * @return
     */
    @GetMapping(path = "/query/goods")
    public ResultListResponse<QueryGoodsResponseDto> getSatisfy(QueryGoodsRequestDto queryGoodsRequestDto) {
        ResultListResponse<QueryGoodsResponseDto> res = new ResultListResponse<>();
        try {
            Pageable pageable = new PageRequest(queryGoodsRequestDto.getCurrentPage(), queryGoodsRequestDto.getPageSize());
            Specification<Goods> specification = new Specification<Goods>() {
                @Override
                public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicates = new ArrayList<>();
                    predicates.add(cb.equal(root.get("goodsType"), queryGoodsRequestDto.getTypeId()));
                    if (!queryGoodsRequestDto.getKeyword().equals("")) {
                        predicates.add(cb.or(cb.like(root.get("upc"), "%" + queryGoodsRequestDto.getKeyword() + "%"),
                                cb.like(root.get("name"), "%" + queryGoodsRequestDto.getKeyword() + "%"),
                                cb.like(root.get("spell"), "%" + queryGoodsRequestDto.getKeyword() + "%")));
                    }
                    query.where(cb.and(predicates.toArray(new Predicate[0])));
                    return null;
                }
            };
            Page<Goods> page = goodsRepository.findAll(specification, pageable);
            for (Goods goods : page.getContent()) {
                QueryGoodsResponseDto queryGoodsResponseDto = new QueryGoodsResponseDto();
                queryGoodsResponseDto.setUpc(goods.getUpc());
                queryGoodsResponseDto.setName(goods.getName());
                queryGoodsResponseDto.setjPrice(goods.getjPrice());
                queryGoodsResponseDto.setsPrice(goods.getsPrice());
                queryGoodsResponseDto.setInventory(goods.getInventory());
                res.setData(queryGoodsResponseDto);
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
     * 根据条码查询商品
     *
     * @param queryOneGoodsRequestDto
     * @return
     */
    @GetMapping(path = "/query/goods/one")
    public QueryOneGoodsResponseDto queryOneGoods(QueryOneGoodsRequestDto queryOneGoodsRequestDto) {
        QueryOneGoodsResponseDto queryOneGoodsResponseDto = new QueryOneGoodsResponseDto();
        try {
            Goods goods = goodsRepository.findOne(queryOneGoodsRequestDto.getUpc());
            queryOneGoodsResponseDto.setUpc(goods.getUpc());
            queryOneGoodsResponseDto.setName(goods.getName());
            queryOneGoodsResponseDto.setSpell(goods.getSpell());
            queryOneGoodsResponseDto.setjPrice(goods.getjPrice());
            queryOneGoodsResponseDto.setsPrice(goods.getsPrice());
            queryOneGoodsResponseDto.setScDate(goods.getScDate());
            queryOneGoodsResponseDto.setBzDate(goods.getBzDate());
            queryOneGoodsResponseDto.setInventory(goods.getInventory());
            queryOneGoodsResponseDto.setType(goods.getGoodsType().getName());
            queryOneGoodsResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            queryOneGoodsResponseDto.setMessage(e.getMessage());
            queryOneGoodsResponseDto.setCode(ResultCode.ERROR);
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
        }
        return queryOneGoodsResponseDto;
    }

    /**
     * 查询交易商品
     *
     * @param queryTradeGoodsRequestDto
     * @return
     */
    @GetMapping(path = "/query/trade/goods")
    public QueryTradeGoodsResponseDto<Goods> queryTradeGoods(QueryTradeGoodsRequestDto queryTradeGoodsRequestDto) {
        QueryTradeGoodsResponseDto<Goods> queryTradeGoodsResponseDto = new QueryTradeGoodsResponseDto<>();
        try {
            List<Goods> goods = goodsRepository.findAllByUpcLikeOrNameLikeOrSpellLike(queryTradeGoodsRequestDto.getKeyword(), queryTradeGoodsRequestDto.getKeyword(), queryTradeGoodsRequestDto.getKeyword());
            if (!goods.isEmpty()) {
                queryTradeGoodsResponseDto.setData(goods);
                queryTradeGoodsResponseDto.setCode(ResultCode.OK);
            } else {
                queryTradeGoodsResponseDto.setCode(ResultCode.ERROR);
            }
        } catch (Exception e) {
            queryTradeGoodsResponseDto.setMessage(e.getMessage());
            queryTradeGoodsResponseDto.setCode(ResultCode.ERROR);
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
        }
        return queryTradeGoodsResponseDto;
    }
}