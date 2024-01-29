import java.awt.*;
import java.awt.geom.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Spirograph extends Application {
    private TextField v1;
    private TextField v2;
    private TextField v3;
    private TextField v4;
    private Canvas canvas;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        canvas = new Canvas(1920, 1080);
       
        VBox mainBox = new VBox();
        HBox topBar = new HBox();
        mainBox.getChildren().add(topBar);
        mainBox.getChildren().add(new Group(canvas));
        
        topBar.getChildren().add(v1 = new TextField("300"));
        topBar.getChildren().add(v2 = new TextField("1"));
        topBar.getChildren().add(v3 = new TextField("300"));
        topBar.getChildren().add(v4 = new TextField("10"));

        v1.textProperty().addListener(e -> draw(new FXGraphics2D(canvas.getGraphicsContext2D())));
        v2.textProperty().addListener(e -> draw(new FXGraphics2D(canvas.getGraphicsContext2D())));
        v3.textProperty().addListener(e -> draw(new FXGraphics2D(canvas.getGraphicsContext2D())));
        v4.textProperty().addListener(e -> draw(new FXGraphics2D(canvas.getGraphicsContext2D())));
        
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(mainBox));
        primaryStage.setTitle("Spirograph");
        primaryStage.show();
    }
    
    
    public void draw(FXGraphics2D graphics) {
        //you can use Double.parseDouble(v1.getText()) to get a double value from the first textfield
        //feel free to add more textfields or other controls if needed, but beware that swing components might clash in naming

        // Start
        graphics.translate(this.canvas.getWidth()/2 ,this.canvas.getHeight()/2);
        graphics.scale(1, -1);
        // Grafiek
        // Waardes: 1: a, 2: b, 3: c, 4: d
        double x1 = Double.parseDouble(v1.getText()) * Math.cos(Double.parseDouble(v2.getText())) + Double.parseDouble(v3.getText()) * Math.cos(Double.parseDouble(v4.getText()));
        double y1 = Double.parseDouble(v1.getText()) * Math.sin(Double.parseDouble(v2.getText())) + Double.parseDouble(v3.getText()) * Math.sin(Double.parseDouble(v4.getText()));
        for (int i = 0; i < 1000; i++){
            double x2 = Double.parseDouble(v1.getText()) * Math.cos(Double.parseDouble(v2.getText()) * i) + Double.parseDouble(v3.getText()) * Math.cos(Double.parseDouble(v4.getText()) * i);
            double y2 = Double.parseDouble(v1.getText()) * Math.sin(Double.parseDouble(v2.getText()) * i) + Double.parseDouble(v3.getText()) * Math.sin(Double.parseDouble(v4.getText()) * i);

            graphics.setColor(Color.getHSBColor(200, 1, 1));
            graphics.drawLine((int) x1, (int) y1, (int) x2, (int) y2);

            x1 = x2;
            y1 = y2;
        }

//        // 1: Length, 2: 2nd Length, 3: , 4: Radius
//        double text1 = Double.parseDouble(v1.getText());
//        double text2 = Double.parseDouble(v2.getText());
//        double text3 = Double.parseDouble(v3.getText());
//        double text4 = Double.parseDouble(v4.getText());
//
//        double radiusBinnen = 2*text4;
//        double radiusBuiten = text4;
//        for (int i = 0; i < text1; i++){
//            double hoek = (float) (i/Math.PI);
//            double x1 = radiusBinnen * Math.cos(hoek);
//            double y1 = radiusBinnen * Math.sin(hoek);
//            double x2 = radiusBuiten * Math.cos(hoek);
//            double y2 = radiusBuiten * Math.sin(hoek);
//
//            graphics.setColor(Color.getHSBColor(5, 1, 1));
//            graphics.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
//            for (int j = 0; j < text2; j++){
//                double hoek2 = (float) (i/Math.PI);
//                double x3 = radiusBinnen * Math.cos(hoek2);
//                double y3 = radiusBinnen * Math.sin(hoek2);
//                double x4 = radiusBuiten * Math.cos(hoek2);
//                double y4 = radiusBuiten * Math.sin(hoek2);
//
//                graphics.setColor(Color.getHSBColor(200, 1, 1));
//                graphics.drawLine((int) x3, (int) y3, (int) x4, (int) y4);
//            }
//        }

    }
    
    
    
    public static void main(String[] args) {
        launch(Spirograph.class);
    }

}
