package io.github.piscescup.mc.fabric.register.recipe;

import io.github.piscescup.mc.fabric.register.recipe.crafting.ReversibleCompactingCraftingRecipe;
import org.junit.jupiter.api.Test;




class ReversibleCompactingCraftingRecipeTest {


    public static
    class CompactingTypeTest {
        @Test
        public void testCompactingType() {
            System.out.println(ReversibleCompactingCraftingRecipe.CompactingType.REVERSIBLE_2x2);
            System.out.println(ReversibleCompactingCraftingRecipe.CompactingType.REVERSIBLE_2x3);
            System.out.println(ReversibleCompactingCraftingRecipe.CompactingType.REVERSIBLE_3x2);
            System.out.println(ReversibleCompactingCraftingRecipe.CompactingType.REVERSIBLE_3x3);
        }
    }
}