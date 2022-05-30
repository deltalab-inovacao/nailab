/*
 *
 * ****************************************************************************
 *  * Copyright (C) 2019 Testsigma Technologies Inc.
 *  * All rights reserved.
 *  ****************************************************************************
 *
 */

package com.testsigma.controller.api.agent;

import java.util.List;

import com.testsigma.dto.TestCaseDTO;
import com.testsigma.dto.TestCaseEntityDTO;
import com.testsigma.dto.TestStepDTO;
import com.testsigma.mapper.TestCaseMapper;
import com.testsigma.mapper.TestStepMapper;
import com.testsigma.model.TestCase;
import com.testsigma.service.TestCaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;



@RestController(value = "agentAPIsTestCaseController")
@RequestMapping(path = "/api/agents/test_case")
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestCasesController {

  private final TestCaseService testCaseService;
  private final TestCaseMapper testCaseMapper;
  private final TestStepMapper testStepMapper;

  @RequestMapping(method = RequestMethod.GET)
  public List<TestCaseDTO> findAll( @PageableDefault(value = 100, page = 0) Pageable pageable) throws Exception {
    return testCaseService.findAll(pageable);
  }

  @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public TestCaseEntityDTO find(@PathVariable("id") Long id,
                                @RequestParam(value = "testDataSetName", required = false) String testDataSetName,
                                @RequestParam("testCaseResultId") Long testCaseResultId,
                                @RequestParam("environmentResultId") Long environmentResultId
  ) throws Exception {
    return testCaseService.find(id, environmentResultId, testDataSetName, testCaseResultId);
  }
  @RequestMapping(path = "/{id}/detail", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public TestCaseEntityDTO find(@PathVariable("id") Long id) throws Exception {
    TestCase detailedFind = testCaseService.detailedFind(id);
    TestCaseEntityDTO map = testCaseMapper.map(detailedFind);
    detailedFind.getTestSteps().forEach(testStep -> {
      //TestCaseStepEntityDTO testStepDTO = testStepMapper.mapEntity(testStep);
      map.getTestSteps().add(testStepMapper.mapEntity(testStep));
    });
    //List<TestStepDTO> stepsDtos = testStepMapper.mapDTOs(detailedFind.getTestSteps());
    
    return map;
  }
  
 
}
