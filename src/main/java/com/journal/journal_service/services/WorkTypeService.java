package com.journal.journal_service.services;


import com.journal.journal_service.dto.WorkTypeDto;
import com.journal.journal_service.models.WorkType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;


public interface WorkTypeService {

    List<WorkType> getAllWorkType() throws Exception;

    WorkType createWorkType(WorkTypeDto workTypeDto) throws Exception;
}
