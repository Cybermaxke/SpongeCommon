/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.registry.type.item;

import static com.google.common.base.Preconditions.checkNotNull;

import net.minecraft.item.Item;
import org.spongepowered.api.CatalogKey;
import org.spongepowered.api.data.type.ToolType;
import org.spongepowered.api.data.type.ToolTypes;
import org.spongepowered.api.registry.CatalogRegistryModule;
import org.spongepowered.api.registry.util.AdditionalRegistration;
import org.spongepowered.api.registry.util.RegisterCatalog;
import org.spongepowered.common.registry.AbstractCatalogRegistryModule;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RegisterCatalog(ToolTypes.class)
public class ToolTypeRegistryModule extends AbstractCatalogRegistryModule<ToolType> implements CatalogRegistryModule<ToolType> {

    @Override
    public void registerDefaults() {
        for (Item.ToolMaterial toolMaterial : Item.ToolMaterial.values()) {
            if (toolMaterial == Item.ToolMaterial.DIAMOND) {
                this.map.put(CatalogKey.minecraft("diamond"), (ToolType) (Object) toolMaterial);
            }
            this.map.put(CatalogKey.resolve(toolMaterial.name().toLowerCase(Locale.ENGLISH)), (ToolType) (Object) toolMaterial);
        }
    }

    @AdditionalRegistration
    public void customRegistration() {
        for (Item.ToolMaterial toolMaterial : Item.ToolMaterial.values()) {
            final CatalogKey key = ((ToolType) (Object) toolMaterial).getKey();
            if (!this.map.containsKey(key)) {
                register((ToolType) (Object) toolMaterial);
            }
        }
    }
}
