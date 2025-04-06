package net.petting.procedures;

import net.minecraft.world.entity.Entity;

import java.util.regex.Pattern;

public class ReturnTamedCountProcedure {
	public static double execute(Entity entity) {
		if (entity == null)
			return 0;
		double c = 0;
		c = -1;
		{
			String[] _array = (entity.getPersistentData().getString("petlist")).split(Pattern.quote(","));
			if (_array.length != 0) {
				for (String stringiterator : _array) {
					if (!(stringiterator).equals("")) {
						c = c + 1;
					}
				}
			} else {
				String stringiterator = (entity.getPersistentData().getString("petlist"));
				for (int _yourmother = 0; _yourmother < 1; _yourmother++) {
					if (!(stringiterator).equals("")) {
						c = c + 1;
					}
				}
			}
		}
		return c;
	}
}
