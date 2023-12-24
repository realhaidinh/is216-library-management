package util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import model.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class ExportToExcel<V> {
	String filePath = null;
	FileOutputStream file = null;
	XSSFWorkbook workbook = null;

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	protected Boolean open() {
		try {
			file = new FileOutputStream(filePath);
			workbook = new XSSFWorkbook();
			return true;
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	protected Boolean close(){
		try {
			workbook.write(file);
			workbook.close();
			file.close();
			return true;
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}
	public abstract Boolean export(ArrayList<V> data, String[] headers);
}

