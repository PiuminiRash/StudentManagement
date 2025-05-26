package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.dto.StudentDto;
import lk.ijse.dto.tm.studentTm;
import lk.ijse.model.studentModel;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public class studentManageController {
    @FXML
    public JFXTextField txtId;

    @FXML
    public JFXTextField txtName;

    @FXML
    public JFXButton btnAdd;

    @FXML
    public JFXButton btnUpdate;

    @FXML
    private TableColumn<?,?> colDelete;

    @FXML
    private TableView<studentTm> tblStudentDetails;

    @FXML
    private TableColumn<?,?> colId;

    @FXML
    private TableColumn<?,?> colName;

    private studentModel studentModel = new studentModel();

    private ObservableList<studentTm> obList = FXCollections.observableArrayList();

    public void initialize() {
        generateNextStudentId();
        setCellValueFactory();
        loadAllStudent();
    }

    private void generateNextStudentId() {
        try {
            String studentId = studentModel.generateNextStudentId();
            txtId.setText(studentId);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");

        generateNextStudentId();
    }

    private void setCellValueFactory(){
        colId.setCellValueFactory(new PropertyValueFactory<>("StudentId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("StudentName"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("DeleteBtn"));
    }

    private void loadAllStudent(){
        var model = new studentModel();

        try {
            List<StudentDto> dtoList = studentModel.getAllStudent();

            for (StudentDto dto : dtoList) {
                Button deleteBtn = new Button("Delete");

                setDeleteButtonOnAction(deleteBtn,dto.getStudentId());

                obList.add(
                        new studentTm(
                                dto.getStudentId(),
                                dto.getStudentName(),
                                deleteBtn
                        )
                );
            }

            tblStudentDetails.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDeleteButtonOnAction(Button deleteBtn , String studentId) {
        deleteBtn.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove student ?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                obList.removeIf(studentTm -> studentTm.getStudentId().equals(studentId));
                tblStudentDetails.refresh();

                DeleteStudent(studentId);
                loadAllStudent();
            }
        });
    }

    private void DeleteStudent(String studentId){
        try {
            boolean isDeleted = studentModel.deleteStudent(studentId);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Student deleted!").show();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Student not deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        tblStudentDetails.refresh();
    }


    public void btnSaveOnAction(ActionEvent actionEvent) {
        String studentId = txtId.getText();
        String studentName = txtName.getText();

        try {
            StudentDto studentDto = new StudentDto(studentId, studentName);
            boolean isStudentSaved = studentModel.saveStudent(studentDto);

            if (isStudentSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Student Details Saved!").show();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Error saving student").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String id =  txtId.getText();
        String name = txtName.getText();
        try{
            var dto = new StudentDto (id,name);
            boolean isUpdate = studentModel.updateStudent(dto);
                if (isUpdate){
                    new Alert(Alert.AlertType.CONFIRMATION,"updated Student").show();
            }
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }
}
