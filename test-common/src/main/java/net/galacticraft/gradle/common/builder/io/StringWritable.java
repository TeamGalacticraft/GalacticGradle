package net.galacticraft.gradle.common.builder.io;

public class StringWritable implements FileWritable {
    private String str;

    public StringWritable(String str) {
        this.str = str;
    }

    @Override
    public void write(FileBuilder fileBuilder) {
        fileBuilder.append(str);
    }
}
