package de.rub.springwebapplication.MitabeiterView;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.WebBrowser;
import de.rub.springwebapplication.Data.DatabaseUtils;
import de.rub.springwebapplication.Listen.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import static java.lang.Integer.parseInt;
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
                    editDialog.close();gridDialog.close();
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
                Label info = new Label("WARNUNG Dieser Vorgang kann nicht r??ckg??ngig gemacht werden");info.getStyle().set("color", "red");
                H2 H2 = new H2("Verzeichnis-Liste: Ldap");H2.getStyle().set("margin", "0 auto 0 0");
                H2 H3 = new H2("");H2.getStyle().set("margin", "0 auto 0 0");
                Button saveButton = new Button("Speichern");saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button saveButton1 = new Button("Speichern");saveButton1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button db1 = new Button("L??schen");db1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                Button cancelButton = new Button("Nein, Abbrechen");cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button createButton = new Button("Hinzuf??gen");createButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);maximizeButton.addClickListener(Click -> Ldap_grid.setAllRowsVisible(true));
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);minimizeButton.addClickListener(Click -> Ldap_grid.setAllRowsVisible(false));
                Button deleteButton2 = new Button("L??schen");deleteButton2.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                TextField tf1 = new TextField("Gruppen Name");tf1.setWidthFull();
                VerticalLayout dialogLayout = new VerticalLayout(tf1);dialogLayout.setPadding(false);dialogLayout.setSpacing(false);dialogLayout.setSizeFull();
                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout tools = new HorizontalLayout(H3, createButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                Dialog gridDialog = new Dialog();gridDialog.open();gridDialog.setCloseOnOutsideClick(false);gridDialog.setWidthFull();
                Dialog deleteDialog = new Dialog();deleteDialog.setHeaderTitle("Verzeichnis L??schen ?");deleteDialog.add(info);deleteDialog.setCloseOnOutsideClick(false);
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

                            Notification.show("Erfolgreich Ge??ndert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                    });
                    deleteButton.addClickListener(Click -> {
                        deleteDialog.open();
                        deleteDialog.getFooter().add(deleteButton2, cancelButton);
                        deleteButton2.addClickListener(click -> {
                            dataBaseUtils.deleteInfoLDAP(parseInt(Tools.getId()));
                            gridDialog.close();deleteDialog.close();editDialog.close();

                            Ldap_grid.getDataProvider().refreshAll();

                            Notification.show("Erfolgreich Gel??scht", 5000, Notification.Position.TOP_CENTER);
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
                    gridDialog.close();deleteDialog.close();editDialog.close();createDialog.close();
                });

                gridDialog.add(heading, tools, Ldap_grid);
            });
            Link_item.addClickListener(e -> {

                MenuBar Settings = new MenuBar();Settings.addThemeVariants(MenuBarVariant.LUMO_ICON, MenuBarVariant.LUMO_TERTIARY_INLINE, MenuBarVariant.LUMO_LARGE);
                MenuItem item = Settings.addItem(VaadinIcon.COG.create());
                SubMenu subMenu = item.getSubMenu();
                MenuItem add = subMenu.addItem("Hinzuf??gen");
                MenuItem edit = subMenu.addItem("Bearbeiten");
                MenuItem delete = subMenu.addItem("Delete");
                Grid<de.rub.springwebapplication.Listen.Link> Link_grid = new Grid<>();
                List<de.rub.springwebapplication.Listen.Link> list = dataBaseUtils.getInfo_Link();
                List<de.rub.springwebapplication.Listen.Link_grp> link_grp_list = dataBaseUtils.getInfo_Link_Grp();
                List<de.rub.springwebapplication.Listen.Ldap> ldap_grp_list = dataBaseUtils.getInfo_Ldap();
                Label info = new Label("WARNUNG Dieser Vorgang kann nicht r??ckg??ngig gemacht werden");info.getStyle().set("color", "red");
                H2 H2 = new H2("Verzeichnis-Liste: Link");H2.getStyle().set("margin", "0 auto 0 0");
                H2 H3 = new H2("");H2.getStyle().set("margin", "0 auto 0 0");
                Button saveButton1 = new Button("Speichern");saveButton1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button saveButton2 = new Button("Speichern");saveButton2.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button saveButton3 = new Button("Speichern");saveButton3.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button saveButton4 = new Button("Speichern");saveButton3.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button deleteButton1 = new Button("L??schen");deleteButton1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                Button deleteButton2 = new Button("L??schen");deleteButton2.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);deleteButton2.setVisible(false);
                Button cancelButton = new Button("Nein, Abbrechen");cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button cancelButton1 = new Button("Nein, Abbrechen");cancelButton1.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button cancelButton2 = new Button("Nein, Abbrechen");cancelButton2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);maximizeButton.addClickListener(Click -> Link_grid.setAllRowsVisible(true));
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);minimizeButton.addClickListener(Click -> Link_grid.setAllRowsVisible(false));
                Button addLinkGroup = new Button("Link Group Hinzuf??gen");addLinkGroup.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);addLinkGroup.setWidthFull();
                Button createLinkGroup = new Button(VaadinIcon.PLUS.create());createLinkGroup.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button createLink = new Button(VaadinIcon.PLUS.create());createLink.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button ReturnCreateLink = new Button(VaadinIcon.ARROW_LEFT.create());ReturnCreateLink.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);ReturnCreateLink.setVisible(false);
                TextField textField1 = new TextField("Linktext");textField1.setWidthFull();
                TextField textField4 = new TextField("Description");textField4.setWidthFull();
                TextField createLinkField = new TextField("Neuen Link erstellen");createLinkField.setWidthFull();createLinkField.setVisible(false);
                ComboBox<de.rub.springwebapplication.Listen.Link_grp> textField2 = new ComboBox<>("Link Group");textField2.setItems(link_grp_list);textField2.setItemLabelGenerator(de.rub.springwebapplication.Listen.Link_grp::getGrp_Linktext);textField2.setWidthFull();
                ComboBox<de.rub.springwebapplication.Listen.Ldap> LdapField = new ComboBox<>("Ldap Group");LdapField.setItems(ldap_grp_list);LdapField.setItemLabelGenerator(de.rub.springwebapplication.Listen.Ldap::getContent);LdapField.setWidthFull();
                ComboBox<de.rub.springwebapplication.Listen.Link> textField5 = new ComboBox<>("Vorhandenen Link ausw??hlen");textField5.setItems(list);textField5.setItemLabelGenerator(de.rub.springwebapplication.Listen.Link::getUrl_active);textField5.setAllowCustomValue(false);textField5.setWidthFull();
                Checkbox textField6 = new Checkbox();textField6.setLabel("URL Inaktiv");textField6.setWidthFull();
                Checkbox textField7 = new Checkbox();textField7.setLabel("URL Aktivieren");textField7.setWidthFull();
                Checkbox textField9 = new Checkbox();textField9.setLabel("Neuen Tab ??ffnen");textField9.setWidthFull();
                NumberField textField3 = new NumberField("Sort(Number Only)");textField3.setWidthFull();
                NumberField textField8 = new NumberField("Authenticator Level");textField8.setWidthFull();
                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout tools = new HorizontalLayout(H3, Settings);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout Checkbox = new HorizontalLayout(textField6, textField7, textField9);
                HorizontalLayout LinkGroup = new HorizontalLayout(textField2, createLinkGroup);LinkGroup.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);LinkGroup.setWidthFull();
                HorizontalLayout Link = new HorizontalLayout(textField5, createLinkField, ReturnCreateLink, createLink);Link.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);Link.setWidthFull();
                HorizontalLayout Ldap_Grp = new HorizontalLayout(LdapField);Ldap_Grp.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);Ldap_Grp.setWidthFull();
                HorizontalLayout createLayoutFooter = new HorizontalLayout(saveButton3, cancelButton1);
                VerticalLayout dialogLayout = new VerticalLayout(textField1, LinkGroup, Ldap_Grp, Link, textField3, textField4, textField8, Checkbox);dialogLayout.setPadding(false);dialogLayout.setSpacing(false);dialogLayout.setWidthFull();

                Dialog gridDialog = new Dialog();gridDialog.open();gridDialog.setCloseOnOutsideClick(false);gridDialog.setWidthFull();gridDialog.setCloseOnEsc(false);
                Dialog deleteDialog = new Dialog();deleteDialog.setHeaderTitle("Verzeichnis L??schen ?");deleteDialog.add("Dieser Vorgang kann nicht R??ckg??nig gemacht werden !");deleteDialog.getFooter().add(deleteButton1, cancelButton);deleteDialog.setCloseOnOutsideClick(false);deleteDialog.setCloseOnEsc(false);
                Dialog editDialog = new Dialog();editDialog.setCloseOnOutsideClick(false);editDialog.setHeaderTitle("Verzeichnis bearbeiten");editDialog.setWidth(60, Unit.PERCENTAGE);editDialog.add(dialogLayout);editDialog.getFooter().add(createLayoutFooter);editDialog.setCloseOnEsc(false);
                Dialog createDialog = new Dialog();createDialog.setCloseOnOutsideClick(false);createDialog.setHeaderTitle("Verzeichnis erstellen");createDialog.add(dialogLayout);createDialog.getFooter().add(createLayoutFooter);createDialog.setWidth(60, Unit.PERCENTAGE);createDialog.setCloseOnEsc(false);

                TextField tf1 = new TextField("Linktext Gruppe");tf1.setWidthFull();
                TextField tf5 = new TextField("Beschreibung");tf5.setWidthFull();
                NumberField tf2 = new NumberField("Symbol");tf2.setWidthFull();
                NumberField tf3 = new NumberField("Kachel");tf3.setWidthFull();
                NumberField tf4 = new NumberField("Sortieren");tf4.setWidthFull();

                VerticalLayout createLinkGroupDialogLayout = new VerticalLayout(tf1, tf2, tf3, tf4, tf5);createLinkGroupDialogLayout.setPadding(false);createLinkGroupDialogLayout.setSpacing(false);createLinkGroupDialogLayout.setWidthFull();

                Dialog createLinkGroupDialog = new Dialog();createLinkGroupDialog.setWidth(60, Unit.PERCENTAGE);createLinkGroupDialog.add(createLinkGroupDialogLayout);createLinkGroupDialog.setCloseOnOutsideClick(false);createLinkGroupDialog.getFooter().add(saveButton4, cancelButton2);

                Link_grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
                Link_grid.setAllRowsVisible(false);
                Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getId).setHeader("ID").setSortable(true).setAutoWidth(true);
                Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getLinktext).setHeader("Linktext").setSortable(true).setAutoWidth(true);
                Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getLink_grp_id).setHeader("Grp Linktext").setSortable(true).setAutoWidth(true);
                Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getSort).setHeader("Sort").setSortable(true).setAutoWidth(true);
                Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getDescription).setHeader("Description").setSortable(true).setAutoWidth(true);
                Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getUrl_active).setHeader("Link").setSortable(true).setAutoWidth(true).setKey("Link");
                Link_grid.setItems(list);

                cancelButton.addClickListener(Click -> {
                    gridDialog.close();deleteDialog.close();editDialog.close();createDialog.close();
                });
                closeButton.addClickListener(Click -> {
                    gridDialog.close();deleteDialog.close();editDialog.close();createDialog.close();
                });

                createLinkGroup.addClickListener(click -> {
                    createLinkGroupDialog.open();
                    createDialog.close();
                });
                createLink.addClickListener(click -> {
                    textField5.setVisible(false);
                    createLinkField.setValue("");
                    createLinkField.setPlaceholder("Bitte geben sie einen Link ein");
                    createLink.setVisible(false);
                    createLinkField.setVisible(true);
                    ReturnCreateLink.setVisible(true);
                });
                ReturnCreateLink.addClickListener(click -> {
                    textField5.setVisible(true);
                    createLinkField.setVisible(false);
                    ReturnCreateLink.setVisible(false);
                    createLink.setVisible(true);
                });

                add.addClickListener(Click -> {
                    gridDialog.close();
                    delete.setEnabled(true);
                    edit.setEnabled(true);

                    Link_grid.removeAllColumns();
                    Link_grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
                    Link_grid.setAllRowsVisible(false);
                    Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getId).setHeader("ID").setSortable(true).setAutoWidth(true);
                    Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getLinktext).setHeader("Linktext").setSortable(true).setAutoWidth(true);
                    Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getLink_grp_id).setHeader("Grp Linktext").setSortable(true).setAutoWidth(true);
                    Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getSort).setHeader("Sort").setSortable(true).setAutoWidth(true);
                    Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getDescription).setHeader("Description").setSortable(true).setAutoWidth(true);
                    Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getUrl_active).setHeader("Link").setSortable(true).setAutoWidth(true).setKey("Link");
                    Link_grid.setItems(list);

                    deleteButton2.setVisible(false);
                    Link_grid.setSelectionMode(Grid.SelectionMode.NONE);
                    createDialog.open();

                });
                edit.addClickListener(Click -> {
                    add.setEnabled(true);
                    edit.setEnabled(false);
                    delete.setEnabled(true);
                    deleteButton2.setVisible(false);
                    Link_grid.removeAllColumns();
                    Link_grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
                    Link_grid.setAllRowsVisible(false);
                    Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getId).setHeader("ID").setSortable(true).setAutoWidth(true);
                    Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getLinktext).setHeader("Linktext").setSortable(true).setAutoWidth(true);
                    Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getLink_grp_id).setHeader("Grp Linktext").setSortable(true).setAutoWidth(true);
                    Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getSort).setHeader("Sort").setSortable(true).setAutoWidth(true);
                    Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getDescription).setHeader("Description").setSortable(true).setAutoWidth(true);
                    Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getUrl_active).setHeader("Link").setSortable(true).setAutoWidth(true).setKey("Link");
                    Link_grid.addComponentColumn(Tools -> {
                        int i = parseInt(Tools.getId());
                        Button editButton = new Button(VaadinIcon.COG.create());editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
                        editButton.addClickListener(click -> {
                            editDialog.open();
                            try {
                                textField1.setValue(Tools.getLinktext());
                            } catch (NullPointerException npe) {
                                textField1.setPlaceholder("Momentan ist nichts Vorhanden");
                            }
                            try {
                                textField2.setPlaceholder(Tools.getLink_grp_id());
                            } catch (NullPointerException npe) {
                                textField2.setPlaceholder("Momentan ist nichts Vorhanden");
                            }
                            try {
                                textField3.setValue(Double.valueOf(Tools.getSort()));
                            } catch (NullPointerException npe) {
                                textField3.setPlaceholder("Momentan ist nichts Vorhanden");
                            }
                            try {
                                textField4.setValue((Tools.getDescription()));
                            } catch (NullPointerException npe) {
                                textField4.setPlaceholder("Momentan ist nichts Vorhanden");
                            }
                            try {
                                textField5.setPlaceholder(Tools.getUrl_active());
                            } catch (NullPointerException npe) {
                                textField5.setPlaceholder("Momentan ist nichts Vorhanden");
                            }
                            try {
                                if (Objects.equals(Tools.getUrl_inactive(), String.valueOf(1))) {
                                    textField6.setValue(true);
                                }
                                else {
                                    textField6.setValue(false);
                                }
                            } catch (NullPointerException npe) {
                                npe.printStackTrace();
                            }
                            try {
                                if (Objects.equals(Tools.getActive(), String.valueOf(1))) {
                                    textField7.setValue(true);
                                }
                                else {
                                    textField7.setValue(false);
                                }
                            } catch (NullPointerException npe) {
                                npe.printStackTrace();
                            }
                            try {
                                textField8.setValue(Double.valueOf(Tools.getAuth_level()));
                            } catch (NullPointerException npe) {
                                textField8.setPlaceholder("Momentan ist nichts Vorhanden");
                            }
                            try {
                                if (Objects.equals(Tools.getNewtab(), String.valueOf(1))) {
                                    textField9.setValue(true);
                                }
                                else {
                                    textField9.setValue(false);
                                }
                            } catch (NullPointerException npe) {
                                npe.printStackTrace();
                            }

                            editDialog.add(dialogLayout);editDialog.getFooter().add(saveButton1, cancelButton);

                            saveButton1.addClickListener(klick -> {
                                int tf6_info;
                                int tf7_info;
                                int tf9_info;

                                if (textField2.isEmpty()) {
                                    Notification.show("Link Unvollst??ndig").addThemeVariants(NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_ERROR);
                                    return;
                                }
                                if (textField5.isEmpty()) {
                                    Notification.show("Link Unvollst??ndig").addThemeVariants(NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_ERROR);
                                    return;
                                }
                                if (textField6.getValue().equals(true)) {
                                    tf6_info = 1;
                                } else {
                                    tf6_info = 0;
                                }
                                if (textField7.getValue().equals(true)) {
                                    tf7_info = 1;
                                } else {
                                    tf7_info = 0;
                                }
                                if (textField9.getValue().equals(true)) {
                                    tf9_info = 1;
                                } else {
                                    tf9_info = 0;
                                }

                                dataBaseUtils.editInfoLink(i, textField1.getValue(), Integer.valueOf(textField2.getValue().getId()), textField3.getValue(), textField4.getValue(), textField5.getValue().getUrl_active(), tf6_info, tf7_info, textField8.getValue(), tf9_info);
                                textField1.setValue("Wird Ge??ndert");
                                gridDialog.close();deleteDialog.close();editDialog.close();
                                Link_grid.getDataProvider().refreshAll();
                                Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                            });
                        });
                        return new HorizontalLayout(editButton);
                    }).setFlexGrow(0);
                    Link_grid.setItems(list);
                });
                delete.addClickListener(Click -> {

                    Link_grid.removeAllColumns();
                    delete.setEnabled(false);
                    add.setEnabled(true);
                    edit.setEnabled(true);
                    deleteButton2.setVisible(true);

                    Link_grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
                    Link_grid.setAllRowsVisible(false);
                    Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getId).setHeader("ID").setSortable(true).setAutoWidth(true);
                    Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getLinktext).setHeader("Linktext").setSortable(true).setAutoWidth(true);
                    Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getLink_grp_id).setHeader("Grp Linktext").setSortable(true).setAutoWidth(true);
                    Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getSort).setHeader("Sort").setSortable(true).setAutoWidth(true);
                    Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getDescription).setHeader("Description").setSortable(true).setAutoWidth(true);
                    Link_grid.addColumn(de.rub.springwebapplication.Listen.Link::getUrl_active).setHeader("Link").setSortable(true).setAutoWidth(true).setKey("Link");
                    Link_grid.addComponentColumn(Tools -> {

                        Checkbox deleteBox = new Checkbox();

                        deleteBox.addClickListener(click -> deleteButton2.addClickListener(klick -> {
                                deleteDialog.open();
                            deleteButton1.addClickListener(event -> {
                                if (deleteBox.getValue().equals(true)) {
                                    int i = Integer.parseInt(Tools.getId());
                                    dataBaseUtils.deleteInfoLink(i);
                                    Notification.show("Alle ausgew??hlten Rows wurden entfernt");
                                    gridDialog.close();deleteDialog.close();editDialog.close();createDialog.close();
                                }
                            });
                        }));
                        return new HorizontalLayout(deleteBox);
                    }).setFlexGrow(0);
                    Link_grid.setItems(list);
                });

                saveButton3.addClickListener(click -> {
                    if (createDialog.isOpened()) {
                        if (textField1.isEmpty()) {
                            textField1.setValue("N/A");
                        }
                        if (textField3.isEmpty()) {
                            textField3.setValue(Double.valueOf("1"));
                        }
                        if (textField4.isEmpty()) {
                            textField4.setValue("N/A");
                        }
                        if (textField6.isEmpty()) {
                            textField6.setValue(false);
                        }
                        if (textField7.isEmpty()) {
                            textField7.setValue(false);
                        }
                        if (textField8.isEmpty()) {
                            textField8.setValue(Double.valueOf("1"));
                        }
                        if (textField9.isEmpty()) {
                            textField9.setValue(false);
                        }
                        if (textField2.isEmpty()) {
                            Notification.show("Sie haben im Feld 'Link Group' keine Angabe gemacht, bitte korrigieren").addThemeVariants(NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_ERROR);
                            return;
                        }
                        if (!textField5.isVisible()) {

                            dataBaseUtils.addNewIdAndName_Link(textField1.getValue(), String.valueOf(textField2.getValue().getId()), textField3.getValue(), textField4.getValue(), createLinkField.getValue(), textField6.getValue(), textField7.getValue(), textField8.getValue(), textField9.getValue());
                        } else {
                            if (textField5.isEmpty()) {
                                Notification.show("Sie haben im Feld 'URL Active' keine Angabe gemacht, bitte korrigieren").addThemeVariants(NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_ERROR);
                                return;
                            }
                            dataBaseUtils.addNewIdAndName_Link(textField1.getValue(), String.valueOf(textField2.getValue().getId()), textField3.getValue(), textField4.getValue(), textField5.getValue().getUrl_active(), textField6.getValue(), textField7.getValue(), textField8.getValue(), textField9.getValue());
                        }
                        Link_grid.getDataProvider().refreshAll();
                        gridDialog.close();deleteDialog.close();editDialog.close();createDialog.close();
                        Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        gridDialog.open();
                }
                });
                saveButton4.addClickListener(click -> {
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
                    gridDialog.close();createLinkGroupDialog.close();deleteDialog.close();editDialog.close();createDialog.open();
                    Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                    textField2.getDataProvider().refreshAll();
                });

                cancelButton1.addClickListener(click -> {
                    gridDialog.open();deleteDialog.close();editDialog.close();createDialog.close();
                });
                cancelButton2.addClickListener(click -> {
                    gridDialog.close();createLinkGroupDialog.close();deleteDialog.close();editDialog.close();createDialog.open();
                });

                gridDialog.add(heading, tools, Link_grid);
                gridDialog.getFooter().add(deleteButton2);

            });
            Ldap_Role_item.addClickListener(e -> {
                List<de.rub.springwebapplication.Listen.LDAP_ROLE> LdapRole = dataBaseUtils.getAllInfos_LDAP_ROLE();
                Grid<LDAP_ROLE> LdapRole_grid = new Grid<>();
                Label info = new Label("WARNUNG Dieser Vorgang kann nicht r??ckg??ngig gemacht werden");info.getStyle().set("color", "red");
                H2 H2 = new H2("Verzeichnis-Liste: Ldap Role");H2.getStyle().set("margin", "0 auto 0 0");
                H2 H3 = new H2("");H2.getStyle().set("margin", "0 auto 0 0");
                Button sb1 = new Button("Speichern");sb1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button sb2 = new Button("Speichern");sb2.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button db1 = new Button("Ja, L??schen");db1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                Button cancelButton = new Button("Nein, Abbrechen");cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button createButton = new Button("Hinzuf??gen");createButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);maximizeButton.addClickListener(Click -> LdapRole_grid.setAllRowsVisible(true));
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);  minimizeButton.addClickListener(Click -> LdapRole_grid.setAllRowsVisible(false));
                TextField tf1 = new TextField("Role Name");tf1.setWidthFull();
                VerticalLayout dialogLayout = new VerticalLayout(tf1);dialogLayout.setPadding(false);dialogLayout.setSpacing(false);dialogLayout.setSizeFull();
                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout tools = new HorizontalLayout(H3, createButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                Dialog gridDialog = new Dialog();gridDialog.open();gridDialog.setCloseOnOutsideClick(false);gridDialog.setWidthFull();gridDialog.getFooter().add();
                Dialog deleteDialog = new Dialog();deleteDialog.setWidth(60, Unit.PERCENTAGE);deleteDialog.setHeaderTitle("Verzeichnis L??schen ?");deleteDialog.setCloseOnOutsideClick(false);deleteDialog.add("Dieser Vorgang kann nicht R??ckg??nig gemacht werden !");
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
                            Notification.show("Erfolgreich Verzeichnis "+Tools.getId()+" gel??scht", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
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
                Label info = new Label("WARNUNG Dieser Vorgang kann nicht r??ckg??ngig gemacht werden");info.getStyle().set("color", "red");
                H2 H2 = new H2("Verzeichnis-Liste: Link Group");H2.getStyle().set("margin", "0 auto 0 0");
                H2 H3 = new H2("");H2.getStyle().set("margin", "0 auto 0 0");
                Button sb1 = new Button("Speichern");sb1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button sb2 = new Button("Speichern");sb2.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button sb3 = new Button("Speichern");sb3.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button db1 = new Button("Ja, L??schen");db1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                Button cancelButton = new Button("Nein, Abbrechen");cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button createButton = new Button("Hinzuf??gen");createButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
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
                Dialog gridDialog = new Dialog();gridDialog.open();gridDialog.setCloseOnOutsideClick(false);gridDialog.setWidthFull();
                Dialog deleteDialog = new Dialog();deleteDialog.setWidth(60, Unit.PERCENTAGE);
                Dialog editDialog = new Dialog();editDialog.setWidth(60, Unit.PERCENTAGE);
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
                        editDialog.add(dialogLayout);
                        editDialog.getFooter().add(sb2, cancelButton);
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
                        deleteDialog.add("Dieser Vorgang kann nicht R??ckg??nig gemacht werden !");
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
                    createDialog.getFooter().add(sb3, cancelButton);

                    sb3.addClickListener(click -> {
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

                gridDialog.add(heading, tools, LinkGrp_grid);
            });
            Link_Tile_item.addClickListener(e -> {

                List<Link_Tile> LinkTile = dataBaseUtils.getInfo_Link_Tile();
                Grid<Link_Tile> LinkTile_grid = new Grid<>();
                Label info = new Label("WARNUNG Dieser Vorgang kann nicht r??ckg??ngig gemacht werden");info.getStyle().set("color", "red");
                H2 H2 = new H2("Verzeichnis-Liste: Link Tile");H2.getStyle().set("margin", "0 auto 0 0");
                H2 H3 = new H2("");H2.getStyle().set("margin", "0 auto 0 0");
                Button sb1 = new Button("Speichern");sb1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button sb2 = new Button("Speichern");sb1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button db1 = new Button("Ja, L??schen");db1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                Button cancelButton = new Button("Nein, Abbrechen");cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button createButton = new Button("Hinzuf??gen");createButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
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
                Dialog deleteDialog = new Dialog();deleteDialog.setHeaderTitle("Verzeichnis L??schen ?");deleteDialog.setCloseOnOutsideClick(false);deleteDialog.add("Dieser Vorgang kann nicht R??ckg??nig gemacht werden !");deleteDialog.setWidth(60, Unit.PERCENTAGE);
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
                Dialog deleteDialog = new Dialog();deleteDialog.setHeaderTitle("Verzeichnis L??schen ?");deleteDialog.setCloseOnOutsideClick(false);deleteDialog.add("Dieser Vorgang kann nicht R??ckg??nig gemacht werden !");deleteDialog.getFooter().add(cancelButton);deleteDialog.setWidth(60, Unit.PERCENTAGE);

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
