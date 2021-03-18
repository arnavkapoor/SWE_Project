import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
abstract class Emoticon {
    protected String emoticon;
    abstract void addEmotion();
    public String getEmoticon(){
        return emoticon;
    }
}
