package net.galacticraft.plugins.curseforge.internal.json;

public class CurseError {
    @Override
    public String toString() {
        return "CurseError{" + "errorCode=" + errorCode + ", errorMessage='" + errorMessage + "\'" + "}";
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * The HTTP status code
     */
    private int errorCode;
    /**
     * The error message
     */
    private String errorMessage;
}
