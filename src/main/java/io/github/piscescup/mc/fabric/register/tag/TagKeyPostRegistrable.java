package io.github.piscescup.mc.fabric.register.tag;

import io.github.piscescup.mc.fabric.register.PostRegistrable;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

/**
 * Represents the post-registration stage of a {@link TagKey} registration workflow.
 *
 * <p>This interface is returned after the pre-registration stage has been finalized (typically
 * via {@code register()} on the corresponding pre-registrable).
 *
 * <p>Implementations should keep this stage side-effect light and focused on metadata/configuration,
 * leaving the actual creation/registration of the {@link TagKey} to the pre-registration phase.
 *
 * @param <T> the element type contained by the tag (e.g., {@code Item}, {@code Block})
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface TagKeyPostRegistrable<T>
    extends PostRegistrable<TagKey<T>, TagKeyPostRegistrable<T>, TagKeyRegister<T>>
{

}
