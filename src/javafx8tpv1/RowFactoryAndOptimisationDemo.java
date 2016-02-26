package javafx;


import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TooltipBuilder;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.stage.Stage;
import javafx.util.Callback;

public class RowFactoryAndOptimisationDemo extends Application {

    StackPane root;

    @Override
    public void start(Stage stage) throws Exception {
        root = new StackPane();
        root.autosize();
        Scene scene = new Scene(root, Color.LINEN);

        stage.setTitle("Row Factory Demo");
        stage.setWidth(500);
        stage.setHeight(300);
        stage.setScene(scene);
        stage.show();

        configureTable();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    static int i = 0;
    static int k = 0;
    static int j = 0;

    @SuppressWarnings("unchecked")
    private void configureTable() {
        final ObservableList<RFDDomain> data = FXCollections.observableArrayList();
        int id =1;
        for (int i = 1; i <= 10000; i++) {
            data.add(new RFDDomain(id++,"First Row", "This is for check.", 1));
            data.add(new RFDDomain(id++,"Second Row", null, 2));
            data.add(new RFDDomain(id++,"Third Row", "This is for check.", 3));
            data.add(new RFDDomain(id++,"Fourth Row", "dil", 4));
        }

        TableView<RFDDomain> tableView = new TableView<RFDDomain>();
        tableView.getStyleClass().add("myTable");
        tableView.setItems(data);

        tableView.setRowFactory(new Callback<TableView<RFDDomain>, TableRow<RFDDomain>>() {
            @Override
            public TableRow<RFDDomain> call(TableView<RFDDomain> paramP) {
                return new TableRow<RFDDomain>() {
                    @Override
                    protected void updateItem(RFDDomain paramT, boolean paramBoolean) {
                                                String style = "-fx-background-color: linear-gradient(#007F0E 0%, #FFFFFF 90%, #eaeaea 90%);";
                        setStyle(style);

                                                super.updateItem(paramT, paramBoolean);
                    }
                };
            }
        });

        TableColumn<RFDDomain, Integer> column0 = new TableColumn<RFDDomain, Integer>("Id");
        column0.setCellValueFactory(new PropertyValueFactory<RFDDomain, Integer>("id"));

        TableColumn<RFDDomain, String> column1 = new TableColumn<RFDDomain, String>("Title");
        column1.setCellValueFactory(new PropertyValueFactory<RFDDomain, String>("name"));

        TableColumn<RFDDomain, String> column2 = new TableColumn<RFDDomain, String>("Description");
        column2.setCellValueFactory(new PropertyValueFactory<RFDDomain, String>("description"));

        TableColumn<RFDDomain, Number> column3 = new TableColumn<RFDDomain, Number>("Status");
        column3.setPrefWidth(55);
        column3.setCellValueFactory(new PropertyValueFactory<RFDDomain, Number>("status"));

        TableColumn<RFDDomain, String> column4 = new TableColumn<RFDDomain, String>("Action");
        column4.setCellValueFactory(new PropertyValueFactory<RFDDomain, String>("name"));

        tableView.getColumns().addAll(column0, column1, column2, column3, column4);
        this.root.getChildren().add(tableView);


    }

    /**
     * Domain Model for this demo.
     */
    public class RFDDomain {
        private SimpleIntegerProperty id = new SimpleIntegerProperty();
        private SimpleStringProperty name = new SimpleStringProperty();
        private SimpleStringProperty description = new SimpleStringProperty();
        private SimpleIntegerProperty status = new SimpleIntegerProperty();

        public RFDDomain(int id,String name, String desc, int status) {
            this.id.set(id);
            this.name.set(name);
            this.description.set(desc);
            this.status.set(status);
        }

        public int getId() {
            return id.get();
        }

        public SimpleIntegerProperty idProperty() {
            return id;
        }

        public String getDescription() {
            return description.get();
        }

        public SimpleStringProperty descriptionProperty() {
            return description;
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public int getStatus() {
            return status.get();
        }

        public SimpleIntegerProperty statusProperty() {
            return status;
        }
    }
}