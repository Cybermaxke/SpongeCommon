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
package org.spongepowered.common.mixin.core.advancement;

import net.minecraft.advancements.FrameType;
import org.spongepowered.api.advancement.AdvancementType;
import org.spongepowered.api.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.common.text.translation.SpongeTranslation;

import javax.annotation.Nullable;

@Implements(@Interface(iface = AdvancementType.class, prefix = "type$"))
@Mixin(FrameType.class)
public abstract class MixinFrameType implements AdvancementType {

    @Shadow @Final private String name;

    @Nullable private String id;
    @Nullable private String spongeName;
    @Nullable private Text toastText;

    @Override
    public Text format(Text title) {
        if (this.toastText == null) {
            this.toastText = Text.of(new SpongeTranslation("advancements.toast." + this.name));
        }
        return Text.of(this.toastText, Text.NEW_LINE, title);
    }

    @Override
    public String getId() {
        if (this.id == null) {
            this.id = "minecraft:" + this.name;
        }
        return this.id;
    }

    @Intrinsic
    public String type$getName() {
        if (this.spongeName == null) {
            this.spongeName = Character.isUpperCase(this.name.charAt(0)) + this.name.substring(1);
        }
        return this.spongeName;
    }
}