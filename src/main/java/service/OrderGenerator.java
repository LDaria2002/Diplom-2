package service;

import data.Orders;


public class OrderGenerator {
    public static Orders getListOrder() {

        return new Orders()
                .setIngredients(CreateOrderSteps.getAllIngredients()
                        .extract()
                        .path("data._id"));
    }
}