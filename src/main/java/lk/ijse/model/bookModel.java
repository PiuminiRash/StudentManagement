package lk.ijse.model;

import lk.ijse.DB.DbConnection;
import lk.ijse.dto.bookDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class bookModel {
    public String generateNextBookId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT bookId FROM book ORDER BY bookId DESC LIMIT 1";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return splitBookId(resultSet.getString(1));
        }
        return splitBookId(null);
    }

    private String splitBookId(String currentBookId) {
        if(currentBookId != null) {
            String[] split = currentBookId.split("B");

            int id = Integer.parseInt(split[1]);
            id++;
            return "B00" + id;
        } else {
            return "B001";
        }
    }

    public static boolean saveBook(bookDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "INSERT INTO book VALUES (?, ? ,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getBookCode());
        pstm.setString(2, dto.getTitle());
        pstm.setString(3, String.valueOf(dto.getAvailable_qty()));

        int rowsAffected = pstm.executeUpdate();
        return rowsAffected > 0;
    }

    public List<bookDto> getAllBook() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM book";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        ArrayList<bookDto> dtoList = new ArrayList<>();

        while(resultSet.next()) {
            dtoList.add(
                    new bookDto(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getInt(3)
                    )
            );
        }
        return dtoList;
    }

    public boolean deleteBook(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM book WHERE  bookId= ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, id);

        return pstm.executeUpdate() > 0;
    }

    public boolean updateBook(bookDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE book SET bookName = ? WHERE bookId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getTitle());
        pstm.setString(2, String.valueOf(dto.getAvailable_qty()));
        pstm.setString(2, dto.getBookCode());

        return pstm.executeUpdate() > 0;
    }

    public bookDto searchBook(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection ();

        String sql = "SELECT * FROM book WHERE bookId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        bookDto dto = null;

        if(resultSet.next()) {
            String name = resultSet.getString(2);
            int qty = resultSet.getInt(3);


            dto = new bookDto(id,name,qty);
        }
        return dto;
    }
}
