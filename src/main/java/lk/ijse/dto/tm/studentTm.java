package lk.ijse.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class studentTm {
    private String StudentId;
    private String StudentName;
    private Button DeleteBtn;
}
