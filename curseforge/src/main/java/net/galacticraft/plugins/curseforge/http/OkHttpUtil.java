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

package net.galacticraft.plugins.curseforge.http;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.gradle.api.Project;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.initialization.layout.ProjectCacheDir;

import lombok.var;
import net.galacticraft.plugins.curseforge.curse.json.CurseError;
import net.galacticraft.plugins.curseforge.curse.json.CurseReponse;
import net.galacticraft.plugins.curseforge.curse.json.ReturnReponse;
import net.galacticraft.plugins.curseforge.util.Util;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpUtil {
	
	public static OkHttpUtil instance;
	
	private OkHttpClient client;
    private Cache okHttpCache;
    private Map<String, String> headerMap;
	
    private OkHttpUtil() {}
    
    private OkHttpUtil(Project project) {
		super();
		project.getLogger();
		this.setupCache(project);
		this.setupClient(project);
		this.headerMap = new HashMap<>();
	}
	
	public static void setup(Project project) {
		OkHttpUtil util = new OkHttpUtil(project);
		OkHttpUtil.instance = util;
	}

	private void setupCache(Project project) {
        if (project == project.getRootProject()) {
            ProjectCacheDir projectCacheDir = ((ProjectInternal) project).getServices().get(ProjectCacheDir.class);
            this.okHttpCache = new Cache(new File(projectCacheDir.getDir(), getClass().getName()), 50 * 1024 * 1024);
        }
	}
	
	private void setupClient(Project project) {
        if (this.client == null) {

            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .cache(this.okHttpCache);

            if (project.getGradle().getStartParameter().isOffline()) {
                builder = builder.addInterceptor(new ForceCacheInterceptor());
            }

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(project.getLogger()::info);
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder = builder.addInterceptor(loggingInterceptor);

            this.client = builder.build();
        }
	}
	
	
	
	public String get(final String url) {
		try {
			var request = new Request.Builder().url(url);
			headerMap.forEach((k,v) -> {
				request.addHeader(k, v);
			});
			try(var r = client.newCall(request.build()).execute()) {
				return r.body().string();
			}
		} catch (Exception e) {
		}
		return null;
	}
	
	public ReturnReponse post(MultipartBody.Builder builder, final String url) {
		ReturnReponse reponse;
		try {
			var request = new Request.Builder().url(url);
			headerMap.forEach((k,v) -> {
				request.addHeader(k, v);
			});
			request.post(builder.build());
	        try(var r = client.newCall(request.build()).execute()) {
	        	if(r.code() == 200) {
	        		reponse = Util.getGson().fromJson(r.body().string(), CurseReponse.class);
	        	} else {
					reponse = Util.getGson().fromJson(r.body().string(), CurseError.class);
				}
	        }
		} catch (Exception e) {
			reponse = new CurseError().code(000).message("Exception trying to fetch from " + url);
        }
		return reponse;
	}
	
	public void addHeader(String key, String value) {
		headerMap.put(key, value);
	}
	
    private static class ForceCacheInterceptor implements Interceptor {
        @Nonnull
        @Override
        public Response intercept(@Nonnull Chain chain) throws IOException {
            Request newRequest = chain.request()
                    .newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();

            return chain.proceed(newRequest);
        }
    }
}
