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
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
@Embeddable
@Setter
@ToString
public class User extends BaseTimeEntity implements UserDetails, OAuth2User {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(updatable = false)
    // 사용자가 자신의 정보를 업데이트하는 동안,
    // 데이터베이스에서 식별자로 사용되는 id 값의 안정성을 보장
    private Long id;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W).{8,}$",
            message = "비밀번호는 8자 이상이며, 숫자, 문자, 특수문자를 각각 하나 이상 포함해야 합니다.")
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

    @Enumerated(EnumType.STRING)
    private ProviderType providerType; // 네이버, 카카오, 구글
    private String providerId; // (네이버, 카카오, 구글)에서 받아온 아이디

    private int reviewExp;
    private int point;

    @Enumerated(EnumType.STRING)
    private ActiveStatus activeStatus;


    // OAuth2User 메소드

    @Override
    public String getName() {
        return this.providerId; // providerId를 반환
    }

    public String getUserName() {
        return this.name;
    }

    // 제네릭 타입 A를 사용하여, OAuth2User 인터페이스에서
    // 다양한 타입의 사용자 속성을 안전하게 가져오도록 설계(필수)
    // String email = user.getAttribute("email")과 같이 사용할 때
    // 컴파일러가 반환 타입이 String임을 확인하고, 다른 타입으로 잘못 캐스팅하는 실수를 방지
    // 또 name 파라미터는 OAuth2User의 속성 중 하나를 지정하는 문자열
    // email, name, nickname 등과 같이 실제 속성 이름을 문자열로 전달받아 해당 속성 값을 반환
    @Override
    public <A> A getAttribute(String name) {
        switch (name) {
            case "email":
                return (A) this.email;
            case "name":
                return (A) this.name;
            case "nickname":
                return (A) this.nickname;
            case "imagePath":
                return (A) this.imagePath;
            default:
                return null;  // 해당하는 속성이 없을 경우 null 반환
        }
    }

    // 메소드는 속성 맵 전체를 반환
    @Override
    public Map<String, Object> getAttributes() {
        // 실제 사용 시 필요한 모든 속성을 포함한 Map을 반환
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", this.email);
        attributes.put("name", this.name);
        attributes.put("nickname", this.nickname);
        attributes.put("imagePath", this.imagePath);
        attributes.put("providerType", this.providerType != null ? this.providerType.name() : null);
        attributes.put("providerId", this.providerId);
        return attributes;
    }

    // UserDetails 메소드
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
    // 로그인을 할 수 있는 전제가 회원의 상태가 active여야 함
    // 이때 true를 반환

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    // 닉네임, 비밀번호를 변경하는 메소드 추가
    public void updateProfile(String nickname, String password) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (password != null) {
            this.password = password;
        }
    }

    public void changeActiveStatus(ActiveStatus newStatus) {
        this.activeStatus = newStatus;
    }

    public void setProfileimgPath(String path) {
        this.imagePath = path;

    }
}
