package org.tizzer.smmgr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tizzer.smmgr.repository.InsiderTypeRepository;

@Service
public class InsiderTypeService {

    @Autowired
    InsiderTypeRepository insiderTypeRepository;

    @Transactional
    public void deleteInsiderType(int[] ids) {
        for (Integer id : ids) {
            insiderTypeRepository.delete(id);
        }
    }
}
