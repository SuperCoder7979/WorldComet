package tk.valoeghese.biomeoverhaul.impl.type;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.world.level.LevelGeneratorType;

public final class OverhaulWorldtypeProvider
{
	private OverhaulWorldtypeProvider() {}
	
	/**
	 * Credit: Beta-Plus mod, fabric 1.14
	 */
	public static LevelGeneratorType createWorldType(String name)
	{
		LevelGeneratorType worldType;
		int id = 7;
		Field types = null;

		for(Field f : LevelGeneratorType.class.getDeclaredFields())
		{
			if(f.getType()==LevelGeneratorType[].class)
			{
				types = f;
			}
		}

		if(types != null)
		{
			try 
			{
				LevelGeneratorType newTypes[] = new LevelGeneratorType[LevelGeneratorType.TYPES.length+1];

				System.arraycopy(LevelGeneratorType.TYPES, 0, newTypes, 0, LevelGeneratorType.TYPES.length);
				newTypes[newTypes.length-1] = null;

				types.setAccessible(true);
				Field modifiers = Field.class.getDeclaredField("modifiers");
				modifiers.setAccessible(true);

				modifiers.setInt(types, types.getModifiers() & ~Modifier.FINAL);
				types.set(null,newTypes);
				id=LevelGeneratorType.TYPES.length - 1;
			}
			catch (IllegalAccessException | NoSuchFieldException e)
			{
				return null;
			}
		}
		else
		{
			return null;
		}
		try
		{
			Constructor<LevelGeneratorType> constructor = LevelGeneratorType.class.getDeclaredConstructor(int.class, String.class);
			constructor.setAccessible(true);
			worldType = constructor.newInstance(id, name);
			worldType.setCustomizable(false);
		} catch (Exception e)
		{
			return null;
		}

		return worldType;

	}
}
