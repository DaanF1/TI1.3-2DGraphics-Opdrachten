//import java.awt.*;
import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Rainbow extends Application {
    private ResizableCanvas canvas;
    private Font font;
    @Override
    public void start(Stage stage) throws Exception
    {
        // Fonts:
        this.font = new Font("font1", Font.PLAIN, 72);

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        stage.setScene(new Scene(mainPane));
        stage.setTitle("Rainbow");
        stage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        AffineTransform aff = new AffineTransform();
        graphics.setTransform(aff);
        graphics.setBackground(Color.white);
        graphics.translate((int) canvas.getWidth()/2, (int) canvas.getHeight()/2);
        graphics.scale(1, 1);
        graphics.clearRect(-(int) canvas.getWidth()/2, -(int) canvas.getHeight()/2, (int) canvas.getWidth(), (int) canvas.getHeight());

        String tekst = "regenboog";
        double times = 60;
        double newTimes = -10;
        double fix = 0;
        for (int i = 0; i < tekst.length(); i++){
            // Buitenrand uitlijnen
            Shape shape = this.font.createGlyphVector(graphics.getFontRenderContext(), tekst.substring(i, i+1)).getOutline();

            // Rotatie
            Shape newShape = AffineTransform.getRotateInstance(4.7+(i*0.4)).createTransformedShape(shape);

            // Start tekenen bij begin
            graphics.setColor(Color.BLACK);
            graphics.draw(AffineTransform.getTranslateInstance(-150+(i*40), fix-(i*times)).createTransformedShape(newShape));
            graphics.setColor(Color.getHSBColor(i/8.0f, 1, 1));
            graphics.fill(AffineTransform.getTranslateInstance(-150+(i*40), fix-(i*times)).createTransformedShape(newShape));

            // Translaties berekenen
            if (i < 4){
                times = times*0.85;
            } else if (i == 4){
                times = newTimes;
                fix = -175;
            } else if (i > 4){
                times = times*1.2;
            }


            // Regenboog (maar dan zelf getekend)):
//            if (i == 0){
//                graphics.draw(AffineTransform.getTranslateInstance(-120+(10+(i*35)), 0).createTransformedShape(newShape));
//                graphics.setColor(Color.getHSBColor(i/8.0f, 1, 1));
//                graphics.fill(AffineTransform.getTranslateInstance(-120+(10+(i*35)), 0).createTransformedShape(newShape));
//            } else if (i == 1){
//                graphics.draw(AffineTransform.getTranslateInstance(-120+(10+(i*35)), -45).createTransformedShape(newShape));
//                graphics.setColor(Color.getHSBColor(i/8.0f, 1, 1));
//                graphics.fill(AffineTransform.getTranslateInstance(-120+(10+(i*35)), -45).createTransformedShape(newShape));
//            } else if (i == 2){
//                graphics.draw(AffineTransform.getTranslateInstance(-120+(10+(i*35)), -85).createTransformedShape(newShape));
//                graphics.setColor(Color.getHSBColor(i/8.0f, 1, 1));
//                graphics.fill(AffineTransform.getTranslateInstance(-120+(10+(i*35)), -85).createTransformedShape(newShape));
//            } else if (i == 3){
//                graphics.draw(AffineTransform.getTranslateInstance(-120+(10+(i*35)), -110).createTransformedShape(newShape));
//                graphics.setColor(Color.getHSBColor(i/8.0f, 1, 1));
//                graphics.fill(AffineTransform.getTranslateInstance(-120+(10+(i*35)), -110).createTransformedShape(newShape));
//            } else if (i == 4){
//                graphics.draw(AffineTransform.getTranslateInstance(-120+(10+(i*35)), -120).createTransformedShape(newShape));
//                graphics.setColor(Color.getHSBColor(i/8.0f, 1, 1));
//                graphics.fill(AffineTransform.getTranslateInstance(-120+(10+(i*35)), -120).createTransformedShape(newShape));
//            } else if (i == 5){
//                graphics.draw(AffineTransform.getTranslateInstance(-120+(10+(i*35)), -110).createTransformedShape(newShape));
//                graphics.setColor(Color.getHSBColor(i/8.0f, 1, 1));
//                graphics.fill(AffineTransform.getTranslateInstance(-120+(10+(i*35)), -110).createTransformedShape(newShape));
//            } else if (i == 6){
//                graphics.draw(AffineTransform.getTranslateInstance(-120+(10+(i*35)), -90).createTransformedShape(newShape));
//                graphics.setColor(Color.getHSBColor(i/8.0f, 1, 1));
//                graphics.fill(AffineTransform.getTranslateInstance(-120+(10+(i*35)), -90).createTransformedShape(newShape));
//            } else if (i == 7){
//                graphics.draw(AffineTransform.getTranslateInstance(-120+(10+(i*35)), -55).createTransformedShape(newShape));
//                graphics.setColor(Color.getHSBColor(i/8.0f, 1, 1));
//                graphics.fill(AffineTransform.getTranslateInstance(-120+(10+(i*35)), -55).createTransformedShape(newShape));
//            } else if (i == 8){
//                graphics.draw(AffineTransform.getTranslateInstance(-120+(10+(i*35)), -20).createTransformedShape(newShape));
//                graphics.setColor(Color.getHSBColor(i/8.0f, 1, 1));
//                graphics.fill(AffineTransform.getTranslateInstance(-120+(10+(i*35)), -20).createTransformedShape(newShape));
//            }
        }
    }


    public static void main(String[] args)
    {
        launch(Rainbow.class);
    }

}
