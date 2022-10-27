package de.rub.springwebapplication.MitabeiterView;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
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
            //
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
            //
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

        else if (tab.equals(Logout)) {
            UI.getCurrent().navigate(Redirect.class);

        }

        else if (tab.equals(LDAP)) {

            MenuBar LDAP_EDIT = new MenuBar();
            List<String[]> allIdsAndNames = dataBaseUtils.getAllInfos();
            Button[] buttons = new Button[allIdsAndNames.size() + 1];
            Button[] button = new Button[allIdsAndNames.size() + 1];
            Button[] delete = new Button[allIdsAndNames.size() + 1];
            Button[] cancel = new Button[allIdsAndNames.size() + 1];
            Dialog[] dialog = new Dialog[allIdsAndNames.size() + 1];
            TextField[] textFields = new TextField[allIdsAndNames.size() + 1];



            MenuItem LDAP_IDs = LDAP_EDIT.addItem("Bearbeiten");
            MenuItem LDAP_ADD = LDAP_EDIT.addItem("Hinzufügen");
            MenuItem LDAP_Overview = LDAP_EDIT.addItem("Übersicht");


            //Bearbeiten
            {
                SubMenu subItems = LDAP_IDs.getSubMenu();

                for (int i = 0; i < buttons.length; i++) {

                    final int l = i;
                    buttons[l] = new Button("Bestätigen");
                    //Dialog Buttons
                    button[l] = new Button("Verzeichnis Löschen");
                    delete[l] = new Button("Löschen");
                    cancel[l] = new Button("Abbrechen");
                    dialog[l]  = new Dialog();

                    textFields[l] = new TextField();

                    textFields[l].setVisible(false);
                    textFields[l].setWidth(40, Unit.PERCENTAGE);
                    button[l].setVisible(false); //Dialog trigger button
                    delete[l].setVisible(false); //Im Dialog Button
                    cancel[l].setVisible(false); //Im Dialog Button
                    buttons[l].setVisible(false); //Der Bestätigen-Button

                    buttons[l].addClickShortcut(Key.ENTER);
                    if (i != buttons.length - 1) {
                        MenuItem LDAP_ID = subItems.addItem("Verzeichnis_" + allIdsAndNames.get(l)[0]); {
                            textFields[l].setValue(allIdsAndNames.get(l)[1]);
                            textFields[l].setLabel("Verzeichnis_" + allIdsAndNames.get(l)[0]);

                            LDAP_ID.addClickListener(event -> {
                                for (int j = 0; j < buttons.length; j++) {
                                    textFields[j].setVisible(false);
                                    buttons[j].setVisible(false);

                                }
                                textFields[l].setVisible(true);
                                textFields[l].setReadOnly(false);
                                textFields[l].setMinLength(1);
                                buttons[l].setVisible(true);
                                button[l].setVisible(true);


                                textFields[l].setPlaceholder(allIdsAndNames.get(l)[1]);

                                dialog[l].setHeaderTitle(
                                        String.format("Möchten Sie Verzeichnis_ " + allIdsAndNames.get(l)[0] + " " + "Löschen ?"));
                                dialog[l].add("Sind Sie sicher, dass sie dieses Verzeichnis endgültig löschen wollen ?");

                                button[l].addClickListener(e -> {
                                    delete[l].setVisible(true);
                                    cancel[l].setVisible(true);
                                    dialog[l].open();

                                });

                                delete[l].addClickListener((e) -> {
                                    dataBaseUtils.deleteInfoLDAP(Integer.parseInt(allIdsAndNames.get(l)[0]));
                                    dialog[l].close();
                                    UI.getCurrent().getPage().reload();

                                });
                                delete[l].addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                                delete[l].getStyle().set("margin-right", "auto");
                                dialog[l].getFooter().add(delete[l]);

                                cancel[l].addClickListener((e) -> dialog[l].close());
                                cancel[l].addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                                dialog[l].getFooter().add(cancel[l]);


                                buttons[l].addClickListener(Click -> {
                                    if (textFields[l].getValue().length() < textFields[l].getMinLength()){
                                        textFields[l].setInvalid(true);
                                        textFields[l].setErrorMessage("Dieses Feld darf NICHT Leer sein !");

                                    } else {
                                        dataBaseUtils.editInfoLDAP(l, textFields[l].getValue());
                                        textFields[l].setInvalid(false);
                                        textFields[l].setPlaceholder(allIdsAndNames.get(l)[1]);
                                        UI.getCurrent().getPage().reload();
                                    }
                                });

                            });
                        }
                    }
                }

            }
            //Hinzufügen
            {
                dialog[button.length - 1].setVisible(true);
                buttons[button.length - 1].setVisible(false);
                textFields[button.length - 1].setVisible(false);
                textFields[buttons.length - 1].setVisible(false);
                textFields[buttons.length - 1].setReadOnly(false);
                textFields[buttons.length - 1].setWidth(100, Unit.PERCENTAGE);
                button[buttons.length - 1].setVisible(false);
                cancel[button.length - 1].setVisible(false);

                LDAP_ADD.addClickListener(event -> {

                    for (int j = 0; j < buttons.length; j++) {
                        textFields[j].setVisible(false);
                        buttons[j].setVisible(false);
                        button[j].setVisible(false);

                    }

                    dialog[button.length - 1].open();
                    dialog[button.length - 1].setWidth(40, Unit.PERCENTAGE);
                    dialog[button.length -1].setHeaderTitle("Verzeichnis hinzufügen");
                    cancel[button.length - 1].setVisible(true);
                    cancel[button.length - 1].addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                    buttons[button.length - 1].setVisible(true);
                    textFields[button.length - 1].setVisible(true);
                    buttons[buttons.length - 1].setVisible(true);
                    buttons[buttons.length - 1].addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                    textFields[buttons.length - 1].setVisible(true);
                    textFields[buttons.length - 1].setReadOnly(false);
                    textFields[button.length - 1].setReadOnly(false);


                    buttons[buttons.length - 1].addClickShortcut(Key.ENTER);
                    cancel[button.length - 1].addClickListener(e1 -> dialog[button.length -1].close());

                    buttons[buttons.length - 1].addClickListener(Click -> {
                        textFields[buttons.length - 1].setMinLength(1);

                        if (textFields[buttons.length - 1].getValue().length() < textFields[buttons.length-1].getMinLength()){
                            textFields[buttons.length - 1].setInvalid(true);
                            textFields[buttons.length - 1].setErrorMessage("Dieses Feld darf NICHT Leer sein !");

                        } else {
                            dataBaseUtils.addNewIdAndName(textFields[textFields.length - 1].getValue());

                            Notification.show("Eintrag hinzugefügt");
                            textFields[buttons.length - 1].setValue("");
                            textFields[buttons.length - 1].setInvalid(false);
                            dialog[button.length -1].close();

                        }
                        textFields[buttons.length - 1].setMinLength(0);
                    });

                    dialog[button.length - 1].getFooter().add(buttons[button.length - 1]);
                    dialog[button.length - 1].getFooter().add(cancel[button.length - 1]);
                    dialog[button.length - 1].add(textFields[button.length - 1]);
                    textFields[buttons.length - 1].setLabel("Verzeichnis hinzufügen");
                    textFields[buttons.length - 1].setMinLength(1);
                    buttons[button.length - 1].setText("Speichern");
                    cancel[button.length - 1].setText("Abbrechen");

                });
            }
            //Übersicht
            {

                LDAP_Overview.addClickListener(event -> {

                    for (int i = 0; i < buttons.length; i++)
                    {
                        textFields[i].setVisible(true);
                        textFields[i].setReadOnly(true);
                        textFields[buttons.length - 1].setVisible(false);
                        textFields[textFields.length - 1].setVisible(false);


                }
                    for (int j = 0; j < buttons.length; j++)
                    {
                        textFields[j].setVisible(true);
                        buttons[j].setVisible(false);
                    }

                });
            }

            content.setAlignItems(FlexComponent.Alignment.CENTER);
            content.add(LDAP_EDIT, LDAP_ADD, LDAP_IDs, LDAP_Overview);
            for (int i = 0; i < buttons.length; i++) {
                content.add(textFields[i], buttons[i], button[i], delete[i], cancel[i], dialog[i]);
            }
        }
    }
}
