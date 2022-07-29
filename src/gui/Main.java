package gui;

import java.io.InputStream;
import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

	private boolean running = false;
	public static Board enemyBoard, playerBoard;
	public static int moveCounter = 0;
	private int shipsToPlace = 5;

	private boolean enemyTurn = false;

	private Random random = new Random();

	int x = random.nextInt(10);
	int y = random.nextInt(10);

	Boolean flag = false;

	private Parent createContent(Stage primaryStage) {
		BorderPane root = new BorderPane();
		root.setPrefSize(1800, 900);

		ImageView mar = new ImageView("gui/images/mar.jpg");

		root.getChildren().add(mar);

		Button btnExit = new Button("Salir");
		btnExit.setAlignment(Pos.CENTER);
		btnExit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				System.exit(0);
			}
		});

		enemyBoard = new Board(true, event -> {
			if (!running)
				return;

			Cell cell = (Cell) event.getSource();
			if (cell.wasShot)
				return;

			enemyTurn = !cell.shoot();

			if (enemyBoard.ships == 0) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Batalla Finalizada!");
				alert.setHeaderText("Has Ganado!");
				alert.showAndWait();
				System.exit(0);
			}
			if (enemyTurn)
				enemyMove();
		});

		playerBoard = new Board(false, event -> {
			if (running)
				return;

			Cell cell = (Cell) event.getSource();
			if (playerBoard.placeShip(new Ship(shipsToPlace, event.getButton() == MouseButton.PRIMARY), cell.x,
					cell.y)) {
				if (--shipsToPlace == 0) {
					startGame();
				}
			}
		});


		Button menu = new Button("Volver al menú principal");
		menu.setAlignment(Pos.CENTER);
		menu.setOnAction(e-> {
					primaryStage.setScene(new Scene(createContent1(primaryStage), 854, 503));
					primaryStage.setTitle("Menú principal");
				}
		);


		VBox vbRight = new VBox();

		vbRight.setAlignment(Pos.BASELINE_CENTER);
		ImageView cap = new ImageView("gui/images/going.jpg");
		cap.setFitHeight(150);
		cap.setFitWidth(150);
		vbRight.getChildren().add(cap);
		vbRight.getChildren().addAll(btnExit, menu, new Label(""));
		vbRight.setSpacing(75);
		vbRight.setPadding(new Insets(45));

		VBox vbox = new VBox(50, enemyBoard, playerBoard);
		vbox.setAlignment(Pos.CENTER);

		root.setCenter(vbox);

		root.setRight(vbRight);
		return root;
	}

	public void enemyMove() {
		Bot b = new Bot();
		b.play();
		moveCounter++;

	}

	private void startGame() {
		// place enemy ships
		int type = 5;

		while (type > 0) {
			int x = random.nextInt(10);
			int y = random.nextInt(10);

			if (enemyBoard.placeShip(new Ship(type, Math.random() < 0.5), x, y)) {
				type--;
			}
		}

		running = true;
	}

	private Parent createContent1(Stage primaryStage){

		VBox left = new VBox();
		VBox login = new VBox();

		Button btnLog = new Button("Iniciar juego");
		btnLog.setMaxWidth(150);
		btnLog.setCursor(Cursor.HAND);
		btnLog.setFont(new Font( 18));

		Button btnIns = new Button("Instrucciones");
		btnIns.setMaxWidth(150);
		btnIns.setCursor(Cursor.HAND);
		btnIns.setFont(new Font( 18));

		Button salir = new Button("Salir");
		salir.setMaxWidth(150);
		salir.setCursor(Cursor.HAND);
		salir.setFont(new Font( 18));


		login.getChildren().addAll(btnLog, btnIns, salir);
		login.setAlignment(Pos.CENTER);

		left.getChildren().addAll(login);
		left.setPrefWidth(422);
		left.setAlignment(Pos.CENTER);
		VBox.setMargin(left, new Insets(0,30,0,30));

		VBox right = new VBox();

		ImageView imageLogo;

		InputStream inputStream;
		inputStream = getClass().getResourceAsStream("images/muerte.jpg");

		Image image = new Image(inputStream);
		imageLogo = new ImageView(image);

		right.getChildren().add(imageLogo);
		right.setPrefWidth(422);
		right.setAlignment(Pos.CENTER);
		right.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

		HBox root = new HBox();
		root.getChildren().addAll(left,right);

		HBox.setHgrow(left, Priority.ALWAYS);
		HBox.setHgrow(right, Priority.ALWAYS);

		primaryStage.setTitle("Menú de inicio");
		primaryStage.show();

		btnLog.setOnAction(e-> {
			primaryStage.setScene(new Scene(createContent(primaryStage)));
			primaryStage.setTitle("Batalla Naval");
			primaryStage.setMaximized(true);
		});

		btnIns.setOnAction(e->{
			Instructions.display("Instrucciones", "\t\t\t\t\t\t    REGLAS\n\n\n- Cada jugador tiene un total de 5 naves\n\n- Coloca tus 5 naves en la parte de abajo del tablero y comienza a jugar\t\n\n\n\n");
		});

		salir.setOnAction(e->{
			primaryStage.close();
		});

		primaryStage.setTitle("Menú principal");

		return root;

	}


	@Override
	public void start(Stage primaryStage) throws Exception {

		Scene scene1 = new Scene(createContent1(primaryStage), 854, 503);
		Scene scene2 = new Scene(createContent(primaryStage));
		primaryStage.setScene(scene1);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
