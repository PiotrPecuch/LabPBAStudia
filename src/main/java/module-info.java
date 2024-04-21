module com.example.pablab {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires lombok;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;
    requires org.apache.logging.log4j;

    opens com.example.pablab.Entity to org.hibernate.orm.core;
    opens com.example.pablab to javafx.fxml;
    exports com.example.pablab;
    exports com.example.pablab.Controller;
    opens com.example.pablab.Controller to javafx.fxml;


}