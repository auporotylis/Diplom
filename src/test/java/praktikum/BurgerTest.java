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
    private int indexAfterMove0;
    private int indexAfterMove1;
    private int indexAfterMove2;

    public BurgerTest (int indexOld, int indexNew, int indexAfterMove0, int indexAfterMove1, int indexAfterMove2) {
        this.indexOld = indexOld;
        this.indexNew = indexNew;
        this.indexAfterMove0 = indexAfterMove0;
        this.indexAfterMove1 = indexAfterMove1;
        this.indexAfterMove2 = indexAfterMove2;

    }

    @Parameterized.Parameters
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
        Ingredient ingredient0 = mock(Ingredient.class);
        Ingredient ingredient1 = mock(Ingredient.class);

        burger.addIngredient(ingredient0);
        burger.addIngredient(ingredient1);
        burger.removeIngredient(0);
        assertEquals("В списке ингредиентов должен был остаться " + ingredient1.getName() +
                        " вместо " + burger.ingredients.get(0).getName(),
                ingredient1, burger.ingredients.get(0));
    }

    @Test
    @Description("Проверка изменения порядка ингредиентов в бургере")
    public void moveIngredientShouldMoveIngredientsInBurgerTest() {
        Ingredient ingredient0 = mock(Ingredient.class);
        Ingredient ingredient1 = mock(Ingredient.class);
        Ingredient ingredient2 = mock(Ingredient.class);

        when(ingredient0.getName()).thenReturn("kepchuk");
        when(ingredient1.getName()).thenReturn("kotleta");
        when(ingredient2.getName()).thenReturn("mazik");

        List<Ingredient> expectedList = Arrays.asList(ingredient0, ingredient1, ingredient2);

        burger.addIngredient(ingredient0);
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        burger.moveIngredient(indexOld, indexNew);
        assertEquals("Ожидался порядок: " + expectedList.get(indexAfterMove0).getName() + ", " +
                        expectedList.get(indexAfterMove1).getName() + ", " +
                        expectedList.get(indexAfterMove2).getName() +
                        " фактический порядок " + burger.ingredients.get(0).getName() + ", " +
                        burger.ingredients.get(1).getName() + ", " +
                        burger.ingredients.get(2).getName() + ", ",
                Arrays.asList(expectedList.get(indexAfterMove0).getName(),
                        expectedList.get(indexAfterMove1).getName(),
                        expectedList.get(indexAfterMove2).getName()),
                Arrays.asList(burger.ingredients.get(0).getName(),
                        burger.ingredients.get(1).getName(),
                        burger.ingredients.get(2).getName()));
    }

    @Test
    @Description("Проверка расчета цены бургера")
    public void getPriceShouldReturnCorrectBurgerPriceTest() {
        Bun bun =  Mockito.mock(Bun.class);
        when(bun.getPrice()).thenReturn(150f);

        Ingredient ingredient0 = mock(Ingredient.class);
        Ingredient ingredient1 = mock(Ingredient.class);

        when(ingredient0.getPrice()).thenReturn(50f);

        when(ingredient1.getPrice()).thenReturn(250f);

        burger.setBuns(bun);
        burger.addIngredient(ingredient0);
        burger.addIngredient(ingredient1);

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

        Ingredient ingredient0 = mock(Ingredient.class);
        Ingredient ingredient1 = mock(Ingredient.class);

        when(ingredient0.getName()).thenReturn("1000 ostrovov");
        when(ingredient0.getPrice()).thenReturn(100f);
        when(ingredient0.getType()).thenReturn(IngredientType.SAUCE);


        when(ingredient1.getName()).thenReturn("indeyka");
        when(ingredient1.getPrice()).thenReturn(200f);
        when(ingredient1.getType()).thenReturn(IngredientType.FILLING);

        burger.setBuns(bun);
        burger.addIngredient(ingredient0);
        burger.addIngredient(ingredient1);

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
