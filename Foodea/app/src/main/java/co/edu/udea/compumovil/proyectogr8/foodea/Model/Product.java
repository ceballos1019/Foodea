package co.edu.udea.compumovil.proyectogr8.foodea.Model;

/**
 * Created by user on 5/05/2016.
 */
public class Product {

    private int id;
    private String name;
    private String type;
    private String category;

    //Constructor without parameters
    public Product(){};

    //Constructors with parameters
    public Product(int id, String name, String type, String category) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
    }

    public Product(String name, String type, String category) {
        this.name = name;
        this.type = type;
        this.category = category;
    }

    //Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
