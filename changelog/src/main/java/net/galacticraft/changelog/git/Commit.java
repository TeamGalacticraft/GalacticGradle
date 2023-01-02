/*
 * This file is part of GalacticGradle, licensed under the MIT License (MIT).
 *
 * Copyright (c) TeamGalacticraft <https://galacticraft.net>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.galacticraft.changelog.git;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jetbrains.annotations.Nullable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Commit
{
    private String gitHash;

    private String fullCommitText;

    @Builder.ObtainVia(method = "configureType")
    private ConventionType type;

    private String actualType;

    @Nullable
    private String scope;

    private boolean isBreakingChange;

    private String message;

    @Override
    public String toString()
    {
        //@off
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
            append("Hash", gitHash).
            append("Text", fullCommitText).
            append("ConventionType", type.toString()).
            append("Type", actualType).
            append("Scope", scope == null ? "" : scope).
            append("BreakingChange", isBreakingChange).
            append("Message", message).
            toString();
        //@on
    }

    public ConventionType configureType()
    {
        ConventionType type;
        if (isBreakingChange)
            type = ConventionType.BREAKING_CHANGES;
        else if (actualType.equals("feat"))
            type = ConventionType.FEATURES;
        else if (actualType.equals("fix"))
            type = ConventionType.FIXES;
        else
            type = ConventionType.MISC;
        
        return type;
    }
}
