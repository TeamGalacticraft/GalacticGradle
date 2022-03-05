package dev.galacticraft.gradle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddonExtension {
	private boolean snapshots = false;
	private String version = "latest";
}