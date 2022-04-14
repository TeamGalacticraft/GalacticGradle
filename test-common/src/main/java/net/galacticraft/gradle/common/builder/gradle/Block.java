package net.galacticraft.gradle.common.builder.gradle;

public class Block {
    private StringBuilder stringBuilder = new StringBuilder();
    private String indentation = "";

    public Block indent() {
        indentation += "\t";
        return this;
    }

    public Block unindent() {
        indentation = indentation.substring(0, indentation.length() - 1);
        return this;
    }

    public Block append(String str) {
        stringBuilder.append(str);
        return this;
    }

    public Block append(int i) {
        stringBuilder.append(i);
        return this;
    }

    public Block append(double d) {
        stringBuilder.append(d);
        return this;
    }

    public Block append(char c) {
        stringBuilder.append(c);
        return this;
    }

    public Block append(boolean b) {
        stringBuilder.append(b);
        return this;
    }

    public Block append(long l) {
        stringBuilder.append(l);
        return this;
    }

    public Block append(short s) {
        stringBuilder.append(s);
        return this;
    }

    public Block append(Object obj) {
        stringBuilder.append(obj);
        return this;
    }

    public Block append(float f) {
        stringBuilder.append(f);
        return this;
    }

    public Block append(char[] c) {
        stringBuilder.append(c);
        return this;
    }

    public Block append(StringBuffer sb) {
        stringBuilder.append(sb);
        return this;
    }

    public Block appendNewLine() {
        stringBuilder.append("\n").append(indentation);
        return this;
    }
    
    public String complete() {
    	return stringBuilder.toString();
    }
}
