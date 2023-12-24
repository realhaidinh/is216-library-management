package util;

import model.Reader;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import java.util.ArrayList;

public class ReaderToExcel extends ExportToExcel<Reader> {

	@Override
	public Boolean export(ArrayList<Reader> data, String[] headers) {
		if(open() == false)
			return false;
		Sheet sheet = workbook.createSheet("Độc giả");
		Row row = null;
		Cell cell = null;
		int rowIndex = 0;
		row = sheet.createRow(rowIndex);
		for(int i = 0; i < headers.length; ++i) {
			cell = row.createCell(i);
			cell.setCellValue(headers[i]);
		}
		rowIndex++;
		for(Reader reader : data) {
			row = sheet.createRow(rowIndex);
			cell = row.createCell(0);
			cell.setCellValue(reader.getReaderId());
			cell = row.createCell(1);
			cell.setCellValue(reader.getName());
			cell = row.createCell(2);
			cell.setCellValue(reader.getPhone());
			cell = row.createCell(3);
			cell.setCellValue(reader.getBirthDate());
			rowIndex++;
		}
		int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
		for(int colIndex = 0; colIndex < numberOfColumn; ++colIndex)
			sheet.autoSizeColumn(colIndex);
		return close();
	}
}