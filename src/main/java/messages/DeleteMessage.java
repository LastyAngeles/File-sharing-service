package messages;

import java.io.Serializable;

public class DeleteMessage extends Message implements Serializable {

    private String directory;

    public DeleteMessage(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}