package com.zxtx.hummer.common.utils;

import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.exception.BizError;
import com.zxtx.hummer.common.exception.BusinessException;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 导入工具类
 */
public class ExcelImportUtil {

    private static Logger logger = LoggerFactory.getLogger(ExcelImportUtil.class);

    public static void main(String[] args) {

    }


    public static <T> List<T> importFromExcel(String fileName, InputStream is, int colSize, Class<T> c) throws Exception {

        List<T> lsRet = new ArrayList();
        try {
            if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
                throw new BusinessException(BizError.IMPORT_FILE_PATTERN_ERRO);
            }
            boolean isExcel2003 = true;
            if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
                isExcel2003 = false;
            }
            Workbook wb = null;
            if (isExcel2003) {
                wb = new HSSFWorkbook(is);
            } else {
                wb = new XSSFWorkbook(is);
            }
            Sheet sheet = wb.getSheetAt(0);
            if (sheet != null) {
                Row rh = sheet.getRow(0);
                Map<String, Integer> mapColNameToPos = new HashMap<>();
                for (int i = 0; i < colSize; i++) {
                    Cell cell = rh.getCell(i);
                    if (cell != null) {
                        String colName = cell.getStringCellValue();
                        if (StringUtils.isNotBlank(colName)) {
                            mapColNameToPos.put(colName, i);
                        }
                    }
                }
                List<Field> lsFields = getFields(c);
                for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) {
                        continue;
                    }
                    T dataItem = c.newInstance();
                    for (Field field : lsFields) {
                        ExcelField excelField = field.getAnnotation(ExcelField.class);
                        String colName = excelField.name();
                        Integer colPos = mapColNameToPos.get(colName);
                        if (colPos != null) {
                            Cell cell = row.getCell(colPos);
                            if (cell != null) {
                                String cellVal = cell.getStringCellValue();
                                Class typeClass = field.getType();
                                Constructor con = typeClass.getConstructor(cellVal.getClass());
                                field.setAccessible(true);
                                field.set(dataItem, con.newInstance(cellVal));
                            }
                        }
                    }
                    lsRet.add(dataItem);
                }
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return lsRet;
    }

    public static <T> List<T> importFromExcel(InputStream is, Class<T> c) throws Exception {
        is = new PushbackInputStream(is, 8);
        List<T> lsRet = new ArrayList<>();
        Workbook wb;
        if (POIFSFileSystem.hasPOIFSHeader(is)) {
            wb = new HSSFWorkbook(is);
        } else if (POIXMLDocument.hasOOXMLHeader(is)) {
            wb = new XSSFWorkbook(is);
        } else {
            throw new BusinessException(BizError.IMPORT_FILE_PATTERN_ERRO);
        }
        Sheet sheet = wb.getSheetAt(0);
        if (sheet != null) {
            Row rh = sheet.getRow(0);
            Map<String, Integer> mapColNameToPos = new HashMap<>();
            for (int i = 0; i < rh.getLastCellNum(); i++) {
                Cell cell = rh.getCell(i);
                if (cell != null) {
                    String colName = cell.getStringCellValue();
                    if (StringUtils.isNotBlank(colName)) {
                        mapColNameToPos.put(colName, i);
                    }
                }
            }
            return setValues(c, sheet, mapColNameToPos);
        }

        return lsRet;
    }

    private static <T> List<T> setValues(Class<T> c, Sheet sheet, Map<String, Integer> col) {
        List<T> result = new ArrayList<>();
        List<Field> lsFields = getFields(c);
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null || org.apache.commons.lang3.StringUtils.isEmpty(getStringCellValue(row.getCell(0)))) {
                continue;
            }
            T dataItem = null;
            try {
                dataItem = c.newInstance();
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
                throw new BusinessException(BizError.IMPORT_FAILED);
            }
            for (Field field : lsFields) {
                ExcelField excelField = field.getAnnotation(ExcelField.class);
                String colName = excelField.name();
                Integer colPos = col.get(colName);
                if (colPos != null) {
                    Cell cell = row.getCell(colPos);
                    if (cell == null) {
                        continue;
                    }
                    String value = getStringCellValue(cell);
                    Class<?> type = field.getType();
                    field.setAccessible(true);
                    try {
                        if (type == Boolean.class) {
                            field.set(dataItem, Boolean.valueOf(value));
                        } else if (type == Integer.class) {
                            field.set(dataItem, Integer.valueOf(value));
                        } else if (type == Date.class) {
                            field.set(dataItem, new SimpleDateFormat(excelField.pattern()).parse(value));
                        }
//                        else if (type == LocalDateTime.class) {
//                            field.set(dataItem, LocalDateTimeUtil.format(value,excelField.pattern()) );
//                        }
                        else {
                            field.set(dataItem, value);
                        }
                    } catch (Exception e) {
                        logger.error(e.getLocalizedMessage(), e);
                    }

                }
            }
            result.add(dataItem);
        }
        return result;
    }

    private static String getStringCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        int cellType = cell.getCellType();
        String value;
        switch (cellType) {
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = new DataFormatter().formatCellValue(cell);
                } else {
                    double d = cell.getNumericCellValue();
                    NumberFormat nf = NumberFormat.getInstance();
                    value = nf.format(d);
                    if (value.contains(",")) {
                        value = value.replace(",", "");
                    }
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            default:
                value = cell.getStringCellValue();
        }
        return value;
    }

    private static List<Field> getFields(Class<?> c) {
        List<Field> fields = new ArrayList<Field>();
        if (c.getDeclaredFields() != null && c.getDeclaredFields().length > 0) {
            for (Field field : c.getDeclaredFields()) {
                ExcelField excelField = field.getAnnotation(ExcelField.class);
                if (excelField != null) {
                    fields.add(field);
                }
            }
        }
        if (fields == null || fields.size() == 0) {
            throw new RuntimeException(">>>>>>>>>>> xxl-excel error, data field can not be empty.");
        }
        return fields;
    }


}