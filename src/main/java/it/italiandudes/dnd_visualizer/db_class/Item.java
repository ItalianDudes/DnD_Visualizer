package it.italiandudes.dnd_visualizer.db_class;

@SuppressWarnings("unused")
public class Item {

    //Attributes
    private final String name;
    private final String lore;
    private final int requiredLevel;
    private final int requiredKnowledge;
    private final String rarity;
    private final String cost;
    private final double weight;
    private final String properties;

    //Constructors
    public Item(String name, String lore, int requiredLevel, int requiredKnowledge, String rarity, String cost, double weight, String properties) {
        this.name = name;
        this.lore = lore;
        this.requiredLevel = requiredLevel;
        this.requiredKnowledge = requiredKnowledge;
        this.rarity = rarity;
        this.cost = cost;
        this.weight = weight;
        this.properties = properties;
    }

    //Methods
    public String getName() {
        return name;
    }
    public String getLore() {
        return lore;
    }
    public int getRequiredLevel() {
        return requiredLevel;
    }
    public int getRequiredKnowledge() {
        return requiredKnowledge;
    }
    public String getRarity() {
        return rarity;
    }
    public String getCost() {
        return cost;
    }
    public double getWeight() {
        return weight;
    }
    public String getProperties() {
        return properties;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item1 = (Item) o;

        if (getRequiredLevel() != item1.getRequiredLevel()) return false;
        if (getRequiredKnowledge() != item1.getRequiredKnowledge()) return false;
        if (Double.compare(item1.getWeight(), getWeight()) != 0) return false;
        if (!getName().equals(item1.getName())) return false;
        if (!getLore().equals(item1.getLore())) return false;
        if (!getRarity().equals(item1.getRarity())) return false;
        if (!getCost().equals(item1.getCost())) return false;
        return getProperties().equals(item1.getProperties());
    }
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getName().hashCode();
        result = 31 * result + getLore().hashCode();
        result = 31 * result + getRequiredLevel();
        result = 31 * result + getRequiredKnowledge();
        result = 31 * result + getRarity().hashCode();
        result = 31 * result + getCost().hashCode();
        temp = Double.doubleToLongBits(getWeight());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getProperties().hashCode();
        return result;
    }
    @Override
    public String toString() {
        return "Item{" +
                "item='" + name + '\'' +
                ", lore='" + lore + '\'' +
                ", requiredLevel=" + requiredLevel +
                ", requiredKnowledge=" + requiredKnowledge +
                ", rarity='" + rarity + '\'' +
                ", cost='" + cost + '\'' +
                ", weight=" + weight +
                ", properties='" + properties + '\'' +
                '}';
    }
}
