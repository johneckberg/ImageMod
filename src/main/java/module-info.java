module eckbergj.imagemod {
    //Let it be known i may have shot myself in the foot in maven and this may only run on MacOS
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires ImageUtil; //MSOE specific package
    requires org.jfxtras.styles.jmetro;

    opens eckbergj.imagemod to javafx.fxml;
    exports eckbergj.imagemod;
}