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
import org.springframework.web.servlet.support.RequestContext;
import org.tizzer.smmgr.common.Log;
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
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/smmgr")
public class InsiderController {

    private final InsiderRepository insiderRepository;
    private final InsiderTypeRepository insiderTypeRepository;

    @Autowired
    public InsiderController(InsiderRepository insiderRepository, InsiderTypeRepository insiderTypeRepository) {
        this.insiderRepository = insiderRepository;
        this.insiderTypeRepository = insiderTypeRepository;
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
            saveInsiderTypeResponseDto.setMessage(e.getMessage());
            saveInsiderTypeResponseDto.setCode(ResultCode.ERROR);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return saveInsiderTypeResponseDto;
    }

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
            queryAllInsiderTypeResponseDto.setMessage(e.getMessage());
            queryAllInsiderTypeResponseDto.setCode(ResultCode.ERROR);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return queryAllInsiderTypeResponseDto;
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
            updateInsiderTypeResponseDto.setMessage(e.getMessage());
            updateInsiderTypeResponseDto.setCode(ResultCode.ERROR);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return updateInsiderTypeResponseDto;
    }

    /**
     * 删除会员类型
     *
     * @param request
     * @param deleteInsiderTypeRequestDto
     * @return
     */
    @PostMapping(path = "/delete/insider/type")
    public DeleteInsiderTypeResponseDto deleteInsiderType(HttpServletRequest request, DeleteInsiderTypeRequestDto deleteInsiderTypeRequestDto) {
        RequestContext requestContext = new RequestContext(request);
        DeleteInsiderTypeResponseDto deleteInsiderTypeResponseDto = new DeleteInsiderTypeResponseDto();
        try {
            List<Integer> usingTypeIds = insiderRepository.findAllUsingType();
            for (Integer id : deleteInsiderTypeRequestDto.getId()) {
                if (usingTypeIds.contains(id)) {
                    deleteInsiderTypeResponseDto.setMessage(requestContext.getMessage("msg.delete.insider.type.result.error"));
                    deleteInsiderTypeResponseDto.setCode(ResultCode.ERROR);
                    return deleteInsiderTypeResponseDto;
                }
            }
            for (Integer id : deleteInsiderTypeRequestDto.getId()) {
                insiderTypeRepository.delete(id);
            }
            deleteInsiderTypeResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            deleteInsiderTypeResponseDto.setMessage(e.getMessage());
            deleteInsiderTypeResponseDto.setCode(ResultCode.ERROR);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return deleteInsiderTypeResponseDto;
    }

    /**
     * 保存会员
     *
     * @param saveInsiderRequestDto
     * @return
     */
    @PostMapping(path = "/save/insider")
    public SaveInsiderResponseDto saveInsider(HttpServletRequest request, SaveInsiderRequestDto saveInsiderRequestDto) {
        RequestContext requestContext = new RequestContext(request);
        SaveInsiderResponseDto saveInsiderResponseDto = new SaveInsiderResponseDto();
        try {
            Insider savedInsider = insiderRepository.findByPhone(saveInsiderRequestDto.getPhone());
            if (savedInsider != null) {
                saveInsiderResponseDto.setMessage(requestContext.getMessage("msg.save.insider.result.error"));
                saveInsiderResponseDto.setCode(ResultCode.ERROR);
            } else {
                Insider toSaveInsider = new Insider();
                toSaveInsider.setCardNo(saveInsiderRequestDto.getCardNo());
                toSaveInsider.setName(saveInsiderRequestDto.getName());
                toSaveInsider.setPhone(saveInsiderRequestDto.getPhone());
                toSaveInsider.setInsiderType(insiderTypeRepository.findOne(saveInsiderRequestDto.getType()));
                if (saveInsiderRequestDto.getAddress() != null || !Objects.equals(saveInsiderRequestDto.getAddress(), "")) {
                    toSaveInsider.setAddress(saveInsiderRequestDto.getAddress());
                }
                if (saveInsiderRequestDto.getNote() != null || !Objects.equals(saveInsiderRequestDto.getNote(), "")) {
                    toSaveInsider.setNote(saveInsiderRequestDto.getNote());
                }
                if (saveInsiderRequestDto.getBirth() != null) {
                    toSaveInsider.setBirth(TimeUtil.string2Date(saveInsiderRequestDto.getBirth()));
                }
                toSaveInsider.setCreateAt(new Date());
                insiderRepository.save(toSaveInsider);
                saveInsiderResponseDto.setCode(ResultCode.OK);
            }
        } catch (Exception e) {
            saveInsiderResponseDto.setMessage(e.getMessage());
            saveInsiderResponseDto.setCode(ResultCode.ERROR);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return saveInsiderResponseDto;
    }

    /**
     * 查询满足条件的所有会员
     *
     * @param querySomeInsiderRequestDto
     * @return
     */
    @GetMapping(path = "/query/insider")
    public ResultListResponse<QuerySomeInsiderResponseDto> querySomeInsider(QuerySomeInsiderRequestDto querySomeInsiderRequestDto) {
        ResultListResponse<QuerySomeInsiderResponseDto> res = new ResultListResponse<>();
        try {
            Pageable pageable = new PageRequest(querySomeInsiderRequestDto.getCurrentPage(), querySomeInsiderRequestDto.getPageSize());
            Specification<Insider> specification = new Specification<Insider>() {
                @Override
                public Predicate toPredicate(Root<Insider> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicates = new ArrayList<>();
                    if (querySomeInsiderRequestDto.getStartDate() != null) {
                        predicates.add(cb.greaterThanOrEqualTo(root.get("createAt"), TimeUtil.string2DateTime(querySomeInsiderRequestDto.getStartDate())));
                    }
                    if (querySomeInsiderRequestDto.getEndDate() != null) {
                        predicates.add(cb.lessThanOrEqualTo(root.get("createAt"), TimeUtil.string2DateTime(querySomeInsiderRequestDto.getEndDate())));
                    }
                    if (!querySomeInsiderRequestDto.getKeyword().equals("")) {
                        predicates.add(cb.or(cb.like(root.get("cardNo"), "%" + querySomeInsiderRequestDto.getKeyword() + "%"),
                                cb.like(root.get("name"), "%" + querySomeInsiderRequestDto.getKeyword() + "%"),
                                cb.like(root.get("phone"), "%" + querySomeInsiderRequestDto.getKeyword() + "%"),
                                cb.like(root.get("address"), "%" + querySomeInsiderRequestDto.getKeyword() + "%")));
                    }
                    if (!predicates.isEmpty()) {
                        query.where(cb.and(predicates.toArray(new Predicate[0])));
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
     * 查询某个会员
     *
     * @param queryOneInsiderRequestDto
     * @return
     */
    @GetMapping(path = "/query/insider/one")
    public QueryOneInsiderResponseDto queryOneInsider(HttpServletRequest request, QueryOneInsiderRequestDto queryOneInsiderRequestDto) {
        RequestContext requestContext = new RequestContext(request);
        QueryOneInsiderResponseDto queryOneInsiderResponseDto = new QueryOneInsiderResponseDto();
        try {
            Insider insider = insiderRepository.findByCardNoOrPhone(queryOneInsiderRequestDto.getKeyword(), queryOneInsiderRequestDto.getKeyword());
            if (insider != null) {
                queryOneInsiderResponseDto.setCardNo(insider.getCardNo());
                queryOneInsiderResponseDto.setName(insider.getName());
                queryOneInsiderResponseDto.setPhone(insider.getPhone());
                queryOneInsiderResponseDto.setAddress(insider.getAddress());
                queryOneInsiderResponseDto.setNote(insider.getNote());
                queryOneInsiderResponseDto.setType(insider.getInsiderType().getName());
                queryOneInsiderResponseDto.setDiscount(insider.getInsiderType().getDiscount());
                queryOneInsiderResponseDto.setBirth(insider.getBirth());
                queryOneInsiderResponseDto.setCreateAt(insider.getCreateAt());
                queryOneInsiderResponseDto.setCode(ResultCode.OK);
            } else {
                queryOneInsiderResponseDto.setMessage(requestContext.getMessage("msg.query.insider.result.none"));
                queryOneInsiderResponseDto.setCode(ResultCode.ERROR);
            }
        } catch (Exception e) {
            queryOneInsiderResponseDto.setMessage(e.getMessage());
            queryOneInsiderResponseDto.setCode(ResultCode.ERROR);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return queryOneInsiderResponseDto;
    }

    /**
     * 修改会员资料
     *
     * @param updateInsiderRequestDto
     * @return
     */
    @PostMapping(path = "/update/insider")
    public UpdateInsiderResponseDto updateInsider(UpdateInsiderRequestDto updateInsiderRequestDto) {
        UpdateInsiderResponseDto updateInsiderResponseDto = new UpdateInsiderResponseDto();
        try {
            insiderRepository.updateInsider(updateInsiderRequestDto.getCardNo(), updateInsiderRequestDto.getId(), updateInsiderRequestDto.getBirth());
            updateInsiderResponseDto.setCode(ResultCode.OK);
        } catch (Exception e) {
            updateInsiderResponseDto.setMessage(e.getMessage());
            updateInsiderResponseDto.setCode(ResultCode.ERROR);
            Log.type(getClass(), e.getMessage(), Log.LogLevel.ERROR);
            e.printStackTrace();
        }
        return updateInsiderResponseDto;
    }
}
