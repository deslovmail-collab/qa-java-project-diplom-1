package praktikum;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class BurgerTest {

    private Burger burger;
    private final String bunName;
    private final float bunPrice;
    private final IngredientType ingredientType;
    private final String ingredientName;
    private final float ingredientPrice;

    public BurgerTest(String bunName, float bunPrice,
                      IngredientType ingredientType, String ingredientName, float ingredientPrice) {
        this.bunName = bunName;
        this.bunPrice = bunPrice;
        this.ingredientType = ingredientType;
        this.ingredientName = ingredientName;
        this.ingredientPrice = ingredientPrice;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][] {
                {"black bun", 100.0f, IngredientType.SAUCE, "hot sauce", 50.0f},
                {"white bun", 200.0f, IngredientType.FILLING, "cutlet", 150.0f},
                {"red bun", 300.0f, IngredientType.SAUCE, "chili sauce", 80.0f}
        });
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        burger = new Burger();
    }

    @Test
    public void setBunsSetsBunCorrectly() {
        Bun bun = new Bun(bunName, bunPrice);

        burger.setBuns(bun);

        assertNotNull(burger.bun);
        assertEquals(bunName, burger.bun.getName());
    }

    @Test
    public void setBunsSetsCorrectBunPrice() {
        Bun bun = new Bun(bunName, bunPrice);

        burger.setBuns(bun);

        assertEquals(bunPrice, burger.bun.getPrice(), 0.001);
    }

    @Test
    public void addIngredientIncreasesIngredientsListSize() {
        Ingredient ingredient = new Ingredient(ingredientType, ingredientName, ingredientPrice);

        burger.addIngredient(ingredient);

        assertEquals(1, burger.ingredients.size());
    }

    @Test
    public void addIngredientAddsCorrectIngredientName() {
        Ingredient ingredient = new Ingredient(ingredientType, ingredientName, ingredientPrice);

        burger.addIngredient(ingredient);

        assertEquals(ingredientName, burger.ingredients.get(0).getName());
    }

    @Test
    public void addIngredientAddsCorrectIngredientPrice() {
        Ingredient ingredient = new Ingredient(ingredientType, ingredientName, ingredientPrice);

        burger.addIngredient(ingredient);

        assertEquals(ingredientPrice, burger.ingredients.get(0).getPrice(), 0.001);
    }

    @Test
    public void addIngredientAddsCorrectIngredientType() {
        Ingredient ingredient = new Ingredient(ingredientType, ingredientName, ingredientPrice);

        burger.addIngredient(ingredient);

        assertEquals(ingredientType, burger.ingredients.get(0).getType());
    }

    @Test
    public void removeIngredientDecreasesIngredientsListSize() {
        Ingredient ingredient1 = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        Ingredient ingredient2 = new Ingredient(IngredientType.FILLING, "cutlet", 200.0f);

        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        burger.removeIngredient(0);

        assertEquals(1, burger.ingredients.size());
    }

    @Test
    public void removeIngredientRemovesCorrectIngredient() {
        Ingredient ingredient1 = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        Ingredient ingredient2 = new Ingredient(IngredientType.FILLING, "cutlet", 200.0f);

        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        burger.removeIngredient(0);

        assertEquals("cutlet", burger.ingredients.get(0).getName());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeIngredientWithInvalidIndexThrowsException() {
        Ingredient ingredient = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        burger.addIngredient(ingredient);
        burger.removeIngredient(5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeIngredientFromEmptyListThrowsException() {
        burger.removeIngredient(0);
    }

    @Test
    public void moveIngredientChangesPositionCorrectly() {
        Ingredient ingredient1 = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        Ingredient ingredient2 = new Ingredient(IngredientType.FILLING, "cutlet", 200.0f);
        Ingredient ingredient3 = new Ingredient(IngredientType.SAUCE, "sour cream", 50.0f);

        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        burger.addIngredient(ingredient3);

        burger.moveIngredient(0, 2);

        assertEquals("cutlet", burger.ingredients.get(0).getName());
    }

    @Test
    public void moveIngredientPreservesAllIngredients() {
        Ingredient ingredient1 = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        Ingredient ingredient2 = new Ingredient(IngredientType.FILLING, "cutlet", 200.0f);
        Ingredient ingredient3 = new Ingredient(IngredientType.SAUCE, "sour cream", 50.0f);

        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        burger.addIngredient(ingredient3);

        burger.moveIngredient(0, 2);

        assertEquals(3, burger.ingredients.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void moveIngredientWithInvalidSourceIndexThrowsException() {
        Ingredient ingredient = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        burger.addIngredient(ingredient);
        burger.moveIngredient(5, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void moveIngredientWithInvalidTargetIndexThrowsException() {
        Ingredient ingredient = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        burger.addIngredient(ingredient);
        burger.moveIngredient(0, 5);
    }

    @Test
    public void getPriceReturnsCorrectValue() {
        Bun bun = new Bun(bunName, bunPrice);
        Ingredient ingredient = new Ingredient(ingredientType, ingredientName, ingredientPrice);
        Ingredient additionalIngredient = new Ingredient(IngredientType.SAUCE, "sour cream", 70.0f);

        burger.setBuns(bun);
        burger.addIngredient(ingredient);
        burger.addIngredient(additionalIngredient);

        float expectedPrice = (bunPrice * 2) + ingredientPrice + 70.0f;

        assertEquals(expectedPrice, burger.getPrice(), 0.001);
    }

    @Test(expected = NullPointerException.class)
    public void getPriceWithoutBunThrowsException() {
        Ingredient ingredient = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        burger.addIngredient(ingredient);
        burger.getPrice();
    }

    @Test
    public void getReceiptContainsBunName() {
        Bun bun = new Bun(bunName, bunPrice);
        Ingredient ingredient = new Ingredient(ingredientType, ingredientName, ingredientPrice);

        burger.setBuns(bun);
        burger.addIngredient(ingredient);

        String receipt = burger.getReceipt();

        assertTrue(receipt.contains(bunName));
    }

    @Test
    public void getReceiptContainsIngredientName() {
        Bun bun = new Bun(bunName, bunPrice);
        Ingredient ingredient = new Ingredient(ingredientType, ingredientName, ingredientPrice);

        burger.setBuns(bun);
        burger.addIngredient(ingredient);

        String receipt = burger.getReceipt();

        assertTrue(receipt.contains(ingredientName));
    }

    @Test
    public void getReceiptContainsPrice() {
        Bun bun = new Bun(bunName, bunPrice);
        Ingredient ingredient = new Ingredient(ingredientType, ingredientName, ingredientPrice);

        burger.setBuns(bun);
        burger.addIngredient(ingredient);

        String receipt = burger.getReceipt();

        assertTrue(receipt.contains("Price:"));
    }

    @Test
    public void getReceiptContainsIngredientTypeInLowerCase() {
        Bun bun = new Bun(bunName, bunPrice);
        Ingredient ingredient = new Ingredient(ingredientType, ingredientName, ingredientPrice);

        burger.setBuns(bun);
        burger.addIngredient(ingredient);

        String receipt = burger.getReceipt();

        assertTrue(receipt.contains(ingredientType.toString().toLowerCase()));
    }

    @Test(expected = NullPointerException.class)
    public void getReceiptWithoutBunThrowsException() {
        Ingredient ingredient = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        burger.addIngredient(ingredient);
        burger.getReceipt();
    }

    @Test
    public void getReceiptWithEmptyIngredientsContainsBunNameTwice() {
        Bun bun = new Bun(bunName, bunPrice);
        burger.setBuns(bun);

        String receipt = burger.getReceipt();
        String bunLine = String.format("(==== %s ====)", bunName);
        int firstIndex = receipt.indexOf(bunLine);
        int lastIndex = receipt.lastIndexOf(bunLine);

        assertTrue(firstIndex != -1 && lastIndex != -1 && firstIndex != lastIndex);
    }
}