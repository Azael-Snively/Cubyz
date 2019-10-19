package io.cubyz.blocks;

import io.cubyz.items.Item;

public class RubyOre extends Ore {

	public RubyOre() {
		setID("cubyz:ruby_ore");
		setHeight(8);
		setChance(0.006F);
		setHardness(50);
		bc = BlockClass.STONE;
		Item bd = new Item();
		bd.setID("cubyz:ruby");
		bd.setTexture("materials/ruby.png");
		setBlockDrop(bd);
	}
	
}