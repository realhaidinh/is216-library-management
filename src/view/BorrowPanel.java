package view;

import controller.BorrowingDAO;
import model.Borrowing;
import util.BorrowToExcel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

public class BorrowPanel extends JFrame {
	private JPanel borrowPanel;
	private JLabel title;
	private JTable borrowTable;
	private JTextField readerIdField;
	private JTextField isbnField;
	private JTextField borrowDateField;
	private JTextField returnDateField;
	private JScrollPane scroll;
	private JButton borrowBtn;
	private JButton deleteBtn;
	private JButton returnBtn;
	private JButton exportBtn;
	private JTextField idField;
	private JButton searchBtn;
	private JTextField searchField;
	private JComboBox searchCombo;
	private ArrayList<Borrowing> borrows;
	private DefaultTableModel model;
	String[] headers = {"Mã phiếu mượn", "Mã độc giả", "Mã sách", "Ngày mượn", "Ngày trả"};
//	private BookPanel bookPanel;

	public BorrowPanel(BookPanel bookPanel) {
		$$$setupUI$$$();
		model = (DefaultTableModel) borrowTable.getModel();
		model.setColumnIdentifiers(headers);
		borrows = BorrowingDAO.findAllBorrowing();
		showTable(-1, "");
		borrowTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				idField.setText(model.getValueAt(borrowTable.getSelectedRow(), 0).toString());
				readerIdField.setText(model.getValueAt(borrowTable.getSelectedRow(), 1).toString());
				isbnField.setText(model.getValueAt(borrowTable.getSelectedRow(), 2).toString());
				borrowDateField.setText(model.getValueAt(borrowTable.getSelectedRow(), 3).toString());
				returnDateField.setText(model.getValueAt(borrowTable.getSelectedRow(), 4).toString());
			}
		});
		BorrowPanel thisPanel = this;
		borrowBtn.addActionListener(e -> {
			AddBorrow addForm = new AddBorrow(thisPanel, bookPanel);
			addForm.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			addForm.pack();
			addForm.setVisible(true);
		});
		returnBtn.addActionListener(e -> {
			int row = borrowTable.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn phiếu mượn cần trả");
			} else if (model.getValueAt(row, 4).toString().isEmpty()) {
				ReturnBook returnForm = new ReturnBook(thisPanel, bookPanel, row);

				returnForm.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				returnForm.pack();
				returnForm.setVisible(true);
				clearTextField();
			} else {
				JOptionPane.showMessageDialog(rootPane, "Sách đã hoàn trả");
			}
		});
		deleteBtn.addActionListener(e -> {
			int row = borrowTable.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn phiếu mượn cần xóa");
			} else {
				int input = JOptionPane.showConfirmDialog(thisPanel, "Xác nhận xóa phiếu mượn", "Xóa phiếu mượn", JOptionPane.OK_CANCEL_OPTION);
				if (input == JOptionPane.OK_OPTION) {
					if (BorrowingDAO.getDAO().deleteBorrow(model.getValueAt(row, 0).toString(), model.getValueAt(row, 2).toString())) {
						bookPanel.showTable(-1, "");
						showTable(-1, "");
						JOptionPane.showMessageDialog(rootPane, "Xóa phiếu mượn thành công");
						clearTextField();
					} else {
						JOptionPane.showMessageDialog(rootPane, "Xóa phiếu mượn thất bại");
					}
				}
			}
		});
		exportBtn.addActionListener(e -> {
			SaveExcelFile saveFile = new SaveExcelFile();
			int input = saveFile.showDialog(thisPanel, "Lưu");
			if (input == JFileChooser.APPROVE_OPTION) {
				File file = saveFile.getSelectedFile();
				BorrowToExcel excel = new BorrowToExcel();
				excel.setFilePath(file.getAbsolutePath().concat(".xlsx"));
				if (excel.export(borrows, headers)) {
					JOptionPane.showMessageDialog(rootPane, "Xuất danh sách thành công");
				} else {
					JOptionPane.showMessageDialog(rootPane, "Xuất danh sách thất bại");
				}
			}
		});
		for (int i = 0; i < 3; ++i) {
			searchCombo.addItem(headers[i]);
		}

		searchBtn.addActionListener(e -> {
			showTable(searchCombo.getSelectedIndex(), searchField.getText());
		});
	}

	public void clearTextField() {
		idField.setText("");
		readerIdField.setText("");
		isbnField.setText("");
		borrowDateField.setText("");
		returnDateField.setText("");
	}

	public JTable getBorrowTable() {
		return borrowTable;
	}

	public void showTable(int type, String query) {
		if (query.isEmpty()) {
			borrows = BorrowingDAO.findAllBorrowing();
		} else {
			switch (type) {
				case -1: {
					borrows = BorrowingDAO.findAllBorrowing();
					break;
				}
				case 0: {
					borrows = new ArrayList<Borrowing>();
					Borrowing borrow = BorrowingDAO.findById(query);
					if (borrow.getISBN() != null) {
						borrows.add(borrow);
					}
					break;
				}
				case 1: {
					borrows = BorrowingDAO.findBorrowingByReaderId(query);
					break;
				}
				case 2: {
					borrows = BorrowingDAO.findBorrowingByISBN(query);
					break;
				}
			}
		}
		model.setNumRows(0);
		for (Borrowing borrow : borrows) {
			addToTable(borrow);
		}
	}

	public void addToTable(Borrowing borrow) {
		model.addRow(new Object[]{
				borrow.getId(),
				borrow.getReaderId(),
				borrow.getISBN(),
				borrow.getBorrowDate(),
				borrow.getReturnDate()
		});
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		borrowPanel = new JPanel();
		borrowPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 8, new Insets(0, 0, 0, 0), -1, -1));
		scroll = new JScrollPane();
		borrowPanel.add(scroll, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 6, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		borrowTable = new JTable();
		scroll.setViewportView(borrowTable);
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		borrowPanel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(5, 7, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		borrowPanel.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		borrowPanel.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 7, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		borrowBtn = new JButton();
		borrowBtn.setText("Mượn sách");
		borrowPanel.add(borrowBtn, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		title = new JLabel();
		title.setText("Quản lý cho mượn sách");
		borrowPanel.add(title, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 7, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		returnBtn = new JButton();
		returnBtn.setText("Trả sách");
		borrowPanel.add(returnBtn, new com.intellij.uiDesigner.core.GridConstraints(6, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		deleteBtn = new JButton();
		deleteBtn.setText("Xóa");
		borrowPanel.add(deleteBtn, new com.intellij.uiDesigner.core.GridConstraints(6, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		exportBtn = new JButton();
		exportBtn.setText("Xuất danh sách");
		borrowPanel.add(exportBtn, new com.intellij.uiDesigner.core.GridConstraints(6, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
		borrowPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		final JLabel label1 = new JLabel();
		label1.setText("Mã phiếu mượn");
		borrowPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		idField = new JTextField();
		idField.setEditable(true);
		idField.setText("");
		borrowPanel.add(idField, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		final JLabel label2 = new JLabel();
		label2.setText("Mã độc giả");
		borrowPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(87, 17), null, 0, false));
		readerIdField = new JTextField();
		readerIdField.setEditable(true);
		borrowPanel.add(readerIdField, new com.intellij.uiDesigner.core.GridConstraints(1, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		returnDateField = new JTextField();
		returnDateField.setEditable(true);
		returnDateField.setText("");
		borrowPanel.add(returnDateField, new com.intellij.uiDesigner.core.GridConstraints(3, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		final JLabel label3 = new JLabel();
		label3.setText("Ngày trả");
		borrowPanel.add(label3, new com.intellij.uiDesigner.core.GridConstraints(3, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label4 = new JLabel();
		label4.setText("Mã sách");
		borrowPanel.add(label4, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(87, 17), null, 0, false));
		isbnField = new JTextField();
		isbnField.setEditable(true);
		borrowPanel.add(isbnField, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		final JLabel label5 = new JLabel();
		label5.setText("Ngày mượn");
		borrowPanel.add(label5, new com.intellij.uiDesigner.core.GridConstraints(2, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(87, 17), null, 0, false));
		borrowDateField = new JTextField();
		borrowDateField.setEditable(true);
		borrowDateField.setText("");
		borrowPanel.add(borrowDateField, new com.intellij.uiDesigner.core.GridConstraints(2, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		searchBtn = new JButton();
		searchBtn.setText("Tìm kiếm");
		borrowPanel.add(searchBtn, new com.intellij.uiDesigner.core.GridConstraints(4, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		searchField = new JTextField();
		borrowPanel.add(searchField, new com.intellij.uiDesigner.core.GridConstraints(4, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		searchCombo = new JComboBox();
		borrowPanel.add(searchCombo, new com.intellij.uiDesigner.core.GridConstraints(4, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return borrowPanel;
	}

}
