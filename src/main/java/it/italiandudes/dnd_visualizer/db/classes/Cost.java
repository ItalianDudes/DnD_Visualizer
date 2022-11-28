package it.italiandudes.dnd_visualizer.db.classes;

import it.italiandudes.dnd_visualizer.db.enums.Coin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Set;

@SuppressWarnings("unused")
public final class Cost {

    //Attributes
    @NotNull private final HashMap<Coin, Integer> costMap;

    //Constructors
    public Cost(@NotNull HashMap<Coin, Integer> costMap){
        this.costMap = costMap;
    }
    public Cost(){
        this.costMap = new HashMap<>();
    }
    public Cost(@NotNull Coin coinType, int amount) {
        costMap = new HashMap<>();
        addCost(coinType, amount);
    }

    //Methods
    private void refactorCosts(){
        int totalCost = getCostByCoinType(Coin.COPPER);
        makeFree();
        Coin[] coinTypes = Coin.values();
        for(int i=coinTypes.length-1; i>=0; i--){
            costMap.put(coinTypes[i], totalCost/coinTypes[i].getSingleCoinValue());
            totalCost/=coinTypes[i].getSingleCoinValue();
        }
    }
    public void setCost(Coin coinType, int amount){
        makeFree();
        costMap.put(coinType, amount);
        refactorCosts();
    }
    public void addCost(Coin coinType, int amount){
        if(amount<0) throw new RuntimeException("The amount is less than 0");
        if(costMap.containsKey(coinType))
            amount+=costMap.get(coinType);
        costMap.put(coinType, amount);
        refactorCosts();
    }
    public boolean removeCost(Coin coinType, int amount){
        if(amount<0) throw new RuntimeException("The amount is less than 0");
        int actualCost = getCostByCoinType(Coin.COPPER);
        int removeCost = coinType.getSingleCoinValue()*amount;
        if(actualCost-removeCost < 0) return false;
        setCost(Coin.COPPER, actualCost-removeCost);
        return true;
    }
    public void makeFree(){
        costMap.clear();
    }
    public int getCostByCoinType(Coin coinType){

        if(costMap.isEmpty()) return 0;
        int finalCost = 0;
        Set<Coin> coinSet = costMap.keySet();

        for(Coin coin : coinSet){
            finalCost += costMap.get(coin)*coin.getSingleCoinValue();
        }

        return finalCost/coinType.getSingleCoinValue();

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cost cost = (Cost) o;

        return costMap.equals(cost.costMap);
    }
    @Override
    public int hashCode() {
        return costMap.hashCode();
    }
    @Override
    public String toString() {
        return String.valueOf(getCostByCoinType(Coin.COPPER));
    }
}
