package de.rub.springwebapplication.MitabeiterView;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.WebBrowser;
import de.rub.springwebapplication.Data.DatabaseUtils;
import de.rub.springwebapplication.Tabs.ItemUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.SQLException;

@Route("")
public class MitabeiterView extends Div {
    public DatabaseUtils dataBaseUtils;
    public ItemUtils tab_item;
    private final Tab Startseite_WebClient;
    private final Tab Bearbeiten;
    private final VerticalLayout content;
    public MitabeiterView() throws IOException {

        final WebBrowser webBrowser = UI.getCurrent().getSession().getBrowser();
        System.out.println("IP: " + webBrowser.getAddress() + " " + "Opened: " + getClass());

        tab_item = new ItemUtils();
        dataBaseUtils = new DatabaseUtils();
        content = new VerticalLayout();
        Startseite_WebClient = new Tab(VaadinIcon.HOME.create(), new Span("Startseite WebClient"));
        Bearbeiten = new Tab(VaadinIcon.EDIT.create(), new Span("Datenbank Tabellen Bearbeiten"));

        Tabs tabs = new Tabs(Startseite_WebClient, Bearbeiten);
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
                try {
                    tab_item.Infobox();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            Ldap_item.addClickListener(e -> {
                try {
                    tab_item.Ldap();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            Link_item.addClickListener(e -> {
                try {
                    tab_item.Link();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            Ldap_Role_item.addClickListener(e -> {
                try {
                    tab_item.Ldap_Role();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            Link_Grp_item.addClickListener(e -> {
                try {
                    tab_item.Link_Group();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            Link_Tile_item.addClickListener(e -> {
                try {
                    tab_item.Link_Tile();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            Icon_item.addClickListener(e -> {
                try {
                    tab_item.Icon();
                } catch (IOException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
            content.add(menuBar);
        }
    }
}
