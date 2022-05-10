package net.galacticraft.plugins.tinyforge.providers;

import de.siegmar.fastcsv.writer.CsvWriter;
import de.siegmar.fastcsv.writer.LineDelimiter;
import net.galacticraft.plugins.tinyforge.Utils;
import net.galacticraft.plugins.tinyforge.versions.YarnVersion;
import net.minecraftforge.gradle.common.config.MCPConfigV2;
import net.minecraftforge.gradle.common.util.MavenArtifactDownloader;
import net.minecraftforge.gradle.mcp.ChannelProvider;
import net.minecraftforge.gradle.mcp.MCPRepo;
import net.minecraftforge.srgutils.IMappingBuilder;
import net.minecraftforge.srgutils.IMappingFile;
import net.minecraftforge.srgutils.INamedMappingFile;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static net.minecraftforge.gradle.common.util.Utils.getZipData;
import static net.minecraftforge.gradle.common.util.Utils.ZIPTIME;

public class YarnChannelProvider implements ChannelProvider {
    public YarnChannelProvider(Project project) {
        project.getRepositories().maven(fmc -> fmc.setUrl("https://maven.fabricmc.net"));
    }

    @NotNull
    @Override
    public Set<String> getChannels() {
        return Stream.of("yarn").collect(Collectors.toSet());
    }

    @Nullable
    @Override
    public File getMappingsFile(@NotNull MCPRepo mcpRepo,
                                @NotNull Project project,
                                @NotNull String mappingsChannel,
                                @NotNull String mappingsVersion) throws IOException {
        YarnVersion version = YarnVersion.of(mappingsVersion);

        File yarnFile = Utils.getDependency(project, version.getDependencyNotation());
        if(yarnFile == null)
            throw new IllegalStateException("Could not find Yarn artifact for " + version.getVersion());
        project.getLogger().lifecycle("Found Yarn artifact " + version.getVersion());

        File intFile = Utils.getDependency(project, version.getIntermediaryDependencyNotation());
        if(intFile == null)
            throw new IllegalStateException("Could not find Intermediary artifact for " + version.getIntermediaryDependencyNotation());
        project.getLogger().lifecycle("Found Intermediary artifact " + version.getMinecraftVersion());

        File mcpFile = MavenArtifactDownloader.manual(project, "de.oceanlabs.mcp:mcp_config:" + version.getMinecraftVersion() + "@zip", false);
        if(mcpFile == null)
            throw new IllegalStateException("Could not get MCP config for v" + version.getMinecraftVersion());
        MCPConfigV2 mcp = MCPConfigV2.getFromArchive(mcpFile);
        IMappingFile obfToSrg = IMappingFile.load(new ByteArrayInputStream(getZipData(mcpFile, mcp.getData("mappings"))));


        File mappings = cacheYarnSrg(project, version, "jar");
//        if(mappings.exists())
//            return mappings;

        try(InputStream yarnMappingsStream = getTinyV2Mappings(new ZipFile(yarnFile));
            InputStream intMappingsStream = getTinyV2Mappings(new ZipFile(intFile));
        ) {
            IMappingFile obfToInt = IMappingFile.load(intMappingsStream);
            IMappingFile intToYrn = IMappingFile.load(yarnMappingsStream);

            obfToSrg.write(project.file("obfToSrg.txt").toPath(), IMappingFile.Format.TSRG, false);
            obfToInt.write(project.file("obfToInt.txt").toPath(), IMappingFile.Format.TSRG, false);

            IMappingFile yarnSrgMappings = convertMappings(project, obfToSrg, obfToInt, intToYrn).getMap("srg", "yarn");
            yarnSrgMappings.write(project.file("srgToYarn.txt").toPath(), IMappingFile.Format.TSRG, false);
            if(!mappings.getParentFile().exists())
                mappings.getParentFile().mkdirs();

            if(mappings.exists())
                mappings.delete();

            mergeToCsvZip(yarnSrgMappings, String.valueOf(mappings.toURI()));

            return mappings;
        }
    }

    private void mergeToCsvZip(IMappingFile mapping, String path) throws IOException {
        List<String> headers = new ArrayList<>();
        headers.add("searge");
        headers.add("name");
        headers.add("desc");

        List<List<String>> packages = new ArrayList<>();
        List<List<String>> classes = new ArrayList<>();
        List<List<String>> fields = new ArrayList<>();
        List<List<String>> methods = new ArrayList<>();
        List<List<String>> parameters = new ArrayList<>();

        packages.add(headers);
        classes.add(headers);
        fields.add(headers);
        methods.add(headers);
        parameters.add(headers);

        mapping.getPackages().forEach(pkg -> {
            packages.add(Arrays.asList(pkg.getOriginal(), pkg.getMapped(), findComment(pkg.getMetadata())));
        });

        mapping.getClasses().forEach(cls -> {
            classes.add(Arrays.asList(cls.getOriginal(), cls.getMapped(), findComment(cls.getMetadata())));

            cls.getFields().forEach(fld -> {
                fields.add(Arrays.asList(fld.getOriginal(), fld.getMapped(), findComment(fld.getMetadata())));
            });

            cls.getMethods().forEach(mtd -> {
                methods.add(Arrays.asList(mtd.getOriginal(), mtd.getMapped(), findComment(mtd.getMetadata())));
                mtd.getParameters().forEach(par -> {
                    parameters.add(Arrays.asList(par.getOriginal(), par.getMapped(), findComment(par.getMetadata())));
                });
            });
        });

        try {
            Map<String, String> env = new HashMap<>();
            env.put("create", "true");
            try (FileSystem zipFs = FileSystems.newFileSystem(new URI("jar:" + path), env)) {
                Path rootPath = zipFs.getPath("/");
                writeCsv("classes.csv", classes, rootPath);
                writeCsv("fields.csv", fields, rootPath);
                writeCsv("methods.csv", methods, rootPath);
                writeCsv("params.csv", parameters, rootPath);
                writeCsv("packages.csv", packages, rootPath);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private INamedMappingFile convertMappings(Project project, IMappingFile obfToSrg, IMappingFile obfToInt, IMappingFile intToYrn) {
        long startTime = System.currentTimeMillis();
        project.getLogger().lifecycle("Merging mappings...");
        IMappingBuilder builder = IMappingBuilder.create("obf", "intermediary", "yarn", "srg");

        obfToInt.getPackages().forEach(iPkg -> {
            IMappingFile.IPackage yPkg = intToYrn.getPackage(iPkg.getMapped());
            if(yPkg == null) yPkg = iPkg;
            IMappingFile.IPackage sPkg = obfToSrg.getPackage(iPkg.getOriginal());
            IMappingBuilder.IPackage bPkg = builder.addPackage(iPkg.getOriginal(), iPkg.getMapped(), yPkg.getMapped(), sPkg.getMapped());

            copyMeta(yPkg, bPkg);
        });

        obfToInt.getClasses().forEach(iCls -> {
            IMappingFile.IClass yCls = intToYrn.getClass(iCls.getMapped());
            if(yCls == null) yCls = iCls;
            IMappingFile.IClass sCls = obfToSrg.getClass(iCls.getOriginal());
            IMappingBuilder.IClass bCls = builder.addClass(iCls.getOriginal(), iCls.getMapped(), yCls.getMapped(), sCls.getMapped());

            copyMeta(yCls, bCls);

            IMappingFile.IClass finalYCls = yCls;
            iCls.getFields().forEach(iFld -> {
                IMappingFile.IField yFld = finalYCls.getField(iFld.getMapped());
                if(yFld == null) yFld = iFld;
                IMappingFile.IField sFld = sCls.getField(iFld.getOriginal());
                IMappingBuilder.IField bFld = bCls.field(iFld.getOriginal(), iFld.getMapped(), yFld.getMapped(), sFld.getMapped());

                copyMeta(yFld, bFld);
            });

            iCls.getMethods().forEach(iMtd -> {
                IMappingFile.IMethod yMtd = finalYCls.getMethod(iMtd.getMapped(), iMtd.getMappedDescriptor());
                if(yMtd == null) yMtd = iMtd;
                IMappingFile.IMethod sMtd = sCls.getMethod(iMtd.getOriginal(), iMtd.getDescriptor());
                IMappingBuilder.IMethod bMtd = bCls.method(iMtd.getDescriptor(), iMtd.getOriginal(), iMtd.getMapped(), yMtd.getMapped(), sMtd.getMapped());

                copyMeta(yMtd, bMtd);

                IMappingFile.IMethod finalYMtd = yMtd;
                iMtd.getParameters().forEach(iPar -> {
                    Optional<? extends IMappingFile.IParameter> yParOpt = finalYMtd.getParameters().stream().filter(p -> p.getIndex() == iPar.getIndex()).findFirst();
                    if(!yParOpt.isPresent()) yParOpt = Optional.of(iPar);
                    IMappingFile.IParameter yPar = yParOpt.get();
                    IMappingFile.IParameter sPar = sMtd.getParameters().stream().filter(p -> p.getIndex() == iPar.getIndex()).findFirst().get();
                    IMappingBuilder.IParameter bPar = bMtd.parameter(iPar.getIndex(), iPar.getOriginal(), iPar.getMapped(), yPar.getMapped(), sPar.getMapped());

                    copyMeta(yPar, bPar);
                });
            });
        });

        project.getLogger().lifecycle("Merged mappings. (Took {}ms)", System.currentTimeMillis()-startTime);

        return builder.build();
    }

    private void copyMeta(IMappingFile.IPackage source, IMappingBuilder.IPackage target) {
        source.getMetadata().forEach(target::meta);
    }

    private void copyMeta(IMappingFile.IClass source, IMappingBuilder.IClass target) {
        source.getMetadata().forEach(target::meta);
    }

    private void copyMeta(IMappingFile.IField source, IMappingBuilder.IField target) {
        source.getMetadata().forEach(target::meta);
    }

    private void copyMeta(IMappingFile.IMethod source, IMappingBuilder.IMethod target) {
        source.getMetadata().forEach(target::meta);
    }

    private void copyMeta(IMappingFile.IParameter source, IMappingBuilder.IParameter target) {
        source.getMetadata().forEach(target::meta);
    }

    private String findComment(Map<String, String> meta) {
        return meta.getOrDefault("comment", "");
    }

    @NotNull
    private File cacheYarnSrg(Project project, YarnVersion version, String etx) {
        return Utils.getCache(project, "net", "fabricmc", "yarn-srg", version.getVersion(), "." + etx);
    }

    /*
        @Nullable
        @Override
        public File getMappingsFile(@NotNull MCPRepo mcpRepo,
                                    @NotNull Project project,
                                    @NotNull String mappingsChannel,
                                    @NotNull String mappingsVersion) throws IOException {
            YarnVersion version = YarnVersion.of(mappingsVersion);

            File proguardFile = MavenArtifactDownloader.generate(project, "net.minecraft:client:" + version.getMinecraftVersion() + ":mappings@txt", true);
            if (proguardFile == null)
                throw new IllegalStateException("Could not create " + version.getMinecraftVersion() + " yarn conversion due to missing ProGuard mappings.");

            File mcpFile = MavenArtifactDownloader.manual(project, "de.oceanlabs.mcp:mcp_config:" + version.getMinecraftVersion() + "@zip", false);
            MCPConfigV2 mcp = MCPConfigV2.getFromArchive(mcpFile);

            IMappingFile parsedProguard = IMappingFile.load(proguardFile);
            IMappingFile obfToSrg = IMappingFile.load(new ByteArrayInputStream(Utils.getZipData(mcpFile, mcp.getData("mappings"))));


            File yarnFile = MavenArtifactDownloader.gradle(project, version.getArtifact(), true);
            if(yarnFile == null)
                throw new IllegalStateException("Could not download Yarn artifact " + version.getArtifact());

            File tempFile = new File(project.getRootDir(), "mappings.txt");
    //        copyFileUsingStream(proguardFile, tempFile);

            try(Reader yarnMappings = getMappingsFileFromJar(new ZipFile(yarnFile))) {
                MappingFormat format = MappingReader.detectFormat(yarnMappings);
                project.getLogger().info("Format: {}", format);

                MemoryMappingTree proguardMappingsTree = new MemoryMappingTree();
                MemoryMappingTree yarnMappingsTree = new MemoryMappingTree();

                project.getLogger().info("Creating " + MappingReader.detectFormat(yarnMappings).name() + " mappings from " + MappingReader.detectFormat(proguardFile.toPath()).name());

                MappingReader.read(proguardFile.toPath(), proguardMappingsTree);
                MappingReader.read(yarnMappings, yarnMappingsTree);

                try(MappingWriter writer = MappingWriter.create(Paths.get("cringe.jar"), MappingFormat.MCP)) {
                    writer.
                }

            } catch (Exception e) {
                project.getLogger().error("Error while generating Yarn mappings.", e);
            }

            return proguardFile;
        }
    */
    private InputStream getTinyV2Mappings(ZipFile mappingsJar) throws IOException {
        ZipEntry entry = mappingsJar.getEntry("mappings/mappings.tiny");
        if (entry == null)
            throw new IllegalStateException("Jar does not contain mappings file.");

        return mappingsJar.getInputStream(entry);
    }

    protected void writeCsv(String name, List<List<String>> mappings, Path rootPath) throws IOException {
        if (mappings.size() <= 1)
            return;
        Path csvPath = rootPath.resolve(name);
        try (CsvWriter writer = CsvWriter.builder().lineDelimiter(LineDelimiter.LF).build(csvPath, StandardCharsets.UTF_8)) {
            mappings.forEach(writer::writeRow);
        }
        Files.setLastModifiedTime(csvPath, FileTime.fromMillis(ZIPTIME));
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        try (InputStream is = Files.newInputStream(source.toPath()); OutputStream os = Files.newOutputStream(dest.toPath())) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }
}
