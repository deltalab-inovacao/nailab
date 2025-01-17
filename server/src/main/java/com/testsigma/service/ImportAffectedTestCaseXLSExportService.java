package com.testsigma.service;



import com.testsigma.config.StorageServiceFactory;
import com.testsigma.dto.BackupDTO;
import com.testsigma.exception.ResourceNotFoundException;
import com.testsigma.exception.TestsigmaException;
import com.testsigma.model.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
@Log4j2
@RequiredArgsConstructor(onConstructor = @__({@Autowired, @Lazy}))
public class ImportAffectedTestCaseXLSExportService {
  private final WorkspaceVersionService versionService;
  private final TestCaseService testCaseService;
  private final StorageServiceFactory storageServiceFactory;
  @Setter
  @Getter
  private HashMap<TestStep, String> stepsMap= new HashMap<>();

  public XSSFWorkbook initializeWorkBook(){
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("affected_test_cases");
    Row row = sheet.createRow(0);
    row.createCell(0).setCellValue("S.No");
    row.createCell(1).setCellValue("Testcase ID");
    row.createCell(2).setCellValue("Testcase Name");
    row.createCell(3).setCellValue("Application Name");
    row.createCell(4).setCellValue("Step Number");
    row.createCell(5).setCellValue("Action Text");
    row.createCell(6).setCellValue("Reason");
    return workbook;
  }

  public void addToExcelSheet(HashMap<TestStep, String> testSteps, XSSFWorkbook workbook, BackupDTO importDTO, int rowNum) throws ResourceNotFoundException {
    for (Map.Entry<TestStep, String> entry : testSteps.entrySet()) {
      TestStep step = entry.getKey();
      TestCase testCase = testCaseService.find(step.getTestCaseId());
      WorkspaceVersion version = versionService.find(testCase.getWorkspaceVersionId());
      Row row = workbook.getSheetAt(0).createRow(rowNum);
      row.createCell(0).setCellValue(rowNum);
      row.createCell(1).setCellValue(testCase.getId().toString());
      row.createCell(2).setCellValue(testCase.getName());
      row.createCell(3).setCellValue(version.getWorkspace().getName() + "-" + version.getVersionName());
      row.createCell(4).setCellValue(String.valueOf(step.getPosition().intValue()+1));
      row.createCell(5).setCellValue(step.getAction());
      row.createCell(6).setCellValue(entry.getValue());
      ++rowNum;
    }
    try {
      this.uploadImportFileToStorage(workbook, importDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public void uploadImportFileToStorage(Workbook workbook, BackupDTO importDTO) throws TestsigmaException {
    String fileName = System.currentTimeMillis() + ".xls";
    String filePath = "/export_xlsx/" + importDTO.getId() + File.separator + fileName;
    log.info(String.format("Uploading affected Testcase import file to storage path %s", filePath));
    ByteArrayOutputStream bos;
    try {
      bos = new ByteArrayOutputStream();
      workbook.write(bos);
      byte[] bArray = bos.toByteArray();
      InputStream is = new ByteArrayInputStream(bArray);
      storageServiceFactory.getStorageService().addFile(filePath, is);
      importDTO.setAffectedCasesListPath(filePath);
      log.info("Import Affected test cases XLS sheet uploaded!");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}
