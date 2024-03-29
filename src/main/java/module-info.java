/**
 * version:     $
 * created by:  d.stueken
 * created on:  01.05.2022 22:48
 * modified by: $
 * modified on: $
 */
module ditz.atrops.hedron {
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.controls;
    requires javafx.swing;

    requires org.controlsfx.controls;

    opens ditz.atrops.hedron to javafx.graphics;
    opens ditz.atrops.collections to javafx.graphics;
    opens ditz.atrops.hedron.colors to javafx.graphics;
    opens ditz.atrops.hedron.gui to javafx.graphics;
}