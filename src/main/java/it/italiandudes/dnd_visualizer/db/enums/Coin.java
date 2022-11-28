package it.italiandudes.dnd_visualizer.db.enums;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public enum Coin {

    //Enum
    COPPER(1),
    SILVER(10),
    ELECTRUM(50),
    GOLD(100),
    PLATINUM(1000);

    //Attributes
    private final int singleCoinValue;

    //Constructors
    Coin(int value){
        this.singleCoinValue = value;
    }

    //Methods
    public int getSingleCoinValue(){
        return singleCoinValue;
    }
    public static int getCoinAmountConvertingTo(@NotNull Coin coin, int coinAmount, @NotNull Coin otherCoin){
        double coinValue = coin.singleCoinValue*coinAmount; //Value in the lowest type possible
        return (int) (coinValue/otherCoin.singleCoinValue);
    }
    @Override
    public String toString() {
        return String.valueOf(singleCoinValue);
    }
}
