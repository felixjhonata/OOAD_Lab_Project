package view;

import controller.UserController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class RegisterView extends Application implements EventHandler<ActionEvent> {
    private BorderPane root;
    private GridPane grid;
    private Label title, usernameLabel, passwordLabel, phoneLabel, addressLabel, roleLabel;
    private TextField usernameField, passwordField, phoneField, addressField;
    private RadioButton buyerRadio, sellerRadio;
    private ToggleGroup roleGroup;
    private Button registerButton, backButton;
    private HBox hbBtn;
    public Scene scene;
    private UserController uc;

    public void init() {
        root = new BorderPane();
        title = new Label("Register");
        grid = new GridPane();

        usernameLabel = new Label("Username:");
        usernameField = new TextField();

        passwordLabel = new Label("Password:");
        passwordField = new TextField();

        phoneLabel = new Label("Phone Number:");
        phoneField = new TextField();

        addressLabel = new Label("Address:");
        addressField = new TextField();

        roleLabel = new Label("Role:");
        roleGroup = new ToggleGroup();
        buyerRadio = new RadioButton("Buyer");
        buyerRadio.setToggleGroup(roleGroup);
        sellerRadio = new RadioButton("Seller");
        sellerRadio.setToggleGroup(roleGroup);

        registerButton = new Button("Register");
        backButton = new Button("Back");
        hbBtn = new HBox(10);
        scene = new Scene(root, 1100, 550);
        uc = new UserController();
    }

    public void setPosition() {
        root.setCenter(title);
        root.setBottom(grid);

        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPrefHeight(400);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        title.setAlignment(Pos.TOP_CENTER);

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);

        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);

        grid.add(phoneLabel, 0, 2);
        grid.add(phoneField, 1, 2);

        grid.add(addressLabel, 0, 3);
        grid.add(addressField, 1, 3);

        grid.add(roleLabel, 0, 4);
        grid.add(buyerRadio, 1, 4);
        grid.add(sellerRadio, 1, 5);

        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(backButton);
        hbBtn.getChildren().add(registerButton);
        grid.add(hbBtn, 1, 6);
    }

    public void setStyle() {
        title.setStyle("-fx-font-size: 36px;");
    }

    private void events() {
        registerButton.setOnAction(e -> handle(e));
        backButton.setOnAction(e -> handle(e));
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void handle(ActionEvent e) {
        if (e.getSource() == registerButton) {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            RadioButton selectedRole = (RadioButton) roleGroup.getSelectedToggle();
            String role = selectedRole == null ? "" : selectedRole.getText();

            String result = uc.registerUser(username, password, phone, address, role);
            if (result.equals("Registrasi berhasil!")) {
                showSuccess("Information", result);
                clearFields();
                LoginView loginPage = new LoginView();
                view.Main.redirect(loginPage.scene);
            } else {
                showAlert("Error", result);
            }
        } else if (e.getSource() == backButton) {
            clearFields();
            LoginView loginPage = new LoginView();
            view.Main.redirect(loginPage.scene);
        }
    }

    public void clearFields() {
        usernameField.clear();
        passwordField.clear();
        phoneField.clear();
        addressField.clear();
        roleGroup.selectToggle(null);
    }

    public RegisterView() {
        init();
        setPosition();
        setStyle();
        events();
        view.Main.redirect(scene);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Register Page");
        stage.setScene(scene);
        stage.show();
    }
}
