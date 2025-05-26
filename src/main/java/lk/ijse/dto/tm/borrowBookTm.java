package lk.ijse.dto.tm;

import lombok.*;

import java.awt.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class borrowBookTm {
    private String studentId;
    private String bookId;
    private String date;
    private Button delete;
}
