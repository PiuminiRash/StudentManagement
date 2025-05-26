package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lk.ijse.dto.StudentDto;
import lk.ijse.dto.bookDto;
import lk.ijse.dto.tm.bookTm;
import lk.ijse.dto.tm.borrowBookTm;
import lk.ijse.model.bookModel;
import lk.ijse.model.studentModel;

import java.sql.SQLException;
import java.util.List;

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

    private bookModel bookModel = new bookModel();

    private studentModel studentModel = new studentModel();

    private ObservableList<borrowBookTm> obList = FXCollections.observableArrayList();

    public void initialize() {
        loadBook();
        loadStudent();
    }

    private void loadBook() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<bookDto> borrowBook = bookModel.getAllBook();

            for (bookDto dto : borrowBook) {
                obList.add(dto.getBookCode());
            }
            cmdBookId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadStudent() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<StudentDto> borrowBook = studentModel.getAllStudent();

            for (StudentDto dto : borrowBook) {
                obList.add(dto.getStudentId());
            }
            cmdStudentId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String studentId = String.valueOf(cmdStudentId.getItems());
        String studentName = txtName.getText();

        String bookId = String.valueOf(cmdBookId.getItems());
        String title = txtBookTitle.getText();

        try {
            StudentDto studentDto = new StudentDto(studentId, studentName,bookId,title);
            boolean isStudentSaved = StudentModel.saveStudent(studentDto);

            if (isStudentSaved) {
                BookDto guardianDto = new BookDto(studentId);
                boolean isGuardianSaved = GardianModel.saveGardian(guardianDto);

                if (isGuardianSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Student Details Saved!").show();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Error saving guardian").show();
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
}
