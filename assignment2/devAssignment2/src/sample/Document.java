package sample;

public class Document {
    private String clientFile;
    private String serverFile;

    public Document(String clientFile, String serverFile) {
        this.clientFile = clientFile;
        this.serverFile = serverFile;
    }

    public String getClientFile() { return this.clientFile; }
    public String getServerFile() { return this.serverFile; }

    public void setClientFile(String clientFile) { this.clientFile = clientFile; }
    public void setServerFile(String serverFile) { this.serverFile = serverFile; }
}