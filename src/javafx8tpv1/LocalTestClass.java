package javafx8tpv1;
import java.util.ArrayList;
import javafx.application.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;

public class LocalTestClass extends Application {
    public static final String NAME_COLUMN_NAME  = "Name";
    public static final String VALUE_COLUMN_NAME = "Value";

    final TableView<Pair<String, Object>> table = new TableView<>();

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // model data
        ObservableList<Pair<String, Object>> data = FXCollections.observableArrayList(
                pair("Song", "Bach Cello Suite 2"),
                pair("Image", new Image("http://upload.wikimedia.org/wikipedia/en/9/99/Bach_Seal.jpg")),
                pair("Rating", 4),
                pair("Classic", true),
                pair("Song Data", new byte[]{})
        );

        table.getItems().setAll(data);
        table.setPrefHeight(275);

        // table definition
        TableColumn<Pair<String, Object>, String> nameColumn = new TableColumn<>(NAME_COLUMN_NAME);
        nameColumn.setPrefWidth(100);
        TableColumn<Pair<String, Object>, Object> valueColumn = new TableColumn<>(VALUE_COLUMN_NAME);
        valueColumn.setSortable(false);
        valueColumn.setPrefWidth(150);

        nameColumn.setCellValueFactory(new PairKeyFactory());
        valueColumn.setCellValueFactory(new PairValueFactory());

        table.getColumns().setAll(nameColumn, valueColumn);
        valueColumn.setCellFactory(new Callback<TableColumn<Pair<String, Object>, Object>, TableCell<Pair<String, Object>, Object>>() {
            @Override
            public TableCell<Pair<String, Object>, Object> call(TableColumn<Pair<String, Object>, Object> column) {
                return new PairValueCell();
            }
        });

        // layout the scene.
        final StackPane layout = new StackPane();
        ComboBox combo = new ComboBox();
        combo.getItems().add("TARJETA NARANJA");
        combo.getItems().add("TARJETA VISA");
        combo.getItems().add("TARJETA NEVADA");        
        combo.getItems().add("EFECTIVO");        
        combo.getItems().add("VOUCHER");        
        layout.getChildren().add(combo);
        
        //layout.getChildren().setAll(table);
        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.show();
    }

    private Pair<String, Object> pair(String name, Object value) {
        return new Pair<>(name, value);
    }
}

class PairKeyFactory implements Callback<TableColumn.CellDataFeatures<Pair<String, Object>, String>, ObservableValue<String>> {
    @Override
    public ObservableValue<String> call(TableColumn.CellDataFeatures<Pair<String, Object>, String> data) {
        return new ReadOnlyObjectWrapper<>(data.getValue().getKey());
    }
}

class PairValueFactory implements Callback<TableColumn.CellDataFeatures<Pair<String, Object>, Object>, ObservableValue<Object>> {
    @SuppressWarnings("unchecked")
    @Override
    public ObservableValue<Object> call(TableColumn.CellDataFeatures<Pair<String, Object>, Object> data) {
        Object value = data.getValue().getValue();
        return (value instanceof ObservableValue)
                ? (ObservableValue) value
                : new ReadOnlyObjectWrapper<>(value);
    }
}

class PairValueCell extends TableCell<Pair<String, Object>, Object> {
    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null) {
            if (item instanceof String) {
                setText((String) item);
                setGraphic(null);
            } else if (item instanceof Integer) {
                setText(Integer.toString((Integer) item));
                setGraphic(null);
            } else if (item instanceof Boolean) {
                CheckBox checkBox = new CheckBox();
                checkBox.setSelected((boolean) item);
                setGraphic(checkBox);
            } else if (item instanceof Image) {
                setText(null);
                ImageView imageView = new ImageView((Image) item);
                imageView.setFitWidth(100);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                setGraphic(imageView);
            } else {
                setText("N/A");
                setGraphic(null);
            }
        } else {
            setText(null);
            setGraphic(null);
        }
    }
}