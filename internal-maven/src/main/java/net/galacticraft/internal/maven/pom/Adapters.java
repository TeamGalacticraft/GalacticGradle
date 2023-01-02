package net.galacticraft.internal.maven.pom;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.galacticraft.internal.maven.pom.PomJsonSpec.Role;

final class Adapters
{
	static class RoleListSerialization implements JsonSerializer<List<Role>>, JsonDeserializer<List<Role>>
	{
		@Override
		public JsonElement serialize(List<Role> src, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonArray jsonRole = new JsonArray();

			for (Role role : src)
			{
				jsonRole.add("" + role.getName());
			}

			return jsonRole;
		}

		@Override
		public List<Role> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			List<Role> roles = new ArrayList<>();
			for (JsonElement e : json.getAsJsonArray())
			{
				roles.add(Role.builder().name(e.getAsString()).build());
			}
			return roles;
		}
	}
}
