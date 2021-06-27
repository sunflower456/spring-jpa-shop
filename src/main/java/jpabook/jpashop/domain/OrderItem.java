package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice;
    private int count;

    protected OrderItem(){
        // 생성 메서드로만 orderitem을 생성하고 new OrderItem() 사용하지 말라는 뜻
    }

    // 생성 메서드 //
    public static OrderItem setOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); // 재고를 까주는 로직
        return orderItem;
    }

    // 비즈니스 로직 //
    public void cancel() {
        getItem().addStock(count);
    }

    // 조회 로직 //
    public int getTotalPrice(){
        return orderPrice * count;
    }
}
