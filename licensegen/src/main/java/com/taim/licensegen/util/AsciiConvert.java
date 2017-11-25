package com.taim.licensegen.util;

/**
 * Created by tjin on 2016-12-02.
 */
public class AsciiConvert {
    public static String MungeTextIntoAscii(String text) {
        StringBuilder out = new StringBuilder();
        for(char c : text.toCharArray()) {
            if(c < '\u007f') {
                out.append(c);
            } else if(c >= '\u00c0' && c < '\u0100') {
                // Convert diacriticals and ligatures in this block to ASCII
                switch(c) {
                    case '\u00c0':
                    case '\u00c1':
                    case '\u00c2':
                    case '\u00c3':
                    case '\u00c4':
                    case '\u00c5':
                        out.append("A");
                        break;
                    case '\u00c6':
                        out.append("AE");
                        break;
                    case '\u00c7':
                        out.append("C");
                        break;
                    case '\u00c8':
                    case '\u00c9':
                    case '\u00ca':
                    case '\u00cb':
                        out.append("E");
                        break;
                    case '\u00cc':
                    case '\u00cd':
                    case '\u00ce':
                    case '\u00cf':
                        out.append("I");
                        break;
                    case '\u00d0':
                        out.append("D");
                        break;
                    case '\u00d1':
                        out.append("N");
                        break;
                    case '\u00d2':
                    case '\u00d3':
                    case '\u00d4':
                    case '\u00d5':
                    case '\u00d6':
                    case '\u00d8':
                        out.append("O");
                        break;
                    case '\u00d9':
                    case '\u00da':
                    case '\u00db':
                    case '\u00dc':
                        out.append("U");
                        break;
                    case '\u00dd':
                        out.append("Y");
                        break;
                    case '\u00df':
                        out.append("sz");   // German long S.  See Wikipedia entry.
                        break;
                    case '\u00e0':
                    case '\u00e1':
                    case '\u00e2':
                    case '\u00e3':
                    case '\u00e4':
                    case '\u00e5':
                        out.append("a");
                        break;
                    case '\u00e6':
                        out.append("ae");
                        break;
                    case '\u00e7':
                        out.append("c");
                        break;
                    case '\u00e8':
                    case '\u00e9':
                    case '\u00ea':
                    case '\u00eb':
                        out.append("e");
                        break;
                    case '\u00ec':
                    case '\u00ed':
                    case '\u00ee':
                    case '\u00ef':
                        out.append("i");
                        break;
                    case '\u00f0':
                        out.append("d");
                        break;
                    case '\u00f1':
                        out.append("n");
                        break;
                    case '\u00f2':
                    case '\u00f3':
                    case '\u00f4':
                    case '\u00f5':
                    case '\u00f6':
                    case '\u00f8':
                        out.append("o");
                        break;
                    case '\u00f9':
                    case '\u00fa':
                    case '\u00fb':
                    case '\u00fc':
                        out.append("u");
                        break;
                    case '\u00fd':
                        out.append("y");
                        break;
                    case '\u00ff':
                        out.append("y");
                        break;
                    default:
                        out.append("-");
                }
            } else {
                out.append("-");
            }
        }
        return out.toString();
    }
}
