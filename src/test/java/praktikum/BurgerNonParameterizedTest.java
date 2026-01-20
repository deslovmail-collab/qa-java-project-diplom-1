package praktikum;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BurgerNonParameterizedTest {

    private Burger burger;

    @Mock
    private Bun bunMock;

    @Mock
    private Ingredient ingredientMock;

    @Mock
    private Database databaseMock;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        burger = new Burger();
    }

    @Test
    public void setBunsWithMockSetsBun() {
        burger.setBuns(bunMock);

        assertNotNull(burger.bun);
        assertEquals(bunMock, burger.bun);
    }

    @Test
    public void getPriceWithMocksReturnsCorrectValue() {
        when(bunMock.getPrice()).thenReturn(100.0f);
        when(ingredientMock.getPrice()).thenReturn(50.0f);

        burger.setBuns(bunMock);
        burger.addIngredient(ingredientMock);

        float expectedPrice = (100.0f * 2) + 50.0f;
        float actualPrice = burger.getPrice();

        assertEquals(expectedPrice, actualPrice, 0.001);
    }

    @Test
    public void getReceiptWithMocksReturnsFormattedString() {
        when(bunMock.getName()).thenReturn("Test Bun");
        when(bunMock.getPrice()).thenReturn(100.0f);
        when(ingredientMock.getType()).thenReturn(IngredientType.SAUCE);
        when(ingredientMock.getName()).thenReturn("Test Sauce");
        when(ingredientMock.getPrice()).thenReturn(50.0f);

        burger.setBuns(bunMock);
        burger.addIngredient(ingredientMock);

        String receipt = burger.getReceipt();

        assertTrue(receipt.contains("Test Bun"));
        assertTrue(receipt.contains("Test Sauce"));
        assertTrue(receipt.contains("sauce"));
        assertTrue(receipt.contains("Price:"));
    }

    @Test
    public void addMultipleIngredientsAndRemoveSome() {
        Ingredient ingredient1 = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        Ingredient ingredient2 = new Ingredient(IngredientType.FILLING, "cutlet", 200.0f);
        Ingredient ingredient3 = new Ingredient(IngredientType.SAUCE, "sour cream", 50.0f);

        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        burger.addIngredient(ingredient3);

        assertEquals(3, burger.ingredients.size());

        burger.removeIngredient(1);

        assertEquals(2, burger.ingredients.size());
        assertEquals("hot sauce", burger.ingredients.get(0).getName());
        assertEquals("sour cream", burger.ingredients.get(1).getName());
    }

    @Test
    public void moveIngredientToDifferentPosition() {
        Ingredient ingredient1 = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        Ingredient ingredient2 = new Ingredient(IngredientType.FILLING, "cutlet", 200.0f);
        Ingredient ingredient3 = new Ingredient(IngredientType.SAUCE, "sour cream", 50.0f);

        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        burger.addIngredient(ingredient3);

        burger.moveIngredient(0, 1);

        assertEquals("cutlet", burger.ingredients.get(0).getName());
        assertEquals("hot sauce", burger.ingredients.get(1).getName());
        assertEquals("sour cream", burger.ingredients.get(2).getName());
    }

    @Test
    public void burgerReceiptFormatIsCorrect() {
        Bun bun = new Bun("black bun", 100);
        Ingredient sauce = new Ingredient(IngredientType.SAUCE, "hot sauce", 100);
        Ingredient filling = new Ingredient(IngredientType.FILLING, "cutlet", 200);

        burger.setBuns(bun);
        burger.addIngredient(sauce);
        burger.addIngredient(filling);

        String receipt = burger.getReceipt();

        // Проверяем основные части рецепта (более гибкая проверка)
        assertTrue("Receipt should contain bun name", receipt.contains("black bun"));
        assertTrue("Receipt should contain hot sauce", receipt.contains("hot sauce"));
        assertTrue("Receipt should contain cutlet", receipt.contains("cutlet"));
        assertTrue("Receipt should contain sauce type", receipt.contains("sauce"));
        assertTrue("Receipt should contain filling type", receipt.contains("filling"));
        assertTrue("Receipt should contain price", receipt.contains("Price:"));
        assertTrue("Receipt should contain total price 500", receipt.contains("500"));

        // Проверяем структуру рецепта
        String[] lines = receipt.split("\n");
        assertTrue("First line should contain bun", lines[0].contains("black bun"));
        assertTrue("Second line should contain sauce", lines[1].contains("sauce hot sauce"));
        assertTrue("Third line should contain filling", lines[2].contains("filling cutlet"));
        assertTrue("Fourth line should contain bun again", lines[3].contains("black bun"));
        assertTrue("Last line should contain price", lines[lines.length - 1].contains("Price:"));
    }

    @Test
    public void databaseStubReturnsCorrectData() {
        Bun testBun = new Bun("Test Bun", 150.0f);
        Ingredient testIngredient = new Ingredient(IngredientType.SAUCE, "Test Sauce", 50.0f);

        when(databaseMock.availableBuns()).thenReturn(Arrays.asList(testBun));
        when(databaseMock.availableIngredients()).thenReturn(Arrays.asList(testIngredient));

        assertEquals(1, databaseMock.availableBuns().size());
        assertEquals("Test Bun", databaseMock.availableBuns().get(0).getName());
        assertEquals(1, databaseMock.availableIngredients().size());
        assertEquals("Test Sauce", databaseMock.availableIngredients().get(0).getName());
    }

    @Test
    public void ingredientOrderIsPreservedAfterMultipleOperations() {
        Ingredient ingredient1 = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        Ingredient ingredient2 = new Ingredient(IngredientType.FILLING, "cutlet", 200.0f);
        Ingredient ingredient3 = new Ingredient(IngredientType.SAUCE, "sour cream", 50.0f);

        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        burger.addIngredient(ingredient3);

        // Перемещаем sour cream на позицию 0
        burger.moveIngredient(2, 0);
        // Удаляем cutlet (которая теперь на позиции 2 после перемещения sour cream)
        burger.removeIngredient(2);

        assertEquals(2, burger.ingredients.size());
        assertEquals("sour cream", burger.ingredients.get(0).getName());
        assertEquals("hot sauce", burger.ingredients.get(1).getName());
    }

    @Test
    public void receiptContainsCorrectPriceFormat() {
        Bun bun = new Bun("white bun", 200.0f);
        Ingredient ingredient = new Ingredient(IngredientType.FILLING, "cutlet", 150.0f);

        burger.setBuns(bun);
        burger.addIngredient(ingredient);

        String receipt = burger.getReceipt();

        // Находим строку с ценой
        String[] lines = receipt.split("\n");
        String priceLine = "";
        for (String line : lines) {
            if (line.startsWith("Price:")) {
                priceLine = line;
                break;
            }
        }

        assertTrue("Price line should start with 'Price:'", priceLine.startsWith("Price:"));
        assertTrue("Price should contain 550", priceLine.contains("550"));
    }

    @Test
    public void burgerReceiptHasCorrectStructure() {
        Bun bun = new Bun("test bun", 100.0f);
        burger.setBuns(bun);

        String receipt = burger.getReceipt();

        // Проверяем, что в рецепте есть переносы строк
        assertTrue("Receipt should contain newlines", receipt.contains("\n"));

        // Проверяем, что цена выводится в правильном формате
        String[] lines = receipt.split("\n");

        // Проверяем, что последняя строка содержит цену
        String lastLine = lines[lines.length - 1];
        assertTrue("Last line should contain 'Price:'", lastLine.startsWith("Price:"));

        // Проверяем, что после "Price:" идет какое-то значение
        String pricePart = lastLine.substring("Price:".length()).trim();
        assertFalse("Price part should not be empty", pricePart.isEmpty());

        // Проверяем, что значение содержит цифры
        assertTrue("Price should contain digits", pricePart.matches(".*\\d.*"));
    }

    @Test
    public void getReceiptWithEmptyIngredientsContainsBunLines() {
        Bun bun = new Bun("test bun", 100.0f);
        burger.setBuns(bun);

        String receipt = burger.getReceipt();

        // Должно быть 2 строки с названием булочки
        int bunLineCount = 0;
        String bunLine = "(==== test bun ====)";
        String[] lines = receipt.split("\n");

        for (String line : lines) {
            if (line.contains(bunLine)) {
                bunLineCount++;
            }
        }

        assertEquals("Bun should appear twice in receipt", 2, bunLineCount);
    }
}