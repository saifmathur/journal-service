package com.journal.journal_service.services.implementation;

import com.journal.journal_service.dto.WorkTypeDto;
import com.journal.journal_service.models.WorkType;
import com.journal.journal_service.repository.WorkTypeRepo;
import com.journal.journal_service.services.WorkTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkTypeServiceImpl implements WorkTypeService {

    @Autowired
    WorkTypeRepo workTypeRepo;

    @Override
    public List<WorkType> getAllWorkType() throws Exception {
        try {
            return workTypeRepo.findAll();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public WorkType createWorkType(WorkTypeDto workTypeDto) throws Exception {
        try{
            System.out.println(workTypeDto);
            WorkType wt = new WorkType();
            //wt.setWorkType(workTypeDto.getWorkType());
            //wt.setUserId(workTypeDto.getUserId());
            wt = workTypeRepo.saveAndFlush(wt);
            return wt;

        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
