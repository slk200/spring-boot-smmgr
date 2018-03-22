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
import org.tizzer.smmgr.entity.Insider;
import org.tizzer.smmgr.entity.InsiderType;
import org.tizzer.smmgr.model.request.*;
import org.tizzer.smmgr.model.response.*;
import org.tizzer.smmgr.repository.InsiderRepository;
import org.tizzer.smmgr.repository.InsiderTypeRepository;
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
public class InsiderController {

    @Autowired
    InsiderRepository insiderRepository;

    @Autowired
    InsiderTypeRepository insiderTypeRepository;

    /**
     * 查询所有会员类型
     *
     * @return
     */
    @GetMapping(path = "/query/insider/type")
    public QueryAllInsiderTypeResponseDto<InsiderType> queryAllInsiderType() {
        QueryAllInsiderTypeResponseDto<InsiderType> queryAllInsiderTypeResponseDto = new QueryAllInsiderTypeResponseDto<>();
        try {
            List<InsiderType> insiderTypes = insiderTypeRepository.findAll();
            queryAllInsiderTypeResponseDto.setData(insiderTypes);
            queryAllInsiderTypeResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
            queryAllInsiderTypeResponseDto.setMessage(e.getMessage());
            queryAllInsiderTypeResponseDto.setCode(ResultCode.ERROR);
        }
        return queryAllInsiderTypeResponseDto;
    }

    /**
     * 保存会员类型
     *
     * @param saveInsiderTypeRequestDto
     * @return
     */
    @PostMapping(path = "/save/insider/type")
    public SaveInsiderTypeResponseDto saveInsiderType(SaveInsiderTypeRequestDto saveInsiderTypeRequestDto) {
        SaveInsiderTypeResponseDto saveInsiderTypeResponseDto = new SaveInsiderTypeResponseDto();
        try {
            InsiderType insiderType = new InsiderType();
            insiderType.setName(saveInsiderTypeRequestDto.getName());
            insiderType.setDiscount(saveInsiderTypeRequestDto.getDiscount());
            insiderTypeRepository.save(insiderType);
            saveInsiderTypeResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
            saveInsiderTypeResponseDto.setMessage(e.getMessage());
            saveInsiderTypeResponseDto.setCode(ResultCode.ERROR);
        }
        return saveInsiderTypeResponseDto;
    }

    /**
     * 修改会员类型信息
     *
     * @param updateInsiderTypeRequestDto
     * @return
     */
    @PostMapping(path = "/update/insider/type")
    public UpdateInsiderTypeResponseDto saveInsiderType(UpdateInsiderTypeRequestDto updateInsiderTypeRequestDto) {
        UpdateInsiderTypeResponseDto updateInsiderTypeResponseDto = new UpdateInsiderTypeResponseDto();
        try {
            InsiderType insiderType = new InsiderType();
            insiderType.setId(updateInsiderTypeRequestDto.getId());
            insiderType.setName(updateInsiderTypeRequestDto.getName());
            insiderType.setDiscount(updateInsiderTypeRequestDto.getDiscount());
            insiderTypeRepository.save(insiderType);
            updateInsiderTypeResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
            updateInsiderTypeResponseDto.setMessage(e.getMessage());
            updateInsiderTypeResponseDto.setCode(ResultCode.ERROR);
        }
        return updateInsiderTypeResponseDto;
    }

    /**
     * 查询所有会员
     *
     * @param queryAllInsiderRequestDto
     * @return
     */
    @PostMapping(path = "/query/insider/all")
    public ResultListResponse<QueryAllInsiderResponseDto> queryAllInsider(QueryAllInsiderRequestDto queryAllInsiderRequestDto) {
        ResultListResponse<QueryAllInsiderResponseDto> res = new ResultListResponse<>();
        try {
            Pageable pageable = new PageRequest(queryAllInsiderRequestDto.getCurrentPage(), queryAllInsiderRequestDto.getPageSize());
            Page<Insider> page = insiderRepository.findAll(pageable);
            for (Insider insider : page.getContent()) {
                QueryAllInsiderResponseDto queryAllInsiderResponseDto = new QueryAllInsiderResponseDto();
                queryAllInsiderResponseDto.setCardNo(insider.getCardNo());
                queryAllInsiderResponseDto.setName(insider.getName());
                queryAllInsiderResponseDto.setPhone(insider.getPhone());
                queryAllInsiderResponseDto.setAddress(insider.getAddress());
                queryAllInsiderResponseDto.setType(insider.getInsiderType().getName());
                queryAllInsiderResponseDto.setNote(insider.getNote());
                queryAllInsiderResponseDto.setBirth(insider.getBirth());
                queryAllInsiderResponseDto.setCreateAt(insider.getCreateAt());
                res.setData(queryAllInsiderResponseDto);
            }
            res.setPageCount(page.getTotalPages());
            res.setCurrentPage(queryAllInsiderRequestDto.getCurrentPage());
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
     * 查询满足条件的所有会员
     *
     * @param querySomeInsiderRequestDto
     * @return
     */
    @PostMapping(path = "/query/insider/some")
    public ResultListResponse<QuerySomeInsiderResponseDto> querySomeInsider(QuerySomeInsiderRequestDto querySomeInsiderRequestDto) {
        ResultListResponse<QuerySomeInsiderResponseDto> res = new ResultListResponse<>();
        try {
            Pageable pageable = new PageRequest(querySomeInsiderRequestDto.getCurrentPage(), querySomeInsiderRequestDto.getPageSize());
            Specification<Insider> specification = new Specification<Insider>() {
                @Override
                public Predicate toPredicate(Root<Insider> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicates = new ArrayList<>();
                    if (querySomeInsiderRequestDto.getStartDate() != null) {
                        predicates.add(cb.greaterThanOrEqualTo(root.get("createAt"), TimeUtil.startOfDay(querySomeInsiderRequestDto.getStartDate())));
                    }
                    if (querySomeInsiderRequestDto.getEndDate() != null) {
                        predicates.add(cb.lessThanOrEqualTo(root.get("createAt"), TimeUtil.endOfDay(querySomeInsiderRequestDto.getEndDate())));
                    }
                    if (!querySomeInsiderRequestDto.getKeyWord().equals("")) {
                        predicates.add(cb.or(cb.like(root.get("cardNo"), "%" + querySomeInsiderRequestDto.getKeyWord() + "%"),
                                cb.like(root.get("name"), "%" + querySomeInsiderRequestDto.getKeyWord() + "%"),
                                cb.like(root.get("phone"), "%" + querySomeInsiderRequestDto.getKeyWord() + "%"),
                                cb.like(root.get("address"), "%" + querySomeInsiderRequestDto.getKeyWord() + "%")));
                    }
                    if (!predicates.isEmpty()) {
                        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
                    }
                    return null;
                }
            };
            Page<Insider> page = insiderRepository.findAll(specification, pageable);
            for (Insider insider : page.getContent()) {
                QuerySomeInsiderResponseDto querySomeInsiderResponseDto = new QuerySomeInsiderResponseDto();
                querySomeInsiderResponseDto.setCardNo(insider.getCardNo());
                querySomeInsiderResponseDto.setName(insider.getName());
                querySomeInsiderResponseDto.setPhone(insider.getPhone());
                querySomeInsiderResponseDto.setAddress(insider.getAddress());
                querySomeInsiderResponseDto.setType(insider.getInsiderType().getName());
                querySomeInsiderResponseDto.setNote(insider.getNote());
                querySomeInsiderResponseDto.setBirth(insider.getBirth());
                querySomeInsiderResponseDto.setCreateAt(insider.getCreateAt());
                res.setData(querySomeInsiderResponseDto);
            }
            res.setPageCount(page.getTotalPages());
            res.setCurrentPage(querySomeInsiderRequestDto.getCurrentPage());
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
     * 保存会员
     *
     * @param saveInsiderRequestDto
     * @return
     */
    @PostMapping(path = "/save/insider")
    public SaveInsiderResponseDto saveInsider(SaveInsiderRequestDto saveInsiderRequestDto) {
        SaveInsiderResponseDto saveInsiderResponseDto = new SaveInsiderResponseDto();
        try {
            Insider savedInsider = insiderRepository.findByPhone(saveInsiderRequestDto.getPhone());
            if (savedInsider != null) {
                saveInsiderResponseDto.setMessage("手机号不能重复绑定会员卡");
                saveInsiderResponseDto.setCode(ResultCode.ERROR);
            } else {
                Insider toSaveInsider = new Insider();
                toSaveInsider.setCardNo(saveInsiderRequestDto.getCardNo());
                toSaveInsider.setName(saveInsiderRequestDto.getName());
                toSaveInsider.setPhone(saveInsiderRequestDto.getPhone());
                toSaveInsider.setInsiderType(insiderTypeRepository.findOne(saveInsiderRequestDto.getType()));
                if (saveInsiderRequestDto.getPassword() != null || !saveInsiderRequestDto.getPassword().equals("")) {
                    toSaveInsider.setPassword(saveInsiderRequestDto.getPassword());
                }
                if (saveInsiderRequestDto.getAddress() != null || !saveInsiderRequestDto.getAddress().equals("")) {
                    toSaveInsider.setAddress(saveInsiderRequestDto.getAddress());
                }
                if (saveInsiderRequestDto.getNote() != null || !saveInsiderRequestDto.getNote().equals("")) {
                    toSaveInsider.setNote(saveInsiderRequestDto.getNote());
                }
                if (saveInsiderRequestDto.getBirth() != null) {
                    toSaveInsider.setBirth(TimeUtil.startOfDay(saveInsiderRequestDto.getBirth()));
                }
                toSaveInsider.setCreateAt(new Date());
                insiderRepository.save(toSaveInsider);
                saveInsiderResponseDto.setCode(ResultCode.OK);
            }
        } catch (Exception e) {
            Logcat.type(getClass(), e.getMessage(), LogLevel.ERROR);
            e.printStackTrace();
            saveInsiderResponseDto.setMessage(e.getMessage());
            saveInsiderResponseDto.setCode(ResultCode.ERROR);
        }
        return saveInsiderResponseDto;
    }
}
