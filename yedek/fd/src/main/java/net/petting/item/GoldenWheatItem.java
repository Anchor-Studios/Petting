
package net.petting.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class GoldenWheatItem extends Item {
	public GoldenWheatItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON));
	}
}
