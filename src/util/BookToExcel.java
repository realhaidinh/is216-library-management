package util;

import model.Book;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import java.util.ArrayList;

public class BookToExcel extends ExportToExcel<Book> {

	@Override
	public Boolean export(ArrayList<Book> data, String[] headers) {
		if(open() == false)
			return false;
		Sheet sheet = workbook.createSheet("Sách");
		Row row = null;
		Cell cell = null;
		int rowIndex = 0;
		row = sheet.createRow(rowIndex);
		for(int i = 0; i < headers.length; ++i) {
			cell = row.createCell(i);
			cell.setCellValue(headers[i]);
		}
		rowIndex++;
		for(Book book : data) {
			row = sheet.createRow(rowIndex);
			cell = row.createCell(0);
			cell.setCellValue(book.getISBN());
			cell = row.createCell(1);
			cell.setCellValue(book.getTitle());
			cell = row.createCell(2);
			cell.setCellValue(book.getAuthor());
			cell = row.createCell(3);
			cell.setCellValue(book.getCategory());
			cell = row.createCell(4);
			cell.setCellValue(book.getPublisher());
			cell = row.createCell(5);
			cell.setCellValue(book.getPublishDate());
			cell = row.createCell(6);
			cell.setCellValue(book.getStringStatus());
			rowIndex++;
		}
		int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
		for(int colIndex = 0; colIndex < numberOfColumn; ++colIndex)
			sheet.autoSizeColumn(colIndex);
		return close();
	}
}