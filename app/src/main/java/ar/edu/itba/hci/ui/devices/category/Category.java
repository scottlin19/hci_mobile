package ar.edu.itba.hci.ui.devices.category;

public class Category {

    private String name;
    private int thumbnail;

    public Category(String name, int thumbnail){
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public Category(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int img_id) {
        this.thumbnail = img_id;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", thumbnail=" + thumbnail +
                '}';
    }
}
