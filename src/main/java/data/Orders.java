package data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    private List<String> ingredients;

    public Orders setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
        return this;
    }
}
