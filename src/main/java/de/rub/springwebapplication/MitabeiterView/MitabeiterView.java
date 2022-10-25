package de.rub.springwebapplication.MitabeiterView;

import com.vaadin.flow.component.UI;
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
    private final VerticalLayout content;

    public MitabeiterView() throws IOException {

        final WebBrowser webBrowser = UI.getCurrent().getSession().getBrowser();
        System.out.println("IP: " + webBrowser.getAddress() + " " + "Opened: " + getClass());

        dataBaseUtils = new DataBaseUtils();
        content = new VerticalLayout();
        Startseite_WebClient = new Tab(VaadinIcon.HOME.create(), new Span("Startseite WebClient"));
        Logout = new Tab(VaadinIcon.ARROW_LEFT.create(), new Span("Logout"));
        Bearbeiten = new Tab(VaadinIcon.EDIT.create(), new Span("Bearbeiten"));
        Tab LDAP = new Tab("LDAP");

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
        else if (tab.equals(Logout)){
            UI.getCurrent().navigate(Redirect.class);

        }
        else {
            MenuBar LDAP_EDIT = new MenuBar();
            MenuItem options = LDAP_EDIT.addItem("Change LDAP Values");
            SubMenu subItems = options.getSubMenu();

            List<String[]> allIdsAndNames = dataBaseUtils.getAllInfos();
            Button[] buttons = new Button[allIdsAndNames.size() +1];//Erstellt ein Array aus Buttons für so viele Rows wie Verhanden +1 Button für ADD_LDAP
            TextField[] textFields = new TextField[allIdsAndNames.size() +1];//Erstellt ein Array aus TextFeldern für so viele Rows wie Verhanden +1 Textfeld für ADD_LDAP


            for (int i = 0; i < buttons.length; i++) //Für jeden Button u.o Textfeld
            {
                final int l = i; //Gleichzusetzen mit i
                buttons[l] = new Button("Bestätigen");//Initialisierung von Buttons
                textFields[l] = new TextField();//Initialisierung von TextFeldern

                buttons[l].setVisible(false);
                buttons[l].setWidthFull();

                textFields[l].setVisible(false);
                textFields[l].setWidthFull();

                if (i == buttons.length-1)
                {
                    MenuItem LDAP_ADD = subItems.addItem("LDAP_ADD_ID");
                    LDAP_ADD.addClickListener(event ->
                    {
                        for (int j = 0; j < buttons.length; j++) {
                            buttons[j].setVisible(false);
                            textFields[j].setVisible(false);
                        }
                        buttons[buttons.length-1].setVisible(true);
                        textFields[buttons.length-1].setVisible(true);
                        textFields[buttons.length-1].setValue("");
                        textFields[buttons.length-1].setPlaceholder(allIdsAndNames.get(buttons.length-1)[1]);
                    });

                    textFields[buttons.length-1] = new TextField();
                    buttons[buttons.length-1] = new Button("Bestätigen");
                    buttons[buttons.length-1].addClickListener(Click -> {
                        dataBaseUtils.addNewIdAndName(textFields[textFields.length-1].getValue());
                        Notification.show("Eintrag hinzugefügt");
                    });
                }
                else
                {
                    textFields[l].setValue(allIdsAndNames.get(l)[1]);

                    MenuItem LDAP_ID = subItems.addItem("LDAP_ID_"+allIdsAndNames.get(l)[0]);

                    LDAP_ID.addClickListener(event -> {

                        for (int j = 0; j < buttons.length; j++) {
                            buttons[j].setVisible(false);
                            textFields[j].setVisible(false);
                        }
                        buttons[l].setVisible(true);
                        textFields[l].setVisible(true);
                        textFields[l].setValue("");
                        textFields[l].setPlaceholder(allIdsAndNames.get(l)[1]);

                    });

                    buttons[l].addClickListener(Click -> dataBaseUtils.editInfoLDAP(l, textFields[l].getValue()));
                }

            }

            content.add(LDAP_EDIT, options);
            for (int i = 0; i < buttons.length; i++)
            {
                content.add(buttons[i], textFields[i]);
            }
        }
    }

}
