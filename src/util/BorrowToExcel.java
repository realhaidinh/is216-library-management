package util;

import model.Borrowing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import java.util.ArrayList;

public class BorrowToExcel extends ExportToExcel<Borrowing> {

	@Override
	public Boolean export(ArrayList<Borrowing> data, String[] headers) {
		if(open() == false)
			return false;
		Sheet sheet = workbook.createSheet("Phiếu mượn sách");
		Row row = null;
		Cell cell = null;
		int rowIndex = 0;
		row = sheet.createRow(rowIndex);
		for(int i = 0; i < headers.length; ++i) {
			cell = row.createCell(i);
			cell.setCellValue(headers[i]);
		}
		rowIndex++;
		for(Borrowing borrow : data) {
			row = sheet.createRow(rowIndex);
			cell = row.createCell(0);
			cell.setCellValue(borrow.getId());
			cell = row.createCell(1);
			cell.setCellValue(borrow.getReaderId());
			cell = row.createCell(2);
			cell.setCellValue(borrow.getISBN());
			cell = row.createCell(3);
			cell.setCellValue(borrow.getBorrowDate());
			cell = row.createCell(4);
			cell.setCellValue(borrow.getReturnDate());
			rowIndex++;
		}
		int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
		for(int colIndex = 0; colIndex < numberOfColumn; ++colIndex)
			sheet.autoSizeColumn(colIndex);
		return close();
	}
}