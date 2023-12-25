package view;

import controller.BookDAO;
import model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import util.BookToExcel;

public class BookPanel extends JFrame {
	private JPanel bookPanel;
	private JTable bookTable;
	private JScrollPane scroll;
	private JTextField isbnField;
	private JTextField titleField;
	private JTextField authorField;
	private JTextField categoryField;
	private JTextField publisherField;
	private JTextField pubdateField;
	private JTextField statusField;
	private JLabel isbn;
	private JLabel author;
	private JLabel category;
	private JLabel title;
	private JLabel publisher;
	private JLabel publishdate;
	private JLabel status;
	private JLabel panelTitle;
	private JButton addBtn;
	private JButton deleteBtn;
	private JButton editBtn;
	private JButton exportBtn;
	private JButton searchBtn;
	private JTextField searchField;
	private JComboBox searchCombo;
	private ArrayList<Book> books;
	private DefaultTableModel model;
	private String[] headers = {"Mã sách", "Tên sách", "Tác giả", "Thể loại", "Nhà xuất bản", "Năm xuất bản", "Trạng thái"};

	public BookPanel() throws SQLException {
		$$$setupUI$$$();
		model = (DefaultTableModel) bookTable.getModel();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		model.setColumnIdentifiers(headers);
		books = BookDAO.getDAO().findAllBook();
		showTable(-1, "");
		bookTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				isbnField.setText(model.getValueAt(bookTable.getSelectedRow(), 0).toString());
				titleField.setText(model.getValueAt(bookTable.getSelectedRow(), 1).toString());
				authorField.setText(model.getValueAt(bookTable.getSelectedRow(), 2).toString());
				categoryField.setText(model.getValueAt(bookTable.getSelectedRow(), 3).toString());
				publisherField.setText(model.getValueAt(bookTable.getSelectedRow(), 4).toString());
				pubdateField.setText(model.getValueAt(bookTable.getSelectedRow(), 5).toString());
				statusField.setText(model.getValueAt(bookTable.getSelectedRow(), 6).toString());
			}
		});
		BookPanel thisPanel = this;
		addBtn.addActionListener(e -> {
			AddBook addForm = new AddBook(thisPanel);
			addForm.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			addForm.pack();
			addForm.setVisible(true);
		});
		deleteBtn.addActionListener(e -> {
			int row = bookTable.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn sách cần xóa");
			} else {
				if (BookDAO.getDAO().deleteByISBN(model.getValueAt(row, 0).toString())) {
					showTable(-1, "");
					clearTextField();
					JOptionPane.showMessageDialog(rootPane, "Xóa sách thành công");
				} else {
					JOptionPane.showMessageDialog(rootPane, "Xóa sách thất bại");
				}
			}
		});
		editBtn.addActionListener(e -> {
			int row = bookTable.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn sách cần sửa thông tin");
			} else {
				EditBook editForm = new EditBook(thisPanel, row);
				editForm.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				editForm.pack();
				editForm.setVisible(true);
				clearTextField();
			}
		});
		exportBtn.addActionListener(e -> {
			SaveExcelFile saveFile = new SaveExcelFile();
			int input = saveFile.showDialog(thisPanel, "Lưu");
			if (input == JFileChooser.APPROVE_OPTION) {
				File file = saveFile.getSelectedFile();
				BookToExcel excel = new BookToExcel();
				excel.setFilePath(file.getAbsolutePath().concat(".xlsx"));
				if (excel.export(books, headers)) {
					JOptionPane.showMessageDialog(rootPane, "Xuất danh sách thành công");
				} else {
					JOptionPane.showMessageDialog(rootPane, "Xuất danh sách thất bại");
				}
			}
		});
		for (String item : headers)
			searchCombo.addItem(item);
		searchBtn.addActionListener(e -> {
			showTable(searchCombo.getSelectedIndex(), searchField.getText());
		});
	}

	public JTable getBookTable() {
		return bookTable;
	}

	public void showTable(int type, String query) {
		if (query.isEmpty()) {
			books = BookDAO.getDAO().findAllBook();
		} else {
			switch (type) {
				case -1: {
					books = BookDAO.getDAO().findAllBook();
					break;
				}
				case 0: {
					books = new ArrayList<Book>();
					Book book = BookDAO.getDAO().findBookByISBN(query);
					if (book.getISBN() != null)
						books.add(book);
					break;
				}
				case 1: {
					books = BookDAO.getDAO().findBookByTitle(query);
					break;
				}
				case 2: {
					books = BookDAO.getDAO().findBookByAuthor(query);
					break;
				}
				case 3: {
					books = BookDAO.getDAO().findBookByCategory(query);
					break;
				}
				case 4: {
					books = BookDAO.getDAO().findBookByPublisher(query);
					break;
				}
				case 5: {
					books = BookDAO.getDAO().findBookByPublishDate(query);
					break;
				}
				case 6: {
					books = BookDAO.getDAO().findBookByStatus(query);
					break;
				}
			}
		}
		model.setNumRows(0);
		for (Book book : books) {
			addToTable(book);
		}
	}

	public void clearTextField() {
		isbnField.setText("");
		titleField.setText("");
		authorField.setText("");
		categoryField.setText("");
		publisherField.setText("");
		pubdateField.setText("");
		statusField.setText("");
	}

	public void addToTable(Book book) {
		model.addRow(new Object[]{
				book.getISBN(), book.getTitle(), book.getAuthor(), book.getCategory(), book.getPublisher(), book.getPublishDate(), book.getStatus() ? "Sẵn có" : "Đang cho mượn"
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
		bookPanel = new JPanel();
		bookPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 7, new Insets(0, 0, 0, 0), -1, -1));
		isbn = new JLabel();
		isbn.setText("Mã sách");
		bookPanel.add(isbn, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(131, 17), null, 0, false));
		author = new JLabel();
		author.setText("Tác giả");
		bookPanel.add(author, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(131, 17), null, 0, false));
		publisher = new JLabel();
		publisher.setText("Nhà xuất bản");
		bookPanel.add(publisher, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(131, 17), null, 0, false));
		status = new JLabel();
		status.setText("Tình trạng");
		bookPanel.add(status, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(131, 17), null, 0, false));
		authorField = new JTextField();
		authorField.setEditable(false);
		authorField.setText("");
		bookPanel.add(authorField, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		publisherField = new JTextField();
		publisherField.setEditable(false);
		publisherField.setText("");
		bookPanel.add(publisherField, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		statusField = new JTextField();
		statusField.setEditable(false);
		statusField.setText("");
		bookPanel.add(statusField, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		title = new JLabel();
		title.setText("Tên sách");
		bookPanel.add(title, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(105, 17), null, 0, false));
		titleField = new JTextField();
		titleField.setEditable(false);
		titleField.setText("");
		bookPanel.add(titleField, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		category = new JLabel();
		category.setText("Thể loại");
		bookPanel.add(category, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(105, 17), null, 0, false));
		categoryField = new JTextField();
		categoryField.setEditable(false);
		categoryField.setText("");
		bookPanel.add(categoryField, new com.intellij.uiDesigner.core.GridConstraints(2, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		publishdate = new JLabel();
		publishdate.setText("Năm xuất bản");
		bookPanel.add(publishdate, new com.intellij.uiDesigner.core.GridConstraints(3, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(105, 17), null, 0, false));
		pubdateField = new JTextField();
		pubdateField.setEditable(false);
		pubdateField.setText("");
		bookPanel.add(pubdateField, new com.intellij.uiDesigner.core.GridConstraints(3, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		panelTitle = new JLabel();
		panelTitle.setText("Quản lý sách");
		bookPanel.add(panelTitle, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		scroll = new JScrollPane();
		bookPanel.add(scroll, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		bookTable = new JTable();
		bookTable.setCellSelectionEnabled(false);
		bookTable.setColumnSelectionAllowed(false);
		bookTable.setRowSelectionAllowed(true);
		scroll.setViewportView(bookTable);
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		bookPanel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 5, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		bookPanel.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(1, 6, 5, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		isbnField = new JTextField();
		isbnField.setEditable(false);
		bookPanel.add(isbnField, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		addBtn = new JButton();
		addBtn.setText("Thêm");
		bookPanel.add(addBtn, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		deleteBtn = new JButton();
		deleteBtn.setText("Xóa");
		bookPanel.add(deleteBtn, new com.intellij.uiDesigner.core.GridConstraints(6, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		bookPanel.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		editBtn = new JButton();
		editBtn.setText("Sửa");
		bookPanel.add(editBtn, new com.intellij.uiDesigner.core.GridConstraints(6, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		exportBtn = new JButton();
		exportBtn.setText("Xuất danh sách");
		bookPanel.add(exportBtn, new com.intellij.uiDesigner.core.GridConstraints(6, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		searchBtn = new JButton();
		searchBtn.setText("Tìm kiếm");
		bookPanel.add(searchBtn, new com.intellij.uiDesigner.core.GridConstraints(4, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		searchField = new JTextField();
		bookPanel.add(searchField, new com.intellij.uiDesigner.core.GridConstraints(4, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		searchCombo = new JComboBox();
		bookPanel.add(searchCombo, new com.intellij.uiDesigner.core.GridConstraints(4, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return bookPanel;
	}

}
