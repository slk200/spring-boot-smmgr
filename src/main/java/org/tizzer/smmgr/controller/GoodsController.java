package org.tizzer.smmgr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tizzer.smmgr.common.LogLevel;
import org.tizzer.smmgr.common.Logcat;
import org.tizzer.smmgr.constant.ResultCode;
import org.tizzer.smmgr.entity.Goods;
import org.tizzer.smmgr.entity.GoodsType;
import org.tizzer.smmgr.model.request.QueryAllGoodsRequestDto;
import org.tizzer.smmgr.model.request.QueryTradeGoodsRequestDto;
import org.tizzer.smmgr.model.request.SaveGoodsRequestDto;
import org.tizzer.smmgr.model.response.*;
import org.tizzer.smmgr.repository.GoodsRepository;
import org.tizzer.smmgr.repository.GoodsTypeRepository;

import java.util.List;

@RestController
@RequestMapping(path = "/smmgr")
public class GoodsController {

    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    GoodsTypeRepository goodsTypeRepository;

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
     * 查询全部商品
     *
     * @param queryAllGoodsRequestDto
     * @return
     */
    @PostMapping(path = "/queryallgoods")
    public ResultListResponse<QueryAllGoodsResponseDto> getSatisfy(QueryAllGoodsRequestDto queryAllGoodsRequestDto) {
        ResultListResponse<QueryAllGoodsResponseDto> res = new ResultListResponse<>();
        try {
            Pageable pageable = new PageRequest(queryAllGoodsRequestDto.getCurrentPage(), queryAllGoodsRequestDto.getPageSize());
            Page<Goods> page = goodsRepository.findAll(pageable);
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
     * 查询交易商品
     *
     * @param queryTradeGoodsRequestDto
     * @return
     */
    @PostMapping(path = "/query/goods/trade")
    public QueryTradeGoodsResponseDto<Goods> queryTradeGoods(QueryTradeGoodsRequestDto queryTradeGoodsRequestDto) {
        QueryTradeGoodsResponseDto<Goods> queryTradeGoodsResponseDto = new QueryTradeGoodsResponseDto<>();
        try {
            List<Goods> goods = goodsRepository.findAllByUpcLikeOrNameLikeOrSpellLike(queryTradeGoodsRequestDto.getKeyword(), queryTradeGoodsRequestDto.getKeyword(), queryTradeGoodsRequestDto.getKeyword());
            queryTradeGoodsResponseDto.setData(goods);
            queryTradeGoodsResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            queryTradeGoodsResponseDto.setMessage(e.getMessage());
            queryTradeGoodsResponseDto.setCode(ResultCode.ERROR);
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
        }
        return queryTradeGoodsResponseDto;
    }

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
            goods.setScDate(saveGoodsRequestDto.getScDate());
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
}