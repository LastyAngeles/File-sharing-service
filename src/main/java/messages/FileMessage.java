package messages;
import java.io.Serializable;

public class FileMessage extends Message implements Serializable{
    private String name;
    private byte[] dataBlock;
    private String login;
    private String purpose;

    public FileMessage(String name, byte[] dataBlock, String login, String purpose) {
        this.name = name;
        this.dataBlock = dataBlock;
        this.login = login;
        this.purpose = purpose;
    }

    public byte[] getDataBlock() {
        return dataBlock;
    }

    public void setDataBlock(byte[] block) {
        this.dataBlock = block;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}