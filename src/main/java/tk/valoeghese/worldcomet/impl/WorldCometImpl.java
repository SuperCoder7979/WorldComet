package tk.valoeghese.worldcomet.impl;

import java.util.Map;

import com.google.common.collect.Maps;
import com.mojang.datafixers.Dynamic;

import net.minecraft.world.level.LevelGeneratorOptions;
import net.minecraft.world.level.LevelGeneratorType;
import tk.valoeghese.worldcomet.api.surface.Surface;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;
import tk.valoeghese.worldcomet.api.terrain.function.DepthmapFunction;
import tk.valoeghese.worldcomet.api.terrain.function.HeightmapFunction;
import tk.valoeghese.worldcomet.api.terrain.function.SurfaceDepthmapFunction;
import tk.valoeghese.worldcomet.api.type.WorldType;
import tk.valoeghese.worldcomet.api.type.WorldType.OverworldChunkGeneratorFactory;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGeneratorConfig;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGeneratorType;

public final class WorldCometImpl {
	private WorldCometImpl() {
	}

	public static <T extends SurfaceProvider> WorldCometChunkGeneratorType<T> createChunkGeneratorType(final WorldCometChunkGeneratorConfig<T> config) {
		return new WorldCometChunkGeneratorType<>(config);
	}

	public static double sampleHeightmapDepthmap(int noiseGenX, int noiseGenY, int noiseGenZ, Iterable<HeightmapFunction> heightmaps, Iterable<DepthmapFunction> depthmaps, Iterable<SurfaceDepthmapFunction> surfaceDepthmaps, SurfaceProvider surfaceProvider) {
		Surface surface = surfaceProvider.getSurfaceForGeneration(noiseGenX, noiseGenY, noiseGenZ);
		double result = 0.0;

		for (HeightmapFunction function : heightmaps) {
			result += function.getHeight(noiseGenX << 4, noiseGenZ << 4);
		}

		for (DepthmapFunction function : depthmaps) {
			result += function.getHeight(noiseGenX << 2, noiseGenY << 3, noiseGenZ << 2);
		}

		for (SurfaceDepthmapFunction function : surfaceDepthmaps) {
			result += function.getHeight(noiseGenX << 2, noiseGenY << 3, noiseGenZ << 2, surface);
		}

		return result;
	}

	public static double sampleHeightmapDepthmap(int noiseGenX, int noiseGenY, int noiseGenZ, Iterable<HeightmapFunction> heightmaps, Iterable<DepthmapFunction> depthmaps) {
		double result = 0.0;

		for (HeightmapFunction function : heightmaps) {
			result += function.getHeight(noiseGenX << 4, noiseGenZ << 4);
		}

		for (DepthmapFunction function : depthmaps) {
			result += function.getHeight(noiseGenX << 2, noiseGenY << 3, noiseGenZ << 2);
		}

		return result;
	}

	public static double sampleDepthmap(int noiseGenX, int noiseGenY, int noiseGenZ, Iterable<DepthmapFunction> depthmaps, Iterable<SurfaceDepthmapFunction> surfaceDepthmaps, SurfaceProvider surfaceProvider) {
		Surface surface = surfaceProvider.getSurfaceForGeneration(noiseGenX, noiseGenY, noiseGenZ);
		double result = 0.0;

		for (DepthmapFunction function : depthmaps) {
			result += function.getHeight(noiseGenX << 2, noiseGenY << 3, noiseGenZ << 2);
		}

		for (SurfaceDepthmapFunction function : surfaceDepthmaps) {
			result += function.getHeight(noiseGenX << 2, noiseGenY << 3, noiseGenZ << 2, surface);
		}

		return result;
	}

	public static double sampleDepthmap(int noiseGenX, int noiseGenY, int noiseGenZ, Iterable<DepthmapFunction> depthmaps) {
		double result = 0.0;

		for (DepthmapFunction function : depthmaps) {
			result += function.getHeight(noiseGenX << 2, noiseGenY << 3, noiseGenZ << 2);
		}

		return result;
	}

	public static double sampleHeightmap(int x, int z, Iterable<HeightmapFunction> heightmaps) {
		double result = 0.0;

		for (HeightmapFunction function : heightmaps) {
			result += function.getHeight(x, z);
		}

		return result;
	}

	public static LevelGeneratorOptions createGeneratorOptions(LevelGeneratorType levelType, Dynamic<?> dynamic, OverworldChunkGeneratorFactory generatorFactory) {
		return new LevelGeneratorOptions(levelType, dynamic, (world) -> {
			return generatorFactory.create(world);
		});
	}

	public static final SurfaceProvider NONE_SURFACE_PROVIDER = new NoneSurfaceProvider();
	public static final Map<String, WorldType<?>> STR_TO_WT_MAP = Maps.newHashMap();
}

class NoneSurfaceProvider implements SurfaceProvider {
	@Override
	public Surface getSurface(int x, int z, int height) {
		return Surface.DEFAULT;
	}

	@Override
	public Surface getSurfaceForGeneration(int noiseGenX, int noiseGenY, int noiseGenZ) {
		return Surface.DEFAULT;
	}
}