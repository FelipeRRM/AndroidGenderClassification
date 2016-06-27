package feliperrm.trabalhoic.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by felip on 26/06/2016.
 */
public class Category implements Serializable {

    String name;
    ArrayList<String> files;

    public Category(){}

    public Category(String name, ArrayList<String> files) {
        this.name = name;
        this.files = files;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }
}
