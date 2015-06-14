package elisa.main;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

/**
 * @author Adam Goscicki
 */
public class TextAttributes {
    public final static AttributeSet USER_ATTRIBUTE_SET;
    public final static AttributeSet BOT_ATTRIBUTE_SET;

    static {
        SimpleAttributeSet userTempSet = new SimpleAttributeSet();
        StyleConstants.setAlignment(userTempSet, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setForeground(userTempSet, Color.BLUE);
        StyleConstants.setFontSize(userTempSet, 20);
        USER_ATTRIBUTE_SET = userTempSet;

        SimpleAttributeSet botTempSet = new SimpleAttributeSet();
        StyleConstants.setAlignment(botTempSet, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(botTempSet, Color.BLACK);
        StyleConstants.setFontSize(botTempSet, 20);
        BOT_ATTRIBUTE_SET = botTempSet;
    }
}
