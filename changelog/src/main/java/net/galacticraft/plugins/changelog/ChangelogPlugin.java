package net.galacticraft.plugins.changelog;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.gradle.api.GradleException;

import net.galacticraft.common.plugins.GradlePlugin;
import net.galacticraft.plugins.changelog.template.TemplateFile;
import se.bjurr.gitchangelog.api.exceptions.GitChangelogRepositoryException;
import se.bjurr.gitchangelog.internal.git.GitRepo;
import se.bjurr.gitchangelog.internal.settings.Settings;
import se.bjurr.gitchangelog.plugin.gradle.GitChangelogGradlePlugin;

public class ChangelogPlugin extends GradlePlugin {

	@Override
	public void plugin() {
		applyPlugin(GitChangelogGradlePlugin.class);

		ChangelogExtension extension = extensions().findOrCreate("changelog", ChangelogExtension.class);
		GitRepo gitRepo = null;
		Git git = null;
		try {
			gitRepo = new GitRepo(project().getLayout().getProjectDirectory().getAsFile());
			git = Git.open(project().getLayout().getProjectDirectory().getAsFile());
		} catch (GitChangelogRepositoryException | IOException e) {
		}
		if (gitRepo == null) {
			lifecycle("Project directory is not a git repo, changelog plugin will not function");
		} else {
			Settings settings = new Settings();
			settings.setFromRepo(project().getLayout().getProjectDirectory().getAsFile().getPath());
			List<Ref> tags;
			try {
				tags = git.tagList().call();
			} catch (GitAPIException e) {
				throw new GradleException("An error occured while retreiving repository tag list");
			}
			if (tags != null) {

				settings.setFromRef(null);
			}

			project().afterEvaluate(p -> {
				if (!extension.getTemplate().isPresent()) {
					TemplateFile.Builder builder = TemplateFile.builder();
					Map<String, String> types = extension.getTypes().get();
					if (!extension.getTypes().get().isEmpty()) {
						types.forEach((k, v) -> {
							builder.addTypePart(k, v);
						});
					}
					// settings.set
				}
			});
		}
	}
}
