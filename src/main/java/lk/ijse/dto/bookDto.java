package lk.ijse.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class bookDto {
    private String bookCode;
    private String title;
    private int available_qty;
}
