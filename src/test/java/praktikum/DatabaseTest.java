package praktikum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class DatabaseTest {

    private final String expectedBunName;
    private final float expectedBunPrice;
    private final IngredientType expectedIngredientType;
    private final String expectedIngredientName;
    private final float expectedIngredientPrice;

    public DatabaseTest(String expectedBunName, float expectedBunPrice,
                        IngredientType expectedIngredientType, String expectedIngredientName,
                        float expectedIngredientPrice) {
        this.expectedBunName = expectedBunName;
        this.expectedBunPrice = expectedBunPrice;
        this.expectedIngredientType = expectedIngredientType;
        this.expectedIngredientName = expectedIngredientName;
        this.expectedIngredientPrice = expectedIngredientPrice;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][] {
                {"black bun", 100.0f, IngredientType.SAUCE, "hot sauce", 100.0f},
                {"white bun", 200.0f, IngredientType.SAUCE, "sour cream", 200.0f},
                {"red bun", 300.0f, IngredientType.FILLING, "cutlet", 100.0f}
        });
    }

    @Test
    public void databaseContainsExpectedBuns() {
        Database database = new Database();
        List<Bun> buns = database.availableBuns();

        assertTrue(buns.size() >= 3);

        boolean found = false;
        for (Bun bun : buns) {
            if (bun.getName().equals(expectedBunName) &&
                    Math.abs(bun.getPrice() - expectedBunPrice) < 0.001) {
                found = true;
                break;
            }
        }

        assertTrue("Database should contain bun: " + expectedBunName, found);
    }

    @Test
    public void databaseContainsExpectedIngredients() {
        Database database = new Database();
        List<Ingredient> ingredients = database.availableIngredients();

        assertTrue(ingredients.size() >= 6);

        boolean found = false;
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getName().equals(expectedIngredientName) &&
                    ingredient.getType() == expectedIngredientType &&
                    Math.abs(ingredient.getPrice() - expectedIngredientPrice) < 0.001) {
                found = true;
                break;
            }
        }

        assertTrue("Database should contain ingredient: " + expectedIngredientName, found);
    }

    @Test
    public void availableBunsReturnsNonEmptyList() {
        Database database = new Database();
        List<Bun> buns = database.availableBuns();

        assertNotNull(buns);
        assertFalse(buns.isEmpty());
    }

    @Test
    public void availableIngredientsReturnsNonEmptyList() {
        Database database = new Database();
        List<Ingredient> ingredients = database.availableIngredients();

        assertNotNull(ingredients);
        assertFalse(ingredients.isEmpty());
    }
}