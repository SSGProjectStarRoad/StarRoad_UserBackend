package com.ssg.starroad.board.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long managerId;
    private String category;
    private String content;
    private String title;
    private Long userId;
}
