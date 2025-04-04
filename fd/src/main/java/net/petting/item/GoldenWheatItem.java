
package net.petting.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class GoldenWheatItem extends Item {
	public GoldenWheatItem(Item.Properties properties) {
		super(properties.rarity(Rarity.EPIC).stacksTo(64));
	}
}
