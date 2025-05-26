package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lk.ijse.dto.tm.borrowBookTm;

public class bookBorrowManageController {
    @FXML
    public JFXTextField txtName;

    @FXML
    public JFXButton btnAdd;

    @FXML
    public JFXButton btnUpdate;

    @FXML
    public TableView<borrowBookTm> tblStudentBook;

    @FXML
    public TableColumn<?,?> colId;

    @FXML
    public TableColumn<?,?> colName;

    @FXML
    public TableColumn<?,?> colDelete;

    @FXML
    public JFXComboBox<String> cmdStudentId;

    @FXML
    public JFXComboBox<String> cmdBookId;

    @FXML
    public JFXTextField txtBookTitle;


}
