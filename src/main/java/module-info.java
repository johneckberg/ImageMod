module eckbergj.imagemod {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires ImageUtil; //MSOE specific package

    opens eckbergj.imagemod to javafx.fxml;
    exports eckbergj.imagemod;
}