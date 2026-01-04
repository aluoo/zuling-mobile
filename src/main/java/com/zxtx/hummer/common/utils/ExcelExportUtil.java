package com.zxtx.hummer.common.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.annotation.ExcelSheet;
import com.zxtx.hummer.common.annotation.IgnoreExcelField;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Excel导出工具
 *
 * @author xuxueli 2017-09-08 22:27:20
 */
public class ExcelExportUtil {
    private static Logger logger = LoggerFactory.getLogger(ExcelExportUtil.class);


    /**
     * 导出Excel对象
     *
     * @param dataList Excel数据
     * @return
     */
    public static Workbook exportWorkbook(List<?> dataList) {

        // data
        if (dataList == null || dataList.size() == 0) {
            throw new RuntimeException(">>>>>>>>>>> xxl-excel error, data can not be empty.");
        }

        // sheet
        Class<?> sheetClass = dataList.get(0).getClass();
        ExcelSheet excelSheet = sheetClass.getAnnotation(ExcelSheet.class);

        String sheetName = dataList.get(0).getClass().getSimpleName();
        Short headColor = null;
        if (excelSheet != null) {
            if (excelSheet.name() != null && excelSheet.name().trim().length() > 0) {
                sheetName = excelSheet.name().trim();
            }
            headColor = excelSheet.headColor();
        }


        // sheet field
        List<Field> fields = setField(dataList);

        // book
        Workbook workbook = new SXSSFWorkbook(5000);     // HSSFWorkbook=2003/xls、XSSFWorkbook=2007/xlsx
        Sheet sheet = workbook.createSheet(sheetName);

        // sheet header row
        CellStyle headStyle = null;
        if (headColor != null) {
            headStyle = workbook.createCellStyle();
            /*Font headFont = book.createFont();
            headFont.setColor(headColor);
            headStyle.setFont(headFont);*/
            headStyle.setFillForegroundColor(headColor);
            headStyle.setFillPattern((short) FillPatternType.SOLID_FOREGROUND.ordinal());
            headStyle.setFillBackgroundColor(headColor);
        }
        setHead(sheet, fields, headStyle);
        // sheet data rows
        setDataRows(dataList, sheet, fields);
        return workbook;
    }

    /**
     * 导出Excel对象
     *
     * @param dataList Excel数据
     * @return
     */
    public static Workbook exportWorkbook(Map<String, List> sheets) {
        // book
        Workbook workbook = new HSSFWorkbook();     // HSSFWorkbook=2003/xls、XSSFWorkbook=2007/xlsx
        sheets.forEach((k, dataList) -> {
            // data
            if (dataList == null || dataList.size() == 0) {
                throw new RuntimeException(">>>>>>>>>>> xxl-excel error, data can not be empty.");
            }

            Sheet sheet = workbook.createSheet(k);
            List<Field> fields = setField(dataList);

            setHead(sheet, fields, null);
            setDataRows(dataList, sheet, fields);
        });


        return workbook;
    }

    private static void setHead(Sheet sheet, List<Field> fields, CellStyle style) {
        Row headRow = sheet.createRow(0);
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            String fieldName = (excelField != null && excelField.name() != null && excelField.name().trim().length() > 0) ? excelField.name() : field.getName();

            Cell cellX = headRow.createCell(i, Cell.CELL_TYPE_STRING);
            if (style != null) {
                cellX.setCellStyle(style);
            }
            cellX.setCellValue(String.valueOf(fieldName));
        }
    }

    private static List<Field> setField(List<?> dataList) {
        // sheet
        Class<?> sheetClass = dataList.get(0).getClass();
        // sheet field
        List<Field> fields = new ArrayList<Field>();
        if (sheetClass.getDeclaredFields() != null && sheetClass.getDeclaredFields().length > 0) {
            for (Field field : sheetClass.getDeclaredFields()) {
                IgnoreExcelField ignoreExcelField = field.getAnnotation(IgnoreExcelField.class);
                if (Modifier.isStatic(field.getModifiers()) || ignoreExcelField != null) {
                    continue;
                }
                fields.add(field);
            }
        }
        if (fields == null || fields.size() == 0) {
            throw new RuntimeException(">>>>>>>>>>> xxl-excel error, data field can not be empty.");
        }
        return fields;
    }

    private static void setDataRows(List<?> dataList, Sheet sheet, List<Field> fields) {
        // sheet data rows
        for (int dataIndex = 0; dataIndex < dataList.size(); dataIndex++) {
            int rowIndex = dataIndex + 1;
            Object rowData = dataList.get(dataIndex);

            Row rowX = sheet.createRow(rowIndex);
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                try {
                    field.setAccessible(true);
                    Object fieldValue = field.get(rowData);

                    Cell cellX = rowX.createCell(i, Cell.CELL_TYPE_STRING);
                    if (fieldValue != null) {
                        ExcelField excelField = field.getAnnotation(ExcelField.class);
                        Class<?> filedType = field.getType();
                        if (filedType == Date.class) {
                            String pattern = excelField.pattern();
                            if (StringUtils.isNotEmpty(pattern)) {
                                //sf.p
                                SimpleDateFormat sf = new SimpleDateFormat(pattern);
                                fieldValue = sf.format((Date) fieldValue);
                            }
                        }
                        if (excelField.replace() != null && (excelField.replace().length > 0)) {
                            if (fieldValue != null) {
                                for (String replaceRule : excelField.replace()
                                ) {
                                    int pos = replaceRule.indexOf("_");
                                    if (pos > 0) {
                                        String oldStr = replaceRule.substring(0, pos);
                                        String newStr = replaceRule.substring(pos + 1);
                                        if (fieldValue.toString().equals(oldStr)) {
                                            fieldValue = newStr;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        cellX.setCellValue(String.valueOf(fieldValue));
                    } else {
                        cellX.setCellValue("-");
                    }
                } catch (IllegalAccessException e) {
                    logger.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        }

    }

    /**
     * 导出Excel文件到磁盘
     *
     * @param dataList
     * @param filePath
     */
    public static void exportToFile(List<?> dataList, String filePath) {
        // workbook
        Workbook workbook = exportWorkbook(dataList);

        FileOutputStream fileOutputStream = null;
        try {
            // workbook 2 FileOutputStream
            fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream);

            // flush
            fileOutputStream.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 导出Excel字节数据
     *
     * @param dataList
     * @return
     */
    public static byte[] exportToBytes(List<?> dataList) {
        // workbook
        Workbook workbook = exportWorkbook(dataList);

        ByteArrayOutputStream byteArrayOutputStream = null;
        byte[] result = null;
        try {
            // workbook 2 ByteArrayOutputStream
            byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);

            // flush
            byteArrayOutputStream.flush();

            result = byteArrayOutputStream.toByteArray();
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 根据模板导出
     *
     * @param response
     * @param fileName
     * @param tplName
     * @param data
     * @param playLoad 负载
     * @return
     */
    public static boolean exportByTemplate(HttpServletResponse response, String fileName, String tplName, Map data, String playLoad) {
        try {
          /*  response.setHeader("Content-Disposition", "attachment;filename=" +
                    new String(fileName.getBytes(), "ISO8859-1") + ".xls");*/
            response.setContentType("application/vnd.ms-excel");
            XLSTransformer xlsTransformer = new XLSTransformer();
            Workbook wb = xlsTransformer.transformXLS(Thread.currentThread().getContextClassLoader().getResourceAsStream(tplName), data);
            String filename = URLEncoder.encode(fileName, "UTF-8") + ".xls";
            response.setContentType("application/ms-excel;charset=UTF-8");
            //iso-8859-1
            if (com.zxtx.hummer.common.utils.StringUtils.isNotEmpty(playLoad)) {
                /* playLoad=URLEncoder.encode(playLoad,"UTF-8");*/
                response.setHeader("playLoad", playLoad);
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(), "utf-8"));
            OutputStream out = response.getOutputStream();
            try {
                wb.write(out);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                out.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 根据模板导出
     *
     * @param response
     * @param fileName 导出名
     * @param tplName  模板路径
     * @param data     数据
     */
    public static boolean exportByTemplate(HttpServletResponse response, String fileName, String tplName, Map data) {

        return exportByTemplate(response, fileName, tplName, data, null);
    }

    public static void setResponseContent(HttpServletResponse response, String filename) {
        response.setContentType("application/ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(), StandardCharsets.ISO_8859_1));
    }

    public static String buildName(String prefix) {
        String date = cn.hutool.core.date.DateUtil.format(DateUtil.date(), DatePattern.NORM_DATE_PATTERN);
        String suffix = ".xls";
        return prefix + date + suffix;
    }
}