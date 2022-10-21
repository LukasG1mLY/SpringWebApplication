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

@PageTitle("MitabeiterView")
@Route("MitabeiterView")

    public class MitabeiterView extends Div {
    private TextField ID_1;
    private Button ID_1_Button;
    private TextField ID_2;
    private Button ID_2_Button;
    private TextField ID_3;
    private Button ID_3_Button;
    private TextField ID_4;
    private Button ID_4_Button;
    private TextField ID_5;
    private Button ID_5_Button;
    private TextField ID_6;
    private Button ID_6_Button;
    private TextField ID_7;
    private Button ID_7_Button;
    private TextField ID_8;
    private Button ID_8_Button;
    public TextField ID_ADD;
    public Button ID_ADD_Button;


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
        ID_1 = null;
        ID_2 = null;
        ID_3 = null;
        ID_4 = null;
        ID_5 = null;
        ID_6 = null;
        ID_8 = null;
        ID_7 = null;
        ID_ADD = null;
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

            MenuItem LDAP_ID_1 = subItems.addItem("LDAP_ID_1");
            MenuItem LDAP_ID_2 = subItems.addItem("LDAP_ID_2");
            MenuItem LDAP_ID_3 = subItems.addItem("LDAP_ID_3");
            MenuItem LDAP_ID_4 = subItems.addItem("LDAP_ID_4");
            MenuItem LDAP_ID_5 = subItems.addItem("LDAP_ID_5");
            MenuItem LDAP_ID_6 = subItems.addItem("LDAP_ID_6");
            MenuItem LDAP_ID_7 = subItems.addItem("LDAP_ID_7");
            MenuItem LDAP_ID_8 = subItems.addItem("LDAP_ID_8");
            MenuItem LDAP_ADD_ID = subItems.addItem("LDAP_ADD_ID");

            ID_1_Button = new Button("Bestätigen");
            ID_2_Button = new Button("Bestätigen");
            ID_3_Button = new Button("Bestätigen");
            ID_4_Button = new Button("Bestätigen");
            ID_5_Button = new Button("Bestätigen");
            ID_6_Button = new Button("Bestätigen");
            ID_7_Button = new Button("Bestätigen");
            ID_8_Button = new Button("Bestätigen");
            ID_ADD_Button = new Button("Bestätigen");

            ID_1_Button.setVisible(false);
            ID_2_Button.setVisible(false);
            ID_3_Button.setVisible(false);
            ID_4_Button.setVisible(false);
            ID_5_Button.setVisible(false);
            ID_6_Button.setVisible(false);
            ID_7_Button.setVisible(false);
            ID_8_Button.setVisible(false);
            ID_ADD_Button.setVisible(false);



            ID_1 = new TextField("LDAP_ID_1");
            ID_2 = new TextField("LDAP_ID_2");
            ID_3 = new TextField("LDAP_ID_3");
            ID_4 = new TextField("LDAP_ID_4");
            ID_5 = new TextField("LDAP_ID_5");
            ID_6 = new TextField("LDAP_ID_6");
            ID_7 = new TextField("LDAP_ID_7");
            ID_8 = new TextField("LDAP_ID_8");
            ID_ADD = new TextField("LDAP_ADD_ID");


            ID_1.setVisible(false);
            ID_2.setVisible(false);
            ID_3.setVisible(false);
            ID_4.setVisible(false);
            ID_5.setVisible(false);
            ID_6.setVisible(false);
            ID_7.setVisible(false);
            ID_8.setVisible(false);
            ID_ADD.setVisible(false);

            ID_1.setWidthFull();
            ID_2.setWidthFull();
            ID_3.setWidthFull();
            ID_4.setWidthFull();
            ID_5.setWidthFull();
            ID_6.setWidthFull();
            ID_7.setWidthFull();
            ID_8.setWidthFull();
            ID_ADD.setWidthFull();

            ID_1.setPlaceholder(dataBaseUtils.getInfoLDAP_ID_1());
            ID_2.setPlaceholder(dataBaseUtils.getInfoLDAP_ID_2());
            ID_3.setPlaceholder(dataBaseUtils.getInfoLDAP_ID_3());
            ID_4.setPlaceholder(dataBaseUtils.getInfoLDAP_ID_4());
            ID_5.setPlaceholder(dataBaseUtils.getInfoLDAP_ID_5());
            ID_6.setPlaceholder(dataBaseUtils.getInfoLDAP_ID_6());
            ID_7.setPlaceholder(dataBaseUtils.getInfoLDAP_ID_7());
            ID_8.setPlaceholder(dataBaseUtils.getInfoLDAP_ID_8());

            ID_1.setHelperText(dataBaseUtils.getInfoLDAP_ID_1());
            ID_2.setHelperText(dataBaseUtils.getInfoLDAP_ID_2());
            ID_3.setHelperText(dataBaseUtils.getInfoLDAP_ID_3());
            ID_4.setHelperText(dataBaseUtils.getInfoLDAP_ID_4());
            ID_5.setHelperText(dataBaseUtils.getInfoLDAP_ID_5());
            ID_6.setHelperText(dataBaseUtils.getInfoLDAP_ID_6());
            ID_7.setHelperText(dataBaseUtils.getInfoLDAP_ID_7());
            ID_8.setHelperText(dataBaseUtils.getInfoLDAP_ID_8());
            ID_ADD.setHelperText("Füge eine neue ID in die Datenbank hinzu");

            LDAP_ID_1.addClickListener(event -> {

                ID_1.setVisible(true);
                ID_2.setVisible(false);
                ID_3.setVisible(false);
                ID_4.setVisible(false);
                ID_5.setVisible(false);
                ID_6.setVisible(false);
                ID_7.setVisible(false);
                ID_8.setVisible(false);
                ID_ADD.setVisible(false);

                ID_1_Button.setVisible(true);
                ID_2_Button.setVisible(false);
                ID_3_Button.setVisible(false);
                ID_4_Button.setVisible(false);
                ID_5_Button.setVisible(false);
                ID_6_Button.setVisible(false);
                ID_7_Button.setVisible(false);
                ID_8_Button.setVisible(false);
                ID_ADD_Button.setVisible(false);




                ID_1_Button.addClickListener(Click -> dataBaseUtils.editInfoLDAP_ID_1(ID_1.getValue()));

            });

            LDAP_ID_2.addClickListener(event -> {

                ID_1.setVisible(false);
                ID_2.setVisible(true);
                ID_3.setVisible(false);
                ID_4.setVisible(false);
                ID_5.setVisible(false);
                ID_6.setVisible(false);
                ID_7.setVisible(false);
                ID_8.setVisible(false);
                ID_ADD.setVisible(false);

                ID_1_Button.setVisible(false);
                ID_2_Button.setVisible(true);
                ID_3_Button.setVisible(false);
                ID_4_Button.setVisible(false);
                ID_5_Button.setVisible(false);
                ID_6_Button.setVisible(false);
                ID_7_Button.setVisible(false);
                ID_8_Button.setVisible(false);
                ID_ADD_Button.setVisible(false);

                ID_2_Button.addClickListener(Click -> dataBaseUtils.editInfoLDAP_ID_2(ID_2.getValue()));

            });

            LDAP_ID_3.addClickListener(event -> {

                ID_1.setVisible(false);
                ID_2.setVisible(false);
                ID_3.setVisible(true);
                ID_4.setVisible(false);
                ID_5.setVisible(false);
                ID_6.setVisible(false);
                ID_7.setVisible(false);
                ID_8.setVisible(false);
                ID_ADD.setVisible(false);

                ID_1_Button.setVisible(false);
                ID_2_Button.setVisible(false);
                ID_3_Button.setVisible(true);
                ID_4_Button.setVisible(false);
                ID_5_Button.setVisible(false);
                ID_6_Button.setVisible(false);
                ID_7_Button.setVisible(false);
                ID_8_Button.setVisible(false);
                ID_ADD_Button.setVisible(false);

                ID_3_Button.addClickListener(Click -> dataBaseUtils.editInfoLDAP_ID_3(ID_3.getValue()));

            });

            LDAP_ID_4.addClickListener(event -> {

                ID_1.setVisible(false);
                ID_2.setVisible(false);
                ID_3.setVisible(false);
                ID_4.setVisible(true);
                ID_5.setVisible(false);
                ID_6.setVisible(false);
                ID_7.setVisible(false);
                ID_8.setVisible(false);
                ID_ADD.setVisible(false);

                ID_1_Button.setVisible(false);
                ID_2_Button.setVisible(false);
                ID_3_Button.setVisible(false);
                ID_4_Button.setVisible(true);
                ID_5_Button.setVisible(false);
                ID_6_Button.setVisible(false);
                ID_7_Button.setVisible(false);
                ID_8_Button.setVisible(false);
                ID_ADD_Button.setVisible(false);

                ID_4_Button.addClickListener(Click -> dataBaseUtils.editInfoLDAP_ID_4(ID_4.getValue()));

            });

            LDAP_ID_5.addClickListener(event -> {

                ID_1.setVisible(false);
                ID_2.setVisible(false);
                ID_3.setVisible(false);
                ID_4.setVisible(false);
                ID_5.setVisible(true);
                ID_6.setVisible(false);
                ID_7.setVisible(false);
                ID_8.setVisible(false);
                ID_ADD.setVisible(false);

                ID_1_Button.setVisible(false);
                ID_2_Button.setVisible(false);
                ID_3_Button.setVisible(false);
                ID_4_Button.setVisible(false);
                ID_5_Button.setVisible(true);
                ID_6_Button.setVisible(false);
                ID_7_Button.setVisible(false);
                ID_8_Button.setVisible(false);
                ID_ADD_Button.setVisible(false);

                ID_5_Button.addClickListener(Click -> dataBaseUtils.editInfoLDAP_ID_5(ID_5.getValue()));

            });

            LDAP_ID_6.addClickListener(event -> {

                ID_1.setVisible(false);
                ID_2.setVisible(false);
                ID_3.setVisible(false);
                ID_4.setVisible(false);
                ID_5.setVisible(false);
                ID_6.setVisible(true);
                ID_7.setVisible(false);
                ID_8.setVisible(false);
                ID_ADD.setVisible(false);

                ID_1_Button.setVisible(false);
                ID_2_Button.setVisible(false);
                ID_3_Button.setVisible(false);
                ID_4_Button.setVisible(false);
                ID_5_Button.setVisible(false);
                ID_6_Button.setVisible(true);
                ID_7_Button.setVisible(false);
                ID_8_Button.setVisible(false);
                ID_ADD_Button.setVisible(false);

                ID_6_Button.addClickListener(Click -> dataBaseUtils.editInfoLDAP_ID_6(ID_6.getValue()));

            });

            LDAP_ID_7.addClickListener(event -> {

                ID_1.setVisible(false);
                ID_2.setVisible(false);
                ID_3.setVisible(false);
                ID_4.setVisible(false);
                ID_5.setVisible(false);
                ID_6.setVisible(false);
                ID_7.setVisible(true);
                ID_8.setVisible(false);
                ID_ADD.setVisible(false);

                ID_1_Button.setVisible(false);
                ID_2_Button.setVisible(false);
                ID_3_Button.setVisible(false);
                ID_4_Button.setVisible(false);
                ID_5_Button.setVisible(false);
                ID_6_Button.setVisible(false);
                ID_7_Button.setVisible(true);
                ID_8_Button.setVisible(false);
                ID_ADD_Button.setVisible(false);

                ID_7_Button.addClickListener(Click -> dataBaseUtils.editInfoLDAP_ID_7(ID_7.getValue()));

            });

            LDAP_ID_8.addClickListener(event -> {

                ID_1.setVisible(false);
                ID_2.setVisible(false);
                ID_3.setVisible(false);
                ID_4.setVisible(false);
                ID_5.setVisible(false);
                ID_6.setVisible(false);
                ID_7.setVisible(false);
                ID_8.setVisible(true);
                ID_ADD.setVisible(false);

                ID_1_Button.setVisible(false);
                ID_2_Button.setVisible(false);
                ID_3_Button.setVisible(false);
                ID_4_Button.setVisible(false);
                ID_5_Button.setVisible(false);
                ID_6_Button.setVisible(false);
                ID_7_Button.setVisible(false);
                ID_8_Button.setVisible(true);
                ID_ADD_Button.setVisible(false);

                ID_8_Button.addClickListener(Click -> dataBaseUtils.editInfoLDAP_ID_8(ID_8.getValue()));
            });

            LDAP_ADD_ID.addClickListener(event -> {



                ID_1.setVisible(false);
                ID_2.setVisible(false);
                ID_3.setVisible(false);
                ID_4.setVisible(false);
                ID_5.setVisible(false);
                ID_6.setVisible(false);
                ID_7.setVisible(false);
                ID_8.setVisible(false);
                ID_ADD.setVisible(true);
                ID_ADD.setPlaceholder("Diese Funktion Gibt es leider noch nicht");

                ID_1_Button.setVisible(false);
                ID_2_Button.setVisible(false);
                ID_3_Button.setVisible(false);
                ID_4_Button.setVisible(false);
                ID_5_Button.setVisible(false);
                ID_6_Button.setVisible(false);
                ID_7_Button.setVisible(false);
                ID_8_Button.setVisible(false);
                ID_ADD_Button.setVisible(true);

                ID_ADD_Button.addClickListener(Click -> Notification.show("Noch gibt es diese Möglichkeit leider nicht"));

            });

            content.add(LDAP_EDIT, options, ID_1, ID_1_Button,
                                            ID_2, ID_2_Button,
                                            ID_3, ID_3_Button,
                                            ID_4, ID_4_Button,
                                            ID_5, ID_5_Button,
                                            ID_6, ID_6_Button,
                                            ID_7, ID_7_Button,
                                            ID_8, ID_8_Button,
                                            ID_ADD, ID_ADD_Button);
        }
    }

}
