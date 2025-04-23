package org.example.delivery.domain.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCartItemRequest {
    //null, 0, 음수를 허용하지 않음
    @NotNull @Min(1)
    private Long menuId;    //메뉴 아이디

    @NotNull @Min(1)
    private int quantity;  //수량
}
