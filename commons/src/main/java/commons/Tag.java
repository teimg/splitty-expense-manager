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

    private int rgb;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses;

    /**
     * General constructor
     * @param name - label name
     * @param rgb - int
     */
    public Tag(String name, int rgb) {
        this.name = name;
        this.rgb = rgb;
    }

    /**
     * Overloaded constructor
     * @param name - label name
     * @param rgb - int
     * @param expenses - list of expenses
     */
    public Tag(String name, int rgb, List<Expense> expenses) {
        this.name = name;
        this.rgb = rgb;
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

    public int getRgb() {
        return rgb;
    }

    public void setRgb(int rgb) {
        this.rgb = rgb;
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

    /**
     * Standard equals
     * @param o other
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (id != tag.id) return false;
        if (rgb != tag.rgb) return false;
        if (!Objects.equals(name, tag.name)) return false;
        return Objects.equals(expenses, tag.expenses);
    }

    /**
     * Standard hashcode
     * @return int
     */
    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + rgb;
        result = 31 * result + (expenses != null ? expenses.hashCode() : 0);
        return result;
    }
}
