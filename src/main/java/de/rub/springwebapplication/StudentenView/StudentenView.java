package de.rub.springwebapplication.StudentenView;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.WebBrowser;
import de.rub.springwebapplication.Data.DataBaseUtils;
import de.rub.springwebapplication.Login.Redirect;

import java.io.IOException;


@PageTitle("StudentView")
@Route("StudentView")
public class StudentenView extends Div {

    public DataBaseUtils dataBaseUtils;
    private final Tab Startseite_WebClient;
    private final Tab Logout;
    private final VerticalLayout content;

    public StudentenView() throws IOException {

        final WebBrowser webBrowser = UI.getCurrent().getSession().getBrowser();
        System.out.println("IP: " + webBrowser.getAddress() + " " + "Opened: " + getClass());


        dataBaseUtils = new DataBaseUtils();
        content = new VerticalLayout();
        content.setSpacing(true);
        Startseite_WebClient = new Tab(VaadinIcon.HOME.create(), new Span("Startseite WebClient"));
        Logout = new Tab(VaadinIcon.ARROW_LEFT.create(), new Span("Logout"));

        Tabs tabs = new Tabs(Startseite_WebClient, Logout);
        tabs.addSelectedChangeListener(event -> setContentS(event.getSelectedTab()));

        setContentS(tabs.getSelectedTab());

        add(tabs, content);

    }


    private void setContentS(Tab tab) {
        content.removeAll();

        if (tab.equals(Startseite_WebClient)) {
            content.add(new Image("images/LogoeCampus_5_2013.jpg", "images/RUB.png"));
            content.add(new H1("Startseite WebClient"));
            content.add(new H2("Informationen über die Ruhr Universität Bochum"));
            Text InfoField;
            content.add(InfoField = new Text(""));
            InfoField.setText(dataBaseUtils.getInfoStudent());
            content.setAlignItems(FlexComponent.Alignment.CENTER);
            content.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        } else if (tab.equals(Logout)) {

            UI.getCurrent().navigate(Redirect.class);

        }
    }
}
