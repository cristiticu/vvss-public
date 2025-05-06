module pizzashop {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;

    opens pizzashop.model;

    exports pizzashop.model;

    opens pizzashop to javafx.fxml;

    exports pizzashop;

    opens pizzashop.controller to javafx.fxml;

    exports pizzashop.controller;
    exports pizzashop.service;
    exports pizzashop.repository;

    opens pizzashop.repository;
    opens pizzashop.service;
}
