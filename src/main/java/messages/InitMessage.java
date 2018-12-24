package messages;

import java.io.Serializable;
import java.util.List;

public class InitMessage extends Message implements Serializable {
    private String directoryName;
    private List<String> pathsList;

    public InitMessage(String directoryName) {
        this.directoryName = "received\\" + directoryName;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public List<String> getPathsList() {
        return pathsList;
    }

    public void setPathsList(List<String> pathsList) {
        this.pathsList = pathsList;
    }
}
