package com.ssg.starroad.board.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardContentDTO {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String imagePath;
    private Long boardId;
}
