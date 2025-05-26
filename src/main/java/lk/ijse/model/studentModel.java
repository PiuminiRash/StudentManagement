package lk.ijse.model;

import lk.ijse.DB.DbConnection;
import lk.ijse.dto.StudentDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class studentModel {
    public String generateNextStudentId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT StudentId FROM Student ORDER BY StudentId DESC LIMIT 1";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return splitStudentId(resultSet.getString(1));
        }
        return splitStudentId(null);
    }

    private String splitStudentId(String currentStudentId) {
        if(currentStudentId != null) {
            String[] split = currentStudentId.split("S");

            int id = Integer.parseInt(split[1]);
            id++;
            return "S00" + id;
        } else {
            return "S001";
        }
    }

    public static boolean saveStudent(StudentDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "INSERT INTO Student VALUES (?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getStudentId());
        pstm.setString(2, dto.getStudentName());

        int rowsAffected = pstm.executeUpdate();
        return rowsAffected > 0;
    }

    public List<StudentDto> getAllStudent() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM Student";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        ArrayList<StudentDto> dtoList = new ArrayList<>();

        while(resultSet.next()) {
            dtoList.add(
                    new StudentDto(
                            resultSet.getString(1),
                            resultSet.getString(2)
                    )
            );
        }
        return dtoList;
    }

    public boolean deleteStudent(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM Student WHERE  StudentId= ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, id);

        return pstm.executeUpdate() > 0;
    }

    public boolean updateStudent(StudentDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE Student SET StudentName = ? WHERE StudentId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getStudentName());
        pstm.setString(2, String.valueOf(dto.getStudentId()));

        return pstm.executeUpdate() > 0;
    }

    public StudentDto searchStudent(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection ();

        String sql = "SELECT * FROM Student WHERE StudentId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        StudentDto dto = null;

        if(resultSet.next()) {
            String name = resultSet.getString(2);


            dto = new StudentDto(id,name);
        }
        return dto;
    }
}
