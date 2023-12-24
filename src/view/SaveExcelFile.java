package view;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SaveExcelFile extends JFileChooser {

	FileNameExtensionFilter filter = new FileNameExtensionFilter(".xlsx", "xlsx");
	public SaveExcelFile() {
		this.setFileFilter(filter);
		this.setMultiSelectionEnabled(false);
	}

}
