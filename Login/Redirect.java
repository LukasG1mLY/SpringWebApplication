package de.rub.springwebapplication.Login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.rub.springwebapplication.MitabeiterView.MitabeiterView;
import de.rub.springwebapplication.StudentenView.StudentenView;


@Route("")
public class Redirect extends VerticalLayout {


    public Redirect() {


        LoginOverlay studentenLogin = new LoginOverlay();

        studentenLogin.setTitle("Studenten Login");
        studentenLogin.setDescription("Login für Studenten");
        studentenLogin.addLoginListener(event -> UI.getCurrent().navigate(StudentenView.class));

        LoginOverlay mitabeiterLogin = new LoginOverlay();

        mitabeiterLogin.setTitle("Mitabeiter Login");
        mitabeiterLogin.setDescription("Login für Mitabeitende");
        mitabeiterLogin.addLoginListener(event -> UI.getCurrent().navigate(MitabeiterView.class));

        Button loginStudenten = new Button("Studenten Login", new Icon(VaadinIcon.USERS));
        loginStudenten.addClickListener(event -> studentenLogin.setOpened(true));

        Button loginMitabeiter = new Button("Mitabeiter Login", new Icon(VaadinIcon.USER_STAR));
        loginMitabeiter.addClickListener(event -> mitabeiterLogin.setOpened(true));

        Image image = new Image("images/LogoeCampus_5_2013.jpg", "images/RUB.png");
        image.setVisible(true);

        Image image2 = new Image("images/LogoeCampus_5_20132.png", "images/RUB.png");
        image2.setVisible(false);

        Button showimage = new Button("show/hide",Clickevent -> {
            if (image.isVisible()) {
                image.setVisible(false);
                image2.setVisible(true);

            } else {
                image.setVisible(true);
                image2.setVisible(false);
            }
        });

        H2 Titel = new H2("Willkommen zum Portal der Weiterleitung von der Ruhr Universität Bochum");

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        add(image,image2, Titel, loginMitabeiter,loginStudenten, mitabeiterLogin, studentenLogin, showimage);



    }
}
