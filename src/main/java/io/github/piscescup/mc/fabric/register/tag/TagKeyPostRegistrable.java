package io.github.piscescup.mc.fabric.register.tag;

import io.github.piscescup.mc.fabric.register.PostRegistrable;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface TagKeyPostRegistrable<T>
    extends PostRegistrable<TagKey<T>, TagKeyPostRegistrable<T>, TagKeyRegister<T>>
{

}
