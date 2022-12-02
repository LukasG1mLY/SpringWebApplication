package de.rub.springwebapplication.MitabeiterView;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.WebBrowser;
import de.rub.springwebapplication.Data.DatabaseUtils;
import de.rub.springwebapplication.Listen.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static java.lang.Integer.parseInt;

@PageTitle("MitabeiterView")
@Route("MitabeiterView")

public class MitabeiterView extends Div {

    public DatabaseUtils dataBaseUtils;
    private final Tab Startseite_WebClient;
    private final Tab Bearbeiten;
    private final VerticalLayout content;

    public MitabeiterView() throws IOException {

        final WebBrowser webBrowser = UI.getCurrent().getSession().getBrowser();
        System.out.println("IP: " + webBrowser.getAddress() + " " + "Opened: " + getClass());

        dataBaseUtils = new DatabaseUtils();
        content = new VerticalLayout();
        Startseite_WebClient = new Tab(VaadinIcon.HOME.create(), new Span("Startseite WebClient"));
        Tab logout = new Tab(VaadinIcon.ARROW_LEFT.create(), new Span("Logout"));
        Bearbeiten = new Tab(VaadinIcon.EDIT.create(), new Span("Datenbank Tabellen Bearbeiten"));

        Tabs tabs = new Tabs(Startseite_WebClient, Bearbeiten, logout);
        tabs.addSelectedChangeListener(event -> setContent(event.getSelectedTab()));
        setContent(tabs.getSelectedTab());

        add(tabs, content);
    }
    private void setContent(@NotNull Tab tab) {
        content.removeAll();

        if (tab.equals(Startseite_WebClient)) {
            content.setAlignItems(FlexComponent.Alignment.CENTER);
            content.add(new Image("images/Icon1.png", "images/RUB.png"));
            H1 Titel = new H1("Startseite eCampus Webclient");
            Paragraph Info_Text_Studenten = new Paragraph();
            Info_Text_Studenten.setText(dataBaseUtils.getInfoStaff());

            content.add(Titel, Info_Text_Studenten);

        }
        else if (tab.equals(Bearbeiten)) {
            MenuBar menuBar = new MenuBar();
            MenuItem Infobox_item = menuBar.addItem("Infotext");
            MenuItem Ldap_item = menuBar.addItem("Ldap");
            MenuItem Link_item = menuBar.addItem("Link");
            MenuItem Ldap_Role_item = menuBar.addItem("Ldap Role");
            MenuItem Link_Grp_item = menuBar.addItem("Link Group");
            MenuItem Link_Tile_item = menuBar.addItem("Link Tile");
            MenuItem Icon_item = menuBar.addItem("Icon");

            Infobox_item.addClickListener(e -> {
                List<InfoBox> InfoBox = dataBaseUtils.getInfo_InfoBox();
                Grid<InfoBox> InfoBox_grid = new Grid<>();
                H2 H2 = new H2("Verzeichnis-Liste: Infobox");H2.getStyle().set("margin", "0 auto 0 0");
                Button cancelButton = new Button("Nein, Abbrechen");cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button editButton = new Button("Bearbeiten");editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button saveButton = new Button("Speichern");saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                TextField tf1 = new TextField("Mitabeiter");tf1.setWidthFull();
                TextField tf2 = new TextField("Studenten");tf2.setWidthFull();
                VerticalLayout dialogLayout = new VerticalLayout(tf1, tf2);dialogLayout.setPadding(false);dialogLayout.setSpacing(false);
                HorizontalLayout heading = new HorizontalLayout(H2, closeButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                Dialog gridDialog = new Dialog();gridDialog.open();gridDialog.setWidthFull();gridDialog.setCloseOnOutsideClick(false);gridDialog.getFooter().add(editButton);
                Dialog editDialog = new Dialog();editDialog.setHeaderTitle("Bearbeiten");editDialog.setCloseOnOutsideClick(false);editDialog.getFooter().add(saveButton);

                InfoBox_grid.addColumn(de.rub.springwebapplication.Listen.InfoBox::getRolle).setHeader("Rolle");
                InfoBox_grid.addColumn(de.rub.springwebapplication.Listen.InfoBox::getInfo).setHeader("Info");
                InfoBox_grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);
                InfoBox_grid.setSelectionMode(Grid.SelectionMode.NONE);
                InfoBox_grid.getDataProvider().refreshAll();
                InfoBox_grid.setItems(InfoBox);

                editButton.addClickListener(Click -> {
                    tf1.setValue(dataBaseUtils.getInfoStaff());
                    tf2.setValue(dataBaseUtils.getInfoStudent());

                    editDialog.open();
                    editDialog.add(tf1, tf2);
                });
                saveButton.addClickListener(Click -> {
                    if (tf1.isEmpty()) {
                        tf1.setValue(dataBaseUtils.getInfoStaff());
                    }
                    if (tf2.isEmpty()) {
                        tf2.setValue(dataBaseUtils.getInfoStudent());
                    }
                    dataBaseUtils.editInfoStaff(tf1.getValue());
                    dataBaseUtils.editInfoStudent(tf2.getValue());
                    editDialog.close();
                    gridDialog.close();
                    Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                });
                closeButton.addClickListener(Click -> {
                    gridDialog.close();
                    editDialog.close();
                });

                gridDialog.add(heading, InfoBox_grid);
            });
            Ldap_item.addClickListener(e -> {
                List<Ldap> Ldap = dataBaseUtils.getInfo_Ldap();
                Grid<Ldap> Ldap_grid = new Grid<>();
                Label info = new Label("WARNUNG Dieser Vorgang kann nicht rückgängig gemacht werden");info.getStyle().set("color", "red");
                H2 H2 = new H2("Verzeichnis-Liste: Ldap");H2.getStyle().set("margin", "0 auto 0 0");
                H2 H3 = new H2("");H2.getStyle().set("margin", "0 auto 0 0");
                Button saveButton = new Button("Speichern");saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button saveButton1 = new Button("Speichern");saveButton1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button db1 = new Button("Löschen");db1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                Button cancelButton = new Button("Nein, Abbrechen");cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button createButton = new Button("Hinzufügen");createButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);maximizeButton.addClickListener(Click -> Ldap_grid.setAllRowsVisible(true));
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);minimizeButton.addClickListener(Click -> Ldap_grid.setAllRowsVisible(false));
                Button deleteButton2 = new Button("Löschen");deleteButton2.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                TextField tf1 = new TextField("Gruppen Name");tf1.setWidthFull();
                VerticalLayout dialogLayout = new VerticalLayout(tf1);dialogLayout.setPadding(false);dialogLayout.setSpacing(false);dialogLayout.setSizeFull();
                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout tools = new HorizontalLayout(H3, createButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                Dialog gridDialog = new Dialog();gridDialog.open();gridDialog.setCloseOnOutsideClick(false);gridDialog.setWidthFull();
                Dialog deleteDialog = new Dialog();deleteDialog.setHeaderTitle("Verzeichnis Löschen ?");deleteDialog.add(info);deleteDialog.setCloseOnOutsideClick(false);
                Dialog editDialog = new Dialog();editDialog.setHeaderTitle("Verzeichnis Bearbeiten");editDialog.setCloseOnOutsideClick(false);editDialog.add(tf1);editDialog.setWidth(60, Unit.PERCENTAGE);
                Dialog createDialog = new Dialog();createDialog.setHeaderTitle("Verzeichnis erstellen");createDialog.setCloseOnOutsideClick(false);createDialog.getFooter().add(saveButton,cancelButton);createDialog.add(tf1);createDialog.setWidth(60, Unit.PERCENTAGE);

                Ldap_grid.addColumn(de.rub.springwebapplication.Listen.Ldap::getId).setHeader("ID").setFlexGrow(0);
                Ldap_grid.addColumn(de.rub.springwebapplication.Listen.Ldap::getContent).setHeader("Content").setAutoWidth(true);
                Ldap_grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
                Ldap_grid.setItems(Ldap);

                Ldap_grid.addComponentColumn(Tools -> {int i = parseInt(Tools.getId());

                    Button deleteButton = new Button(VaadinIcon.TRASH.create()); deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                    Button editButton = new Button(VaadinIcon.EDIT.create());editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

                    editButton.addClickListener(Click -> {
                        tf1.setValue(Tools.getContent());
                        editDialog.open();
                        editDialog.add(tf1);
                        editDialog.getFooter().add(saveButton1);
                        saveButton1.addClickListener(click -> {
                            if (tf1.isEmpty()) {
                                tf1.setValue("N/A");
                            }
                            dataBaseUtils.editInfoLDAP(i, tf1.getValue());tf1.setValue("Unsichtbar");
                            gridDialog.close();deleteDialog.close();editDialog.close();
                            Ldap_grid.getDataProvider().refreshAll();

                            Notification.show("Erfolgreich Geändert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                    });
                    deleteButton.addClickListener(Click -> {
                        deleteDialog.open();
                        deleteDialog.getFooter().add(deleteButton2, cancelButton);
                        deleteButton2.addClickListener(click -> {
                            dataBaseUtils.deleteInfoLDAP(parseInt(Tools.getId()));
                            gridDialog.close();deleteDialog.close();editDialog.close();

                            Ldap_grid.getDataProvider().refreshAll();

                            Notification.show("Erfolgreich Gelöscht", 5000, Notification.Position.TOP_CENTER);
                        });
                    });
                    return new HorizontalLayout(editButton, deleteButton);
                }).setFlexGrow(0);
                cancelButton.addClickListener(Click -> {
                    gridDialog.close();
                    deleteDialog.close();
                    editDialog.close();
                });
                createButton.addClickListener(Click -> {
                    createDialog.open();
                    saveButton.addClickListener(click -> {
                        if (tf1.isEmpty()) {
                        tf1.setValue("N/A");
                    }
                        dataBaseUtils.addNewIdAndName_Ldap(tf1.getValue());
                        Ldap_grid.getDataProvider().refreshAll();
                        gridDialog.close();deleteDialog.close();createDialog.close();
                        Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                    });

                });
                closeButton.addClickListener(Click -> {
                    gridDialog.close();
                    deleteDialog.close();
                    editDialog.close();
                    createDialog.close();
                });

                gridDialog.add(heading, tools, Ldap_grid);
            });
            Link_item.addClickListener(e -> {

                Grid<Link> Link_grid = new Grid<>();
                List<Link> list = dataBaseUtils.getAll();
                List<Link_grp_Id> List = dataBaseUtils.getInfoLink_Grp_Id();
                List<Links> links = dataBaseUtils.getLinks();
                Label info = new Label("WARNUNG Dieser Vorgang kann nicht rückgängig gemacht werden");info.getStyle().set("color", "red");
                H2 H2 = new H2("Verzeichnis-Liste: Link");H2.getStyle().set("margin", "0 auto 0 0");
                H2 H3 = new H2("");H2.getStyle().set("margin", "0 auto 0 0");
                Button sb1 = new Button("Speichern");sb1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button sb2 = new Button("Speichern");sb2.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button deleteButton1 = new Button("Löschen");deleteButton1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                Button cancelButton = new Button("Nein, Abbrechen");cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button createButton = new Button("Hinzufügen");createButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);maximizeButton.addClickListener(Click -> Link_grid.setAllRowsVisible(true));
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);minimizeButton.addClickListener(Click -> Link_grid.setAllRowsVisible(false));
                Button addLinkGroup = new Button("Link Group Hinzufügen");addLinkGroup.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);addLinkGroup.setWidthFull();
                TextField tf1 = new TextField("Linktext");tf1.setWidthFull();
                TextField tf4 = new TextField("Description");tf4.setWidthFull();
                ComboBox<Links> tf5 = new ComboBox<>("URL Active");tf5.setItems(links);tf5.setItemLabelGenerator(Links::getLinks);tf5.setWidthFull();
                ComboBox<Link_grp_Id> tf2 = new ComboBox<>("Link Group");tf2.setItems(List);tf2.setItemLabelGenerator(Link_grp_Id::getGrp_Linktext);tf2.setWidthFull();
                Checkbox tf6 = new Checkbox();tf6.setLabel("URL Inaktiv");tf6.setWidthFull();
                Checkbox tf7 = new Checkbox();tf7.setLabel("URL Aktivieren");tf7.setWidthFull();
                Checkbox tf9 = new Checkbox();tf9.setLabel("Neuen Tab öffnen");tf9.setWidthFull();
                NumberField tf3 = new NumberField("Sort(Number Only)");tf3.setWidthFull();
                NumberField tf8 = new NumberField("Authenticator Level");tf8.setWidthFull();
                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout tools = new HorizontalLayout(H3, createButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout Checkbox = new HorizontalLayout(tf6, tf7, tf9);
                VerticalLayout dialogLayout = new VerticalLayout(tf1, tf2,tf5, tf3, tf4, tf8, Checkbox);dialogLayout.setPadding(false);dialogLayout.setSpacing(false);dialogLayout.setWidthFull();
                Dialog gridDialog = new Dialog();gridDialog.open();gridDialog.setCloseOnOutsideClick(false);gridDialog.setWidthFull();
                Dialog deleteDialog = new Dialog();deleteDialog.setHeaderTitle("Verzeichnis Löschen ?");deleteDialog.add("Dieser Vorgang kann nicht Rückgänig gemacht werden !");deleteDialog.getFooter().add(deleteButton1, cancelButton);deleteDialog.setCloseOnOutsideClick(false);
                Dialog editDialog = new Dialog();editDialog.setCloseOnOutsideClick(false);editDialog.setHeaderTitle("Verzeichnis bearbeiten");editDialog.setWidth(60, Unit.PERCENTAGE);
                Dialog createDialog = new Dialog();createDialog.setCloseOnOutsideClick(false);createDialog.setHeaderTitle("Verzeichnis erstellen");createDialog.add(dialogLayout);createDialog.getFooter().add(sb2, cancelButton);createDialog.setWidth(60, Unit.PERCENTAGE);

                Link_grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
                Link_grid.setAllRowsVisible(false);
                Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getId).setHeader("ID").setSortable(true).setAutoWidth(true);
                Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getLinktext).setHeader("Linktext").setSortable(true).setAutoWidth(true);
                Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getGrp_Linktext).setHeader("Grp Linktext").setSortable(true).setAutoWidth(true);
                Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getSort).setHeader("Sort").setSortable(true).setAutoWidth(true);
                Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getDescription).setHeader("Description").setSortable(true).setAutoWidth(true);
                Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getUrl_active).setHeader("Link").setSortable(true).setAutoWidth(true).setKey("Link");
                Link_grid.setItems(list);
                Link_grid.setVisible(gridDialog.isOpened());
                Link_grid.addComponentColumn(Tools -> {

                    int i = parseInt(Tools.getId());

                    Button deleteButton = new Button(VaadinIcon.TRASH.create());deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                    Button editButton = new Button(VaadinIcon.EDIT.create());editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

                    editButton.addClickListener(Click -> {
                        editDialog.open();
                        try {
                            tf1.setValue(Tools.getLinktext());
                        } catch (NullPointerException npe) {
                            tf1.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        try {
                           tf2.setPlaceholder(Tools.getGrp_Linktext());
                        } catch (NullPointerException npe) {
                            tf2.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        try {
                            tf3.setValue(Double.valueOf(Tools.getSort()));
                        } catch (NullPointerException npe) {
                            tf3.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        try {
                            tf4.setValue((Tools.getDescription()));
                        } catch (NullPointerException npe) {
                            tf4.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        try {
                            tf5.setPlaceholder(Tools.getUrl_active());
                        } catch (NullPointerException npe) {
                            tf5.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        try {
                            if (Objects.equals(Tools.getUrl_inactive(), String.valueOf(1))) {
                                tf6.setValue(true);
                            }
                            else {
                                tf6.setValue(false);
                            }
                        } catch (NullPointerException npe) {
                            npe.printStackTrace();
                        }
                        try {
                            if (Objects.equals(Tools.getActive(), String.valueOf(1))) {
                                tf7.setValue(true);
                            }
                            else {
                                tf7.setValue(false);
                            }
                        } catch (NullPointerException npe) {
                            npe.printStackTrace();
                        }
                        try {
                            tf8.setValue(Double.valueOf(Tools.getAuth_level()));
                        } catch (NullPointerException npe) {
                            tf8.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        try {
                            if (Objects.equals(Tools.getNewtab(), String.valueOf(1))) {
                                tf9.setValue(true);
                            }
                            else {
                                tf9.setValue(false);
                            }
                        } catch (NullPointerException npe) {
                            npe.printStackTrace();
                        }

                        editDialog.add(dialogLayout);editDialog.getFooter().add(sb1, cancelButton);

                        sb1.addClickListener(click -> {
                            if (tf2.isEmpty()) {
                                Notification.show("Sie haben im Feld 'Link Group' keine Angabe gemacht, bitte korrigieren").addThemeVariants(NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_ERROR);
                                return;
                            }
                            if (tf5.isEmpty()) {
                                Notification.show("Sie haben im Feld 'URL Active' keine Angabe gemacht, bitte korrigieren").addThemeVariants(NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_ERROR);
                                return;
                            }
                            int tf6_info;
                            if (tf6.getValue().equals(true)) {
                                tf6_info = 1;
                            } else {
                                tf6_info = 0;
                            }
                            int tf7_info;
                            if (tf7.getValue().equals(true)) {
                               tf7_info = 1;
                            } else {
                                tf7_info = 0;
                            }
                            int tf9_info;
                            if (tf9.getValue().equals(true)) {
                                tf9_info = 1;
                            } else {
                                tf9_info = 0;
                            }

                            dataBaseUtils.editInfoLink(i, tf1.getValue(), tf2.getValue().getId(), tf3.getValue(), tf4.getValue(), tf5.getValue().getLinks(), tf6_info, tf7_info, tf8.getValue(), tf9_info);
                            tf1.setValue("Wird Geändert");
                            gridDialog.close();deleteDialog.close();editDialog.close();
                            Link_grid.getDataProvider().refreshAll();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                    });
                    deleteButton.addClickListener(Click -> {
                        deleteDialog.open();
                        deleteDialog.getFooter().add(cancelButton);
                        deleteButton1.addClickListener(click -> {
                            dataBaseUtils.deleteInfoLink(parseInt(Tools.getId()));
                            gridDialog.close();deleteDialog.close();editDialog.close();createDialog.close();
                            Link_grid.getDataProvider().refreshAll();

                            Notification.show("Erfolgreich Verzeichnis "+Tools.getId()+" Gelöscht", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                    });
                    return new HorizontalLayout(editButton, deleteButton);
                }).setFlexGrow(0);

                cancelButton.addClickListener(Click -> {
                    gridDialog.close();deleteDialog.close();editDialog.close();createDialog.close();
                });
                createButton.addClickListener(Click -> {
                    createDialog.open();
                    sb2.addClickListener(click -> {
                        if (tf1.isEmpty()) {
                            tf1.setValue("N/A");
                        }
                        if (tf3.isEmpty()) {
                            tf3.setValue(Double.valueOf("1"));
                        }
                        if (tf4.isEmpty()) {
                            tf4.setValue("N/A");
                        }
                        if (tf6.isEmpty()) {
                            tf6.setValue(false);
                        }
                        if (tf7.isEmpty()) {
                            tf7.setValue(false);
                        }
                        if (tf8.isEmpty()) {
                            tf8.setValue(Double.valueOf("1"));
                        }
                        if (tf9.isEmpty()) {
                            tf9.setValue(false);
                        }
                        if (tf2.isEmpty()) {
                            Notification.show("Sie haben im Feld 'Link Group' keine Angabe gemacht, bitte korrigieren").addThemeVariants(NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_ERROR);
                            return;
                        }
                        if (tf5.isEmpty()) {
                            Notification.show("Sie haben im Feld 'URL Active' keine Angabe gemacht, bitte korrigieren").addThemeVariants(NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_ERROR);
                            return;
                        }
                        Integer tf2_Info = tf2.getValue().getId();
                        dataBaseUtils.addNewIdAndName_Link(tf1.getValue(), tf2_Info, tf3.getValue(), tf4.getValue(), tf5.getValue().getLinks(), tf6.getValue(), tf7.getValue(), tf8.getValue(), tf9.getValue());
                        Link_grid.getDataProvider().refreshAll();
                        gridDialog.close();deleteDialog.close();editDialog.close();createDialog.close();
                        Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                    });
                });
                closeButton.addClickListener(Click -> {
                    gridDialog.close();deleteDialog.close();editDialog.close();createDialog.close();
                });
                gridDialog.add(heading, tools, Link_grid);
            });
            Ldap_Role_item.addClickListener(e -> {
                List<de.rub.springwebapplication.Listen.LDAP_ROLE> LdapRole = dataBaseUtils.getAllInfos_LDAP_ROLE();
                Grid<LDAP_ROLE> LdapRole_grid = new Grid<>();
                Label info = new Label("WARNUNG Dieser Vorgang kann nicht rückgängig gemacht werden");info.getStyle().set("color", "red");
                H2 H2 = new H2("Verzeichnis-Liste: Ldap Role");H2.getStyle().set("margin", "0 auto 0 0");
                H2 H3 = new H2("");H2.getStyle().set("margin", "0 auto 0 0");
                Button sb1 = new Button("Speichern");sb1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button sb2 = new Button("Speichern");sb2.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button db1 = new Button("Ja, Löschen");db1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                Button cancelButton = new Button("Nein, Abbrechen");cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button createButton = new Button("Hinzufügen");createButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);maximizeButton.addClickListener(Click -> LdapRole_grid.setAllRowsVisible(true));
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);  minimizeButton.addClickListener(Click -> LdapRole_grid.setAllRowsVisible(false));
                TextField tf1 = new TextField("Role Name");tf1.setWidthFull();
                VerticalLayout dialogLayout = new VerticalLayout(tf1);dialogLayout.setPadding(false);dialogLayout.setSpacing(false);dialogLayout.setSizeFull();
                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout tools = new HorizontalLayout(H3, createButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                Dialog gridDialog = new Dialog();gridDialog.open();gridDialog.setCloseOnOutsideClick(false);gridDialog.setWidthFull();gridDialog.getFooter().add();
                Dialog deleteDialog = new Dialog();deleteDialog.setWidth(60, Unit.PERCENTAGE);deleteDialog.setHeaderTitle("Verzeichnis Löschen ?");deleteDialog.setCloseOnOutsideClick(false);deleteDialog.add("Dieser Vorgang kann nicht Rückgänig gemacht werden !");
                Dialog editDialog = new Dialog();editDialog.setWidth(60, Unit.PERCENTAGE);editDialog.add(dialogLayout);editDialog.setCloseOnOutsideClick(false);editDialog.getFooter().add(sb2, cancelButton);
                Dialog createDialog = new Dialog();createDialog.setWidth(60, Unit.PERCENTAGE);createDialog.add(dialogLayout);createDialog.setCloseOnOutsideClick(false);createDialog.getFooter().add(sb1, cancelButton);

                LdapRole_grid.addColumn(de.rub.springwebapplication.Listen.LDAP_ROLE::getId).setHeader("ID").setWidth("75px");
                LdapRole_grid.addColumn(de.rub.springwebapplication.Listen.LDAP_ROLE::getContent).setHeader("Role Name").setFlexGrow(1);
                LdapRole_grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
                LdapRole_grid.setItems(LdapRole);
                LdapRole_grid.getId();

                LdapRole_grid.addComponentColumn(Tools -> {
                    Button deleteButton = new Button(VaadinIcon.TRASH.create());deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                    Button editButton = new Button(VaadinIcon.EDIT.create());editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

                    editButton.addClickListener(Click -> {
                        tf1.setValue(Tools.getContent());
                        editDialog.open();
                        editDialog.add(tf1);
                        sb2.addClickListener(click -> {
                            int i = parseInt(Tools.getId());
                            if (tf1.isEmpty()) {
                                tf1.setValue("N/A");
                            }
                            dataBaseUtils.editInfoLDAP_ROLE(i, tf1.getValue());
                            LdapRole_grid.getDataProvider().refreshAll();
                            gridDialog.close();deleteDialog.close();editDialog.close();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });

                    });
                    deleteButton.addClickListener(Click -> {
                        deleteDialog.open();
                        deleteDialog.getFooter().add(db1, cancelButton);
                        db1.addClickListener(click -> {
                            dataBaseUtils.deleteInfoLDAP_ROLE(parseInt(Tools.getId()));
                            LdapRole_grid.getDataProvider().refreshAll();
                            gridDialog.close();deleteDialog.close();editDialog.close();
                            Notification.show("Erfolgreich Verzeichnis "+Tools.getId()+" gelöscht", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                    });
                    return new HorizontalLayout(editButton, deleteButton);
                }).setFlexGrow(0);
                cancelButton.addClickListener(Click -> {
                    editDialog.close();deleteDialog.close();gridDialog.close();createDialog.close();
                });
                createButton.addClickListener(Click -> {
                    createDialog.open();
                    sb1.addClickListener(click -> {
                        if (tf1.isEmpty()) {
                            tf1.setValue("N/A");
                    }
                        dataBaseUtils.addNewIdAndName_ROLE(tf1.getValue());
                        gridDialog.close();deleteDialog.close();editDialog.close();createDialog.close();
                        Notification.show("Erfolgreich Gespeichert",5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        LdapRole_grid.getDataProvider().refreshAll();
                    });
                });
                closeButton.addClickListener(Click -> {
                    gridDialog.close();deleteDialog.close();gridDialog.close();createDialog.close();
                });

                gridDialog.add(heading, tools, LdapRole_grid);
            });
            Link_Grp_item.addClickListener(e -> {
                List<Link_grp> LinkGrp = dataBaseUtils.getInfo_Link_Grp();
                Grid<Link_grp> LinkGrp_grid = new Grid<>();
                Label info = new Label("WARNUNG Dieser Vorgang kann nicht rückgängig gemacht werden");info.getStyle().set("color", "red");
                H2 H2 = new H2("Verzeichnis-Liste: Link Group");H2.getStyle().set("margin", "0 auto 0 0");
                H2 H3 = new H2("");H2.getStyle().set("margin", "0 auto 0 0");
                Button sb1 = new Button("Speichern");sb1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button sb2 = new Button("Speichern");sb1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button db1 = new Button("Ja, Löschen");db1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                Button cancelButton = new Button("Nein, Abbrechen");cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button createButton = new Button("Hinzufügen");createButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);maximizeButton.addClickListener(Click -> LinkGrp_grid.setAllRowsVisible(true));
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY); minimizeButton.addClickListener(Click -> LinkGrp_grid.setAllRowsVisible(false));
                TextField tf1 = new TextField("Linktext Gruppe");tf1.setWidthFull();
                TextField tf5 = new TextField("Beschreibung");tf5.setWidthFull();
                NumberField tf2 = new NumberField("Symbol Id");tf2.setWidthFull();
                NumberField tf3 = new NumberField("Kachel Id");tf3.setWidthFull();
                NumberField tf4 = new NumberField("Sortieren");tf4.setWidthFull();
                VerticalLayout dialogLayout = new VerticalLayout(tf1, tf2, tf3, tf4, tf5);dialogLayout.setPadding(false);dialogLayout.setSpacing(false);dialogLayout.setSizeFull();
                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout tools = new HorizontalLayout(H3, createButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                Dialog gridDialog = new Dialog();gridDialog.open();gridDialog.setCloseOnOutsideClick(false);gridDialog.setWidthFull();gridDialog.add(heading, tools, LinkGrp_grid);
                Dialog deleteDialog = new Dialog();deleteDialog.setWidth(60, Unit.PERCENTAGE);
                Dialog editDialog = new Dialog();editDialog.setWidth(60, Unit.PERCENTAGE);editDialog.add(tf1, tf2, tf3, tf4, tf5);editDialog.getFooter().add(sb2, cancelButton);
                Dialog createDialog = new Dialog();createDialog.setWidth(60, Unit.PERCENTAGE);

                LinkGrp_grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);
                LinkGrp_grid.addColumn(Link_grp::getId).setHeader("ID").setWidth("75px").setFlexGrow(0);
                LinkGrp_grid.addColumn(Link_grp::getGrp_Linktext).setHeader("Linktext_Grp");
                LinkGrp_grid.addColumn(Link_grp::getIcon_Id).setHeader("Icon_Id");
                LinkGrp_grid.addColumn(Link_grp::getTile_Id).setHeader("Tile_Id");
                LinkGrp_grid.addColumn(Link_grp::getSort).setHeader("Sort");
                LinkGrp_grid.addColumn(Link_grp::getDescription).setHeader("Description");
                LinkGrp_grid.addComponentColumn(Tools -> {

                    Button deleteButton = new Button(VaadinIcon.TRASH.create());deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                    Button editButton = new Button(VaadinIcon.EDIT.create());editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);


                    editButton.addClickListener(Click -> {
                        editDialog.open();
                        tf1.setValue(Tools.getGrp_Linktext());
                        try {
                            tf5.setValue(Tools.getDescription());
                        } catch (NullPointerException npe) {
                            tf5.setPlaceholder("Momentan keine Beschreibung Vorhanden");
                        }
                        tf2.setValue(Double.valueOf(Tools.getIcon_Id()));
                        tf3.setValue(Double.valueOf(Tools.getTile_Id()));
                        tf4.setValue(Double.valueOf(Tools.getSort()));
                        sb2.addClickListener(click -> {
                            final int i;
                            i = parseInt(Tools.getId());
                            if (tf1.isEmpty()) {
                                tf1.setValue("N/A");
                            }
                            if (tf2.isEmpty()) {
                                tf2.setValue(Double.valueOf("1"));
                            }
                            if (tf3.isEmpty()) {
                                tf3.setValue(Double.valueOf("1"));
                            }
                            if (tf4.isEmpty()) {
                                tf4.setValue(Double.valueOf("1"));
                            }
                            if (tf5.isEmpty()) {
                                tf5.setValue("N/A");
                            }

                            dataBaseUtils.editInfoLink_Grp(i, tf1.getValue(), tf2.getValue(), tf3.getValue(), tf4.getValue(), tf5.getValue());
                            LinkGrp_grid.getDataProvider().refreshAll();
                            gridDialog.close();deleteDialog.close();editDialog.close();createDialog.close();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                    });
                    deleteButton.addClickListener(Click -> {
                        final int i;
                        i = parseInt(Tools.getId());
                        deleteDialog.open();
                        deleteDialog.setHeaderTitle("Verzeichnis " + i);
                        deleteDialog.setCloseOnOutsideClick(false);
                        deleteDialog.add("Dieser Vorgang kann nicht Rückgänig gemacht werden !");
                        deleteDialog.getFooter().add(db1, cancelButton);
                        db1.addClickListener(click -> {
                            dataBaseUtils.deleteInfoLink_Grp(parseInt(Tools.getId()));
                            LinkGrp_grid.getDataProvider().refreshAll();
                            gridDialog.close();deleteDialog.close();editDialog.close();createDialog.close();
                            Notification.show("Erfolgreich Verzeichnis "+Tools.getId()+" Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                    });

                    return new HorizontalLayout(editButton, deleteButton);
                }).setWidth("125px").setFlexGrow(0);
                LinkGrp_grid.setItems(LinkGrp);
                LinkGrp_grid.setVisible(gridDialog.isOpened());

                cancelButton.addClickListener(Click -> {
                    editDialog.close();deleteDialog.close();createDialog.close();gridDialog.close();
                });
                createButton.addClickListener(Click -> {
                    createDialog.open();
                    createDialog.add(dialogLayout);
                    createDialog.setCloseOnOutsideClick(false);
                    createDialog.getFooter().add(sb2, cancelButton);

                    sb2.addClickListener(click -> {
                        if (tf1.isEmpty()) {
                            tf1.setValue("N/A");
                        }
                        if (tf2.isEmpty()) {
                            tf2.setValue(Double.valueOf("1"));
                        }
                        if (tf3.isEmpty()) {
                            tf3.setValue(Double.valueOf("1"));
                        }
                        if (tf4.isEmpty()) {
                            tf4.setValue(Double.valueOf("1"));
                        }
                        if (tf5.isEmpty()) {
                            tf5.setValue("N/A");
                        }
                        dataBaseUtils.addNewIdAndName_Link_Grp(tf1.getValue(), tf2.getValue(), tf3.getValue(), tf4.getValue(), tf5.getValue());
                        LinkGrp_grid.getDataProvider().refreshAll();
                        gridDialog.close();deleteDialog.close();editDialog.close();createDialog.close();
                        Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                    });
                });
                closeButton.addClickListener(Click -> {
                    editDialog.close();deleteDialog.close();createDialog.close();gridDialog.close();
                });
            });
            Link_Tile_item.addClickListener(e -> {

                List<Link_Tile> LinkTile = dataBaseUtils.getInfo_Link_Tile();
                Grid<Link_Tile> LinkTile_grid = new Grid<>();
                Label info = new Label("WARNUNG Dieser Vorgang kann nicht rückgängig gemacht werden");info.getStyle().set("color", "red");
                H2 H2 = new H2("Verzeichnis-Liste: Link Tile");H2.getStyle().set("margin", "0 auto 0 0");
                H2 H3 = new H2("");H2.getStyle().set("margin", "0 auto 0 0");
                Button sb1 = new Button("Speichern");sb1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button sb2 = new Button("Speichern");sb1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button db1 = new Button("Ja, Löschen");db1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                Button cancelButton = new Button("Nein, Abbrechen");cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button createButton = new Button("Hinzufügen");createButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);maximizeButton.addClickListener(Click -> LinkTile_grid.setAllRowsVisible(true));
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);minimizeButton.addClickListener(Click -> LinkTile_grid.setAllRowsVisible(false));
                TextField tf1 = new TextField("Name");tf1.setWidthFull();
                TextField tf2 = new TextField("Description");tf2.setWidthFull();
                NumberField tf3 = new NumberField("Sort");tf3.setWidthFull();
                NumberField tf4 = new NumberField("Tile Column Id");tf4.setWidthFull();
                VerticalLayout dialogLayout = new VerticalLayout(tf1, tf2, tf3, tf4);dialogLayout.setPadding(false);dialogLayout.setSpacing(false);dialogLayout.setSizeFull();
                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout tools = new HorizontalLayout(H3, createButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                Dialog gridDialog = new Dialog();gridDialog.open();gridDialog.setCloseOnOutsideClick(false);gridDialog.setWidthFull();gridDialog.getFooter().add();
                Dialog deleteDialog = new Dialog();deleteDialog.setHeaderTitle("Verzeichnis Löschen ?");deleteDialog.setCloseOnOutsideClick(false);deleteDialog.add("Dieser Vorgang kann nicht Rückgänig gemacht werden !");deleteDialog.setWidth(60, Unit.PERCENTAGE);
                Dialog editDialog = new Dialog();editDialog.setWidth(60, Unit.PERCENTAGE);editDialog.add(dialogLayout);editDialog.setCloseOnOutsideClick(false);editDialog.getFooter().add(sb1, cancelButton);
                Dialog createDialog = new Dialog();createDialog.add(dialogLayout);createDialog.setCloseOnOutsideClick(false);createDialog.getFooter().add(sb2, cancelButton);createDialog.setWidth(60, Unit.PERCENTAGE);

                LinkTile_grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);
                LinkTile_grid.addColumn(Link_Tile::getId).setHeader("ID").setWidth("75px").setFlexGrow(0);
                LinkTile_grid.addColumn(Link_Tile::getName).setHeader("Name");
                LinkTile_grid.addColumn(Link_Tile::getDescription).setHeader("Description");
                LinkTile_grid.addColumn(Link_Tile::getSort).setHeader("Sort");
                LinkTile_grid.addColumn(Link_Tile::getTile_Column_Id).setHeader("Tile Column Id");
                LinkTile_grid.setItems(LinkTile);

                LinkTile_grid.addComponentColumn(Tools -> {
                    Button deleteButton = new Button(VaadinIcon.TRASH.create());
                    Button editButton = new Button(VaadinIcon.EDIT.create());

                    editButton.addClickListener(Click -> {
                        editDialog.open();
                        try {
                            tf1.setValue(Tools.getName());
                        } catch (NullPointerException npe) {
                            tf1.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        try {
                            tf2.setValue(Tools.getDescription());
                        } catch (NullPointerException npe) {
                            tf2.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        try {
                            tf3.setValue(Double.valueOf(Tools.getSort()));
                        } catch (NullPointerException npe) {
                            tf3.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        try {
                            tf4.setValue(Double.valueOf(Tools.getTile_Column_Id()));
                        } catch (NullPointerException npe) {
                            tf4.setPlaceholder("Momentan ist nichts Vorhanden");
                        }

                        editDialog.add(dialogLayout);editDialog.setCloseOnOutsideClick(false);editDialog.getFooter().add(sb1, cancelButton);

                        sb1.addClickListener(click -> {
                            int i = parseInt(Tools.getId());
                            dataBaseUtils.editInfoLink_Tile(tf1.getValue(), tf2.getValue(), tf3.getValue(), tf4.getValue(), i);
                            LinkTile_grid.getDataProvider().refreshAll();
                            gridDialog.close();deleteDialog.close();editDialog.close();createDialog.close();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                        cancelButton.addClickListener(click -> {
                            gridDialog.close();deleteDialog.close();editDialog.close();createDialog.close();
                        });
                    });
                    editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
                    deleteButton.addClickListener(Click -> {
                        deleteDialog.open();
                        deleteDialog.getFooter().add(db1, cancelButton);
                        db1.addClickListener(click -> {
                            dataBaseUtils.deleteInfoLink_Tile(parseInt(Tools.getId()));
                            LinkTile_grid.getDataProvider().refreshAll();
                            gridDialog.close();deleteDialog.close();editDialog.close();createDialog.close();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                    });
                    deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_ICON);

                    return new HorizontalLayout(editButton, deleteButton);
                }).setFlexGrow(0);
                cancelButton.addClickListener(Click -> {
                    gridDialog.close();deleteDialog.close();editDialog.close();createDialog.close();
                });
                createButton.addClickListener(Click -> {
                    createDialog.open();
                    sb2.addClickListener(click -> {
                        if (tf1.isEmpty()) {
                            tf1.setValue("N/A");
                        }
                        if (tf2.isEmpty()) {
                            tf2.setValue("N/A");
                        }
                        if (tf3.isEmpty()) {
                            tf3.setValue(Double.valueOf("1"));
                        }
                        if (tf4.isEmpty()) {
                            tf4.setValue(Double.valueOf("1"));
                        }
                        dataBaseUtils.addNewIdAndName_Link_Tile(tf1.getValue(), tf2.getValue(), tf3.getValue(), tf4.getValue());
                        gridDialog.close();deleteDialog.close();editDialog.close();createDialog.close();
                        Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        LinkTile_grid.getDataProvider().refreshAll();
                    });
                });
                closeButton.addClickListener(Click -> {
                    gridDialog.close();deleteDialog.close();editDialog.close();createDialog.close();
                });
                gridDialog.add(heading, tools, LinkTile_grid);
            });
            Icon_item.addClickListener(e -> {

                Grid<dbIcon> Icon_grid = new Grid<>();
                List<dbIcon> dbIcon = dataBaseUtils.getIconImage();
                H2 H2 = new H2("Verzeichnis-Liste: Link Tile");H2.getStyle().set("margin", "0 auto 0 0");
                Button cancelButton = new Button("Nein, Abbrechen");cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);maximizeButton.addClickListener(Click -> Icon_grid.setAllRowsVisible(true));
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);minimizeButton.addClickListener(Click -> Icon_grid.setAllRowsVisible(false));
                NumberField tf3 = new NumberField("Sort");tf3.setWidthFull();
                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                Dialog gridDialog = new Dialog();gridDialog.open();gridDialog.setCloseOnOutsideClick(false);gridDialog.setWidthFull();gridDialog.getFooter().add();
                Dialog deleteDialog = new Dialog();deleteDialog.setHeaderTitle("Verzeichnis Löschen ?");deleteDialog.setCloseOnOutsideClick(false);deleteDialog.add("Dieser Vorgang kann nicht Rückgänig gemacht werden !");deleteDialog.getFooter().add(cancelButton);deleteDialog.setWidth(60, Unit.PERCENTAGE);

                Icon_grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);
                Icon_grid.addColumn(de.rub.springwebapplication.Listen.dbIcon::getId).setHeader("Id");
                Icon_grid.addComponentColumn(row -> new Image(dataBaseUtils.getLinkImage(parseInt(row.getId())), "Icon"));
                Icon_grid.addColumn(de.rub.springwebapplication.Listen.dbIcon::getContentType).setHeader("Contenttype");
                Icon_grid.setItems(dbIcon);

                cancelButton.addClickListener(Click -> deleteDialog.close());
                closeButton.addClickListener(Click -> {
                    gridDialog.close();deleteDialog.close();
                });
                gridDialog.add(heading, Icon_grid);
            });

            content.add(menuBar);
        }
    }
}
