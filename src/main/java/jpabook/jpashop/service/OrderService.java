package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    // 주문 //
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        OrderItem orderItem = OrderItem.setOrderItem(item, item.getPrice(), count);

        Order order = Order.createOrder(member, delivery, orderItem);

        orderRepository.save(order); //cascade option때문에 delivery, orderitem 각각에 persist안해줘도 됨
        // order만 orderItem 사용하고 lifecycle 같기 때문에 cascade 쓰는거임
        // 두가지 조건 만족 안하고 자주 사용하는 곳이면 cascade 안써야됨
        return order.getId();
    }

    // 주문 취소 //
    @Transactional
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findOne(orderId);
        // entity에 핵심 비즈니스 로직이 있는것
        // 도메인 모델 패턴
        // 서비스는 위임만 하고 있음
        // <-> 서비스에 비즈니스 로직이 있는 것 : 트랜잭션 스크립트 패
        // JPA 쓰면 도메인 모델 패턴을 더 많이 쓰게 된다
        order.cancel();
    }
}
