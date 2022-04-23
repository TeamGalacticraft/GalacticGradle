package net.galacticraft.plugins.curseforge.internal;

import com.google.common.base.Strings;
import groovy.lang.Closure;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CurseRelation implements Serializable {
    private void addRelation(String typeIn, String slugIn) {
        Project project = new Project();


        projects.add(project.setType(typeIn)project.setSlug(slugIn));
    }

    @Deprecated
    public void requiredLibrary(String slugIn) {
        addRelation("requiredDependency", slugIn);
    }

    public void requiredDependency(String slugIn) {
        addRelation("requiredDependency", slugIn);
    }

    public void embeddedLibrary(String slugIn) {
        addRelation("embeddedLibrary", slugIn);
    }

    @Deprecated
    public void optionalLibrary(String slugIn) {
        addRelation("optionalDependency", slugIn);
    }

    public void optionalDependency(String slugIn) {
        addRelation("optionalDependency", slugIn);
    }

    public void tool(String slugIn) {
        addRelation("tool", slugIn);
    }

    public void incompatible(String slugIn) {
        addRelation("incompatible", slugIn);
    }

    public void methodMissing(String name, Object args) {
        Util.check(false, name + " is not a valid relation type. Valid types are: " + String.valueOf(CurseGradlePlugin.getVALID_RELATIONS()));
    }

    public void validate(final Object id) {
        DefaultGroovyMethods.each(projects, new Closure<Object>(this, this) {
            public void doCall(Object project) {
                ((Project) project).validate(id);
            }

        });
    }

    public HashSet<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    private Set<Project> projects = new HashSet<Project>();

    public static class Project implements Serializable {
        public void validate(final Object id) {
            Util.check(!Strings.isNullOrEmpty(slug), "Project relation slug not set for relation in project " + DefaultGroovyMethods.invokeMethod(String.class, "valueOf", new Object[]{id}));
            Util.check(CurseGradlePlugin.getVALID_RELATIONS().contains(type), "Invalid relation type (" + getType() + ") for relation in project " + DefaultGroovyMethods.invokeMethod(String.class, "valueOf", new Object[]{id}) + ". Valid options are: " + String.valueOf(CurseGradlePlugin.getVALID_RELATIONS()));
        }

        @Override
        public boolean equals(final Object o) {
            if (DefaultGroovyMethods.is(this, o)) {
                return true;
            }

            if (!getClass().equals(o.getClass())) {
                return false;
            }


            final Project project = (Project) o;

            if (!slug.equals(project.getSlug())) {
                return false;
            }


            return true;
        }

        @Override
        public int hashCode() {
            return (slug != null ? slug.hashCode() : 0);
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        /**
         * The unique slug for the project
         */
        private String slug;
        /**
         * The type of relationship. Must be one of {@link CurseGradlePlugin#VALID_RELATIONS}
         */
        private String type;
    }
}
