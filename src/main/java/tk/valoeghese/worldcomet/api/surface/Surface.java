package tk.valoeghese.worldcomet.api.surface;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.chunk.Chunk;
import tk.valoeghese.worldcomet.impl.NamespacedRegistry;

/**
 * Class which controls the initial block replacing of the surface of the chunk. i.e. adding the bedrock floor, top block (often grass), under block (often dirt), and other relevant blocks.
 */
public class Surface {
	protected BlockState topBlock = GRASS_BLOCK;
	protected BlockState underBlock = DIRT;
	protected BlockState underwaterBlock = Blocks.GRAVEL.getDefaultState();

	public Surface(String id) {
		this(new Identifier(id));
	}

	public Surface(Identifier id) {
		NamespacedRegistry.register(NamespacedRegistry.SURFACE, id, this);
	}

	public Biome getBiome(int surfaceHeight) {
		return Biomes.PLAINS;
	}

	public void replaceSurfaceBlocks(IWorld world, Chunk chunk, Random rand, int x, int z, double noise) {
		int run = -1;

		BlockPos.Mutable pos = new BlockPos.Mutable();
		pos.set(x & 15, 0, z & 15);
		boolean water = false;

		for (int y = 255; y >= 0; --y) {
			pos.setY(y);
			++run;

			if (y < rand.nextInt(5)) { // add bedrock
				chunk.setBlockState(pos, BEDROCK, false);
				continue;
			}

			BlockState currentState = chunk.getBlockState(pos);

			if (currentState == AIR) {
				water = false;
				run = -1;
			} else if (currentState == WATER) {
				water = true;
				run = -1;
			} else if (currentState == STONE) {
				if (run < 5) {
					if (water) {
						chunk.setBlockState(pos, this.underwaterBlock, false);
					} else {
						if (run == 0) {
							chunk.setBlockState(pos, this.topBlock, false);
						} else {
							chunk.setBlockState(pos, this.underBlock, false);
						}
					}
				}
			}
		}
	}

	protected static final BlockState AIR = Blocks.AIR.getDefaultState();
	protected static final BlockState WATER = Blocks.WATER.getDefaultState();
	protected static final BlockState STONE = Blocks.STONE.getDefaultState();
	protected static final BlockState SAND = Blocks.SAND.getDefaultState();
	protected static final BlockState GRASS_BLOCK = Blocks.GRASS_BLOCK.getDefaultState();
	protected static final BlockState DIRT = Blocks.DIRT.getDefaultState();
	protected static final BlockState BEDROCK = Blocks.BEDROCK.getDefaultState();

	public static final Surface DEFAULT = new Surface("default");
}
