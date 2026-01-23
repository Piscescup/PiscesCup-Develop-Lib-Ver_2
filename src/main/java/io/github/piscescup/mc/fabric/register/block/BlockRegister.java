package io.github.piscescup.mc.fabric.register.block;

import io.github.piscescup.mc.fabric.register.Register;
import io.github.piscescup.mc.fabric.utils.CheckUtils;
import io.github.piscescup.mc.fabric.utils.CheckUtils.NullCheck;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * The {@code BlockRegister} class is used to register blocks with specified settings
 * and a factory for block creation. This class implements both {@link BlockPreRegistrable}
 * and {@link BlockPostRegistrable} interfaces, allowing for pre-registration setup and
 * post-registration actions.
 * <p>
 * The {@code BlockRegister} supports block creation with customizable {@code AbstractBlock.Settings}
 * and allows for the definition of custom factories. It typically handles the generation
 * of associated {@code BlockItem}s automatically if configured.
 * </p>
 *
 * <h2>Usages</h2>
 * <pre>{@code
 * public static final Block TEST_BLOCK = BlockRegister.createForBlock(MOD_ID, "test_block")
 *         .setting(AbstractBlock.Settings.create()
 *         .strength(2.0f)
 *         .requiresTool()
 *     )
 *     .register()
 *     .translate(MCLanguage.EN_US, "PCDevLib Test Block")
 *     .translate(MCLanguage.ZH_CN, "PCDevLib 测试方块")
 *     .get();
 * }</pre>
 * @author REN YuanTong
 * @since 1.0.0
 */
public class BlockRegister
    extends Register<Block, BlockPostRegistrable, BlockRegister>
    implements BlockPreRegistrable, BlockPostRegistrable
{
    private AbstractBlock.Settings settings = AbstractBlock.Settings.create();
    private Function<AbstractBlock.Settings, Block> factory = Block::new;

    private BlockRegister(@NotNull Identifier id) {
        NullCheck.requireNonNull(id);
        super(RegistryKeys.BLOCK, id);
    }

    /**
     * Creates a new instance of {@link BlockPreRegistrable} for the given namespace and path.
     *
     * @param namespace the namespace for the block; must not be null
     * @param path the path for the block; must not be null
     * @throws NullPointerException if either {@code namespace} or {@code path} is null
     */
    @Contract("_, _ -> new")
    public static @NotNull BlockPreRegistrable createFor(
        @NotNull String namespace,
        @NotNull String path
    ) {
        NullCheck.requireNonNull(namespace);
        NullCheck.requireNonNull(path);
        return new BlockRegister(Identifier.of(namespace, path));
    }

    /**
     * Creates a new instance of {@link BlockPreRegistrable} for the given identifier.
     *
     * @param id the unique identifier for the block; must not be null
     * @throws NullPointerException if {@code id} is null
     */
    @Contract("_ -> new")
    public static @NotNull BlockPreRegistrable createFor(
        @NotNull Identifier id
    ) {
        NullCheck.requireNonNull(id);
        return new BlockRegister(id);
    }

    @Override
    public BlockPostRegistrable register() {
        Block block = this.factory.apply(this.settings.registryKey(this.registryKey));

        this.thing = Registry.register(
            Registries.BLOCK,
            this.id,
            block
        );

        return this;
    }

    @Override
    public BlockPreRegistrable settings(@NotNull AbstractBlock.Settings settings) {
        NullCheck.requireNonNull(settings);
        this.settings = settings;
        return this;
    }

    @Override
    public BlockPreRegistrable factory(@NotNull Function<AbstractBlock.Settings, net.minecraft.block.Block> factory) {
        NullCheck.requireNonNull(factory);
        this.factory = factory;
        return this;
    }
}
