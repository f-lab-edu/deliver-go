package org.deliverygo.order.service;

import org.deliverygo.login.constants.UserGrade;
import org.deliverygo.login.entity.User;
import org.deliverygo.restaurant.entity.Restaurant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class OrderServiceTest {

    User owner;
    Restaurant restaurant;

    @BeforeEach
    void setUp() {
        owner = User.of("testPassword", "사용자", "3jg@gmail.com", "인천 서구",
            "01022223333", UserGrade.OWNER);
    }
    /**
     * 음식점이 운영중인지
     * 음식정보(음식점, 메뉴, 가격, 재고) + 사용자 정보(email) order table 에 저장
     * 결제 서비스에 API 호출, 성공 여부에 따라 Transaction 커밋/롤백
     * 성공 시, 카프카로 이벤트 전송, delivery 가 받아서 고객에게 push, 라이더에게도 push
     */

    @Test
    @DisplayName("음식점이 open이면 주문 성공")
    void test() {
        //given
        restaurant = Restaurant.ofOpen("프랩김밥점", "서울 용산", "01011112222", owner);

        // orderService.order();

        // order
        //then
        Assertions.assertEquals(Boolean.TRUE, restaurant.isOpen());
    }

    @Test
    @DisplayName("음식점이 close 이면 주문 예외")
    void test2() {
        //given
        restaurant = Restaurant.ofClose("프랩김밥점", "서울 용산", "01011112222", owner);

        //when

        //then
        Assertions.assertEquals(Boolean.FALSE, restaurant.isOpen());
    }


}
