package it.italiandudes.dnd_visualizer.javafx.controllers.sheetviewer;

import it.italiandudes.dnd_visualizer.data.enums.*;
import it.italiandudes.dnd_visualizer.data.item.Addon;
import it.italiandudes.dnd_visualizer.data.item.Armor;
import it.italiandudes.dnd_visualizer.data.item.Weapon;
import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.javafx.alerts.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.controllers.ControllerSceneSheetViewer;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public final class TabEquipment {

    // Attributes
    public static final Armor ARMOR_EMPTY = new Armor(ArmorSlot.NO_ARMOR);
    public static final Addon ADDON_EMPTY = new Addon(AddonSlot.NO_ADDON);


    // Methods
    private static ObservableList<Armor> getEmptyArmorList() {
        ObservableList<Armor> EMPTY_LIST = FXCollections.observableList(new ArrayList<>());
        EMPTY_LIST.add(ARMOR_EMPTY);
        return EMPTY_LIST;
    }
    private static ObservableList<Addon> getEmptyAddonList() {
        ObservableList<Addon> EMPTY_LIST = FXCollections.observableList(new ArrayList<>());
        EMPTY_LIST.add(ADDON_EMPTY);
        return EMPTY_LIST;
    }

    // Initialize
    public static void initialize(@NotNull final ControllerSceneSheetViewer controller) {
        reloadEquipment(controller);
        setOnChangeTriggers(controller);
    }

    // OnChange Triggers Setter
    private static void setOnChangeTriggers(@NotNull final ControllerSceneSheetViewer controller) {
        controller.comboBoxEquipmentHead.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ARMOR_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentNecklace.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ADDON_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentFullSet.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ARMOR_EMPTY)) newValue.setEquipped(true);
            TabEquipment.equipmentToggleFullSet(controller);
        }));
        controller.comboBoxEquipmentLeftShoulder.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ARMOR_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentLeftArm.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ARMOR_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentLeftForearm.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ARMOR_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentLeftHand.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ARMOR_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentMantle.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ADDON_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentBack.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ARMOR_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentLeftLeg.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ARMOR_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentLeftKnee.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ARMOR_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentLeftFoot.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ARMOR_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentLeftEarring.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ADDON_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentLeftBracelet.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ADDON_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentRing1.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ADDON_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentRing2.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ADDON_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentRightShoulder.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ARMOR_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentRightArm.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ARMOR_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentRightForearm.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ARMOR_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentRightHand.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ARMOR_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentChest.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ARMOR_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentAbdomen.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ARMOR_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentRightLeg.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ARMOR_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentRightKnee.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ARMOR_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentRightFoot.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ARMOR_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentRightEarring.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ADDON_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentRightBracelet.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ADDON_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentRing3.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ADDON_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentRing4.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ADDON_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        }));
        controller.comboBoxEquipmentBackpack.getSelectionModel().selectedItemProperty().addListener((((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ADDON_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        })));
        controller.comboBoxEquipmentBelt.getSelectionModel().selectedItemProperty().addListener((((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ADDON_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        })));
        controller.comboBoxEquipmentBandolier.getSelectionModel().selectedItemProperty().addListener((((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(ADDON_EMPTY)) newValue.setEquipped(true);
            TabEquipment.updateEquipmentProperties(controller);
        })));
    }

    // EDT
    private static void setEmpty(@NotNull final ControllerSceneSheetViewer controller) {
        controller.comboBoxEquipmentFullSet.setItems(getEmptyArmorList());
        controller.comboBoxEquipmentFullSet.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentHead.setItems(getEmptyArmorList());
        controller.comboBoxEquipmentHead.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentNecklace.setItems(getEmptyAddonList());
        controller.comboBoxEquipmentNecklace.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentLeftShoulder.setItems(getEmptyArmorList());
        controller.comboBoxEquipmentLeftShoulder.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentLeftArm.setItems(getEmptyArmorList());
        controller.comboBoxEquipmentLeftArm.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentLeftForearm.setItems(getEmptyArmorList());
        controller.comboBoxEquipmentLeftForearm.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentLeftHand.setItems(getEmptyArmorList());
        controller.comboBoxEquipmentLeftHand.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentMantle.setItems(getEmptyAddonList());
        controller.comboBoxEquipmentMantle.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentBack.setItems(getEmptyArmorList());
        controller.comboBoxEquipmentBack.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentLeftLeg.setItems(getEmptyArmorList());
        controller.comboBoxEquipmentLeftLeg.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentLeftKnee.setItems(getEmptyArmorList());
        controller.comboBoxEquipmentLeftKnee.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentLeftFoot.setItems(getEmptyArmorList());
        controller.comboBoxEquipmentLeftFoot.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentLeftEarring.setItems(getEmptyAddonList());
        controller.comboBoxEquipmentLeftEarring.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentLeftBracelet.setItems(getEmptyAddonList());
        controller.comboBoxEquipmentLeftBracelet.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRing1.setItems(getEmptyAddonList());
        controller.comboBoxEquipmentRing1.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRing2.setItems(getEmptyAddonList());
        controller.comboBoxEquipmentRing2.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRightShoulder.setItems(getEmptyArmorList());
        controller.comboBoxEquipmentRightShoulder.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRightArm.setItems(getEmptyArmorList());
        controller.comboBoxEquipmentRightArm.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRightForearm.setItems(getEmptyArmorList());
        controller.comboBoxEquipmentRightForearm.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRightHand.setItems(getEmptyArmorList());
        controller.comboBoxEquipmentRightHand.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentChest.setItems(getEmptyArmorList());
        controller.comboBoxEquipmentChest.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentAbdomen.setItems(getEmptyArmorList());
        controller.comboBoxEquipmentAbdomen.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRightLeg.setItems(getEmptyArmorList());
        controller.comboBoxEquipmentRightLeg.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRightKnee.setItems(getEmptyArmorList());
        controller.comboBoxEquipmentRightKnee.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRightFoot.setItems(getEmptyArmorList());
        controller.comboBoxEquipmentRightFoot.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRightEarring.setItems(getEmptyAddonList());
        controller.comboBoxEquipmentRightEarring.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRightBracelet.setItems(getEmptyAddonList());
        controller.comboBoxEquipmentRightBracelet.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRing3.setItems(getEmptyAddonList());
        controller.comboBoxEquipmentRing3.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRing4.setItems(getEmptyAddonList());
        controller.comboBoxEquipmentRing4.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentBackpack.setItems(getEmptyAddonList());
        controller.comboBoxEquipmentBackpack.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentBandolier.setItems(getEmptyAddonList());
        controller.comboBoxEquipmentBandolier.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentBelt.setItems(getEmptyAddonList());
        controller.comboBoxEquipmentBelt.getSelectionModel().selectFirst();
        controller.listViewEquippedWeapons.getItems().clear();
    }
    public static void reloadEquipment(@NotNull final ControllerSceneSheetViewer controller) {
        Armor head = controller.comboBoxEquipmentHead.getSelectionModel().getSelectedItem();
        Addon necklace = controller.comboBoxEquipmentNecklace.getSelectionModel().getSelectedItem();
        Armor fullSet = controller.comboBoxEquipmentFullSet.getSelectionModel().getSelectedItem();
        Armor leftShoulder = controller.comboBoxEquipmentLeftShoulder.getSelectionModel().getSelectedItem();
        Armor leftArm = controller.comboBoxEquipmentLeftArm.getSelectionModel().getSelectedItem();
        Armor leftForearm = controller.comboBoxEquipmentLeftForearm.getSelectionModel().getSelectedItem();
        Armor leftHand = controller.comboBoxEquipmentLeftHand.getSelectionModel().getSelectedItem();
        Addon mantle = controller.comboBoxEquipmentMantle.getSelectionModel().getSelectedItem();
        Armor back = controller.comboBoxEquipmentBack.getSelectionModel().getSelectedItem();
        Armor leftLeg = controller.comboBoxEquipmentLeftLeg.getSelectionModel().getSelectedItem();
        Armor leftKnee = controller.comboBoxEquipmentLeftKnee.getSelectionModel().getSelectedItem();
        Armor leftFoot = controller.comboBoxEquipmentLeftFoot.getSelectionModel().getSelectedItem();
        Addon leftEarring = controller.comboBoxEquipmentLeftEarring.getSelectionModel().getSelectedItem();
        Addon leftBracelet = controller.comboBoxEquipmentLeftBracelet.getSelectionModel().getSelectedItem();
        Addon ring1 = controller.comboBoxEquipmentRing1.getSelectionModel().getSelectedItem();
        Addon ring2 = controller.comboBoxEquipmentRing2.getSelectionModel().getSelectedItem();
        Armor rightShoulder = controller.comboBoxEquipmentRightShoulder.getSelectionModel().getSelectedItem();
        Armor rightArm = controller.comboBoxEquipmentRightArm.getSelectionModel().getSelectedItem();
        Armor rightForearm = controller.comboBoxEquipmentRightForearm.getSelectionModel().getSelectedItem();
        Armor rightHand = controller.comboBoxEquipmentRightHand.getSelectionModel().getSelectedItem();
        Armor chest = controller.comboBoxEquipmentChest.getSelectionModel().getSelectedItem();
        Armor abdomen = controller.comboBoxEquipmentAbdomen.getSelectionModel().getSelectedItem();
        Armor rightLeg = controller.comboBoxEquipmentRightLeg.getSelectionModel().getSelectedItem();
        Armor rightKnee = controller.comboBoxEquipmentRightKnee.getSelectionModel().getSelectedItem();
        Armor rightFoot = controller.comboBoxEquipmentRightFoot.getSelectionModel().getSelectedItem();
        Addon rightEarring = controller.comboBoxEquipmentRightEarring.getSelectionModel().getSelectedItem();
        Addon rightBracelet = controller.comboBoxEquipmentRightBracelet.getSelectionModel().getSelectedItem();
        Addon ring3 = controller.comboBoxEquipmentRing3.getSelectionModel().getSelectedItem();
        Addon ring4 = controller.comboBoxEquipmentRing4.getSelectionModel().getSelectedItem();
        Addon backpack = controller.comboBoxEquipmentBackpack.getSelectionModel().getSelectedItem();
        Addon belt = controller.comboBoxEquipmentBelt.getSelectionModel().getSelectedItem();
        Addon bandolier = controller.comboBoxEquipmentBandolier.getSelectionModel().getSelectedItem();
        setEmpty(controller);
        equipmentToggleFullSet(controller);
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        PreparedStatement ps = null;
                        String query;
                        try {
                            query = "SELECT i.name AS name, e.type AS type FROM items AS i JOIN equipments AS e ON i.id = e.item_id WHERE i.category=? AND i.quantity>0;";
                            ps = DBManager.preparedStatement(query);
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            ps.setInt(1, Category.EQUIPMENT.getDatabaseValue());
                            ResultSet result = ps.executeQuery();

                            ArrayList<Armor> armors = new ArrayList<>();
                            ArrayList<Weapon> weapons = new ArrayList<>();
                            ArrayList<Addon> addons = new ArrayList<>();
                            while (result.next()) {
                                EquipmentType type = EquipmentType.values()[result.getInt("type")];
                                switch (type) {
                                    case ARMOR:
                                        armors.add(new Armor(result.getString("name")));
                                        break;

                                        case WEAPON:
                                        weapons.add(new Weapon(result.getString("name")));
                                        break;

                                        case ADDON:
                                        addons.add(new Addon(result.getString("name")));
                                        break;
                                }
                            }
                            ps.close();

                            for (Armor armor : armors) {
                                assert armor.getArmorID()!=null;
                                switch (armor.getSlot()) {
                                    case HEAD:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentHead.getItems().add(armor);
                                            if ((head != null && armor.getArmorID().equals(head.getArmorID())) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentHead.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case FULL_SET:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentFullSet.getItems().add(armor);
                                            if ((fullSet != null && armor.getArmorID().equals(fullSet.getArmorID())) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentFullSet.getSelectionModel().select(armor);
                                                equipmentToggleFullSet(controller);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case LEFT_SHOULDER:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentLeftShoulder.getItems().add(armor);
                                            if ((leftShoulder != null && armor.getArmorID().equals(leftShoulder.getArmorID())) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentLeftShoulder.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case LEFT_ARM:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentLeftArm.getItems().add(armor);
                                            if ((leftArm != null && armor.getArmorID().equals(leftArm.getArmorID())) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentLeftArm.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case LEFT_FOREARM:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentLeftForearm.getItems().add(armor);
                                            if ((leftForearm != null && armor.getArmorID().equals(leftForearm.getArmorID())) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentLeftForearm.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case LEFT_HAND:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentLeftHand.getItems().add(armor);
                                            if ((leftHand != null && armor.getArmorID().equals(leftHand.getArmorID())) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentLeftHand.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case BACK:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentBack.getItems().add(armor);
                                            if ((back != null && armor.getArmorID().equals(back.getArmorID())) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentBack.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case LEFT_LEG:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentLeftLeg.getItems().add(armor);
                                            if ((leftLeg != null && armor.getArmorID().equals(leftLeg.getArmorID())) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentLeftLeg.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case LEFT_KNEE:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentLeftKnee.getItems().add(armor);
                                            if ((leftKnee != null && armor.getArmorID().equals(leftKnee.getArmorID())) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentLeftKnee.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case LEFT_FOOT:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentLeftFoot.getItems().add(armor);
                                            if ((leftFoot != null && armor.getArmorID().equals(leftFoot.getArmorID())) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentLeftFoot.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RIGHT_SHOULDER:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRightShoulder.getItems().add(armor);
                                            if ((rightShoulder != null && armor.getArmorID().equals(rightShoulder.getArmorID())) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRightShoulder.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RIGHT_ARM:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRightArm.getSelectionModel().select(armor);
                                            if ((rightArm != null && armor.getArmorID().equals(rightArm.getArmorID())) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRightArm.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RIGHT_FOREARM:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRightForearm.getItems().add(armor);
                                            if ((rightForearm != null && armor.getArmorID().equals(rightForearm.getArmorID())) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRightForearm.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RIGHT_HAND:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRightHand.getItems().add(armor);
                                            if ((rightHand != null && armor.getArmorID().equals(rightHand.getArmorID())) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRightHand.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case CHEST:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentChest.getItems().add(armor);
                                            if ((chest != null && armor.getArmorID().equals(chest.getArmorID())) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentChest.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case ABDOMEN:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentAbdomen.getItems().add(armor);
                                            if ((abdomen != null && armor.getArmorID().equals(abdomen.getArmorID())) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentAbdomen.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RIGHT_LEG:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRightLeg.getItems().add(armor);
                                            if ((rightLeg != null && armor.getArmorID().equals(rightLeg.getArmorID())) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRightLeg.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RIGHT_KNEE:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRightKnee.getItems().add(armor);
                                            if ((rightKnee != null && armor.getArmorID().equals(rightKnee.getArmorID())) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRightKnee.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RIGHT_FOOT:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRightFoot.getItems().add(armor);
                                            if ((rightFoot != null && armor.getArmorID().equals(rightFoot.getArmorID())) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRightFoot.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;
                                }
                            }
                            for (Weapon weapon : weapons) {
                                assert weapon.getWeaponID()!=null;
                                if (weapon.isEquipped()) Platform.runLater(() -> controller.listViewEquippedWeapons.getItems().add(weapon));
                            }
                            for (Addon addon : addons) {
                                assert addon.getAddonID()!=null;
                                switch (addon.getSlot()) {
                                    case NECKLACE:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentNecklace.getItems().add(addon);
                                            if ((necklace != null && addon.getAddonID().equals(necklace.getAddonID())) || addon.isEquipped()) {
                                                controller.comboBoxEquipmentNecklace.getSelectionModel().select(addon);
                                                addon.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case MANTLE:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentMantle.getItems().add(addon);
                                            if ((mantle != null && addon.getAddonID().equals(mantle.getAddonID())) || addon.isEquipped()) {
                                                controller.comboBoxEquipmentMantle.getSelectionModel().select(addon);
                                                addon.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case LEFT_BRACELET:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentLeftBracelet.getItems().add(addon);
                                            if (leftBracelet != null && addon.getAddonID().equals(leftBracelet.getAddonID()) || addon.isEquipped()) {
                                                controller.comboBoxEquipmentLeftBracelet.getSelectionModel().select(addon);
                                                addon.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RIGHT_BRACELET:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRightBracelet.getItems().add(addon);
                                            if (rightBracelet != null && addon.getAddonID().equals(rightBracelet.getAddonID()) || addon.isEquipped()) {
                                                controller.comboBoxEquipmentRightBracelet.getSelectionModel().select(addon);
                                                addon.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case LEFT_EARRING:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentLeftEarring.getItems().add(addon);
                                            if (leftEarring != null && addon.getAddonID().equals(leftEarring.getAddonID()) || addon.isEquipped()) {
                                                controller.comboBoxEquipmentLeftEarring.getSelectionModel().select(addon);
                                                addon.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RIGHT_EARRING:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRightEarring.getItems().add(addon);
                                            if (rightEarring != null && addon.getAddonID().equals(rightEarring.getAddonID()) || addon.isEquipped()) {
                                                controller.comboBoxEquipmentRightEarring.getSelectionModel().select(addon);
                                                addon.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RING_1:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRing1.getItems().add(addon);
                                            if (ring1 != null && addon.getAddonID().equals(ring1.getAddonID()) || addon.isEquipped()) {
                                                controller.comboBoxEquipmentRing1.getSelectionModel().select(addon);
                                                addon.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RING_2:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRing2.getItems().add(addon);
                                            if (ring2 != null && addon.getAddonID().equals(ring2.getAddonID()) || addon.isEquipped()) {
                                                controller.comboBoxEquipmentRing2.getSelectionModel().select(addon);
                                                addon.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RING_3:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRing3.getItems().add(addon);
                                            if (ring3 != null && addon.getAddonID().equals(ring3.getAddonID()) || addon.isEquipped()) {
                                                controller.comboBoxEquipmentRing3.getSelectionModel().select(addon);
                                                addon.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RING_4:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRing4.getItems().add(addon);
                                            if (ring4 != null && addon.getAddonID().equals(ring4.getAddonID()) || addon.isEquipped()) {
                                                controller.comboBoxEquipmentRing4.getSelectionModel().select(addon);
                                                addon.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case BACKPACK:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentBackpack.getItems().add(addon);
                                            if (backpack != null && addon.getAddonID().equals(backpack.getAddonID()) || addon.isEquipped()) {
                                                controller.comboBoxEquipmentBackpack.getSelectionModel().select(addon);
                                                addon.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case BELT:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentBelt.getItems().add(addon);
                                            if (belt != null && addon.getAddonID().equals(belt.getAddonID()) || addon.isEquipped()) {
                                                controller.comboBoxEquipmentBelt.getSelectionModel().select(addon);
                                                addon.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case BANDOLIER:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentBandolier.getItems().add(addon);
                                            if (bandolier != null && addon.getAddonID().equals(bandolier.getAddonID()) || addon.isEquipped()) {
                                                controller.comboBoxEquipmentBandolier.getSelectionModel().select(addon);
                                                addon.setEquipped(true);
                                            }
                                        });
                                        break;
                                }
                            }
                            Platform.runLater(() -> updateEquipmentProperties(controller));
                        } catch (SQLException e) {
                            try {
                                if (ps != null) ps.close();
                            } catch (SQLException ignored) {}
                            Logger.log(e);
                            new ErrorAlert("ERRORE", "ERRORE DI DATABASE", "Si e' verificato un errore durante la comunicazione con il database.");
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
    public static void updateCA(@NotNull final ControllerSceneSheetViewer controller) {
        int caMods = TabCharacter.getCAMods(controller);
        int fleshAC = caMods+10;
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        String query;
                        PreparedStatement ps = null;
                        ResultSet result;
                        try {
                            query = "SELECT e.ca_effect AS ca, a.slot AS slot, a.weight_category AS weight_category FROM items AS i JOIN equipments AS e JOIN armors AS a ON i.id = e.item_id AND e.id = a.equipment_id WHERE e.is_equipped=1;";
                            ps = DBManager.preparedStatement(query);
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            result = ps.executeQuery();
                            double caArmor = fleshAC*(ArmorSlot.values().length-2);
                            int slot;
                            ArmorWeightCategory weightCategory;
                            boolean fullSet = false;
                            while (result.next()) {
                                weightCategory = ArmorWeightCategory.values()[result.getInt("weight_category")];
                                slot = result.getInt("slot");
                                if (slot > ArmorSlot.NO_ARMOR.getDatabaseValue()) {
                                    if (slot == ArmorSlot.FULL_SET.getDatabaseValue()) {
                                        fullSet = true;
                                        caArmor = result.getInt("ca");
                                        if (weightCategory == ArmorWeightCategory.LIGHT) caArmor+= caMods;
                                        else if (weightCategory == ArmorWeightCategory.MEDIUM) caArmor += Math.min(2, caMods);
                                        break;
                                    } else {
                                        caArmor += result.getInt("ca")-fleshAC;
                                        if (weightCategory == ArmorWeightCategory.LIGHT) caArmor+= caMods;
                                        else if (weightCategory == ArmorWeightCategory.MEDIUM) caArmor += Math.min(2, caMods);
                                    }
                                }
                            }
                            ps.close();
                            int ca;
                            if (fullSet) {
                                ca = (int) caArmor;
                            } else {
                                ca = (int) Math.floor(caArmor / (ArmorSlot.values().length-2));
                            }
                            query = "SELECT e.ca_effect AS ca FROM items AS i JOIN equipments AS e JOIN addons AS a ON i.id = e.item_id AND e.id = a.equipment_id WHERE e.is_equipped=1;";
                            ps = DBManager.preparedStatement(query);
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            result = ps.executeQuery();
                            int caAddon = 0;
                            while (result.next()) {
                                caAddon += result.getInt("ca");
                            }
                            ps.close();
                            ca += caAddon;
                            query = "SELECT e.ca_effect AS ca FROM items AS i JOIN equipments AS e JOIN weapons AS w ON i.id = e.item_id AND e.id = w.equipment_id WHERE e.is_equipped=1;";
                            ps = DBManager.preparedStatement(query);
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            result = ps.executeQuery();
                            int caWeapon = 0;
                            while (result.next()) {
                                caWeapon += result.getInt("ca");
                            }
                            ps.close();
                            ca += caWeapon;
                            query = "SELECT ca_effect AS ca FROM effects WHERE is_active=1;";
                            ps = DBManager.preparedStatement(query);
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            result = ps.executeQuery();
                            int caEffect = 0;
                            while (result.next()) {
                                caEffect += result.getInt("ca");
                            }
                            ps.close();
                            ca += caEffect;
                            int finalCa = ca;
                            Platform.runLater(() -> TabCharacter.updateCASymbol(controller, finalCa));
                        } catch (SQLException e) {
                            try {
                                if (ps != null) ps.close();
                            } catch (SQLException ignored) {}
                            Logger.log(e);
                            new ErrorAlert("ERRORE", "ERRORE DI DATABASE", "Si e' verificato un errore durante la comunicazione con il database.");
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
    public static void equipmentToggleFullSet(@NotNull final ControllerSceneSheetViewer controller) {
        if (controller.comboBoxEquipmentFullSet.getSelectionModel().getSelectedItem()!=null && controller.comboBoxEquipmentFullSet.getSelectionModel().getSelectedItem().equals(ARMOR_EMPTY)) {
            controller.comboBoxEquipmentHead.setDisable(false);
            controller.comboBoxEquipmentLeftShoulder.setDisable(false);
            controller.comboBoxEquipmentLeftArm.setDisable(false);
            controller.comboBoxEquipmentLeftForearm.setDisable(false);
            controller.comboBoxEquipmentLeftHand.setDisable(false);
            controller.comboBoxEquipmentBack.setDisable(false);
            controller.comboBoxEquipmentLeftLeg.setDisable(false);
            controller.comboBoxEquipmentLeftKnee.setDisable(false);
            controller.comboBoxEquipmentLeftFoot.setDisable(false);
            controller.comboBoxEquipmentRightShoulder.setDisable(false);
            controller.comboBoxEquipmentRightArm.setDisable(false);
            controller.comboBoxEquipmentRightForearm.setDisable(false);
            controller.comboBoxEquipmentRightHand.setDisable(false);
            controller.comboBoxEquipmentChest.setDisable(false);
            controller.comboBoxEquipmentAbdomen.setDisable(false);
            controller.comboBoxEquipmentRightLeg.setDisable(false);
            controller.comboBoxEquipmentRightKnee.setDisable(false);
            controller.comboBoxEquipmentRightFoot.setDisable(false);
        } else {
            controller.comboBoxEquipmentHead.setDisable(true);
            controller.comboBoxEquipmentLeftShoulder.setDisable(true);
            controller.comboBoxEquipmentLeftArm.setDisable(true);
            controller.comboBoxEquipmentLeftForearm.setDisable(true);
            controller.comboBoxEquipmentLeftHand.setDisable(true);
            controller.comboBoxEquipmentBack.setDisable(true);
            controller.comboBoxEquipmentLeftLeg.setDisable(true);
            controller.comboBoxEquipmentLeftKnee.setDisable(true);
            controller.comboBoxEquipmentLeftFoot.setDisable(true);
            controller.comboBoxEquipmentRightShoulder.setDisable(true);
            controller.comboBoxEquipmentRightArm.setDisable(true);
            controller.comboBoxEquipmentRightForearm.setDisable(true);
            controller.comboBoxEquipmentRightHand.setDisable(true);
            controller.comboBoxEquipmentChest.setDisable(true);
            controller.comboBoxEquipmentAbdomen.setDisable(true);
            controller.comboBoxEquipmentRightLeg.setDisable(true);
            controller.comboBoxEquipmentRightKnee.setDisable(true);
            controller.comboBoxEquipmentRightFoot.setDisable(true);
        }
        TabEquipment.updateEquipmentProperties(controller);
    }
    public static void updateMaxCalculatedHP(@NotNull final ControllerSceneSheetViewer controller) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        String query;
                        PreparedStatement ps = null;
                        ResultSet result;
                        int lifeEffect = 0;
                        double lifePercentageEffect = 0;
                        try {
                            query = "SELECT e.life_effect AS life_effect, e.life_percentage_effect AS life_percentage_effect, a.slot AS slot FROM items AS i JOIN equipments AS e JOIN armors AS a ON i.id = e.item_id AND e.id = a.equipment_id WHERE e.is_equipped=1;";
                            ps = DBManager.preparedStatement(query);
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            result = ps.executeQuery();
                            int slot;
                            while (result.next()) {
                                slot = result.getInt("slot");
                                if (slot > ArmorSlot.NO_ARMOR.getDatabaseValue()) {
                                    if (slot == ArmorSlot.FULL_SET.getDatabaseValue()) {
                                        lifeEffect = result.getInt("life_effect");
                                        lifePercentageEffect = result.getDouble("life_percentage_effect");
                                        break;
                                    } else {
                                        lifeEffect += result.getInt("life_effect");
                                        lifePercentageEffect += result.getDouble("life_percentage_effect");
                                    }
                                }
                            }
                            ps.close();
                            query = "SELECT e.life_effect AS life_effect, e.life_percentage_effect AS life_percentage_effect FROM items AS i JOIN equipments AS e JOIN weapons AS w ON i.id = e.item_id AND e.id = w.equipment_id WHERE e.is_equipped=1;";
                            ps = DBManager.preparedStatement(query);
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            result = ps.executeQuery();
                            while (result.next()) {
                                lifeEffect += result.getInt("life_effect");
                                lifePercentageEffect += result.getDouble("life_percentage_effect");
                            }
                            ps.close();
                            query = "SELECT e.life_effect AS life_effect, e.life_percentage_effect AS life_percentage_effect FROM items AS i JOIN equipments AS e JOIN addons AS a ON i.id = e.item_id AND e.id = a.equipment_id WHERE e.is_equipped=1;";
                            ps = DBManager.preparedStatement(query);
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            result = ps.executeQuery();
                            while (result.next()) {
                                lifeEffect += result.getInt("life_effect");
                                lifePercentageEffect += result.getDouble("life_percentage_effect");
                            }
                            ps.close();
                            query = "SELECT life_effect, life_percentage_effect FROM effects WHERE is_active=1;";
                            ps = DBManager.preparedStatement(query);
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            result = ps.executeQuery();
                            while (result.next()) {
                                lifeEffect += result.getInt("life_effect");
                                lifePercentageEffect += result.getDouble("life_percentage_effect");
                            }
                            ps.close();
                            int finalLifeEffect = lifeEffect;
                            double finalLifePercentageEffect = lifePercentageEffect;
                            Platform.runLater(() -> TabCharacter.updateCalculatedMaxHP(controller, finalLifeEffect, finalLifePercentageEffect));
                        } catch (SQLException e) {
                            try {
                                if (ps != null) ps.close();
                            } catch (SQLException ignored) {}
                            Logger.log(e);
                            new ErrorAlert("ERRORE", "ERRORE DI DATABASE", "Si e' verificato un errore durante la comunicazione con il database.");
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
    public static void updateMaxLoad(@NotNull final ControllerSceneSheetViewer controller) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        String query;
                        PreparedStatement ps = null;
                        ResultSet result;
                        int loadEffect = 0;
                        double loadPercentageEffect = 0;
                        try {
                            query = "SELECT e.load_effect AS load_effect, e.load_percentage_effect AS load_percentage_effect, a.slot AS slot FROM items AS i JOIN equipments AS e JOIN armors AS a ON i.id = e.item_id AND e.id = a.equipment_id WHERE e.is_equipped=1;";
                            ps = DBManager.preparedStatement(query);
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            result = ps.executeQuery();
                            int slot;
                            while (result.next()) {
                                slot = result.getInt("slot");
                                if (slot > ArmorSlot.NO_ARMOR.getDatabaseValue()) {
                                    if (slot == ArmorSlot.FULL_SET.getDatabaseValue()) {
                                        loadEffect = result.getInt("load_effect");
                                        loadPercentageEffect = result.getDouble("load_percentage_effect");
                                        break;
                                    } else {
                                        loadEffect += result.getInt("load_effect");
                                        loadPercentageEffect += result.getDouble("load_percentage_effect");
                                    }
                                }
                            }
                            ps.close();
                            query = "SELECT e.load_effect AS load_effect, e.load_percentage_effect AS load_percentage_effect FROM items AS i JOIN equipments AS e JOIN weapons AS w ON i.id = e.item_id AND e.id = w.equipment_id WHERE e.is_equipped=1;";
                            ps = DBManager.preparedStatement(query);
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            result = ps.executeQuery();
                            while (result.next()) {
                                loadEffect += result.getInt("load_effect");
                                loadPercentageEffect += result.getDouble("load_percentage_effect");
                            }
                            ps.close();
                            query = "SELECT e.load_effect AS load_effect, e.load_percentage_effect AS load_percentage_effect FROM items AS i JOIN equipments AS e JOIN addons AS a ON i.id = e.item_id AND e.id = a.equipment_id WHERE e.is_equipped=1;";
                            ps = DBManager.preparedStatement(query);
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            result = ps.executeQuery();
                            while (result.next()) {
                                loadEffect += result.getInt("load_effect");
                                loadPercentageEffect += result.getDouble("load_percentage_effect");
                            }
                            ps.close();
                            query = "SELECT load_effect, load_percentage_effect FROM effects WHERE is_active=1;";
                            ps = DBManager.preparedStatement(query);
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            result = ps.executeQuery();
                            while (result.next()) {
                                loadEffect += result.getInt("load_effect");
                                loadPercentageEffect += result.getDouble("load_percentage_effect");
                            }
                            int finalLoadEffect = loadEffect;
                            double finalLoadPercentageEffect = loadPercentageEffect;
                            Platform.runLater(() -> TabInventory.updateMaxLoad(controller, finalLoadEffect, finalLoadPercentageEffect));
                        } catch (SQLException e) {
                            try {
                                if (ps != null) ps.close();
                            } catch (SQLException ignored) {}
                            Logger.log(e);
                            new ErrorAlert("ERRORE", "ERRORE DI DATABASE", "Si e' verificato un errore durante la comunicazione con il database.");
                        }
                        return null;
                    }
                };
            }
        }.start();
    }

    public static void updateEquipmentProperties(@NotNull final ControllerSceneSheetViewer controller) {
        TabInventory.updateLoad(controller);
        updateCA(controller);
        updateMaxCalculatedHP(controller);
        updateMaxLoad(controller);
    }
}
