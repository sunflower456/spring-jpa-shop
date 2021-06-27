package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockExcpetion;
import jpabook.jpashop.repository.OrderRepository;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        Member member = new Member();
        member.setName("kim");
        member.setAddress(new Address("seoul", "sanbon", "123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("JPA");
        book.setStockQuantity(10);
        book.setPrice(10000);
        em.persist(book);

        int orderCount = 2;
        Long order = orderService.order(member.getId(), book.getId(), orderCount);
        Order one = orderRepository.findOne(order);

        assertEquals(OrderStatus.ORDER, one.getStatus());
        assertEquals(1, one.getOrderItems().size());
        assertEquals(10000*orderCount, one.getTotalPrice());
        assertEquals(8, book.getStockQuantity());

    }

    @Test
    public void 주문취소() throws Exception{
        Member member = new Member();
        member.setName("kim");
        member.setAddress(new Address("seoul", "sanbon", "123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("JPA");
        book.setStockQuantity(10);
        book.setPrice(10000);
        em.persist(book);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        orderService.cancelOrder(orderId);

        Order order = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, order.getStatus());
        assertEquals(10, book.getStockQuantity());
    }

    @Test
    public void 재고수량초과() throws Exception {
        Member member = new Member();
        member.setName("kim");
        member.setAddress(new Address("seoul", "sanbon", "123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("JPA");
        book.setStockQuantity(10);
        book.setPrice(10000);
        em.persist(book);

        int orderCount = 11;

        try{
            orderService.order(member.getId(), book.getId(), orderCount);
        }catch(NotEnoughStockExcpetion e){
            return;
        }


        fail("여기까지 오면 안됨");
    }
}
