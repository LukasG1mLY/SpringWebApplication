package de.rub.springwebapplication.MitabeiterView;


import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import de.rub.springwebapplication.Data.SQLUtils;
import de.rub.springwebapplication.Listen.Icon;

@Route("images")
public class ImageViewer extends VerticalLayout {

    private StreamResource createImageResource(Blob blob) {
        byte[] byteArray = null;
        try {
            byteArray = blob.getBytes(1, (int) blob.length());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        byte[] finalByteArray = byteArray;
        return new StreamResource("image.png", () -> {
            assert finalByteArray != null;
            return new ByteArrayInputStream(finalByteArray);
        });
    }

    public ImageViewer() throws SQLException {
        SQLUtils sqlUtils = new SQLUtils();
        sqlUtils.setDatabase("jdbc:oracle:thin:@ora-uv.uv.rub.de/multi.uv", "portaltest", "DfkLSr4ETest");
        ResultSet resultSet = sqlUtils.onQuery("SELECT ID, CONTENTTYPE, ICON FROM ICON");
        List<Icon> icons = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String id = resultSet.getString("ID");
                String contentType = resultSet.getString("CONTENTTYPE");
                Blob iconData = resultSet.getBlob("ICON");
                Icon icon = new Icon(id, contentType, iconData);
                icons.add(icon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Grid<Icon> grid = new Grid<>();
        grid.addColumn(Icon::getId).setHeader("ID");
        grid.addColumn(Icon::getContentType).setHeader("Content Type");
        grid.addComponentColumn(icon -> {
            StreamResource resource = createImageResource(icon.getIconData());
            Image image = new Image(resource, "Image not found");
            return image;
        }).setHeader("Icon");

        grid.setItems(icons);
        add(grid);
    }
}