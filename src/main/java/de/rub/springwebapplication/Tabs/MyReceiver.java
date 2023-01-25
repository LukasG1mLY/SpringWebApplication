package de.rub.springwebapplication.Tabs;

import com.vaadin.flow.component.upload.Receiver;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class MyReceiver implements Receiver {
    private byte[] iconData;

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        iconData = new byte[0];
        return new ByteArrayOutputStream() {
            @Override
            public void write(int b) {
                byte[] newData = new byte[iconData.length + 1];
                System.arraycopy(iconData, 0, newData, 0, iconData.length);
                newData[iconData.length] = (byte) b;
                iconData = newData;
            }

            @Override
            public void write(byte[] b, int off, int len) {
                byte[] newData = new byte[iconData.length + len];
                System.arraycopy(iconData, 0, newData, 0, iconData.length);
                System.arraycopy(b, off, newData, iconData.length, len);
                iconData = newData;
            }
        };
    }

    public byte[] getIconData() {
        return iconData;
    }
}
