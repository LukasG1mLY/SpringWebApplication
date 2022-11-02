package de.rub.springwebapplication.MitabeiterView;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
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
import java.util.ArrayList;
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
            List<de.rub.springwebapplication.MitabeiterView.Eintrag> eintraege = dataBaseUtils.getAllInfos();

            Grid<de.rub.springwebapplication.MitabeiterView.Eintrag> lGrid = new Grid<>();
            lGrid.addColumn(de.rub.springwebapplication.MitabeiterView.Eintrag::getId).setHeader("ID");
            lGrid.addColumn(de.rub.springwebapplication.MitabeiterView.Eintrag::getContent).setHeader("Content");
            lGrid.setMaxWidth(50, Unit.PERCENTAGE);
            lGrid.setItems(eintraege);


            Button[] buttons = new Button[eintraege.size() + 1];
            Button[] button = new Button[eintraege.size() + 1];
            Button[] delete = new Button[eintraege.size() + 1];
            Button[] cancel = new Button[eintraege.size() + 1];
            Dialog[] dialog = new Dialog[eintraege.size() + 1];
            TextField[] textFields = new TextField[eintraege.size() + 1];

            MenuItem LDAP_IDs = LDAP_EDIT.addItem("Bearbeiten");
            MenuItem LDAP_ADD = LDAP_EDIT.addItem("Hinzufügen");
            MenuItem LDAP_Overview = LDAP_EDIT.addItem("Übersicht");

                for (int i = 0; i < buttons.length; i++) {

                    final int l = i;
                    buttons[l] = new Button("Bestätigen");
                    buttons[l].setVisible(false);

                    button[l] = new Button("Löschen");
                    button[l].setVisible(false);

                    delete[l] = new Button("Löschen");
                    delete[l].setVisible(false);

                    cancel[l] = new Button("Abbrechen");
                    cancel[l].setVisible(false);

                    textFields[l] = new TextField();
                    textFields[l].setVisible(false);
                    textFields[l].setWidth(40, Unit.PERCENTAGE);

                    dialog[l]  = new Dialog();

                    if (i != buttons.length - 1) {

                        SubMenu subItems = LDAP_IDs.getSubMenu();

                        MenuItem LDAP_ID = subItems.addItem("Verzeichnis_" + eintraege.get(l).getId()); {

                            LDAP_ID.addClickListener(event -> {
                                for (int j = 0; j < buttons.length; j++) {
                                    textFields[j].setVisible(false);
                                    buttons[j].setVisible(false);
                                    button[j].setVisible(false);

                                }

                                buttons[l].addClickShortcut(Key.ENTER);
                                buttons[l].setVisible(true);
                                button[l].setVisible(true);

                                delete[l].addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                                delete[l].getStyle().set("margin-right", "auto");

                                cancel[l].addThemeVariants(ButtonVariant.LUMO_TERTIARY);

                                textFields[l].setVisible(true);
                                textFields[l].setReadOnly(false);
                                textFields[l].setMinLength(1);
                                textFields[l].setValue(eintraege.get(l).getContent());
                                textFields[l].setLabel("Verzeichnis_" + eintraege.get(l).getId());
                                textFields[l].setPlaceholder(eintraege.get(l).getContent());

                                dialog[l].setWidth(40, Unit.PERCENTAGE);
                                dialog[l].setHeaderTitle(String.format("Möchten Sie Verzeichnis_ " + eintraege.get(l).getId() + " " + "Löschen ?"));
                                dialog[l].add("Sind Sie sicher, dass sie dieses Verzeichnis endgültig löschen wollen ?");
                                dialog[l].getFooter().add(delete[l]);
                                dialog[l].getFooter().add(cancel[l]);

                                button[l].addClickListener(Click -> {
                                    delete[l].setVisible(true);
                                    cancel[l].setVisible(true);
                                    dialog[l].open();

                                });

                                delete[l].addClickListener(Click -> {
                                    dataBaseUtils.deleteInfoLDAP(Integer.parseInt(eintraege.get(l).getId()));
                                    dialog[l].close();
                                    UI.getCurrent().getPage().reload();
                                });

                                cancel[l].addClickListener(Click -> dialog[l].close());

                                buttons[l].addClickListener(Click -> {
                                    if (textFields[l].getValue().length() < textFields[l].getMinLength()) {
                                        dialog[l].close();

                                    } else {
                                        dataBaseUtils.editInfoLDAP(l, textFields[l].getValue());
                                        textFields[l].setPlaceholder(eintraege.get(l).getContent());
                                        UI.getCurrent().getPage().reload();
                                    }
                                });
                            });

                            LDAP_Overview.addClickListener(event -> {

                                for (int j = 0; j < buttons.length; j++) {
                                    buttons[j].setVisible(false);
                                    button[j].setVisible(false);

                                }

                                textFields[l].setVisible(true);
                                textFields[l].setReadOnly(true);
                                textFields[l].setValue(eintraege.get(l).getContent());
                                textFields[l].setLabel("Verzeichnis " + eintraege.get(l).getId());
                            });

                        }
                    }
                }

                LDAP_ADD.addClickListener(event -> {

                dialog[buttons.length - 1].setVisible(true);
                buttons[buttons.length - 1].setVisible(false);
                textFields[buttons.length - 1].setVisible(false);
                textFields[buttons.length - 1].setVisible(false);
                button[buttons.length - 1].setVisible(false);
                cancel[buttons.length - 1].setVisible(false);

                dialog[buttons.length - 1].open();
                dialog[buttons.length - 1].setWidth(40, Unit.PERCENTAGE);
                dialog[buttons.length -1].setHeaderTitle("Verzeichnis hinzufügen");
                dialog[buttons.length - 1].getFooter().add(buttons[buttons.length - 1]);
                dialog[buttons.length - 1].getFooter().add(cancel[buttons.length - 1]);
                dialog[buttons.length - 1].add(textFields[buttons.length - 1]);

                cancel[buttons.length - 1].setVisible(true);
                cancel[buttons.length - 1].addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                cancel[buttons.length - 1].setText("Abbrechen");

                buttons[buttons.length - 1].setVisible(true);
                buttons[buttons.length - 1].setVisible(true);
                buttons[buttons.length - 1].addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                buttons[buttons.length - 1].addClickShortcut(Key.ENTER);
                buttons[buttons.length - 1].setText("Speichern");

                textFields[buttons.length - 1].setWidth(100, Unit.PERCENTAGE);
                textFields[buttons.length - 1].setVisible(true);
                textFields[buttons.length - 1].setReadOnly(false);
                textFields[buttons.length - 1].setLabel("Verzeichnis hinzufügen");
                textFields[buttons.length - 1].setMinLength(1);

                buttons[buttons.length - 1].addClickListener(Click -> {

                    if (textFields[buttons.length - 1].getValue().length() > textFields[buttons.length-1].getMinLength()) {

                        dataBaseUtils.addNewIdAndName(textFields[textFields.length - 1].getValue());
                        dialog[button.length -1].close();
                        UI.getCurrent().getPage().reload();
                    }
                    else {
                        dialog[button.length -1].close();
                    }

                    textFields[buttons.length - 1].setMinLength(1);
                });

                cancel[buttons.length - 1].addClickListener(Click -> dialog[buttons.length -1].close());

            });

            content.setAlignItems(FlexComponent.Alignment.CENTER);
            content.add(LDAP_EDIT, LDAP_ADD, LDAP_IDs, LDAP_Overview, lGrid);
            for (int i = 0; i < buttons.length; i++) {
                content.add(textFields[i], buttons[i], button[i], delete[i], cancel[i], dialog[i]);

            }
        }
    }
}
