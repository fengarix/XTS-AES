package id.crypto;

/**
 * Created by ilhamkurniawan on 4/23/2016.
 */
public class TargetFiles {

    private String sourceName;
    private String sourcePath;
    private String keyPath;

    public void TargetFiles(){}

    public void TargetFiles(String sourceName, String sourcePath, String keyPath){
        this.sourceName = sourceName;
        this.sourcePath = sourcePath;
        this.keyPath = keyPath;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getKeyPath() {
        return keyPath;
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }
}
