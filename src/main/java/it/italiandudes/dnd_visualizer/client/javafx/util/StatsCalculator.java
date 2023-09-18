package it.italiandudes.dnd_visualizer.client.javafx.util;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

@SuppressWarnings("unused")
public final class StatsCalculator {

    public static int calculateModifier(String statString) {
        return calculateModifier(Integer.parseInt(statString));
    }
    public static int calculateModifier(int stat) {
        return (int) Math.floor((stat-10.0)/2.0);
    }
    public static int calculateAbilityValue(int mod, int proficiencyBonus, RadioButton proficiency, RadioButton mastery) {
        if (mastery.isSelected()) {
            return mod + proficiencyBonus*2;
        } else if (proficiency.isSelected()) {
            return mod + proficiencyBonus;
        } else {
            return mod;
        }
    }
    public static void setNewAbilityValue(Label abilityLabel, int mod, int proficiencyBonus, RadioButton proficiency, RadioButton mastery) {
        abilityLabel.setText(String.valueOf(calculateAbilityValue(mod, proficiencyBonus, proficiency, mastery)));
    }

}
