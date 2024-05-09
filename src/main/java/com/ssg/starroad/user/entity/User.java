package com.ssg.starroad.user.entity;

import com.ssg.starroad.common.entity.BaseTimeEntity;
import com.ssg.starroad.user.enums.ActiveStatus;
import com.ssg.starroad.user.enums.Gender;
import com.ssg.starroad.user.enums.ProviderType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder(toBuilder = true)
// User 객체의 현재 상태를 기반으로 새로운 User 객체를 쉽게 생성
// 예를 들어, 특정 속성만 변경하고 나머지는 기존 값으로 유지하려는 경우,
// toBuilder() 메소드를 사용하여 빌더를 얻고, 변경할 속성만 설정한 다음 build() 메소드를 호출하면 됨
@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class User extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(updatable = false)
    // 사용자가 자신의 정보를 업데이트하는 동안,
    // 데이터베이스에서 식별자로 사용되는 id 값의 안정성을 보장
    private Long id;

    @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
    private String password; // bcrypt

    @Size(min = 2, max = 10, message = "이름은 2자 이상 10자 이하여야 합니다.")
    private String name;

    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\p{Punct}]{3,10}$",
            message = "닉네임은 3자 이상 10자 이하로, 영어, 한국어, 숫자, 특수문자를 포함해야 합니다.")
    @Column(nullable = false, unique = true)
    private String nickname;

    private String imagePath; // profile image

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birth;

    @Pattern(regexp = "^[0-9]{11}$", message = "전화번호는 하이픈 없이 11자리여야 합니다.")
    private String phone; // 하이픈 없이 11자리

    @Column(nullable = false, unique = true)
    @Email(message = "유효한 이메일 주소여야 합니다.")
    private String email;

    private ProviderType providerType; // 네이버, 카카오, 구글
    private String providerId; // (네이버, 카카오, 구글)에서 받아온 아이디

    private int reviewExp;
    private int point;

    @Enumerated(EnumType.STRING)
    private ActiveStatus activeStatus;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 모든 사용자에게 'ROLE_USER' 권한 부여
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        // 사용자의 아이디 반환 (고유한 값)
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료 여부 반환
        return true;
        // true 만료되지 않았음
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금 여부 반환
        return true;
        // true 잠금되지 않았음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드 만료 여부 반환
        return true;
        // true 만료되지 않았음
    }

    @Override
    public boolean isEnabled() {
        // activeStatus가 ACTIVE인 경우에만 true 반환
        return this.activeStatus == ActiveStatus.ACTIVE;
    }

}
