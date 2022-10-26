package de.rub.springwebapplication.MitabeiterView;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.WebBrowser;
import de.rub.springwebapplication.Data.DataBaseUtils;
import de.rub.springwebapplication.Login.Redirect;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
@PageTitle("MitabeiterView")
@Route("MitabeiterView")

public class MitabeiterView extends Div {

    public DataBaseUtils dataBaseUtils;
    private final Tab Startseite_WebClient;
    private final Tab Bearbeiten;
    private final Tab Logout;
    private final Tab LDAP;
    private final VerticalLayout content;

    public MitabeiterView() throws IOException {

        final WebBrowser webBrowser = UI.getCurrent().getSession().getBrowser();
        System.out.println("IP: " + webBrowser.getAddress() + " " + "Opened: " + getClass());

        dataBaseUtils = new DataBaseUtils();
        content = new VerticalLayout();
        Startseite_WebClient = new Tab(VaadinIcon.HOME.create(), new Span("Startseite WebClient"));
        Logout = new Tab(VaadinIcon.ARROW_LEFT.create(), new Span("Logout"));
        Bearbeiten = new Tab(VaadinIcon.EDIT.create(), new Span("Bearbeiten"));
        LDAP = new Tab(VaadinIcon.EDIT.create(), new Span("LDAP  Liste"));

        Tabs tabs = new Tabs(Startseite_WebClient, Bearbeiten, LDAP, Logout);
        tabs.addSelectedChangeListener(event ->  setContent(event.getSelectedTab()));
        setContent(tabs.getSelectedTab());

        add(tabs, content);
    }
    private void setContent(@NotNull Tab tab) {
        content.removeAll();

        if (tab.equals(Startseite_WebClient)) {
            content.setAlignItems(FlexComponent.Alignment.CENTER);
            content.add(new Image("images/LogoeCampus_5_2013.jpg", "images/RUB.png"));
            H1 Titel = new H1("Startseite eCampus Webclient");
            Paragraph Info_Text_Studenten = new Paragraph();
            Info_Text_Studenten.setText(dataBaseUtils.getInfoStaff());

            content.add(Titel, Info_Text_Studenten);


        }
        else if (tab.equals(Bearbeiten)) {
            content.setAlignItems(FlexComponent.Alignment.END);


            //Studenten Infobox
            TextArea Change_Info_Text_Studenten = new TextArea("Info Text Studenten");
            Button Confirm_Changed_Text_Value_For_Studenten = new Button("Bestätigen");
            Confirm_Changed_Text_Value_For_Studenten.addClickListener(ClickEvent -> {
                        dataBaseUtils.editInfoStudent(Change_Info_Text_Studenten.getValue());
                        Change_Info_Text_Studenten.setPlaceholder(dataBaseUtils.getInfoStudent());
                        Change_Info_Text_Studenten.setValue("");
            });
            Change_Info_Text_Studenten.setWidthFull();
            Change_Info_Text_Studenten.setMinHeight("100px");
            Change_Info_Text_Studenten.setMaxHeight("150px");
            Change_Info_Text_Studenten.setPlaceholder(dataBaseUtils.getInfoStudent());
            Change_Info_Text_Studenten.setClearButtonVisible(true);

            //Mitabeiter Infobox
            TextArea Change_Info_Text_Mitabeiter = new TextArea("Info Text Mitabeiter");
            Button Confirm_Changed_Text_Value_For_Mitabeiter = new Button("Bestätigen");
            Confirm_Changed_Text_Value_For_Mitabeiter.addClickListener(ClickEvent -> {
                dataBaseUtils.editInfoStaff(Change_Info_Text_Mitabeiter.getValue());
                Change_Info_Text_Mitabeiter.setPlaceholder(dataBaseUtils.getInfoStaff());
                Change_Info_Text_Mitabeiter.setValue("");
            });
            Change_Info_Text_Mitabeiter.setWidthFull();
            Change_Info_Text_Mitabeiter.setMinHeight("100px");
            Change_Info_Text_Mitabeiter.setMaxHeight("150px");
            Change_Info_Text_Mitabeiter.setPlaceholder(dataBaseUtils.getInfoStaff());
            Change_Info_Text_Mitabeiter.setClearButtonVisible(true);


            content.add(Change_Info_Text_Mitabeiter, Confirm_Changed_Text_Value_For_Mitabeiter);
            content.add(Change_Info_Text_Studenten, Confirm_Changed_Text_Value_For_Studenten);

        }
        else if (tab.equals(LDAP)){
            content.setAlignItems(FlexComponent.Alignment.CENTER);

            //Erstellt die Komponenten für den jeweiligen Tab.

            MenuBar LDAP_EDIT = new MenuBar();
            MenuItem options = LDAP_EDIT.addItem("LDAP Verzeichnisse");
            SubMenu subItems = options.getSubMenu();

            //Erstellt eine Liste mit allen Einträgen in der Datenbank und mit denen die noch dazukommen
            List<String[]> allIdsAndNames = dataBaseUtils.getAllInfos();

            //Erstellt ein Array aus Buttons für so viele Rows wie Vorhanden
            //außerdem wird hier zusätzlich noch ein Platzhalter
            //für die Methode LDAP_ADD freigehalten, sodass beim Erstellen einer neuen ROW in der Datenbank
            //diese erstell-methode nicht überschrieben wird.(Deshalb das +1)
            Button[] buttons = new Button[allIdsAndNames.size() +1];

            //Erstellt ein Array aus TextFeldern für so viele Rows wie Verhanden +1 Textfeld für ADD_LDAP
            TextField[] textFields = new TextField[allIdsAndNames.size() +1];


            //Dies ist eine For-Schleife diese Dient dazu
            //für jeden bereits vorhandenen Eintrag in der DatenBank
            //einen Button und ein Textfeld zu erstellen
            //damit man diesen Eintrag auf der Weboberfläche bearbeiten kann
            //für jeden Button u.o Textfeld
            for (int i = 0; i < buttons.length; i++) {
                //Gleichsetzen von l mit i da l = i sein soll
                //dies dient dazu, dass das zu erstellende Textfeld,
                //der zu erstellende button genau die gleichen Erscheinungen haben soll
                //wie für die Textfelder/Buttons für [i].
                //(Einfach ausgedrückt soll der Integer[l] genauso aussehen wie der Integer[i]
                //aber er soll nicht das Gleiche machen dazu komme ich im nächsten Schritt.
                final int l = i;

                //Initialisierung von Buttons
                buttons[l] = new Button("Bestätigen");

                //Initialisierung von TextFeldern
                textFields[l] = new TextField();

                //Hier wird bestimmt was für besondere Erscheinungs-Variablen
                //ein Textfeld bzw. ein Button haben soll.
                textFields[l].setVisible(true);
                textFields[l].setReadOnly(true);
                textFields[l].setWidth(40, Unit.PERCENTAGE);

                //Nehmen wir diesen Komponenten als beispiel:
                //wenn wir diesen Button[l] erstellen, dann wird dieser Automatisch vom code sichtbar gemacht,
                //da wir den Button jedoch nur zu einem bestimmten Event(Im demfall ein Selection-event)
                //sichtbar haben wollen sagen wir der Methode hier, dass sie den Button hier verstecken soll.
                buttons[l].setVisible(false);
                buttons[l].addClickShortcut(Key.ENTER);

                //Bei diesem if Statement passiert die eigentliche Magie,
                //sobald dieses IF-Statement getriggert wird, sorgt es dafür,
                //dass jedem erstellten Textfeld
                //der jeweilige Inhalt aus der Datenbank in jedes Textfeld geschrieben wird.
                //Damit stellt sich bestimmt die Frage wie weiß denn der Code wie wo was eingesetz wird
                //Ganz einfach! Dieser Code basiert auf Java und da java eine Sprache ist,
                //die von Oben nach unten Arbeitet, liest der Code einfach nur jede Spalte aus fügt sie in ein textfeld ein und geht dann zur nächsten
                //dies funktioniert ungefähr so:
                //Sobald der Code eine Spalte(ROW) in der Datenbank fertig abgelesen und in das bestimmte Textfeld gesetzt hat,
                //springt er mit dem befehl rs.next zur nächten ROW in der Datenbank
                if (i != buttons.length-1)
                {
                    MenuItem LDAP_ID = subItems.addItem("Verzeichnis_" + allIdsAndNames.get(l)[0]);

                    textFields[l].setValue(allIdsAndNames.get(l)[1]);
                    textFields[l].setLabel("Verzeichnis_" + allIdsAndNames.get(l)[0]);


                    LDAP_ID.addClickListener(event -> {

                        for (int j = 0; j < buttons.length; j++) {
                            textFields[j].setVisible(false);
                            buttons[j].setVisible(false);
                            textFields[l].setValue("");

                        }
                        //Genauso wie beim Button[l] wird in diesem Click listener der Befehlt setVisible getriggert.
                        textFields[l].setVisible(true);
                        textFields[l].setReadOnly(false);
                        //Wie zuvor in Line 152-155 beschrieben wird hier der button[l] sichtbar gemacht
                        buttons[l].setVisible(true);
                        textFields[l].setPlaceholder(allIdsAndNames.get(l)[1]);

                        textFields[l].addValueChangeListener(e -> {

                            buttons[l].setEnabled(true);
                            buttons[l].setVisible(true);


                        });

                    });

                    buttons[l].addClickListener(Click -> {
                        if (textFields[l].isEmpty()) {
                            textFields[l].setInvalid(true);
                            textFields[l].setErrorMessage("Dieses Feld darf NICHT Leer sein !");
                            textFields[l].setPlaceholder(textFields[l].getValue());
                            buttons[l].setEnabled(false);
                        } else {
                            Notification.show("Wurde geändert");
                            dataBaseUtils.editInfoLDAP(l, textFields[l].getValue());
                            textFields[l].setPlaceholder(textFields[l].getValue());
                            textFields[l].setValue("");
                            buttons[l].setEnabled(true);
                        }


                    });



                }
            }

            MenuItem LDAP_ADD = subItems.addItem("Hinzufügen");


            LDAP_ADD.addClickListener(event -> {
                for (int j = 0; j < buttons.length; j++) {
                    textFields[j].setVisible(false);
                    buttons[j].setVisible(false);
                }
                textFields[buttons.length - 1].setVisible(true);
                textFields[buttons.length - 1].setReadOnly(false);
                textFields[buttons.length - 1].setLabel("Verzeichnis hinzufügen");
                textFields[buttons.length - 1].setValue("");
                textFields[buttons.length - 1].setPlaceholder("");
                textFields[buttons.length - 1].addValueChangeListener(e ->
                   buttons[buttons.length - 1].setEnabled(true));
                   buttons[buttons.length - 1].setVisible(true);
                   buttons[buttons.length - 1].addClickShortcut(Key.ENTER);

                buttons[buttons.length-1].addClickListener(Click -> {
                    if (textFields[buttons.length - 1].isEmpty()) {
                        textFields[buttons.length - 1].setInvalid(true);
                        textFields[buttons.length - 1].setErrorMessage("Dieses Feld darf NICHT Leer sein !");
                        buttons[buttons.length - 1].setEnabled(false);
                    }
                    else {
                        dataBaseUtils.addNewIdAndName(textFields[textFields.length-1].getValue());
                        Notification.show("Eintrag hinzugefügt");
                        textFields[textFields.length-1].setValue("");
                    }

                });
            });


            content.add(LDAP_EDIT, options);
            for (int i = 0; i < buttons.length; i++)
            {
                content.add(textFields[i], buttons[i]);
            }
        }
        else if (tab.equals(Logout)){
            UI.getCurrent().navigate(Redirect.class);

        }
    }

}
