package orderPack;

import data.Order;
import orderPack.CreateOrderSteps;


public class OrderGenerator {
    public static Order getListOrder() {

        return new Order()
                .setIngredients(CreateOrderSteps.getAllIngredients()
                        .extract()
                        .path("data._id"));
    }
}