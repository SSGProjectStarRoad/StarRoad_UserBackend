package com.ssg.starroad.user.dto;

import com.ssg.starroad.user.enums.ActiveStatus;
import com.ssg.starroad.user.enums.Gender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String password;
    private String name;
    private String nickname;
    private String email;
    private Gender gender;
    private LocalDate birth;
    private String phone;
    private String imagePath;
    private String provider;
    private int reviewExp;
    private int point;
    private ActiveStatus activeStatus;
}
