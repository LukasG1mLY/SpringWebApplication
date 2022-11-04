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
import com.vaadin.flow.component.grid.GridVariant;
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
import java.util.List;

@PageTitle("MitabeiterView")
@Route("MitabeiterView")

public class MitabeiterView extends Div {

    public DataBaseUtils dataBaseUtils;
    private final Tab Startseite_WebClient;
    private final Tab Bearbeiten;
    private final Tab Logout;
    private final Tab LDAP;
    private final Tab Link;
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
        Link = new Tab(VaadinIcon.EDIT.create(), new Span("Link Liste"));


        Tabs tabs = new Tabs(Startseite_WebClient, Bearbeiten, LDAP, Link, Logout);
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
            List<LDAP> ldap = dataBaseUtils.getAllInfos();

            Grid<LDAP> lGrid = new Grid<>();
            lGrid.addColumn(de.rub.springwebapplication.MitabeiterView.LDAP::getId).setHeader("ID").setWidth("50px").setFlexGrow(0);
            lGrid.addColumn(de.rub.springwebapplication.MitabeiterView.LDAP::getContent).setHeader("Content").setAutoWidth(true);

            lGrid.setMaxWidth(100, Unit.PERCENTAGE);
            lGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            lGrid.setItems(ldap);
            lGrid.setVisible(false);


            Button[] buttons = new Button[ldap.size() + 1];
            Button[] button = new Button[ldap.size() + 1];
            Button[] delete = new Button[ldap.size() + 1];
            Button[] cancel = new Button[ldap.size() + 1];
            Dialog[] dialog = new Dialog[ldap.size() + 1];
            TextField[] textFields = new TextField[ldap.size() + 1];

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

                        MenuItem LDAP_ID = subItems.addItem("Verzeichnis_" + ldap.get(l).getId()); {

                            LDAP_ID.addClickListener(event -> {
                                for (int j = 0; j < buttons.length; j++) {
                                    textFields[j].setVisible(false);
                                    buttons[j].setVisible(false);
                                    button[j].setVisible(false);
                                    lGrid.setVisible(false);

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
                                textFields[l].setValue(ldap.get(l).getContent());
                                textFields[l].setLabel("Verzeichnis_" + ldap.get(l).getId());
                                textFields[l].setPlaceholder(ldap.get(l).getContent());

                                dialog[l].setWidth(40, Unit.PERCENTAGE);
                                dialog[l].setHeaderTitle(String.format("Möchten Sie Verzeichnis_ " + ldap.get(l).getId() + " " + "Löschen ?"));
                                dialog[l].add("Sind Sie sicher, dass sie dieses Verzeichnis endgültig löschen wollen ?");
                                dialog[l].getFooter().add(delete[l]);
                                dialog[l].getFooter().add(cancel[l]);

                                button[l].addClickListener(Click -> {
                                    delete[l].setVisible(true);
                                    cancel[l].setVisible(true);
                                    dialog[l].open();

                                });

                                delete[l].addClickListener(Click -> {
                                    dataBaseUtils.deleteInfoLDAP(Integer.parseInt(ldap.get(l).getId()));
                                    dialog[l].close();
                                    UI.getCurrent().getPage().reload();
                                });

                                cancel[l].addClickListener(Click -> dialog[l].close());

                                buttons[l].addClickListener(Click -> {
                                    if (textFields[l].getValue().length() < textFields[l].getMinLength()) {
                                        dialog[l].close();

                                    } else {
                                        dataBaseUtils.editInfoLDAP(l, textFields[l].getValue());
                                        textFields[l].setPlaceholder(ldap.get(l).getContent());
                                        UI.getCurrent().getPage().reload();
                                    }
                                });
                            });

                            LDAP_Overview.addClickListener(event -> {
                                for (int j = 0; j < buttons.length; j++) {
                                    buttons[j].setVisible(false);
                                    button[j].setVisible(false);
                                    textFields[j].setVisible(false);


                                }

                                lGrid.setVisible(true);

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
                textFields[buttons.length - 1].setMaxLength(400);
                buttons[buttons.length - 1].addClickListener(Click -> {

                    if (textFields[buttons.length - 1].getValue().length() > textFields[buttons.length-1].getMinLength()) {

                        dataBaseUtils.  addNewIdAndName(textFields[textFields.length - 1].getValue());
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

        else if (tab.equals(Link)) {


            List<Link> Link = dataBaseUtils.getAllInfos_Link();


            Grid<Link> Link_grid = new Grid<>();


            Link_grid.addColumn(de.rub.springwebapplication.MitabeiterView.Link::getId).setHeader("ID").setWidth("75px").setFlexGrow(0);
            Link_grid.addColumn(de.rub.springwebapplication.MitabeiterView.Link::getLinktext).setHeader("Link Text");
            Link_grid.addColumn(de.rub.springwebapplication.MitabeiterView.Link::getLink_grp_id).setHeader("Link_group_ID");
            Link_grid.addColumn(de.rub.springwebapplication.MitabeiterView.Link::getSort).setHeader("Sort");
            Link_grid.addColumn(de.rub.springwebapplication.MitabeiterView.Link::getDescription).setHeader("Description");
            Link_grid.addColumn(de.rub.springwebapplication.MitabeiterView.Link::getUrl_active).setHeader("URL_Active");
            Link_grid.addColumn(de.rub.springwebapplication.MitabeiterView.Link::getUrl_inactive).setHeader("URL_InActive");
            Link_grid.addColumn(de.rub.springwebapplication.MitabeiterView.Link::getActive).setHeader("Active");
            Link_grid.addColumn(de.rub.springwebapplication.MitabeiterView.Link::getAuth_level).setHeader("Auth_Level");
            Link_grid.addColumn(de.rub.springwebapplication.MitabeiterView.Link::getNewtab).setHeader("NewTab");
            Link_grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
            Link_grid.setItems(Link);
            Link_grid.setVisible(false);

            MenuBar Link_EDIT = new MenuBar();

            MenuItem Link_IDs = Link_EDIT.addItem("Bearbeiten");
            MenuItem Link_ADD = Link_EDIT.addItem("Hinzufügen");
            MenuItem Link_Overview = Link_EDIT.addItem("Übersicht");

            Button[] buttons = new Button[Link.size() + 1];
            Button[] button = new Button[Link.size() + 1];
            Button[] delete = new Button[Link.size() + 1];
            Button[] cancel = new Button[Link.size() + 1];
            Dialog[] dialog = new Dialog[Link.size() + 1];
            TextField[] Linktext = new TextField[Link.size() + 1];
            TextField[] Description = new TextField[Link.size() + 1];
            TextField[] URL_ACTIVE = new TextField[Link.size() + 1];
            TextField[] URL_INACTIVE = new TextField[Link.size() + 1];



            for (int i = 0; i < buttons.length; i++) {

                final int l = i;
                buttons[l] = new Button("Bestätigen");
                buttons[l].setVisible(false);

                button[l] = new Button("Erweiterte Einstellungen");
                button[l].setVisible(false);

                delete[l] = new Button("Löschen");
                delete[l].addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                delete[l].setVisible(false);

                cancel[l] = new Button("Abbrechen");
                cancel[l].addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                cancel[l].setVisible(false);

                Linktext[l] = new TextField("Linktext");
                Linktext[l].setVisible(false);
                Linktext[l].setWidth(100, Unit.PERCENTAGE);

                Description[l] = new TextField("Description");
                Description[l].setVisible(false);
                Description[l].setWidth(100, Unit.PERCENTAGE);

                URL_ACTIVE[l] = new TextField("URL_ACTIVE");
                URL_ACTIVE[l].setVisible(false);
                URL_ACTIVE[l].setWidth(100, Unit.PERCENTAGE);

                URL_INACTIVE[l] = new TextField("URL_INACTIVE");
                URL_INACTIVE[l].setVisible(false);
                URL_INACTIVE[l].setWidth(100, Unit.PERCENTAGE);


                dialog[l]  = new Dialog();



                if (i != buttons.length - 1) {

                    SubMenu subItems = Link_IDs.getSubMenu();

                    MenuItem Link_ID = subItems.addItem("Verzeichnis_" + Link.get(l).getId()); {


                        Link_ID.addClickListener(event -> {

                            Linktext[l].setValue(Link.get(l).Linktext);
                            Description[l].setValue(Link.get(l).Description);
                            URL_ACTIVE[l].setValue(Link.get(l).Url_active);
                            URL_INACTIVE[l].setValue(Link.get(l).Url_inactive);

                            VerticalLayout dialogLayout = new VerticalLayout(Linktext[l], Description[l], URL_ACTIVE[l], URL_INACTIVE[l]);
                            dialogLayout.setPadding(false);
                            dialogLayout.setSpacing(false);
                            dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
                            dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

                            dialog[l].open();
                            cancel[l].setVisible(true);
                            delete[l].setVisible(true);
                            Linktext[l].setVisible(true);
                            Description[l].setVisible(true);
                            URL_ACTIVE[l].setVisible(true);
                            URL_INACTIVE[l].setVisible(true);
                            dialog[l].add(dialogLayout);
                            dialog[l].setHeaderTitle("Verzeichnis_" + Link.get(l).getId());
                            dialog[l].getFooter().add(delete[l], cancel[l]);
                        });


                        Link_Overview.addClickListener(event -> {
                            for (int j = 0; j < buttons.length; j++) {
                                buttons[j].setVisible(false);
                                button[j].setVisible(false);
                                Linktext[j].setVisible(false);


                            }

                            Link_grid.setVisible(true);

                        });

                    }
                }
            }


            content.setAlignItems(FlexComponent.Alignment.CENTER);
            content.add(Link_EDIT, Link_IDs, Link_ADD, Link_Overview, Link_grid);
            for (int i = 0; i < buttons.length; i++) {
                content.add(Linktext[i], buttons[i], button[i], delete[i], cancel[i], dialog[i]);
            }
        }

    }
}
