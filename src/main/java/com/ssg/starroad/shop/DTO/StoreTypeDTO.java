package com.ssg.starroad.shop.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class StoreTypeDTO {
@JsonProperty("name")
    private String name;

}
