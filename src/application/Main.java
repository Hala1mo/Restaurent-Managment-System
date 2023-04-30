package application;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.plaf.basic.BasicToggleButtonUI;

import java.sql.ResultSet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Main extends Application {

	static Connection con;
	private Alert error = new Alert(AlertType.ERROR);
	static String userLogin = "Hala";
	static Stage primaryStage;

	ArrayList<Meal> m = new ArrayList<>();
	ArrayList<Employee> emp = new ArrayList<>();
	ArrayList<customer> c = new ArrayList<>();
	ArrayList<Ingredients> ing = new ArrayList<>();
	ArrayList<meal_ing> meal_ing = new ArrayList<>();
	ArrayList<meal_ing> meall = new ArrayList<>();
	ArrayList<Supp_Ing> supp_ing = new ArrayList<>();
	ArrayList<OrderCustomer> ord_cus = new ArrayList<>();
	ArrayList<Order> order = new ArrayList<>();
	HashMap<String, Integer> map2 = new HashMap<>();
	HashMap<String, Double> map = new HashMap<>();

	int lastEmpId = 0;
	int i = 0;
	int customerid = 0;
	int orderid = 0;
	int lastOrder = 0;

	@Override
	public void start(Stage primaryStage) {
		boolean b = connectDataBase();
		if (b) {
			try {
				// apply your code here
				// you will use appyQueryOnDataBase (just send Query and get the ResultSet)
				// and applyOnDataBase for delete, insert ...

				String sql4 = "select * from supplier s,Ingredients f where f.supp_Id=s.ID;";
				try {
					
					Statement stmt4 = con.createStatement();
					ResultSet result = stmt4.executeQuery(sql4);
					int supp_id = 1;
					String supp_name = "";
					String supp_address = "";
					String supp_phone = "";
					String ing = "";
					String x = "";
					while (result.next())

					{
						if(supp_id==2) {
							supp_id++;
						}
						if (Integer.valueOf(result.getString(1)) == (supp_id)) {
							supp_name = result.getString(4);
							supp_address = result.getString(2);
							supp_phone = result.getString(3);
							ing = result.getString(7);
							x = x + ing + ",";

						} else {
							if (x != "") {
								x = x.substring(0, x.lastIndexOf(','));
							}

							supp_ing.add(new Supp_Ing(supp_id, supp_address, supp_phone, supp_name, x));
							x = "";
							supp_name = result.getString(4);
							supp_address = result.getString(2);
							supp_phone = result.getString(3);
							ing = result.getString(7);
							x = x + ing + ",";
							supp_id++;

						}

					}
					if (x != "") {
						x = x.substring(0, x.lastIndexOf(','));
					}
					supp_ing.add(new Supp_Ing(supp_id, supp_address, supp_phone, supp_name, x));

				} catch (SQLException ee) {
					// TODO Auto-generated catch block
					ee.printStackTrace();
				}

				String sql3 = " select e.ID ,e.ename,o.order_ID ,o.order_date,m.Cust_ID ,m.meal_ID,m.Quantity ,m.price  from Employee e,ordert o,Meal_Cust m  where e.ID=o.e_ID And o.cust_ID=m.Cust_ID ;";
				Statement stmt3;

				try {
					stmt3 = con.createStatement();
					ResultSet result = stmt3.executeQuery(sql3);

					while (result.next()) {
						Date date = result.getDate(4);
						ord_cus.add(new OrderCustomer(Integer.parseInt(result.getString(1)), result.getString(2),
								Integer.parseInt(result.getString(3)), date, Integer.parseInt(result.getString(5)),
								Integer.parseInt(result.getString(6)), Integer.parseInt(result.getString(7)),
								Double.parseDouble(result.getString(8))));
						lastOrder = Integer.parseInt(result.getString(3));
					}
				} catch (SQLException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				String sql2 = "select * from customer;";
				Statement stmt2;

				try {
					stmt2 = con.createStatement();
					ResultSet result = stmt2.executeQuery(sql2);

					while (result.next()) {
						c.add(new customer(Integer.parseInt(result.getString(1)), result.getString(2),
								result.getString(3), result.getString(4)));
						++customerid;
					}
				} catch (SQLException ee) {
					// TODO Auto-generated catch block
					ee.printStackTrace();
				}

				// Join
				String sql = "select * from Meal m,Meal_Ing mg,ingredients i where m.ID=mg.meal_ID And mg.ing_Id=i.ID order by m.ID;";
				Statement stmt;
				try {
					stmt = con.createStatement();
					ResultSet result = stmt.executeQuery(sql);

					String id_meal = "1";
					String mealname = "";
					String mealtype = "";
					String price = "";
					String ingredients = "";
					String x = "";
					while (result.next()) {

						if (result.getString(1).compareToIgnoreCase(id_meal) == 0) {
							mealname = result.getString(2);

							mealtype = result.getString(3);
							price = result.getString(4);
							ingredients = result.getString(9);
							x = x + ingredients + ",";

						} else {
							if (x != "") {
								x = x.substring(0, x.lastIndexOf(','));
							}
							meal_ing.add(new meal_ing(Integer.parseInt(id_meal), mealname, mealtype,
									Double.parseDouble(price), x));

							x = "";
							id_meal = result.getString(1);
							mealname = result.getString(2);
							mealtype = result.getString(3);
							price = result.getString(4);
							ingredients = result.getString(9);
							x = x + ingredients + ",";

						}

					}

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ResultSet s2 = appyQueryOnDataBase("select * from Meal;");

				try {
					while (s2.next()) {

						m.add(new Meal(Integer.parseInt(s2.getString(1)), s2.getString(2), s2.getString(3),
								Double.parseDouble(s2.getString(4))));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ResultSet or = appyQueryOnDataBase("select * from ordert;");

				try {
					while (or.next()) {

						order.add(new Order(Integer.parseInt(or.getString(1)), Integer.parseInt(or.getString(2)),
								Integer.parseInt(or.getString(3)), or.getString(4)));
						++orderid;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ResultSet s3 = appyQueryOnDataBase("select * from Ingredients;");

				try {
					while (s3.next()) {

						ing.add(new Ingredients(Integer.parseInt(s3.getString(1)), Integer.parseInt(s3.getString(2)),
								s3.getString(3)));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String s5 = "select * from Employee;";
				Statement stmt5;

				try {
					stmt5 = con.createStatement();
					ResultSet result = stmt5.executeQuery(s5);
					while (result.next()) {

						emp.add(new Employee(Integer.valueOf(result.getString(1)), result.getString(5),
								result.getDate(3), result.getString(4), result.getString(2), result.getString(6),
								result.getString(7), Integer.valueOf(result.getString(8))));
						lastEmpId = (Integer.valueOf(result.getString(1)));
						//System.out.println(result.getString(7));
					}
				} catch (SQLException ee) {
					// TODO Auto-generated catch block
					ee.printStackTrace();
				}

				BorderPane root = new BorderPane();
				root.setStyle("-fx-background-image: url('hun.jpg')");

				Circle r1 = new Circle();
				r1.setRadius(65.0);
				Image image = new Image("managerr.png");
				r1.setFill(new ImagePattern(image));
				Circle r2 = new Circle();
				r2.setRadius(65.0);
				Image image2 = new Image("client.png");
				r2.setFill(new ImagePattern(image2));

				Circle circle = new Circle(70);

				Button manager = new Button(null, r1);
				manager.setShape(circle);
				Button cus = new Button(null, r2);
				cus.setShape(circle);
				HBox h = new HBox();
				h.setAlignment(Pos.CENTER);
				h.setSpacing(20);
				h.getChildren().addAll(manager, cus);

				ImageView a2 = new ImageView("hun.jpg");
				a2.setFitWidth(800);
				root.setBottom(h);
				Scene scene = new Scene(root, 1550, 810);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.show();

				manager.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						// TODO Auto-generated method stub

						GridPane g = new GridPane();
						Scene scene3 = new Scene(g, 1550, 820);
						scene3.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						primaryStage.setScene(scene3);
						primaryStage.show();
						g.setAlignment(Pos.CENTER);
						g.setStyle("-fx-background-image: url('hun.jpg')");

						final Label message = new Label("");

						VBox vb = new VBox();
						vb.setPadding(new Insets(10, 0, 0, 10));
						vb.setSpacing(10);
						HBox hb = new HBox();
						hb.setSpacing(10);
						hb.setAlignment(Pos.CENTER_LEFT);

						Button back = new Button("Back");

						back.setTextFill(Color.BROWN);
						back.setStyle("-fx-background-color:  #FFFFF0; ");
						back.setFont(Font.font(25));
						back.setFont(Font.font(null, FontWeight.BOLD, back.getFont().getSize()));

						Label label = new Label("Password");
						label.setFont(Font.font(25));
						label.setTextFill(Color.BLANCHEDALMOND);
						label.setFont(Font.font(null, FontWeight.BOLD, back.getFont().getSize()));

						final PasswordField pb = new PasswordField();

						pb.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent e) {
								if (!pb.getText().equals("hala1234")) {
									message.setText("Your password is incorrect!");
									message.setFont(Font.font(15));
									message.setTextFill(Color.rgb(210, 39, 30));

								} else {
									BorderPane rr = new BorderPane();

									rr.setStyle("-fx-background-image: url('hun.jpg')");

									Button b1 = new Button("Data of Employees");
									b1.setMinSize(350, 30);
									b1.setTextFill(Color.BROWN);
									b1.setStyle("-fx-background-color:  #FFFFF0; ");
									b1.setFont(Font.font(null, FontWeight.BOLD, back.getFont().getSize()));

									Button b2 = new Button("Data of Customers");
									b2.setMinSize(350, 30);
									b2.setTextFill(Color.BROWN);
									b2.setStyle("-fx-background-color:  #FFFFF0; ");
									b2.setFont(Font.font(null, FontWeight.BOLD, back.getFont().getSize()));
									Button b3 = new Button("Data of Orders");
									b3.setMinSize(350, 30);
									b3.setTextFill(Color.BROWN);
									b3.setStyle("-fx-background-color:  #FFFFF0; ");
									b3.setFont(Font.font(null, FontWeight.BOLD, back.getFont().getSize()));
									Button b4 = new Button("Data of Supplier");
									b4.setMinSize(350, 30);
									b4.setTextFill(Color.BROWN);
									b4.setStyle("-fx-background-color:  #FFFFF0; ");
									b4.setFont(Font.font(null, FontWeight.BOLD, back.getFont().getSize()));
									Button b5 = new Button("Add Employee");
									b5.setMinSize(350, 30);
									b5.setTextFill(Color.BROWN);
									b5.setStyle("-fx-background-color:  #FFFFF0; ");
									b5.setFont(Font.font(null, FontWeight.BOLD, back.getFont().getSize()));
									/*
									 * Button b6 = new Button("Add Supplier"); b6.setMinSize(350, 30); Button b7 =
									 * new Button("Remove Supplier"); b7.setMinSize(350, 30);
									 */
									Button b8 = new Button("Remove Employee ");
									b8.setMinSize(350, 30);
									b8.setTextFill(Color.BROWN);
									b8.setStyle("-fx-background-color:  #FFFFF0; ");
									b8.setFont(Font.font(null, FontWeight.BOLD, back.getFont().getSize()));
									Button b9 = new Button("Update Employee ");
									b9.setMinSize(350, 30);
									b9.setTextFill(Color.BROWN);
									b9.setStyle("-fx-background-color:  #FFFFF0; ");
									b9.setFont(Font.font(null, FontWeight.BOLD, back.getFont().getSize()));
									Button back = new Button("Back ");
									back.setMinSize(350, 30);
									back.setTextFill(Color.BROWN);
									back.setStyle("-fx-background-color:  #FFFFF0; ");

									b1.setFont(Font.font(20));
									b2.setFont(Font.font(20));
									b3.setFont(Font.font(20));
									b4.setFont(Font.font(20));
									b5.setFont(Font.font(20));
									b8.setFont(Font.font(20));
									b9.setFont(Font.font(20));
									back.setFont(Font.font(20));

									VBox v = new VBox();
									v.getChildren().addAll(b1, b2, b3, b4, b5, b8, b9, back);
									v.setSpacing(10);
									v.setAlignment(Pos.CENTER);
									v.setPadding(new Insets(10, 10, 10, 10));
									rr.setCenter(v);

									Scene scene4 = new Scene(rr, 1550, 810);
									scene4.getStylesheets()
											.add(getClass().getResource("application.css").toExternalForm());
									primaryStage.setScene(scene4);
									primaryStage.show();

									b1.setOnAction(new EventHandler<ActionEvent>() {

										@Override
										public void handle(ActionEvent arg0) {
											// TODO Auto-generated method stub

											BorderPane root3 = new BorderPane();
											Scene scene5 = new Scene(root3, 1550, 810);
											scene5.getStylesheets()
													.add(getClass().getResource("application.css").toExternalForm());
											primaryStage.setScene(scene5);
											primaryStage.show();

											// root3.setAlignment(Pos.CENTER);
											// root3.setPadding(new Insets(10,10,10,10));

											var image = new Image("hun.jpg", true);
											var bgImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
													BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
													new BackgroundSize(1.0, 1.0, true, true, false, false));
											root3.setBackground(new Background(bgImage));

											final Label label = new Label("Employee Tabel ");
											label.setFont(new Font("Arial", 20));

											label.setTextFill(Color.BLANCHEDALMOND);

											label.setFont(Font.font(null, FontWeight.BOLD, label.getFont().getSize()));
											TableView<Employee> table = new TableView<>();
											table.setMaxSize(1500, 900);
											table.setEditable(true);

											TableColumn<Employee, Integer> id = new TableColumn<Employee, Integer>(
													"Employee Id");
											id.setCellValueFactory(
													p -> new SimpleIntegerProperty(p.getValue().getID()).asObject());
											id.setMinWidth(200);
											TableColumn<Employee, String> name = new TableColumn<Employee, String>(
													"Employee Name");
											name.setCellValueFactory(
													p -> new SimpleStringProperty(p.getValue().getEname()));
											name.setMinWidth(200);
											TableColumn<Employee, String> date = new TableColumn<Employee, String>(
													"Employee Date");
											date.setCellValueFactory(
													p -> new SimpleStringProperty(p.getValue().getBirth().toString()));
											date.setPrefWidth(200);
											TableColumn<Employee, String> add = new TableColumn<Employee, String>(
													"Employee Address");
											add.setCellValueFactory(
													p -> new SimpleStringProperty(p.getValue().getAddress()));
											add.setMinWidth(200);

											TableColumn<Employee, String> gender = new TableColumn<Employee, String>(
													"Gender");
											gender.setCellValueFactory(
													p -> new SimpleStringProperty(p.getValue().getGender()));
											gender.setMinWidth(200);
											TableColumn<Employee, String> des = new TableColumn<Employee, String>(
													"Designation");
											des.setCellValueFactory(
													p -> new SimpleStringProperty(p.getValue().getDesignation()));
											des.setMinWidth(200);
											TableColumn<Employee, String> phone = new TableColumn<Employee, String>(
													"Mobile");
											phone.setCellValueFactory(
													p -> new SimpleStringProperty(p.getValue().getPhone()));
											phone.setMinWidth(200);
											TableColumn<Employee, Integer> Salary = new TableColumn<Employee, Integer>(
													"Salary");
											Salary.setCellValueFactory(
													p -> new SimpleIntegerProperty(p.getValue().getSalary())
															.asObject());
											Salary.setMinWidth(200);

											ObservableList<Employee> data = FXCollections.observableArrayList();
											for (Employee i : emp) {
												data.add(i);
											}
											table.setItems(data);
											table.getColumns().addAll(id, name, date, add, gender, des, phone, Salary);

											final VBox vbox = new VBox();
											vbox.setSpacing(10);
											vbox.setPadding(new Insets(10, 0, 10, 10));
											Button back = new Button("Back");
											vbox.getChildren().addAll(label, table, back);
											vbox.setAlignment(Pos.CENTER);
											root3.setCenter(vbox);

											back.setTextFill(Color.BROWN);
											back.setStyle("-fx-background-color:  #FFFFF0; ");
											back.setFont(Font.font(20));
											back.setFont(Font.font(null, FontWeight.BOLD, back.getFont().getSize()));

											back.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													// TODO Auto-generated method stub
													primaryStage.setScene(scene4);
												}
											});

										}
									});

									b2.setOnAction(new EventHandler<ActionEvent>() {

										@Override
										public void handle(ActionEvent arg0) {
											// TODO Auto-generated method stub

											BorderPane root3 = new BorderPane();
											Scene scene5 = new Scene(root3, 1550, 810);
											scene5.getStylesheets()
													.add(getClass().getResource("application.css").toExternalForm());
											primaryStage.setScene(scene5);
											primaryStage.show();

											var image = new Image("hun.jpg", true);
											var bgImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
													BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
													new BackgroundSize(1.0, 1.0, true, true, false, false));
											root3.setBackground(new Background(bgImage));

											final Label label = new Label("Customer Tabel ");
											label.setFont(new Font("Arial", 20));

											label.setTextFill(Color.BLANCHEDALMOND);

											label.setFont(Font.font(null, FontWeight.BOLD, label.getFont().getSize()));
											TableView<customer> table = new TableView<>();
											table.setMaxSize(800, 900);
											table.setEditable(true);

											TableColumn<customer, Integer> id = new TableColumn<customer, Integer>(
													"customer Id");
											id.setCellValueFactory(
													p -> new SimpleIntegerProperty(p.getValue().getCust_id())
															.asObject());
											id.setMinWidth(200);
											TableColumn<customer, String> name = new TableColumn<customer, String>(
													"customer Name");
											name.setCellValueFactory(
													p -> new SimpleStringProperty(p.getValue().getCname()));
											name.setMinWidth(200);
											TableColumn<customer, String> email = new TableColumn<customer, String>(
													"customer Email");
											email.setCellValueFactory(
													p -> new SimpleStringProperty(p.getValue().getEmail()));
											email.setPrefWidth(200);
											TableColumn<customer, String> add = new TableColumn<customer, String>(
													"customer Address");
											add.setCellValueFactory(
													p -> new SimpleStringProperty(p.getValue().getAddress()));
											add.setMinWidth(200);

											ObservableList<customer> data = FXCollections.observableArrayList();
											for (customer i : c) {
												data.add(i);
											}
											table.setItems(data);
											table.getColumns().addAll(id, name, email, add);

											final VBox vbox = new VBox();
											vbox.setSpacing(10);
											vbox.setPadding(new Insets(10, 0, 10, 10));
											Button back = new Button("Back");
											vbox.getChildren().addAll(label, table, back);
											vbox.setAlignment(Pos.CENTER);
											root3.setCenter(vbox);

											back.setTextFill(Color.BROWN);

											back.setStyle("-fx-background-color:  #FFFFF0; ");
											back.setFont(Font.font(20));
											back.setFont(Font.font(null, FontWeight.BOLD, back.getFont().getSize()));

											back.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													// TODO Auto-generated method stub
													primaryStage.setScene(scene4);
												}
											});

										}

									});

									b3.setOnAction(new EventHandler<ActionEvent>() {

										@Override
										public void handle(ActionEvent arg0) {
											// TODO Auto-generated method stub

											BorderPane root3 = new BorderPane();
											Scene scene5 = new Scene(root3, 1550, 810);
											scene5.getStylesheets()
													.add(getClass().getResource("application.css").toExternalForm());
											primaryStage.setScene(scene5);
											primaryStage.show();

											var image = new Image("hun.jpg", true);
											var bgImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
													BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
													new BackgroundSize(1.0, 1.0, true, true, false, false));
											root3.setBackground(new Background(bgImage));

											final Label label = new Label("Order Tabel ");
											label.setFont(new Font("Arial", 20));

											label.setTextFill(Color.BLANCHEDALMOND);

											label.setFont(Font.font(null, FontWeight.BOLD, label.getFont().getSize()));
											TableView<OrderCustomer> table = new TableView<>();
											table.setMaxSize(1300, 900);
											table.setEditable(true);

											TableColumn<OrderCustomer, Integer> Oid = new TableColumn<OrderCustomer, Integer>(
													"Order Id");
											Oid.setCellValueFactory(
													p -> new SimpleIntegerProperty(p.getValue().getOrder_ID())
															.asObject());
											Oid.setMinWidth(200);
											TableColumn<OrderCustomer, String> date = new TableColumn<OrderCustomer, String>(
													"Order Date");
											date.setCellValueFactory(
													p -> new SimpleStringProperty(p.getValue().getDate().toString()));
											date.setPrefWidth(200);
											TableColumn<OrderCustomer, String> name = new TableColumn<OrderCustomer, String>(
													"Employee Name");
											name.setCellValueFactory(
													p -> new SimpleStringProperty(p.getValue().getEname()));
											name.setMinWidth(200);
											TableColumn<OrderCustomer, Integer> Eid = new TableColumn<OrderCustomer, Integer>(
													"Employee Id");
											Eid.setCellValueFactory(
													p -> new SimpleIntegerProperty(p.getValue().getID()).asObject());
											Eid.setPrefWidth(200);
											TableColumn<OrderCustomer, Integer> Cid = new TableColumn<OrderCustomer, Integer>(
													"Customer Id");
											Cid.setCellValueFactory(
													p -> new SimpleIntegerProperty(p.getValue().getCut_ID())
															.asObject());
											Cid.setMinWidth(200);

											TableColumn<OrderCustomer, Integer> meal = new TableColumn<OrderCustomer, Integer>(
													"Meal Id");
											meal.setCellValueFactory(
													p -> new SimpleIntegerProperty(p.getValue().getMeal_ID())
															.asObject());
											meal.setMinWidth(200);

											TableColumn<OrderCustomer, Integer> qn = new TableColumn<OrderCustomer, Integer>(
													"Quantity");
											qn.setCellValueFactory(
													p -> new SimpleIntegerProperty(p.getValue().getQuantity())
															.asObject());
											qn.setMinWidth(200);

											TableColumn<OrderCustomer, Double> pr = new TableColumn<OrderCustomer, Double>(
													"Price");
											pr.setCellValueFactory(
													p -> new SimpleDoubleProperty(p.getValue().getPrice()).asObject());
											pr.setMinWidth(200);

											ObservableList<OrderCustomer> data = FXCollections.observableArrayList();
											for (OrderCustomer i : ord_cus) {
												data.add(i);
											}
											table.setItems(data);
											table.getColumns().addAll(Oid, date, name, Eid, Cid, meal, qn, pr);

											final VBox vbox = new VBox();
											vbox.setSpacing(10);
											vbox.setPadding(new Insets(10, 0, 10, 10));
											Button back = new Button("Back");
											vbox.getChildren().addAll(label, table, back);
											vbox.setAlignment(Pos.CENTER);
											root3.setCenter(vbox);

											back.setTextFill(Color.BROWN);

											back.setStyle("-fx-background-color:  #FFFFF0; ");
											back.setFont(Font.font(20));
											back.setFont(Font.font(null, FontWeight.BOLD, back.getFont().getSize()));

											back.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													// TODO Auto-generated method stub
													primaryStage.setScene(scene4);
												}
											});

										}
									});
									b5.setOnAction(new EventHandler<ActionEvent>() {

										@Override
										public void handle(ActionEvent arg0) {
											// TODO Auto-generated method stub
											GridPane root2 = new GridPane();
											root2.setPadding(new Insets(20, 20, 10, 10));
											root2.setVgap(5);
											root2.setHgap(5);
											// root.setPadding(new Insets(90,30,30,30));
											root2.setAlignment(Pos.TOP_LEFT);
											root2.setStyle("-fx-background-image: url('hun.jpg')");
											Scene scene2 = new Scene(root2, 1550, 810);
											scene2.getStylesheets()
													.add(getClass().getResource("application.css").toExternalForm());
											primaryStage.setScene(scene2);
											primaryStage.show();

											Label ll = new Label("Enter Employee Data:");
											ll.setFont(Font.font(25));
											ll.setTextFill(Color.BLANCHEDALMOND);
											root2.add(ll, 0, 0);
											ll.setFont(Font.font(null, FontWeight.BOLD, ll.getFont().getSize()));

											Label name = new Label("Name");
											name.setFont(Font.font(25));
											name.setTextFill(Color.BLANCHEDALMOND);
											name.setFont(Font.font(null, FontWeight.BOLD, name.getFont().getSize()));
											TextField namee = new TextField();
											namee.setMinSize(150, 40);
											root2.add(name, 0, 1);
											root2.add(namee, 1, 1);
											Label mobile = new Label("Mobile");
											mobile.setTextFill(Color.BLANCHEDALMOND);
											mobile.setFont(Font.font(25));
											mobile.setFont(
													Font.font(null, FontWeight.BOLD, mobile.getFont().getSize()));
											TextField mobilee = new TextField();
											mobilee.setMinSize(150, 40);
											root2.add(mobile, 0, 2);
											root2.add(mobilee, 1, 2);
											Label g = new Label("gender");

											g.setTextFill(Color.BLANCHEDALMOND);
											g.setFont(Font.font(25));
											g.setFont(Font.font(null, FontWeight.BOLD, mobile.getFont().getSize()));

											RadioButton ch2 = new RadioButton("male");
											ch2.setTextFill(Color.BLANCHEDALMOND);
											ch2.setFont(Font.font(null, FontWeight.BOLD, 20));
											RadioButton ch1 = new RadioButton("female");
											ch1.setTextFill(Color.BLANCHEDALMOND);
											ch1.setFont(Font.font(null, FontWeight.BOLD, 20));
											ToggleGroup t = new ToggleGroup();
											HBox vv = new HBox();
											vv.setSpacing(10);
											vv.setAlignment(Pos.CENTER);
											ch1.setToggleGroup(t);
											ch2.setToggleGroup(t);
											vv.getChildren().addAll(ch1, ch2);

											root2.add(g, 0, 3);
											root2.add(vv, 1, 3);
											Label address = new Label("Address");
											address.setTextFill(Color.BLANCHEDALMOND);
											address.setFont(Font.font(25));
											address.setFont(
													Font.font(null, FontWeight.BOLD, mobile.getFont().getSize()));
											TextField addresss = new TextField();
											addresss.setMinSize(150, 40);
											root2.add(address, 0, 4);
											root2.add(addresss, 1, 4);
											root2.setVgap(25);
											root2.setHgap(25);
											Label d = new Label("Date");
											d.setTextFill(Color.BLANCHEDALMOND);
											d.setFont(Font.font(25));
											d.setFont(Font.font(null, FontWeight.BOLD, mobile.getFont().getSize()));
											DatePicker datePicker = new DatePicker();
											// datePicker.setMinSize(150, 30);
											// TextField dd=new TextField();
											datePicker.setMinSize(150, 40);
											root2.add(d, 0, 5);
											root2.add(datePicker, 1, 5);
											root2.setVgap(25);
											root2.setHgap(25);
											Label de = new Label("Designation");
											de.setTextFill(Color.BLANCHEDALMOND);
											de.setFont(Font.font(25));
											de.setFont(Font.font(null, FontWeight.BOLD, mobile.getFont().getSize()));
											// TextField dee=new TextField();
											ComboBox<String> dee = new ComboBox();
											dee.getItems().add("Cashier");
											dee.getItems().add("Chef");
											dee.getItems().add("Chef moderator");
											dee.getItems().add("Clean Worker");
											dee.getItems().add("Deleivery");
											dee.getItems().add("Waiter");

											dee.setMinSize(150, 40);
											root2.add(de, 0, 6);
											root2.add(dee, 1, 6);
											root2.setVgap(25);
											root2.setHgap(25);
											Label sal = new Label("Salary");
											sal.setTextFill(Color.BLANCHEDALMOND);
											sal.setFont(Font.font(25));
											sal.setFont(Font.font(null, FontWeight.BOLD, mobile.getFont().getSize()));
											TextField sall = new TextField();
											sall.setMinSize(150, 40);
											root2.add(sal, 0, 7);
											root2.add(sall, 1, 7);
											root2.setVgap(25);
											root2.setHgap(25);

											Button Add = new Button("Add");

											Add.setTextFill(Color.BROWN);

											Add.setStyle("-fx-background-color:  #FFFFF0; ");
											Add.setFont(Font.font(25));
											Add.setFont(Font.font(null, FontWeight.BOLD, mobile.getFont().getSize()));
											root2.add(Add, 0, 8);
											Add.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													// TODO Auto-generated method stub

													lastEmpId++;

													LocalDate d = datePicker.getValue();

													// System.out.println(d.toString());
													int day = d.getDayOfMonth();
													int month = d.getMonthValue() - 1;
													int year = d.getYear();
													Calendar cc = new GregorianCalendar(year, month, day);
													Date date = cc.getTime();

													try {
														Statement statement;
														statement = con.createStatement();
														PreparedStatement pstmt = con.prepareStatement(
																"INSERT INTO Employee (ID ,Address,birth,gender,ename,designation,phone,salary)  VALUES (?,?,?,?,?,?,?,?)");
														int value1 = lastEmpId;
														String value2 = addresss.getText();
														// Date value3= date;
														String value4 = "";
														String value5 = namee.getText();
														String value6 = dee.getValue();
														String value7 = mobilee.getText();
														int value8 = Integer.parseInt(sall.getText());

														pstmt.setInt(1, value1);
														pstmt.setString(2, value2);
														java.sql.Date sqlDate = new java.sql.Date(date.getTime());
														pstmt.setDate(3, sqlDate);

														pstmt.setString(5, value5);
														pstmt.setString(6, value6);
														pstmt.setString(7, value7);
														pstmt.setInt(8, value8);
														if (ch1.isSelected()) {

															emp.add(new Employee(lastEmpId, namee.getText(), date,
																	"female", addresss.getText(), dee.getValue(),
																	mobilee.getText(),
																	Integer.parseInt(sall.getText())));
															value4 = "female";
															pstmt.setString(4, value4);
														} else if (ch2.isSelected()) {

															emp.add(new Employee(lastEmpId, namee.getText(), date,
																	"male", addresss.getText(), dee.getValue(),
																	mobilee.getText(),
																	Integer.parseInt(sall.getText())));
															value4 = "male";
															pstmt.setString(4, value4);
														}

														pstmt.execute();

														// Step 6: Close the resources
														pstmt.close();
													} catch (SQLException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}

												}
											});

											Button bb2 = new Button("Back");

											bb2.setTextFill(Color.BROWN);
											bb2.setStyle("-fx-background-color:  #FFFFF0; ");
											bb2.setFont(Font.font(25));
											bb2.setFont(Font.font(null, FontWeight.BOLD, mobile.getFont().getSize()));
											root2.add(bb2, 1, 8);

											bb2.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													// TODO Auto-generated method stub
													primaryStage.setScene(scene4);
												}
											});

										}
									});

									b4.setOnAction(new EventHandler<ActionEvent>() {

										@Override
										public void handle(ActionEvent arg0) {
											// TODO Auto-generated method stub

											BorderPane root4 = new BorderPane();
											root4.setPadding(new Insets(20, 20, 10, 10));

											root4.setStyle("-fx-background-image: url('hun.jpg')");
											Scene scene5 = new Scene(root4, 1550, 810);
											scene5.getStylesheets()
													.add(getClass().getResource("application.css").toExternalForm());
											primaryStage.setScene(scene5);
											primaryStage.show();

											final Label label = new Label("Supplier Tabel ");
											label.setFont(new Font("Arial", 20));

											label.setTextFill(Color.BLANCHEDALMOND);

											label.setFont(Font.font(null, FontWeight.BOLD, label.getFont().getSize()));
											TableView<Supp_Ing> table = new TableView<>();
											table.setMaxSize(1300, 900);
											table.setEditable(true);

											TableColumn<Supp_Ing, Integer> id = new TableColumn<Supp_Ing, Integer>(
													"Supplier Id");
											id.setCellValueFactory(
													p -> new SimpleIntegerProperty(p.getValue().getSupp_ID())
															.asObject());
											id.setMinWidth(200);
											TableColumn<Supp_Ing, String> name = new TableColumn<Supp_Ing, String>(
													"Supplier Name");
											name.setCellValueFactory(
													p -> new SimpleStringProperty(p.getValue().getSupp_Name()));
											name.setMinWidth(200);
											TableColumn<Supp_Ing, String> phone = new TableColumn<Supp_Ing, String>(
													"Supplier Phone");
											phone.setCellValueFactory(
													p -> new SimpleStringProperty(p.getValue().getPhone()));
											phone.setPrefWidth(200);
											TableColumn<Supp_Ing, String> add = new TableColumn<Supp_Ing, String>(
													"Supplier Address");
											add.setCellValueFactory(
													p -> new SimpleStringProperty(p.getValue().getAddress()));
											add.setMinWidth(200);

											TableColumn<Supp_Ing, String> ing = new TableColumn<Supp_Ing, String>(
													"Ingredients");
											ing.setCellValueFactory(
													p -> new SimpleStringProperty(p.getValue().getIng_Name()));
											// ing.setMinWidth(200);
											ing.setPrefWidth(800);

											ObservableList<Supp_Ing> data = FXCollections.observableArrayList();
											for (Supp_Ing i : supp_ing) {

												data.add(i);
											}
											table.setItems(data);
											table.getColumns().addAll(id, name, phone, add, ing);

											final VBox vbox = new VBox();
											vbox.setSpacing(10);
											vbox.setPadding(new Insets(10, 0, 10, 10));
											Button back = new Button("Back");
											vbox.getChildren().addAll(label, table, back);
											vbox.setAlignment(Pos.CENTER);
											root4.setCenter(vbox);

											// back.setTextFill(Color.BLANCHEDALMOND);
											back.setTextFill(Color.BROWN);
											back.setStyle("-fx-background-color:  #FFFFF0; ");
											back.setFont(Font.font(20));
											back.setFont(Font.font(null, FontWeight.BOLD, back.getFont().getSize()));
											back.setAlignment(Pos.BOTTOM_CENTER);
											// root4.add(back, 0, 1000);

											back.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													// TODO Auto-generated method stub
													primaryStage.setScene(scene4);
												}
											});

										}
									});

									b8.setOnAction(new EventHandler<ActionEvent>() {

										@Override
										public void handle(ActionEvent arg0) {
											// TODO Auto-generated method stub
											GridPane root2 = new GridPane();
											root2.setPadding(new Insets(20, 20, 10, 10));
											root2.setVgap(5);
											root2.setHgap(5);
											// root.setPadding(new Insets(90,30,30,30));
											root2.setAlignment(Pos.TOP_LEFT);
											root2.setStyle("-fx-background-image: url('hun.jpg')");
											Scene scene2 = new Scene(root2, 1550, 810);
											scene2.getStylesheets()
													.add(getClass().getResource("application.css").toExternalForm());
											primaryStage.setScene(scene2);
											primaryStage.show();
											Label ll = new Label("Enter Employee ID:");
											ll.setFont(Font.font(25));
											ll.setTextFill(Color.BLANCHEDALMOND);
											root2.add(ll, 0, 0);
											ll.setFont(Font.font(null, FontWeight.BOLD, ll.getFont().getSize()));

											Label name = new Label("ID:");
											name.setFont(Font.font(25));
											name.setTextFill(Color.BLANCHEDALMOND);
											name.setFont(Font.font(null, FontWeight.BOLD, name.getFont().getSize()));
											TextField namee = new TextField();
											namee.setMinSize(150, 40);
											root2.add(name, 0, 1);
											root2.add(namee, 1, 1);

											Button bb1 = new Button("Delete");
											bb1.setTextFill(Color.BROWN);

											bb1.setStyle("-fx-background-color:  #FFFFF0; ");
											bb1.setFont(Font.font(25));
											bb1.setFont(Font.font(null, FontWeight.BOLD, bb1.getFont().getSize()));
											root2.add(bb1, 0, 4);
											bb1.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													// TODO Auto-generated method stub

													if (namee.getText() != null) {

														int i = 0;
														while (i < emp.size()) {

															if (emp.get(i).getID() == Integer
																	.valueOf(namee.getText())) {

																String query = "DELETE FROM Employee WHERE id = ?";
																emp.remove(i);
																PreparedStatement preparedStmt;
																try {
																	preparedStmt = con.prepareStatement(query);
																	preparedStmt.setInt(1,
																			Integer.valueOf(namee.getText()));
																	preparedStmt.executeUpdate();

																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}
																break;
															} else {
																i++;
															}

														}
														if (i > emp.size()) {
															Label aa = new Label("There's no Employee with this ID");

															aa.setFont(Font.font(25));
															aa.setTextFill(Color.rgb(210, 39, 30));
															root2.add(aa, 0, 10);

														}
													}
												}
											});
											Button bb2 = new Button("Back");
											// bb2.setTextFill(Color.BLANCHEDALMOND);
											bb2.setTextFill(Color.BROWN);
											bb2.setStyle("-fx-background-color:  #FFFFF0; ");
											bb2.setFont(Font.font(25));
											bb2.setFont(Font.font(null, FontWeight.BOLD, bb2.getFont().getSize()));
											root2.add(bb2, 1, 4);

											bb2.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													// TODO Auto-generated method stub
													primaryStage.setScene(scene4);
												}
											});
										}
									});

									b9.setOnAction(new EventHandler<ActionEvent>() {

										@Override
										public void handle(ActionEvent arg0) {
											// TODO Auto-generated method stub

											GridPane root2 = new GridPane();
											root2.setPadding(new Insets(20, 20, 10, 10));
											root2.setVgap(5);
											root2.setHgap(5);
											// root.setPadding(new Insets(90,30,30,30));
											root2.setAlignment(Pos.TOP_LEFT);
											root2.setStyle("-fx-background-image: url('hun.jpg')");
											Scene scene2 = new Scene(root2, 1550, 810);
											scene2.getStylesheets()
													.add(getClass().getResource("application.css").toExternalForm());
											primaryStage.setScene(scene2);
											primaryStage.show();
											Label ll = new Label("Enter Employee ID:");
											ll.setFont(Font.font(25));
											ll.setTextFill(Color.BLANCHEDALMOND);
											root2.add(ll, 0, 0);
											ll.setFont(Font.font(null, FontWeight.BOLD, ll.getFont().getSize()));

											Label name = new Label("ID:");
											name.setFont(Font.font(25));
											name.setTextFill(Color.BLANCHEDALMOND);
											name.setFont(Font.font(null, FontWeight.BOLD, name.getFont().getSize()));
											TextField namee = new TextField();
											namee.setMinSize(150, 40);
											root2.add(name, 0, 1);
											root2.add(namee, 1, 1);

											Button bb1 = new Button("Select");
											// bb1.setTextFill(Color.BLANCHEDALMOND);
											bb1.setTextFill(Color.BROWN);
											bb1.setStyle("-fx-background-color:  #FFFFF0; ");
											bb1.setFont(Font.font(25));
											bb1.setFont(Font.font(null, FontWeight.BOLD, bb1.getFont().getSize()));
											root2.add(bb1, 0, 4);

											bb1.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													// TODO Auto-generated method stub

													if (namee.getText() != null) {

														for (; i < emp.size(); i++) {
															if (emp.get(i).getID() == Integer
																	.valueOf(namee.getText())) {
																int x = i;
																GridPane root4 = new GridPane();
																root4.setPadding(new Insets(20, 20, 10, 10));
																root4.setVgap(5);
																root4.setHgap(5);
																// root.setPadding(new Insets(90,30,30,30));
																root4.setAlignment(Pos.TOP_LEFT);
																root4.setStyle("-fx-background-image: url('hun.jpg')");
																Scene scene2 = new Scene(root4, 1550, 810);
																scene2.getStylesheets()
																		.add(getClass().getResource("application.css")
																				.toExternalForm());
																primaryStage.setScene(scene2);
																primaryStage.show();

																Label ll = new Label("Enter Employee Data:");
																ll.setFont(Font.font(25));
																ll.setTextFill(Color.BLANCHEDALMOND);
																root4.add(ll, 0, 0);
																ll.setFont(Font.font(null, FontWeight.BOLD,
																		ll.getFont().getSize()));

																TextField namee = new TextField(emp.get(i).getEname());

																namee.setMinSize(150, 40);

																RadioButton r1 = new RadioButton("Name");

																r1.setTextFill(Color.BLANCHEDALMOND);
																r1.setFont(Font.font(null, FontWeight.BOLD, 25));
																root4.add(r1, 0, 1);

																root4.add(namee, 1, 1);

																RadioButton r2 = new RadioButton("Mobile");
																r2.setTextFill(Color.BLANCHEDALMOND);
																r2.setFont(Font.font(null, FontWeight.BOLD, 25));

																TextField mobilee = new TextField(
																		emp.get(i).getPhone());
																mobilee.setMinSize(150, 40);
																root4.add(r2, 0, 2);
																root4.add(mobilee, 1, 2);

																RadioButton r3 = new RadioButton("Gender");
																r3.setTextFill(Color.BLANCHEDALMOND);
																r3.setFont(Font.font(null, FontWeight.BOLD, 25));

																RadioButton ch2 = new RadioButton("male");
																ch2.setTextFill(Color.BLANCHEDALMOND);
																ch2.setFont(Font.font(null, FontWeight.BOLD, 20));
																RadioButton ch1 = new RadioButton("female");
																ch1.setTextFill(Color.BLANCHEDALMOND);
																ch1.setFont(Font.font(null, FontWeight.BOLD, 20));
																ToggleGroup t = new ToggleGroup();
																HBox vv = new HBox();
																vv.setSpacing(10);
																vv.setAlignment(Pos.CENTER);
																ch1.setToggleGroup(t);
																ch2.setToggleGroup(t);
																vv.getChildren().addAll(ch1, ch2);
																if (emp.get(i).getGender().compareTo("male") == 0) {
																	ch2.setSelected(true);
																	ch1.setSelected(false);
																} else if (emp.get(i).getGender()
																		.compareTo("female") == 0) {
																	ch1.setSelected(true);
																	ch2.setSelected(false);
																}

																root4.add(r3, 0, 3);
																root4.add(vv, 1, 3);

																RadioButton r4 = new RadioButton("Address");
																r4.setTextFill(Color.BLANCHEDALMOND);
																r4.setFont(Font.font(null, FontWeight.BOLD, 25));
																TextField addresss = new TextField(
																		emp.get(i).getAddress());
																addresss.setMinSize(150, 40);
																root4.add(r4, 0, 4);
																root4.add(addresss, 1, 4);
																root4.setVgap(25);
																root4.setHgap(25);

																RadioButton r5 = new RadioButton("Date");
																r5.setTextFill(Color.BLANCHEDALMOND);
																r5.setFont(Font.font(null, FontWeight.BOLD, 25));
																DatePicker datePicker = new DatePicker();
																LocalDate localDate = Instant
																		.ofEpochMilli(emp.get(i).getBirth().getTime())
																		.atZone(ZoneId.systemDefault()).toLocalDate();

																datePicker.setValue(localDate);
																datePicker.setMinSize(150, 30);
																// TextField dd=new TextField();
																datePicker.setMinSize(150, 40);
																root4.add(r5, 0, 5);
																root4.add(datePicker, 1, 5);
																root4.setVgap(25);
																root4.setHgap(25);

																RadioButton r6 = new RadioButton("Designation");
																r6.setTextFill(Color.BLANCHEDALMOND);
																r6.setFont(Font.font(null, FontWeight.BOLD, 25));
																// TextField dee=new TextField();
																ComboBox<String> dee = new ComboBox();
																dee.getItems().add("Cashier");
																dee.getItems().add("Chef");
																dee.getItems().add("Chef moderator");
																dee.getItems().add("Clean Worker");
																dee.getItems().add("Deleivery");
																dee.getItems().add("Waiter");

																dee.setValue(emp.get(i).getDesignation());
																// dee.setValue("waiter");
																dee.setMinSize(150, 40);
																root4.add(r6, 0, 6);
																root4.add(dee, 1, 6);
																root4.setVgap(25);
																root4.setHgap(25);

																RadioButton r7 = new RadioButton("Salary");
																r7.setTextFill(Color.BLANCHEDALMOND);
																r7.setFont(Font.font(null, FontWeight.BOLD, 25));
																TextField sall = new TextField(
																		String.valueOf(emp.get(i).getSalary()));
																sall.setMinSize(150, 40);
																root4.add(r7, 0, 7);
																root4.add(sall, 1, 7);
																root4.setVgap(25);
																root4.setHgap(25);

																Button update = new Button("Update");
																update.setTextFill(Color.BROWN);

																update.setStyle("-fx-background-color:  #FFFFF0; ");
																update.setFont(Font.font(25));
																update.setFont(Font.font(null, FontWeight.BOLD,
																		bb1.getFont().getSize()));
																root4.add(update, 0, 8);

																Button bb1 = new Button("Back");
																bb1.setTextFill(Color.BROWN);

																bb1.setStyle("-fx-background-color:  #FFFFF0; ");
																bb1.setFont(Font.font(25));
																bb1.setFont(Font.font(null, FontWeight.BOLD,
																		bb1.getFont().getSize()));
																root4.add(bb1, 1, 8);

																update.setOnAction(new EventHandler<ActionEvent>() {

																	@Override
																	public void handle(ActionEvent arg0) {
																		// TODO Auto-generated method stub

																		if (r1.isSelected()) {

																			emp.get(x).setEname(namee.getText());

																			String query = "Update Employee SET  ename = ? WHERE ID = ?";
																			PreparedStatement pstmt;
																			try {
																				pstmt = con.prepareStatement(query);
																				String value1 = namee.getText();

																				int value2 = emp.get(x).getID();

																				pstmt.setString(1, value1);
																				pstmt.setInt(2, value2);

																				pstmt.execute();
																				pstmt.close();
																			} catch (SQLException e) {
																				// TODO Auto-generated catch block
																				e.printStackTrace();
																			}

																		}

																		if (r2.isSelected()) {

																			emp.get(x).setPhone(mobilee.getText());

																			String query = "Update Employee SET  phone = ? WHERE ID = ?";
																			PreparedStatement pstmt;
																			try {
																				pstmt = con.prepareStatement(query);
																				String value1 = mobilee.getText();

																				int value2 = emp.get(x).getID();

																				pstmt.setString(1, value1);
																				pstmt.setInt(2, value2);

																				pstmt.execute();
																				pstmt.close();
																			} catch (SQLException e) {
																				// TODO Auto-generated catch block
																				e.printStackTrace();
																			}

																		}

																		if (r3.isSelected()) {

																			String query = "Update Employee SET  gender = ? WHERE ID = ?";
																			PreparedStatement pstmt;
																			try {
																				pstmt = con.prepareStatement(query);

																				String value1 = "";
																				if (ch1.isSelected()) {

																					value1 = "female";

																				} else if (ch2.isSelected()) {
																					value1 = "male";
																					// pstmt.setString(3, value3);
																				}

																				emp.get(x).setGender(value1);

																				int value2 = emp.get(x).getID();

																				pstmt.setString(1, value1);
																				pstmt.setInt(2, value2);

																				pstmt.execute();
																				pstmt.close();
																			} catch (SQLException e) {
																				// TODO Auto-generated catch block
																				e.printStackTrace();
																			}

																		}
																		if (r4.isSelected()) {

																			emp.get(x).setAddress(addresss.getText());

																			String query = "Update  Employee SET  Address = ? WHERE ID = ?";
																			PreparedStatement pstmt;
																			try {
																				pstmt = con.prepareStatement(query);
																				String value1 = addresss.getText();

																				int value2 = emp.get(x).getID();

																				pstmt.setString(1, value1);
																				pstmt.setInt(2, value2);

																				pstmt.execute();
																				pstmt.close();
																			} catch (SQLException e) {
																				// TODO Auto-generated catch block
																				e.printStackTrace();
																			}

																		}
																		if (r5.isSelected()) {
																			LocalDate d = datePicker.getValue();

																			// System.out.println(d.toString());
																			int day = d.getDayOfMonth();
																			int month = d.getMonthValue() - 1;
																			int year = d.getYear();
																			Calendar cc = new GregorianCalendar(year,
																					month, day);
																			Date date = cc.getTime();

																			emp.get(x).setBirth(date);

																			String query = "Update Employee SET  birth = ? WHERE ID = ?";
																			PreparedStatement pstmt;
																			try {
																				pstmt = con.prepareStatement(query);
																				java.sql.Date sqlDate = new java.sql.Date(
																						date.getTime());
																				pstmt.setDate(1, sqlDate);

																				int value2 = emp.get(x).getID();

																				pstmt.setInt(2, value2);

																				pstmt.execute();
																				pstmt.close();
																			} catch (SQLException e) {
																				// TODO Auto-generated catch block
																				e.printStackTrace();
																			}

																		}

																		if (r6.isSelected()) {

																			emp.get(x).setDesignation(dee.getValue());

																			String query = "Update Employee SET  Designation = ? WHERE ID = ?";
																			PreparedStatement pstmt;
																			try {
																				pstmt = con.prepareStatement(query);
																				String value1 = dee.getValue();

																				int value2 = emp.get(x).getID();

																				pstmt.setString(1, value1);
																				pstmt.setInt(2, value2);

																				pstmt.execute();
																				pstmt.close();
																			} catch (SQLException e) {
																				// TODO Auto-generated catch block
																				e.printStackTrace();
																			}

																		}

																		if (r7.isSelected()) {

																			emp.get(x).setSalary(
																					Integer.parseInt(sall.getText()));

																			String query = "Update Employee SET  Salary = ? WHERE ID = ?";
																			PreparedStatement pstmt;
																			try {
																				pstmt = con.prepareStatement(query);

																				int value1 = Integer
																						.parseInt(sall.getText());
																				// String value1=dee.getValue();

																				int value2 = emp.get(x).getID();

																				pstmt.setInt(1, value1);
																				pstmt.setInt(2, value2);

																				pstmt.execute();
																				pstmt.close();
																			} catch (SQLException e) {
																				// TODO Auto-generated catch block
																				e.printStackTrace();
																			}

																		}
																	}
																});
																bb1.setOnAction(new EventHandler<ActionEvent>() {

																	@Override
																	public void handle(ActionEvent arg0) {
																		// TODO Auto-generated method stub
																		primaryStage.setScene(scene4);
																	}
																});

															} else {
																Label aa = new Label(
																		"There's no Employee with this ID");

																aa.setFont(Font.font(25));
																aa.setTextFill(Color.rgb(210, 39, 30));
																root2.add(aa, 0, 10);
															}
														}
													}
												}
											});

											Button bb2 = new Button("Back");
											// bb2.setTextFill(Color.BLANCHEDALMOND);
											bb2.setTextFill(Color.BROWN);
											bb2.setStyle("-fx-background-color:  #FFFFF0; ");
											bb2.setFont(Font.font(25));
											bb2.setFont(Font.font(null, FontWeight.BOLD, bb2.getFont().getSize()));
											root2.add(bb2, 1, 4);

											bb2.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													// TODO Auto-generated method stub
													primaryStage.setScene(scene4);
												}
											});

										}
									});

									back.setOnAction(new EventHandler<ActionEvent>() {

										@Override
										public void handle(ActionEvent arg0) {
											// TODO Auto-generated method stub
											primaryStage.setScene(scene);

										}
									});

									{
										message.setText("Your password has been confirmed");
										message.setFont(Font.font(25));
										message.setTextFill(Color.rgb(21, 117, 84));
									}
									pb.clear();
								}
							}
						});

						hb.getChildren().addAll(label, pb);
						vb.getChildren().addAll(hb, message);
						g.add(vb, 0, 0);
						g.add(back, 0, 1);
						back.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent arg0) {
								// TODO Auto-generated method stub
								primaryStage.setScene(scene);

							}
						});

					}
				});

				cus.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						// TODO Auto-generated method stub

						// BorderPane root2=new BorderPane();

						GridPane root2 = new GridPane();

						root2.setVgap(5);
						root2.setHgap(5);
						root2.setAlignment(Pos.CENTER);
						root2.setStyle("-fx-background-image: url('hun.jpg')");
						Scene scene2 = new Scene(root2, 1550, 810);
						scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						primaryStage.setScene(scene2);
						primaryStage.show();

						Label ll = new Label("Please Enter your Data:");
						ll.setFont(Font.font(25));
						ll.setTextFill(Color.BLANCHEDALMOND);
						root2.add(ll, 0, 0);
						ll.setFont(Font.font(null, FontWeight.BOLD, ll.getFont().getSize()));

						Label name = new Label("Name");
						name.setFont(Font.font(25));
						name.setTextFill(Color.BLANCHEDALMOND);
						name.setFont(Font.font(null, FontWeight.BOLD, name.getFont().getSize()));
						TextField namee = new TextField();
						namee.setMinSize(150, 40);
						root2.add(name, 0, 1);
						root2.add(namee, 1, 1);
						Label mobile = new Label("Mobile");
						mobile.setTextFill(Color.BLANCHEDALMOND);
						mobile.setFont(Font.font(25));
						mobile.setFont(Font.font(null, FontWeight.BOLD, mobile.getFont().getSize()));
						TextField mobilee = new TextField();
						mobilee.setMinSize(150, 40);
						root2.add(mobile, 0, 2);
						root2.add(mobilee, 1, 2);
						Label address = new Label("Address");
						address.setTextFill(Color.BLANCHEDALMOND);
						address.setFont(Font.font(25));
						address.setFont(Font.font(null, FontWeight.BOLD, mobile.getFont().getSize()));
						TextField addresss = new TextField();
						addresss.setMinSize(150, 40);
						root2.add(address, 0, 3);
						root2.add(addresss, 1, 3);

						Label e = new Label("E-mail");
						e.setTextFill(Color.BLANCHEDALMOND);
						e.setFont(Font.font(25));
						e.setFont(Font.font(null, FontWeight.BOLD, mobile.getFont().getSize()));
						TextField ee = new TextField();
						ee.setMinSize(150, 40);
						root2.add(e, 0, 4);
						root2.add(ee, 1, 4);
						root2.setVgap(25);
						root2.setHgap(25);

						Button bb = new Button("Menu");

						bb.setTextFill(Color.BROWN);

						bb.setStyle("-fx-background-color:  #FFFFF0; ");
						bb.setFont(Font.font(25));
						bb.setFont(Font.font(null, FontWeight.BOLD, mobile.getFont().getSize()));
						root2.add(bb, 1, 5);

						Button bb2 = new Button("Back");

						// bb2.setTextFill(Color.BLANCHEDALMOND);
						bb2.setTextFill(Color.BROWN);
						bb2.setStyle("-fx-background-color:  #FFFFF0; ");
						bb2.setFont(Font.font(25));
						bb2.setFont(Font.font(null, FontWeight.BOLD, mobile.getFont().getSize()));
						root2.add(bb2, 0, 6);

						Button add = new Button("Add");

						add.setTextFill(Color.BROWN);
						add.setStyle("-fx-background-color:  #FFFFF0; ");
						add.setFont(Font.font(25));
						add.setMinWidth(100);
						add.setFont(Font.font(null, FontWeight.BOLD, add.getFont().getSize()));
						root2.add(add, 0, 5);

						add.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent arg0) {
								Statement statement;
								try {

									if (namee.getText() != "" && mobilee.getText() != "" && addresss.getText() != ""
											&& ee.getText() != "") {

										statement = con.createStatement();

										customerid = ++customerid;
										PreparedStatement stmt = con.prepareStatement(
												"insert into customer (Cust_ID,cname,email,address) values(?,?,?,?)");
										
										c.add(new customer(customerid, namee.getText(), ee.getText(),
												addresss.getText()));
										stmt.setInt(1, customerid);
										stmt.setString(2, namee.getText());

										stmt.setString(3, ee.getText());
										stmt.setString(4, addresss.getText());
										stmt.execute();
									} else {
										Label message = new Label("Please check the TextFields");
										message.setFont(Font.font(20));
										message.setFont(Font.font(null, FontWeight.BOLD, mobile.getFont().getSize()));
										message.setTextFill(Color.rgb(210, 39, 30));
										root2.add(message, 0, 7);
									}

									// statement.executeUpdate("INSERT INTO customer" +"VALUES
									// (namee.getText(),emailee.getText(),addresss.getText())");
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}

							}

						});

						bb2.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent arg0) {
								// TODO Auto-generated method stub
								primaryStage.setScene(scene);
							}
						});

						bb.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent arg0) {
								// TODO Auto-generated method stub

								if (namee.getText() != "" && mobilee.getText() != "" && addresss.getText() != ""
										&& ee.getText() != "") {
									BorderPane rr = new BorderPane();

									rr.setPadding(new Insets(100, 100, 100, 100));
									rr.setStyle("-fx-background-image: url('hun.jpg')");
									Scene scene2 = new Scene(rr, 1550, 810);
									scene2.getStylesheets()
											.add(getClass().getResource("application.css").toExternalForm());
									primaryStage.setScene(scene2);
									primaryStage.show();

									Circle r1 = new Circle();
									r1.setRadius(70.0);
									Image image = new Image("burger.jpg");
									r1.setFill(new ImagePattern(image));

									Circle r2 = new Circle();
									r2.setRadius(70.0);
									Image image2 = new Image("appetizers.png");
									r2.setFill(new ImagePattern(image2));

									Circle r3 = new Circle();
									r3.setRadius(70.0);
									Image image3 = new Image("ss.jpg");
									r3.setFill(new ImagePattern(image3));

									Circle r4 = new Circle();
									r4.setRadius(70.0);
									Image image4 = new Image("friesss.jpg");
									r4.setFill(new ImagePattern(image4));

									Circle r5 = new Circle();
									r5.setRadius(70.0);
									Image image5 = new Image("drinks.jpg");
									r5.setFill(new ImagePattern(image5));

									Circle circle = new Circle(70);
									Button Burger = new Button(null, r1);
									Burger.setShape(circle);

									Button done = new Button("Done!");
									done.setTextFill(Color.BROWN);
									done.setStyle("-fx-background-color:  #FFFFF0; ");
									done.setFont(Font.font(25));
									done.setFont(Font.font(null, FontWeight.BOLD, done.getFont().getSize()));

									Button bb2 = new Button("Back");

									// bb2.setTextFill(Color.BLANCHEDALMOND);
									bb2.setTextFill(Color.BROWN);
									bb2.setStyle("-fx-background-color:  #FFFFF0; ");
									bb2.setFont(Font.font(25));
									bb2.setFont(Font.font(null, FontWeight.BOLD, bb2.getFont().getSize()));
									// root2.add(bb2, 1, 5);
									HBox h2 = new HBox();
									h2.getChildren().addAll(done, bb2);
									h2.setSpacing(10);

									Burger.setOnAction(new EventHandler<ActionEvent>() {

										@Override
										public void handle(ActionEvent arg0) {
											// TODO Auto-generated method stub

											GridPane root3 = new GridPane();
											root3.setAlignment(Pos.TOP_LEFT);
											root3.setPadding(new Insets(10, 10, 10, 10));

											var image = new Image("BB.jpg", true);
											var bgImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
													BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
													new BackgroundSize(1.0, 1.0, true, true, false, false));
											root3.setBackground(new Background(bgImage));

											HBox h = new HBox();

											Button back = new Button("Back");

											// bb2.setTextFill(Color.BLANCHEDALMOND);
											back.setTextFill(Color.BROWN);
											back.setStyle("-fx-background-color:  #FFFFF0; ");
											back.setFont(Font.font(25));
											back.setFont(Font.font(null, FontWeight.BOLD, bb2.getFont().getSize()));

											Button add = new Button("Add");

											// bb2.setTextFill(Color.BLANCHEDALMOND);
											add.setTextFill(Color.BROWN);
											add.setStyle("-fx-background-color:  #FFFFF0; ");
											add.setFont(Font.font(25));
											add.setFont(Font.font(null, FontWeight.BOLD, bb2.getFont().getSize()));

											// .setAlignment(Pos.BOTTOM_CENTER);
											h.getChildren().addAll(add, back);
											h.setSpacing(10);
											h.setAlignment(Pos.CENTER);

											/// root3.setStyle("-fx-background-image: url('BB2.jpg')");
											back.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													// TODO Auto-generated method stub
													primaryStage.setScene(scene2);
												}
											});

											Scene scene3 = new Scene(root3, 1550, 810);
											scene3.getStylesheets()
													.add(getClass().getResource("application.css").toExternalForm());
											primaryStage.setScene(scene3);
											primaryStage.show();

											Button a = new Button("+");
											a.setFont(Font.font(20));
											a.setTextFill(Color.BROWN);
											a.setStyle("-fx-background-color:  #FFFFF0; ");
											Button b = new Button("+");
											b.setFont(Font.font(20));
											b.setTextFill(Color.BROWN);
											b.setStyle("-fx-background-color:  #FFFFF0; ");
											Button c = new Button("+");
											c.setFont(Font.font(20));
											c.setTextFill(Color.BROWN);
											c.setStyle("-fx-background-color:  #FFFFF0; ");
											Button d = new Button("+");
											d.setFont(Font.font(20));
											d.setTextFill(Color.BROWN);
											d.setStyle("-fx-background-color:  #FFFFF0; ");

											int jj = 0;
											int r = 1;
											for (int i = 1; i <= 4; ++i) {
												if (i == 1) {
													root3.add(a, 1, r);
												}
												if (i == 2) {
													root3.add(b, 1, r);
												}
												if (i == 3) {
													root3.add(c, 1, r);
												}
												if (i == 4) {
													root3.add(d, 1, r);
												}
												r = r + 2;
											}

											for (int j = 0; j < meal_ing.size(); ++j) {
												if (meal_ing.get(j).mealType.compareToIgnoreCase("burger") == 0) {

													Label l1 = new Label(meal_ing.get(j).mealname);
													l1.setFont(Font.font(45));
													l1.setTextFill(Color.BLANCHEDALMOND);
													Label l2 = new Label(String.valueOf(meal_ing.get(j).price));
													l2.setFont(Font.font(25));

													l2.setTextFill(Color.BLANCHEDALMOND);
													Label l3 = new Label(meal_ing.get(j).getIng());
													l3.setFont(Font.font(20));
													l3.setTextFill(Color.BLANCHEDALMOND);
													map2.put(meal_ing.get(j).mealname, meal_ing.get(j).id_meal);
													map.put(meal_ing.get(j).mealname, meal_ing.get(j).price);
													root3.add(l1, 0, jj);
													root3.add(l2, 1, jj);

													jj++;
													root3.add(l3, 0, jj);

													jj++;

												}

											}

											root3.add(h, 0, jj + 2);
											a.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Classic Burger") == true) {
														int id = map2.get("Classic Burger");
														double price = map.get("Classic Burger");
														System.out.print(id);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();
														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");
																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {

																	e.printStackTrace();
																}

															}

														});

													}
												}

											});
											b.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Hungers Burger") == true) {
														int id = map2.get("Hungers Burger");
														double price = map.get("Hungers Burger");
														System.out.print(id);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});
											c.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Chicken Burger") == true) {
														int id = map2.get("Chicken Burger");
														Double price = map.get("Chicken Burger");
														System.out.print(id);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});
											d.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Mashroom Burger") == true) {
														int id = map2.get("Mashroom Burger");
														double price = map.get("Mashroom Burger");

														System.out.println(id);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});
										}
									});

									Button App = new Button(null, r2);
									App.setShape(circle);

									App.setOnAction(new EventHandler<ActionEvent>() {

										@Override
										public void handle(ActionEvent arg0) {
											// TODO Auto-generated method stub

											GridPane root3 = new GridPane();
											root3.setAlignment(Pos.TOP_LEFT);
											root3.setPadding(new Insets(10, 10, 10, 10));

											var image = new Image("app.jpg", true);
											var bgImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
													BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
													new BackgroundSize(1.0, 1.0, true, true, false, false));
											root3.setBackground(new Background(bgImage));

											HBox h = new HBox();

											Button back = new Button("Back");
											back.setTextFill(Color.BROWN);

											back.setStyle("-fx-background-color:  #FFFFF0; ");
											back.setFont(Font.font(25));
											back.setFont(Font.font(null, FontWeight.BOLD, mobile.getFont().getSize()));

											Button add = new Button("Add");
											add.setTextFill(Color.BROWN);

											add.setStyle("-fx-background-color:  #FFFFF0; ");
											add.setFont(Font.font(25));
											add.setFont(Font.font(null, FontWeight.BOLD, mobile.getFont().getSize()));

											h.getChildren().addAll(add, back);
											h.setSpacing(10);
											h.setAlignment(Pos.CENTER);

											back.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													// TODO Auto-generated method stub
													primaryStage.setScene(scene2);
												}
											});

											Scene scene3 = new Scene(root3, 1550, 810);
											scene3.getStylesheets()
													.add(getClass().getResource("application.css").toExternalForm());
											primaryStage.setScene(scene3);
											primaryStage.show();

											Button a = new Button("+");
											a.setFont(Font.font(20));
											a.setTextFill(Color.BROWN);
											a.setStyle("-fx-background-color:  #FFFFF0; ");
											Button b = new Button("+");
											b.setFont(Font.font(20));
											b.setTextFill(Color.BROWN);
											b.setStyle("-fx-background-color:  #FFFFF0; ");
											Button c = new Button("+");
											c.setFont(Font.font(20));
											c.setTextFill(Color.BROWN);
											c.setStyle("-fx-background-color:  #FFFFF0; ");
											Button d = new Button("+");
											d.setFont(Font.font(20));
											d.setTextFill(Color.BROWN);
											d.setStyle("-fx-background-color:  #FFFFF0; ");
											Button e = new Button("+");
											e.setFont(Font.font(20));
											e.setTextFill(Color.BROWN);
											e.setStyle("-fx-background-color:  #FFFFF0; ");
											Button f = new Button("+");
											f.setFont(Font.font(20));
											f.setTextFill(Color.BROWN);
											f.setStyle("-fx-background-color:  #FFFFF0; ");
											Button g = new Button("+");
											g.setFont(Font.font(20));
											g.setTextFill(Color.BROWN);
											g.setStyle("-fx-background-color:  #FFFFF0; ");

											int jj = 0;
											int r = 1;
											for (int ii = 1; ii <= 8; ++ii) {
												if (ii == 1) {
													root3.add(a, 1, r);
												}
												if (ii == 2) {
													root3.add(b, 1, r);
												}
												if (ii == 3) {
													root3.add(c, 1, r);
												}
												if (ii == 4) {
													root3.add(d, 1, r);
												}
												if (ii == 5) {
													root3.add(e, 1, r);
												}
												if (ii == 6) {
													root3.add(f, 1, r);
												}
												if (ii == 7) {
													root3.add(g, 1, r);
												}

												r = r + 2;
											}

											for (int j = 0; j < m.size(); ++j) {
												if (m.get(j).m_Type.compareToIgnoreCase("Appetizeres") == 0) {

													Label l1 = new Label(m.get(j).m_Name);
													l1.setFont(Font.font(30));
													l1.setTextFill(Color.BLANCHEDALMOND);
													Label l2 = new Label(String.valueOf(m.get(j).price));
													l2.setFont(Font.font(25));
													l2.setTextFill(Color.BLANCHEDALMOND);

													map2.put(m.get(j).m_Name, m.get(j).ID);

													map.put(m.get(j).m_Name, m.get(j).price);
													root3.add(l1, 0, jj);
													root3.add(l2, 1, jj);

													jj = jj + 2;

												}

											}

											root3.add(h, 0, jj + 2);

											a.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Spicy wings") == true) {
														int id = map2.get("Spicy wings");
														Double price = map.get("Spicy wings");

														System.out.print(id);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});

											b.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Barbeque wings") == true) {
														int id = map2.get("Barbeque wings");
														Double price = map.get("Barbeque wings");
														System.out.print(id);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});

											c.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Onion rings") == true) {
														int id = map2.get("Onion rings");
														Double price = map.get("Onion rings");
														System.out.print(id);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});

											d.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Mozarella sticks") == true) {
														int id = map2.get("Mozarella sticks");
														Double price = map.get("Mozarella sticks");
														System.out.print(id);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});
											e.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Popcorn chicken") == true) {
														int id = map2.get("Popcorn chicken");
														Double price = map.get("Popcorn chicken");
														System.out.print(id);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});
											f.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Cheese balls") == true) {
														int id = map2.get("Cheese balls");
														Double price = map.get("Cheese balls");
														System.out.print(id);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});
											g.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Cheese sause") == true) {
														int id = map2.get("Cheese sause");
														Double price = map.get("Cheese sause");
														System.out.print(id);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});

										}

									});

									Button salads = new Button(null, r3);
									salads.setShape(circle);

									salads.setOnAction(new EventHandler<ActionEvent>() {

										@Override
										public void handle(ActionEvent arg0) {
											// TODO Auto-generated method stub

											GridPane root3 = new GridPane();
											root3.setAlignment(Pos.TOP_LEFT);
											root3.setPadding(new Insets(10, 10, 10, 10));

											var image = new Image("salads2.jpg", true);
											var bgImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
													BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
													new BackgroundSize(1.0, 1.0, true, true, false, false));
											root3.setBackground(new Background(bgImage));

											HBox h = new HBox();

											Button back = new Button("Back");
											back.setTextFill(Color.BROWN);
											back.setStyle("-fx-background-color:  #FFFFF0; ");
											back.setFont(Font.font(25));
											back.setFont(Font.font(null, FontWeight.BOLD, back.getFont().getSize()));

											Button add = new Button("Add");
											add.setTextFill(Color.BROWN);
											add.setStyle("-fx-background-color:  #FFFFF0; ");
											add.setFont(Font.font(25));
											add.setFont(Font.font(null, FontWeight.BOLD, add.getFont().getSize()));
											h.getChildren().addAll(add, back);
											h.setSpacing(10);
											h.setAlignment(Pos.CENTER);

											/// root3.setStyle("-fx-background-image: url('BB2.jpg')");
											back.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													// TODO Auto-generated method stub
													primaryStage.setScene(scene2);
												}
											});

											Scene scene3 = new Scene(root3, 1550, 810);
											scene3.getStylesheets()
													.add(getClass().getResource("application.css").toExternalForm());
											primaryStage.setScene(scene3);
											primaryStage.show();

											Button a = new Button("+");
											a.setFont(Font.font(20));
											a.setTextFill(Color.BROWN);
											a.setStyle("-fx-background-color:  #FFFFF0; ");
											Button b = new Button("+");
											b.setFont(Font.font(20));
											b.setTextFill(Color.BROWN);
											b.setStyle("-fx-background-color:  #FFFFF0; ");
											Button c = new Button("+");
											c.setFont(Font.font(20));
											c.setTextFill(Color.BROWN);
											c.setStyle("-fx-background-color:  #FFFFF0; ");
											Button d = new Button("+");
											d.setFont(Font.font(20));
											d.setTextFill(Color.BROWN);
											d.setStyle("-fx-background-color:  #FFFFF0; ");
											int jj = 0;
											int r = 1;
											for (int i = 1; i <= 3; ++i) {
												if (i == 1) {
													root3.add(a, 1, r);
												}
												if (i == 2) {
													root3.add(b, 1, r);
												}
												if (i == 3) {
													root3.add(c, 1, r);
												}

												r = r + 2;
											}

											for (int j = 0; j < meal_ing.size(); ++j) {
												if (meal_ing.get(j).mealType.compareToIgnoreCase("Salad") == 0) {

													Label l1 = new Label(meal_ing.get(j).mealname);
													l1.setFont(Font.font(45));
													l1.setTextFill(Color.BLANCHEDALMOND);
													Label l2 = new Label(String.valueOf(meal_ing.get(j).price));
													l2.setFont(Font.font(25));
													l2.setTextFill(Color.BLANCHEDALMOND);
													Label l3 = new Label(meal_ing.get(j).getIng());
													l3.setFont(Font.font(20));
													l3.setTextFill(Color.BLANCHEDALMOND);
													map2.put(meal_ing.get(j).mealname, meal_ing.get(j).id_meal);
													map.put(meal_ing.get(j).mealname, meal_ing.get(j).price);
													root3.add(l1, 0, jj);
													root3.add(l2, 1, jj);

													jj++;
													root3.add(l3, 0, jj);

													jj++;

												}

											}

											root3.add(h, 0, jj + 2);
											a.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Ceaser Salad") == true) {
														int id = map2.get("Ceaser Salad");
														Double price = map.get("Ceaser Salad");
														System.out.println(price);
														System.out.print(id);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});
											b.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Greek Salad") == true) {
														int id = map2.get("Greek Salad");
														Double price = map.get("Greek Salad");
														System.out.print(id);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});
											c.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Chicken Salad") == true) {
														int id = map2.get("Chicken Salad");
														double price = map.get("Chicken Salad");
														System.out.print(id);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});

										}
									});

									Button fries = new Button(null, r4);
									fries.setShape(circle);
									fries.setOnAction(new EventHandler<ActionEvent>() {

										@Override
										public void handle(ActionEvent arg0) {
											GridPane root3 = new GridPane();
											root3.setAlignment(Pos.TOP_LEFT);
											root3.setPadding(new Insets(10, 10, 10, 10));

											var image = new Image("friess.jpg", true);
											var bgImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
													BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
													new BackgroundSize(1.0, 1.0, true, true, false, false));
											root3.setBackground(new Background(bgImage));

											HBox h = new HBox();

											Button back = new Button("Back");

											back.setTextFill(Color.BROWN);
											back.setStyle("-fx-background-color:  #FFFFF0; ");
											back.setFont(Font.font(25));
											back.setFont(Font.font(null, FontWeight.BOLD, bb2.getFont().getSize()));

											Button add = new Button("Add");

											add.setTextFill(Color.BROWN);
											add.setStyle("-fx-background-color:  #FFFFF0; ");
											add.setFont(Font.font(25));
											add.setFont(Font.font(null, FontWeight.BOLD, bb2.getFont().getSize()));
											h.getChildren().addAll(add, back);
											h.setSpacing(10);
											h.setAlignment(Pos.CENTER);

											/// root3.setStyle("-fx-background-image: url('BB2.jpg')");
											back.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													// TODO Auto-generated method stub
													primaryStage.setScene(scene2);
												}
											});

											Scene scene3 = new Scene(root3, 1550, 810);
											scene3.getStylesheets()
													.add(getClass().getResource("application.css").toExternalForm());
											primaryStage.setScene(scene3);
											primaryStage.show();

											Button a = new Button("+");
											a.setFont(Font.font(20));
											a.setTextFill(Color.BROWN);
											a.setStyle("-fx-background-color:  #FFFFF0; ");
											Button b = new Button("+");
											b.setFont(Font.font(20));
											b.setTextFill(Color.BROWN);
											b.setStyle("-fx-background-color:  #FFFFF0; ");
											Button c = new Button("+");
											c.setFont(Font.font(20));
											c.setFont(Font.font(20));
											c.setTextFill(Color.BROWN);
											c.setStyle("-fx-background-color:  #FFFFF0; ");
											Button d = new Button("+");
											d.setFont(Font.font(20));
											d.setTextFill(Color.BROWN);
											d.setStyle("-fx-background-color:  #FFFFF0; ");

											Button e = new Button("+");
											e.setFont(Font.font(20));
											e.setTextFill(Color.BROWN);
											e.setStyle("-fx-background-color:  #FFFFF0; ");

											int jj = 0;
											int r = 1;
											for (int i = 1; i <= 5; ++i) {
												if (i == 1) {
													root3.add(a, 1, r);
												}
												if (i == 2) {
													root3.add(b, 1, r);
												}
												if (i == 3) {
													root3.add(c, 1, r);
												}
												if (i == 4) {
													root3.add(d, 1, r);
												}
												if (i == 5) {
													root3.add(e, 1, r);
												}
												r = r + 2;
											}

											for (int j = 0; j < m.size(); ++j) {
												if (m.get(j).m_Type.compareToIgnoreCase("Fries") == 0) {
													System.out.println(m.get(j).m_Name);

													Label l1 = new Label(m.get(j).m_Name);
													l1.setFont(Font.font(40));
													l1.setTextFill(Color.BLANCHEDALMOND);
													Label l2 = new Label(String.valueOf(m.get(j).price));
													l2.setFont(Font.font(25));
													l2.setTextFill(Color.BLANCHEDALMOND);

													map2.put(m.get(j).m_Name, m.get(j).ID);
													map.put(m.get(j).m_Name, m.get(j).price);
													root3.add(l1, 0, jj);
													root3.add(l2, 1, jj);

													jj = jj + 2;

												}

											}

											root3.add(h, 0, jj + 2);
											a.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Potato balls") == true) {
														int id = map2.get("Potato balls");
														double price = map.get("Potato balls");
														System.out.print(id);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});
											b.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Potato widges") == true) {
														int id = map2.get("Potato widges");
														double price = map.get("Potato widges");

														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});
											c.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Curly fries") == true) {
														int id = map2.get("Curly fries");
														double price = map.get("Curly fries");
														System.out.print("curlyy friesssssss");
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});
											d.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Frensh fries") == true) {
														int id = map2.get("Frensh fries");
														double price = map.get("Frensh fries");
														System.out.println(" wlkkkkkk French fries");
														System.out.println(price);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});
											e.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Sweet potato") == true) {
														int id = map2.get("Sweet potato");
														double price = map.get("Sweet potato");
														System.out.println("sweet potatooo");
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});

										}

									});

									Button drinks = new Button(null, r5);
									drinks.setShape(circle);
									drinks.setOnAction(new EventHandler<ActionEvent>() {

										@Override
										public void handle(ActionEvent arg0) {
											// TODO Auto-generated method stub

											GridPane root3 = new GridPane();
											root3.setAlignment(Pos.TOP_LEFT);
											root3.setPadding(new Insets(100, 10, 10, 20));

											var image = new Image("cok.jpg", true);
											var bgImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
													BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
													new BackgroundSize(1.0, 1.0, true, true, false, false));
											root3.setBackground(new Background(bgImage));

											HBox h = new HBox();

											Button back = new Button("Back");
											back.setTextFill(Color.BROWN);
											back.setStyle("-fx-background-color:  #FFFFF0; ");
											back.setFont(Font.font(25));
											back.setFont(Font.font(null, FontWeight.BOLD, back.getFont().getSize()));

											Button add = new Button("Add");
											add.setTextFill(Color.BROWN);
											add.setStyle("-fx-background-color:  #FFFFF0; ");
											add.setFont(Font.font(25));
											add.setFont(Font.font(null, FontWeight.BOLD, add.getFont().getSize()));
											h.getChildren().addAll(add, back);
											h.setSpacing(10);
											h.setAlignment(Pos.CENTER);

											/// root3.setStyle("-fx-background-image: url('BB2.jpg')");
											back.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													// TODO Auto-generated method stub
													primaryStage.setScene(scene2);
												}
											});

											Scene scene3 = new Scene(root3, 1550, 810);
											scene3.getStylesheets()
													.add(getClass().getResource("application.css").toExternalForm());
											primaryStage.setScene(scene3);
											primaryStage.show();

											Button a = new Button("+");
											a.setFont(Font.font(20));
											a.setTextFill(Color.BROWN);
											a.setStyle("-fx-background-color:  #FFFFF0; ");
											Button b = new Button("+");
											b.setFont(Font.font(20));
											b.setTextFill(Color.BROWN);
											b.setStyle("-fx-background-color:  #FFFFF0; ");
											Button c = new Button("+");
											c.setFont(Font.font(20));
											c.setTextFill(Color.BROWN);
											c.setStyle("-fx-background-color:  #FFFFF0; ");
											Button d = new Button("+");
											d.setFont(Font.font(20));
											d.setTextFill(Color.BROWN);
											d.setStyle("-fx-background-color:  #FFFFF0; ");

											int jj = 0;
											int r = 1;
											for (int i = 1; i <= 4; ++i) {
												if (i == 1) {
													root3.add(a, 1, r);
												}
												if (i == 2) {
													root3.add(b, 1, r);
												}
												if (i == 3) {
													root3.add(c, 1, r);
												}
												if (i == 4) {
													root3.add(d, 1, r);
												}

												r = r + 2;
											}

											for (int j = 0; j < m.size(); ++j) {
												if (m.get(j).m_Type.compareToIgnoreCase("Drinks") == 0) {

													Label l1 = new Label(m.get(j).m_Name);
													l1.setFont(Font.font(40));
													l1.setTextFill(Color.BLANCHEDALMOND);
													Label l2 = new Label(String.valueOf(m.get(j).price));
													l2.setFont(Font.font(25));
													l2.setTextFill(Color.BLANCHEDALMOND);

													map2.put(m.get(j).m_Name, m.get(j).ID);
													map.put(m.get(j).m_Name, m.get(j).price);
													root3.add(l1, 0, jj);
													root3.add(l2, 1, jj);

													jj = jj + 2;

												}

											}

											root3.add(h, 0, jj + 2);
											a.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("CocaCola") == true) {
														int id = map2.get("CocaCola");
														double price = map.get("CocaCola");
														System.out.print(id);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});

											b.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("7up") == true) {
														int id = map2.get("7up");
														Double price = map.get("7up");
														System.out.print(id);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});
											c.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Water") == true) {
														int id = map2.get("Water");
														double price = map.get("Water");
														System.out.print(id);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});

											d.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													if (map2.containsKey("Fanta") == true) {
														int id = map2.get("Fanta");
														double price = map.get("Fanta");
														System.out.print(id);
														Label quantity = new Label("Quantity");
														quantity.setFont(Font.font(25));
														quantity.setTextFill(Color.BLANCHEDALMOND);
														quantity.setFont(Font.font(null, FontWeight.BOLD,
																quantity.getFont().getSize()));
														root3.add(quantity, 6, 0);
														TextField q = new TextField();

														q.setMinSize(50, 5);
														root3.add(q, 6, 1);
														add.setOnAction(new EventHandler<ActionEvent>() {

															@Override
															public void handle(ActionEvent arg0) {
																Statement statement;
																try {
																	statement = con.createStatement();
																	PreparedStatement stmt = con.prepareStatement(
																			"insert into Meal_Cust (Cust_ID ,meal_ID,Quantity,price) values(?,?,?,?)");

																	stmt.setInt(1, customerid);
																	stmt.setInt(2, id);
																	System.out.print(q.getText());
																	stmt.setInt(3, Integer.parseInt(q.getText()));
																	stmt.setDouble(4,
																			Integer.parseInt(q.getText()) * price);
																	stmt.execute();
																} catch (SQLException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}

															}

														});

													}
												}

											});
										}
									});

									done.setOnAction(new EventHandler<ActionEvent>() {

										@Override
										public void handle(ActionEvent arg0) {
											System.out.println("ldld");
											GridPane g = new GridPane();
											g.setStyle("-fx-background-image: url('hun.jpg')");
											Scene scene2 = new Scene(g, 1550, 800);
											scene2.getStylesheets()
													.add(getClass().getResource("application.css").toExternalForm());
											primaryStage.setScene(scene2);
											primaryStage.show();
											Label id = new Label("Employee ID");
											id.setFont(Font.font(25));
											id.setTextFill(Color.BLANCHEDALMOND);
											id.setFont(Font.font(null, FontWeight.BOLD, id.getFont().getSize()));
											TextField idd = new TextField();
											idd.setMinSize(150, 40);
											g.add(id, 0, 0);
											g.add(idd, 1, 0);
											Label date = new Label("Date of Order");
											date.setTextFill(Color.BLANCHEDALMOND);
											date.setFont(Font.font(25));
											date.setFont(Font.font(null, FontWeight.BOLD, date.getFont().getSize()));
											TextField datee = new TextField();

											LocalDate localDate = LocalDate.now();

											// Format the date
											DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
											String formattedDate = localDate.format(formatter);
											datee.setText(formattedDate);
											datee.setMinSize(150, 40);
											g.add(date, 0, 1);
											g.add(datee, 1, 1);
											Button add = new Button("ADD");
											add.setTextFill(Color.BROWN);
											add.setStyle("-fx-background-color:  #FFFFF0; ");
											add.setFont(Font.font(25));
											add.setFont(Font.font(null, FontWeight.BOLD, bb2.getFont().getSize()));
											g.add(add, 0, 2);
											Button print = new Button("Print");
											print.setTextFill(Color.BROWN);
											print.setStyle("-fx-background-color:  #FFFFF0; ");
											print.setFont(Font.font(25));
											print.setFont(Font.font(null, FontWeight.BOLD, bb2.getFont().getSize()));
											g.add(print, 1, 2);
											Button back = new Button("Back");
											back.setTextFill(Color.BROWN);
											back.setStyle("-fx-background-color:  #FFFFF0; ");
											back.setFont(Font.font(25));
											back.setFont(Font.font(null, FontWeight.BOLD, bb2.getFont().getSize()));
											g.add(back, 0, 3);
											g.setVgap(10);
											g.setHgap(10);
											g.setAlignment(Pos.CENTER);

											add.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent arg0) {
													// TODO Auto-generated method stub
													Statement statement;
													try {
														statement = con.createStatement();
														orderid = orderid + 1;
														PreparedStatement stmt = con.prepareStatement(
																"insert into ordert (order_ID ,e_ID,cust_ID,order_date) values(?,?,?,?)");
														/*ord_cus.add(new OrderCustomer(
																Integer.parseInt(orderid),
																result.getString(2),
																Integer.parseInt(result.getString(3)), date,
																Integer.parseInt(result.getString(5)),
																Integer.parseInt(result.getString(6)),
																Integer.parseInt(result.getString(7)),
																Double.parseDouble(result.getString(8))));*/
														stmt.setInt(1, orderid);
														stmt.setInt(2, Integer.parseInt(idd.getText()));
														System.out.println("cusss" + customerid);
														stmt.setInt(3, customerid);
														String ss = datee.getText();
														// Date dateee=Date.stmt.setDate(4,dateee);
														DateTimeFormatter formatter = DateTimeFormatter
																.ofPattern("yyyy/MM/dd");
														LocalDate date = LocalDate.parse(ss, formatter);
														java.sql.Date sqlDate = java.sql.Date.valueOf(date);
														stmt.setDate(4, sqlDate);
														stmt.execute();

													} catch (SQLException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}

													String sql = " select e.ID ,e.ename,o.order_ID ,o.order_date,m.Cust_ID ,m.meal_ID,m.Quantity ,m.price  from Employee e,ordert o,Meal_Cust m  where e.ID=o.e_ID And o.cust_ID=m.Cust_ID ;";
													//System.out.println(sql);

													Statement stmt;

													try {
														stmt = con.createStatement();
														ResultSet result = stmt.executeQuery(sql);

														while (result.next()) {
															Date date = result.getDate(4);
															System.out.println("last:" + lastOrder);
															if (lastOrder < Integer.parseInt(result.getString(3))) {

																ord_cus.add(new OrderCustomer(
																		Integer.parseInt(result.getString(1)),
																		result.getString(2),
																		Integer.parseInt(result.getString(3)), date,
																		Integer.parseInt(result.getString(5)),
																		Integer.parseInt(result.getString(6)),
																		Integer.parseInt(result.getString(7)),
																		Double.parseDouble(result.getString(8))));
															}
														}
													} catch (SQLException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}

													back.setOnAction(new EventHandler<ActionEvent>() {

														@Override
														public void handle(ActionEvent arg0) {
															// TODO Auto-generated method stub
															primaryStage.setScene(scene2);
														}
													});
													print.setOnAction(new EventHandler<ActionEvent>() {

														@Override
														public void handle(ActionEvent arg0) {
															BorderPane root = new BorderPane();
															VBox v = new VBox();
															root.setStyle("-fx-background-image: url('hun.jpg')");
															Scene scene2 = new Scene(root, 1550, 800);
															scene2.getStylesheets().add(getClass()
																	.getResource("application.css").toExternalForm());
															primaryStage.setScene(scene2);
															primaryStage.show();
															TextArea text = new TextArea();
															text.setMinHeight(100);
															text.setPrefWidth(30);
															text.setFont(Font.font(25));
															v.setPadding(new Insets(100, 100, 100, 100));
															text.setFont(Font.font(null, FontWeight.BOLD,
																	text.getFont().getSize()));
															Button back = new Button("Back");
															back.setTextFill(Color.BROWN);
															back.setStyle("-fx-background-color:  #FFFFF0; ");
															back.setFont(Font.font(25));
															back.setFont(Font.font(null, FontWeight.BOLD,
																	bb2.getFont().getSize()));

															String x = "";
															String d = "";
															double sum = 0;
															for (int i = 0; i < ord_cus.size(); ++i) {
																if (ord_cus.get(i).cut_ID == customerid) {
																	d = String.valueOf(b);

																	sum = sum + ord_cus.get(i).price;
																	System.out.println(ord_cus.get(i).meal_ID);
																	if (map2.containsValue(ord_cus.get(i).meal_ID)) {
																		System.out.println(
																				getKey(map2, ord_cus.get(i).meal_ID));
																		x = x + "\n"
																				+ getKey(map2, ord_cus.get(i).meal_ID)
																				+ "                "
																				+ ord_cus.get(i).price;
																	}

																}
															}
															v.setAlignment(Pos.CENTER);
															System.out.print(x + d + " " + sum);

															text.setText(
																	"                                         Hungers Restaurent                                           "
																			+ "\n" + "Customer ID:" + customerid + "\n"
																			+ "Employee ID:" + idd.getText() + "\n"
																			+ "----------------------------------------------------------------------------------------------------------------------------------"
																			+ "\n" + x + "\n"
																			+ "----------------------------------------------------------------------------------------------------------------------------------"
																			+ "\n" + "TOTAL:" + sum + " " + "" + "" + ""
																			+ "" + "" + "" + "");

															v.getChildren().addAll(text, back);

															back.setOnAction(new EventHandler<ActionEvent>() {

																@Override
																public void handle(ActionEvent arg0) {
																	// TODO Auto-generated method stub
																	primaryStage.setScene(scene);
																}
															});

															root.setCenter(v);

														}

													});

												}

											});

										}

									});

									bb2.setOnAction(new EventHandler<ActionEvent>() {

										@Override
										public void handle(ActionEvent arg0) {
											// TODO Auto-generated method stub
											primaryStage.setScene(scene);
										}
									});

									HBox h = new HBox();
									h.setAlignment(Pos.CENTER);
									h.setSpacing(20);
									h.getChildren().addAll(Burger, App, salads, fries, drinks);

									rr.setCenter(h);
									h2.setAlignment(Pos.BOTTOM_CENTER);
									rr.setBottom(h2);

								} else {
									Label message = new Label("Please check the TextFields");
									message.setFont(Font.font(20));
									message.setFont(Font.font(null, FontWeight.BOLD, mobile.getFont().getSize()));
									message.setTextFill(Color.rgb(210, 39, 30));
									root2.add(message, 0, 7);
								}
							}
						});

					}
				});

			} catch (Exception e) {
				error.setContentText(e.getMessage());
				error.show();
			}
		}

	}

	boolean applyOnDataBase(String string) {

		try {
			Statement stmt = Main.con.createStatement();
			boolean rs = stmt.execute(string);
			return rs;
		} catch (Exception e) {

			error.setContentText(string);
			error.show();
		}
		return false;

	}

	ResultSet appyQueryOnDataBase(String string) {

		try {
			Statement stmt = Main.con.createStatement();
			ResultSet rs = stmt.executeQuery(string);
			return rs;
		} catch (Exception e) {
			error.setContentText(string);
			error.show();
		}

		return null;

	}

	private boolean connectDataBase() {

		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "Restaurent", "root", "haloos0599/");
			System.out.println("Connection established!");

			return true;
		} catch (SQLException ee) {
			ee.printStackTrace();
			error.setContentText("Can't connect to database");
			error.show();
			return false;
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

	public String getKey(HashMap<String, Integer> map, Integer value) {
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}
}
