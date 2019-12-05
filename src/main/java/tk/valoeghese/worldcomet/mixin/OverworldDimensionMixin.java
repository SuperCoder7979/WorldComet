package tk.valoeghese.worldcomet.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.level.LevelGeneratorType;
import tk.valoeghese.worldcomet.impl.type.WorldType;

@Mixin(OverworldDimension.class)
public abstract class OverworldDimensionMixin extends Dimension {
	public OverworldDimensionMixin(World world, DimensionType type) {
		super(world, type, 0);
	}

	@Inject(method = "createChunkGenerator", at = @At("RETURN"), cancellable = true)
	public void createChunkGenerator(CallbackInfoReturnable<ChunkGenerator<? extends ChunkGeneratorConfig>> info) {
		LevelGeneratorType type = this.world.getLevelProperties().getGeneratorType();

		if(WorldType.LGT_TO_WT_MAP.containsKey(type)) {
			WorldType worldType = WorldType.LGT_TO_WT_MAP.get(type);
			info.setReturnValue(
				worldType.chunkGenSupplier.create(this.world)
			);
		}
	}
}