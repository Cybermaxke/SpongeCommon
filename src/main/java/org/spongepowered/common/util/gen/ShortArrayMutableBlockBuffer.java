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
package org.spongepowered.common.util.gen;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.util.DiscreteTransform3;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.api.world.extent.ImmutableBlockVolume;
import org.spongepowered.api.world.extent.MutableBlockVolume;
import org.spongepowered.api.world.extent.StorageType;
import org.spongepowered.api.world.extent.UnmodifiableBlockVolume;
import org.spongepowered.api.world.extent.worker.MutableBlockVolumeWorker;
import org.spongepowered.api.world.schematic.BlockPalette;
import org.spongepowered.common.world.extent.MutableBlockViewDownsize;
import org.spongepowered.common.world.extent.MutableBlockViewTransform;
import org.spongepowered.common.world.extent.UnmodifiableBlockVolumeWrapper;
import org.spongepowered.common.world.extent.worker.SpongeMutableBlockVolumeWorker;
import org.spongepowered.common.world.schematic.GlobalBlockPalette;

@NonnullByDefault
public class ShortArrayMutableBlockBuffer extends AbstractBlockBuffer implements MutableBlockVolume {

    @SuppressWarnings("ConstantConditions") private static final BlockState AIR = BlockTypes.AIR.getDefaultState();
    protected final BlockPalette palette;
    private final short[] blocks;

    public ShortArrayMutableBlockBuffer(Vector3i start, Vector3i size) {
        this(new short[size.getX() * size.getY() * size.getZ()], start, size, GlobalBlockPalette.getInstance());
    }

    public ShortArrayMutableBlockBuffer(short[] blocks, Vector3i start, Vector3i size) {
        this(blocks, start, size, GlobalBlockPalette.getInstance());
    }

    public ShortArrayMutableBlockBuffer(Vector3i start, Vector3i size, BlockPalette palette) {
        this(new short[size.getX() * size.getY() * size.getZ()], start, size, palette);
    }

    public ShortArrayMutableBlockBuffer(short[] blocks, Vector3i start, Vector3i size, BlockPalette palette) {
        super(start, size);
        this.blocks = blocks;
        this.palette = palette;
        // TODO we should validate that the palette contains all of the blocks given to us in the array
    }

    @Override
    public void setBlock(int x, int y, int z, BlockState block) {
        checkRange(x, y, z);
        this.blocks[getIndex(x, y, z)] = (short) this.palette.addState(block);
    }

    @Override
    public BlockState getBlock(int x, int y, int z) {
        checkRange(x, y, z);
        BlockState block = this.palette.getState(this.blocks[getIndex(x, y, z)]).get();
        return block == null ? AIR : block;
    }

    @Override
    public MutableBlockVolume getBlockView(Vector3i newMin, Vector3i newMax) {
        checkRange(newMin.getX(), newMin.getY(), newMin.getZ());
        checkRange(newMax.getX(), newMax.getY(), newMax.getZ());
        return new MutableBlockViewDownsize(this, newMin, newMax);
    }

    @Override
    public MutableBlockVolume getBlockView(DiscreteTransform3 transform) {
        return new MutableBlockViewTransform(this, transform);
    }

    @Override
    public MutableBlockVolumeWorker<? extends MutableBlockVolume> getBlockWorker() {
        return new SpongeMutableBlockVolumeWorker<>(this);
    }

    @Override
    public UnmodifiableBlockVolume getUnmodifiableBlockView() {
        return new UnmodifiableBlockVolumeWrapper(this);
    }

    @Override
    public MutableBlockVolume getBlockCopy(StorageType type) {
        switch (type) {
            case STANDARD:
                return new ShortArrayMutableBlockBuffer(this.blocks.clone(), this.start, this.size);
            case THREAD_SAFE:
            default:
                throw new UnsupportedOperationException(type.name());
        }
    }

    @Override
    public ImmutableBlockVolume getImmutableBlockCopy() {
        return new ShortArrayImmutableBlockBuffer(this.blocks, this.start, this.size);
    }
}
