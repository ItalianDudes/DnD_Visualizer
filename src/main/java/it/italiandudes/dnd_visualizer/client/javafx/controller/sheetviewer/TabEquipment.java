package it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.client.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.client.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.data.enums.ArmorSlot;
import it.italiandudes.dnd_visualizer.data.enums.Category;
import it.italiandudes.dnd_visualizer.data.enums.EquipmentType;
import it.italiandudes.dnd_visualizer.data.item.Armor;
import it.italiandudes.dnd_visualizer.db.DBManager;
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
    public static final Armor EMPTY = new Armor(ArmorSlot.NO_ARMOR);

    // Method
    private static ObservableList<Armor> getEmptyList() {
        ObservableList<Armor> EMPTY_LIST = FXCollections.observableList(new ArrayList<>());
        EMPTY_LIST.add(EMPTY);
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
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentNecklace.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentFullSet.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentLeftShoulder.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentLeftArm.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentLeftForearm.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentLeftHand.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentMantle.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentBack.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentLeftLeg.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentLeftKnee.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentLeftFoot.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentLeftEarring.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentLeftBracelet.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentRing1.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentRing2.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentRightShoulder.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentRightArm.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentRightForearm.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentRightHand.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentChest.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentAbdomen.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentRightLeg.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentRightKnee.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentRightFoot.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentRightEarring.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentRightBracelet.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentRing3.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
        controller.comboBoxEquipmentRing4.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null) oldValue.setEquipped(false);
            if (newValue!=null && !newValue.equals(EMPTY)) newValue.setEquipped(true);
        }));
    }

    // EDT
    private static void setEmpty(@NotNull final ControllerSceneSheetViewer controller) {
        controller.comboBoxEquipmentFullSet.setItems(getEmptyList());
        controller.comboBoxEquipmentFullSet.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentHead.setItems(getEmptyList());
        controller.comboBoxEquipmentHead.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentNecklace.setItems(getEmptyList());
        controller.comboBoxEquipmentNecklace.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentLeftShoulder.setItems(getEmptyList());
        controller.comboBoxEquipmentLeftShoulder.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentLeftArm.setItems(getEmptyList());
        controller.comboBoxEquipmentLeftArm.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentLeftForearm.setItems(getEmptyList());
        controller.comboBoxEquipmentLeftForearm.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentLeftHand.setItems(getEmptyList());
        controller.comboBoxEquipmentLeftHand.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentMantle.setItems(getEmptyList());
        controller.comboBoxEquipmentMantle.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentBack.setItems(getEmptyList());
        controller.comboBoxEquipmentBack.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentLeftLeg.setItems(getEmptyList());
        controller.comboBoxEquipmentLeftLeg.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentLeftKnee.setItems(getEmptyList());
        controller.comboBoxEquipmentLeftKnee.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentLeftFoot.setItems(getEmptyList());
        controller.comboBoxEquipmentLeftFoot.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentLeftEarring.setItems(getEmptyList());
        controller.comboBoxEquipmentLeftEarring.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentLeftBracelet.setItems(getEmptyList());
        controller.comboBoxEquipmentLeftBracelet.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRing1.setItems(getEmptyList());
        controller.comboBoxEquipmentRing1.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRing2.setItems(getEmptyList());
        controller.comboBoxEquipmentRing2.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRightShoulder.setItems(getEmptyList());
        controller.comboBoxEquipmentRightShoulder.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRightArm.setItems(getEmptyList());
        controller.comboBoxEquipmentRightArm.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRightForearm.setItems(getEmptyList());
        controller.comboBoxEquipmentRightForearm.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRightHand.setItems(getEmptyList());
        controller.comboBoxEquipmentRightHand.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentChest.setItems(getEmptyList());
        controller.comboBoxEquipmentChest.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentAbdomen.setItems(getEmptyList());
        controller.comboBoxEquipmentAbdomen.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRightLeg.setItems(getEmptyList());
        controller.comboBoxEquipmentRightLeg.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRightKnee.setItems(getEmptyList());
        controller.comboBoxEquipmentRightKnee.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRightFoot.setItems(getEmptyList());
        controller.comboBoxEquipmentRightFoot.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRightEarring.setItems(getEmptyList());
        controller.comboBoxEquipmentRightEarring.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRightBracelet.setItems(getEmptyList());
        controller.comboBoxEquipmentRightBracelet.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRing3.setItems(getEmptyList());
        controller.comboBoxEquipmentRing3.getSelectionModel().selectFirst();
        controller.comboBoxEquipmentRing4.setItems(getEmptyList());
        controller.comboBoxEquipmentRing4.getSelectionModel().selectFirst();
    }
    public static void reloadEquipment(@NotNull final ControllerSceneSheetViewer controller) {
        Armor head = controller.comboBoxEquipmentHead.getSelectionModel().getSelectedItem();
        Armor necklace = controller.comboBoxEquipmentNecklace.getSelectionModel().getSelectedItem();
        Armor fullSet = controller.comboBoxEquipmentFullSet.getSelectionModel().getSelectedItem();
        Armor leftShoulder = controller.comboBoxEquipmentLeftShoulder.getSelectionModel().getSelectedItem();
        Armor leftArm = controller.comboBoxEquipmentLeftArm.getSelectionModel().getSelectedItem();
        Armor leftForearm = controller.comboBoxEquipmentLeftForearm.getSelectionModel().getSelectedItem();
        Armor leftHand = controller.comboBoxEquipmentLeftHand.getSelectionModel().getSelectedItem();
        Armor mantle = controller.comboBoxEquipmentMantle.getSelectionModel().getSelectedItem();
        Armor back = controller.comboBoxEquipmentBack.getSelectionModel().getSelectedItem();
        Armor leftLeg = controller.comboBoxEquipmentLeftLeg.getSelectionModel().getSelectedItem();
        Armor leftKnee = controller.comboBoxEquipmentLeftKnee.getSelectionModel().getSelectedItem();
        Armor leftFoot = controller.comboBoxEquipmentLeftFoot.getSelectionModel().getSelectedItem();
        Armor leftEarring = controller.comboBoxEquipmentLeftEarring.getSelectionModel().getSelectedItem();
        Armor leftBracelet = controller.comboBoxEquipmentLeftBracelet.getSelectionModel().getSelectedItem();
        Armor ring1 = controller.comboBoxEquipmentRing1.getSelectionModel().getSelectedItem();
        Armor ring2 = controller.comboBoxEquipmentRing2.getSelectionModel().getSelectedItem();
        Armor rightShoulder = controller.comboBoxEquipmentRightShoulder.getSelectionModel().getSelectedItem();
        Armor rightArm = controller.comboBoxEquipmentRightArm.getSelectionModel().getSelectedItem();
        Armor rightForearm = controller.comboBoxEquipmentRightForearm.getSelectionModel().getSelectedItem();
        Armor rightHand = controller.comboBoxEquipmentRightHand.getSelectionModel().getSelectedItem();
        Armor chest = controller.comboBoxEquipmentChest.getSelectionModel().getSelectedItem();
        Armor abdomen = controller.comboBoxEquipmentAbdomen.getSelectionModel().getSelectedItem();
        Armor rightLeg = controller.comboBoxEquipmentRightLeg.getSelectionModel().getSelectedItem();
        Armor rightKnee = controller.comboBoxEquipmentRightKnee.getSelectionModel().getSelectedItem();
        Armor rightFoot = controller.comboBoxEquipmentRightFoot.getSelectionModel().getSelectedItem();
        Armor rightEarring = controller.comboBoxEquipmentRightEarring.getSelectionModel().getSelectedItem();
        Armor rightBracelet = controller.comboBoxEquipmentRightBracelet.getSelectionModel().getSelectedItem();
        Armor ring3 = controller.comboBoxEquipmentRing3.getSelectionModel().getSelectedItem();
        Armor ring4 = controller.comboBoxEquipmentRing4.getSelectionModel().getSelectedItem();
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
                            query = "SELECT i.name AS name FROM items AS i JOIN equipments AS e ON i.id = e.item_id WHERE i.category=? AND e.type=? AND i.quantity>0;";
                            ps = DBManager.preparedStatement(query);
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            ps.setInt(1, Category.EQUIPMENT.getDatabaseValue());
                            ps.setInt(2, EquipmentType.ARMOR.getDatabaseValue());
                            ResultSet result = ps.executeQuery();

                            ArrayList<Armor> armors = new ArrayList<>();
                            while (result.next()) {
                                armors.add(new Armor(result.getString("name")));
                            }
                            ps.close();

                            for (Armor armor : armors) {
                                switch (armor.getSlot()) {
                                    case HEAD:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentHead.getItems().add(armor);
                                            if (armor.equals(head ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentHead.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case NECKLACE:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentNecklace.getItems().add(armor);
                                            if (armor.equals(necklace ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentNecklace.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case FULL_SET:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentFullSet.getItems().add(armor);
                                            if (armor.equals(fullSet ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentFullSet.getSelectionModel().select(armor);
                                                equipmentToggleFullSet(controller);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case LEFT_SHOULDER:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentLeftShoulder.getItems().add(armor);
                                            if (armor.equals(leftShoulder ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentLeftShoulder.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case LEFT_ARM:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentLeftArm.getItems().add(armor);
                                            if (armor.equals(leftArm ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentLeftArm.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case LEFT_FOREARM:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentLeftForearm.getItems().add(armor);
                                            if (armor.equals(leftForearm ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentLeftForearm.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case LEFT_HAND:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentLeftHand.getItems().add(armor);
                                            if (armor.equals(leftHand ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentLeftHand.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case MANTLE:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentMantle.getItems().add(armor);
                                            if (armor.equals(mantle ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentMantle.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case BACK:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentBack.getItems().add(armor);
                                            if (armor.equals(back ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentBack.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case LEFT_LEG:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentLeftLeg.getItems().add(armor);
                                            if (armor.equals(leftLeg ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentLeftLeg.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case LEFT_KNEE:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentLeftKnee.getItems().add(armor);
                                            if (armor.equals(leftKnee ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentLeftKnee.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case LEFT_FOOT:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentLeftFoot.getItems().add(armor);
                                            if (armor.equals(leftFoot ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentLeftFoot.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case LEFT_EARRING:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentLeftEarring.getItems().add(armor);
                                            if (armor.equals(leftEarring ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentLeftEarring.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case LEFT_BRACELET:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentLeftBracelet.getItems().add(armor);
                                            if (armor.equals(leftBracelet ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentLeftBracelet.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RING_1:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRing1.getItems().add(armor);
                                            if (armor.equals(ring1 ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRing1.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RING_2:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRing2.getItems().add(armor);
                                            if (armor.equals(ring2 ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRing2.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RIGHT_SHOULDER:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRightShoulder.getItems().add(armor);
                                            if (armor.equals(rightShoulder ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRightShoulder.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RIGHT_ARM:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRightArm.getSelectionModel().select(armor);
                                            if (armor.equals(rightArm ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRightArm.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RIGHT_FOREARM:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRightForearm.getItems().add(armor);
                                            if (armor.equals(rightForearm ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRightForearm.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RIGHT_HAND:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRightHand.getItems().add(armor);
                                            if (armor.equals(rightHand ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRightHand.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case CHEST:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentChest.getItems().add(armor);
                                            if (armor.equals(chest ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentChest.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case ABDOMEN:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentAbdomen.getItems().add(armor);
                                            if (armor.equals(abdomen ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentAbdomen.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RIGHT_LEG:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRightLeg.getItems().add(armor);
                                            if (armor.equals(rightLeg ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRightLeg.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RIGHT_KNEE:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRightKnee.getItems().add(armor);
                                            if (armor.equals(rightKnee ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRightKnee.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RIGHT_FOOT:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRightFoot.getItems().add(armor);
                                            if (armor.equals(rightFoot ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRightFoot.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RIGHT_EARRING:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRightEarring.getItems().add(armor);
                                            if (armor.equals(rightEarring ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRightEarring.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RIGHT_BRACELET:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRightBracelet.getItems().add(armor);
                                            if (armor.equals(rightBracelet ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRightBracelet.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RING_3:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRing3.getItems().add(armor);
                                            if (armor.equals(ring3 ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRing3.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;

                                    case RING_4:
                                        Platform.runLater(() -> {
                                            controller.comboBoxEquipmentRing4.getItems().add(armor);
                                            if (armor.equals(ring4 ) || armor.isEquipped()) {
                                                controller.comboBoxEquipmentRing4.getSelectionModel().select(armor);
                                                armor.setEquipped(true);
                                            }
                                        });
                                        break;
                                }
                            }
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
        if (controller.comboBoxEquipmentFullSet.getSelectionModel().getSelectedItem()!=null && controller.comboBoxEquipmentFullSet.getSelectionModel().getSelectedItem().equals(EMPTY)) {
            controller.comboBoxEquipmentHead.setDisable(false);
            controller.comboBoxEquipmentLeftShoulder.setDisable(false);
            controller.comboBoxEquipmentLeftArm.setDisable(false);
            controller.comboBoxEquipmentLeftForearm.setDisable(false);
            controller.comboBoxEquipmentLeftHand.setDisable(false);
            controller.comboBoxEquipmentMantle.setDisable(false);
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
            controller.comboBoxEquipmentMantle.setDisable(true);
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
    }
}
