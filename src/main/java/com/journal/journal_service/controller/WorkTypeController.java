package com.journal.journal_service.controller;

import com.journal.journal_service.dto.WorkTypeDto;
import com.journal.journal_service.models.WorkType;
import com.journal.journal_service.services.WorkTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/work")
public class WorkTypeController {

    @Autowired
    WorkTypeService workTypeService;

    @RequestMapping("/getAllWorkType")
    public List<WorkType> getAllWorkType() throws Exception {
        try {
            return workTypeService.getAllWorkType();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    @PostMapping("/createWorkType")
    public ResponseEntity<String> createWorkType(@RequestBody WorkTypeDto workTypeDto) throws Exception {
        try {
            WorkType wt = workTypeService.createWorkType(workTypeDto);
            return ResponseEntity.ok("Created");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


}

