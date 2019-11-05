package com.example.rxjavastu;

public class Flower {
    private int order_id;
    private int age;
    private String full_desc;
    private int flower_id;
    private int user_id;
    private int price;
    private String url;

    @Override
    public String toString() {
        return "Flower{" +
                "order_id=" + order_id +
                ", age=" + age +
                ", full_desc='" + full_desc + '\'' +
                ", flower_id=" + flower_id +
                ", user_id=" + user_id +
                ", price=" + price +
                ", url='" + url + '\'' +
                '}';
    }
}
