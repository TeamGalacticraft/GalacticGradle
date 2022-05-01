package net.galacticraft.plugins.curseforge.util;

import java.util.function.Consumer;

@FunctionalInterface
public interface Wrapper<E> {
	void perform(Consumer<E> action);
}
