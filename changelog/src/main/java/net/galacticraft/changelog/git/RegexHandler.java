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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jgit.revwalk.RevCommit;

import lombok.Getter;

public class RegexHandler
{
    private final String regexString = "^(?<type>%s)(?:\\((?<scope>[\\w-]+)\\))?(?<breaking>!)?:\\s(?<message>.+)$";
    private Pattern regexPattern;
    private Matcher matcher;
    
    @Getter
    private boolean found;
    
    public RegexHandler(List<String> typeList)
    {
        this.regexPattern = Pattern.compile(String.format(regexString, String.join("|", typeList)));
    }
    
    public void parseCommit(RevCommit commit)
    {
        this.matcher = this.regexPattern.matcher(commit.getShortMessage());
        this.found = this.matcher.find();
    }
    
    public String getFullMatch()
    {
        return matcher.group(0);
    }
    
    public String getType()
    {
        return matcher.group("type");
    }
    
    public String getScope()
    {
        return matcher.group("scope") != null ? matcher.group("scope") : null;
    }
    
    public boolean isBreakingChange()
    {
        return matcher.group("breaking") != null;
    }
    
    public String getMessage()
    {
        return matcher.group("message");
    }
}
