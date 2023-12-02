package com.example.paint;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.IOException;

public class HelloController {
    @FXML Canvas lienzo, liencito;
    @FXML ColorPicker comboColor;
    @FXML ComboBox comboCuadricula,cbTamañoLienzo;
    @FXML Slider slideTamaño, slideCuadricula;
    @FXML ImageView imagenReferencia;
    GraphicsContext context;
    String tipoDibujo="",tipoForma="";
    Color color =Color.WHITESMOKE;
    double tamaño=5;
    boolean primerClick=true;
    double coorFormasXUno=0, coorFormasYUno=0,coorFormasXDos=0, coorFormasYDos=0;
    int rotar=0;

    @FXML protected void initialize(){
        context=lienzo.getGraphicsContext2D();
        context.setFill(color);
        //context.fillRect(0,0,800,800);
        liencito();
        cbTamañoLienzo.getItems().addAll("Tamaño: 200x200","Tamaño: 400x400","Tamaño: 600x600","Tamaño: 800x800","Tamaño: 1000x1000");
        comboCuadricula.getItems().addAll("Color Solido","Cuadricula","Chess","Estrella","Estrella pro","Circulos");
        slideCuadricula.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
            sliderCuadricula((number.intValue()));
            }
        });
        comboCuadricula.getSelectionModel().selectFirst();
        //context.setFont(Font.font("Arial", FontWeight.LIGHT,32));
    }
    public void cambiosComboCuadricula(){
        sliderCuadricula(slideCuadricula.getValue());
    }
    public void cambiosTamañoLienzo(){///////////////////////////////////////////////////////////////////TamañoLienzo
        Image imagenTemp=lienzo.snapshot(null,null);
        if(cbTamañoLienzo.getSelectionModel().getSelectedIndex()==0){
            lienzo.setWidth(200);
            lienzo.setHeight(200);
        }else if(cbTamañoLienzo.getSelectionModel().getSelectedIndex()==1){
            lienzo.setWidth(400);
            lienzo.setHeight(400);
        }else if(cbTamañoLienzo.getSelectionModel().getSelectedIndex()==2){
            lienzo.setWidth(600);
            lienzo.setHeight(600);
        }else if(cbTamañoLienzo.getSelectionModel().getSelectedIndex()==3){
            lienzo.setWidth(800);
            lienzo.setHeight(800);
        }else if(cbTamañoLienzo.getSelectionModel().getSelectedIndex()==4){
            lienzo.setWidth(1000);
            lienzo.setHeight(1000);
        }
        context.drawImage(imagenTemp,0,0,lienzo.getWidth(),lienzo.getHeight());
    }////////////////////////////////////////////////////////////////////////////TamañoLienzo

    public void sliderCuadricula(double valor){////////////////////////////////////////////////////Fondos
        context.setFill(Color.BLACK);
        context.fillRect(0,0, lienzo.getWidth(), lienzo.getHeight());
        context.setFill(color);
        context.setStroke(color);

        if(valor%2==0){
            System.out.println("Par");
            System.out.println(valor);
            valor++;

        }else{
            System.out.println("impar");
            System.out.println(valor);
        }

        if(comboCuadricula.getSelectionModel().getSelectedIndex()==0){
            context.fillRect(0,0, lienzo.getWidth(), lienzo.getHeight());

        }
        else if(comboCuadricula.getSelectionModel().getSelectedIndex()==1){//Cuadricula
            double divisines=valor;
            double incrementoX=lienzo.getWidth()/divisines;
            double incrementoY=lienzo.getHeight()/divisines;
            context.strokeLine(0,0, lienzo.getWidth(), 0);
            context.strokeLine(0,0, 0, lienzo.getHeight());

            for (int i=1;i<divisines+1;i++){
                ///(x1,y1, x2,y2)
                context.strokeLine(0,incrementoY*i,lienzo.getWidth(),incrementoY*i);
                context.strokeLine(incrementoX*i,0,incrementoX*i, lienzo.getHeight());
            }
        }else if(comboCuadricula.getSelectionModel().getSelectedIndex()==2){ ///Chess
            double divX=lienzo.getWidth()/valor;
            double divY=lienzo.getHeight()/valor;

            boolean bandera=true;
            for (int y=0;y<valor;y++){
                for (int x=0;x<valor;x++){
                    if (bandera){
                        context.setFill(color);
                    }else{
                        context.setFill(Color.WHITE);
                    }
                    bandera=!bandera;
                    context.fillRect(x*divX,y*divY,divX,divY);
                }
            }
        }//chess
        else if(comboCuadricula.getSelectionModel().getSelectedIndex()==3){//Estrella
           double mitadAncho=lienzo.getWidth()/2;
           double mitadAlto=lienzo.getHeight()/2;
           context.strokeLine(mitadAncho,0,mitadAncho, lienzo.getHeight());//vertical
           context.strokeLine(0,mitadAlto, lienzo.getWidth(), mitadAlto);//horizontal
           double divisiones = mitadAncho/valor;
           for(int i=0;i<valor;i++){
               context.strokeLine(mitadAncho, i*divisiones,mitadAncho+(divisiones*i),mitadAlto);//Arriba Derecha
               context.strokeLine(i*divisiones,mitadAncho,mitadAlto,mitadAncho+(divisiones*i));//Abajo Izquierda
               context.strokeLine(mitadAncho, i*divisiones,mitadAncho-(divisiones*i),mitadAlto);//Arriba Izquierda
               context.strokeLine(mitadAncho,mitadAlto+(i*divisiones), lienzo.getWidth()-(i*divisiones), mitadAlto);//Abajo Derecha
               context.strokeLine(0,1,3,4);
           }

        }else if(comboCuadricula.getSelectionModel().getSelectedIndex()==4){//Estrella pro
            double mitadAncho=lienzo.getWidth()/2;
            double mitadAlto=lienzo.getHeight()/2;
            context.strokeLine(mitadAncho,0,mitadAncho, lienzo.getHeight());//vertical
            context.strokeLine(0,mitadAlto, lienzo.getWidth(), mitadAlto);//horizontal
            context.strokeLine(0, lienzo.getHeight(), lienzo.getWidth(), 0);//diagonal "/"
            context.strokeLine(0,0, lienzo.getWidth(), lienzo.getHeight());//diagonal "\"
            double divisiones = mitadAncho/valor;
            for(int i=0;i<valor;i++){
                context.strokeLine(mitadAncho, i*divisiones,mitadAncho+(divisiones*i),mitadAlto);//Arriba Derecha
                context.strokeLine(i*divisiones,mitadAncho,mitadAlto,mitadAncho+(divisiones*i));//Abajo Izquierda
                context.strokeLine(mitadAncho, i*divisiones,mitadAncho-(divisiones*i),mitadAlto);//Arriba Izquierda
                context.strokeLine(mitadAncho,mitadAlto+(i*divisiones), lienzo.getWidth()-(i*divisiones), mitadAlto);//Abajo Derecha

                context.strokeLine(0+(divisiones*i),0+(divisiones*i),mitadAncho+(divisiones*i),mitadAlto-(divisiones*i));//Arriba
                context.strokeLine(0+(divisiones*i),lienzo.getHeight()-(divisiones*i),mitadAncho+(divisiones*i),mitadAlto+(divisiones*i));//Abajo
                context.strokeLine(0+(divisiones*i),0+(divisiones*i),mitadAncho-(divisiones*i),mitadAlto+(divisiones*i));//Izquierda
                context.strokeLine(lienzo.getWidth()-(divisiones*i), lienzo.getHeight()-(divisiones*i),mitadAncho+(divisiones*i),mitadAlto-(divisiones*i));//Derecha

                context.strokeLine(mitadAncho+(i*divisiones),0, lienzo.getWidth(),0+(divisiones*i));//Arriba derecha
                context.strokeLine(lienzo.getWidth(),mitadAlto+(divisiones*i),lienzo.getWidth()-(divisiones*i), lienzo.getHeight());//Abajo derecha
                context.strokeLine(0,mitadAlto+(divisiones*i),0+(divisiones*i), lienzo.getHeight());//Abajo izquierda
                context.strokeLine(0,mitadAlto-(divisiones*i),0+(divisiones*i),0);
            }

        }else if(comboCuadricula.getSelectionModel().getSelectedIndex()==5){////////Circulos/

            double radio=lienzo.getWidth()/2;
            int angulodivisiones=(int)(360/valor);
            for (int i=0;i<360;i=i+angulodivisiones){
                double posX=Math.cos(Math.toRadians(i))*radio/2;
                double posY=Math.sin(Math.toRadians(i))*radio/2;
                System.out.println("Angulo: "+i+" Intervalo "+angulodivisiones+" X: "+posX+" Y: "+posY);
                //context.strokeLine(radio,radio,(radio+posX),(radio+posY));//crear linea de referencia
                context.strokeOval((radio+posX)-radio/2,(radio+posY)-radio/2,radio,radio);
            }
        }
    }//////////////////////////////////////////////////////////////////////////////////////////////////////////Fondos

    public void arrastrar(MouseEvent mouseEvent){//////////////////////////////////////////////////////////////////Arrastrar
        if(tipoDibujo.equals("pincel")){
            String pincelText=";";
            for (int i=1;i<slideTamaño.getValue()/2;i++){
                pincelText=pincelText+";";
            }
            context.fillText(pincelText, mouseEvent.getX()-pincelText.length(), mouseEvent.getY());
        }else if(tipoDibujo.equals("cuadrado")){
            context.fillRect(mouseEvent.getX()-tamaño/2, mouseEvent.getY()-tamaño/2,tamaño, tamaño );
        }else if(tipoDibujo.equals("borrador")){
            context.clearRect(mouseEvent.getX()-tamaño/2,mouseEvent.getY()-tamaño/2,tamaño,tamaño);
        }else if(tipoDibujo.equals("circulo")){
            context.fillOval(mouseEvent.getX()-tamaño/2, mouseEvent.getY()-tamaño/2,tamaño, tamaño );
        }else if(tipoDibujo.equals("texto")){
            //context.strokeText("....", mouseEvent.getX(), mouseEvent.getY(),slideTamaño.getValue()*2);

        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////Arrastrar

    public void liencito(){/////////////////////////////////////////////////////////////////////////////////////Liencito
        GraphicsContext contextito= liencito.getGraphicsContext2D();
        contextito.setFill(Color.WHITESMOKE);
        contextito.fillRect(0,0,liencito.getWidth(),liencito.getHeight());
        contextito.setFill(Color.GRAY);
        double divisines=48;
        double incrementoX=lienzo.getWidth()/divisines;
        double incrementoY=lienzo.getHeight()/divisines;
        contextito.strokeLine(0,0, liencito.getWidth(), 0);
        contextito.strokeLine(0,0, 0, liencito.getHeight());
        for (int i=1;i<divisines+1;i++){
            ///(x1,y1, x2,y2)
            contextito.strokeLine(0,incrementoY*i,liencito.getWidth(),incrementoY*i);
            contextito.strokeLine(incrementoX*i,0,incrementoX*i, liencito.getHeight());
        }
        contextito.setFill(color);
        double coor=(liencito.getHeight()/2)-tamaño/2;

        if(tipoDibujo.equals("circulo")){

            contextito.fillOval(coor,coor,tamaño,tamaño);
        }else if(tipoDibujo.equals("cuadrado")){
            contextito.fillRect(coor,coor,tamaño,tamaño);
        }else if(tipoDibujo.equals("imagen")){

        }else  if(tipoDibujo.equals("borrador")){
            contextito.clearRect(coor,coor,tamaño,tamaño);
        }else if(tipoDibujo.equals("pincel")){
            String pincelText=";";
            for (int i=1;i<slideTamaño.getValue()/2;i++){
                pincelText=pincelText+";";
            }
            contextito.fillText(pincelText,(liencito.getWidth()/2)-pincelText.length(),liencito.getHeight()/2);
        }
        if(!tipoDibujo.equals("forma")){
            resetForma();
        }
    }/////////////////////////////////////////////////////////////////////////////////////////////////Liencito

    public void sustituirImagenReferencia(){

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(HelloApplication.stage);

        fileChooser.setTitle("Abrir imagen");
        if(file!=null){
            ImageView imageView=new ImageView(new Image(file.toURI().toString()));
            imagenReferencia.setImage(imageView.getImage());


            lienzo.setHeight(204);
            lienzo.setWidth(324);
            //imageView.setFitWidth(1004);
            //imageView.setFitHeight(638);

            context.drawImage(imageView.getImage(),0,0);

        }
    }//SustImagen de referencia

    public void pegarImagenLienzo( MouseEvent mouseEvent){
        if(tipoDibujo.equals("imagen")){
            context.drawImage(imagenReferencia.getImage(), mouseEvent.getX()-(imagenReferencia.getImage().getWidth()/2), mouseEvent.getY()-(imagenReferencia.getImage().getHeight()/2));
            resetForma();
        }else if(tipoDibujo.equals("forma")){
            if(primerClick){
                coorFormasXUno= mouseEvent.getX();
                coorFormasYUno=mouseEvent.getY();
                primerClick=false;
            }else if(!primerClick){
                coorFormasXDos= mouseEvent.getX();
                coorFormasYDos=mouseEvent.getY();

                File f=new File("");
                if(tipoForma.equals("formaCirculo")){
                    f=new File ("src/main/resources/com/example/paint/Formas/formaCirculo.png");
                }else if(tipoForma.equals("formaCuadrado")){
                    f=new File ("src/main/resources/com/example/paint/Formas/formaCuadrado.png");
                }else if(tipoForma.equals("formaEstrella")){
                    f=new File ("src/main/resources/com/example/paint/Formas/formaEstrella.png");
                }
                ImageView image = new ImageView(new Image(f.toURI().toString()));
                context.drawImage(image.getImage(),coorFormasXUno,coorFormasYUno, -1*(coorFormasXUno -  coorFormasXDos),-1*(coorFormasYUno-coorFormasYDos));
                resetForma();
            }
        }
    }

    public void guardarImagen(){
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show save file dialog
        File file = fileChooser.showSaveDialog(HelloApplication.stage);

        if(file != null){
            try {
                WritableImage writableImage = new WritableImage((int) lienzo.getWidth(),(int) lienzo.getHeight());
                lienzo.snapshot(null, writableImage);
                RenderedImage renderedImage = javafx.embed.swing.SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                System.out.printf(ex.getMessage());
            }
        }
    }
    public void setImagen() {
        tipoDibujo="imagen";
    }
    public void cambiarColor(){
        color=comboColor.getValue();
        context.setFill(color);
        liencito();
    }
    public void cambiarTamaño(){
        tamaño = slideTamaño.getValue();
        liencito();
    }

    public void resetForma(){
        coorFormasXUno=0;
        coorFormasYUno=0;
        coorFormasXDos=0;
        coorFormasYDos=0;
        primerClick=true;
    }

    public void btnImprimir(){

        Image imagenTemp=lienzo.snapshot(null, null);
        ImageView imageView=new ImageView();
        imageView.setImage(imagenTemp);

        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            System.out.println("entró1");
            boolean continuar = printerJob.showPrintDialog(HelloApplication.stage);//No es redundante porque decimos cual es la impresora predeterminada

            if (continuar) {
                System.out.println("entró");
                printerJob.printPage(imageView);
                printerJob.endJob();
            }else{
                printerJob.cancelJob();
            }
        }
        /*

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(HelloApplication.stage);

        fileChooser.setTitle("Abrir imagen");
        if(file!=null){
            ImageView imageView=new ImageView(new Image(file.toURI().toString()));
            lienzo.setHeight(204);
            lienzo.setWidth(324);

        }*/





    }//btnImprimir

   ///////////////////////////////////////////////////////////////Rotar
    public void rotarDerecha(){
        rotar=rotar+90;
        lienzo.rotateProperty().setValue(rotar);
        System.out.println("rotacion: "+lienzo.getRotate());
    }
    public void rotarIzquierda(){
        rotar=rotar-90;
        lienzo.rotateProperty().setValue(rotar);
        System.out.println("rotacion: "+lienzo.getRotate());
    }
    public void rotarMitad(){
        rotar=rotar+180;
        lienzo.rotateProperty().setValue(rotar);
        System.out.println("rotacion: "+lienzo.getRotate());
    }

    //////////////////////////////////////////////////////////////////////seters
    public void setCuadrado() {
        tipoDibujo="cuadrado";
        liencito();
    }
    public void setformaEstrella(){
        tipoForma="formaEstrella";
        tipoDibujo="forma";
    }
    public void setformaCirculo(){
        tipoForma="formaCirculo";
        tipoDibujo="forma";
    }
    public void setformaCuadrado(){
        tipoForma="formaCuadrado";
        tipoDibujo="forma";
    }
    public void setBorrador(){
        tipoDibujo="borrador";
        liencito();
    }
    public void setPincel(){
        tipoDibujo="pincel";
        liencito();
    }
    public void setText(){
        tipoDibujo="texto";
        liencito();
    }
    public void setCirculo(){
        tipoDibujo="circulo";
        liencito();
    }
    public void setEliminar(){
        context.clearRect(0,0,lienzo.getWidth(),lienzo.getHeight());
    }
}