package commons;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

/**
 * The Tag Class used to store
 * the tag information regarding an expense
 */
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private int red;

    private int green;

    private int blue;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses;

    /**
     * General constructor
     * @param name - label name
     * @param red - int
     * @param green - int
     * @param blue - int
     *
     */
    public Tag(String name, int red, int green, int blue) {
        this.name = name;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * Overloaded constructor
     * @param name - label name
     * @param red - int
     * @param green - int
     * @param blue - int
     * @param expenses - list of expenses
     */
    public Tag(String name, int red, int green, int blue, List<Expense> expenses) {
        this.name = name;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.expenses = expenses;
    }

    /**
     * No arg constructor for jakarta framework
     */
    public Tag() {}

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if ((id != tag.id) || (red != tag.red)|| (green != tag.green)) return false;
        if (blue != tag.blue) return false;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + red;
        result = 31 * result + green;
        result = 31 * result + blue;
        result = 31 * result + (expenses != null ? expenses.hashCode() : 0);
        return result;
    }
}
