package lk.ijse.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class bookTm {
    private String bookCode;
    private String title;
    private int available_qty;
    private Button DeleteBtn;
}
