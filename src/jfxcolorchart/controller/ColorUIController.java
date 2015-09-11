/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxcolorchart.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import jfxcolorchart.animation.MyAnimation;
import jfxcolorchart.model.Color;
import jfxcolorchart.utils.Settings;

/**
 * FXML Controller class
 *
 * @author Karanja
 */
public class ColorUIController implements Initializable {

    @FXML
    private BorderPane wrapper;
    @FXML
    private TableView<Color> tvColors;
    @FXML
    private TableColumn<Color, Integer> colNo;
    @FXML
    private TableColumn<Color, String> colColor;
    @FXML
    private TableColumn<Color, Integer> colR;
    @FXML
    private TableColumn<Color, Integer> colG;
    @FXML
    private TableColumn<Color, Integer> colB;
    @FXML
    private TableColumn<Color, String> colHex;
    @FXML
    private TextField txtSearch;
    @FXML
    private TextField tfR;
    @FXML
    private TextField tfG;
    @FXML
    private TextField tfB;
    @FXML
    private TextField tfHex;
    @FXML
    private ProgressBar pbLoading;
    @FXML
    private Label lblStatus;
    @FXML
    private Label lblRenderTime;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private Button btnMobile;
    @FXML
    private Button btnPrefs;
    @FXML
    private Button btnAbout;
    @FXML
    private Pane paneShadePreview;
    @FXML
    private Slider sliderShade;
    @FXML
    private Slider sliderLighter;
    
    private static final double BLUR_AMOUNT = 10;
    private static final Effect frostEffect = new BoxBlur(BLUR_AMOUNT, BLUR_AMOUNT, 6);
    private ObservableList<Color> colorPalette;
    private long renderStart = 0;
    private long renderStop = 0;
    private Color c;
    private final MyAnimation animation = new MyAnimation();
    private final Settings pref = new Settings();
    private String selectedColor = "#FFFFFF";

    /**
     * Display the color of corresponding Hexadecimal value on the color column
     * using a custom cell renderer
     */
    private void displayColor() {
        colColor.setCellFactory(column -> {
            return new TableCell<Color, String>() {
                @Override
                protected void updateItem(String hex, boolean empty) {
                    super.updateItem(hex, empty);
                    setStyle("-fx-background-color: " + hex + ";");
                }
            };
        });
    }

    /**
     * Allow sorting and filtering on the TableView using the Hexadecimal color
     * value from the search field, The Hex color value should be a standard
     * Hexadecimal color value e.g. #FFFFFF
     */
    private void initSortAndFilterColors() {
        colHex.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getHex());
        });
        FilteredList<Color> filteredData = new FilteredList<>(colorPalette, p -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return person.getHex().toLowerCase().contains(lowerCaseFilter);
            });
        });
        SortedList<Color> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tvColors.comparatorProperty());
        tvColors.setItems(sortedData);
    }

    /**
     * The ColorBot class is a background Service that loops trough 0 - 255 to
     * generate a combination of RED GREEN and BLUE values of the color pallete.
     * When the full scale is provided i.e. 255, it will generate the full color
     * pallete. ie 16 million colors. [NOTE 1] The algorithm uses n(c) = n^3,
     * 255<n>0:- Where n(c) = number of colors generated and n = number of
     * iterations. E.g if n = 5, the the n(5) = 5^3 = 125 colors. [NOTE 2] The
     * algorithm uses Big O notation of O(2N), which doubles after every
     * iteration, meaning the execution time (Speed+Memory) will increase
     * dramatically as n becomes large. Therfore its advised to use as small
     * value of n as possible. The recommended size of n<=70.
     */
    private class ColorBot extends Service<Integer> {

        private final ObservableList<Color> colorPalette;
        private final TableView<Color> tvColors;

        public ColorBot(ObservableList<Color> colorPalette, TableView<Color> tvColors) {
            this.colorPalette = colorPalette;
            this.tvColors = tvColors;
        }

        @Override
        protected Task<Integer> createTask() {
            return new Task<Integer>() {

                @Override
                protected Integer call() throws Exception {
                    int iterations = pref.getIterations();
                    System.out.println("iterations:: " + iterations);
                    int workDone = 0;
                    int totalWork = (iterations * iterations * iterations);
                    String info[][][] = new String[iterations][iterations][iterations];
                    for (String[][] info1 : info) {
                        for (String[] info11 : info1) {
                            for (String info111 : info11) {
                                c = new Color();
                                int randomR = (int) (Math.random() * 255);
                                int randomG = (int) (Math.random() * 255);
                                int randomB = (int) (Math.random() * 255);
                                c.setR(randomR);
                                c.setG(randomG);
                                c.setB(randomB);
                                c.setNo(workDone + 1);
                                String hex = String.format("#%02X%02X%02X", randomR, randomG, randomB);
                                c.setHex(hex);
                                c.setColColor(hex);
                                colorPalette.add(c);
                                workDone++;
                                tvColors.setItems(colorPalette);
                                updateProgress(workDone, totalWork);
                                updateMessage("Generated " + workDone + " Colors / Target " + totalWork);
                            }
                        }
                    }

                    return workDone;
                }
            };

        }
    ;

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AnchorPane holder = new AnchorPane();
        tvColors.setEffect(frostEffect);
        //wrapper.setLeft(null);
        //wrapper.setLeft(holder);
        tvColors.setEditable(true);
        colHex.setCellFactory(TextFieldTableCell.forTableColumn());
        colR.setSortable(false);
        colG.setSortable(false);
        colB.setSortable(false);
        colHex.setSortable(false);
        colNo.setSortable(false);
        colColor.setSortable(false);
        lblRenderTime.setOpacity(0);
        colorPalette = FXCollections.observableArrayList();
        colColor.setCellValueFactory(new PropertyValueFactory("colColor"));
        colNo.setCellValueFactory(new PropertyValueFactory("No"));
        colR.setCellValueFactory(new PropertyValueFactory("R"));
        colG.setCellValueFactory(new PropertyValueFactory("G"));
        colB.setCellValueFactory(new PropertyValueFactory("B"));
        colHex.setCellValueFactory(new PropertyValueFactory("hex"));
        ColorBot cb = new ColorBot(colorPalette, tvColors);

        cb.setOnSucceeded((WorkerStateEvent event) -> {
            lblStatus.setText(event.getSource().getValue() + " Colors Generated");
        });
        cb.setOnSucceeded((WorkerStateEvent e) -> {
            finalizeGeneration();

        });
        renderStart = System.currentTimeMillis();
        cb.start();
        lblStatus.textProperty().bind(cb.messageProperty());
        pbLoading.progressProperty().bind(cb.progressProperty());
        pbLoading.setOpacity(1.0);

//        btnPrefs.setOnAction((ActionEvent event) -> {
//            try {
//                AnchorPane preferences = FXMLLoader.load(getClass().getResource("/jfxcolorchart/view/Settings.fxml"));
//                wrapper.setCenter(null);
//                wrapper.setCenter(preferences);
//            } catch (IOException ex) {
//                System.out.println("Error Loading: " + ex.getMessage());
//            }
//        });
//        btnMobile.setOnAction((ActionEvent event) -> {
//            wrapper.setCenter(null);
//            wrapper.setCenter(mainView);
//        });
//        btnAbout.setOnAction((ActionEvent event) -> {
//            try {
//                AnchorPane about = FXMLLoader.load(getClass().getResource("/jfxcolorchart/view/About.fxml"));
//                wrapper.setCenter(null);
//                wrapper.setCenter(about);
//            } catch (IOException ex) {
//                System.out.println("Error Loading: " + ex.getMessage());
//            }
//        });
        tvColors.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedColor = obs.getValue().getHex();
            paneShadePreview.setStyle("-fx-background-color:" + selectedColor + ";");
        });
        sliderShade.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            System.out.println(new_val.doubleValue());
            double factor = new_val.doubleValue();
            int[] rgb = getRGB(selectedColor);
            int r = rgb[0];
            int g = rgb[1];
            int b = rgb[2];

            int rs = (int) ((int) r * factor);
            int gs = (int) ((int) g * factor);
            int bs = (int) ((int) b * factor);

            String hex = String.format("#%02X%02X%02X", rs, gs, bs);

            paneShadePreview.setStyle("-fx-background-color:" + hex + ";");

            displaySummary(rs, gs, bs, hex);

        });
        sliderLighter.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            System.out.println(new_val.doubleValue());
            double factor = new_val.doubleValue();
            int[] rgb = getRGB(selectedColor);
            int r = rgb[0];
            int g = rgb[1];
            int b = rgb[2];

            int rl = (int) ((int) r + ((255 - (int) r) * factor));
            int gl = (int) ((int) g + ((255 - (int) g) * factor));
            int bl = (int) ((int) b + ((255 - (int) b) * factor));

            String hex = String.format("#%02X%02X%02X", rl, gl, bl);

            paneShadePreview.setStyle("-fx-background-color:" + hex + ";");

            displaySummary(rl, gl, bl, hex);

        });
    }

    private void displaySummary(int rs, int gs, int bs, String hex) {
        tfR.setText(String.valueOf(rs));
        tfG.setText(String.valueOf(gs));
        tfB.setText(String.valueOf(bs));
        tfHex.setText(hex);
    }

    /**
     *
     * @param hex e.g. "#FFFFFF"
     * @return
     */
    private int[] getRGB(String hex) {
        String rgb = hex.substring(1);
        final int[] ret = new int[3];
        for (int i = 0; i < 3; i++) {
            ret[i] = Integer.parseInt(rgb.substring(i * 2, i * 2 + 2), 16);
        }
        return ret;
    }

    private void finalizeGeneration() {
        lblRenderTime.setOpacity(1.0);
        renderStop = System.currentTimeMillis();
        lblRenderTime.setText("Color Rendering Done in " + (renderStop - renderStart) + " ms");
        animation.fadeOut(lblRenderTime, 2000).play();
        animation.fadeIn(pbLoading, 10000).play();
        displayColor();
        initSortAndFilterColors();
        tvColors.setEffect(null);
        animation.fadeIn(tvColors, 1000).play();
        animation.fadeOut(tvColors, 4000).play();
    }

}
