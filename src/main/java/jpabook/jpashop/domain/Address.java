package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address { // 값 타입은 변경 불가능하게 설계해야 한다
    private String city;
    private String street;
    private String zipcode;

    protected Address(){
        // JPA 생성 때문에 만들어둔 생성자
        // 함부로 상속 해서는 안되는 객체
    }

    public Address(String city, String street, String zipcode){
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
