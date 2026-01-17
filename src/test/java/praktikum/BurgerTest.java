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

    // Параметры для тестов
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
        // Инициализация Mockito
        MockitoAnnotations.openMocks(this);
        burger = new Burger();
    }

    @Test
    public void testSetBuns() {
        // Создание реального объекта Bun
        Bun bun = new Bun(bunName, bunPrice);

        burger.setBuns(bun);

        assertNotNull("После установки значение булочки не должно быть нулевым", burger.bun);
        assertEquals("Название булочки должно совпадать", bunName, burger.bun.getName());
        assertEquals("Цена булочки должна соответствовать", bunPrice, burger.bun.getPrice(), 0.001);
    }

    @Test
    public void testAddIngredient() {
        // Создание реального объекта Ingredient
        Ingredient ingredient = new Ingredient(ingredientType, ingredientName, ingredientPrice);

        burger.addIngredient(ingredient);

        assertEquals("В списке ингредиентов должен быть 1 пункт", 1, burger.ingredients.size());
        assertEquals("Название ингредиента должно совпадать", ingredientName, burger.ingredients.get(0).getName());
        assertEquals("Цена ингредиента должна соответствовать", ingredientPrice, burger.ingredients.get(0).getPrice(), 0.001);
        assertEquals("Тип ингредиента должен совпадать", ingredientType, burger.ingredients.get(0).getType());
    }

    @Test
    public void testRemoveIngredient() {
        Ingredient ingredient1 = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        Ingredient ingredient2 = new Ingredient(IngredientType.FILLING, "cutlet", 200.0f);

        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        assertEquals("Перед удалением необходимо добавить 2 ингредиента", 2, burger.ingredients.size());

        burger.removeIngredient(0);

        assertEquals("После удаления должен остаться 1 ингредиент", 1, burger.ingredients.size());
        assertEquals("Оставшимся ингредиентом должен быть ингредиент № 2", ingredient2.getName(), burger.ingredients.get(0).getName());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIngredientWithInvalidIndex() {
        Ingredient ingredient = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        burger.addIngredient(ingredient);
        burger.removeIngredient(5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIngredientFromEmptyList() {
        burger.removeIngredient(0);
    }

    @Test
    public void testMoveIngredient() {
        Ingredient ingredient1 = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        Ingredient ingredient2 = new Ingredient(IngredientType.FILLING, "cutlet", 200.0f);
        Ingredient ingredient3 = new Ingredient(IngredientType.SAUCE, "sour cream", 50.0f);

        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        burger.addIngredient(ingredient3);

        // Проверка начального порядка
        assertEquals("Первым ингредиентом должен быть ингредиент1", ingredient1.getName(), burger.ingredients.get(0).getName());
        assertEquals("Вторым ингредиентом должен быть ингредиент2", ingredient2.getName(), burger.ingredients.get(1).getName());
        assertEquals("Третьим ингредиентом должен быть ингредиент3", ingredient3.getName(), burger.ingredients.get(2).getName());

        burger.moveIngredient(0, 2); // Перемещаем первый в конец

        // Проверка нового порядка
        assertEquals("Первым ингредиентом должен быть ингредиент2", ingredient2.getName(), burger.ingredients.get(0).getName());
        assertEquals("Вторым ингредиентом должен быть ингредиент3", ingredient3.getName(), burger.ingredients.get(1).getName());
        assertEquals("Третьим ингредиентом должен быть ингредиент1", ingredient1.getName(), burger.ingredients.get(2).getName());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientWithInvalidSourceIndex() {
        Ingredient ingredient = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        burger.addIngredient(ingredient);
        burger.moveIngredient(5, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientWithInvalidTargetIndex() {
        Ingredient ingredient = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        burger.addIngredient(ingredient);
        burger.moveIngredient(0, 5);
    }

    @Test
    public void testGetPrice() {
        Bun bun = new Bun(bunName, bunPrice);
        Ingredient ingredient1 = new Ingredient(ingredientType, ingredientName, ingredientPrice);
        Ingredient ingredient2 = new Ingredient(IngredientType.SAUCE, "sour cream", 70.0f);

        burger.setBuns(bun);
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        float expectedPrice = (bunPrice * 2) + ingredientPrice + 70.0f;
        float actualPrice = burger.getPrice();

        assertEquals("Расчет цены должен быть правильным", expectedPrice, actualPrice, 0.001);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPriceWithoutBun() {
        Ingredient ingredient = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        burger.addIngredient(ingredient);
        burger.getPrice(); // Должен бросить NPE
    }

    @Test
    public void testGetReceipt() {
        Bun bun = new Bun(bunName, bunPrice);
        Ingredient ingredient1 = new Ingredient(ingredientType, ingredientName, ingredientPrice);
        Ingredient ingredient2 = new Ingredient(IngredientType.SAUCE, "sour cream", 70.0f);

        burger.setBuns(bun);
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        String receipt = burger.getReceipt();

        assertNotNull("Receipt should not be null", receipt);

        // Проверка содержимого чека
        assertTrue("В чеке должно быть указано название булочки", receipt.contains(bunName));
        assertTrue("В рецепте должно быть указано название первого ингредиента", receipt.contains(ingredientName));
        assertTrue("В рецепте должно быть указано второе название ингредиента", receipt.contains("sour cream"));
        assertTrue("На квитанции должна быть указана цена", receipt.contains("Price:"));
        assertTrue("В рецепте ингредиенты должны быть указаны строчными буквами",
                receipt.contains(ingredientType.toString().toLowerCase()));

        // Проверка формата
        String expectedBunLine = String.format("(==== %s ====)", bunName);
        assertTrue("Квитанция должна начинаться с номера", receipt.contains(expectedBunLine));
    }

    @Test(expected = NullPointerException.class)
    public void testGetReceiptWithoutBun() {
        Ingredient ingredient = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        burger.addIngredient(ingredient);
        burger.getReceipt();
    }

    @Test
    public void testGetReceiptWithEmptyIngredients() {
        Bun bun = new Bun(bunName, bunPrice);
        burger.setBuns(bun);

        String receipt = burger.getReceipt();

        assertNotNull("Receipt should not be null", receipt);
        assertTrue("Receipt should contain bun name", receipt.contains(bunName));
        assertTrue("Receipt should contain price", receipt.contains("Price:"));

        // Проверка, что булочка упоминается дважды
        String bunLine = String.format("(==== %s ====)", bunName);
        int firstIndex = receipt.indexOf(bunLine);
        int lastIndex = receipt.lastIndexOf(bunLine);
        assertTrue("Имя получателя должно быть указано в квитанции дважды", firstIndex != -1 && lastIndex != -1 && firstIndex != lastIndex);
    }

    @Test
    public void testMultipleAddAndRemoveOperations() {
        Bun bun = new Bun(bunName, bunPrice);
        burger.setBuns(bun);

        Ingredient ingredient = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);

        // Добавление 10 раз
        for (int i = 0; i < 10; i++) {
            burger.addIngredient(ingredient);
        }

        assertEquals("Should have 10 ingredients", 10, burger.ingredients.size());

        // Удаление каждого второго
        for (int i = 9; i >= 0; i -= 2) {
            burger.removeIngredient(i);
        }

        assertEquals("После удаления должно остаться 5 ингредиентов", 5, burger.ingredients.size());
    }

    @Test
    public void testIngredientOrderPreservation() {
        Bun bun = new Bun(bunName, bunPrice);
        burger.setBuns(bun);

        Ingredient ingredient1 = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        Ingredient ingredient2 = new Ingredient(IngredientType.FILLING, "cutlet", 200.0f);
        Ingredient ingredient3 = new Ingredient(IngredientType.SAUCE, "sour cream", 50.0f);

        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        burger.addIngredient(ingredient3);

        List<Ingredient> ingredients = burger.ingredients;

        assertEquals("Should have 3 ingredients", 3, ingredients.size());
        assertEquals("First ingredient should match", ingredient1.getName(), ingredients.get(0).getName());
        assertEquals("Second ingredient should match", ingredient2.getName(), ingredients.get(1).getName());
        assertEquals("Third ingredient should match", ingredient3.getName(), ingredients.get(2).getName());
    }

    @Test
    public void testReceiptFormatWithDifferentIngredientTypes() {
        Bun bun = new Bun(bunName, bunPrice);
        burger.setBuns(bun);

        Ingredient sauce = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        Ingredient filling = new Ingredient(IngredientType.FILLING, "cutlet", 200.0f);

        burger.addIngredient(sauce);
        burger.addIngredient(filling);

        String receipt = burger.getReceipt();

        // Проверка, что типы выводятся в нижнем регистре
        String receiptLower = receipt.toLowerCase();
        assertTrue("Receipt should contain 'sauce'", receiptLower.contains("sauce"));
        assertTrue("Receipt should contain 'filling'", receiptLower.contains("filling"));

        // Проверка, что имена ингредиентов присутствуют
        assertTrue("Receipt should contain 'hot sauce'", receipt.contains("hot sauce"));
        assertTrue("Receipt should contain 'cutlet'", receipt.contains("cutlet"));
    }

    @Test
    public void testReceiptPriceFormat() {
        Bun bun = new Bun(bunName, bunPrice);
        Ingredient ingredient = new Ingredient(ingredientType, ingredientName, ingredientPrice);

        burger.setBuns(bun);
        burger.addIngredient(ingredient);

        String receipt = burger.getReceipt();

        // Проверка формата цены
        assertTrue("Receipt should contain 'Price:'", receipt.contains("Price:"));

        // Проверка, что после "Price:" есть число
        String priceLine = receipt.substring(receipt.indexOf("Price:"));
        String pricePart = priceLine.substring("Price:".length()).trim().split("\\r?\\n")[0];
        assertFalse("Price part should not be empty", pricePart.isEmpty());
        assertTrue("Price should contain digits", pricePart.matches(".*[0-9].*"));
    }

    @Test
    public void testBurgerWithSpy() {
        // Создание spy для Burger
        Burger spyBurger = spy(new Burger());

        Bun bun = new Bun(bunName, bunPrice);
        Ingredient ingredient = new Ingredient(ingredientType, ingredientName, ingredientPrice);

        spyBurger.setBuns(bun);
        spyBurger.addIngredient(ingredient);

        verify(spyBurger).setBuns(bun);
        verify(spyBurger).addIngredient(ingredient);

        doReturn(999.99f).when(spyBurger).getPrice();

        assertEquals(999.99f, spyBurger.getPrice(), 0.001);
    }

    @Test
    public void testBurgerWithDatabaseStub() {
        // Создание заглушки для Database
        Database databaseStub = mock(Database.class);

        Bun bun = new Bun(bunName, bunPrice);
        Ingredient ingredient = new Ingredient(ingredientType, ingredientName, ingredientPrice);

        // Настройка стаба
        when(databaseStub.availableBuns()).thenReturn(Arrays.asList(bun));
        when(databaseStub.availableIngredients()).thenReturn(Arrays.asList(ingredient));

        // Проверка, что стаб возвращает правильные данные
        assertEquals(1, databaseStub.availableBuns().size());
        assertEquals(bunName, databaseStub.availableBuns().get(0).getName());
        assertEquals(1, databaseStub.availableIngredients().size());
        assertEquals(ingredientName, databaseStub.availableIngredients().get(0).getName());
    }

    @Test
    public void testParameterizedWithMock() {

        // Создание реального объекта для теста
        Bun bun1 = new Bun("Test Bun", 150.0f);
        Bun bun2 = new Bun("Another Bun", 250.0f);

        burger.setBuns(bun1);
        assertEquals("Test Bun", burger.bun.getName());

        burger.setBuns(bun2);
        assertEquals("Another Bun", burger.bun.getName());
    }
}