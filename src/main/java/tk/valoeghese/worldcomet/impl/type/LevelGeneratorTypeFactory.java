package tk.valoeghese.worldcomet.impl.type;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.BiFunction;

import com.mojang.datafixers.Dynamic;

import net.minecraft.world.level.LevelGeneratorOptions;
import net.minecraft.world.level.LevelGeneratorType;
import tk.valoeghese.worldcomet.mixin.LevelGeneratorTypeAccessor;

public final class LevelGeneratorTypeFactory {
	private LevelGeneratorTypeFactory() {}
	
	private static int idToUse = 7;
	
	// Credit: Beta-Plus mod, fabric 1.14
	public static LevelGeneratorType createWorldType(String name, OptionsFactory optionsFactory) {
		LevelGeneratorType levelGenType;
		int id = idToUse;
		Field types = null;

		for(Field f : LevelGeneratorType.class.getDeclaredFields()) {
			if(f.getType()==LevelGeneratorType[].class) {
				types = f;
			}
		}

		if(types != null) {
			try {
				LevelGeneratorType newTypes[] = new LevelGeneratorType[LevelGeneratorType.TYPES.length+1];

				System.arraycopy(LevelGeneratorType.TYPES, 0, newTypes, 0, LevelGeneratorType.TYPES.length);
				newTypes[newTypes.length-1] = null;

				types.setAccessible(true);
				Field modifiers = Field.class.getDeclaredField("modifiers");
				modifiers.setAccessible(true);

				modifiers.setInt(types, types.getModifiers() & ~Modifier.FINAL);
				types.set(null,newTypes);
				id = LevelGeneratorType.TYPES.length - 1;
				idToUse = id;
			}
			catch (IllegalAccessException | NoSuchFieldException e) {
				return null;
			}
		}
		else {
			return null;
		}
		try {
			levelGenType = LevelGeneratorTypeAccessor.create(id, name, optionsFactory);
			levelGenType.setCustomizable(false);
		} catch (Exception e) {
			return null;
		}

		return levelGenType;
	}

	public static interface OptionsFactory extends BiFunction<LevelGeneratorType, Dynamic<?>, LevelGeneratorOptions> {
	}
}