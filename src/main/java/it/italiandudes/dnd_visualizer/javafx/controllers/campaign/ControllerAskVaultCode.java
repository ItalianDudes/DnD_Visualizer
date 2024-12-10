package it.italiandudes.dnd_visualizer.javafx.controllers.campaign;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.Nullable;

public final class ControllerAskVaultCode {

    // Attributes
    @Nullable
    public static String USER_ATTEMPT = null;

    // PWD
    public static final String PASSWORD = "c7494d9d1b67b8a68e9ee3f6dfb7e43e8919b3496423b47f0c7faadb8ad5bf6d6c43ff706d147aad7359a99147bec2b92230f18b38f665d1e7d186b04b648a88";

    //Graphic Elements
    @FXML private PasswordField passwordFieldVaultCode;

    // EDT
    @FXML private void back() {
        USER_ATTEMPT = null;
        passwordFieldVaultCode.getScene().getWindow().hide();
    }
    @FXML private void proceed() {
        USER_ATTEMPT = DigestUtils.sha512Hex(passwordFieldVaultCode.getText());
        passwordFieldVaultCode.getScene().getWindow().hide();
    }
}
