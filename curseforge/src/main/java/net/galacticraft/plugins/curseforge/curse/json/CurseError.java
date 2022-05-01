package net.galacticraft.plugins.curseforge.curse.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurseError extends ReturnReponse {

	@Expose
	@SerializedName("errorCode")
	private int errorCode;

	@Expose
	@SerializedName("errorMessage")
	private String errorMessage;
	
	public CurseError message(String message) {
		this.errorMessage = message;
		return this;
	}
	
	public CurseError code(int code) {
		this.errorCode = code;
		return this;
	}

	public int code() {
		return errorCode;
	}

	public String message() {
		return errorMessage;
	}
}
