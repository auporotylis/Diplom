package praktikum;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class BurgerTest {

    private Burger burger;

    private int indexOld;
    private int indexNew;
    private int firstElementAfterMove;
    private int secondElementAfterMove;
    private int thirdElementAfterMove;

    public BurgerTest (int indexOld, int indexNew, int firstElementAfterMove, int secondElementAfterMove, int thirdElementAfterMove) {
        this.indexOld = indexOld;
        this.indexNew = indexNew;
        this.firstElementAfterMove = firstElementAfterMove;
        this.secondElementAfterMove = secondElementAfterMove;
        this.thirdElementAfterMove = thirdElementAfterMove;

    }

    @Parameterized.Parameters(name = "Индексы: старый: {0}, новый :{1}; Новый порядок в списке: {2},{3},{4}")
    public static Object[][] testData() {
        return new Object[][]{
                {0, 2, 1, 2, 0},
                {2, 0, 2, 0, 1},
                {0, 0, 0, 1, 2},
                {1, 1, 0, 1, 2},
        };
    }

    @Before
    public void setUp() {
        burger = new Burger();
    }

    @After
    public void tearDown() {
        burger = null;
    }

    @Test
    @Description("Проверка установки булки в бургер")
    public void setBunsShouldSetBunTest() {
        Bun bun =  Mockito.mock(Bun.class);
        when(bun.getName()).thenReturn("S kunzhutom");
        burger.setBuns(bun);
        assertEquals("Булка " + bun.getName() + " не установилась в бургер (установлена " + burger.bun.getName() + ")",
                bun, burger.bun);
    }

    @Test
    @Description("Проверка добавления ингредиентов в бургер")
    public void addIngredientShouldAddIngredientInBurgerTest() {
        Ingredient ingredient = mock(Ingredient.class);
        when(ingredient.getName()).thenReturn("kepchuk");
        burger.addIngredient(ingredient);
        assertEquals("Добавленный ингредиент не совпадает (" + burger.ingredients.get(0).getName() +
                        " вместо " + ingredient.getName() + ")",
                ingredient, burger.ingredients.get(0));
    }

    @Test
    @Description("Проверка удаления ингредиентов из бургера")
    public void removeIngredientShouldDeleteIngredientFromBurgerTest() {
        Ingredient firstIngredient = mock(Ingredient.class);
        Ingredient secondIngredient = mock(Ingredient.class);

        burger.addIngredient(firstIngredient);
        burger.addIngredient(secondIngredient);
        burger.removeIngredient(0);
        assertEquals("В списке ингредиентов должен был остаться " + secondIngredient.getName() +
                        " вместо " + burger.ingredients.get(0).getName(),
                secondIngredient, burger.ingredients.get(0));
    }

    @Test
    @Description("Проверка изменения порядка ингредиентов в бургере")
    public void moveIngredientShouldMoveIngredientsInBurgerTest() {
        Ingredient firstIngredient = mock(Ingredient.class);
        Ingredient secondIngredient = mock(Ingredient.class);
        Ingredient thirdIngredient = mock(Ingredient.class);

        when(firstIngredient.getName()).thenReturn("kepchuk");
        when(secondIngredient.getName()).thenReturn("kotleta");
        when(thirdIngredient.getName()).thenReturn("mazik");

        List<Ingredient> expectedList = Arrays.asList(firstIngredient, secondIngredient, thirdIngredient);

        burger.addIngredient(firstIngredient);
        burger.addIngredient(secondIngredient);
        burger.addIngredient(thirdIngredient);

        burger.moveIngredient(indexOld, indexNew);
        assertEquals("Ожидался порядок: " + expectedList.get(firstElementAfterMove).getName() + ", " +
                        expectedList.get(secondElementAfterMove).getName() + ", " +
                        expectedList.get(thirdElementAfterMove).getName() +
                        " фактический порядок " + burger.ingredients.get(0).getName() + ", " +
                        burger.ingredients.get(1).getName() + ", " +
                        burger.ingredients.get(2).getName() + ", ",
                Arrays.asList(expectedList.get(firstElementAfterMove).getName(),
                        expectedList.get(secondElementAfterMove).getName(),
                        expectedList.get(thirdElementAfterMove).getName()),
                Arrays.asList(burger.ingredients.get(0).getName(),
                        burger.ingredients.get(1).getName(),
                        burger.ingredients.get(2).getName()));
    }

    @Test
    @Description("Проверка расчета цены бургера")
    public void getPriceShouldReturnCorrectBurgerPriceTest() {
        Bun bun =  Mockito.mock(Bun.class);
        when(bun.getPrice()).thenReturn(150f);

        Ingredient firstIngredient = mock(Ingredient.class);
        Ingredient secondIngredient = mock(Ingredient.class);

        when(firstIngredient.getPrice()).thenReturn(50f);

        when(secondIngredient.getPrice()).thenReturn(250f);

        burger.setBuns(bun);
        burger.addIngredient(firstIngredient);
        burger.addIngredient(secondIngredient);

        float expectedPrice = 150f * 2 + 50f + 250f;
        float actualPrice = burger.getPrice();
        assertEquals("Неверно рассчитана цена бургера", expectedPrice, actualPrice, 0.0001f);
    }

    @Test
    @Description("Проверка рецепта бургера")
    public void getReceiptShouldReturnCorrectReceiptTest() {
        Bun bun =  Mockito.mock(Bun.class);
        when(bun.getName()).thenReturn("S oregano");
        when(bun.getPrice()).thenReturn(100f);

        Ingredient firstIngredient = mock(Ingredient.class);
        Ingredient secondIngredient = mock(Ingredient.class);

        when(firstIngredient.getName()).thenReturn("1000 ostrovov");
        when(firstIngredient.getPrice()).thenReturn(100f);
        when(firstIngredient.getType()).thenReturn(IngredientType.SAUCE);


        when(secondIngredient.getName()).thenReturn("indeyka");
        when(secondIngredient.getPrice()).thenReturn(200f);
        when(secondIngredient.getType()).thenReturn(IngredientType.FILLING);

        burger.setBuns(bun);
        burger.addIngredient(firstIngredient);
        burger.addIngredient(secondIngredient);

        String expectedReceipt = "(==== S oregano ====)\n" +
                                 "= sauce 1000 ostrovov =\n" +
                                 "= filling indeyka =\n" +
                                 "(==== S oregano ====)\n" +
                                 "\nPrice: 500,000000\n";
        String actualReceipt = (burger.getReceipt());

        assertEquals("Неверно составлен рецепт бургера", expectedReceipt, actualReceipt);
    }

    @Test
    @Description("Проверка рецепта бургера без ингредиентов")
    public void getReceiptShouldReturnCorrectReceiptWithoutIngredientsTest() {
        Bun bun =  Mockito.mock(Bun.class);
        when(bun.getName()).thenReturn("S oregano");
        when(bun.getPrice()).thenReturn(100f);
        burger.setBuns(bun);

        String expectedReceipt = "(==== S oregano ====)\n" +
                "(==== S oregano ====)\n" +
                "\nPrice: 200,000000\n";
        String actualReceipt = (burger.getReceipt());

        assertEquals("Неверно составлен рецепт бургера", expectedReceipt, actualReceipt);
    }
}
