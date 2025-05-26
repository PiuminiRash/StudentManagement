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
import lk.ijse.dto.bookDto;
import lk.ijse.dto.tm.bookTm;
import lk.ijse.dto.tm.studentTm;
import lk.ijse.model.bookModel;
import lk.ijse.model.studentModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class bookManageController {
    @FXML
    public JFXTextField txtId;

    @FXML
    public JFXTextField txtName;

    @FXML
    public JFXTextField txtQty;


    @FXML
    public JFXTextField colQty;

    @FXML
    public JFXButton btnAdd;

    @FXML
    public JFXButton btnUpdate;

    @FXML
    private TableColumn<?,?> colDelete;

    @FXML
    private TableView<bookTm> tblBook;

    @FXML
    private TableColumn<?,?> colId;

    @FXML
    private TableColumn<?,?> colName;

    private bookModel bookModel = new bookModel();

    private ObservableList<bookTm> obList = FXCollections.observableArrayList();

    public void initialize() {
        generateNextBookId();
        setCellValueFactory();
        loadAllBook();
    }

    private void generateNextBookId() {
        try {
            String bookId = bookModel.generateNextBookId();
            txtId.setText(bookId);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");

        generateNextBookId();
    }

    private void setCellValueFactory(){
        colId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("DeleteBtn"));
    }

    private void loadAllBook(){
        var model = new bookModel();

        try {
            List<bookDto> dtoList = bookModel.getAllBook();

            for (bookDto dto : dtoList) {
                Button deleteBtn = new Button("Delete");

                setDeleteButtonOnAction(deleteBtn,dto.getBookCode());

                obList.add(
                        new bookTm(
                                dto.getBookCode(),
                                dto.getTitle(),
                                dto.getAvailable_qty(),
                                deleteBtn
                        )
                );
            }

            tblBook.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDeleteButtonOnAction(Button deleteBtn , String bookId) {
        deleteBtn.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove book ?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                obList.removeIf(bookTm -> bookTm.getBookCode().equals(bookId));
                tblBook.refresh();

                DeleteBook(bookId);
                loadAllBook();
            }
        });
    }

    private void DeleteBook(String bookId){
        try {
            boolean isDeleted = bookModel.deleteBook(bookId);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Book deleted!").show();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Book not deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        tblBook.refresh();
    }


    public void btnSaveOnAction(ActionEvent actionEvent) {
        String bookId = txtId.getText();
        String bookName = txtName.getText();
        int availableQty = Integer.parseInt(txtQty.getText());

        try {
            bookDto bookDto = new bookDto(bookId, bookName, availableQty);
            boolean isBookSaved = bookModel.saveBook(bookDto);

            if (isBookSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Book Details Saved!").show();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Error saving Book").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String id =  txtId.getText();
        String name = txtName.getText();
        int qty = Integer.parseInt(txtQty.getText());
        try{
            var dto = new bookDto (id,name,qty);
            boolean isUpdate = bookModel.updateBook(dto);
            if (isUpdate){
                new Alert(Alert.AlertType.CONFIRMATION,"updated Book").show();
            }
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

}
