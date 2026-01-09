package io.github.piscescup.mc.fabric.register;

/**
 * Represents an object that participates in a pre-registration phase and can
 * be committed to the registration workflow.
 * <p>
 * Implementations perform any required preparation and return a fluent
 * {@code PostRegistrable} instance for further chained configuration (such as
 * localization or metadata adjustments).
 *
 * @param <POST> The fluent {@code PostRegistrable} type returned by {@link #register()}
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see PostRegistrable
 * @see Register
 */
public interface PreRegistrable<POST extends PostRegistrable<?, POST, ?>> {

    /**
     * Executes the pre-registration phase of the two-phase registration workflow.
     * <p>
     * This method is the <b>mandatory entry point</b> that transitions a {@code Register}
     * from the pre-registration stage into the fluent post-registration stage.
     * Implementations must ensure that, <b>before this method returns</b> (and typically
     * <b>during</b> its execution), the following core registration state is fully established:
     *
     * <h2>Required registration state</h2>
     * <ul>
     *   <li>
     *     {@link Register#thing} — the concrete object instance to be registered (e.g. an Item or Block).
     *     <br>
     *     It must be created, prepared, or otherwise assigned such that it is accessible via {@link POST#get() get()}.
     *   </li>
     *   <li>
     *     {@link Register#registryKey} — the registry key bound to the registered object.
     *     <br>
     *     It must be assigned such that it is accessible via {@link POST#getRegistryKey() getRegistryKey()}.
     *   </li>
     * </ul>
     *
     *
     * <h2>Error handling</h2>
     * <p>
     * If an implementation returns without properly establishing {@link Register#thing} or {@link Register#registryKey},
     * subsequent terminal calls (such as {@link POST#get() get()} or {@link POST#getRegistryKey() getRegistryKey()}) will fail
     * (and are expected to throw an {@link NullPointerException} due to the null state).
     * Implementations are encouraged to fail fast by validating required fields before returning,
     * and throwing an {@link NullPointerException} with a clear message if the registration cannot
     * be completed.
     *
     * <h2>Implementation notes</h2>
     * <ul>
     *   <li>
     *     This method should typically be called exactly once per register instance. If repeated calls
     *     are allowed by a specific implementation, it must clearly define whether it is idempotent
     *     or whether repeated registration attempts are illegal.
     *   </li>
     *   <li>
     *     The returned {@code POST} object is usually {@code this} (or a wrapper view of {@code this})
     *     to support fluent chaining, but implementations may return a distinct post-registration helper
     *     object as long as terminal access remains consistent.
     *   </li>
     * </ul>
     *
     * <h2>Typical usage</h2>
     * <pre>{@code
     * public static final Item TEST_ITEM1 = ItemRegister.createFor(MOD_ID, "test_item_1")
     *     .settings(new Item.Settings()
     *         .maxCount(16)
     *     )
     *     .register()
     *     .translate(MCLanguage.ZH_CN, "测试物品1")
     *     .translate(MCLanguage.EN_US, "Test Item 1");
     *     .get();
     * }</pre>
     *
     * @return a fluent post-registration instance used for chaining post-registration operations
     * @see PostRegistrable
     * @see Register
     */
    POST register();
}
