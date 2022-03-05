package dev.galacticraft.gradle.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArtifactData {
	
	public static final ArtifactData NULL = new ArtifactData();
	
	private String version;
	private String group;
	private String id;
	
	public String artifactString() {
		return group + ":" + id + ":" + version;
	}
}
